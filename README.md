# lein-otf

Leiningen plugin to produce OTF-compiled uberjars. (OTF = on-the-fly)

## The problem

An uberjar's main class must be compiled ahead of time ("AOT") for
the jar to work as an executable. Since AOT compilation is infectious
(or "transitive"), most or all of the jar is also AOT'd, along with
any linked libraries. This reduces portability.

## The solution

lein-otf works by injecting a sacrificial Java loader class.  This class
inspects the JAR's manifest file for the `lein-otf-real-main` attribute
(injected when the JAR was created), which bears the name of the
`:main` namespace or var that was specified in the dependent project.
The loader stub then calls that namespace's `-main` (or other explicitly
named) function.  Because the loader is not statically linked against
the rest of your codebase, AOT compilation is restricted to
namespaces explicitly mentioned in `:aot` (and anything they link
against, of course.)

## Usage

Compatible with projects using Clojure 1.3.0 and 1.4.0.  Requires Leiningen 2.0
or greater (including preview versions).

1. Specify the plugin as a plugin:
   `:plugins [[org.timmc/lein-otf "2.0.1"]]`
2. Take `:gen-class` out of your main namespace.  Leave project.clj's `:main`
   pointing to it, but add `^:skip-aot` metadata on the namespace symbol.
3. Run the `uberjar` task, or anything else which invokes it:
   `$ lein uberjar`

As with Leiningen itself, lein-otf assumes that the main fn is called `-main`
unless it is explicitly specified, e.g. `foo.core/alt-main`.

## Changelog

### v1.0.0

* Provides `lein uberjar-otf` command. Works on Clojure v1.3.0.

### v1.1.0

* Extends support back to projects using Clojure v1.2.0.

### v1.2.0

* Warns on missing :main
* Likely the last minor release on 1.x branch.

### v1.2.1

* Major bugfix: Was always rejecting :main in target project.

### v1.3.0

* Allow :main to have a var name as well, e.g. `foo.core/alt-main`. If
  :main does not contain a slash, var name defaults to `-main`. This gives
  parity with Leiningen itself.

### v2.0.0

* Support and require Leiningen 2.0.  Switch to implementation using separated
  loader stub dependency and Leiningen 2 automatic hooks and middleware.  No
  `:hooks` entry is necessary.  Support (deprecated) `uberjar-otf` task as
  alias for `uberjar`.

### v2.0.1

* No source change. Depend on lein-otf.loader v1.0.1 (patch version bump.)

## License

Copyright (C) 2012–2013 Tim McCormack & Marshall Vandegrift

Distributed under the Eclipse Public License v1.0, the same as Clojure.
License text is provided in `./epl-v1.0.txt`.
