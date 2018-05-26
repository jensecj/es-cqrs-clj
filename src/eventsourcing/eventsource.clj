(ns eventsourcing.eventsource
  (:require
   [eventsourcing.eventsource-protocol :as esp]
   [eventsourcing.eventlog-protocol :as elp]))

(def^{:private true} handlers (atom {}))
(reset! handlers {})

(defn- handle-emit [eventlog topic event]
  (println (format "source: emitting %s" event))

  (elp/add-event eventlog topic event)

  ;; call all handlers that are subscribed to the topic of this event
  (if-let [fns (topic @handlers)]
    (doseq [f fns]
      (f event))))

(defn- handle-get-events [eventlog topic]
  (elp/get-events eventlog topic))

(defn- handle-subscribe [topic handler]
  (println (format "source: subscribing to %s" topic))
  (swap! handlers update-in [topic] conj handler))

(defn- handle-unsubscribe [topic handler]
  (println (format "source: unsubscribing from %s" topic))
  (swap! handlers update-in [topic] (fn [fns] (remove #{handler} fns))))

(defrecord EventSource [eventlog]
  esp/EventSourceProtocol
  (emit [this topic event]
    (handle-emit eventlog topic event))
  (get-events [this topic]
    (handle-get-events eventlog topic))
  (subscribe [this topic handler]
    (handle-subscribe topic handler))
  (unsubscribe [this topic handler]
    (handle-unsubscribe topic handler)))
