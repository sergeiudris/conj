(ns aq.aq
  (:require [aq.nrepl]
            ; [dq.psql]
            [cors.server]
            [aq.conn :refer [conn db]]
            ; [dq.server]
            ; [dq.schema]
            ; [dq.query]
            ; [dq.etl]
            ))

(defn -main []
  (prn conn)
  (aq.nrepl/-main)
  (cors.server/run-dev)
  ; (aq.server/start)
  )

