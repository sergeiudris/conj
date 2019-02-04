(ns wui.events
  (:require
   [re-frame.core :as re-frame]
   [wui.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced defn-traced]]
   [ajax.core :as ajax]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-active-panel
 (fn-traced [db [_ active-panel]]
   (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
 ::set-re-pressed-example
 (fn [db [_ value]]
   (assoc db :re-pressed-example value)))

(re-frame/reg-event-fx
 :get-entities
 (fn [{:keys [db]} [_ a]] 
   {:http-xhrio {:method :get
                 :uri "http://localhost:8893/entity"
                 :response-format (ajax/raw-response-format)
                 :on-success [:process-response]
                 :on-fail [:failed-response]}
    :db (assoc db :flag true)
    }
   )
)


(re-frame/reg-event-db
 :process-response
 (fn [db [_ value]]
   (prn value)
   (assoc db :entities value)
   ))

(re-frame/reg-event-db
 :failed-response
 (fn [db [_ value]]
   (assoc db :entities-failed value)))


(comment
  
  *1
  )