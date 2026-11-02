

        #ifndef ADB_COMMITTRANSACTIONSRESPONSE_H
        #define ADB_COMMITTRANSACTIONSRESPONSE_H

          
                                          
         
                                                
                                                                                         
          

          
                                               
          
        typedef struct adb_commitTransactionsResponse adb_commitTransactionsResponse_t;

        
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
        

                                                                                                     

           
                                                                    
                                                   
                                                                        
           
        adb_commitTransactionsResponse_t* AXIS2_CALL
        adb_commitTransactionsResponse_create(
            const axutil_env_t *env );

           
                                                       
                                                                                              
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_commitTransactionsResponse_free (
            adb_commitTransactionsResponse_t* _commitTransactionsResponse,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                                
                                                                                      
                                                   
                                     
           
        adb_WsJdbcError_t* AXIS2_CALL
        adb_commitTransactionsResponse_get_commitTransactionsReturn(
            adb_commitTransactionsResponse_t* _commitTransactionsResponse,
            const axutil_env_t *env);

           
                                               
                                                                                      
                                                   
                                                                 
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_commitTransactionsResponse_set_commitTransactionsReturn(
            adb_commitTransactionsResponse_t* _commitTransactionsResponse,
            const axutil_env_t *env,
            adb_WsJdbcError_t*  arg_commitTransactionsReturn);

           
                                                
                                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_commitTransactionsResponse_reset_commitTransactionsReturn(
            adb_commitTransactionsResponse_t* _commitTransactionsResponse,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                                         
                                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_commitTransactionsResponse_is_commitTransactionsReturn_nil(
                adb_commitTransactionsResponse_t* _commitTransactionsResponse,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                                      
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_commitTransactionsResponse_deserialize(
            adb_commitTransactionsResponse_t* _commitTransactionsResponse,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                                      
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_commitTransactionsResponse_declare_parent_namespaces(
                    adb_commitTransactionsResponse_t* _commitTransactionsResponse,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                                      
                                                   
                                                                           
                                                                                        
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_commitTransactionsResponse_serialize(
            adb_commitTransactionsResponse_t* _commitTransactionsResponse,
            const axutil_env_t *env,
            axiom_node_t* commitTransactionsResponse_om_node, axiom_element_t *commitTransactionsResponse_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                            
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_commitTransactionsResponse_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                                       
    

