(ns trading-data-miner.stock-data
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(defn -ohlc-vector-to-map
  [[date open high low close & rest]]
  {:date date
  :open (java.lang.Float/valueOf open)
  :high (java.lang.Float/valueOf high)
  :low (java.lang.Float/valueOf low)
  :close (java.lang.Float/valueOf close)})

(def -skip-csv-header (partial drop 1))

(defn -load-csv-file
  [path] (with-open [in-file (io/reader path)]
           (doall (csv/read-csv in-file))))

(defn load-series
  [path] (map
           -ohlc-vector-to-map
           (-skip-csv-header (-load-csv-file path))))

(defn load-expd-series
  [] (load-series "expd_d.csv"))

(defn close-series
  [s] (map :close s))

(defn -average
  [xs] (/
	(apply + xs)
	(count xs)))

(defn moving-average-series
  [s p] (map
	 -average
	 (partition p 1 (close-series s))))

(defn lowest-low
  [aggs] (apply min (map :low aggs)))

(defn scan-snake-pattern
  [[a10 a9 a8 a7 a6 a5 a4 a3 a2 a1 { date0 :date open0 :open close0 :close } & before]]
  (when
    (and
      ;; just checking new low after 5 bars, and an up close
      (< open0 (lowest-low before))
      (> close0 open0))
    [{:date date0 :diff (- (:open a10) (:open a1))}]))

(defn scan
  [s] (mapcat scan-snake-pattern (partition 61 1 s)))

