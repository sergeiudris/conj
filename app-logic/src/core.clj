(ns core
  (:require [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            [dev.server]
            [core.clara.clara]
            [core.rabbitmq]
            [core.logic]
            [core.datomic]
            [core.little-schemer]
            [core.seasoned-schemer]
            
            
            [dev.nrepl]))


(defn -main []
  (dev.nrepl/-main)
  (dev.server/run-dev))


(comment
  
  (+)
  
  (defn hi [] "hi")
  
  )