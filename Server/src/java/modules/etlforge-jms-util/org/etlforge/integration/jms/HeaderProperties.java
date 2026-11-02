/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms;

import java.util.Collection;

import org.etlforge.collections.KongaListUtils;

   
                                                                                
      
                                     
   
                         
   
               
   
   
public final class HeaderProperties {
    
    public static final MessageProperty DELIVERY_MODE = new MessageProperty(
                    "JMSDeliveryMode", MessagePropertyType.INT, true);
    
    public static final MessageProperty MESSAGE_ID = new MessageProperty(
                    "JMSMessageID", MessagePropertyType.STRING, false);
    
    public static final MessageProperty  TIMESTAMP = new MessageProperty(
                    "JMSTimestamp", MessagePropertyType.LONG, false);
    
    public static final MessageProperty  CORRELATION_ID = new MessageProperty(
                    "JMSCorrelationID", MessagePropertyType.STRING, true);
    
    public static final MessageProperty  REDELIVERED = new MessageProperty(
                    "JMSRedelivered", MessagePropertyType.BOOLEAN, false);
    
    public static final MessageProperty  TYPE = new MessageProperty(
                    "JMSType", MessagePropertyType.STRING, true);
    
    public static final MessageProperty  EXPIRATION = new MessageProperty(
                    "JMSExpiration", MessagePropertyType.LONG, false);
    
    public static final MessageProperty  PRIORITY = new MessageProperty(
                    "JMSPriority", MessagePropertyType.INT, true);
    
       
                                         
       
       
    public static Collection<MessageProperty> getProperties() {
        return KongaListUtils.asList(
                        DELIVERY_MODE,
                        MESSAGE_ID,
                        TIMESTAMP,
                        CORRELATION_ID,
                        REDELIVERED,
                        TYPE,
                        EXPIRATION,
                        PRIORITY);
    }
    
    private HeaderProperties() {    }
}
