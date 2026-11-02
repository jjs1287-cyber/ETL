

        #ifndef ADB_WSDBLOOKUPRESULT_H
        #define ADB_WSDBLOOKUPRESULT_H

          
                                
         
                                                
                                                                                         
          

          
                                     
          
        typedef struct adb_WsDbLookupResult adb_WsDbLookupResult_t;

        
          #include "adb_WsJdbcError.h"
          

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
        

                                                                                                     

           
                                                          
                                                   
                                                              
           
        adb_WsDbLookupResult_t* AXIS2_CALL
        adb_WsDbLookupResult_create(
            const axutil_env_t *env );

           
                                             
                                                                          
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupResult_free (
            adb_WsDbLookupResult_t* _WsDbLookupResult,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                   
                                                                  
                                                   
                                     
           
        adb_WsJdbcError_t* AXIS2_CALL
        adb_WsDbLookupResult_get_returnValue(
            adb_WsDbLookupResult_t* _WsDbLookupResult,
            const axutil_env_t *env);

           
                                  
                                                                  
                                                   
                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupResult_set_returnValue(
            adb_WsDbLookupResult_t* _WsDbLookupResult,
            const axutil_env_t *env,
            adb_WsJdbcError_t*  arg_returnValue);

           
                                   
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupResult_reset_returnValue(
            adb_WsDbLookupResult_t* _WsDbLookupResult,
            const axutil_env_t *env);

        
        

           
                             
                                                                  
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsDbLookupResult_get_value(
            adb_WsDbLookupResult_t* _WsDbLookupResult,
            const axutil_env_t *env);

           
                            
                                                                  
                                                   
                                         
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupResult_set_value(
            adb_WsDbLookupResult_t* _WsDbLookupResult,
            const axutil_env_t *env,
            const axis2_char_t*  arg_value);

           
                             
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupResult_reset_value(
            adb_WsDbLookupResult_t* _WsDbLookupResult,
            const axutil_env_t *env);

        
        

           
                                   
                                                                  
                                                   
                      
           
        int AXIS2_CALL
        adb_WsDbLookupResult_get_updateCount(
            adb_WsDbLookupResult_t* _WsDbLookupResult,
            const axutil_env_t *env);

           
                                  
                                                                  
                                                   
                                     
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupResult_set_updateCount(
            adb_WsDbLookupResult_t* _WsDbLookupResult,
            const axutil_env_t *env,
            const int  arg_updateCount);

           
                                   
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupResult_reset_updateCount(
            adb_WsDbLookupResult_t* _WsDbLookupResult,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                            
                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsDbLookupResult_is_returnValue_nil(
                adb_WsDbLookupResult_t* _WsDbLookupResult,
                const axutil_env_t *env);


        
           
                                                                
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupResult_set_returnValue_nil(
                adb_WsDbLookupResult_t* _WsDbLookupResult,
                const axutil_env_t *env);
        

           
                                      
                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsDbLookupResult_is_value_nil(
                adb_WsDbLookupResult_t* _WsDbLookupResult,
                const axutil_env_t *env);


        
           
                                                          
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupResult_set_value_nil(
                adb_WsDbLookupResult_t* _WsDbLookupResult,
                const axutil_env_t *env);
        

           
                                            
                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsDbLookupResult_is_updateCount_nil(
                adb_WsDbLookupResult_t* _WsDbLookupResult,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                  
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbLookupResult_deserialize(
            adb_WsDbLookupResult_t* _WsDbLookupResult,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                  
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsDbLookupResult_declare_parent_namespaces(
                    adb_WsDbLookupResult_t* _WsDbLookupResult,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                  
                                                   
                                                                 
                                                                              
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsDbLookupResult_serialize(
            adb_WsDbLookupResult_t* _WsDbLookupResult,
            const axutil_env_t *env,
            axiom_node_t* WsDbLookupResult_om_node, axiom_element_t *WsDbLookupResult_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                  
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsDbLookupResult_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                             
    

