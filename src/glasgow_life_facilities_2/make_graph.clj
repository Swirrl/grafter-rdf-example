(ns glasgow-life-facilities-2.make-graph
  (:require [clojure.string :as st]
            [grafter.tabular :refer :all]
            [grafter.rdf :refer [prefixer s graph graphify]]
            [grafter.rdf.sesame :as ses]
            [grafter.rdf.ontologies.rdf :refer :all]
            [grafter.rdf.ontologies.void :refer :all]
            [grafter.rdf.ontologies.dcterms :refer :all]
            [grafter.rdf.ontologies.vcard :refer :all]
            [grafter.rdf.ontologies.pmd :refer :all]
            [grafter.rdf.ontologies.qb :refer :all]
            [grafter.rdf.ontologies.os :refer :all]
            [grafter.rdf.ontologies.sdmx-measure :refer :all]
            [glasgow-life-facilities-2.prefixers :refer :all]
            [glasgow-life-facilities-2.pipeline :refer [pipeline]]))

(defn make-life-facilities [path]
  (let [processed-rows (pipeline path)]

         ((graphify [facility-description facility-name monthly-attendence year month address town postcode website facility-type name-slug ref-facility-uri postcode-uri date prefix-date type-name observation-uri]

                    (graph (base-graph "glasgow-life-facilities")
                          [ref-facility-uri
                            [rdfs:label (rdfstr name)]
                            [vcard:hasUrl website]
                            [rdf:a (urban "Museum")]
                            [rdf:a (urban "LeisureFacility")]
                            [vcard:hasAddress [[rdf:a vcard:Address]
                                              [rdfs:label street-address]
                                              [vcard:street-address street-address]
                                              [vcard:locality city]
                                              [vcard:country-name (rdfstr "Scotland")]
                                              [vcard:postal-code postcode]
                                              [os:postcode postcode-uri]]]])

                    (graph (base-graph "glasgow-life-attendances")
                           [observation-uri
                            [(glasgow "refFacility") ref-facility-uri]
                            [(glasgow "numAttendees") attendance]
                            [qb:dataSet "http://linked.glasgow.gov.uk/data/glasgow-life-attendances"]
                            [(sd "refPeriod") "http://reference.data.gov.uk/id/month/2013-09"]
                            [rdf:a qb:Observation]]))

          processed-rows)
         ;processed-rows
         ))
