/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms.provider;

   
                                                            
      
                                                                              
                                      
   
               
   
class UserDefinedProvider implements JmsProvider {

    private final String id;

    private final String name;

    private final String initialContextFactoryName;

    private final String connectionFactoryJndiName;

    private final String urlPackagePrefixes;

    UserDefinedProvider(String name, String initialContextFactoryName, String connectionFactoryJndiName,
                    String urlPackagePrefixes) {
        this(null, name, initialContextFactoryName, connectionFactoryJndiName, urlPackagePrefixes);
    }

    UserDefinedProvider(String id, String name, String initialContextFactoryName, String connectionFactoryJndiName,
                    String urlPackagePrefixes) {
        this.name = validateNonEmptyString("name", name);
        this.id = (id != null) ? id : "org.etlforge.integration.jms.provider." + this.name;
        this.initialContextFactoryName = getNonNullString(initialContextFactoryName);
        this.connectionFactoryJndiName = getNonNullString(connectionFactoryJndiName);
        this.urlPackagePrefixes = getNonNullString(urlPackagePrefixes);
    }

    @Override
    public String getConnectionFactoryJndiName() {
        return connectionFactoryJndiName;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getInitialContextFactoryName() {
        return initialContextFactoryName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrlPackagePrefixes() {
        return urlPackagePrefixes;
    }

    @Override
    public boolean isUserDefined() {
        return true;
    }

    private static String getNonNullString(String value) {
        return (value != null) ? value : "";
    }

    private static String validateNonEmptyString(String name, String value) {
        if (value == null) {
            throw new IllegalArgumentException(name + " cannot be null");
        }
        String trimmed = value.trim();
        if (trimmed.length() == 0) {
            throw new IllegalArgumentException(name + " cannot be empty or blank");
        }
        return trimmed;
    }
}
