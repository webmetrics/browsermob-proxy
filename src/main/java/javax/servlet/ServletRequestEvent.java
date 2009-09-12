/**
 *
 * Copyright 2003-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

//
// This source code implements specifications defined by the Java
// Community Process. In order to remain compliant with the specification
// DO NOT add / change / or delete method signatures!
//

package javax.servlet;


/**
 * This is the event class for notifications about changes to the
 * servlet request of a web application.
 *
 * @see ServletRequestListener
 *
 * @since Servlet2.4
 *
 * @version $Rev: 46019 $ $Date: 2005/03/15 10:03:20 $
 */
public class ServletRequestEvent extends java.util.EventObject {
    private ServletRequest request;

    /**
     * Construct a ServletRequestEvent from the given context.
     *
     * @param sc the ServletContext of the web application.
     * @param request the ServletRequest that is sending the event.
     */
    public ServletRequestEvent(ServletContext sc, ServletRequest request) {
        super(sc);
        this.request = request;
    }

    /**
     * Return the ServletRequest that changed.
     *
     * @return the ServletRequest that sent the event.
     */
    public ServletRequest getServletRequest() {
        return this.request;
    }

    /**
     * Return the ServletContext that changed.
     *
     * @return the ServletContext of the web application.
     */
    public ServletContext getServletContext() {
        return (ServletContext) super.getSource();
    }
}
