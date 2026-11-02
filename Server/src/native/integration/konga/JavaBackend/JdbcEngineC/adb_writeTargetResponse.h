

        #ifndef ADB_WRITETARGETRESPONSE_H
        #define ADB_WRITETARGETRESPONSE_H

          
                                   
         
                                                
                                                                                         
          

          
                                        
          
        typedef struct adb_writeTargetResponse adb_writeTargetResponse_t;

        
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
        

                                                                                                     

           
                                                             
                                                   
                                                                 
           
        adb_writeTargetResponse_t* AXIS2_CALL
        adb_writeTargetResponse_create(
            const axutil_env_t *env );

           
                                                
                                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_writeTargetResponse_free (
            adb_writeTargetResponse_t* _writeTargetResponse,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                         
                                                                        
                                                   
                                     
           
        adb_WsJdbcError_t* AXIS2_CALL
        adb_writeTargetResponse_get_writeTargetReturn(
            adb_writeTargetResponse_t* _writeTargetResponse,
            const axutil_env_t *env);

           
                                        
                                                                        
                                                   
                                                          
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_writeTargetResponse_set_writeTargetReturn(
            adb_writeTargetResponse_t* _writeTargetResponse,
            const axutil_env_t *env,
            adb_WsJdbcError_t*  arg_writeTargetReturn);

           
                                         
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_writeTargetResponse_reset_writeTargetReturn(
            adb_writeTargetResponse_t* _writeTargetResponse,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                                  
                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_writeTargetResponse_is_writeTargetReturn_nil(
                adb_writeTargetResponse_t* _writeTargetResponse,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                        
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_writeTargetResponse_deserialize(
            adb_writeTargetResponse_t* _writeTargetResponse,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                        
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_writeTargetResponse_declare_parent_namespaces(
                    adb_writeTargetResponse_t* _writeTargetResponse,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                        
                                                   
                                                                    
                                                                                 
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_writeTargetResponse_serialize(
            adb_writeTargetResponse_t* _writeTargetResponse,
            const axutil_env_t *env,
            axiom_node_t* writeTargetResponse_om_node, axiom_element_t *writeTargetResponse_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                     
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_writeTargetResponse_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                                
    

