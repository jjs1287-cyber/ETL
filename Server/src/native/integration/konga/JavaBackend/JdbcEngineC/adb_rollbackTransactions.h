

        #ifndef ADB_ROLLBACKTRANSACTIONS_H
        #define ADB_ROLLBACKTRANSACTIONS_H

          
                                    
         
                                                
                                                                                         
          

          
                                         
          
        typedef struct adb_rollbackTransactions adb_rollbackTransactions_t;

        
          #include "adb_WsJdbcSessionId.h"
          

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
        

                                                                                                     

           
                                                              
                                                   
                                                                  
           
        adb_rollbackTransactions_t* AXIS2_CALL
        adb_rollbackTransactions_create(
            const axutil_env_t *env );

           
                                                 
                                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_rollbackTransactions_free (
            adb_rollbackTransactions_t* _rollbackTransactions,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                           
                                                                          
                                                   
                                         
           
        adb_WsJdbcSessionId_t* AXIS2_CALL
        adb_rollbackTransactions_get_in0(
            adb_rollbackTransactions_t* _rollbackTransactions,
            const axutil_env_t *env);

           
                          
                                                                          
                                                   
                                                
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_rollbackTransactions_set_in0(
            adb_rollbackTransactions_t* _rollbackTransactions,
            const axutil_env_t *env,
            adb_WsJdbcSessionId_t*  arg_in0);

           
                           
                                                                          
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_rollbackTransactions_reset_in0(
            adb_rollbackTransactions_t* _rollbackTransactions,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                    
                                                                          
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_rollbackTransactions_is_in0_nil(
                adb_rollbackTransactions_t* _rollbackTransactions,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                          
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_rollbackTransactions_deserialize(
            adb_rollbackTransactions_t* _rollbackTransactions,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                          
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_rollbackTransactions_declare_parent_namespaces(
                    adb_rollbackTransactions_t* _rollbackTransactions,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                          
                                                   
                                                                     
                                                                                  
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_rollbackTransactions_serialize(
            adb_rollbackTransactions_t* _rollbackTransactions,
            const axutil_env_t *env,
            axiom_node_t* rollbackTransactions_om_node, axiom_element_t *rollbackTransactions_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                      
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_rollbackTransactions_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                                 
    

