<html>
<head>
    <title>BrowserMob Proxy</title>
    <link rel="stylesheet" type="text/css" href="/jquery-ui/development-bundle/themes/cupertino/jquery-ui-1.7.2.custom.css"/>
    <link rel="stylesheet" type="text/css" href="/proxy.css"/>
    <script type='text/javascript' src="/dwr/interface/ProxyServer.js"></script>
    <script type='text/javascript' src="/dwr/engine.js"></script>
    <script type="text/javascript" src="/jquery-ui/development-bundle/jquery-1.3.2.js"></script>
    <script type="text/javascript" src="/jquery-ui/development-bundle/ui/jquery-ui-1.7.2.custom.js"></script>
    <script type="text/javascript" src="/raphael-0.8.5.js"></script>
    <script type="text/javascript" src="/proxy.js"></script>
</head>

<body>

<h1>BrowserMob Proxy</h1>

<table width="835">
    <tbody>
    <tr>
        <td valign="top">
            <h2>Bandwidth Simulation</h2>

            <div id="bandwidth">
                <div class="label">
                    Speed: <span id="bandwidthValue"></span>KB/sec
                </div>
                <div id="slider"></div>
            </div>

            <h2>Mock Response</h2>

            <div id="mockResponse">
                <table>
                    <tr>
                        <td>
                            <div class="label">
                                <label for="enabled">
                                    Enabled
                                </label>
                            </div>
                        </td>
                        <td>
                            <input id="enabled" type="checkbox"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="label">
                                <label for="pattern">
                                    URL Pattern
                                </label>
                            </div>
                        </td>
                        <td>
                            <input id="pattern" style="width: 300px"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="label">
                                <label for="responseCode">
                                    Response Code
                                </label>
                            </div>
                        </td>
                        <td>
                            <select id="responseCode" style="width: 300px"></select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="label">
                                <label for="time">
                                    Time to Respond
                                </label>
                            </div>
                        </td>
                        <td>
                            <input id="time" style="width: 90px"/> ms
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="label">
                                <label for="contentType">
                                    Content-Type
                                </label>
                            </div>
                        </td>
                        <td>
                            <select id="contentType" style="width: 90">
                                <option>text/html</option>
                                <option>text/javascript</option>
                                <option>text/css</option>
                                <option>text/plain</option>
                                <option>text/xml</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <div class="label">
                                <label for="content">
                                    Mock Response Content
                                </label>
                            </div>
                            <textarea id="content" cols="25" rows="3" style="width: 450px; height: 166px"></textarea>
                        </td>
                    </tr>
                </table>
            </div>
        </td>
        <td valign="top">
            <div style="background-color: #cccccc; margin-left: 5px; padding: 10px">
                <h3 style="margin-top: 0">About This Tool</h3>

                <p>
                    The BrowserMob Proxy is a free, open source tool that helps you alter the normal behavior of your webapp. This is useful for when you want to simulate uncommon situations, such as a slow or unexpected server response.
                </p>
                <p>
                    To get started, simply begin browsing with your browser configured to use the proxy at <strong>localhost port 9638</strong>.
                </p>
                <p>
                    For HTTPS support, you will need to install <a href="/cybervillainsCA.cer">this</a> Certificate Authority in to your browser. Please <strong>remove it</strong> when done testing.
                </p>
                <p>
                    BrowserMob Proxy was created by <a href="http://browsermob.com">BrowserMob</a>, who provides affordable cloud-based performance testing services using real browsers.
                </p>
                <p>
                    <img src="/browsermob_logo.png" alt="Logo"/>
                </p>
            </div>
        </td>
    </tr>
    </tbody>
</table>


<h1>Network Requests</h1>

The last ten blocks of network requests are displayed below. Each block is grouped together by requests that were all within ~1 second of each other. Reload this page to see the latest results, with the most recent block at the top.

<%--<button id="clearButton" class="fg-button ui-state-default ui-corner-all">Clear</button>--%>

<div id="blockTemplate" class="block" style="display:none">
    <table cellpadding="0" cellspacing="0">
        <thead>
        <tr>
            <th width="125"></th>
            <th width="125"></th>
            <th width="75"></th>
            <th width="50"></th>
            <th width="450"></th>
        </tr>
        <tr>
            <td colspan="2" class="blockRow objectCountHeader">
                X objects
            </td>
            <td colspan="2" class="blockRow bytesHeader">
                X KB
            </td>
            <td class="blockRow responseTimeHeader">
                X.Y secs
            </td>
        </tr>
        </thead>
        <tbody>
        <tr style="display: none">
            <td class="path">
                <div class="smallPath">
                    /path
                </div>
                <div class="fullPath" style="display: none">
                    http://example.com/fullPath
                </div>
            </td>
            <td class="responseCode">
                <div>
                    999
                </div>
            </td>
            <td class="host">
                <div>
                    HOST
                </div>
            </td>
            <td class="bytes">
                <div>
                    X KB
                </div>
            </td>
            <td class="chart">
                <div>
                    chart!
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<div id="footer">
    Copyright BrowserMob LLC 2009 | Distributed under the Apache 2.0 License | <a href="http://proxy.browsermob.com">http://proxy.browsermob.com</a><br/>
    This project uses code from the <a href="http://seleniumhq.org">Selenium</a>, <a href="http://mortbay.org">Jetty</a>, <a href="http://jquery.com">jQuery</a>, <a href="http://grinder.sourceforge.net">Grinder</a>, and <a href="http://www.opensymphony.com">OpenSymphony</a> open source projects.
</div>

</body>
</html>
