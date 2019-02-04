(ns wui.subs
  (:require
   [re-frame.core :as re-frame]))

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
   (:entities db)))

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
    (->> (:entities data)
         first
         keys
         (map (fn [key] {:title key :dataIndex key}))
         (into [])
         tap
         )
    )
   ))


   
  ;  (map fn [entity] {:title (get-in data [:request-data :attribute])
  ;                    :dataIndex 
  ;                    }data )))
