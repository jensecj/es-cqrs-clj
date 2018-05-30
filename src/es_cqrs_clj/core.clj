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
    (ep/commit producer :sometopic {:name "some event"})

    (println (ec/get-after consumer :mytopic 0))

    (ep/commit producer :mytopic {:name "first event"})
    (ep/commit producer :mytopic {:name "second event"})

    (println (ec/get-after consumer :mytopic 0))

    (ep/commit producer :mytopic {:name "last event"})
    ()
    ;; (println (esp/get-events source :mytopic))
    )
  )
