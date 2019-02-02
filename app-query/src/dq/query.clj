(ns dq.query
  (:require [datomic.api :as d]
            [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            [dq.conn :refer [conn db cdb]]
            ))

 (defn entity-by-external-id
   [attribute external-id]
   (->>
    (d/pull (cdb) '[:db/id] [attribute external-id])
    first second))

(comment

  ;; sample queries
  
  (d/q '{:find [(pull ?e [*])]
         :where [[?e :player/id 23]]}
       (cdb))

  ;; find player id
  (defn player-id [tag]
    (->>
     (d/q '{:find [?player-id]
            :in [$ ?tag]
            :where [[?e :player/tag ?tag]
                    [?e :player/id ?player-id]]}
          (cdb) tag)
     ffirst))

  (player-id "DongRaeGu")

  ;; find all matches Scarlett played in
  
  

  (d/q '{
         :find [(pull ?match [*])]
         :in [$ ?payer-id]
         :where [(or
                  [?match :match/pla_id ?player-id]
                  [?match :match/plb_id ?player-id])]}
       (cdb) 23)

  ; (d/q '{:find [(pull ?match [*])]
  ;        :in [$ ?player]
  ;        :where [
  ;                [?player :player/id ?player-id]
  ;                (or
  ;                 [?match :match/pla_id ?player-id]
  ;                 [?match :match/plb_id ?player [] id])]}
  ;      (cdb) [:player/tag "Scarlett"])
  
  (->>
   (d/q '{:find [(pull ?match [*])]
          :in [$ ?player-id]
          :where [(or
                   [?match :match/pla_id ?player-id]
                   [?match :match/plb_id ?player-id])]}
        (cdb) (player-id "Scarlett"))
  ;  pp/pprint
   count)

  (->>
   (d/q '{:find [(count ?match)]
          :in [$ ?player-id]
          :where [(or
                   [?match :match/pla_id ?player-id]
                   [?match :match/plb_id ?player-id])]}
        (cdb) (player-id "Scarlett"))
   pp/pprint)

  ;; count all mathces bobmber palyed in
  (->>
   (d/q '{:find [?match]
          :in [$ ?tag]
          :where [[?player :player/tag ?tag]
                  (or
                   [?match :match/pla ?player]
                   [?match :match/plb ?player])]}
        (cdb) "Bomber")
   count)
  ; => 583
  
  ;; find all events Bomber participated in
  (->>
   (d/q '{:find [(distinct ?event) .]
          :in [$ ?tag]
          :where [[?player :player/tag ?tag]
                  (or
                   [?match :match/pla ?player]
                   [?match :match/plb ?player])
                  [?match :match/eventobj ?event]]}
        (cdb) "Bomber")
   count)
  ; => 402
  
  ;; find how many matches of a player were played in event that had earnings
  (->>
   (d/q '{:find [(distinct ?match) .]
          :in [$ ?tag]
          :where [[?player :player/tag ?tag]
                  [?earnings :earnings/player ?player]
                  [?earnings :earnings/eventobj ?event]
                  [?match :match/eventobj ?event]]}
        (cdb) "Scarlett")
   count)
  ; wrong results - Scarlett has 1335 matches and only 145 w/ earnings ?!
  ; wrong results - Bomber  only 15 w/ earnings ?!
  

;; find date, event name and opponent name for when Scarlett played Protoss
  (->>
   (d/q '{:find [?date ?event-fullname ?opponent-tag]
          ; :find [?match]
          :in [$ ?tag]
          :where [[?player :player/tag ?tag]
                  [?opponent :player/tag "DongRaeGu"]
                  (or
                   (and
                    [?match :match/pla ?player]
                    [?match :match/rca "P"])
                   (and
                    [?match :match/plb ?player]
                    [?match :match/rcb "P"]))
                  (or
                   [?match :match/pla ?opponent]
                   [?match :match/plb ?opponent])
                  [?match :match/date ?date]
                  [?match :match/eventobj ?event]
                  [?event :event/fullname ?event-fullname]
                  [?opponent :player/tag ?opponent-tag]
                  ; [?match :earnings/eventobj ?event]
                  ; [?earnings :earnings/eventobj ?event]
                  ; [?match :match/eventobj ?event]
                  ]}
        (cdb) "Scarlett")
  ;  count
   first
   pp/pprint)


  ;; find date and event name of matches Bomber 2 Scarlett 1 (Homestory Cup)
  (->>
   (d/q '{:find [?date ?event-fullname]
          :in [$ ?tag]
          :where [[?player :player/tag ?tag]
                  [?opponent :player/tag "Bomber"]
                  (or
                   (and
                    [?match :match/pla ?player]
                    [?match :match/sca 1])
                   (and
                    [?match :match/plb ?player]
                    [?match :match/scb 1]))
                  (or
                   (and
                    [?match :match/pla ?player]
                    [?match :match/plb ?opponent])
                   (and
                    [?match :match/plb ?player]
                    [?match :match/pla ?opponent]))
                  [?match :match/date ?date]
                  [?match :match/eventobj ?event]
                  [?event :event/fullname ?event-fullname]]}
        (cdb) "Scarlett")
  ;  count
    ; first
   vec
   pp/pprint)





  ;; find event names when a player won >= 100000
  (->>
   (d/q '{:find [?tag ?event-fullname]
          :where [[?earnings :earnings/earnings ?amount]
                  [(>= ?amount 100000)]
                  [?earnings :earnings/player ?player]
                  [?player :player/tag ?tag]
                  [?earnings :earnings/eventobj ?event]
                  [?event :event/fullname ?event-fullname]
                  ; [?match :match/eventobj ?event]
                  ]}
        (cdb))
  ;  count
   vec
   pp/pprint))


(comment

  ;; count all players
  (->>
   (d/q '{:find [(count ?e)]
          :where [[?e :player/id]]}
        (cdb))
   ffirst
    ; pp/pprint
   )
  
;; count all matches
  (->>
   (d/q '{:find [(count ?e)]
          :where [[?e :match/id]]}
        (cdb))
   ffirst
    ; pp/pprint
   )
  ;; count all events
  (->>
   (d/q '{:find [(count ?e)]
          :where [[?e :event/id]]}
        (cdb))
   ffirst
    ; pp/pprint
   )
  
    ;; count all earnings
  (->>
   (d/q '{:find [(count ?e)]
          :where [[?e :earnings/id]]}
        (cdb))
   ffirst
    ; pp/pprint
   )

  ;; count matches that have player ref
  (->>
   (d/q '{:find [(count ?e)]
          :where [[?e :match/pla]]}
        (cdb))
   ffirst
    ; pp/pprint
   )

 ;; count earnings that have eventobj ref
  (->>
   (d/q '{:find [(count ?e)]
          :where [[?e :earnings/eventobj]]}
        (cdb))
   ffirst
    ; pp/pprint
   )

  (doc d/q)

  
  )

