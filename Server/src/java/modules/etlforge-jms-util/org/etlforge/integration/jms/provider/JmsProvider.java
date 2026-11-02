/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms.provider;

import java.io.Serializable;

   
   
               
  
   
public interface JmsProvider extends Serializable {

    public String getId();
    
    public String getName();

    public String getConnectionFactoryJndiName();
    
    public String getInitialContextFactoryName();
    
    public String getUrlPackagePrefixes();
    
    public boolean isUserDefined();
    
}
