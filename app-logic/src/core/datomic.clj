(ns core.datomic
  (:require [clojure.repl :refer :all]
            [clojure.core.logic :refer :all :as l]
            [clojure.core.logic.unifier]
            [datomic.api :as d]
            [clojure.core.logic.datomic :refer [datom?]]))

(def db-uri "datomic:free://datomicdbfree:4334/mbrainz-1968-1973")


; (d/create-database db-uri)
; (d/delete-database db-uri)





(comment

  (def conn (d/connect db-uri))
  (def db (d/db conn))
  (defn cdb [] (d/db conn))

  (+)

  (d/q '{:find [(count ?e) .]
         :where [[?e :artist/name]]}
       (cdb))


  (defn unify-with-datom* [u v s]
    (when (and (instance? clojure.lang.PersistentVector v) (> (count v) 1))
      (loop [i 0 v v s s]
        (if (empty? v)
          s
          (when-let [s (l/unify s (first v) (nth u i))]
            (recur (inc i) (next v) s))))))


  (extend-type datomic.Datom
    clojure.core.logic.protocols/IUnifyTerms
    (unify-terms [u v s]
      (unify-with-datom* u v s)))

  (extend-type clojure.lang.PersistentVector
    clojure.core.logic.protocols/IUnifyTerms
    (unify-terms [u v s]
      (if (datom? v)
        (unify-with-datom* v u s)
        (when (sequential? v)
          (unify-with-sequential* u v s)))))

  (defn datomic-rel [q]
    (fn [a]
      (l/to-stream
       (map #(l/unify a % q) (d/datoms (d/db conn) :eavt)))))

  (run* [q]
        (fresh [e a v t]
               (== v true)
               (datomic-rel [e a v t])
               (== q [e a v t]))))