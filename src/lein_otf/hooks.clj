(ns lein-otf.hooks
  (:require [lein-otf.plugin :as otf]))

(defn activate []
  (println "lein-otf.hooks is deprecated! lein-otf hooking is now automatic.")
  (otf/hooks))
