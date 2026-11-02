

        #ifndef ADB_RUNBULKLOAD_H
        #define ADB_RUNBULKLOAD_H

          
                           
         
                                                
                                                                                         
          

          
                                
          
        typedef struct adb_runBulkLoad adb_runBulkLoad_t;

        
          #include "adb_WsBulkLoadSessionId.h"
          
          #include "adb_WsBulkLoadParams.h"
          
          #include "adb_WsSalesforceEndpoint.h"
          
          #include "adb_WsKeyValue.h"
          

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
        

                                                                                                     

           
                                                     
                                                   
                                                         
           
        adb_runBulkLoad_t* AXIS2_CALL
        adb_runBulkLoad_create(
            const axutil_env_t *env );

           
                                        
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoad_free (
            adb_runBulkLoad_t* _runBulkLoad,
            const axutil_env_t *env);



                                                                                                       
                                                                                                       
        

           
                                 
                                                        
                                                   
                                             
           
        adb_WsBulkLoadSessionId_t* AXIS2_CALL
        adb_runBulkLoad_get_sessionId(
            adb_runBulkLoad_t* _runBulkLoad,
            const axutil_env_t *env);

           
                                
                                                        
                                                   
                                                          
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoad_set_sessionId(
            adb_runBulkLoad_t* _runBulkLoad,
            const axutil_env_t *env,
            adb_WsBulkLoadSessionId_t*  arg_sessionId);

           
                                 
                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoad_reset_sessionId(
            adb_runBulkLoad_t* _runBulkLoad,
            const axutil_env_t *env);

        
        

           
                                      
                                                        
                                                   
                                          
           
        adb_WsBulkLoadParams_t* AXIS2_CALL
        adb_runBulkLoad_get_bulkloadParams(
            adb_runBulkLoad_t* _runBulkLoad,
            const axutil_env_t *env);

           
                                     
                                                        
                                                   
                                                            
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoad_set_bulkloadParams(
            adb_runBulkLoad_t* _runBulkLoad,
            const axutil_env_t *env,
            adb_WsBulkLoadParams_t*  arg_bulkloadParams);

           
                                      
                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoad_reset_bulkloadParams(
            adb_runBulkLoad_t* _runBulkLoad,
            const axutil_env_t *env);

        
        

           
                                          
                                                        
                                                   
                                              
           
        adb_WsSalesforceEndpoint_t* AXIS2_CALL
        adb_runBulkLoad_get_salesforceEndpoint(
            adb_runBulkLoad_t* _runBulkLoad,
            const axutil_env_t *env);

           
                                         
                                                        
                                                   
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoad_set_salesforceEndpoint(
            adb_runBulkLoad_t* _runBulkLoad,
            const axutil_env_t *env,
            adb_WsSalesforceEndpoint_t*  arg_salesforceEndpoint);

           
                                          
                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoad_reset_salesforceEndpoint(
            adb_runBulkLoad_t* _runBulkLoad,
            const axutil_env_t *env);

        
        

           
                                                                                             
                                                        
                                                   
                                               
           
        axutil_array_list_t* AXIS2_CALL
        adb_runBulkLoad_get_in3(
            adb_runBulkLoad_t* _runBulkLoad,
            const axutil_env_t *env);

           
                                                                                    
                                              
                                                        
                                                   
                                                      
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoad_set_in3(
            adb_runBulkLoad_t* _runBulkLoad,
            const axutil_env_t *env,
            axutil_array_list_t*  arg_in3);

           
                           
                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoad_reset_in3(
            adb_runBulkLoad_t* _runBulkLoad,
            const axutil_env_t *env);

        
                                                                                                          
                                                                                                          

           
                                                     
          
                                                                              
            
                                                               
                                                                                 
          
                                                                 
                                                                                
          
                                                                                           
                                                                          
          
                                                      
                                                                  
               
            
          
           

        
        
           
                                      
                                                        
                                                   
                                               
                                                     
           
        adb_WsKeyValue_t* AXIS2_CALL
        adb_runBulkLoad_get_in3_at(
                adb_runBulkLoad_t* _runBulkLoad,
                const axutil_env_t *env, int i);

           
                                                                                      
                                                        
                                                   
                                               
                                                                       
                                                     
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoad_set_in3_at(
                adb_runBulkLoad_t* _runBulkLoad,
                const axutil_env_t *env, int i,
                adb_WsKeyValue_t* arg_in3);


           
                      
                                                        
                                                   
                                                                       
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoad_add_in3(
                adb_runBulkLoad_t* _runBulkLoad,
                const axutil_env_t *env,
                adb_WsKeyValue_t* arg_in3);

           
                                         
                                                        
                                                    
                                             
           
        int AXIS2_CALL
        adb_runBulkLoad_sizeof_in3(
                    adb_runBulkLoad_t* _runBulkLoad,
                    const axutil_env_t *env);

           
                                         
                                                        
                                                   
                                               
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoad_remove_in3_at(
                adb_runBulkLoad_t* _runBulkLoad,
                const axutil_env_t *env, int i);

        


                                                                                                           
                                                                                                           

           
                                                                  
           

        

           
                                          
                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_runBulkLoad_is_sessionId_nil(
                adb_runBulkLoad_t* _runBulkLoad,
                const axutil_env_t *env);


        

           
                                               
                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_runBulkLoad_is_bulkloadParams_nil(
                adb_runBulkLoad_t* _runBulkLoad,
                const axutil_env_t *env);


        

           
                                                   
                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_runBulkLoad_is_salesforceEndpoint_nil(
                adb_runBulkLoad_t* _runBulkLoad,
                const axutil_env_t *env);


        

           
                                    
                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_runBulkLoad_is_in3_nil(
                adb_runBulkLoad_t* _runBulkLoad,
                const axutil_env_t *env);


        
                                                                                                               

           
                                                                          
                                                                                                                                      
           
        
           
                                         
                                                        
                                                    
                                                
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_runBulkLoad_is_in3_nil_at(
                adb_runBulkLoad_t* _runBulkLoad,
                const axutil_env_t *env, int i);
 
       
           
                               
                                                          
                                                    
                                             
                                                                        
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoad_set_in3_nil_at(
                adb_runBulkLoad_t* _runBulkLoad, 
                const axutil_env_t *env, int i);

        

                                                                                                      
                                                                                                      

        
           
                                            
                                                        
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoad_deserialize(
            adb_runBulkLoad_t* _runBulkLoad,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                        
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_runBulkLoad_declare_parent_namespaces(
                    adb_runBulkLoad_t* _runBulkLoad,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                        
                                                   
                                                            
                                                                         
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_runBulkLoad_serialize(
            adb_runBulkLoad_t* _runBulkLoad,
            const axutil_env_t *env,
            axiom_node_t* runBulkLoad_om_node, axiom_element_t *runBulkLoad_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                             
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_runBulkLoad_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                        
    

