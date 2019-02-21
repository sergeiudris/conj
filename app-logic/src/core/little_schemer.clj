(ns core.little-schemer 
  (:require [clojure.repl :refer :all]))

(defn atom? [a]
  (not (coll? a)))

