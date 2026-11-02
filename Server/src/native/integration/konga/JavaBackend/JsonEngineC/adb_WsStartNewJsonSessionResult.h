

        #ifndef ADB_WSSTARTNEWJSONSESSIONRESULT_H
        #define ADB_WSSTARTNEWJSONSESSIONRESULT_H

          
                                           
         
                                                
                                                                                         
          

          
                                                
          
        typedef struct adb_WsStartNewJsonSessionResult adb_WsStartNewJsonSessionResult_t;

        
          #include "adb_WsJsonError.h"
          

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
        

                                                                                                     

           
                                                                     
                                                   
                                                                         
           
        adb_WsStartNewJsonSessionResult_t* AXIS2_CALL
        adb_WsStartNewJsonSessionResult_create(
            const axutil_env_t *env );

           
                                                        
                                                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsStartNewJsonSessionResult_free (
            adb_WsStartNewJsonSessionResult_t* _WsStartNewJsonSessionResult,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                 
                                                                                        
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsStartNewJsonSessionResult_get_SessionId(
            adb_WsStartNewJsonSessionResult_t* _WsStartNewJsonSessionResult,
            const axutil_env_t *env);

           
                                
                                                                                        
                                                   
                                             
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsStartNewJsonSessionResult_set_SessionId(
            adb_WsStartNewJsonSessionResult_t* _WsStartNewJsonSessionResult,
            const axutil_env_t *env,
            const axis2_char_t*  arg_SessionId);

           
                                 
                                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsStartNewJsonSessionResult_reset_SessionId(
            adb_WsStartNewJsonSessionResult_t* _WsStartNewJsonSessionResult,
            const axutil_env_t *env);

        
        

           
                             
                                                                                        
                                                   
                                     
           
        adb_WsJsonError_t* AXIS2_CALL
        adb_WsStartNewJsonSessionResult_get_Error(
            adb_WsStartNewJsonSessionResult_t* _WsStartNewJsonSessionResult,
            const axutil_env_t *env);

           
                            
                                                                                        
                                                   
                                              
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsStartNewJsonSessionResult_set_Error(
            adb_WsStartNewJsonSessionResult_t* _WsStartNewJsonSessionResult,
            const axutil_env_t *env,
            adb_WsJsonError_t*  arg_Error);

           
                             
                                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsStartNewJsonSessionResult_reset_Error(
            adb_WsStartNewJsonSessionResult_t* _WsStartNewJsonSessionResult,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                          
                                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsStartNewJsonSessionResult_is_SessionId_nil(
                adb_WsStartNewJsonSessionResult_t* _WsStartNewJsonSessionResult,
                const axutil_env_t *env);


        
           
                                                              
                                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsStartNewJsonSessionResult_set_SessionId_nil(
                adb_WsStartNewJsonSessionResult_t* _WsStartNewJsonSessionResult,
                const axutil_env_t *env);
        

           
                                      
                                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsStartNewJsonSessionResult_is_Error_nil(
                adb_WsStartNewJsonSessionResult_t* _WsStartNewJsonSessionResult,
                const axutil_env_t *env);


        
           
                                                          
                                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsStartNewJsonSessionResult_set_Error_nil(
                adb_WsStartNewJsonSessionResult_t* _WsStartNewJsonSessionResult,
                const axutil_env_t *env);
        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                                        
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsStartNewJsonSessionResult_deserialize(
            adb_WsStartNewJsonSessionResult_t* _WsStartNewJsonSessionResult,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                                        
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsStartNewJsonSessionResult_declare_parent_namespaces(
                    adb_WsStartNewJsonSessionResult_t* _WsStartNewJsonSessionResult,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                                        
                                                   
                                                                            
                                                                                         
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsStartNewJsonSessionResult_serialize(
            adb_WsStartNewJsonSessionResult_t* _WsStartNewJsonSessionResult,
            const axutil_env_t *env,
            axiom_node_t* WsStartNewJsonSessionResult_om_node, axiom_element_t *WsStartNewJsonSessionResult_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                             
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsStartNewJsonSessionResult_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                                        
    

