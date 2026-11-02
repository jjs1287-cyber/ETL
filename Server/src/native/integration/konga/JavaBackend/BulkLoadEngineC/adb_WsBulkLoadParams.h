

        #ifndef ADB_WSBULKLOADPARAMS_H
        #define ADB_WSBULKLOADPARAMS_H

          
                                
         
                                                
                                                                                         
          

          
                                     
          
        typedef struct adb_WsBulkLoadParams adb_WsBulkLoadParams_t;

        

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
        

                                                                                                     

           
                                                          
                                                   
                                                              
           
        adb_WsBulkLoadParams_t* AXIS2_CALL
        adb_WsBulkLoadParams_create(
            const axutil_env_t *env );

           
                                             
                                                                          
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadParams_free (
            adb_WsBulkLoadParams_t* _WsBulkLoadParams,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                           
                                                                  
                                                   
                          
           
        int64_t AXIS2_CALL
        adb_WsBulkLoadParams_get_operationInstanceId(
            adb_WsBulkLoadParams_t* _WsBulkLoadParams,
            const axutil_env_t *env);

           
                                          
                                                                  
                                                   
                                                 
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadParams_set_operationInstanceId(
            adb_WsBulkLoadParams_t* _WsBulkLoadParams,
            const axutil_env_t *env,
            const int64_t  arg_operationInstanceId);

           
                                           
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadParams_reset_operationInstanceId(
            adb_WsBulkLoadParams_t* _WsBulkLoadParams,
            const axutil_env_t *env);

        
        

           
                                               
                                                                  
                                                   
                          
           
        int64_t AXIS2_CALL
        adb_WsBulkLoadParams_get_lastOperationInstanceId(
            adb_WsBulkLoadParams_t* _WsBulkLoadParams,
            const axutil_env_t *env);

           
                                              
                                                                  
                                                   
                                                     
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadParams_set_lastOperationInstanceId(
            adb_WsBulkLoadParams_t* _WsBulkLoadParams,
            const axutil_env_t *env,
            const int64_t  arg_lastOperationInstanceId);

           
                                               
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadParams_reset_lastOperationInstanceId(
            adb_WsBulkLoadParams_t* _WsBulkLoadParams,
            const axutil_env_t *env);

        
        

           
                                  
                                                                  
                                                   
                          
           
        int64_t AXIS2_CALL
        adb_WsBulkLoadParams_get_bulkLoadId(
            adb_WsBulkLoadParams_t* _WsBulkLoadParams,
            const axutil_env_t *env);

           
                                 
                                                                  
                                                   
                                        
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadParams_set_bulkLoadId(
            adb_WsBulkLoadParams_t* _WsBulkLoadParams,
            const axutil_env_t *env,
            const int64_t  arg_bulkLoadId);

           
                                  
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadParams_reset_bulkLoadId(
            adb_WsBulkLoadParams_t* _WsBulkLoadParams,
            const axutil_env_t *env);

        
        

           
                                      
                                                                  
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsBulkLoadParams_get_sourceFilePath(
            adb_WsBulkLoadParams_t* _WsBulkLoadParams,
            const axutil_env_t *env);

           
                                     
                                                                  
                                                   
                                                  
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadParams_set_sourceFilePath(
            adb_WsBulkLoadParams_t* _WsBulkLoadParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_sourceFilePath);

           
                                      
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadParams_reset_sourceFilePath(
            adb_WsBulkLoadParams_t* _WsBulkLoadParams,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                                    
                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadParams_is_operationInstanceId_nil(
                adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                const axutil_env_t *env);


        

           
                                                        
                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadParams_is_lastOperationInstanceId_nil(
                adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                const axutil_env_t *env);


        

           
                                           
                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadParams_is_bulkLoadId_nil(
                adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                const axutil_env_t *env);


        

           
                                               
                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadParams_is_sourceFilePath_nil(
                adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                const axutil_env_t *env);


        
           
                                                                   
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadParams_set_sourceFilePath_nil(
                adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                const axutil_env_t *env);
        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                  
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadParams_deserialize(
            adb_WsBulkLoadParams_t* _WsBulkLoadParams,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                  
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsBulkLoadParams_declare_parent_namespaces(
                    adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                  
                                                   
                                                                 
                                                                              
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsBulkLoadParams_serialize(
            adb_WsBulkLoadParams_t* _WsBulkLoadParams,
            const axutil_env_t *env,
            axiom_node_t* WsBulkLoadParams_om_node, axiom_element_t *WsBulkLoadParams_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                  
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadParams_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                             
    

