(ns wui.views
  (:require
   [re-frame.core :as re-frame]
   [re-com.core :as re-com]
   [re-pressed.core :as rp]
   [cljs.repl :refer [doc source]]
   [wui.subs :as subs]
   [re-frame-datatable.core :as dt]
   [re-frame-datatable.views :as dtv]
   [antizer.reagent :as ant]
   [reagent.core :as r]
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




;; people data
(def people [{:id 1 :name "Tracey Davidson" :age 43 :address "5512 Pockrus Page Rd"}
             {:id 2 :name "Pierre de Wiles" :age 41 :address "358 Fermat's St"}
             {:id 3 :name "Lydia Weaver" :age 23 :address "1251 Fourth St"}
             {:id 4 :name "Willie Reynolds" :age 26 :address "2984 Beechcrest Rd"}
             {:id 5 :name "Richard Perelman" :age 51 :address "2003 Poincaré Ricci Rd"}
             {:id 6 :name "Srinivasa Ramanujan" :age 32 :address "1729 Taxi Cab St"}
             {:id 7 :name "Zoe Cruz" :age 31 :address "8593 Pine Rd"}
             {:id 8 :name "Adam Turing" :age 41 :address "1936 Automata Lane"}])

(defn comparison [data1 data2 field]
  (compare (get (js->clj data1 :keywordize-keys true) field)
           (get (js->clj data2 :keywordize-keys true) field)))


;; we need to use dataIndex instead of data-index, see README.md
(def columns [{:title "Name" :dataIndex "name" :sorter #(comparison %1 %2 :name)}
              {:title "Age" :dataIndex "age" :sorter #(comparison %1 %2 :age)}
              {:title "Address" :dataIndex "address" :sorter #(comparison %1 %2 :address)}])




(def pagination {:show-size-changer true
                 :default-page-size 5
                 :page-size-options ["5" "10" "20"]
                 :position "top"
                 :show-total #(str "Total: " % " users")})

(defn add-actions-column [columns data-atom]
  (conj columns
        {:title "Actions"
         :render
         #(r/as-element
           [ant/button {:icon "delete" :type "danger"
                        :on-click
                        (fn []
                          (reset! data-atom
                                  (remove (fn [d] (= (get (js->clj %2) "id")
                                                     (:id d))) @data-atom)))}])}))

(def log (.-log js/console))

(defn on-table-change [pagination filters sorter extra]
  (re-frame/dispatch [:entity-table-state pagination filters sorter extra]
  )
)

;; ant table
(defn datatable []
  "antd table to rednner people"
  (let [data-default (r/atom people)
        data (re-frame/subscribe [:entities-vector])
        columns (re-frame/subscribe [:entities-columns])
        entities-total-count (re-frame/subscribe [:entities-total-count])
        ]
    (fn []
      ; (log (or data []))
      ; (log @data)
      ; (log @columns)
      ; (log (into [] (or columns [])))
      [:div
       [:h2 "Entity Table"]
       [ant/table
        {:columns @columns ;(add-actions-column columns data)
         :dataSource @data 
         :on-change on-table-change
         :pagination (merge pagination {
                                        :total @entities-total-count
                                        :on-change #(log %1 %2)
                                        })
         :row-key "id"
         :row-selection
         {:on-change
          #(let [selected (js->clj %2 :keywordize-keys true)]
             (ant/message-info (str "You have selected: " (map :name selected))))}}]
       
       ])))

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



(defn tags []
  [:section
   [ant/tag {:color "purple"} ":artist/name"]
   [ant/tag {:color "volcano"} ":track/name"]
   [ant/tag {:color "orange"} ":release/year"]
   [ant/tag {:color "gold"} ":medium/tracks"]
   [ant/tag {:color "lime"} ":label/country"]
   
   ])

(defn entity-buttons []
  [:section
   [ant/button-group 
    {:size "small"
    ;  :on-click #(ant/message-info (.-value (-.target %1)))
    ;  :on-click #(prn (.-nativeEvent  %1))
    ;  :on-click #(log %1)
    ;  :on-click #(-> % .-target .-value log)
     :on-click #(re-frame/dispatch [:get-entities (-> % .-target .-value)])
     }
    [ant/button { :value ":artist/name"} ":artist/name"]
    [ant/button { :value ":release/name"} ":release/name"]
    [ant/button {:value ":label/country"} ":label/country"]
    [ant/button {:value ":track/name"} ":track/name"]
    
    
    
    ; [ant/button {:value ":medium/format"} ":medium/format"]
    ; [ant/button {:value ":medium/tracks"} ":medium/tracks"]
    ]
   ])

(defn click-me []
  [ant/button
   {:on-click #(ant/message-info "Hello Reagent!")
    :style {:width "100px"}}
   "Click me"])

(defn entity-title []
  [re-com/title
   :label "Entity"
   :level :level1])

(defn entity-panel []
  [re-com/v-box
   :gap "1em"
   :children [[link-to-home-page]
              ; [entity-title]
              [:br] [:br]
              ; [sneak-peek-for-readme]
              ; [table-pagination]
              ; [tags]
              [entity-buttons]
              [datatable]
              ; [click-me]
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


(comment
  
  (+ 1 1)

  (or nil [])
  
  (doc +)
  
  (doc ant/button)

  (source ant/button)
  
  (doc js->clj)
  (merge {:a 3} {:total 2})
  
  
  )