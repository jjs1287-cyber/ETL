

        #ifndef ADB_READSOURCERESPONSE_H
        #define ADB_READSOURCERESPONSE_H

          
                                  
         
                                                
                                                                                         
          

          
                                       
          
        typedef struct adb_readSourceResponse adb_readSourceResponse_t;

        
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
        

                                                                                                     

           
                                                            
                                                   
                                                                
           
        adb_readSourceResponse_t* AXIS2_CALL
        adb_readSourceResponse_create(
            const axutil_env_t *env );

           
                                               
                                                                              
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_readSourceResponse_free (
            adb_readSourceResponse_t* _readSourceResponse,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                        
                                                                      
                                                   
                                     
           
        adb_WsJdbcError_t* AXIS2_CALL
        adb_readSourceResponse_get_readSourceReturn(
            adb_readSourceResponse_t* _readSourceResponse,
            const axutil_env_t *env);

           
                                       
                                                                      
                                                   
                                                         
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_readSourceResponse_set_readSourceReturn(
            adb_readSourceResponse_t* _readSourceResponse,
            const axutil_env_t *env,
            adb_WsJdbcError_t*  arg_readSourceReturn);

           
                                        
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_readSourceResponse_reset_readSourceReturn(
            adb_readSourceResponse_t* _readSourceResponse,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                                 
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_readSourceResponse_is_readSourceReturn_nil(
                adb_readSourceResponse_t* _readSourceResponse,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                      
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_readSourceResponse_deserialize(
            adb_readSourceResponse_t* _readSourceResponse,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                      
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_readSourceResponse_declare_parent_namespaces(
                    adb_readSourceResponse_t* _readSourceResponse,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                      
                                                   
                                                                   
                                                                                
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_readSourceResponse_serialize(
            adb_readSourceResponse_t* _readSourceResponse,
            const axutil_env_t *env,
            axiom_node_t* readSourceResponse_om_node, axiom_element_t *readSourceResponse_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                    
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_readSourceResponse_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                               
    

