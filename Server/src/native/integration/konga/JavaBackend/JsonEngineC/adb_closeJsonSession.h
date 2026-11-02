

        #ifndef ADB_CLOSEJSONSESSION_H
        #define ADB_CLOSEJSONSESSION_H

          
                                
         
                                                
                                                                                         
          

          
                                     
          
        typedef struct adb_closeJsonSession adb_closeJsonSession_t;

        
          #include "adb_WsJsonSessionId.h"
          

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
        

                                                                                                     

           
                                                          
                                                   
                                                              
           
        adb_closeJsonSession_t* AXIS2_CALL
        adb_closeJsonSession_create(
            const axutil_env_t *env );

           
                                             
                                                                          
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeJsonSession_free (
            adb_closeJsonSession_t* _closeJsonSession,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                 
                                                                  
                                                   
                                         
           
        adb_WsJsonSessionId_t* AXIS2_CALL
        adb_closeJsonSession_get_SessionId(
            adb_closeJsonSession_t* _closeJsonSession,
            const axutil_env_t *env);

           
                                
                                                                  
                                                   
                                                      
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeJsonSession_set_SessionId(
            adb_closeJsonSession_t* _closeJsonSession,
            const axutil_env_t *env,
            adb_WsJsonSessionId_t*  arg_SessionId);

           
                                 
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeJsonSession_reset_SessionId(
            adb_closeJsonSession_t* _closeJsonSession,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                          
                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_closeJsonSession_is_SessionId_nil(
                adb_closeJsonSession_t* _closeJsonSession,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                  
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeJsonSession_deserialize(
            adb_closeJsonSession_t* _closeJsonSession,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                  
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_closeJsonSession_declare_parent_namespaces(
                    adb_closeJsonSession_t* _closeJsonSession,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                  
                                                   
                                                                 
                                                                              
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_closeJsonSession_serialize(
            adb_closeJsonSession_t* _closeJsonSession,
            const axutil_env_t *env,
            axiom_node_t* closeJsonSession_om_node, axiom_element_t *closeJsonSession_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                  
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_closeJsonSession_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                             
    

