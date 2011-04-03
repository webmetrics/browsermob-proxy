package org.browsermob.proxy;

public class FirefoxErrorConstants {
    public static final String SHARED_LONG_DESC = "<ul>\n" +
            "  <li>The site could be temporarily unavailable or too busy. Try again in a few\n" +
            "    moments.</li>\n" +
            "  <li>If you are unable to load any pages, check your computer's network\n" +
            "    connection.</li>\n" +
            "  <li>If your computer or network is protected by a firewall or proxy, make sure\n" +
            "    that Firefox is permitted to access the Web.</li>\n" +
            "</ul>";

    public static final String ERROR_PAGE = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "  <head>\n" +
            "    <title>Problem loading page</title>\n" +
            "    <link rel=\"stylesheet\" href=\"chrome://global/skin/netError.css\" type=\"text/css\" media=\"all\" />\n" +
            "    <link rel=\"icon\" type=\"image/png\" id=\"favicon\" href=\"chrome://global/skin/icons/warning-16.png\"/>\n" +
            "  </head>\n" +
            "\n" +
            "  <body>\n" +
            "\n" +
            "    <!-- PAGE CONTAINER (for styling purposes only) -->\n" +
            "    <div id=\"errorPageContainer\">\n" +
            "    \n" +
            "      <!-- Error Title -->\n" +
            "      <div id=\"errorTitle\">\n" +
            "        <h1 id=\"errorTitleText\">%s</h1>\n" +
            "      </div>\n" +
            "      \n" +
            "      <!-- LONG CONTENT (the section most likely to require scrolling) -->\n" +
            "      <div id=\"errorLongContent\">\n" +
            "      \n" +
            "        <!-- Short Description -->\n" +
            "        <div id=\"errorShortDesc\">\n" +
            "          <p id=\"errorShortDescText\" style=\"white-space: inherit\">\n" +
            "              %s\n" +
            "          </p>\n" +
            "        </div>\n" +
            "\n" +
            "        <!-- Long Description (Note: See netError.dtd for used XHTML tags) -->\n" +
            "        <div id=\"errorLongDesc\">\n" +
            "           %s\n" +
            "        </div>\n" +
            "      </div>\n" +
            "\n" +
            "      <!-- Retry Button -->\n" +
            "      <button id=\"errorTryAgain\">Try Again</button>\n" +
            "\n" +
            "    </div>\n" +
            "\n" +
            "  </body>\n" +
            "</html>";

}
