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

(facts "Camel send with headers"

       (let [context (make-context)
             produce (make-producer context)
             headers (atom {})]
         (defroute context
                   :err-handler (default-error-handler)
                   (from "direct:test-headers")
                   (process (processor
                              (swap! headers #(into % (get-headers ex))))))
         (produce "direct:test-headers" "body"
                  :headers {:operationName "doFoo"}
                  :exchange-pattern in-only)
         (fact "produce and route with headers"
               (get @headers "operationName") => "doFoo")))

(facts "Camel request with headers"

       (let [context (make-context)
             produce (make-producer context)
             headers (atom {})]
         (defroute context
                   :err-handler (default-error-handler)
                   (from "direct:test-out-headers")
                   (process (processor
                              (swap! headers #(into % (get-headers ex))))))
         (let [result (produce "direct:test-out-headers" "body"
                               :headers {:operationName "doFoo"}
                               :exchange-pattern in-out)]
           (fact "produce in-out and route with headers"
                 result => "body"
                 (get @headers "operationName") => "doFoo"))))