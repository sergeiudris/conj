(ns aq.query
  (:require [clojure.repl :refer :all]
            [datomic.api :as d]
            [aq.conn :refer [conn cdb]]
            [clojure.pprint :as pp]
            ))


(defn get-paginted-entity
  "Returns entities and total count given limit,offset and attribute keword"
  [{:keys [attribute limit offset]
    :or {limit 10 offset 0}}]
  {:entities (->>
              (d/q '{:find [?e (count ?e)]
                     :in [$ ?attribute]
                     :where [[?e ?attribute]]}
                   (cdb) attribute)
              (drop offset)
              (take limit)
              (map  #(identity (d/pull (cdb) '[*] (first %))))
              (into [])
              )
   :count (d/q '{:find [(count ?e) .]
                 :in [$ ?attribute]
                 :where [[?e ?attribute]]}
               (cdb) attribute)
   }
  )




(comment

  (d/q '{:find [(count ?e) .]
         :where [[?e :artist/name]]}
       (cdb))

  (->>
   (get-paginted-entity {:attribute :artist/name 
                         :limit 1
                         })
   (pp/pprint)
   )
  (int "1")
  (Integer/parseInt "1")

  
  (defn parse-int [number-string]
    (try (Integer/parseInt number-string)
         (catch Exception e nil)))

  (or (parse-int nil) 10)
  
  (str {:a 3 :fmt "edn"})
  
  )