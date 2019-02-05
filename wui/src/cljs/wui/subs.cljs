(ns wui.subs
  (:require
   [re-frame.core :as re-frame]
   [cljs.repl :as repl]
   [cljs.pprint :as pp]
   [wui.dev :refer [log-idty conlog]]
   ))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))

(re-frame/reg-sub
 ::re-pressed-example
 (fn [db _]
   (:re-pressed-example db)))

(re-frame/reg-sub
 ::songs-list
 (fn [db _]
   (:songs-list db)))


(re-frame/reg-sub
 :entities-data
 (fn [db _]
   ; (prn (:entities-response db) 0.5)
   (->>
   ;  (pp/pprint (get-in db [:entities-response]) )
    (conlog (get-in db [:entities-response]))
    (get-in db [:entities-response :data])
    )
   )
 )

(re-frame/reg-sub
 :entities-vector
 (fn [_ _]  (re-frame/subscribe [:entities-data]))
 (fn [data _]
   (or  (:entities data) [])
   ))

(re-frame/reg-sub
 :entities-columns
 (fn [_ _]  (re-frame/subscribe [:entities-data]))
 (fn [data _]
   (->>  
    (:entities data)
    first
    keys
    (map (fn [key] 
           {
            :title key 
            :dataIndex key 
            :render (fn [text record index] (str text) )}
           ))
    (into [])
    )
   )
 )

(re-frame/reg-sub
 :entities-total-count
 (fn [_ _] (re-frame/subscribe [:entities-data]))
 (fn [data _]
     (:count data)
   )
)

(re-frame/reg-sub
 :entities-table-state
 (fn [db _]
   (:entity-table-state db)))

(re-frame/reg-sub
 :active-attribute
 (fn [db _]
   (:active-attribute db)))


(re-frame/reg-sub
 :entity-request-data
 (fn [_ _]  [(re-frame/subscribe [:active-attribute]) (re-frame/subscribe [:entities-table-state])] )
 (fn [[active-attribute entities-table-state] _]
   (conlog entities-table-state)
   (let [limit (get-in entities-table-state [:pagination :pageSize] 10)
         offset (->> (get-in entities-table-state [:pagination :current] 1) dec (* limit))
         ]
     {:attribute active-attribute
      :limit limit
      :offset offset
      :fmt "edn"} ;{:limit 10 :offset 10 :attribute :artist/name :fmt "edn"}
     )
   )
 )
   
  ;  (map fn [entity] {:title (get-in data [:request-data :attribute])
  ;                    :dataIndex 
  ;                    }data )))


(comment
  
  (repl/doc get-in)
  
  (pp/pprint @re-frame.db/app-db)
  
  (-> js/window .-re_frame .-db .-app_db .-state)
  ;;window.re_frame.db.app_db.state

  
  )