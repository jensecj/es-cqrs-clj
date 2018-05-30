(ns es-cqrs-clj.testproducer
  (:require
   [es-cqrs-clj.event-producer :as ep]))

(defn produce [producer]
  (future (dotimes [i 20]
            (Thread/sleep (+ 1000 (rand-int 1500)))
            (ep/commit producer :mytopic {:name "some event"}))))
