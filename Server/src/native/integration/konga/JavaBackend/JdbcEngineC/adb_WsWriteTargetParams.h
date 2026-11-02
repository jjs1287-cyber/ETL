

        #ifndef ADB_WSWRITETARGETPARAMS_H
        #define ADB_WSWRITETARGETPARAMS_H

          
                                   
         
                                                
                                                                                         
          

          
                                        
          
        typedef struct adb_WsWriteTargetParams adb_WsWriteTargetParams_t;

        
          #include "adb_WsConnectionParams.h"
          
          #include "adb_ArrayOfWsKeyValue.h"
          

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
        

                                                                                                     

           
                                                             
                                                   
                                                                 
           
        adb_WsWriteTargetParams_t* AXIS2_CALL
        adb_WsWriteTargetParams_create(
            const axutil_env_t *env );

           
                                                
                                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_free (
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                  
                                                                        
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsWriteTargetParams_get_targetGuid(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env);

           
                                 
                                                                        
                                                   
                                              
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_set_targetGuid(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_targetGuid);

           
                                  
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_reset_targetGuid(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env);

        
        

           
                                        
                                                                        
                                                   
                                            
           
        adb_WsConnectionParams_t* AXIS2_CALL
        adb_WsWriteTargetParams_get_connectionParams(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env);

           
                                       
                                                                        
                                                   
                                                                
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_set_connectionParams(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env,
            adb_WsConnectionParams_t*  arg_connectionParams);

           
                                        
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_reset_connectionParams(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env);

        
        

           
                                   
                                                                        
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsWriteTargetParams_get_jtrFileName(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env);

           
                                  
                                                                        
                                                   
                                               
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_set_jtrFileName(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_jtrFileName);

           
                                   
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_reset_jtrFileName(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env);

        
        

           
                                    
                                                                        
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsWriteTargetParams_get_dataFileName(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env);

           
                                   
                                                                        
                                                   
                                                
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_set_dataFileName(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_dataFileName);

           
                                    
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_reset_dataFileName(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env);

        
        

           
                                   
                                                                        
                                                   
                               
           
        axis2_bool_t AXIS2_CALL
        adb_WsWriteTargetParams_get_transaction(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env);

           
                                  
                                                                        
                                                   
                                              
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_set_transaction(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env,
            axis2_bool_t  arg_transaction);

           
                                   
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_reset_transaction(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env);

        
        

           
                                        
                                                                        
                                                   
                      
           
        int AXIS2_CALL
        adb_WsWriteTargetParams_get_insertUpdateMode(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env);

           
                                       
                                                                        
                                                   
                                          
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_set_insertUpdateMode(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env,
            const int  arg_insertUpdateMode);

           
                                        
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_reset_insertUpdateMode(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env);

        
        

           
                                 
                                                                        
                                                   
                                           
           
        adb_ArrayOfWsKeyValue_t* AXIS2_CALL
        adb_WsWriteTargetParams_get_keyValues(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env);

           
                                
                                                                        
                                                   
                                                        
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_set_keyValues(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env,
            adb_ArrayOfWsKeyValue_t*  arg_keyValues);

           
                                 
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_reset_keyValues(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                           
                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsWriteTargetParams_is_targetGuid_nil(
                adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                const axutil_env_t *env);


        
           
                                                               
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_set_targetGuid_nil(
                adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                const axutil_env_t *env);
        

           
                                                 
                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsWriteTargetParams_is_connectionParams_nil(
                adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                const axutil_env_t *env);


        
           
                                                                     
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_set_connectionParams_nil(
                adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                const axutil_env_t *env);
        

           
                                            
                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsWriteTargetParams_is_jtrFileName_nil(
                adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                const axutil_env_t *env);


        
           
                                                                
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_set_jtrFileName_nil(
                adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                const axutil_env_t *env);
        

           
                                             
                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsWriteTargetParams_is_dataFileName_nil(
                adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                const axutil_env_t *env);


        
           
                                                                 
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_set_dataFileName_nil(
                adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                const axutil_env_t *env);
        

           
                                            
                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsWriteTargetParams_is_transaction_nil(
                adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                const axutil_env_t *env);


        

           
                                                 
                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsWriteTargetParams_is_insertUpdateMode_nil(
                adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                const axutil_env_t *env);


        

           
                                          
                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsWriteTargetParams_is_keyValues_nil(
                adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                const axutil_env_t *env);


        
           
                                                              
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_set_keyValues_nil(
                adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                const axutil_env_t *env);
        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                        
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_deserialize(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                        
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsWriteTargetParams_declare_parent_namespaces(
                    adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                        
                                                   
                                                                    
                                                                                 
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsWriteTargetParams_serialize(
            adb_WsWriteTargetParams_t* _WsWriteTargetParams,
            const axutil_env_t *env,
            axiom_node_t* WsWriteTargetParams_om_node, axiom_element_t *WsWriteTargetParams_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                     
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsWriteTargetParams_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                                
    

