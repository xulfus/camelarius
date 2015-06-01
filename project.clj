(defproject org.clojars.xulfus/camelarius "0.4.0" 
  :description "Apache Camel DSL in Clojure"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.apache.camel/camel-core  "2.15.2"]
                 [org.clojure/tools.logging "0.2.6"]]

  :profiles {:dev {:dependencies [[midje "1.6.3"]
                                  [midje-junit-formatter "0.1.0-SNAPSHOT"]]
                   :plugins [[lein-midje "3.1.3"]]}})
