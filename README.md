#Camelarius

Forked from https://github.com/clumsyjedi/clj-camel-holygrail

"The holy grail of Apache Camel DSLs in Clojure"

![Clojars Project](http://clojars.org/camelarius/latest-version.svg)

Camelarius (Latin) 'camel driver'

Copyright (C) clumsyjedi (Frazer Irving), xulfus (Janne Haarni)

Distributed under the Eclipse Public License.

## Rationale

[Apache Camel](http://camel.apache.org/) is awesome. Clojure is awesome. Together they are double awesomeness.

## Usage

Include in your project.clj:

```clojure
(ns my.awesome.camel-project
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [camelarius "0.4.0"]])

```

Basic routing:

```clojure
(let [context (make-context)]
  (defroute context
    (from "seda:source")
    (to "mock:dest"))

  ((make-producer context) "seda:source" "body"))
```

See the tests for more examples.

