(ns es-cqrs-clj.event-consumer
  (:require
   [es-cqrs-clj.eventlog-protocol :as elp]))

(defn do-get-before [eventlog topic timestamp]
  (drop-while #(> (:timestamp %) timestamp) (elp/get-events eventlog topic)))

(defn do-get-after [eventlog topic timestamp]
  (take-while #(> (:timestamp %) timestamp) (elp/get-events eventlog topic)))

(defn do-get-between [eventlog topic start-timestamp end-timestamp]
  (->> (elp/get-events eventlog topic)
       (drop-while #(>= (:timestamp %) end-timestamp))
       (take-while #(> (:timestamp %) start-timestamp))))

(defprotocol EventConsumerProtocol
  (get-before [this topic timestamp] "Get all events from TOPIC that have been committed before TIMESTAMP.")
  (get-after [this topic timestamp] "Get all events from TOPIC that have been committed after TIMESTAMP.")
  (get-between [this topic start-timestamp end-timestamp] "Get all events from TOPIC that have been committed between START-TIMESTAMP and END-TIMESTAMP."))

(defrecord EventConsumer [eventlog]
  EventConsumerProtocol

  (get-before [this topic timestamp]
    (do-get-before eventlog topic timestamp))
  (get-after [this topic timestamp]
    (do-get-after eventlog topic timestamp))
  (get-between [this topic start-timestamp end-timestamp]
    (do-get-between eventlog topic start-timestamp end-timestamp)))
