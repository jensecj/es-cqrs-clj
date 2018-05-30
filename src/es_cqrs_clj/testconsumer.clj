(ns es-cqrs-clj.testconsumer
  (:require
   [es-cqrs-clj.event-producer :as ep]
   [es-cqrs-clj.event-consumer :as ec]))


(defn consume [consumer]
  (future
    (let [last-event-timestamp (atom {:timestamp 0})]
      (dotimes [i 10]
        (Thread/sleep (+ 2500 (rand-int 2000)))
        (let [events (ec/get-after consumer :mytopic (:timestamp @last-event-timestamp))
              newest-timestamp (:timestamp (first events) 0)]
          (println (format "found %s new events!" (count events)))
          (if (not (empty? events))
            (do
              (swap! last-event-timestamp assoc :timestamp newest-timestamp)
              (println (format "last seen event: %s" @last-event-timestamp))
              (println (map :timestamp events))))))))
  )
