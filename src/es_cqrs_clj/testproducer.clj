(ns es-cqrs-clj.testproducer
  (:require
   [es-cqrs-clj.event-producer :as ep]))

(defn produce [producer]
  (future
    (println "-- starting producer")

    (while true
      ;; produce new events sporadically
      (Thread/sleep (+ 100 (rand-int 1000)))

      (if (zero? (rand-int 2))
        (ep/commit producer :calculator-topic {:type '+ :value (rand-int 100)})
        (ep/commit producer :calculator-topic {:type '- :value (rand-int 100)})))))
