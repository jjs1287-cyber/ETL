

        #ifndef ADB_WSDBLOOKUPPARAMS_H
        #define ADB_WSDBLOOKUPPARAMS_H

          
                                
         
                                                
                                                                                         
          

          
                                     
          
        typedef struct adb_WsDbLookupParams adb_WsDbLookupParams_t;

        
          #include "adb_WsConnectionParams.h"
          

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
        

                                                                                                     

           
                                                          
                                                   
                                                              
           
        adb_WsDbLookupParams_t* AXIS2_CALL
        adb_WsDbLookupParams_create(
            const axutil_env_t *env );

           
                                             
                                                                          
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_free (
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                  
                                                                  
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsDbLookupParams_get_sourceGuid(
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env);

           
                                 
                                                                  
                                                   
                                              
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_set_sourceGuid(
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_sourceGuid);

           
                                  
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_reset_sourceGuid(
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env);

        
        

           
                                  
                                                                  
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsDbLookupParams_get_targetGuid(
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env);

           
                                 
                                                                  
                                                   
                                              
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_set_targetGuid(
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_targetGuid);

           
                                  
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_reset_targetGuid(
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env);

        
        

           
                                        
                                                                  
                                                   
                                            
           
        adb_WsConnectionParams_t* AXIS2_CALL
        adb_WsDbLookupParams_get_connectionParams(
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env);

           
                                       
                                                                  
                                                   
                                                                
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_set_connectionParams(
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env,
            adb_WsConnectionParams_t*  arg_connectionParams);

           
                                        
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_reset_connectionParams(
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env);

        
        

           
                           
                                                                  
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsDbLookupParams_get_sql(
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env);

           
                          
                                                                  
                                                   
                                       
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_set_sql(
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_sql);

           
                           
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_reset_sql(
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env);

        
        

           
                                  
                                                                  
                                                   
                               
           
        axis2_bool_t AXIS2_CALL
        adb_WsDbLookupParams_get_autoCommit(
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env);

           
                                 
                                                                  
                                                   
                                             
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_set_autoCommit(
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env,
            axis2_bool_t  arg_autoCommit);

           
                                  
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_reset_autoCommit(
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                           
                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsDbLookupParams_is_sourceGuid_nil(
                adb_WsDbLookupParams_t* _WsDbLookupParams,
                const axutil_env_t *env);


        
           
                                                               
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_set_sourceGuid_nil(
                adb_WsDbLookupParams_t* _WsDbLookupParams,
                const axutil_env_t *env);
        

           
                                           
                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsDbLookupParams_is_targetGuid_nil(
                adb_WsDbLookupParams_t* _WsDbLookupParams,
                const axutil_env_t *env);


        
           
                                                               
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_set_targetGuid_nil(
                adb_WsDbLookupParams_t* _WsDbLookupParams,
                const axutil_env_t *env);
        

           
                                                 
                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsDbLookupParams_is_connectionParams_nil(
                adb_WsDbLookupParams_t* _WsDbLookupParams,
                const axutil_env_t *env);


        
           
                                                                     
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_set_connectionParams_nil(
                adb_WsDbLookupParams_t* _WsDbLookupParams,
                const axutil_env_t *env);
        

           
                                    
                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsDbLookupParams_is_sql_nil(
                adb_WsDbLookupParams_t* _WsDbLookupParams,
                const axutil_env_t *env);


        
           
                                                        
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_set_sql_nil(
                adb_WsDbLookupParams_t* _WsDbLookupParams,
                const axutil_env_t *env);
        

           
                                           
                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsDbLookupParams_is_autoCommit_nil(
                adb_WsDbLookupParams_t* _WsDbLookupParams,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                  
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_deserialize(
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                  
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsDbLookupParams_declare_parent_namespaces(
                    adb_WsDbLookupParams_t* _WsDbLookupParams,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                  
                                                   
                                                                 
                                                                              
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsDbLookupParams_serialize(
            adb_WsDbLookupParams_t* _WsDbLookupParams,
            const axutil_env_t *env,
            axiom_node_t* WsDbLookupParams_om_node, axiom_element_t *WsDbLookupParams_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                  
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsDbLookupParams_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                             
    

