(ns es-cqrs-clj.inmemory-eventlog
  (:require [es-cqrs-clj.eventlog-protocol :as elp]))

(def^{:private true} store (atom {}))
(reset! store {})

(defn- handle-add-event [topic event]
  (println (format "log: committing event: %s" event))
  (swap! store update-in [topic] conj event))

(defn- handle-get-events [topic]
  (println (format "log: get events from: %s" topic))
  (topic @store))

(defrecord InMemoryEventLog []
  elp/EventLogProtocol

  (add-event [this topic event]
    (handle-add-event topic event))
  (get-events [this topic]
    (handle-get-events topic)))
