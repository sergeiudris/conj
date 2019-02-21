(ns schemer.little
  (:require [clojure.repl :refer :all]
            )
  )


(defn atom? [a]
  (not (coll? a))
)

(atom? '())