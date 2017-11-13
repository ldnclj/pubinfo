(ns clj-pubs.core
  (:gen-class)
  (:require [clojure.string :as str]))

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

(defn clean-name [name]
  (str/replace-first (str/lower-case name) "the " ""))

(def name-freq
  (->> pubmaps
       (map :name)
       (map clean-name)
       frequencies
       (sort-by val)
       reverse))

(defn top-names [n]
  (take n name-freq))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (->> (top-names 10) 
       (map println)
       dorun))
