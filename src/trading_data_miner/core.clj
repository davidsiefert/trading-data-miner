(ns trading-data-miner.core
  (:require [trading-data-miner.stock-data :as sd]))

(defn -main
  [& args]
  (prn (sd/scan (sd/load-expd-series))))
