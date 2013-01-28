package org.browsermob.proxy.guice;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import cz.mallat.uasparser.OnlineUpdateUASparser;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.browsermob.proxy.http.BrowserMobHttpClient;

import java.io.IOException;

import static java.util.Arrays.asList;

public class ConfigModule implements Module {
    private String[] args;

    public ConfigModule(String[] args) {
        this.args = args;
    }

    @Override
    public void configure(Binder binder) {
        OptionParser parser = new OptionParser();

        ArgumentAcceptingOptionSpec<Integer> portSpec =
                parser.accepts("port", "The port to listen on")
                        .withOptionalArg().ofType(Integer.class).defaultsTo(8080);

        ArgumentAcceptingOptionSpec<Integer> userAgentCacheSpec =
                parser.accepts("uaCache", "The number of days to cache a database of User-Agent records from http://user-agent-string.info")
                        .withOptionalArg().ofType(Integer.class).defaultsTo(1);

        parser.acceptsAll(asList("help", "?"), "This help text");

        OptionSet options = parser.parse(args);

        if (options.has("?")) {
            try {
                parser.printHelpOn(System.out);
                System.exit(0);
            } catch (IOException e) {
                // should never happen, but...
                e.printStackTrace();
            }
            return;
        }

        binder.bind(Key.get(Integer.class, new NamedImpl("port"))).toInstance(portSpec.value(options));

        Integer userAgentCacheDays = userAgentCacheSpec.value(options);
        if (BrowserMobHttpClient.PARSER instanceof OnlineUpdateUASparser) {
            ((OnlineUpdateUASparser) BrowserMobHttpClient.PARSER).setUpdateInterval(1000 * 60 * 60 * 24 * userAgentCacheDays);
        }
    }
}
