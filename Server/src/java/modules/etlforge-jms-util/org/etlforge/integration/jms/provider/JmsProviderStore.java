/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms.provider;

   
                                                  
   
               
   
   
public interface JmsProviderStore {

       
                                                                           
                                     
          
                                                                                                    
                                                                                                   
                                                                                             
                 
       
                                                                                        
       
    public Iterable<JmsProvider> getStoredProviders();

       
                                                                                                  
                
       
                                                                                                  
                                         
       
    public boolean contains(String id);

       
                                                                                                  
                                     
          
                                                                                   
       
                
                                                               
                                                                                               
                                                                 
       
    public JmsProvider getProvider(String id);
}
