/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package org.browsermob.proxy.util;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


/**
 * Convenience object for generating GUIDs. Uses the SHA1PRNG algorithm if available. If unavailable, it leaves this up
 * to the JRE to provide a default. See http://java.sun.com/j2se/1.4.2/docs/api/java/security/SecureRandom.html for more
 * details.
 *
 * @author <a href="mailto:salaman@teknos.com">Victor Salaman</a>
 * @version $Revision: 156 $
 */
public final class GUID {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static SecureRandom rnd;

    static {
        try {
            rnd = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            rnd = new SecureRandom(); //Use default if prefered provider is unavailable
        }

        byte[] seed = rnd.generateSeed(64);
        rnd.setSeed(seed);
    }

    //~ Methods ////////////////////////////////////////////////////////////////

    public static String generateFormattedGUID() {
        //xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
        String guid = generateGUID();

        return guid.substring(0, 8) + '-' + guid.substring(8, 12) + '-' + guid.substring(12, 16) + '-' + guid.substring(16, 20) + '-' + guid.substring(20);
    }

    public static String generateGUID() {
        return new BigInteger(165, rnd).toString(36).toUpperCase();
    }
}
