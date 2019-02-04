(ns wui.db)

(def default-db
  {:name "re-frame"
  ;  :songs-list [{:index 1 :name "Mister Sandman" :duration 136}]
   :songs-list [{:name "Black Porch Bogie"
                 :index 1
                 :stats {:play_count 2, :rating 5}}
                {:name "Georgia on my Mind", :index 2, :stats {:play_count 0}}
                {:name "Salty Dog Rag", :index 3, :stats {:play_count 1, :rating 2}}
                {:name "Rattenbury Rag", :index 4, :stats {:play_count 2, :rating 3}}
                {:name "Steppin' Out", :index 5, :stats {:play_count 0}}
                {:name "Black Mountain Rag"
                 :index 6
                 :stats {:play_count 3, :rating 4}}
                {:name "Flight of the Humminbird", :index 7, :stats {:play_count 0}}
                {:name "Limehouse Blues", :index 8, :stats {:play_count 1}}
                {:name "Linus and Lucy", :index 9, :stats {:play_count 5, :rating 5}}
                {:name "Mostly Merle", :index 10, :stats {:play_count 2}}
                {:name "Mozard 101", :index 11, :stats {:play_count 11, :rating 4}}
                {:name "Wizard of Oz Medley", :index 12, :stats {:play_count 4}}
                {:name "Knights of the Round Table"
                 :index 13
                 :stats {:play_count 2}}]
   })
