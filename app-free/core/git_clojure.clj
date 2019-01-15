(ns git-clojure
  (:require [clj-time.core :as t]
            [nrepl.server :refer [start-server stop-server]]
            [clj-time.format :as f]
            [clojure.pprint :refer [pprint]]
            [datomic.api :as d]))

(def db-uri "datomic:free://datomicdbfree:4334/git")

(d/create-database db-uri)
(def conn (d/connect db-uri))
(def db (d/db conn))


(defn -main []
  (println conn)
  (defonce server (start-server :bind "0.0.0.0" :port 7888)))

(map first
     (d/q '[:find ?repo
            :where [?e :repo/uri ?repo]]
          db))

(map first
     (d/q '[:find ?ns
            :where
            [?e :clj/ns ?n]
            [?n :code/name ?ns]]
          db))

(reduce (fn [agg [o d]]
          (update-in agg [o] (fnil conj []) d))
        {}
        (d/q '[:find ?op ?def
               :where
               [?e :clj/def ?d]
               [?e :clj/defop ?op]
               [?d :code/name ?def]]
             db))

(defn foo
  "I don't do a whole lot."
  []
  (println "Hello, World!"))

(foo )



; (defn -main [& ]
;   (println "Running clojure-and-contrib examples with database" database-uri)
;   (try
;     (let [conn        (d/connect database-uri)
;           db          (-> conn d/db (d/as-of 435691))
;           repos       (map first
;                            (q '[:find ?repo
;                                 :where [?e :repo/uri ?repo]]
;                               db))
;           namespaces  (map first
;                            (q '[:find ?ns
;                                 :where
;                                 [?e :clj/ns ?n]
;                                 [?n :code/name ?ns]]
;                               db))
;           definitions (reduce (fn [agg [o d]]
;                                 (update-in agg [o] (fnil conj []) d))
;                               {}
;                               (q '[:find ?op ?def
;                                    :where
;                                    [?e :clj/def ?d]
;                                    [?e :clj/defop ?op]
;                                    [?d :code/name ?def]]
;                                  db))]
;       (println)
;       (println "#### Repos:")
;       (println)
;       (pprint repos)
;       (println)
;       (println "#### Namespaces:")
;       (println)
;       (pprint namespaces)
;       (println)
;       (println "#### Definitions:")
;       (println)
;       (pprint definitions)
;       (assert (= 44 (-> definitions keys count)))
;       (assert (= 33 (-> "defne" definitions count))))
;     (finally
;       ;; (shutdown-agents)
;       (System/exit 0))))