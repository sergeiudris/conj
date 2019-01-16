
(ns learn.syntax
   (:require [nrepl.server :refer [start-server stop-server]]
             [clojure.pprint :refer [pprint]]
             [clojure.repl :refer :all]))


42 ; Long - 64 bit integer from -2^63 to 2^63-1

6.022e23 ; Double  - double-precision 64-bit floating point 
42N ; BigInt - arbirtrary precision integer
1.0M ; BigDecimal arbitrary precision fixed point
22/7 ; Ration
"hello" ; String
\e ; character


(println 42)

(doc +)


(defn -main []
  (println "running learn.syntax")
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))