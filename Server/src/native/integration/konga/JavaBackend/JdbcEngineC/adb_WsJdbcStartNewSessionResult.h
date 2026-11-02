

        #ifndef ADB_WSJDBCSTARTNEWSESSIONRESULT_H
        #define ADB_WSJDBCSTARTNEWSESSIONRESULT_H

          
                                           
         
                                                
                                                                                         
          

          
                                                
          
        typedef struct adb_WsJdbcStartNewSessionResult adb_WsJdbcStartNewSessionResult_t;

        
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
        

                                                                                                     

           
                                                                     
                                                   
                                                                         
           
        adb_WsJdbcStartNewSessionResult_t* AXIS2_CALL
        adb_WsJdbcStartNewSessionResult_create(
            const axutil_env_t *env );

           
                                                        
                                                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcStartNewSessionResult_free (
            adb_WsJdbcStartNewSessionResult_t* _WsJdbcStartNewSessionResult,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                   
                                                                                        
                                                   
                                     
           
        adb_WsJdbcError_t* AXIS2_CALL
        adb_WsJdbcStartNewSessionResult_get_returnValue(
            adb_WsJdbcStartNewSessionResult_t* _WsJdbcStartNewSessionResult,
            const axutil_env_t *env);

           
                                  
                                                                                        
                                                   
                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcStartNewSessionResult_set_returnValue(
            adb_WsJdbcStartNewSessionResult_t* _WsJdbcStartNewSessionResult,
            const axutil_env_t *env,
            adb_WsJdbcError_t*  arg_returnValue);

           
                                   
                                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcStartNewSessionResult_reset_returnValue(
            adb_WsJdbcStartNewSessionResult_t* _WsJdbcStartNewSessionResult,
            const axutil_env_t *env);

        
        

           
                                 
                                                                                        
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsJdbcStartNewSessionResult_get_sessionId(
            adb_WsJdbcStartNewSessionResult_t* _WsJdbcStartNewSessionResult,
            const axutil_env_t *env);

           
                                
                                                                                        
                                                   
                                             
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcStartNewSessionResult_set_sessionId(
            adb_WsJdbcStartNewSessionResult_t* _WsJdbcStartNewSessionResult,
            const axutil_env_t *env,
            const axis2_char_t*  arg_sessionId);

           
                                 
                                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcStartNewSessionResult_reset_sessionId(
            adb_WsJdbcStartNewSessionResult_t* _WsJdbcStartNewSessionResult,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                            
                                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsJdbcStartNewSessionResult_is_returnValue_nil(
                adb_WsJdbcStartNewSessionResult_t* _WsJdbcStartNewSessionResult,
                const axutil_env_t *env);


        
           
                                                                
                                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcStartNewSessionResult_set_returnValue_nil(
                adb_WsJdbcStartNewSessionResult_t* _WsJdbcStartNewSessionResult,
                const axutil_env_t *env);
        

           
                                          
                                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsJdbcStartNewSessionResult_is_sessionId_nil(
                adb_WsJdbcStartNewSessionResult_t* _WsJdbcStartNewSessionResult,
                const axutil_env_t *env);


        
           
                                                              
                                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcStartNewSessionResult_set_sessionId_nil(
                adb_WsJdbcStartNewSessionResult_t* _WsJdbcStartNewSessionResult,
                const axutil_env_t *env);
        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                                        
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcStartNewSessionResult_deserialize(
            adb_WsJdbcStartNewSessionResult_t* _WsJdbcStartNewSessionResult,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                                        
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsJdbcStartNewSessionResult_declare_parent_namespaces(
                    adb_WsJdbcStartNewSessionResult_t* _WsJdbcStartNewSessionResult,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                                        
                                                   
                                                                            
                                                                                         
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsJdbcStartNewSessionResult_serialize(
            adb_WsJdbcStartNewSessionResult_t* _WsJdbcStartNewSessionResult,
            const axutil_env_t *env,
            axiom_node_t* WsJdbcStartNewSessionResult_om_node, axiom_element_t *WsJdbcStartNewSessionResult_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                             
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsJdbcStartNewSessionResult_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                                        
    

