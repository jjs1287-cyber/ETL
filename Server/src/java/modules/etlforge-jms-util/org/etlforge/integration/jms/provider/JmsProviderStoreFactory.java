/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
package org.etlforge.integration.jms.provider;

import java.io.File;

   
                                                           
      
                                     
   
               
   
   
public final class JmsProviderStoreFactory {

       
                                                                               
                                                                       
          
                                                                                             
                                                                                                 
                                                                                                
                                                                 
                                                                 
          
                                                                                                
                                                                     
       
                       
                                                                                        
       
    public static MutableJmsProviderStore newFileBasedStore(File directory) {
        return new FileBasedJmsProviderStore(directory);
    }
    
    private JmsProviderStoreFactory() {    }
}
