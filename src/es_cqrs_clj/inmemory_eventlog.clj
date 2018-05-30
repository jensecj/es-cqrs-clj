(ns es-cqrs-clj.inmemory-eventlog
  (:require [es-cqrs-clj.eventlog-protocol :as elp]))

(defn- handle-add-event [store topic event]
  (println (format "log: committing event: %s" event))
  (swap! store update-in [topic] conj event))

(defn- handle-get-events [store topic]
  (println (format "log: get events from: %s" topic))
  (topic @store))

(defrecord InMemoryEventLog [store]
  elp/EventLogProtocol

  (add-event [this topic event]
    (handle-add-event store topic event))
  (get-events [this topic]
    (handle-get-events store topic)))
