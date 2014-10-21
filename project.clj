(defproject com.gfredericks/z "0.1.2"
  :description "Complex numbers in Clojure"
  :url "https://github.com/fredericksgary/z"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0-beta2"]
                 [com.gfredericks/lib-4395 "0.1.0"]]
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.5.9"]]}}
  :deploy-repositories [["releases" :clojars]])
