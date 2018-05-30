(ns es-cqrs-clj.testconsumer
  (:require
   [es-cqrs-clj.event-producer :as ep]
   [es-cqrs-clj.event-consumer :as ec]))

(defn- step
  "Transitions from STATE to another state, based on information from EVENT."
  [state event]
  (let [type (:type event)
        val (:value event)]
    (case type
      + (update-in state [:result] + val)
      - (update-in state [:result] - val)
      (println (format "unknown command in: %s" event)))))

(def snapshot
  "Snapshot of this consumers state, as it was after processing all events up
  till, and including the event at TIMESTAMP."
  (atom {:timestamp 0 :state {:result 0}}))

(defn consume [consumer]
  (future
    (println "-- starting consumer")

    (let [last-event-timestamp (atom {:timestamp (:timestamp @snapshot)})
          state (atom (:state @snapshot))
          processed-events (atom 0)]
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
              (println (format "last seen: %s" @last-event-timestamp))))

          ;; count how many events we have processed
          (swap! processed-events + (count events))

          ;; update the state-snapshot when reaching some threshold of events
          ;; processed
          (when (> @processed-events 25)
            (reset! processed-events 0)
            (swap! snapshot assoc :timestamp newest-timestamp :state @state)
            (println (format "updating snapshow, timestamp: %s, state: %s" (:timestamp @snapshot) (:state @snapshot))))
          )))))
