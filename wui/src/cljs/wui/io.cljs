(ns wui.io
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.repl :as repl]
            [cljs.pprint :as pp]
            [cljs.reader :as reader]
            [cljs.core.async :refer [<! take!]]))


(defn app-pro-get-entity-params
  "Query paginated entities using params"
  [{:keys [limit offset attribute] :or {:limit 10 :offset 0}}]
  (go (let [response (<! (http/get "http://localhost:8893/entity-params"
                                   {:with-credentials? false
                                    :query-params {
                                                   "limit" limit
                                                   "offset" offset
                                                   "attribute" attribute
                                                   }}))]
          ; (prn (:status response))
          ; (prn (map :login (:body response)))
        (:body response)
        ))
  )

(defn app-pro-get-entity
  "Query paginated entities"
  [{:keys [data]}]
  (go (let [response (<! (http/get "http://localhost:8893/entity"
                                   {:with-credentials? false
                                    :query-params {
                                                   "data" (str data)
                                                   }}))]
          ; (prn (:status response))
          ; (prn (map :login (:body response)))
        (:body response))))

; (defn github-get-users []
;   (go (let [response (<! (http/get "http://api.github.com/users"
;                                    {:with-credentials? false
;                                     :query-params {"since" 135}}))]
;         ; (prn (:status response))
;         ; (prn (map :login (:body response)))
;         (map :login (:body response))
;         ))
  
;   )

(defn github-get-users []
  (go (let [response (<! (http/get "http://api.github.com/users"
                                   {:with-credentials? false
                                    :query-params {"since" 135}}))]
        ; (prn (:status response))
        ; (prn (map :login (:body response)))
        (if (= 200 (:status response))
          (:body response)
          (print "An error has occured")))))





(comment
  
  (+ 1 1)
  
  (github-get-users)
  
  (prn [1 2 3])
  
  (repl/doc <! )
  (repl/doc first)
  (repl/doc take!)
  (repl/doc repl/apropos)
  
  (keys (ns-publics  'cljs.pprint))

  ;; requests  
  
  (go (let [result (<! (github-get-users))]
        (->>
         (first result)
         :id
         pp/pprint)
        ))
  
  
  (go (let [result (<! ( app-pro-get-entity-params {:limit 1 :offset 0 :attribute :release/name} ))]
        (pp/pprint result)
        (->>
        ;  (first (keys (reader/read-string result)))
         (first (keys  result))
        ;  :id
         pp/pprint
         )
        ))

  
  (go (let [result (<! (app-pro-get-entity {:data {:limit 1 :offset 110 :attribute :artist/name :fmt "edn"}}))]
        (pp/pprint result)
        (->>
         identity
        ;  (first (keys (reader/read-string result)))
        ;  (first (keys  result))
        ;  :id
        ;  pp/pprint
         )))  
  
  
  
  )