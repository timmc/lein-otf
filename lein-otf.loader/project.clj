(defproject org.timmc/lein-otf.loader "1.0.1"
  :url "https://github.com/timmc/lein-otf"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :description "Loader stub for non-AOT Clojure uberjars"
  :scm {:dir ".."}
  ;; Lowest tested-compatible Clojure
  :dependencies [[org.clojure/clojure "1.2.0"]]
  :java-source-paths ["src/java"]
  :source-paths []

  ;; Compatibility for lein 1.x
  :java-source-path "src/java"
  :source-path "src/clj")
