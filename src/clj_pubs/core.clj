(ns clj-pubs.core
  (:gen-class))

(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io])

(def pubs (with-open [reader (io/reader "open_pubs.csv")]
            (doall (csv/read-csv reader))))

(defn -main
             "I don't do a whole lot ... yet."
             [& args]
             (println "hello"))
