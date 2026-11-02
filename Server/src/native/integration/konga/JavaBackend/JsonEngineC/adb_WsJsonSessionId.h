

        #ifndef ADB_WSJSONSESSIONID_H
        #define ADB_WSJSONSESSIONID_H

          
                               
         
                                                
                                                                                         
          

          
                                    
          
        typedef struct adb_WsJsonSessionId adb_WsJsonSessionId_t;

        

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
        

                                                                                                     

           
                                                         
                                                   
                                                             
           
        adb_WsJsonSessionId_t* AXIS2_CALL
        adb_WsJsonSessionId_create(
            const axutil_env_t *env );

           
                                            
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonSessionId_free (
            adb_WsJsonSessionId_t* _WsJsonSessionId,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                    
                                                                
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsJsonSessionId_get_ETLForgeUser(
            adb_WsJsonSessionId_t* _WsJsonSessionId,
            const axutil_env_t *env);

           
                                   
                                                                
                                                   
                                                
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonSessionId_set_ETLForgeUser(
            adb_WsJsonSessionId_t* _WsJsonSessionId,
            const axutil_env_t *env,
            const axis2_char_t*  arg_ETLForgeUser);

           
                                    
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonSessionId_reset_ETLForgeUser(
            adb_WsJsonSessionId_t* _WsJsonSessionId,
            const axutil_env_t *env);

        
        

           
                              
                                                                
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsJsonSessionId_get_Md5Pwd(
            adb_WsJsonSessionId_t* _WsJsonSessionId,
            const axutil_env_t *env);

           
                             
                                                                
                                                   
                                          
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonSessionId_set_Md5Pwd(
            adb_WsJsonSessionId_t* _WsJsonSessionId,
            const axutil_env_t *env,
            const axis2_char_t*  arg_Md5Pwd);

           
                              
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonSessionId_reset_Md5Pwd(
            adb_WsJsonSessionId_t* _WsJsonSessionId,
            const axutil_env_t *env);

        
        

           
                                   
                                                                
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsJsonSessionId_get_SessionGuid(
            adb_WsJsonSessionId_t* _WsJsonSessionId,
            const axutil_env_t *env);

           
                                  
                                                                
                                                   
                                               
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonSessionId_set_SessionGuid(
            adb_WsJsonSessionId_t* _WsJsonSessionId,
            const axutil_env_t *env,
            const axis2_char_t*  arg_SessionGuid);

           
                                   
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonSessionId_reset_SessionGuid(
            adb_WsJsonSessionId_t* _WsJsonSessionId,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                             
                                                                
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsJsonSessionId_is_ETLForgeUser_nil(
                adb_WsJsonSessionId_t* _WsJsonSessionId,
                const axutil_env_t *env);


        
           
                                                                 
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonSessionId_set_ETLForgeUser_nil(
                adb_WsJsonSessionId_t* _WsJsonSessionId,
                const axutil_env_t *env);
        

           
                                       
                                                                
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsJsonSessionId_is_Md5Pwd_nil(
                adb_WsJsonSessionId_t* _WsJsonSessionId,
                const axutil_env_t *env);


        
           
                                                           
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonSessionId_set_Md5Pwd_nil(
                adb_WsJsonSessionId_t* _WsJsonSessionId,
                const axutil_env_t *env);
        

           
                                            
                                                                
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsJsonSessionId_is_SessionGuid_nil(
                adb_WsJsonSessionId_t* _WsJsonSessionId,
                const axutil_env_t *env);


        
           
                                                                
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonSessionId_set_SessionGuid_nil(
                adb_WsJsonSessionId_t* _WsJsonSessionId,
                const axutil_env_t *env);
        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonSessionId_deserialize(
            adb_WsJsonSessionId_t* _WsJsonSessionId,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsJsonSessionId_declare_parent_namespaces(
                    adb_WsJsonSessionId_t* _WsJsonSessionId,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                
                                                   
                                                                
                                                                             
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsJsonSessionId_serialize(
            adb_WsJsonSessionId_t* _WsJsonSessionId,
            const axutil_env_t *env,
            axiom_node_t* WsJsonSessionId_om_node, axiom_element_t *WsJsonSessionId_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                 
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsJsonSessionId_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                            
    

