(ns lein-otf.hooks
  (:require [clojure.java.io :as io]
            [robert.hooke :as rh]
            [leiningen.uberjar]))

(def source-path "target/lein-otf")
(def stub-name "lein_otf/loader/Stub.java")
(def stub-source (io/resource stub-name))
(def stub-dest (io/file source-path stub-name))

(defn juggle
  "Put in a loader namespace for :main and put the real main namespace in a
manifest field. The lein-otf loader should already be present as a dependency."
  [main project]
  (if-let [real (or main (:main project))]
    (-> project
        (dissoc ,,, :main)
        (update-in ,,, [:java-source-paths] (fnil #(conj % source-path) []))
        (assoc-in ,,, [:manifest "lein-otf-real-main"] (str real))
        (assoc-in ,,, [:manifest "Main-Class"] "lein_otf.loader.Stub"))
    project))

(defn hook-uberjar-otf
  "Build an uberjar with on-the-fly (OTF) Clojure compilation."
  [uberjar project & args]
  (io/make-parents stub-dest)
  (when-not (.isFile stub-dest)
    (with-open [source (io/reader stub-source)]
      (io/copy source stub-dest)))
  (let [[main] args, juggle (partial juggle main)
        new-meta (update-in (meta project) [:without-profiles] juggle)
        project (with-meta (juggle project) new-meta)]
    (apply uberjar project args)))

(defn activate []
  (rh/add-hook #'leiningen.uberjar/uberjar hook-uberjar-otf))
