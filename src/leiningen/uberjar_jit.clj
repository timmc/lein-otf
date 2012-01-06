(ns leiningen.uberjar-jit
  (:use [leiningen.uberjar :only (uberjar)]))

(defn juggle
  "Put in a loader namespace for :main and put the real main namespace in a
manifest field. The lein-jit loader should already be present as a dependency."
  [project]
  (if-let [real (:main project)]
    (-> project
        (assoc-in ,,, [:main] 'lein-jit.loader)
        (assoc-in ,,, [:manifest "lein-jit-real-main"] (str real)))
    project))

(defn uberjar-jit
  "Plugin entrance point."
  [project & args]
  (apply uberjar (juggle project) args))
