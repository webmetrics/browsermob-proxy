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
 * to the servlet context of the web application they are part of.
 * To recieve notification events, the implementation class
 * must be configured in the deployment descriptor for the web application.
 *
 * @see ServletContextEvent
 *
 * @since Servlet 2.3
 *
 * @version $Rev: 46019 $ $Date: 2005/03/15 10:03:20 $
 */
public interface ServletContextListener extends EventListener {
    /**
     * Notification that the web application initialization
     * process is starting.
     * All ServletContextListeners are notified of context
     * initialisation before any filter or servlet in the web
     * application is initialized.
     */
    public void contextInitialized(ServletContextEvent sce);

    /**
     * Notification that the servlet context is about to be shut down. All servlets
     * have been destroy()ed before any ServletContextListeners are notified of context
     * destruction.
     */
    public void contextDestroyed(ServletContextEvent sce);
}

