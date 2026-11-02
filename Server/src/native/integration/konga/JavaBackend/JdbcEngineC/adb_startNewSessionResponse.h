

        #ifndef ADB_STARTNEWSESSIONRESPONSE_H
        #define ADB_STARTNEWSESSIONRESPONSE_H

          
                                       
         
                                                
                                                                                         
          

          
                                            
          
        typedef struct adb_startNewSessionResponse adb_startNewSessionResponse_t;

        
          #include "adb_WsJdbcStartNewSessionResult.h"
          

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
        

                                                                                                     

           
                                                                 
                                                   
                                                                     
           
        adb_startNewSessionResponse_t* AXIS2_CALL
        adb_startNewSessionResponse_create(
            const axutil_env_t *env );

           
                                                    
                                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewSessionResponse_free (
            adb_startNewSessionResponse_t* _startNewSessionResponse,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                             
                                                                                
                                                   
                                                     
           
        adb_WsJdbcStartNewSessionResult_t* AXIS2_CALL
        adb_startNewSessionResponse_get_startNewSessionReturn(
            adb_startNewSessionResponse_t* _startNewSessionResponse,
            const axutil_env_t *env);

           
                                            
                                                                                
                                                   
                                                                              
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewSessionResponse_set_startNewSessionReturn(
            adb_startNewSessionResponse_t* _startNewSessionResponse,
            const axutil_env_t *env,
            adb_WsJdbcStartNewSessionResult_t*  arg_startNewSessionReturn);

           
                                             
                                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewSessionResponse_reset_startNewSessionReturn(
            adb_startNewSessionResponse_t* _startNewSessionResponse,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                                      
                                                                                
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_startNewSessionResponse_is_startNewSessionReturn_nil(
                adb_startNewSessionResponse_t* _startNewSessionResponse,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                                
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewSessionResponse_deserialize(
            adb_startNewSessionResponse_t* _startNewSessionResponse,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                                
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_startNewSessionResponse_declare_parent_namespaces(
                    adb_startNewSessionResponse_t* _startNewSessionResponse,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                                
                                                   
                                                                        
                                                                                     
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_startNewSessionResponse_serialize(
            adb_startNewSessionResponse_t* _startNewSessionResponse,
            const axutil_env_t *env,
            axiom_node_t* startNewSessionResponse_om_node, axiom_element_t *startNewSessionResponse_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                         
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_startNewSessionResponse_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                                    
    

