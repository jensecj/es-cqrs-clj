(ns eventsourcing.core
  (:require
   [eventsourcing.inmemory-eventlog :as el]
   [eventsourcing.eventsource :as E]
   [eventsourcing.eventsource-protocol :as esp]))

(def state (atom {}))

(defn- log [event]
  (println (format "subscription fired: %s" event)))

(defn- transition [state event]
  state)

(defn -main [& args]
  (let [log (el/->InMemoryEventLog)
        source (E/->EventSource log)]
    (println "------------------")
    (esp/emit source :sometopic {:name "some event"})
    (esp/subscribe source :mytopic #'log)
    (esp/emit source :mytopic {:name "first event"})
    (esp/emit source :mytopic {:name "second event"})
    (esp/unsubscribe source :mytopic #'log)
    (esp/emit source :mytopic {:name "last event"})

    (println (esp/get-events source :mytopic))
    )
  )
