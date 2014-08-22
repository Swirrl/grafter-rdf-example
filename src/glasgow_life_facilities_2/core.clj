(ns glasgow-life-facilities-2.core
  (:require [grafter.tabular :refer :all]
            [grafter.rdf.protocols :as pr]
            [grafter.rdf.sesame :as ses]
            [grafter.rdf.validation :refer [has-blank? validate-triples]]
            [glasgow-life-facilities-2.filter :refer [filter-triples]]
            [glasgow-life-facilities-2.make-graph :refer [glasgow-life-facilities-template]]
            [glasgow-life-facilities-2.pipeline :refer [pipeline]]))

(defonce my-repo (-> "./tmp/grafter-sesame-store-2" ses/native-store ses/repo))

(defn import-life-facilities
  [quads-seq destination]
  (let [now (java.util.Date.)
        quads (->> quads-seq
                   filter-triples
                   (validate-triples (complement has-blank?)))]

    (pr/add (ses/rdf-serializer destination) quads)))

(defn -main [path output]
  (-> (open-all-datasets path)
      first
      pipeline
      glasgow-life-facilities-template
      (import-life-facilities output))
  (println path "has been grafted using Grafter 0.2.0!"))


