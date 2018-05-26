(ns eventsourcing.inmemory-eventlog
  (:require [eventsourcing.eventlog-protocol :as elp]))

(def^{:private true} store (atom {}))
(reset! store {})

;;(swap! store assoc-in [:mytopic] [])
;;(conj '[1 2 3] 'e)
;;(swap! store update-in [:sometopic] (fn [s] (conj s {:name "3"})))

(defn- handle-add-event [topic event]
  (println (format "log: adding event to log: %s" event))

  (if (empty? (topic @store))
    (swap! store assoc-in [topic] []))

  ;; store the event in the log
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
