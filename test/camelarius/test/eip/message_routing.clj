(ns camelarius.test.eip.message-routing
  (:use [camelarius.core]
        [clojure.test]
        [midje.sweet]
        [camelarius.test-util]))

(facts "Content based router EIP"
  (fact "Content based router using choice"
    (let [context (make-context)]      
      (defroute context
        (from "direct:source")
        (choice)
          (when (predicate (= (get-body ex) "a")))
            (to "mock:a")
          (when (predicate (= (get-body ex) "b")))
            (to "mock:b")
          (otherwise)
            (to "mock:c")
        (end))

      (doseq [x ["a" "b" "c"]]
        ((make-producer context) "direct:source" x)
        (let [mock-dest (make-endpoint context (str "mock:" x))]
          (received-counter mock-dest) => 1
          (get-body (first (received-exchanges mock-dest))) => x)))))

(facts "Multicast"
  (fact "sequential multicast"
    (let [context (make-context)
          produce (make-producer context)]
      (defroute context
        (from "direct:source")
        (multicast)
        (to (into-array ["mock:a" "mock:b"])))

      (produce "direct:source" "foo")

      (received-counter (make-endpoint context "mock:a")) => 1
      (received-counter (make-endpoint context "mock:b")) => 1))
  (fact "parallel multicast"
    (let [context (make-context)
          produce (make-producer context)]
      (defroute context
        (from "direct:source")
        (multicast)
        (parallel-processing)
        (to (into-array ["mock:a" "mock:b"])))

      (produce "direct:source" "foo")

      (received-counter (make-endpoint context "mock:a")) => 1
      (received-counter (make-endpoint context "mock:b")) => 1))  )

(facts "Message Filter EIP"
  (fact "Message filter on header"
    (let [context (make-context)
          produce (make-producer context)
          mock-dest (make-endpoint context "mock:dest")]
      (defroute context
        (from "direct:source")
        (filter (predicate (= "bar" (get-header ex :foo))))
        (to mock-dest))

      (produce "direct:source" "a" :headers {:foo "bar"})
      (produce "direct:source" "b" :headers {:foo "baz"})

      (received-counter mock-dest) => 1
      (get-body (first (received-exchanges mock-dest))) => "a")))

(facts "Wiretap"
  (fact "wiretap with mock destinations and processor"
    (let [context (make-context)
          produce (make-producer context)
          mock-dest (make-endpoint context "mock:dest")
          mock-tap (make-endpoint context "mock:tap")]      
      (defroute context
        (from "direct:tap")
        (process (processor (println ex)))
        (to "mock:tap"))

      (defroute context
        (from "direct:source")
        (wire-tap "direct:tap")
        (to "mock:dest"))

      (produce "direct:source" "foo")

      (Thread/sleep 50)
      (received-counter mock-dest) => 1
      (received-counter mock-tap) => 1)))
  

