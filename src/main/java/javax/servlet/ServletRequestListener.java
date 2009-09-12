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

import java.util.EventListener;

/**
 * Implementations of this interface recieve notifications about changes
 * to the servlet request of the web application they are part of.
 * To receive notification events, the implementation class
 * must be configured in the deployment descriptor for the web
 * application.
 *
 * @see ServletContextEvent
 *
 * @since Servlet 2.4
 *
 * @version $Revision: 1.3 $ $Date: 2004/09/23 08:05:18 $
 */
public interface ServletRequestListener extends EventListener {
    /**
     * Notification that the servlet request is about to go out of scope.
     */
    public void requestDestroyed(ServletRequestEvent sre);

    /**
     * Notification that the servlet request is about to go into scope.
     */
    public void requestInitialized(ServletRequestEvent sre);
}
