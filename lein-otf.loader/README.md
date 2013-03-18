# lein-otf.loader

An injectable dependency for lein-otf, for use in dynamic loading of Clojure.

When lein_otf.loader.Stub's main method is called, it checks the JAR file's
manifest for an attribute containing a Clojure namespace (and optionally
a var name, defaulting to -main), asks Clojure to load that var, then calls
the var with the arguments that were passed in.

## Building

Buildable with Leiningen 1.x or 2.x, although the latter is recommended.

## Changelog

### v1.0.0

* First release
* Uses lein-otf-real-main attribute in manifest.mf
* Accepts either `foo.core` or `foo.core/alt-main` format.

### v1.0.1

* No source change: Corrected project.clj deps to include Clojure.
