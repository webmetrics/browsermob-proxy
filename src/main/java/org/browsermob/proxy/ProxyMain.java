package org.browsermob.proxy;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.browsermob.proxy.util.Log;

import java.util.Arrays;

import static java.util.Arrays.asList;

public class ProxyMain {
    private static final Log LOG = new Log();

    public static void main(String[] args) throws Exception {
        OptionParser parser = new OptionParser();
        ArgumentAcceptingOptionSpec<Integer> portSpec =
            parser.accepts("port", "The port to listen on")
              .withOptionalArg().ofType(Integer.class).defaultsTo(8081);
        parser.acceptsAll(asList("help", "?"), "This help text");

        OptionSet options = parser.parse(args);

        if (options.has("?")) {
          parser.printHelpOn(System.out);
          return;
        }

        LOG.info("Starting up BrowserMob Proxy");
        WebServer.start(portSpec.value(options));
    }
}
