
(ns core.clara.examples
  (:require   [clojure.repl :refer :all]
             [clojure.pprint :as pp]
            ;  [clara.examples.shopping :as shopping]
            ;  [clara.exa/mples.validation :as validation]
             [core.clara.examples.sensors :as sensors]
            ;  [clara.examples.java.shopping :as jshopping]
            ;  [clara.examples.booleans :as booleans]
            ;  [clara.examples.fact-type-options :as type-opts]
            ;  [clara.examples.truth-maintenance :as truth]
            ;  [clara.rules :as r]
             ))

(defn -main []
  

  
  ; (println "Shopping examples:")
  ; (shopping/run-examples)
  ; (println)
  ; (println "JavaBean Shopping examples from Clojure:")
  ; (jshopping/run-examples)
  ; (println)
  ; (println "JavaBean Shopping examples from Java:")
  ; (clara.examples.java.ExampleMain/main (into-array String []))
  ; (println)
  ; (println "Validation examples:")
  ; (validation/run-examples)
  ; (println)
  ; (println "Sensor examples:")
  ; (sensors/run-examples)
  ; (println)
  ; (booleans/run-examples)
  ; (println)
  ; (println "Boolean expression examples:")
  ; (booleans/run-examples)
  ; (println)
  ; (println "Fact type options examples:")
  ; (type-opts/run-examples)
  ; (println)
  ; (println "Truth maintenance examples: ")
  ; (truth/run-examples)
  
  )

(comment
  
    (println "Sensor examples:")
(sensors/run-examples)
  
  )