package org.browsermob.proxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** Functions that hope to be useful in dealing with lists of Blocks */
public class ProxyBlockUtil {

    public static List<HttpObject> collapseBlocks(List<Block> blocks) {
        List<HttpObject> httpObjects = new ArrayList<HttpObject>();
        for (Block block: blocks) {
            List<HttpObject> objs = block.getObjects();
            for (HttpObject obj: objs) {
                httpObjects.add(obj);
            }
        }
        Collections.reverse(httpObjects);
        return httpObjects;
    }


    // returns the first match... null if none found
    public static HttpObject getExact(String toFind, List<HttpObject> httpObjects) {
        for (HttpObject obj: httpObjects) {
            if (obj.getUrl().equals(toFind)) {
                return obj;
            }
        }
        return null;
    }

    public static HttpObject getUrlMatch(String toFind, List<HttpObject> httpObjects) {
        for (HttpObject obj: httpObjects) {
            if (obj.getUrl().indexOf(toFind) != -1) {
                return obj;
            }
        }
        return null;
    }
}
