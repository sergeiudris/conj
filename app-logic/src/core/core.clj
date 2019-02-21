(ns core.core
  (:require [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            [dev.server]
            [core.clara.clara]
            [core.rabbitmq]
            [core.logic]
            [core.datomic]
            [schemer.little]
            [schemer.reasoned]
            [schemer.seasoned]
            [dev.nrepl]))


(defn -main []
  (dev.nrepl/-main)
  (dev.server/run-dev))