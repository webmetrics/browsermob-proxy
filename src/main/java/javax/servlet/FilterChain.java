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

import java.io.IOException;

/**
 * A FilterChain is an object provided by the servlet container to the developer
 * giving a view into the invocation chain of a filtered request for a resource. Filters
 * use the FilterChain to invoke the next filter in the chain, or if the calling filter
 * is the last filter in the chain, to invoke the rosource at the end of the chain.
 *
 * @see Filter
 * @since Servlet 2.3
 *
 * @version $Revision: 1.3 $ $Date: 2004/09/23 08:05:18 $
 */
public interface FilterChain {
    /**
     * Causes the next filter in the chain to be invoked, or if the calling filter is the last filter
     * in the chain, causes the resource at the end of the chain to be invoked.
     *
     * @param request the request to pass along the chain.
     * @param response the response to pass along the chain.
     *
     * @since Servlet 2.3
     */
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException;
}

