

        #ifndef ADB_COMMITTRANSACTIONS_H
        #define ADB_COMMITTRANSACTIONS_H

          
                                  
         
                                                
                                                                                         
          

          
                                       
          
        typedef struct adb_commitTransactions adb_commitTransactions_t;

        
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
        

                                                                                                     

           
                                                            
                                                   
                                                                
           
        adb_commitTransactions_t* AXIS2_CALL
        adb_commitTransactions_create(
            const axutil_env_t *env );

           
                                               
                                                                              
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_commitTransactions_free (
            adb_commitTransactions_t* _commitTransactions,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                           
                                                                      
                                                   
                                         
           
        adb_WsJdbcSessionId_t* AXIS2_CALL
        adb_commitTransactions_get_in0(
            adb_commitTransactions_t* _commitTransactions,
            const axutil_env_t *env);

           
                          
                                                                      
                                                   
                                                
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_commitTransactions_set_in0(
            adb_commitTransactions_t* _commitTransactions,
            const axutil_env_t *env,
            adb_WsJdbcSessionId_t*  arg_in0);

           
                           
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_commitTransactions_reset_in0(
            adb_commitTransactions_t* _commitTransactions,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                    
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_commitTransactions_is_in0_nil(
                adb_commitTransactions_t* _commitTransactions,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                      
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_commitTransactions_deserialize(
            adb_commitTransactions_t* _commitTransactions,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                      
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_commitTransactions_declare_parent_namespaces(
                    adb_commitTransactions_t* _commitTransactions,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                      
                                                   
                                                                   
                                                                                
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_commitTransactions_serialize(
            adb_commitTransactions_t* _commitTransactions,
            const axutil_env_t *env,
            axiom_node_t* commitTransactions_om_node, axiom_element_t *commitTransactions_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                    
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_commitTransactions_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                               
    

