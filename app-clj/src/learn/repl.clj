
(ns learn.repl
  (:require [nrepl.server :refer [start-server stop-server]]
            [clojure.pprint :refer [pprint pp print-table]]
            [clojure.repl :refer :all]))




(defn -main []
  (println "running learn repl")
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))


; Origin of apropos
; 1660–70; < French à propos literally, to purpose < Latin ad prōpositum. See ad-, proposition
(apropos "index")

(defn number-summary
  "Computes a summary of the arithmetic properties of a number, as a data structure."
  [n]
  (let [proper-divisors (into (sorted-set)
                              (filter
                               (fn [d]
                                 (zero? (rem n d)))
                               (range 1 n)))
        divisors-sum (apply + proper-divisors)]
    {:n n
     :proper-divisors proper-divisors
     :even? (even? n)
     :prime? (= proper-divisors #{1})
     :perfect-number? (= divisors-sum n)}))

(mapv number-summary [5 6 7 12 28 42])

(doc mapv)

(pprint (mapv number-summary [5 6 7 12 28 42]))

(pp)

(print-table (mapv number-summary [5 6 7 12 28 42]))

(set! *print-level* 3)

{:a {:b [{:c {:d {:e 42}}}]}}

(set! *print-length* 3)

(repeat 100 (vec (range 100)))

(set! *print-length* nil)

(def m {})

(/ 1 0)
(pst *e)
*e


(defn divide-verbose
  "Divides two numbers `x` and `y`, but throws more informative Exceptions when it goes wrong.
  Returns a (double-precision) floating-point number."
  [x y]
  (try
    (double (/ x y))
    (catch Throwable cause
      (throw
       (ex-info
        (str "Failed to divide " (pr-str x) " by " (pr-str y))
        {:numerator x
         :denominator y}
        cause)))))

(defn average
  "Computes the average of a collection of numbers."
  [numbers]
  (try
    (let [sum (apply + numbers)
          cardinality (count numbers)]
      (divide-verbose sum cardinality))
    (catch Throwable cause
      (throw
       (ex-info
        "Failed to compute the average of numbers"
        {:numbers numbers}
        cause)))))

(average [])

*e

(require '[clojure.java.javadoc :as jdoc])

(jdoc/javadoc #"a+") ; No X11 DISPLAY variable was set, but this program performed an operation which requires it.

 (jdoc/javadoc java.util.regex.Pattern) ;; equivalent to the above

(require '[clojure.inspector :as insp])

(insp/inspect-table (mapv number-summary [2 5 6 28 42])) ; No X11 DISPLAY variable was set, but this program performed an operation which requires it.

(require '[clojure.java.io :as io])

(def v (io/input-stream "https://www.clojure.org"))

v

(type v)

(ancestors (type v))

(require '[clojure.reflect :as reflect])

(reflect/reflect java.io.InputStream)

; (pprint (reflect/reflect java.io.InputStream))

(->> (reflect/reflect java.io.InputStream) :members (sort-by :name) (print-table [:name :flags :parameter-types :return-type]))

(.read v)



