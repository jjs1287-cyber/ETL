

        #ifndef ADB_WSJDBCSESSIONID_H
        #define ADB_WSJDBCSESSIONID_H

          
                               
         
                                                
                                                                                         
          

          
                                    
          
        typedef struct adb_WsJdbcSessionId adb_WsJdbcSessionId_t;

        

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
        

                                                                                                     

           
                                                         
                                                   
                                                             
           
        adb_WsJdbcSessionId_t* AXIS2_CALL
        adb_WsJdbcSessionId_create(
            const axutil_env_t *env );

           
                                            
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcSessionId_free (
            adb_WsJdbcSessionId_t* _WsJdbcSessionId,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                    
                                                                
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsJdbcSessionId_get_etlforgeUser(
            adb_WsJdbcSessionId_t* _WsJdbcSessionId,
            const axutil_env_t *env);

           
                                   
                                                                
                                                   
                                                
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcSessionId_set_etlforgeUser(
            adb_WsJdbcSessionId_t* _WsJdbcSessionId,
            const axutil_env_t *env,
            const axis2_char_t*  arg_etlforgeUser);

           
                                    
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcSessionId_reset_etlforgeUser(
            adb_WsJdbcSessionId_t* _WsJdbcSessionId,
            const axutil_env_t *env);

        
        

           
                              
                                                                
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsJdbcSessionId_get_md5Pwd(
            adb_WsJdbcSessionId_t* _WsJdbcSessionId,
            const axutil_env_t *env);

           
                             
                                                                
                                                   
                                          
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcSessionId_set_md5Pwd(
            adb_WsJdbcSessionId_t* _WsJdbcSessionId,
            const axutil_env_t *env,
            const axis2_char_t*  arg_md5Pwd);

           
                              
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcSessionId_reset_md5Pwd(
            adb_WsJdbcSessionId_t* _WsJdbcSessionId,
            const axutil_env_t *env);

        
        

           
                                   
                                                                
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsJdbcSessionId_get_sessionGuid(
            adb_WsJdbcSessionId_t* _WsJdbcSessionId,
            const axutil_env_t *env);

           
                                  
                                                                
                                                   
                                               
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcSessionId_set_sessionGuid(
            adb_WsJdbcSessionId_t* _WsJdbcSessionId,
            const axutil_env_t *env,
            const axis2_char_t*  arg_sessionGuid);

           
                                   
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcSessionId_reset_sessionGuid(
            adb_WsJdbcSessionId_t* _WsJdbcSessionId,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                             
                                                                
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsJdbcSessionId_is_etlforgeUser_nil(
                adb_WsJdbcSessionId_t* _WsJdbcSessionId,
                const axutil_env_t *env);


        
           
                                                                 
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcSessionId_set_etlforgeUser_nil(
                adb_WsJdbcSessionId_t* _WsJdbcSessionId,
                const axutil_env_t *env);
        

           
                                       
                                                                
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsJdbcSessionId_is_md5Pwd_nil(
                adb_WsJdbcSessionId_t* _WsJdbcSessionId,
                const axutil_env_t *env);


        
           
                                                           
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcSessionId_set_md5Pwd_nil(
                adb_WsJdbcSessionId_t* _WsJdbcSessionId,
                const axutil_env_t *env);
        

           
                                            
                                                                
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsJdbcSessionId_is_sessionGuid_nil(
                adb_WsJdbcSessionId_t* _WsJdbcSessionId,
                const axutil_env_t *env);


        
           
                                                                
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcSessionId_set_sessionGuid_nil(
                adb_WsJdbcSessionId_t* _WsJdbcSessionId,
                const axutil_env_t *env);
        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJdbcSessionId_deserialize(
            adb_WsJdbcSessionId_t* _WsJdbcSessionId,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsJdbcSessionId_declare_parent_namespaces(
                    adb_WsJdbcSessionId_t* _WsJdbcSessionId,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                
                                                   
                                                                
                                                                             
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsJdbcSessionId_serialize(
            adb_WsJdbcSessionId_t* _WsJdbcSessionId,
            const axutil_env_t *env,
            axiom_node_t* WsJdbcSessionId_om_node, axiom_element_t *WsJdbcSessionId_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                 
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsJdbcSessionId_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                            
    

