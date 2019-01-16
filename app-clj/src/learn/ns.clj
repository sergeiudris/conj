(ns learn.ns
  (:require [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            [nrepl.server :refer [start-server stop-server]]))


(defn -main [] 
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))

(pp/pprint "hello")


