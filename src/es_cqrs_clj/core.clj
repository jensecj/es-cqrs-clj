(ns es-cqrs-clj.core
  (:require
   [es-cqrs-clj.inmemory-eventlog :as el]
   [es-cqrs-clj.event-producer :as ep]
   [es-cqrs-clj.event-consumer :as ec]
   [es-cqrs-clj.testconsumer :as tc]
   [es-cqrs-clj.testproducer :as tp])
  (:gen-class))

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

    (def producer-future (tp/produce producer))
    (def consumer-future (tc/consume consumer))

    (Thread/sleep 30000)

    (future-cancel producer-future)
    (future-cancel consumer-future)
    (println "STOPPING EXPERIMENT")
    )
  )
