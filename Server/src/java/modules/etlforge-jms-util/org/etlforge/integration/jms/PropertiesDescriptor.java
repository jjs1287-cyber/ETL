/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms;

   
                                                                          
      
                                                                                      
   
               
   
   
public interface PropertiesDescriptor {

       
                                                                                             
                             
          
                                                                                             
                                                                                           
                                                         
                                                                  
       
       
    public Iterable<MessageProperty> properties();

       
                                                     
       
                                                                                                   
                                                                                                  
                      
       
    public String getDefaultValue(MessageProperty p);                                          
                                                                                   

       
                                                                                                
                             
       
               
                                                      
                          
                                                                                                
                          
                                       
                                                                                        
       
    public void setDefaultValue(MessageProperty p, String defaultValue) throws IllegalArgumentException; 
                                                                        
                    

}
