

        #ifndef ADB_WSCONNECTIONPARAMS_H
        #define ADB_WSCONNECTIONPARAMS_H

          
                                  
         
                                                
                                                                                         
          

          
                                       
          
        typedef struct adb_WsConnectionParams adb_WsConnectionParams_t;

        

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
        

                                                                                                     

           
                                                            
                                                   
                                                                
           
        adb_WsConnectionParams_t* AXIS2_CALL
        adb_WsConnectionParams_create(
            const axutil_env_t *env );

           
                                               
                                                                              
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_free (
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                              
                                                                      
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsConnectionParams_get_server(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

           
                             
                                                                      
                                                   
                                          
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_set_server(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_server);

           
                              
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_reset_server(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

        
        

           
                            
                                                                      
                                                   
                      
           
        int AXIS2_CALL
        adb_WsConnectionParams_get_port(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

           
                           
                                                                      
                                                   
                              
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_set_port(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env,
            const int  arg_port);

           
                            
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_reset_port(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

        
        

           
                                
                                                                      
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsConnectionParams_get_database(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

           
                               
                                                                      
                                                   
                                            
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_set_database(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_database);

           
                                
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_reset_database(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

        
        

           
                            
                                                                      
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsConnectionParams_get_user(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

           
                           
                                                                      
                                                   
                                        
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_set_user(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_user);

           
                            
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_reset_user(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

        
        

           
                                
                                                                      
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsConnectionParams_get_password(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

           
                               
                                                                      
                                                   
                                            
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_set_password(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_password);

           
                                
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_reset_password(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

        
        

           
                                              
                                                                      
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsConnectionParams_get_manualConnectionString(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

           
                                             
                                                                      
                                                   
                                                          
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_set_manualConnectionString(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_manualConnectionString);

           
                                              
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_reset_manualConnectionString(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

        
        

           
                                        
                                                                      
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsConnectionParams_get_additionalParams(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

           
                                       
                                                                      
                                                   
                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_set_additionalParams(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_additionalParams);

           
                                        
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_reset_additionalParams(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

        
        

           
                                  
                                                                      
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsConnectionParams_get_driverName(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

           
                                 
                                                                      
                                                   
                                              
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_set_driverName(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_driverName);

           
                                  
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_reset_driverName(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

        
        

           
                               
                                                                      
                                                   
                      
           
        int AXIS2_CALL
        adb_WsConnectionParams_get_timeout(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

           
                              
                                                                      
                                                   
                                 
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_set_timeout(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env,
            const int  arg_timeout);

           
                               
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_reset_timeout(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

        
        

           
                                                 
                                                                      
                                                   
                      
           
        int AXIS2_CALL
        adb_WsConnectionParams_get_transactionIsolationLevel(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

           
                                                
                                                                      
                                                   
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_set_transactionIsolationLevel(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env,
            const int  arg_transactionIsolationLevel);

           
                                                 
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_reset_transactionIsolationLevel(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                       
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsConnectionParams_is_server_nil(
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env);


        
           
                                                           
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_set_server_nil(
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env);
        

           
                                     
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsConnectionParams_is_port_nil(
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env);


        

           
                                         
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsConnectionParams_is_database_nil(
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env);


        
           
                                                             
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_set_database_nil(
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env);
        

           
                                     
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsConnectionParams_is_user_nil(
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env);


        
           
                                                         
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_set_user_nil(
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env);
        

           
                                         
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsConnectionParams_is_password_nil(
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env);


        
           
                                                             
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_set_password_nil(
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env);
        

           
                                                       
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsConnectionParams_is_manualConnectionString_nil(
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env);


        
           
                                                                           
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_set_manualConnectionString_nil(
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env);
        

           
                                                 
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsConnectionParams_is_additionalParams_nil(
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env);


        
           
                                                                     
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_set_additionalParams_nil(
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env);
        

           
                                           
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsConnectionParams_is_driverName_nil(
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env);


        
           
                                                               
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_set_driverName_nil(
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env);
        

           
                                        
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsConnectionParams_is_timeout_nil(
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env);


        

           
                                                          
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsConnectionParams_is_transactionIsolationLevel_nil(
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                      
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_deserialize(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                      
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsConnectionParams_declare_parent_namespaces(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                      
                                                   
                                                                   
                                                                                
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsConnectionParams_serialize(
            adb_WsConnectionParams_t* _WsConnectionParams,
            const axutil_env_t *env,
            axiom_node_t* WsConnectionParams_om_node, axiom_element_t *WsConnectionParams_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                    
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsConnectionParams_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                               
    

