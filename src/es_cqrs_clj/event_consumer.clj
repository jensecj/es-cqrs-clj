(ns es-cqrs-clj.event-consumer
  (:require
   [es-cqrs-clj.inmemory-eventlog :as el]
   [es-cqrs-clj.eventlog-protocol :as elp]
   [es-cqrs-clj.eventsource :as E]
   [es-cqrs-clj.eventsource-protocol :as esp]))

(def^{:private true} local_store (atom {}))
(reset! local_store {})

(defn do-get-before-id [eventlog topic event-id])
(defn do-get-after-id [eventlog topic event-id])
(defn do-get-before [eventlog topic timestamp])
(defn do-get-after [eventlog topic timestamp]
  (filter #(> (:timestamp %) timestamp) (elp/get-events eventlog topic)))
(defn do-get-between [eventlog topic start-timestamp end-timestamp])

(defprotocol EventConsumerProtocol
  (get-before-id [this topic event-id] "Get all events that have been committed before event with EVENT-ID")
  (get-after-id [this topic event-id] "Get all events that have been committed after event with EVENT-ID")
  (get-before [this topic timestamp] "Get all events that have been committed before TIMESTAMP")
  (get-after [this topic timestamp] "Get all events that have been committed after TIMESTAMP")
  (get-between [this topic start-timestamp end-timestamp]) "Get all events that have been committed between START-TIMESTAMP and END-TIMESTAMP")

(defrecord EventConsumer [eventlog]
  EventConsumerProtocol

  (get-before-id [this topic event-id]
    (do-get-before-id eventlog topic event-id))
  (get-after-id [this topic event-id]
    (do-get-after-id eventlog topic event-id))
  (get-before [this topic timestamp]
    (do-get-before eventlog topic timestamp))
  (get-after [this topic timestamp]
    (do-get-after eventlog topic timestamp))
  (get-between [this topic start-timestamp end-timestamp]
    (do-get-between eventlog topic start-timestamp end-timestamp)))
