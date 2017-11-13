(ns clj-pubs.core
  (:gen-class))

(require '[clojure.data.csv :as csv]
         '[clojure.java.io :as io])

(def pubs (with-open [reader (io/reader "open_pubs.csv")]
            (doall  (csv/read-csv reader))))

(def heading [:fsa-id :name :address :postcode :east :north :lat :long :la])

(defn csv-data->maps [csv-data]
  (map zipmap
       (->> heading ;; First row is the header
            (map keyword) ;; Drop if you want string keys instead
            repeat)
       (rest csv-data)))

(def pubmaps (csv-data->maps pubs))


 (defn -main
             "I don't do a whole lot ... yet."
  [& args]



   (println "most common " (take 5 (reverse (sort-by last (frequencies (map :name pubmaps)))))))
