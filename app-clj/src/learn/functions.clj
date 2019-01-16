
(ns learn.functions
   (:require [nrepl.server :refer [start-server stop-server]]
             [clojure.pprint :refer [pprint]]
             [clojure.repl :refer :all]))



(def greet (fn [name] (str "Hello " name)))

(greet "q")

3
;; equivalent ot (fn [x] (+ 6 x))
#(+ 6 %)

(fn [x y] (+ x y))

#(+ %1 %2)

(fn [x y & zs] (println x y zs))

#(println %1 %2 %&)

(#(vector %) 1)


(defn -main []
  (println "running learn.functions")
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))