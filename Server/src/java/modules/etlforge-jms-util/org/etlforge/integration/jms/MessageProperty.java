/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms;

import javax.jms.JMSException;
import javax.jms.Message;

   
   
             
  
   
public final class MessageProperty {

    private final String                m_name;
    private final MessagePropertyType   m_type;
    private final boolean               m_canValueBeSetByUser;

       
                                                                       
                                                                          
            
       
                  
                                           
                  
                                                
                                       
                                                                            
                                                          
       
    public MessageProperty(String name, MessagePropertyType type) {
        this(name, type, true);
    }
    
       
                                                           
       
                  
                                           
                  
                                                
                                 
                                                                    
                                                                           
                                                                      
                                                                             
                                                                             
                                              
                                       
                                                                            
                                                          
       
    public MessageProperty(String name, 
                           MessagePropertyType type,
                           boolean valueCanBeSetByUser) {
        if (name == null) {
            throw new IllegalArgumentException("A property name cannot be null");
        }
        m_name = name.trim();
        if (m_name.length() == 0) {
            throw new IllegalArgumentException("A property name cannot be blank");
        }
        if (type == null) {
            throw new IllegalArgumentException("A property type cannot be null");
        }
        m_type = type;
        m_canValueBeSetByUser = valueCanBeSetByUser;
    }
    
       
                                         
       
       
    public String getName() {
        return m_name;
    }
    
       
                                         
       
       
    public MessagePropertyType getType() {
        return m_type;
    }
    
       
                                                                            
                
       
       
    public boolean canValueBeSetByUser() {
        return m_canValueBeSetByUser;
    }
    
       
                                                                         
       
                 
                                                                         
                   
                                                                             
                           
                                                                              
                                                                               
                                
                                       
                                                                 
                                                 
       
    public void applyToMessage(Message msg, String value) throws JMSException, IllegalArgumentException {
        m_type.applyTo(msg, m_name, value);
    }
    
       
                                                                            
                                       
          
                                                                
                                                                       
                  
       
                                                                         
                                                                  
       
    public boolean isValidValue(String s) {
        return m_type.isValidValue(s);
    }

       
                                                                           
                                                
       
       
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof MessageProperty) {
            return equals((MessageProperty) obj);
        }
        return false;
    }
    
    private boolean equals(MessageProperty p) {
                                                  
        return m_name.equals(p.m_name) && (m_type == p.m_type);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + m_name.hashCode();
        hash = 31 * hash + m_type.hashCode();
        return hash;
    }
}
