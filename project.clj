(defproject es-cqrs-clj "0.1.0-SNAPSHOT"
  :description "An example of event sourcing + CQRS in pure clojure"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.9.0"]]
  :main ^:skip-aot es-cqrs-clj.core
  :profiles {:uberjar {:aot :all}})
