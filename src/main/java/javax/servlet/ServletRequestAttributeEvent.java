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
 * attributes of the servlet request of a web application.
 *
 * @see ServletRequestAttributeListener
 *
 * @since Servlet 2.4
 *
 * @version $Rev: 46019 $ $Date: 2005/03/15 10:03:20 $
 */
public class ServletRequestAttributeEvent extends ServletRequestEvent {
    private String name;
    private Object value;

    /**
     * Construct a ServletRequestAttributeEvent from the given context for the
     * given attribute name and attribute value.
     *
     * @param sc the ServletContext that is sending the event.
     * @param request the ServletRequest that is sending the event.
     * @param name the name of the request attribute.
     * @param value the value of the request attribute.
     */
    public ServletRequestAttributeEvent(ServletContext sc, ServletRequest request, String name, Object value) {
        super(sc, request);
        this.name = name;
        this.value = value;
    }

    /**
     * Return the name of the attribute that changed on the ServletRequest.
     *
     * @return String the name of the changed request attribute.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the value of the attribute that has been added removed or
     * replaced. If the attribute was added, this is the value of the
     * attribute. If the attribute was removed, this is the value of the
     * removed attribute. If the attribute was replaced, this is the old
     * value of the attribute.
     *
     * @return Object the value of the changed request attribute.
     */
    public Object getValue() {
        return this.value;
    }
}
