(ns glasgow-life-facilities-2.prefixers
  (:require [grafter.rdf.protocols :as pr]
            [clojure.string :as st]
            [grafter.rdf :refer [prefixer s]]
            [grafter.rdf.ontologies.rdf :refer :all]
            [grafter.rdf.ontologies.void :refer :all]
            [grafter.rdf.ontologies.dcterms :refer :all]
            [grafter.rdf.ontologies.vcard :refer :all]
            [grafter.rdf.ontologies.pmd :refer :all]
            [grafter.rdf.ontologies.qb :refer :all]
            [grafter.rdf.ontologies.os :refer :all]
            [grafter.rdf.ontologies.sdmx-measure :refer :all]
            [grafter.parse :refer [lift-1 blank-m replacer mapper parse-int date-time]]
            [grafter.js :refer [js-fn]]
            [clojure.algo.monads :refer [m-chain m-bind m-result with-monad identity-m]]))

(def base-uri (prefixer "http://linked.glasgow.gov.uk"))
(def base-graph (prefixer (base-uri "/graph/")))

(def glasgow (prefixer "http://linked.glasgow.gov.uk/def/"))
(def urban (prefixer "http://linked.glasgow.gov.uk/def/urban-assets/"))
(def urban-id (prefixer "http://linked.glasgow.gov.uk/id/urban-assets/"))
(def ont-graph "http://linked.glasgow.gov.uk/graph/vocab/urban-assets/ontology")

(def attendance (prefixer "http://linked.glasgow.gov.uk/data/facility_attendance"))
(def urban:ontology (urban "ontology"))
(def sd (prefixer "http://data.opendatascotland.org/def/statistical-dimensions/"))

(def uriify-facility {"Museums" (urban "Museum")
                      "Arts" (urban "ArtsCentre")
                      "Community Facility" (urban "CommunityFacility")
                      "Libraries" (urban "Library")
                      "Music" (urban "MusicVenue")
                      "Sport Centres" (urban "SportsCentre")})

(defn uriify-refFacility [type name]
  (str (urban-id type) "/" name))

(defn slug-combine [& args]
  (apply str (interpose "/" args)))

(def uriify-type {"Museums" "museums"
                  "Arts" "arts-centres"
                  "Community Facility" "community-facilities"
                  "Libraries" "libraries"
                  "Music" "music-venues"
                  "Sport Centres" "sports-centres"})

(defn date-slug [date]
  (str (.getYear date) "-" (.getMonthOfYear date) "/"))

(def slugify-facility
  (js-fn "function(name) {
              var lower = name.toLowerCase();
              return lower.replace(/\\ /g, '-');
         }"))

(def prefix-facility (prefixer "http://linked.glasgow.gov.uk/data/glasgow-life-attendances/"))

(with-monad blank-m
  (def rdfstr                    (lift-1 (fn [str] (s str :en))))
  (def replace-comma             (lift-1 (replacer "," ""))  )
  (def trim                      (lift-1 st/trim))
  (def parse-year                (m-chain [trim replace-comma parse-int]))
  (def parse-attendance          (with-monad identity-m (m-chain [(lift-1 (mapper {"" "0"}))
                                                                     (lift-1 (replacer "," ""))
                                                                     trim
                                                                     parse-int])))
  (def convert-month             (m-chain [trim
                                              (lift-1 st/lower-case)
                                              (lift-1 {"january" 1 "jan" 1 "1" 1
                                                       "february" 2 "feb" 2 "2" 2
                                                       "march" 3 "mar" 3 "3" 3
                                                       "april" 4 "apr" 4 "4" 4
                                                       "may" 5 "5" 5
                                                       "june" 6 "jun" 6 "6"  6
                                                       "july" 7 "jul" 7 "7"  7
                                                       "august" 8 "aug" 8 "8" 8
                                                       "september" 9 "sep" 9 "sept" 9 "9"  9
                                                       "october" 10 "oct" 10 "10" 10
                                                       "november" 11 "nov" 11 "11" 11
                                                       "december" 12 "dec" 12 "12" 12
                                                       })]))
  (def convert-year              (m-chain [trim parse-int date-time]))
  (def address-line              (m-chain [trim rdfstr]))
  (def city                      (m-chain [trim rdfstr]))
  (def post-code                 (m-chain [trim rdfstr]))
  (def uriify-pcode              (m-chain [trim
                                              (lift-1 (replacer " " ""))
                                              (lift-1 st/upper-case)
                                              (lift-1 (prefixer "http://data.ordnancesurvey.co.uk/id/postcodeunit/"))]))
  (def url                       (lift-1 #(java.net.URL. %)))
  (def prefix-monthly-attendance (m-chain [(lift-1 date-slug)
                                             (lift-1 (prefixer "http://linked.glasgow.gov.uk/data/glasgow-life-attendances/"))])))
