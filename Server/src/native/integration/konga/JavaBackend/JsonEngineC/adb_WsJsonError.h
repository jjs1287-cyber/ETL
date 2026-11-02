

        #ifndef ADB_WSJSONERROR_H
        #define ADB_WSJSONERROR_H

          
                           
         
                                                
                                                                                         
          

          
                                
          
        typedef struct adb_WsJsonError adb_WsJsonError_t;

        

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
        

                                                                                                     

           
                                                     
                                                   
                                                         
           
        adb_WsJsonError_t* AXIS2_CALL
        adb_WsJsonError_create(
            const axutil_env_t *env );

           
                                        
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonError_free (
            adb_WsJsonError_t* _WsJsonError,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                 
                                                        
                                                   
                      
           
        int AXIS2_CALL
        adb_WsJsonError_get_ErrorCode(
            adb_WsJsonError_t* _WsJsonError,
            const axutil_env_t *env);

           
                                
                                                        
                                                   
                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonError_set_ErrorCode(
            adb_WsJsonError_t* _WsJsonError,
            const axutil_env_t *env,
            const int  arg_ErrorCode);

           
                                 
                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonError_reset_ErrorCode(
            adb_WsJsonError_t* _WsJsonError,
            const axutil_env_t *env);

        
        

           
                             
                                                        
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsJsonError_get_Error(
            adb_WsJsonError_t* _WsJsonError,
            const axutil_env_t *env);

           
                            
                                                        
                                                   
                                         
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonError_set_Error(
            adb_WsJsonError_t* _WsJsonError,
            const axutil_env_t *env,
            const axis2_char_t*  arg_Error);

           
                             
                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonError_reset_Error(
            adb_WsJsonError_t* _WsJsonError,
            const axutil_env_t *env);

        
        

           
                                
                                                        
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsJsonError_get_Warnings(
            adb_WsJsonError_t* _WsJsonError,
            const axutil_env_t *env);

           
                               
                                                        
                                                   
                                            
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonError_set_Warnings(
            adb_WsJsonError_t* _WsJsonError,
            const axutil_env_t *env,
            const axis2_char_t*  arg_Warnings);

           
                                
                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonError_reset_Warnings(
            adb_WsJsonError_t* _WsJsonError,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                          
                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsJsonError_is_ErrorCode_nil(
                adb_WsJsonError_t* _WsJsonError,
                const axutil_env_t *env);


        

           
                                      
                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsJsonError_is_Error_nil(
                adb_WsJsonError_t* _WsJsonError,
                const axutil_env_t *env);


        
           
                                                          
                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonError_set_Error_nil(
                adb_WsJsonError_t* _WsJsonError,
                const axutil_env_t *env);
        

           
                                         
                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsJsonError_is_Warnings_nil(
                adb_WsJsonError_t* _WsJsonError,
                const axutil_env_t *env);


        
           
                                                             
                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonError_set_Warnings_nil(
                adb_WsJsonError_t* _WsJsonError,
                const axutil_env_t *env);
        

                                                                                                      
                                                                                                      

        
           
                                            
                                                        
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsJsonError_deserialize(
            adb_WsJsonError_t* _WsJsonError,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                        
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsJsonError_declare_parent_namespaces(
                    adb_WsJsonError_t* _WsJsonError,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                        
                                                   
                                                            
                                                                         
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsJsonError_serialize(
            adb_WsJsonError_t* _WsJsonError,
            const axutil_env_t *env,
            axiom_node_t* WsJsonError_om_node, axiom_element_t *WsJsonError_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                             
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsJsonError_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                        
    

