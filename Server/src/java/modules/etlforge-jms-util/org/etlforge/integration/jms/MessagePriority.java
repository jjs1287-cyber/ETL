/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms;

   
                                                                           
   
                         
   
             
   
   
public enum MessagePriority {

                                              
    
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9);
    
    private final int jmsSpecValue;
    
    private MessagePriority(int jmsSpecValue) {
        this.jmsSpecValue = jmsSpecValue;
    }

       
                                                                       
       
       
    public static MessagePriority defaultValue() {
        return FOUR;
    }
    
       
                                            
       
       
    public static MessagePriority lowestPriority() {
        return ZERO;
    }
    
       
                                             
       
       
    public static MessagePriority highestPriority() {
        return NINE;
    }
    
       
                                                                                                 
                                                                                                   
                       
       
       
    public int getJmsSpecValue() {
        return jmsSpecValue;
    }
    
       
                                                                                          
                                          
       
       
    public boolean isNormal() {
        return this.compareTo(FOUR) <= 0;
    }
    
       
                                                                                              
                                          
       
       
    public boolean isExpedited() {
        return this.compareTo(FIVE) >= 0;
    }
    
       
                                                                           
                                                                              
       
                                       
                                                                           
                                 
       
    public static MessagePriority fromJmsSpecValue(int i) {
        MessagePriority[] values = values();
        if ((i >= 0) && (i < values.length)) {
            return values[i];
        }
        throw new IllegalArgumentException("Invalid priority value: " + i);
    }
}
