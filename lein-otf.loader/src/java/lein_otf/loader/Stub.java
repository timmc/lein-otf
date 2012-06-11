package lein_otf.loader;

import java.net.JarURLConnection;
import java.util.jar.Manifest;
import java.util.jar.Attributes;
import clojure.lang.RT;
import clojure.lang.Symbol;
import clojure.lang.ArraySeq;

class Stub {

private static final String RESOURCE = "/lein_otf/loader/Stub.class";

private static Attributes
getManifestAttributes() throws Exception {
    final JarURLConnection conn =
        (JarURLConnection)Stub.class.getResource(RESOURCE).openConnection();
    return conn.getManifest().getMainAttributes();
}

private static String[]
getRealMain() throws Exception {
    final Attributes attrs = getManifestAttributes();
    final String main = attrs.getValue("lein-otf-real-main");
    if (main.indexOf("/") >= 0)
        return main.split("/", 2);
    return new String[] { main, "-main" };
}

public static void
main(String[] args) throws Exception {
    final String[] main = getRealMain();
    final String ns = main[0];
    final String f = main[1];
    RT.var("clojure.core", "require").invoke(Symbol.intern(ns));
    RT.var(ns, f).applyTo(ArraySeq.create((Object[]) args));
}

}
