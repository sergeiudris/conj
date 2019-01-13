(ns onprem-start
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [datomic.client.api :as d]))

(def cfg {:server-type :peer-server
          :access-key "myaccesskey"
          :secret "mysecret"
          :endpoint "datomicdb:4334"})

(def client (d/client cfg))

(def conn (d/connect client {:db-name "hello"}))

conn