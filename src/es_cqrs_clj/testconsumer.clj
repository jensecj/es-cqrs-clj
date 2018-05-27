(ns es-cqrs-clj.testconsumer
  (:require
   [es-cqrs-clj.eventlog :as es]))

(defn consume [event]
  (println (format "consumed %s" event)))

(defn setup []
  (let [store (es/->EventLog)]
    (es/subscribe store :mytopic #'consume)))

(defn teardown []
  (let [store (es/->EventLog)]
    (es/unsubscribe store :mytopic #'consume)))
