(ns lein-otf.loader
  (:require [clojure.java.io :as io]))

(def ^:const stub-class-resource
  "Resource path for the stub Java loader class."
  "lein_otf/loader/Stub.class")

(def ^:const real-main-resource
  "Resource path for the injected real main symbol."
  "lein-otf-real-main.clj")

(def real-main
  "Delay loading real main function from JAR manifest or resource file."
  (delay
   (let [url (or (io/resource real-main-resource)
                 (io/resource stub-class-resource))
         main (if (= "jar" (.getProtocol url))
                (-> url .openConnection .getManifest .getMainAttributes
                    (.getValue "lein-otf-real-main"))
                (slurp url))
         [ns sym] (.split main "/" 2), sym (or sym "-main")]
     [ns sym])))

(defn -main
  [& args]
  (let [[ns sym] @real-main]
    (require (symbol ns))
    (apply (ns-resolve *ns* (symbol ns sym)) args)))
