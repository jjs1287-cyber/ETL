/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms.provider;

   
                                                                                
   
               
   
public class JbossJmsMessagingProvider implements JmsProvider {

    @Override
    public String getId() {
        return "org.etlforge.integration.jms.JbossJmsMessagingProvider";
    }

    @Override
    public String getName() {
        return "JBoss Messaging";
    }

    @Override
    public String getConnectionFactoryJndiName() {
        return "/ConnectionFactory";
    }

    @Override
    public String getInitialContextFactoryName() {
        return "org.jnp.interfaces.NamingContextFactory";
    }

    @Override
    public String getUrlPackagePrefixes() {
        return "org.jboss.naming:org.jnp.interfaces";
    }

    @Override
    public boolean isUserDefined() {
        return false;
    }
}
