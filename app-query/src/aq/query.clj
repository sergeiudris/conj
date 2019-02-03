(ns aq.query
  (:require [clojure.repl :refer :all]
            [datomic.api :as d]
            [aq.conn :refer [conn cdb]]
            [clojure.pprint :as pp]
            ))


(defn get-paginted-entity
  "Returns entities and total count given limit,offset and attribute keword"
  [db attribute limit offset]
  {:entities (->>
              (d/q '{:find [?e (count ?e)]
                     :in [$ ?attribute]
                     :where [[?e ?attribute]]}
                   db attribute)
              (drop offset)
              (take limit)
              (map  #(identity (d/pull (cdb) '[*] (first %))))
              (into [])
              )
   :count (d/q '{:find [(count ?e) .]
                 :in [$ ?attribute]
                 :where [[?e ?attribute]]}
               db attribute)
   }
  )



(comment

  (d/q '{:find [(count ?e) .]
         :where [[?e :artist/name]]}
       (cdb))

  (->>
   (get-paginted-entity (cdb) :artist/name 10 0)
   (pp/pprint)
   )
  
  

  )