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

package javax.servlet.http;

import java.util.EventListener;

/**
 * Objects that are bound to a session may listen to container
 * events notifying them that sessions will be passivated and that
 * session will be activated. A container that migrates session between VMs
 * or persists sessions is required to notify all attributes bound to sessions
 * implementing HttpSessionActivationListener.
 *
 * @since Servlet 2.3
 *
 * @version $Revision: 1.2 $ $Date: 2004/09/23 08:05:29 $
 */
public interface HttpSessionActivationListener extends EventListener {
    /**
     * Notification that the session is about to be passivated.
     */
    public void sessionWillPassivate(HttpSessionEvent se);

    /**
     * Notification that the session has just been activated.
     */
    public void sessionDidActivate(HttpSessionEvent se);
}

