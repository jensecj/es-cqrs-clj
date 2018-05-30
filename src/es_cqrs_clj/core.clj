(ns es-cqrs-clj.core
  (:require
   [es-cqrs-clj.inmemory-eventlog :as el]
   [es-cqrs-clj.eventsource :as es]
   [es-cqrs-clj.eventsource-protocol :as esp]
   [es-cqrs-clj.event-producer :as ep]
   [es-cqrs-clj.event-consumer :as ec]
   ))

(def state (atom {}))

(defn- log [event]
  (println (format "subscription fired: %s" event)))

(defn- transition [state event]
  state)

(defn -main [& args]
  (let [log (el/->InMemoryEventLog)
        producer (ep/->EventProducer log)
        consumer (ec/->EventConsumer log)]
    (println "------------------")
    (def producer-future
      (future (dotimes [i 20]
                (Thread/sleep (+ 1000 (rand-int 1500)))
                (ep/commit producer :mytopic {:name "some event"}))
              100))

    (def consumer-future
      (future
        (let [last-event-timestamp (atom {:timestamp 0})]
          (dotimes [i 10]
            (Thread/sleep (+ 2500 (rand-int 2000)))
            (let [events (ec/get-after consumer :mytopic (:timestamp @last-event-timestamp))]
              (println (format "found %s new events!" (count events)))
              (if (not (empty? events))
                (do
                  (swap! last-event-timestamp assoc :timestamp (:timestamp (first events) 0))
                  (println (format "last seen event: %s" @last-event-timestamp))
                  (println (map :timestamp events)))))))
        100))

    (println "KILLING EXPERIMENT")
    (Thread/sleep 30000)
    (future-cancel producer-future)
    (future-cancel consumer-future)
    ()
    )
  )
