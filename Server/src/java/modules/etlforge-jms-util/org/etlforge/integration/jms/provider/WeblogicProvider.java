/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms.provider;

   
   
               
   
public class WeblogicProvider implements JmsProvider {

    @Override
    public String getId() {
        return "org.etlforge.integration.jms.provider.WeblogicProvider";
    }

    @Override
    public String getName() {
        return "Weblogic";
    }

    @Override
    public String getInitialContextFactoryName() {
        return "weblogic.jndi.WLInitialContextFactory";
    }

    @Override
    public String getUrlPackagePrefixes() {
        return null;
    }

    @Override
    public String getConnectionFactoryJndiName() {
        return "jms/connectionFactory";
    }

    @Override
    public boolean isUserDefined() {
        return false;
    }
}
