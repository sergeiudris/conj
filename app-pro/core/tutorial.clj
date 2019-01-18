(ns tutorial 
  (:require [nrepl.server :refer [start-server stop-server]]
            [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            [datomic.api :as d]))

(def db-uri "datomic:dev://datomicdb:4334/tutorial")

(d/create-database db-uri)

(def conn (d/connect db-uri))
(def db (d/db conn))

(defn -main [] 
  (prn conn)
  (prn db)
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))

(comment
  
  (d/q '[:find ?e
         :where [?e :age 42]] db)
  
  (d/q '[:find ?e
         :where [?e :age 42]] '[[sally :age 21]
                                [fred :age 42]
                                [ethel :age 42]
                                [fred :likes pizza]
                                [sally :likes opera]
                                [ethel :likes sushi]])
  
  )


; create assert
; read read
; update accumulate
; delete retract

;Assert/Read/Accumulate/Retract (ARAR) should be pronounced doubled and in a pirate voice "Ar Ar Ar Ar".

(comment

  [:db/add "foo" :db/ident :green]
  ; same as
  {:db/indent :green}

  (d/transact
   conn
   [{:db/ident :red}
    {:db/ident :green}
    {:db/ident :blue}
    {:db/ident :yellow}])

  (doc mapv)

  (doc map-indexed)

  (doc map)

  (defn make-idents
    [x]
    (mapv #(hash-map :db/ident %) x))

  (def sizes [:small :medium :large :xlarge])

  (make-idents sizes)

  (def types [:shirts :dress :hat])
  (def colors [:red :green :blue :yellow])

  (d/transact conn (make-idents sizes))
  (d/transact conn (make-idents types))

  (def schema-1
    [{:db/ident :inv/sku
      :db/valueType :db.type/string
      :db/unique :db.unique/identity
      :db/cardinality :db.cardinality/one}
     {:db/ident :inv/color
      :db/valueType :db.type/ref
      :db/cardinality :db.cardinality/one}
     {:db/ident :inv/size
      :db/valueType :db.type/ref
      :db/cardinality :db.cardinality/one}
     {:db/ident :inv/type
      :db/valueType :db.type/ref
      :db/cardinality :db.cardinality/one}])

  (d/transact conn schema-1)

  (def smaple-data
    (->> (for [color colors size sizes type types]
           {:inv/color color
            :inv/size size
            :inv/type type})
         (map-indexed
          (fn [idx map]
            (assoc map :inv/sku (str "SKU-" idx))))
         vec))

  sample-data

  (d/transact conn sample-data)

  (def db (d/db conn))
  
  [:inv/sku "SKU-42"]

  (d/pull db
          [{:inv/color [:db/ident]}
           {:inv/size [:db/ident]}
           {:inv/type [:db/ident]}]
          [:inv/sku "SKU-42"])
  (d/q '[:find ?e ?sku
         :where [?e :inv/sku "SKU-42"]
         [?e :inv/color ?color]
         [?e2 :inv/color ?color]
         [?e2 :inv/sku ?sku]] 
       db)
  
  

  
  )
