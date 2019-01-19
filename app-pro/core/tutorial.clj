;; https://docs.datomic.com/on-prem/tutorial.html

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

  (def sample-data
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

  (def order-schema
    [{:db/ident :order/items
      :db/valueType :db.type/ref
      :db/cardinality :db.cardinality/many
      :db/isComponent true}
     {:db/ident :item/id
      :db/valueType :db.type/ref
      :db/cardinality :db.cardinality/one}
     {:db/ident :item/count
      :db/valueType :db.type/long
      :db/cardinality :db.cardinality/one}])

  (d/transact conn order-schema)


  (def add-order
    [{:order/items
      [{:item/id [:inv/sku "SKU-25"]
        :item/count 10}
       {:item/id [:inv/sku "SKU-26"]
        :item/count 20}]}])

  (d/transact conn add-order)

  (def db (d/db conn))

  (d/q '[:find ?sku
         :in $ ?inv
         :where [?item :item/id ?inv]
         [?order :order/items ?item]
         [?order :order/items ?other-item]
         [?other-item :item/id ?other-inv]
         [?other-inv :inv/sku ?sku]]
       db [:inv/sku "SKU-25"])


  (def rules
    '[[(ordered-together ?inv ?other-inv)
       [?item :item/id ?inv]
       [?order :order/items ?item]
       [?order :order/items ?other-item]
       [?other-item :item/id ?other-inv]]])

  (d/q '[:find ?sku
         :in $ % ?inv
         :where (ordered-together ?inv ?other-inv)
         [?other-inv :inv/sku ?sku]]
       db rules [:inv/sku "SKU-25"])


  (def inventory-schema
    [{:db/ident :inv/count
      :db/valueType :db.type/long
      :db/cardinality :db.cardinality/one}])

  (d/transact conn inventory-schema)

  (def inventory-update
    [[:db/add [:inv/sku "SKU-21"] :inv/count 7]
     [:db/add [:inv/sku "SKU-22"] :inv/count 7]
     [:db/add [:inv/sku "SKU-42"] :inv/count 100]])

  (d/transact conn inventory-update)

  (d/transact conn [[:db/retract [:inv/sku "SKU-22"] :inv/count 7]
                    [:db/add "datomic.tx" :db/doc "remove incorrect assertion"]])

  (d/transact
   conn
   [[:db/add [:inv/sku "SKU-42"] :inv/count 1000]
    [:db/add "datomic.tx" :db/doc "correct data entry error"]])

  (def db (d/db conn))

  (d/q '[:find ?sku ?count
         :where [?inv :inv/sku ?sku]
         [?inv :inv/count ?count]]
       db)


  ; most recent txs
  (d/q '[:find (max 3 ?tx)
         :where [?tx :db/txInstant]]
       db)

  


  (def txid (->> (d/q '[:find (max 3 ?tx)
                        :where [?tx :db/txInstant]]
                      db)
                 first first last
                 ))
  txid
  
  (def db-before (d/as-of db txid))

  ; query db before retraction and correction
  (d/q '[:find ?sku ?count
         :where [?inv :inv/sku ?sku]
         [?inv :inv/count ?count]]
       db-before)

  
  (def db-hist (d/history db))

  (->> (d/q '[:find ?tx ?sku ?val ?op
              :where [?inv :inv/count ?val ?tx ?op]
              [?inv :inv/sku ?sku]]
            db-hist)
       (sort-by first)
       (pp/pprint))
  

  )
