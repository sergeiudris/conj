(ns core.rabbitmq
  (:require [clojure.repl :refer :all]
            [clojure.pprint :as pp]
            
            [langohr.core :as rmq]
            [langohr.channel :as lch]
            [langohr.queue :as lq]
            [langohr.basic :as lb]
            [langohr.consumers :as lc]
            ))

(defn rmq-connect []
  (rmq/connect {:host "rabbitmq" :port 5672}))

(comment
  (+)
  
  (doc lc/connect)
  
  ;; send
  (defn rmq-send
    [& args]
    (with-open [conn (rmq-connect)]
      (let [ch   (lch/open conn)]
        (lq/declare ch "hello" {:durable false :auto-delete false})
        (lb/publish ch "" "hello" (.getBytes "Hello World!" "UTF-8"))
        (println " [x] Sent 'Hello World!'"))))
  
  (rmq-send)
  
  ;; receive
  (defn handle-delivery
    "Handles message delivery"
    [ch metadata payload]
    (println (format " [x] Received %s" (String. payload "UTF-8"))))
  
  
  (defn rmq-receive
    [& args]
    (with-open [conn (rmq-connect)]
      (let [ch   (lch/open conn)]
        (lq/declare ch "hello" {:durable false :auto-delete false})
        (println " [*] Waiting for messages. To exit press CTRL+C")
        (lcons/subscribe ch "hello" handle-delivery {:auto-ack true}))))
  
  (rmq-receive)

  ;; hello world
  
  (def ^{:const true}
    default-exchange-name "")
  

  (defn message-handler
    [ch {:keys [content-type delivery-tag type] :as meta} ^bytes payload]
    (println (format "[consumer] Received a message: %s, delivery tag: %d, content type: %s, type: %s"
                     (String. payload "UTF-8")
                     delivery-tag
                     content-type
                     type)))

  (defn start-consumer
    "Starts a consumer in a separate thread"
    [ch queue-name]
    (lc/subscribe ch queue-name message-handler {:auto-ack true}))

  (defn rmq-hello-world
    [& args]
    (let [conn  (rmq-connect)
          ch    (lch/open conn)
          qname "langohr.examples.hello-world"]
      (println (format "[main] Connected. Channel id: %d" (.getChannelNumber ch)))
      (lq/declare ch qname {:exclusive false :auto-delete true})
      ; (start-consumer ch qname)
      (println "[main] Publishing...")
      (lb/publish ch default-exchange-name qname "Hell!!o!" {:content-type "text/plain" :type "greetings.hi"})
      (Thread/sleep 2000)
      (println "[main] Disconnecting...")
      (rmq/close ch)
      (rmq/close conn)))  

  (rmq-hello-world)
  
  
  )