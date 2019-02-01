(defproject todomvc-re-frame "0.10.5"
  :dependencies [[org.clojure/clojure        "1.9.0"]
                 [org.clojure/clojurescript  "1.10.339"]
                 [reagent "0.8.1"]
                 [re-frame "0.10.6"]
                 [binaryage/devtools "0.9.9"]
                 [secretary "1.2.3"]
                 [cider/piggieback "0.3.10"]
                 [figwheel-sidecar "0.5.16"]
                 [nrepl "0.5.3"]
                 ]


  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-figwheel  "0.5.18"]]

  :hooks [leiningen.cljsbuild]

  :profiles {:dev  {:cljsbuild
                    {:builds {:client {:compiler {:asset-path           "js"
                                                  :optimizations        :none
                                                  :source-map           true
                                                  :source-map-timestamp true
                                                  :main                 "todomvc.core"}
                                       :figwheel {:on-jsload "todomvc.core/main"
                                                  :websocket-host "0.0.0.0"}}}}
                    :dependencies [[binaryage/devtools "0.9.9"]
                                   [figwheel-sidecar "0.5.16"]
                                   [cider/piggieback "0.3.10"]]
                   ;; need to add dev source path here to get user.clj loaded
                    :source-paths ["src" "dev"]
                    :plugins [[cider/cider-nrepl "0.18.0"]]
                   ;; for CIDER
                   ;; :plugins [[cider/cider-nrepl "0.12.0"]]
                    :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}
                    }

             :prod {:cljsbuild
                    {:builds {:client {:compiler {:optimizations :advanced
                                                  :elide-asserts true
                                                  :pretty-print  false}}}}}}

  :figwheel {:server-port 3450
       ;       :nrepl-host  "0.0.0.0"
       ;       :nrepl-port  7888
       ;       :repl        false
             :repl        true
             }


  :clean-targets ^{:protect false} ["resources/public/js" "target"]

  :cljsbuild {:builds {:client {:source-paths ["src" "../../src"]
                                :compiler     {:output-dir "resources/public/js"
                                               :output-to  "resources/public/js/client.js"}}}})
