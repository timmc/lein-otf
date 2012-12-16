# lein-otf

Leiningen plugin to produce OTF-compiled uberjars. (OTF = on-the-fly)

An uberjar's main class must be AOT-compiled for the jar to work as an
executable. Since AOT compilation is infectious (or "transitive"), most or all
of the jar is also AOT'd, along with any linked libraries. This reduces
portability.

lein-otf works by injecting a sacrificial loader class that is AOT compiled.
This class inspects the jar's manifest file for an attribute (injected when the
jar was created) bearing the name of the :main namespace, and calls
clojure.main/main -m with that namespace. Because the loader is not statically
linked against the rest of your codebase, the AOT compilation is restricted
to the loader and any namespaces asking for gen-class (and anything they link
against, of course.)

## Usage

Compatible with Leiningen 1.x projects using Clojure 1.2.x and 1.3.0.

1. Specify the plugin as a development dependency:
   `:dev-dependencies [[org.timmc/lein-otf "1.2.0"]]`
2. Take :gen-class out of your main namespace, but leave project.clj's :main
   pointing to it. **NB**: Assumes main function is called `-main`.
3. Get the plugin and use it!
   `$ lein uberjar-otf`

## Changelog

Please ignore [org.timmc/lein-jit "0.0.1"], which mysteriously stopped working
after release and had a bad name anyhow.

### v1.0.0

* Provides `lein uberjar-otf` command. Works on Clojure v1.3.0.

### v1.1.0

* Extends support back to projects using Clojure v1.2.0.

### v1.2.0

* Warns on missing :main
* Likely the last minor release on 1.x branch.

### v1.2.1

* Major bugfix: Was always rejecting :main in target project.

## License

Copyright (C) 2012 Tim McCormack

Contains material from `org.clojure/clojure` project v1.3.0.

Distributed under the Eclipse Public License v1.0, the same as Clojure.
License text is provided in `./epl-v1.0.txt`.
