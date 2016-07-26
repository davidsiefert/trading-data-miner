(ns trading-data-miner.stock-data-test
  (:require [clojure.test :refer :all]
            [trading-data-miner.stock-data :refer :all]))

(deftest load-expd-series-kinda-works
  (testing "load-expd-series loads *something* (this is more of a placeholder than a test)"
    (is (= 5 (count (take 5 (load-expd-series)))))))
