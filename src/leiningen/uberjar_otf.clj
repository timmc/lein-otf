(ns leiningen.uberjar-otf
  (:use [leiningen.uberjar :only (uberjar)]))

(defn juggle
  "Put in a loader namespace for :main and put the real main namespace in a
manifest field. The lein-otf loader should already be present as a dependency."
  [project]
  (if-let [real (:main project)]
    (-> project
        (assoc-in ,,, [:main] 'lein-otf.loader)
        (update-in ,,, [:aot]
                       (fnil #(conj % 'lein-otf.loader) []))
        (assoc-in ,,, [:manifest "lein-otf-real-main"] (str real)))
    project))

(defn uberjar-otf
  "Plugin entrance point."
  [project & args]
  (apply uberjar (juggle project) args))
