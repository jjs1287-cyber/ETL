/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.store;

import java.util.Map;

import org.etlforge.integration.server.jms.JmsServerException;
import org.etlforge.integration.server.jms.messaging.LocationProperties;

   
   
                          
                         
   
             
   
public interface JmsLocationInfoStore {

       
                                                                                                    
              
       
                                                                                               
                                               
                                 
                                                                               
       
    public Map<Long, LocationProperties> getJmsLocationInfo() throws JmsServerException;

}
