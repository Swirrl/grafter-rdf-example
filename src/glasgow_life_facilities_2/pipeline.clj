(ns glasgow-life-facilities-2.pipeline
  (:require [grafter.tabular :refer [column-names derive-column mapc swap drop-rows _]]
            [grafter.tabular.common :refer [open-all-datasets make-dataset move-first-row-to-header]]
            [grafter.parse :refer [date-time]]
            [glasgow-life-facilities-2.prefixers :refer :all]))

(defn replace-header [header]
  (fn [ds]
    (make-dataset header ds)))

(defn pipeline [path]
  (-> (open-all-datasets path)
      first
      (make-dataset ["facility-description" "facility-name" "monthly-attendence" "month" "year" "address" "town" "postcode" "website"])
      (drop-rows 1)
      (derive-column "facility-type" ["facility-description"] uriify-type)
      (derive-column "name-slug" ["facility-name"] slugify)
      (mapc {"facility-description" uriify-facility
             "facility-name" _
             "monthly-attendence" parse-attendance
             "month" convert-month
             "year" parse-year
             "address" address-line
             "town" city
             "postcode" post-code
             "website" url})
      (derive-column "ref-facility-uri" ["facility-type" "name-slug"] uriify-refFacility)
      (derive-column "postcode-uri" ["postcode"] uriify-pcode)
      (swap "month" "year")
      (derive-column "date" ["year" "month"] date-time)
;;       (derive-column "date" ["year" "month"] date-time)
;;       (derive-column "date" ["year" "month"] date-time)
      ))






