(defproject wui "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.238"]
                 [reagent "0.8.1"]
                 [antizer "0.3.1"]
                 [re-frame "0.10.6"]
                 [day8.re-frame/http-fx "0.1.6"]
                 [cljs-ajax "0.8.0"]
                 [com.andrewmcveigh/cljs-time "0.5.2"]
                 [org.clojure/core.async "0.4.490"]
                 [re-com "2.1.0"]
                 [secretary "1.2.3"]
                 [garden "1.3.5"]
                 [ns-tracker "0.3.1"]
                 [re-pressed "0.2.2"]
                 [cljs-http "0.1.45"]
                 [re-frame-datatable "0.6.0"]
                 [binaryage/devtools "0.9.10"]
                 [re-frame-utils "0.1.0"]
                 ]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-garden "0.2.8"]]

  :min-lein-version "2.8.3"

  :source-paths ["src/clj" "src/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "resources/public/css"]



  :garden {:builds [{:id           "screen"
                     :source-paths ["src/clj"]
                     :stylesheet   wui.css/screen
                     :compiler     {:output-to     "resources/public/css/screen.css"
                                    :pretty-print? true}}]}

  :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.10"]
                   [day8.re-frame/re-frame-10x "0.3.3"]
                   [day8.re-frame/tracing "0.5.1"]
                   [figwheel-sidecar "0.5.16"]
                   [cider/piggieback "0.3.10"]]

    :plugins      [[lein-figwheel "0.5.16"]
                   [cider/cider-nrepl "0.18.0"]]}
   
   :prod { :dependencies [[day8.re-frame/tracing-stubs "0.5.1"]]}
                   ;; for CIDER
                   ;; :plugins [[cider/cider-nrepl "0.12.0"]]
   }

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "wui.core/mount-root"
                    :websocket-host "0.0.0.0"}
     :compiler     {:main                 wui.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload
                                           day8.re-frame-10x.preload]
                    :closure-defines      {"re_frame.trace.trace_enabled_QMARK_" true
                                           "day8.re_frame.tracing.trace_enabled_QMARK_" true}
                    :external-config      {:devtools/config {:features-to-install :all}}}
    ;  :figwheel {:on-jsload "wui.core/init"
    ;             :websocket-host "0.0.0.0"}
     
     }

    {:id           "min"
     :source-paths ["src/cljs"]
     :compiler     {:main            wui.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}


    ]}
  
  :figwheel {:css-dirs ["resources/public/css"]
             :server-port 3449
             :nrepl-host  "0.0.0.0"
             :nrepl-port  7888
             :repl        false
            ;  :repl        true
             :nrepl-middleware ["cider.nrepl/cider-middleware"
                            ;     "refactor-nrepl.middleware/wrap-refactor"
                                "cider.piggieback/wrap-cljs-repl"]
             }
  )
