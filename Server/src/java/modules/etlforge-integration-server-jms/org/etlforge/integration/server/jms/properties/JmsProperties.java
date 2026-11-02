/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.server.jms.properties;

import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;

import org.etlforge.integration.server.data.DataElement;

import com.google.common.collect.Maps;

   
                                                      
   
                          
   
               
   
public final class JmsProperties {

    private Map<String, DataElement> properties = Maps.newHashMap();

    public JmsProperties() {
                                 
    }

       
                                                                        
       
       
    public JmsProperties(Message message) throws JMSException {
        setPropertiesFromMessage(message);
    }

    public void setPropertiesFromMessage(Message message) throws JMSException {
        Map<String, DataElement> properties = Maps.newHashMap();
        Enumeration names = message.getPropertyNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement().toString();
            if (name.length() == 0)
                continue;
            Object o = message.getObjectProperty(name);
            if (o == null)
                continue;
            DataElement value = new DataElement(o);
            properties.put(name, value);

        }
        this.properties = properties;
    }

       
                                                            
       
                              
       
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

       
                                                                                                    
                             
       
                  
                                
                                                                   
       
    public DataElement getProperty(String name) {
        if (name == null)
            return null;
        return properties.get(name);
    }
    
}
