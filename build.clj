(ns build
  (:require [clojure.tools.build.api :as b]
            [org.corfield.build :as bb]
            [clojure.java.shell :refer [sh]]))

(def lib 'com.adamrenklint/preo)
(def version (format "0.1.0"))

(defn- run-proc-fn [proc-fn]
  (let [{:keys [out exit]} (proc-fn)]
    (println out)
    (when (not (zero? exit))
      (throw (Exception. "Process failed")))))

(defn run-tests [opts]
  (println "Running Clojure tests")
  (run-proc-fn #(sh "clj" "-X:test-clj"))
  (println "Running ClojureScript tests")
  (run-proc-fn #(sh "clj" "-M:test-cljs"))
  opts)

(defn release "Run tests and release to clojars" [opts]
  (-> opts
      (assoc :lib lib :version version)
      (run-tests)
      (bb/clean)
      (bb/jar)
      (bb/deploy)))
