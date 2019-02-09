(ns exp.clara.examples.basic
  (:require [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            [clara.rules :as cr]
            [clara.rules :as cr]))


(defn hello [] "hello")


(comment
  (+)

  (hello)


  ;; your first rules

  (defrecord SupportRequest [client level])

  (defrecord ClientRepresentative [name client])

  (cr/defrule important?
    "Find important support requests"
    [SupportRequest (= :high level)]
    =>
    (println "high support requested"))

  (cr/defrule notify-client-rep
    "Find the client representative and request support"
    [SupportRequest (= ?client client)]
    [ClientRepresentative (= ?client client) (= ?name name)]
    =>
    (println "Notify" ?name "that"
             ?client "has a new support request"))

  (-> (cr/mk-session 'exp.clara)
      (cr/insert (->ClientRepresentative "Alice" "Acme")
                 (->SupportRequest "Acme" :high))
      (cr/fire-rules)))