

        #ifndef ADB_WSKEYVALUE_H
        #define ADB_WSKEYVALUE_H

          
                          
         
                                                
                                                                                         
          

          
                               
          
        typedef struct adb_WsKeyValue adb_WsKeyValue_t;

        

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
        

                                                                                                     

           
                                                    
                                                   
                                                        
           
        adb_WsKeyValue_t* AXIS2_CALL
        adb_WsKeyValue_create(
            const axutil_env_t *env );

           
                                       
                                                              
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsKeyValue_free (
            adb_WsKeyValue_t* _WsKeyValue,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                           
                                                      
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsKeyValue_get_key(
            adb_WsKeyValue_t* _WsKeyValue,
            const axutil_env_t *env);

           
                          
                                                      
                                                   
                                       
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsKeyValue_set_key(
            adb_WsKeyValue_t* _WsKeyValue,
            const axutil_env_t *env,
            const axis2_char_t*  arg_key);

           
                           
                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsKeyValue_reset_key(
            adb_WsKeyValue_t* _WsKeyValue,
            const axutil_env_t *env);

        
        

           
                             
                                                      
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsKeyValue_get_value(
            adb_WsKeyValue_t* _WsKeyValue,
            const axutil_env_t *env);

           
                            
                                                      
                                                   
                                         
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsKeyValue_set_value(
            adb_WsKeyValue_t* _WsKeyValue,
            const axutil_env_t *env,
            const axis2_char_t*  arg_value);

           
                             
                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsKeyValue_reset_value(
            adb_WsKeyValue_t* _WsKeyValue,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                    
                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsKeyValue_is_key_nil(
                adb_WsKeyValue_t* _WsKeyValue,
                const axutil_env_t *env);


        
           
                                                        
                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsKeyValue_set_key_nil(
                adb_WsKeyValue_t* _WsKeyValue,
                const axutil_env_t *env);
        

           
                                      
                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsKeyValue_is_value_nil(
                adb_WsKeyValue_t* _WsKeyValue,
                const axutil_env_t *env);


        
           
                                                          
                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsKeyValue_set_value_nil(
                adb_WsKeyValue_t* _WsKeyValue,
                const axutil_env_t *env);
        

                                                                                                      
                                                                                                      

        
           
                                            
                                                      
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsKeyValue_deserialize(
            adb_WsKeyValue_t* _WsKeyValue,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                      
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsKeyValue_declare_parent_namespaces(
                    adb_WsKeyValue_t* _WsKeyValue,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                      
                                                   
                                                           
                                                                        
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsKeyValue_serialize(
            adb_WsKeyValue_t* _WsKeyValue,
            const axutil_env_t *env,
            axiom_node_t* WsKeyValue_om_node, axiom_element_t *WsKeyValue_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                            
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsKeyValue_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                       
    

