

        #ifndef ADB_WSCONVERSIONPARAMS_H
        #define ADB_WSCONVERSIONPARAMS_H

          
                                  
         
                                                
                                                                                         
          

          
                                       
          
        typedef struct adb_WsConversionParams adb_WsConversionParams_t;

        

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
        

                                                                                                     

           
                                                            
                                                   
                                                                
           
        adb_WsConversionParams_t* AXIS2_CALL
        adb_WsConversionParams_create(
            const axutil_env_t *env );

           
                                               
                                                                              
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConversionParams_free (
            adb_WsConversionParams_t* _WsConversionParams,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                    
                                                                      
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsConversionParams_get_JsonFilePath(
            adb_WsConversionParams_t* _WsConversionParams,
            const axutil_env_t *env);

           
                                   
                                                                      
                                                   
                                                
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConversionParams_set_JsonFilePath(
            adb_WsConversionParams_t* _WsConversionParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_JsonFilePath);

           
                                    
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConversionParams_reset_JsonFilePath(
            adb_WsConversionParams_t* _WsConversionParams,
            const axutil_env_t *env);

        
        

           
                                   
                                                                      
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsConversionParams_get_XmlFilePath(
            adb_WsConversionParams_t* _WsConversionParams,
            const axutil_env_t *env);

           
                                  
                                                                      
                                                   
                                               
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConversionParams_set_XmlFilePath(
            adb_WsConversionParams_t* _WsConversionParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_XmlFilePath);

           
                                   
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConversionParams_reset_XmlFilePath(
            adb_WsConversionParams_t* _WsConversionParams,
            const axutil_env_t *env);

        
        

           
                                        
                                                                      
                                                   
                          
           
        int64_t AXIS2_CALL
        adb_WsConversionParams_get_TransformationId(
            adb_WsConversionParams_t* _WsConversionParams,
            const axutil_env_t *env);

           
                                       
                                                                      
                                                   
                                              
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConversionParams_set_TransformationId(
            adb_WsConversionParams_t* _WsConversionParams,
            const axutil_env_t *env,
            const int64_t  arg_TransformationId);

           
                                        
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConversionParams_reset_TransformationId(
            adb_WsConversionParams_t* _WsConversionParams,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                             
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsConversionParams_is_JsonFilePath_nil(
                adb_WsConversionParams_t* _WsConversionParams,
                const axutil_env_t *env);


        
           
                                                                 
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConversionParams_set_JsonFilePath_nil(
                adb_WsConversionParams_t* _WsConversionParams,
                const axutil_env_t *env);
        

           
                                            
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsConversionParams_is_XmlFilePath_nil(
                adb_WsConversionParams_t* _WsConversionParams,
                const axutil_env_t *env);


        
           
                                                                
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConversionParams_set_XmlFilePath_nil(
                adb_WsConversionParams_t* _WsConversionParams,
                const axutil_env_t *env);
        

           
                                                 
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsConversionParams_is_TransformationId_nil(
                adb_WsConversionParams_t* _WsConversionParams,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                      
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsConversionParams_deserialize(
            adb_WsConversionParams_t* _WsConversionParams,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                      
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsConversionParams_declare_parent_namespaces(
                    adb_WsConversionParams_t* _WsConversionParams,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                      
                                                   
                                                                   
                                                                                
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsConversionParams_serialize(
            adb_WsConversionParams_t* _WsConversionParams,
            const axutil_env_t *env,
            axiom_node_t* WsConversionParams_om_node, axiom_element_t *WsConversionParams_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                    
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsConversionParams_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                               
    

