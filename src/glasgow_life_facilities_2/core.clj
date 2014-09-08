(ns glasgow-life-facilities-2.core
  (:require [grafter.tabular :refer :all]
            [grafter.rdf.protocols :as pr]
            [grafter.rdf.sesame :as ses]
            [grafter.rdf.validation :refer [has-blank? validate-triples]]
            [glasgow-life-facilities-2.filter :refer [filter-triples]]
            [glasgow-life-facilities-2.make-graph :refer [glasgow-life-facilities-template]]
            [glasgow-life-facilities-2.pipeline :refer [pipeline]]))

(defn import-life-facilities
  [quads-seq destination]
  (let [now (java.util.Date.)
        quads (->> quads-seq
                   filter-triples
                   (validate-triples (complement has-blank?)))]

    (pr/add (ses/rdf-serializer destination) quads)))

(defn -main [& [path output]]
  (when-not (and path output)
    (println "Usage: lein run <input-file.csv> <output-file.(nt|rdf|n3|ttl)>")
    (System/exit 0))
  (-> (open-all-datasets path)
      first
      pipeline
      glasgow-life-facilities-template
      (import-life-facilities output))
  (println path "has been grafted using Grafter 0.2.0!"))
