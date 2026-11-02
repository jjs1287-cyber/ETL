/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms;

   
                                                                     
   
             
   
                 
  
   
public enum DeliveryMode {

    NON_PERSISTENT(javax.jms.DeliveryMode.NON_PERSISTENT),
    PERSISTENT(javax.jms.DeliveryMode.PERSISTENT);
    
    private final int   jmsSpecValue;
    
    private DeliveryMode(int jmsSpecValue) {
        this.jmsSpecValue = jmsSpecValue;
    }
    
       
                                                                                             
                                                                                                   
                                                                      
       
       
    public int getJmsSpecValue() {
        return jmsSpecValue;
    }

       
                                                                                          
                                                               
       
                                       
                                                                                           
       
    public static DeliveryMode fromJmsSpecValue(int i) {
        if (i == NON_PERSISTENT.jmsSpecValue) {
            return NON_PERSISTENT;
        }
        if (i == PERSISTENT.jmsSpecValue) {
            return PERSISTENT;
        }
        throw new IllegalArgumentException("Illegal value: " + i);
    }
}
