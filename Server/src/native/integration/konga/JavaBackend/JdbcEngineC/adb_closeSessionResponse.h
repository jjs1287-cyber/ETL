

        #ifndef ADB_CLOSESESSIONRESPONSE_H
        #define ADB_CLOSESESSIONRESPONSE_H

          
                                    
         
                                                
                                                                                         
          

          
                                         
          
        typedef struct adb_closeSessionResponse adb_closeSessionResponse_t;

        
          #include "adb_WsJdbcError.h"
          

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
        

                                                                                                     

           
                                                              
                                                   
                                                                  
           
        adb_closeSessionResponse_t* AXIS2_CALL
        adb_closeSessionResponse_create(
            const axutil_env_t *env );

           
                                                 
                                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeSessionResponse_free (
            adb_closeSessionResponse_t* _closeSessionResponse,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                          
                                                                          
                                                   
                                     
           
        adb_WsJdbcError_t* AXIS2_CALL
        adb_closeSessionResponse_get_closeSessionReturn(
            adb_closeSessionResponse_t* _closeSessionResponse,
            const axutil_env_t *env);

           
                                         
                                                                          
                                                   
                                                           
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeSessionResponse_set_closeSessionReturn(
            adb_closeSessionResponse_t* _closeSessionResponse,
            const axutil_env_t *env,
            adb_WsJdbcError_t*  arg_closeSessionReturn);

           
                                          
                                                                          
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeSessionResponse_reset_closeSessionReturn(
            adb_closeSessionResponse_t* _closeSessionResponse,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                                   
                                                                          
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_closeSessionResponse_is_closeSessionReturn_nil(
                adb_closeSessionResponse_t* _closeSessionResponse,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                          
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeSessionResponse_deserialize(
            adb_closeSessionResponse_t* _closeSessionResponse,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                          
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_closeSessionResponse_declare_parent_namespaces(
                    adb_closeSessionResponse_t* _closeSessionResponse,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                          
                                                   
                                                                     
                                                                                  
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_closeSessionResponse_serialize(
            adb_closeSessionResponse_t* _closeSessionResponse,
            const axutil_env_t *env,
            axiom_node_t* closeSessionResponse_om_node, axiom_element_t *closeSessionResponse_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                      
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_closeSessionResponse_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                                 
    

