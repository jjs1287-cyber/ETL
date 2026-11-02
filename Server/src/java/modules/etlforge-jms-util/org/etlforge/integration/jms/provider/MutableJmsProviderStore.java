/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms.provider;

   
                                                                                                    
   
               
   
   
public interface MutableJmsProviderStore extends JmsProviderStore {

       
                                                                                        
                                                                                       
       
                      
                                                           
                                        
                                                                 
                                       
                                                          
       
    public void addProvider(JmsProvider provider) throws JmsProviderStoreException;

       
                                                                                 
       
                      
                                                   
                                                                            
                                      
       
    public boolean removeProvider(JmsProvider provider);

}
