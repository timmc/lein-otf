(ns leiningen.uberjar-otf
  (:require [clojure.java.io :as io])
  (:use [leiningen.uberjar :only [uberjar]]))

(def source-path "target/lein-otf")
(def stub-name "lein_otf/loader/Stub.java")
(def stub-source (io/resource stub-name))
(def stub-dest (io/file source-path stub-name))

(defn juggle
  "Put in a loader namespace for :main and put the real main namespace in a
manifest field. The lein-otf loader should already be present as a dependency."
  [project]
  (if-let [real (:main project)]
    (-> project
        (dissoc ,,, :main)
        (update-in ,,, [:java-source-paths] (fnil #(conj % source-path) []))
        (assoc-in ,,, [:manifest "lein-otf-real-main"] (str real))
        (assoc-in ,,, [:manifest "Main-Class"] "lein_otf.loader.Stub"))
    project))

(defn uberjar-otf
  "Build an uberjar with on-the-fly (OTF) Clojure compilation."
  [project & args]
  (io/make-parents stub-dest)
  (when-not (.isFile stub-dest)
    (with-open [source (io/reader stub-source)]
      (io/copy source stub-dest)))
  (let [new-meta (update-in (meta project) [:without-profiles] juggle)
        project (with-meta (juggle project) new-meta)]
    (apply uberjar project args)))
