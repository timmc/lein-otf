package lein_otf.loader;

import clojure.lang.RT;
import clojure.lang.Symbol;
import clojure.lang.ArraySeq;

public class Stub {

private static String NS = "lein-otf.loader";

public static void
main(String[] args) throws Exception {
    RT.var("clojure.core", "require").invoke(Symbol.intern(NS));
    RT.var(NS, "-main").applyTo(ArraySeq.create((Object[]) args));
}

}
