

        #ifndef ADB_CLOSEBULKLOADSESSION_H
        #define ADB_CLOSEBULKLOADSESSION_H

          
                                    
         
                                                
                                                                                         
          

          
                                         
          
        typedef struct adb_closeBulkLoadSession adb_closeBulkLoadSession_t;

        
          #include "adb_WsBulkLoadSessionId.h"
          

        #include <stdio.h>
        #include <axiom.h>
        #include <axis2_util.h>
        #include <axiom_soap.h>
        #include <axis2_client.h>

        #ifdef __cplusplus
        extern "C"
        {
        #endif

        #define ADB_DEFAULT_DIGIT_LIMIT 64
        #define ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT 64
        

                                                                                                     

           
                                                              
                                                   
                                                                  
           
        adb_closeBulkLoadSession_t* AXIS2_CALL
        adb_closeBulkLoadSession_create(
            const axutil_env_t *env );

           
                                                 
                                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeBulkLoadSession_free (
            adb_closeBulkLoadSession_t* _closeBulkLoadSession,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                 
                                                                          
                                                   
                                             
           
        adb_WsBulkLoadSessionId_t* AXIS2_CALL
        adb_closeBulkLoadSession_get_sessionId(
            adb_closeBulkLoadSession_t* _closeBulkLoadSession,
            const axutil_env_t *env);

           
                                
                                                                          
                                                   
                                                          
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeBulkLoadSession_set_sessionId(
            adb_closeBulkLoadSession_t* _closeBulkLoadSession,
            const axutil_env_t *env,
            adb_WsBulkLoadSessionId_t*  arg_sessionId);

           
                                 
                                                                          
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeBulkLoadSession_reset_sessionId(
            adb_closeBulkLoadSession_t* _closeBulkLoadSession,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                          
                                                                          
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_closeBulkLoadSession_is_sessionId_nil(
                adb_closeBulkLoadSession_t* _closeBulkLoadSession,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                          
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeBulkLoadSession_deserialize(
            adb_closeBulkLoadSession_t* _closeBulkLoadSession,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                          
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_closeBulkLoadSession_declare_parent_namespaces(
                    adb_closeBulkLoadSession_t* _closeBulkLoadSession,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                          
                                                   
                                                                     
                                                                                  
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_closeBulkLoadSession_serialize(
            adb_closeBulkLoadSession_t* _closeBulkLoadSession,
            const axutil_env_t *env,
            axiom_node_t* closeBulkLoadSession_om_node, axiom_element_t *closeBulkLoadSession_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                      
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_closeBulkLoadSession_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                                 
    

