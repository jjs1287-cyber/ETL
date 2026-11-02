/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms;

import java.util.Collection;

   
                                                                                 
      
                                                                                                
                                                                                               
   
                         
   
               
   
   
public class FixedPropertySetDescriptor implements PropertiesDescriptor {

    private final MessagePropertiesDescriptor properties;

       
                                                                                                    
                  
       
                                   
                                                            
       
    public FixedPropertySetDescriptor(Collection<MessageProperty> properties) {
        this.properties = new MessagePropertiesDescriptor();
        for (MessageProperty p : properties) {
            this.properties.addProperty(p);
        }
    }

       
                                                                           
       
                      
                                                                
       
    public FixedPropertySetDescriptor(FixedPropertySetDescriptor original) {
        properties = new MessagePropertiesDescriptor(original.properties);
    }

       
                                                                                         
                  
       
    @Override
    public Iterable<MessageProperty> properties() {
        return properties.properties();
    }

       
                                                     
          
                                                                                                 
       
               
                                                          
                   
                                                                                                
                          
                                       
                                                                                  
       
    @Override
    public void setDefaultValue(MessageProperty p, String value) throws IllegalArgumentException {
        setDefaultValue(p.getName(), value);
    }

       
                                                     
          
                                                                                                 
       
                  
                                                                      
                   
                                                                                                
                          
                                       
                                                                                  
       
    public void setDefaultValue(String name, String value) throws IllegalArgumentException {
        properties.setDefaultValue(name, value);
    }

       
                                                       
       
                                                                                   
                                                                                                    
                                                                     
       
    @Override
    public String getDefaultValue(MessageProperty p) {
        return getDefaultValue(p.getName());
    }

       
                                                                     
       
                                                                                   
                                                                                              
                                                                                     
       
    public String getDefaultValue(String name) {
        return properties.getDefaultValue(name);
    }

       
                                                                                                    
                             
       
       
    public boolean hasNonNullDefaultValues() {
        return properties.hasNonNullDefaultValues();
    }

       
                                                                                                   
                                                                                                    
                                 
       
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof FixedPropertySetDescriptor) {
            return properties.equals(((FixedPropertySetDescriptor) obj).properties);
        }
        return false;
    }

       
                                                                                                
       
    @Override
    public int hashCode() {
        return properties.hashCode();
    }
    
}
