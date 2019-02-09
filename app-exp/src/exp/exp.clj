(ns exp.exp
  (:require [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            [dev.server]
            [exp.clara.clara]
            [exp.rabbitmq]
            [dev.nrepl]))


(defn -main []
  (dev.nrepl/-main)
  (dev.server/run-dev))