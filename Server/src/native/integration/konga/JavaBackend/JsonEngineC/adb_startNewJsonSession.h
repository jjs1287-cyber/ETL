

        #ifndef ADB_STARTNEWJSONSESSION_H
        #define ADB_STARTNEWJSONSESSION_H

          
                                   
         
                                                
                                                                                         
          

          
                                        
          
        typedef struct adb_startNewJsonSession adb_startNewJsonSession_t;

        

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
        

                                                                                                     

           
                                                             
                                                   
                                                                 
           
        adb_startNewJsonSession_t* AXIS2_CALL
        adb_startNewJsonSession_create(
            const axutil_env_t *env );

           
                                                
                                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewJsonSession_free (
            adb_startNewJsonSession_t* _startNewJsonSession,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                    
                                                                        
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_startNewJsonSession_get_ETLForgeUser(
            adb_startNewJsonSession_t* _startNewJsonSession,
            const axutil_env_t *env);

           
                                   
                                                                        
                                                   
                                                
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewJsonSession_set_ETLForgeUser(
            adb_startNewJsonSession_t* _startNewJsonSession,
            const axutil_env_t *env,
            const axis2_char_t*  arg_ETLForgeUser);

           
                                    
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewJsonSession_reset_ETLForgeUser(
            adb_startNewJsonSession_t* _startNewJsonSession,
            const axutil_env_t *env);

        
        

           
                              
                                                                        
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_startNewJsonSession_get_Md5Pwd(
            adb_startNewJsonSession_t* _startNewJsonSession,
            const axutil_env_t *env);

           
                             
                                                                        
                                                   
                                          
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewJsonSession_set_Md5Pwd(
            adb_startNewJsonSession_t* _startNewJsonSession,
            const axutil_env_t *env,
            const axis2_char_t*  arg_Md5Pwd);

           
                              
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewJsonSession_reset_Md5Pwd(
            adb_startNewJsonSession_t* _startNewJsonSession,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                             
                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_startNewJsonSession_is_ETLForgeUser_nil(
                adb_startNewJsonSession_t* _startNewJsonSession,
                const axutil_env_t *env);


        

           
                                       
                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_startNewJsonSession_is_Md5Pwd_nil(
                adb_startNewJsonSession_t* _startNewJsonSession,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                        
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewJsonSession_deserialize(
            adb_startNewJsonSession_t* _startNewJsonSession,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                        
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_startNewJsonSession_declare_parent_namespaces(
                    adb_startNewJsonSession_t* _startNewJsonSession,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                        
                                                   
                                                                    
                                                                                 
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_startNewJsonSession_serialize(
            adb_startNewJsonSession_t* _startNewJsonSession,
            const axutil_env_t *env,
            axiom_node_t* startNewJsonSession_om_node, axiom_element_t *startNewJsonSession_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                     
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_startNewJsonSession_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                                
    

