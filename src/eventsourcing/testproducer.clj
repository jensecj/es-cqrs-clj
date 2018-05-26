(ns eventsourcing.testproducer
  (:require
   [eventsourcing.eventlog :as es]))

(defn produce []
  (let [store (es/->EventLog)]
    (es/emit store :mytopic {:content (rand)})))
