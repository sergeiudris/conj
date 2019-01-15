(defproject lein "0.1.0-SNAPSHOT"
  :plugins [[cider/cider-nrepl "0.18.0"]
            [nightlight/lein-nightlight "RELEASE"]]
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [cider/piggieback "0.3.10"]
                 [figwheel-sidecar "0.5.16"]
                 [com.datomic/datomic-free "0.9.5544"]
                 [commons-codec "1.7"]]
  :repl-options {:init-ns lein.core
                ;  :nrepl-middleware [cider.piggieback/wrap-cljs-repl]
                 })
