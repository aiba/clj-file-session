(ns clj-file-session.core
  (:use [ring.middleware.session.store :only [SessionStore]]))

;===============================================================================
; File-based session store, cached in ram.
;===============================================================================

(defn- fss-read-map [f]
  (if (.exists f)
    (read-string (slurp f))
    {}))

(defn- fss-write-map [f m]
  (spit f (pr-str m)))

(defn- fss-key [k]
  (cond
    (keyword? k) k
    (string? k) (keyword (last (.split k ":")))
    :else nil))

; Creates a new SessionStore saved to a file and cached in RAM.
(defn file-session-store [file]
  (let [file (clojure.java.io/file file) ; convert to java File
        cache (atom (fss-read-map file))]
    (add-watch cache :writer
               (fn [k r o n] (fss-write-map file n)))
    (reify
      SessionStore
      (read-session [_ k]
        (get @cache (fss-key k) {}))
      (write-session [_ k data]
        (let [k (fss-key (or k (str (java.util.UUID/randomUUID))))]
          (swap! cache assoc k data)
          (name k)))
      (delete-session [_ k]
        (swap! cache dissoc (fss-key k))
        nil))))

