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

(defn name-freq [pubs]
  (->> pubs
       (map :name)
       (map clean-name)
       frequencies
       (sort-by val)
       reverse))

(def name-len
  (->> pubmaps
       (map :name)
       (map clean-name)
       (sort-by count)
       reverse))

(defn top-names [n]
  (take n (name-freq pubmaps)))

(defn top-by-la [la n]
  (->> pubmaps
       (filter #(= la (:la %)))
       name-freq
       (take n)))

 (defn top-names-len [n]
  (take n name-len))

 (defn -main
   "I don't do a whole lot ... yet."
   [& args]
   (println "=======")
   (println "Most common")
   (println "=======")
   (->> (top-names 10)
        (map println)
        dorun)
   (println "=======")
   (println "Longest")
   (println "=======")
   (mapv println (top-names-len 10))
   (println "=======")
   (println "Top by Local auth")
   (println "=======")
   (->> (top-by-la "Westminster" 10)
        (map println)
        dorun))
