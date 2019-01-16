
(ns learn.hashed
  (:require [nrepl.server :refer [start-server stop-server]]
            [clojure.pprint :refer [pprint]]
            [clojure.repl :refer :all]))


(def ps #{"A" "B" "C"})

(conj ps 123)

(disj ps "A")

(contains? ps "B")

(def ps2 (conj (sorted-set) "A" "C" "S" "B"))

(into ps ps2)

(into ps2 ps)

(def m {:b 3 :c 4})

(assoc m :4 3)

(:b m)



(defn -main []
  (println "running learn hashed collections")
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))