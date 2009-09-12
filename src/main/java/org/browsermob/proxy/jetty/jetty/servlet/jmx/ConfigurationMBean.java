//========================================================================
//$Id: ConfigurationMBean.java,v 1.2 2005/08/13 00:01:27 gregwilkins Exp $
//Copyright 2000-2004 Mort Bay Consulting Pty. Ltd.
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at 
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package org.browsermob.proxy.jetty.jetty.servlet.jmx;

import org.apache.commons.logging.Log;
import org.browsermob.proxy.jetty.jetty.servlet.WebApplicationContext.Configuration;
import org.browsermob.proxy.jetty.log.LogFactory;
import org.browsermob.proxy.jetty.util.LogSupport;
import org.browsermob.proxy.jetty.util.jmx.ModelMBeanImpl;

import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.ObjectName;


/**
 * 
 * ConfigurationMBean
 * 
 * MBean proxy for a WebApplicationContext.Configuration object.
 *
 * @author janb
 * @version $Revision: 1.2 $ $Date: 2005/08/13 00:01:27 $
 *
 */
public class ConfigurationMBean extends ModelMBeanImpl
{
    private static final Log log = LogFactory.getLog(ConfigurationMBean.class);
    protected Configuration _config = null;
    
    public ConfigurationMBean()
    throws MBeanException
    {}

    /**defineManagedResource
     * Grab the object which this mbean is proxying for, which in
     * this case is an org.mortbay.jetty.servlet.WebApplicationContext.Configuration
     * @see org.browsermob.proxy.jetty.util.jmx.ModelMBeanImpl#defineManagedResource()
     */
    protected void defineManagedResource()
    {
        super.defineManagedResource();
       defineAttribute("name", READ_ONLY, ON_MBEAN);
        _config=(Configuration)getManagedResource();
    }
    
    /**getName
     * This method is only defined to satisfy JMX: it is non-compliant
     * to have an mbean with no methods on it, so this method has been
     * added as a workaround. 
     * @return classname of the Configuration instance
     */
    public String getName ()
    {
        if (null==_config)
            return null;
        
        return _config.getClass().getName();
    }
    
    /**uniqueObjectName
     * Make a unique jmx name for this configuration object
     * @see org.browsermob.proxy.jetty.util.jmx.ModelMBeanImpl#uniqueObjectName(javax.management.MBeanServer, java.lang.String)
     */
    public synchronized ObjectName uniqueObjectName(MBeanServer server, String on)
    {
        ObjectName oName=null;
        try{oName=new ObjectName(on+",config="+_config.getClass().getName());}
        catch(Exception e){log.warn(LogSupport.EXCEPTION,e);}
        
        return oName;
    }
    
}
