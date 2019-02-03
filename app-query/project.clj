(defproject dq.app-query "0.1.0-SNAPSHOT"
  ; :main dq.dq
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
                 [org.clojure/core.async "0.4.490"]
                 [ch.qos.logback/logback-classic "1.1.7" :exclusions [org.slf4j/slf4j-api]]
                 [org.slf4j/jul-to-slf4j "1.7.21"]
                 [org.slf4j/jcl-over-slf4j "1.7.21"]
                 [org.slf4j/log4j-over-slf4j "1.7.21"]
                 [ring-cors/ring-cors "0.1.13"]
                 ]
  ; :min-lein-version "2.8.3"
  :resource-paths ["config", "resources"]
  :repl-options {:init-ns dq.dq
                 :main dq.dq
                ;  :nrepl-middleware [cider.piggieback/wrap-cljs-repl]
                 }
  :profiles {:dev {:aliases {"run-dev" ["trampoline" "run" "-m" "aq.aq/-main"]}
                   :dependencies [[io.pedestal/pedestal.service-tools "0.5.5"]]}}
  :main ^{:skip-aot true} aq.aq
  
  )
