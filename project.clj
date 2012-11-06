(defproject clj-file-session "0.1"
  :description "Simple file-backed session store to be used with
               Ring's wrap-session."
  :url "http://github.com/aiba/clj-file-session"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [ring/ring-core "1.1.6"]
                 [ring/ring "1.1.6" :scope "test"]])

