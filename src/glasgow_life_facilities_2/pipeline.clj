(ns glasgow-life-facilities-2.pipeline
  (:require [grafter.tabular :refer [column-names derive-column mapc swap drop-rows _]]
            [grafter.tabular.common :refer [open-all-datasets make-dataset move-first-row-to-header]]
            [glasgow-life-facilities-2.prefixers :refer :all]))

(defn replace-header [header]
  (fn [ds]
    (make-dataset header ds)))

(defn pipeline [path]
  (-> (open-all-datasets path)
      first
      ;(make-dataset [:foo :bar :baz])
      :column-names
      ))
