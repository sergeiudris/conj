
(ns learn.flow
  (:require [nrepl.server :refer [start-server stop-server]]
            [clojure.pprint :refer [pprint]]
            [clojure.repl :refer :all]))

(str "2 is " (if (even? 2) "even" "odd"))

(if 0 true false)
(if nil 1 0)
(if false 1 0)




(defn -main []
  (println "running learn flow")
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))