package lein_otf.loader;

import java.net.JarURLConnection;
import java.util.jar.Manifest;
import java.util.jar.Attributes;
import clojure.lang.RT;
import clojure.lang.Symbol;
import clojure.lang.Var;
import clojure.lang.ArraySeq;

class Stub {

private static final String RESOURCE = "/lein_otf/loader/Stub.class";

private static Manifest
getManifest() throws Exception {
    JarURLConnection conn =
        (JarURLConnection) Stub.class.getResource(RESOURCE).openConnection();
    return conn.getManifest();

}

private static String
getReal() throws Exception {
    return getManifest().getMainAttributes().getValue("lein-otf-real-main");
}

public static void
main(String[] args) throws Exception {
    String ns = getReal();
    RT.var("clojure.core", "require").invoke(Symbol.intern(ns));
    RT.var(ns, "-main").applyTo(ArraySeq.create((Object[]) args));
}

}
