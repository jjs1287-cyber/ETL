

        #ifndef ADB_WSBULKLOADSESSIONID_H
        #define ADB_WSBULKLOADSESSIONID_H

          
                                   
         
                                                
                                                                                         
          

          
                                        
          
        typedef struct adb_WsBulkLoadSessionId adb_WsBulkLoadSessionId_t;

        

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
        

                                                                                                     

           
                                                             
                                                   
                                                                 
           
        adb_WsBulkLoadSessionId_t* AXIS2_CALL
        adb_WsBulkLoadSessionId_create(
            const axutil_env_t *env );

           
                                                
                                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadSessionId_free (
            adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                    
                                                                        
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsBulkLoadSessionId_get_etlforgeUser(
            adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
            const axutil_env_t *env);

           
                                   
                                                                        
                                                   
                                                
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadSessionId_set_etlforgeUser(
            adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
            const axutil_env_t *env,
            const axis2_char_t*  arg_etlforgeUser);

           
                                    
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadSessionId_reset_etlforgeUser(
            adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
            const axutil_env_t *env);

        
        

           
                              
                                                                        
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsBulkLoadSessionId_get_md5Pwd(
            adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
            const axutil_env_t *env);

           
                             
                                                                        
                                                   
                                          
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadSessionId_set_md5Pwd(
            adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
            const axutil_env_t *env,
            const axis2_char_t*  arg_md5Pwd);

           
                              
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadSessionId_reset_md5Pwd(
            adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
            const axutil_env_t *env);

        
        

           
                                   
                                                                        
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsBulkLoadSessionId_get_sessionGuid(
            adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
            const axutil_env_t *env);

           
                                  
                                                                        
                                                   
                                               
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadSessionId_set_sessionGuid(
            adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
            const axutil_env_t *env,
            const axis2_char_t*  arg_sessionGuid);

           
                                   
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadSessionId_reset_sessionGuid(
            adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                             
                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadSessionId_is_etlforgeUser_nil(
                adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                const axutil_env_t *env);


        
           
                                                                 
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadSessionId_set_etlforgeUser_nil(
                adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                const axutil_env_t *env);
        

           
                                       
                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadSessionId_is_md5Pwd_nil(
                adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                const axutil_env_t *env);


        
           
                                                           
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadSessionId_set_md5Pwd_nil(
                adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                const axutil_env_t *env);
        

           
                                            
                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadSessionId_is_sessionGuid_nil(
                adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                const axutil_env_t *env);


        
           
                                                                
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadSessionId_set_sessionGuid_nil(
                adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                const axutil_env_t *env);
        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                        
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadSessionId_deserialize(
            adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                        
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsBulkLoadSessionId_declare_parent_namespaces(
                    adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                        
                                                   
                                                                    
                                                                                 
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsBulkLoadSessionId_serialize(
            adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
            const axutil_env_t *env,
            axiom_node_t* WsBulkLoadSessionId_om_node, axiom_element_t *WsBulkLoadSessionId_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                     
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadSessionId_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                                
    

