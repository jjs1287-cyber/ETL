

        #ifndef ADB_WSBULKLOADERROR_H
        #define ADB_WSBULKLOADERROR_H

          
                               
         
                                                
                                                                                         
          

          
                                    
          
        typedef struct adb_WsBulkLoadError adb_WsBulkLoadError_t;

        

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
        

                                                                                                     

           
                                                         
                                                   
                                                             
           
        adb_WsBulkLoadError_t* AXIS2_CALL
        adb_WsBulkLoadError_create(
            const axutil_env_t *env );

           
                                            
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadError_free (
            adb_WsBulkLoadError_t* _WsBulkLoadError,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                 
                                                                
                                                   
                      
           
        int AXIS2_CALL
        adb_WsBulkLoadError_get_errorCode(
            adb_WsBulkLoadError_t* _WsBulkLoadError,
            const axutil_env_t *env);

           
                                
                                                                
                                                   
                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadError_set_errorCode(
            adb_WsBulkLoadError_t* _WsBulkLoadError,
            const axutil_env_t *env,
            const int  arg_errorCode);

           
                                 
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadError_reset_errorCode(
            adb_WsBulkLoadError_t* _WsBulkLoadError,
            const axutil_env_t *env);

        
        

           
                             
                                                                
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsBulkLoadError_get_error(
            adb_WsBulkLoadError_t* _WsBulkLoadError,
            const axutil_env_t *env);

           
                            
                                                                
                                                   
                                         
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadError_set_error(
            adb_WsBulkLoadError_t* _WsBulkLoadError,
            const axutil_env_t *env,
            const axis2_char_t*  arg_error);

           
                             
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadError_reset_error(
            adb_WsBulkLoadError_t* _WsBulkLoadError,
            const axutil_env_t *env);

        
        

           
                                
                                                                
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsBulkLoadError_get_warnings(
            adb_WsBulkLoadError_t* _WsBulkLoadError,
            const axutil_env_t *env);

           
                               
                                                                
                                                   
                                            
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadError_set_warnings(
            adb_WsBulkLoadError_t* _WsBulkLoadError,
            const axutil_env_t *env,
            const axis2_char_t*  arg_warnings);

           
                                
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadError_reset_warnings(
            adb_WsBulkLoadError_t* _WsBulkLoadError,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                          
                                                                
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadError_is_errorCode_nil(
                adb_WsBulkLoadError_t* _WsBulkLoadError,
                const axutil_env_t *env);


        

           
                                      
                                                                
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadError_is_error_nil(
                adb_WsBulkLoadError_t* _WsBulkLoadError,
                const axutil_env_t *env);


        
           
                                                          
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadError_set_error_nil(
                adb_WsBulkLoadError_t* _WsBulkLoadError,
                const axutil_env_t *env);
        

           
                                         
                                                                
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadError_is_warnings_nil(
                adb_WsBulkLoadError_t* _WsBulkLoadError,
                const axutil_env_t *env);


        
           
                                                             
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadError_set_warnings_nil(
                adb_WsBulkLoadError_t* _WsBulkLoadError,
                const axutil_env_t *env);
        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadError_deserialize(
            adb_WsBulkLoadError_t* _WsBulkLoadError,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsBulkLoadError_declare_parent_namespaces(
                    adb_WsBulkLoadError_t* _WsBulkLoadError,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                
                                                   
                                                                
                                                                             
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsBulkLoadError_serialize(
            adb_WsBulkLoadError_t* _WsBulkLoadError,
            const axutil_env_t *env,
            axiom_node_t* WsBulkLoadError_om_node, axiom_element_t *WsBulkLoadError_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                 
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsBulkLoadError_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                            
    

