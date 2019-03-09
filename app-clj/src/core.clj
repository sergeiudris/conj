(ns core
  (:require [nrepl-server]
            [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            [reference.core]
            [guides.spec]
            ))




(defn -main []
  (nrepl-server/-main))