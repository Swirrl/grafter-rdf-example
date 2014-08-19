(ns glasgow-life-facilities-2.core
  (:require [grafter.tabular :refer :all]
            [grafter.rdf.protocols :as pr]
            [grafter.rdf.sesame :as ses]
            [grafter.rdf.validation :refer [has-blank? validate-triples]]
            [glasgow-life-facilities-2.filter :refer [filter-triples]]
            [glasgow-life-facilities-2.make-graph :refer [make-life-facilities]]))

(defonce my-repo (-> "./tmp/grafter-sesame-store-test" ses/native-store ses/repo))

(defn import-life-facilities
  [quads-seq destination]
  (let [now (java.util.Date.)
        quads (->> quads-seq
                   filter-triples
                   (validate-triples (complement has-blank?)))]

    (pr/add (ses/rdf-serializer destination) quads)))

(defn -main [path output]
  (import-life-facilities (make-life-facilities path) output)
  (println path "has been grafted using Grafter 0.2.0!"))


