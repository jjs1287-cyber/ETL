/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms.provider;

   
                          
  
               
   
public class WebSphereMqProvider implements JmsProvider {

    @Override
    public String getId() {
        return "org.etlforge.integration.jms.provider.WebSphereMqProvider";
    }

    @Override
    public String getName() {
        return "WebSphere MQ";
    }

    @Override
    public String getConnectionFactoryJndiName() {
        return "";
    }

    @Override
    public String getInitialContextFactoryName() {
        return "com.sun.jndi.fscontext.RefFSContextFactory";
    }

    @Override
    public String getUrlPackagePrefixes() {
        return "";
    }

    @Override
    public boolean isUserDefined() {
        return false;
    }
}
