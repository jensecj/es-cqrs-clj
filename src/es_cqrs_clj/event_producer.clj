(ns es-cqrs-clj.event-producer
  (:require
   [es-cqrs-clj.eventlog-protocol :as elp]))

(defn- do-commit [eventlog topic event]
  (let [timestamp (System/currentTimeMillis)
        uuid (str (java.util.UUID/randomUUID))]
    (elp/add-event eventlog topic (assoc event :timestamp timestamp :uuid uuid))))

(defprotocol EventProducerProtocol
  ""
  (commit [this topic event] "Commit a new event to the event log."))

(defrecord EventProducer [eventlog]
  EventProducerProtocol

  (commit [this topic event]
    (do-commit eventlog topic event)))
