(ns aq.query
  (:require [clojure.repl :refer :all]
            [datomic.api :as d]
            [aq.conn :refer [conn cdb]]
            [clojure.pprint :as pp]
            ))


(defn get-paginted-entity
  "Returns entities and total count given limit,offset and attribute keword"
  [attribute limit offset]
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
   (get-paginted-entity :artist/name 10 0)
   (pp/pprint)
   )
  
  

  )