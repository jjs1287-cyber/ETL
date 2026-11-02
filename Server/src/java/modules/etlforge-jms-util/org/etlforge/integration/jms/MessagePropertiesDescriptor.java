/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

   
                                                          
   
                         
   
               
   
   
public final class MessagePropertiesDescriptor implements PropertiesDescriptor {

    private final Map<String, MessageProperty> properties;

    private final Map<String, String> defaultValues;

       
                                                                 
       
       
    public MessagePropertiesDescriptor() {
        properties = Maps.newHashMap();
        defaultValues = Maps.newHashMap();
    }

       
                                                                              
       
                      
                                                                       
       
    public MessagePropertiesDescriptor(MessagePropertiesDescriptor original) {
                                                                    
        properties = Maps.newHashMap(original.properties);
        defaultValues = Maps.newHashMap(original.defaultValues);
    }

       
                                                                            
          
                                                                                          
       
                                                                                               
                                                                                                 
                                           
       
    public List<MessageProperty> getProperties() {
        return new ArrayList<MessageProperty>(properties.values());
    }

    @Override
    public String getDefaultValue(MessageProperty p) {
        return getDefaultValue(p.getName());
    }

    @Override
    public Iterable<MessageProperty> properties() {
        return new Iterable<MessageProperty>() {

            @Override
            public Iterator<MessageProperty> iterator() {
                return properties.values().iterator();
            }
        };
    }

    @Override
    public void setDefaultValue(MessageProperty p, String defaultValue) throws IllegalArgumentException {
        setDefaultValue(p.getName(), defaultValue);
    }

       
                                                                     
       
                                                                                   
                                                                                                
                                                                                
       
    public String getDefaultValue(String name) {
        return defaultValues.get(name);
    }

       
                                                                                          
                                                                  
       
                  
                                                                        
                   
                                                                                               
                                       
       
    public void setDefaultValue(String name, String value) throws IllegalArgumentException {
        MessageProperty p = properties.get(name);
        if (p != null) {
            setDefaultValueImpl(name, p.getType(), value);
        }
    }

       
                                                                                                    
            
       
       
    public void addProperty(MessageProperty p) {
        addProperty(p, null);
    }

       
                                                                                               
                                                        
       
               
                                      
                          
                                                                                               
                                       
       
    public void addProperty(MessageProperty p, String defaultValue) throws IllegalArgumentException {
        String name = p.getName();
        properties.put(name, p);
        MessagePropertyType type = p.getType();
        setDefaultValueImpl(name, type, defaultValue);
    }

       
                                                                                             
                                                             
       
       
    public void removeProperty(String name) {
        properties.remove(name);
        defaultValues.remove(name);
    }

       
                                                                     
       
       
    public int size() {
        return properties.size();
    }

       
                                                                        
       
            
                        
             
       
                                                                                                 
                          
       
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    boolean hasNonNullDefaultValues() {
        for (String s : defaultValues.values()) {
            if (s != null) {
                return true;
            }
        }
        return false;
    }

       
                                                                                                
                                                                                             
                                             
       
       
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof MessagePropertiesDescriptor) {
            return equals((MessagePropertiesDescriptor) obj);
        }
        return false;
    }

    private boolean equals(MessagePropertiesDescriptor d) {
                                                          
        return properties.equals(d.properties) && defaultValues.equals(d.defaultValues);
    }

       
                                                                                                
       
       
    @Override
    public int hashCode() {
        int hc = 17;
        hc = 31 * hc + properties.hashCode();
        hc = 31 * hc + defaultValues.hashCode();
        return hc;
    }

    private void setDefaultValueImpl(String name, MessagePropertyType type, String value)
                    throws IllegalArgumentException {
        assert properties.containsKey(name);
        if ((value == null) || (value.length() == 0)) {
            defaultValues.remove(name);
        } else {
                                                                                                 
                                                                                                 
                          
            type.getValueFromString(value);
            defaultValues.put(name, value);
        }
    }
}
