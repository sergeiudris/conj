(ns wui.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [re-pressed.core :as rp]
   [wui.events :as events]
   [wui.routes :as routes]
   [wui.views :as views]
   [wui.config :as config]
   [wui.io :as io]
   [cljs.spec.alpha :as s]
   [goog.events :as gevents]
   [day8.re-frame.http-fx]
   ))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch-sync [::rp/add-keyboard-event-listener "keydown"])
  (re-frame/dispatch-sync [::events/set-active-panel :entity-panel])
  (dev-setup)
  (mount-root))

