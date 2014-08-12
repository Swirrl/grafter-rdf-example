(ns glasgow-life-facilities-2.core
  (:require [grafter.tabular :refer :all]
            [grafter.rdf.protocols :as pr]
            [grafter.rdf.sesame :as ses]
            [grafter.rdf.validation :refer [has-blank? validate-triples]]))

(defonce my-repo (-> "./tmp/grafter-sesame-store" ses/native-store ses/repo))

(defn import-life-facilities
  [quads-seq destination]
  (let [now (java.util.Date.)
        quads (->> quads-seq
                   filter-triples
                   (validate-triples (complement has-blank?)))]

    (pr/add (ses/rdf-serializer destination) quads)))

(defn -main [my-csv output]
  (println "About to graft " my-csv)
  (import-life-facilities (make-life-facilities my-csv) output)
  (println my-csv "has been grafted: " output))


