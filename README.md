# lein-otf

Leiningen plugin to produce OTF-compiled uberjars. (OTF = on-the-fly)

An uberjar's main class must be AOT-compiled for the jar to work as an
executable. Since AOT compilation is infectious (or "transitive"), most or all
of the jar is also AOT'd, along with any linked libraries. This reduces
portability.

lein-otf works by injecting a sacrificial Java loader class.  This class
inspects the JAR's manifest file for an attribute (injected when the JAR was
created) bearing the name of the `:main` namespace then calls that namespace's
`-main` function.  Because the loader is not statically linked against the rest
of your codebase, AOT compilation is restricted to namespaces explicitly
mentioned in `:aot` (and anything they link against, of course.)

## Usage

Compatible with projects using Clojure 1.3.0 and 1.4.0.  Requires Leiningen 2.0
or greater (including preview versions).

1. Specify the plugin as a plugin: 
   `:plugins [[org.clojars.llasram/lein-otf "2.0.0"]]`
2. Add the hooks to your hooks: 
   `:hooks [lein-otf.hooks]`
3. Take `:gen-class` out of your main namespace, but leave project.clj's
   `:main` pointing to it. **NB**: Assumes main function is called `-main`.
4. Run the `uberjar` task, or anything else which invokes it:
   `$ lein uberjar`

## Changelog

Please ignore [org.timmc/lein-jit "0.0.1"], which mysteriously stopped working
after release and had a bad name anyhow.

### v2.0.0

* Support/require Leiningen 2.0.  Switch to hook-based invocation.

### v1.1.0

* Extends support back to projects using Clojure v1.2.0.

### v1.0.0

* Provides `lein uberjar-otf` command. Works on Clojure v1.3.0.

## License

Copyright (C) 2012 Tim McCormack & Marshall Vandegrift

Distributed under the Eclipse Public License v1.0, the same as Clojure.
License text is provided in `./epl-v1.0.txt`.
