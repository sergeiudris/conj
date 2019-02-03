(ns cors.handlers.query
  (:require [clojure.java.io :as io]
            [clojure.core.async :as async]
            [io.pedestal.log :as log]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [io.pedestal.http.sse :as sse]
            [ring.util.response :as ring-resp]
            [ring.middleware.cors :as cors]
            [aq.conn]
            [aq.query]
            [datomic.api :as d]
            [clojure.edn :as edn]
            ))

  (defn parse-int [number-string]
    (try (Integer/parseInt number-string)
         (catch Exception e nil)))

(defn gen-resp-entity-params [request]
  (let [{query-params :query-params} request
        {x :x
         limit :limit
         offset :offset
         attribute :attribute
         fmt :fmt
         :or { 
              attribute ":artist/name"
              limit 10
              offset 0
              fmt "edn"
              }
         } query-params
        ]
    {:status 200 
     :body (let [body {:data (aq.query/get-paginted-entity {:attribute (edn/read-string attribute) 
                                                            :limit (or (parse-int limit) 10)
                                                            :offset (or (parse-int offset) 0)})
                       :query-params query-params
                       :random (Math/random)
                       :uuid (d/squuid)
                       :x x}]
             (if (= fmt "edn") body (str body))
             ) 
     })
  )



; http://localhost:8893/entity-params?limit=1&offset=0&attribute=%22:release/year%22&fmt=str

(def gen-resp-entity-default-body {
                                   :attribute :artist/name
                                   :limit 10
                                   :offset 0
                                   :fmt "edn"
                                   :x 3
                                   })

(defn gen-resp-entity [request]
  (let [{query-params :query-params} request
        {
         data-str :data
         :or {data-str (str {
                             :attribute :artist/name
                             :limit 10
                             :offset 0
                             :fmt "edn"
                             :x 3
                             })
              }
         }query-params
        data (merge gen-resp-entity-default-body (edn/read-string data-str))
        ]
    {:status 200
     :body (let [body {:data (aq.query/get-paginted-entity data )
                       :request-data data
                       :random (Math/random)
                       :uuid (d/squuid)
                       }]
             (if (= (:fmt data) "edn") body (str body)))}))



(defn entity-params [request]
  (gen-resp-entity-params request))


(defn entity [request]
  (gen-resp-entity request))