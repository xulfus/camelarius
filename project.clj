(defproject camelarius/camelarius "0.4.2"
  :description "Apache Camel DSL in Clojure"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :url "https://github.com/xulfus/camelarius"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.apache.camel/camel-core  "2.15.2"]
                 [org.clojure/tools.logging "0.2.6"]]

  :profiles {:dev {:dependencies [[midje "1.6.3"]
                                  [midje-junit-formatter "0.1.0-SNAPSHOT"]]
                   :plugins [[lein-midje "3.1.3"]]}})
