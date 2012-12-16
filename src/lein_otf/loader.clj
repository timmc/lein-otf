(ns lein-otf.loader
  "Loader to be added to projects as a sacrifice to the AOT gods."
  (:require clojure.main)
  (:import (java.net URL JarURLConnection)
           (java.util.jar Manifest Attributes))
  (:gen-class))

(defn ^Manifest get-manifest
  "Retrieve the manifest out of the current JAR file. This is tricky, since it
is easy to accidentally pick up a MANIFEST.MF out of some other jar on the
class path if you use .getResourceAsStream."
  []
  (-> ^Class (class get-manifest) ;; get a class file definitely in *this* jar
      ^URL (.getResource "/lein_otf/loader.class") ;; any resource will do
      ^JarURLConnection (.openConnection)
      (.getManifest)))

(defn interpret-real-main
  "Given the lein-otf-real-main manifest value, produce a coll of the
namespace and main symbols for the real main fn."
  [^String val]
  (let [main-parts (.split val "/")]
    (if (< 2 (count main-parts))
     (throw (RuntimeException.
             (format "lein-otf-real-main has too many slashes: %s" val)))
     (map symbol (take 2 (concat main-parts ["-main"]))))))

(defn get-main-spec
  "Get the spec for the real main fn (as string) from a custom attribute on the
jar manifest. Throws if misisng."
  []
  {:post [(string? %)]}
  (if-let [main (-> (get-manifest)
                    ^Attributes (.getMainAttributes)
                    (.getValue "lein-otf-real-main"))]
    main
    (throw (RuntimeException.
            "Missing 'lein-otf-real-main' manifest attribute."))))

(defn -main
  "Loader entrance point; just relays the call on to the real main fn."
  [& args]
  (let [[main-ns main-name] (interpret-real-main (get-main-spec))]
    (require main-ns)
    (let [main-var (ns-resolve main-ns main-name)]
      (apply main-var args))))
