

        #ifndef ADB_STARTNEWJSONSESSIONRESPONSE_H
        #define ADB_STARTNEWJSONSESSIONRESPONSE_H

          
                                           
         
                                                
                                                                                         
          

          
                                                
          
        typedef struct adb_startNewJsonSessionResponse adb_startNewJsonSessionResponse_t;

        
          #include "adb_WsStartNewJsonSessionResult.h"
          

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
        

                                                                                                     

           
                                                                     
                                                   
                                                                         
           
        adb_startNewJsonSessionResponse_t* AXIS2_CALL
        adb_startNewJsonSessionResponse_create(
            const axutil_env_t *env );

           
                                                        
                                                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewJsonSessionResponse_free (
            adb_startNewJsonSessionResponse_t* _startNewJsonSessionResponse,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                                 
                                                                                        
                                                   
                                                     
           
        adb_WsStartNewJsonSessionResult_t* AXIS2_CALL
        adb_startNewJsonSessionResponse_get_startNewJsonSessionReturn(
            adb_startNewJsonSessionResponse_t* _startNewJsonSessionResponse,
            const axutil_env_t *env);

           
                                                
                                                                                        
                                                   
                                                                                  
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewJsonSessionResponse_set_startNewJsonSessionReturn(
            adb_startNewJsonSessionResponse_t* _startNewJsonSessionResponse,
            const axutil_env_t *env,
            adb_WsStartNewJsonSessionResult_t*  arg_startNewJsonSessionReturn);

           
                                                 
                                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewJsonSessionResponse_reset_startNewJsonSessionReturn(
            adb_startNewJsonSessionResponse_t* _startNewJsonSessionResponse,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                                          
                                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_startNewJsonSessionResponse_is_startNewJsonSessionReturn_nil(
                adb_startNewJsonSessionResponse_t* _startNewJsonSessionResponse,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                                        
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewJsonSessionResponse_deserialize(
            adb_startNewJsonSessionResponse_t* _startNewJsonSessionResponse,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                                        
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_startNewJsonSessionResponse_declare_parent_namespaces(
                    adb_startNewJsonSessionResponse_t* _startNewJsonSessionResponse,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                                        
                                                   
                                                                            
                                                                                         
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_startNewJsonSessionResponse_serialize(
            adb_startNewJsonSessionResponse_t* _startNewJsonSessionResponse,
            const axutil_env_t *env,
            axiom_node_t* startNewJsonSessionResponse_om_node, axiom_element_t *startNewJsonSessionResponse_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                             
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_startNewJsonSessionResponse_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                                        
    

