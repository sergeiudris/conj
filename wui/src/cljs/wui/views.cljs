(ns wui.views
  (:require
   [re-frame.core :as re-frame]
   [re-com.core :as re-com]
   [re-pressed.core :as rp]
   [wui.subs :as subs]
   [re-frame-datatable.core :as dt]
   [re-frame-datatable.views :as dtv]
   ))


;; home

(defn display-re-pressed-example []
  (let [re-pressed-example (re-frame/subscribe [::subs/re-pressed-example])]
    [:div

     [:p
      [:span "Re-pressed is listening for keydown events. A message will be displayed when you type "]
      [:strong [:code "hello"]]
      [:span ". So go ahead, try it out!"]]

     (when-let [rpe @re-pressed-example]
       [re-com/alert-box
        :alert-type :info
        :body rpe])]))

(defn home-title []
  (let [name (re-frame/subscribe [::subs/name])]
    [re-com/title
     :label (str "Hello from " @name ". This is the Home Page.")
     :level :level1]))

(defn link-to-com []
  [re-com/hyperlink-href
   :label "/about"
   :href "#/about"]
  )

(defn link-to-about-page []
  (link-to-com))

(defn link-to-hello-page []
  [re-com/hyperlink-href
   :label "/hello"
   :href "#/hello"])

(defn link-to-entity-page []
  [re-com/hyperlink-href
   :label "/entity"
   :href "#/entity"])


(defn home-panel []
  [re-com/v-box
   :gap "1em"
   :children [[home-title]
              [link-to-about-page]
              [link-to-hello-page]
              [link-to-entity-page]
              [display-re-pressed-example]
              ]])

;; about

(defn about-title-com []
  [re-com/title
   :label "This is the About Page"
   :level :level1]
  )


(defn about-title []
  (about-title-com))

(defn link-to-home-page []
  [re-com/hyperlink-href
   :label "go to Home Page"
   :href "#/"])

(defn about-panel []
  [re-com/v-box
   :gap "1em"
   :children [[about-title]
              [link-to-home-page]]])

;; entity

(defn sneak-peek-for-readme []
  [dt/datatable
   :songs
   [::subs/songs-list]
   [{::dt/column-key   [:index]
     ::dt/sorting      {::dt/enabled? true}
     ::dt/column-label "#"}
    {::dt/column-key   [:name]
     ::dt/column-label "Name"}
    {::dt/column-key   [:stats :play_count]
     ::dt/column-label "Duration"
     ::dt/sorting      {::dt/enabled? true}
     ::dt/render-fn    (fn [val]
                         [:span
                          (let [m (quot val 60)
                                s (mod val 60)]
                            (if (zero? m)
                              s
                              (str m ":" (when (< s 10) 0) s)))])}]
   {::dt/pagination    {::dt/enabled? true
                        ::dt/per-page 5}
    ::dt/table-classes ["ui" "table" "celled"]}
   ])

(defn table-pagination []
  [dtv/default-pagination-controls :pagination [::subs/songs-list]])

(defn entity-title []
  [re-com/title
   :label "Entity"
   :level :level1])

(defn entity-panel []
  [re-com/v-box
   :gap "1em"
   :children [[link-to-home-page]
              [entity-title]
              [sneak-peek-for-readme]
              [table-pagination]
              ]])

;; main

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :about-panel [about-panel]
    :entity-panel [entity-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [re-com/v-box
     :height "100%"
     :children [[panels @active-panel]]]))
