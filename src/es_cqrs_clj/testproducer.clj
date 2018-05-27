(ns es-cqrs-clj.testproducer
  (:require
   [es-cqrs-clj.eventlog :as es]))

(defn produce []
  (let [store (es/->EventLog)]
    (es/emit store :mytopic {:content (rand)})))
