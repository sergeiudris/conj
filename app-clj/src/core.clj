(ns core
  (:require [nrepl-server]
            [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            [reference.core]
            [guides.guides]
            [guides.spec]
            
            ))




(defn -main []
  (nrepl-server/-main))