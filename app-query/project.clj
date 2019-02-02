(defproject dq.app-query "0.1.0-SNAPSHOT"
  :main dq.dq
  :plugins [[cider/cider-nrepl "0.18.0"]
            [nightlight/lein-nightlight "RELEASE"]]
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [cider/piggieback "0.3.10"]
                 [figwheel-sidecar "0.5.16"]
                 [nrepl "0.5.3"]
                 [com.datomic/datomic-free "0.9.5656"]
                 [commons-codec "1.7"]
                 [io.pedestal/pedestal.service       "0.5.5"]
                 [io.pedestal/pedestal.service-tools "0.5.5"] ;; Only needed for ns-watching; WAR tooling
                 [io.pedestal/pedestal.jetty         "0.5.5"]
                 [org.clojure/java.jdbc "0.7.8"]
                 [org.postgresql/postgresql "42.2.5.jre7"]
                ;  [io.pedestal/pedestal.immutant      "0.5.5"]
                ;  [io.pedestal/pedestal.tomcat        "0.5.5"]
                ;  [io.pedestal/pedestal.aws           "0.5.5"]
                 ]
  :repl-options {:init-ns dq.dq
                 :main dq.dq
                ;  :nrepl-middleware [cider.piggieback/wrap-cljs-repl]
                 })
