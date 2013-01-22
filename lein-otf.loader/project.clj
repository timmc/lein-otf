(defproject org.timmc/lein-otf.loader "1.0.0"
  :url "https://github.com/timmc/lein-otf"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :description "Loader stub for non-AOT Clojure uberjars"
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :java-source-paths ["src/java"]
  :source-paths []

  ;; Compatibility for lein 1.x
  :java-source-path "src/java"
  :source-path "src/clj")
