

        #ifndef ADB_CLOSEJSONSESSIONRESPONSE_H
        #define ADB_CLOSEJSONSESSIONRESPONSE_H

          
                                        
         
                                                
                                                                                         
          

          
                                             
          
        typedef struct adb_closeJsonSessionResponse adb_closeJsonSessionResponse_t;

        
          #include "adb_WsJsonError.h"
          

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
        

                                                                                                     

           
                                                                  
                                                   
                                                                      
           
        adb_closeJsonSessionResponse_t* AXIS2_CALL
        adb_closeJsonSessionResponse_create(
            const axutil_env_t *env );

           
                                                     
                                                                                          
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeJsonSessionResponse_free (
            adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                              
                                                                                  
                                                   
                                     
           
        adb_WsJsonError_t* AXIS2_CALL
        adb_closeJsonSessionResponse_get_closeJsonSessionReturn(
            adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
            const axutil_env_t *env);

           
                                             
                                                                                  
                                                   
                                                               
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeJsonSessionResponse_set_closeJsonSessionReturn(
            adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
            const axutil_env_t *env,
            adb_WsJsonError_t*  arg_closeJsonSessionReturn);

           
                                              
                                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeJsonSessionResponse_reset_closeJsonSessionReturn(
            adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                                       
                                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_closeJsonSessionResponse_is_closeJsonSessionReturn_nil(
                adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                                  
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeJsonSessionResponse_deserialize(
            adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                                  
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_closeJsonSessionResponse_declare_parent_namespaces(
                    adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                                  
                                                   
                                                                         
                                                                                      
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_closeJsonSessionResponse_serialize(
            adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
            const axutil_env_t *env,
            axiom_node_t* closeJsonSessionResponse_om_node, axiom_element_t *closeJsonSessionResponse_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                          
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_closeJsonSessionResponse_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                                     
    

