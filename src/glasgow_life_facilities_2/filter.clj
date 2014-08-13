(ns glasgow-life-facilities-2.filter
  (:require [grafter.rdf.protocols :as pr]
            [grafter.rdf.validation :refer [blank?]]
            [grafter.rdf.ontologies.vcard :refer :all]
            [grafter.rdf.ontologies.os :refer :all]))

(defn filter-triples [triples]
  (filter #(not (and (#{vcard:postal-code os:postcode vcard:hasUrl} (pr/predicate %1))
                     (blank? (pr/object %1)))) triples))
