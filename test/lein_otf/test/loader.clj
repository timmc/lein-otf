(ns lein-otf.test.loader
  (:use clojure.test
        lein-otf.loader))

(deftest interpret
  (is (= (interpret-real-main "foo/bar") ['foo 'bar]))
  (is (= (interpret-real-main "foo") ['foo '-main]))
  (is (thrown? RuntimeException (= (interpret-real-main "foo/bar/baz")))))
