(ns exp.exp
  (:require [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            [dev.server]
            [exp.clara.clara]
            [dev.nrepl]))


(defn -main []
  (dev.nrepl/-main)
  (dev.server/run-dev))