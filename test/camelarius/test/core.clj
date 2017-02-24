(ns camelarius.test.core
  (:use [clojure.test]
        [midje.sweet]
        [camelarius.core]))

(facts "Camel sanity checks"

  (let [context (make-context)
        produce (make-producer context)
        counter (atom 0)]
    (defroute context
      :err-handler (default-error-handler)
      (from "direct:test")
      (process (processor
                (swap! counter inc))))
    (produce "direct:test" "foo")
    (fact "produce and route"
      @counter => 1)))

(facts "Camel sanity checks for in-out exchange"

  (let [context (make-context)
        produce (make-producer context)]
    (defroute context
      :err-handler (default-error-handler)
      (from "direct:test-out")
      (process (processor (let [counter (get-body ex)]
                            (set-out-body ex (inc counter))))))
    (let [result (produce "direct:test-out" 1 :exchange-pattern in-out)]
      (fact "produce in-out and route"
            result => 2))))
