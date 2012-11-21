(ns lein-otf.loader)

(def resource
  "/lein-otf-real-main.clj")

(defn -main
  [& args]
  (let [main (-> lein_otf.loader.Stub (.getResource resource)
                 .openConnection .getInputStream slurp)
        [ns sym] (.split main "/" 2), sym (or sym "-main")]
    (require (symbol ns))
    (apply (ns-resolve *ns* (symbol ns sym)) args)))
