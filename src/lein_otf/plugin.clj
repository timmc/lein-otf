(ns lein-otf.plugin
  (:require [clojure.java.io :as io]
            [robert.hooke :as rh]
            [leiningen.uberjar]))

(defn juggle
  "Put in a loader namespace for :main and put the real main namespace in a
manifest field. The lein-otf loader should already be present as a dependency."
  [main project]
  (if-let [real (or main (:main project))]
    (-> project
        (dissoc ,,, :main)
        (assoc-in ,,, [:manifest "lein-otf-real-main"] (str real))
        (assoc-in ,,, [:manifest "Main-Class"] "lein_otf.loader.Stub"))
    project))

(defn hook-uberjar-otf
  "Build an uberjar with on-the-fly (OTF) Clojure compilation."
  [uberjar project & args]
  (let [[main] args, juggle (partial juggle main)
        new-meta (update-in (meta project) [:without-profiles] juggle)
        project (with-meta (juggle project) new-meta)]
    (apply uberjar project args)))

(defn hooks
  "Apply hook to modify project map when building uberjar."
  [] (rh/add-hook #'leiningen.uberjar/uberjar hook-uberjar-otf))

(defn real-main
  "Extract real main function from project."
  [project]
  (str (or (get-in project [:manifest "lein-otf-real-main"])
           (get-in project [:main]))))

(def resource
  "Resource path for storing real main function name."
  "lein-otf-real-main.clj")

(defn middleware
  "Middleware to inject lein-otf.loader as a dependency."
  [project]
  (-> (update-in project [:dependencies] (fnil conj [])
                 `[org.clojars.llasram/lein-otf.loader "1.0.1"])
      (update-in ,,,,,,, [:filespecs] (fnil conj [])
                 {:type :bytes, :path resource, :bytes (real-main project)})
      (update-in ,,,,,,, [:aliases] assoc "uberjar-otf" "uberjar")))
