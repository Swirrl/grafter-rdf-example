(defproject glasgow-life-facilities-2 "0.1.0-SNAPSHOT"
  :description "Example of RDFization using Grafter 0.2.0 on the csv file
  Glasgow Life Facilities"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [grafter "0.2.0-SNAPSHOT"]
                 [org.slf4j/slf4j-jdk14 "1.7.5"]]
  :main glasgow-life-facilities-2.core
  :plugins [[s3-wagon-private "1.1.2"]])
