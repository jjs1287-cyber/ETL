

        #ifndef ADB_WSBULKLOADRESULT_H
        #define ADB_WSBULKLOADRESULT_H

          
                                
         
                                                
                                                                                         
          

          
                                     
          
        typedef struct adb_WsBulkLoadResult adb_WsBulkLoadResult_t;

        
          #include "adb_WsBulkLoadError.h"
          

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
        

                                                                                                     

           
                                                          
                                                   
                                                              
           
        adb_WsBulkLoadResult_t* AXIS2_CALL
        adb_WsBulkLoadResult_create(
            const axutil_env_t *env );

           
                                             
                                                                          
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadResult_free (
            adb_WsBulkLoadResult_t* _WsBulkLoadResult,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                          
                                                                  
                                                   
                               
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadResult_get_allBatchesWereSent(
            adb_WsBulkLoadResult_t* _WsBulkLoadResult,
            const axutil_env_t *env);

           
                                         
                                                                  
                                                   
                                                     
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadResult_set_allBatchesWereSent(
            adb_WsBulkLoadResult_t* _WsBulkLoadResult,
            const axutil_env_t *env,
            axis2_bool_t  arg_allBatchesWereSent);

           
                                          
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadResult_reset_allBatchesWereSent(
            adb_WsBulkLoadResult_t* _WsBulkLoadResult,
            const axutil_env_t *env);

        
        

           
                             
                                                                  
                                                   
                                         
           
        adb_WsBulkLoadError_t* AXIS2_CALL
        adb_WsBulkLoadResult_get_error(
            adb_WsBulkLoadResult_t* _WsBulkLoadResult,
            const axutil_env_t *env);

           
                            
                                                                  
                                                   
                                                  
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadResult_set_error(
            adb_WsBulkLoadResult_t* _WsBulkLoadResult,
            const axutil_env_t *env,
            adb_WsBulkLoadError_t*  arg_error);

           
                             
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadResult_reset_error(
            adb_WsBulkLoadResult_t* _WsBulkLoadResult,
            const axutil_env_t *env);

        
        

           
                               
                                                                  
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsBulkLoadResult_get_logFile(
            adb_WsBulkLoadResult_t* _WsBulkLoadResult,
            const axutil_env_t *env);

           
                              
                                                                  
                                                   
                                           
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadResult_set_logFile(
            adb_WsBulkLoadResult_t* _WsBulkLoadResult,
            const axutil_env_t *env,
            const axis2_char_t*  arg_logFile);

           
                               
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadResult_reset_logFile(
            adb_WsBulkLoadResult_t* _WsBulkLoadResult,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                                   
                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadResult_is_allBatchesWereSent_nil(
                adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                const axutil_env_t *env);


        

           
                                      
                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadResult_is_error_nil(
                adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                const axutil_env_t *env);


        
           
                                                          
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadResult_set_error_nil(
                adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                const axutil_env_t *env);
        

           
                                        
                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadResult_is_logFile_nil(
                adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                const axutil_env_t *env);


        
           
                                                            
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadResult_set_logFile_nil(
                adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                const axutil_env_t *env);
        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                  
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadResult_deserialize(
            adb_WsBulkLoadResult_t* _WsBulkLoadResult,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                  
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsBulkLoadResult_declare_parent_namespaces(
                    adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                  
                                                   
                                                                 
                                                                              
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsBulkLoadResult_serialize(
            adb_WsBulkLoadResult_t* _WsBulkLoadResult,
            const axutil_env_t *env,
            axiom_node_t* WsBulkLoadResult_om_node, axiom_element_t *WsBulkLoadResult_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                  
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadResult_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                             
    

