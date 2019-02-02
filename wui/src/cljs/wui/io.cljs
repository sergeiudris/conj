(ns wui.io
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.repl :as repl]
            [cljs.pprint :as pp]
            [cljs.core.async :refer [<! take!]]))


(defn app-pro-get-health []
    (go (let [response (<! (http/get "http://localhost:8891"
                                     {:with-credentials? false
                                      :query-params {"since" 135}}))]
          ; (prn (:status response))
          ; (prn (map :login (:body response)))
          (:body response)
          ))
  )

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
  (repl/doc take!)
  (keys (ns-publics  'cljs.pprint))

  ;; requests  
  
  (go (let [result (<! (github-get-users))]
        (->>
         (first result)
         :id
         pp/pprint)
        ))
  
  
  (go (let [result (<! (app-pro-get-health))]
        (->>
        ;  (first result)
        ;  :id
         pp/pprint)
        ))
  
  )