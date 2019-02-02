(ns dq.etl
  (:require [datomic.api :as d]
            [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            
            [dq.psql]
            [dq.conn :refer [conn db cdb]]
            [dq.query :refer [entity-by-external-id]]
            ))


(defn run-etl []
  (do
    (def schema (read-string (slurp "resources/schema-aligulac.edn")))
    @(d/transact conn schema)
    @(d/transact conn (dq.psql/player-data))

    @(d/transact conn (dq.psql/event-data 30000 0))
    @(d/transact conn (dq.psql/event-data 30000 30000))
    @(d/transact conn (dq.psql/event-data 28689 60000))
    ; @(d/transact conn (dq.psql/match-data 30000 0))
    ; @(d/transact conn (dq.psql/match-data 30000 30000))
    ; @(d/transact conn (dq.psql/match-data 30000 60000))
    ; @(d/transact conn (dq.psql/match-data 30000 90000))
    ; @(d/transact conn (dq.psql/match-data 30000 120000))
    ; @(d/transact conn (dq.psql/match-data 30000 150000))
    ; @(d/transact conn (dq.psql/match-data 30000 180000))
    ; @(d/transact conn (dq.psql/match-data 30000 210000))
    ; @(d/transact conn (dq.psql/match-data 30000 240000))
    ; @(d/transact conn (dq.psql/match-data 21316 270000))
    
  @(d/transact conn (dq.psql/match-data 50000 0))
  @(d/transact conn (dq.psql/match-data 50000 50000))
  @(d/transact conn (dq.psql/match-data 50000 100000))
  @(d/transact conn (dq.psql/match-data 50000 150000))
  @(d/transact conn (dq.psql/match-data 50000 200000))
  @(d/transact conn (dq.psql/match-data 41316 250000))
  @(d/transact conn (dq.psql/earnings-data))
    )
  )



(comment

  (doc d/transact)

  (run-etl)

  (doc slurp)
  (doc read-string)

  (count (dq.psql/player-data))

  ;; load schema
  (def schema (read-string (slurp "resources/schema-aligulac.edn")))
  @(d/transact conn schema)

  ;; load sample data
  (def sample-data (read-string (slurp "resources/sample-data-aligulac.edn")))
  @(d/transact conn sample-data)

  ;; load sql data and transact to datomic
  
  ;player entity has no refs
  @(d/transact conn (dq.psql/player-data))

  ;event entity has no refs
  @(d/transact conn (dq.psql/event-data 50000 0))
  @(d/transact conn (dq.psql/event-data 38689 50000))

  ;match entity has player, event refs
  @(d/transact conn (dq.psql/match-data 50000 0))
  @(d/transact conn (dq.psql/match-data 50000 50000))
  @(d/transact conn (dq.psql/match-data 50000 100000))
  @(d/transact conn (dq.psql/match-data 50000 150000))
  @(d/transact conn (dq.psql/match-data 50000 200000))
  @(d/transact conn (dq.psql/match-data 41316 250000))


  ;earnings entity has player, event refs
  @(d/transact conn (dq.psql/earnings-data))


  ;; construct references
  
  (doc frequencies)
  (doc sort-by)
  (doc d/pull)
  (doc d/touch)
  (doc d/entity)

  (doc d/query)



  ;; https://docs.datomic.com/on-prem/query.html#custom-aggregates
  (defn page [limit offset vals]
    (->>
     (drop offset vals)
     (take limit)
    ;  first
     ))

  (top-20 (range 100))
  
  (defn paginate [limit offset]
    (fn [vals] 
      (->>
       (drop offset vals)
       (take limit)
       )
      )
    )
  
  (page 10 50 (range 100))
  
  ; get 10 matches 
  (->>
   (d/q '{
          :find [?match]
          ; :find [(pull ?match [*])]
          ; :find [(dq.etl/page 10 10 ?match)]
          :in [$]
          :where [[?match :match/id]]}
        (cdb)  )
   (drop 40)
   (take 10)
  ;  (map  #(str "Hello " % "!")  )
   (map  #( vector (d/pull (cdb) '[*] (first %)) (d/entity (cdb) (first %)) ))
   pp/pprint
  ;  count
   )

(vector 1 2)

  (->>
   (d/q '{
          :find [(dq.etl/page 10 40 ?match)]
          :in [$]
          :where [[?match :match/id]]}
        (cdb)  )
   )

  (defn page-by-attribute [limit offset ?attribute]
    '{; :find [(pull ?match [*])]
      :find [(dq.etl/page limit offset ?match)]
      :in [$]
      :where [[?match :match/id]]}
    )
  
  (page-by-attribute 10 30 :match/id)
  
  (->>
   (d/query {:query '{; :find [(pull ?match [*])]
                      :find [(dq.etl/top-20 ?match)]
                      :in [$ ?offset ?limit]
                      :where [[?match :match/id]]}
             :args [(cdb) 10 10]
             }
            )
  ;  (take 30)
  ;  count
   )

  

  (->>
   (d/q '{; :find [(pull ?match [*])]
          :find [( (dq.etl/paginate 10 10) ?match)]
          :where [[?match :match/id]]}
        (cdb))
  ;  (take 30)
  ;  count
   )
  
  (->>
   (d/q '{:find [(pull ?match [*])]
          :where [[?match :match/id]]}
        (cdb))
  ;  (take 30)
   top-20
   count)



  (->>
   (d/pull (cdb) '[:db/id] [:player/id 23])
   first second)

  (d/pull (cdb) '[*] 17592186059397)

  (entity-by-external-id :player/id 23)

  (entity-by-external-id :match/id 23)

  (entity-by-external-id :event/id 1111)

  (entity-by-external-id :earnings/id 11)





  (doc d/entid)
  (doc d/entity)


  (keys (ns-publics 'datomic.api)))

