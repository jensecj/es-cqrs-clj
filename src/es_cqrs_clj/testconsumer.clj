(ns es-cqrs-clj.testconsumer
  (:require
   [es-cqrs-clj.event-producer :as ep]
   [es-cqrs-clj.event-consumer :as ec]))

;; the local state of this consumer
(def^{:private true} state (atom {:result 0}))
(reset! state {:result 0})

(defn- step
  "Transitions from STATE to another state, based on information from EVENT."
  [state event]
  (let [type (:type event)
        val (:value event)]
    (case type
      + (update-in state [:result] + val)
      - (update-in state [:result] - val)
      (println (format "unknown command in: %s" event)))))

(defn consume [consumer]
  (future
    (println "-- starting consumer")

    (let [last-event-timestamp (atom {:timestamp 0})]
      (while true
        ;; only poll new events once in a while
        (Thread/sleep 3000)

        (let [events (ec/get-after consumer :calculator-topic (:timestamp @last-event-timestamp))
              newest-timestamp (:timestamp (first events))]
          (println (format "found %s new events!" (count events)))
          (if (not (empty? events))
            (do
              ;; if there are new events

              ;; accumulate new events into our state
              (let [new-state (reduce step @state events)]
                (swap! state assoc :result (:result new-state)))

              (println (format "new state: %s" @state))

              ;; then update the latest timestamp we have seen
              (swap! last-event-timestamp assoc :timestamp newest-timestamp)
              (println (format "last seen: %s" @last-event-timestamp)))))))))
