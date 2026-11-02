/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms.provider;

   
                                                                      
  
                         
  
               
   
public final class UserDefinedProviderFactory {

    public static JmsProvider createEmptyProviderTemplate() {
        return new UserDefinedProvider("org.etlforge.integration.jms.ETLForgeCustomProviderTemplate", 
                        "Custom", "", "", "");
    }

    public static JmsProvider createUserDefinedProvider(String name,
                                                         String initialContextFactoryName,
                                                         String connectionFactoryJndiName,
                                                         String urlPackagePrefixes) {
        return new UserDefinedProvider(name, initialContextFactoryName,
                        connectionFactoryJndiName, urlPackagePrefixes);
    }

    private UserDefinedProviderFactory() {    }
}
