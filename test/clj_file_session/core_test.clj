(ns clj-file-session.core-test
  (:use clojure.test
        [ring.middleware reload session]
        clj-file-session.core)
  (:require [ring.adapter.jetty :as jetty]))

(defn handle-request [req]
  (let [session (req :session)
        x (get session :x 0)]
    {:body (str "x=" x)
     :session {:x (inc x)}}))

(def app
  (let [f (java.io.File/createTempFile "sessions" nil nil)
        _ (println "sessions file:" f)
        _ (spit f "{}")
        fss (file-session-store f)]
    (-> #'handle-request
      (wrap-session {:store fss})
      (wrap-reload))))

(defonce jetty* (atom nil))

(defn run-server []
  (reset! jetty*
          (jetty/run-jetty #'app {:port 9999 :join? false})))

(defn restart-server []
  (.stop @jetty*)
  (run-server))

