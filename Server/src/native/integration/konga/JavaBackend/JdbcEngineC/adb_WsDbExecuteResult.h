

        #ifndef ADB_WSDBEXECUTERESULT_H
        #define ADB_WSDBEXECUTERESULT_H

          
                                 
         
                                                
                                                                                         
          

          
                                      
          
        typedef struct adb_WsDbExecuteResult adb_WsDbExecuteResult_t;

        
          #include "adb_WsJdbcError.h"
          
          #include "adb_WsResultSet.h"
          

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
        

                                                                                                     

           
                                                           
                                                   
                                                               
           
        adb_WsDbExecuteResult_t* AXIS2_CALL
        adb_WsDbExecuteResult_create(
            const axutil_env_t *env );

           
                                              
                                                                            
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbExecuteResult_free (
            adb_WsDbExecuteResult_t* _WsDbExecuteResult,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                   
                                                                    
                                                   
                                     
           
        adb_WsJdbcError_t* AXIS2_CALL
        adb_WsDbExecuteResult_get_returnValue(
            adb_WsDbExecuteResult_t* _WsDbExecuteResult,
            const axutil_env_t *env);

           
                                  
                                                                    
                                                   
                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbExecuteResult_set_returnValue(
            adb_WsDbExecuteResult_t* _WsDbExecuteResult,
            const axutil_env_t *env,
            adb_WsJdbcError_t*  arg_returnValue);

           
                                   
                                                                    
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbExecuteResult_reset_returnValue(
            adb_WsDbExecuteResult_t* _WsDbExecuteResult,
            const axutil_env_t *env);

        
        

           
                                 
                                                                    
                                                   
                                     
           
        adb_WsResultSet_t* AXIS2_CALL
        adb_WsDbExecuteResult_get_resultSet(
            adb_WsDbExecuteResult_t* _WsDbExecuteResult,
            const axutil_env_t *env);

           
                                
                                                                    
                                                   
                                                  
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbExecuteResult_set_resultSet(
            adb_WsDbExecuteResult_t* _WsDbExecuteResult,
            const axutil_env_t *env,
            adb_WsResultSet_t*  arg_resultSet);

           
                                 
                                                                    
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbExecuteResult_reset_resultSet(
            adb_WsDbExecuteResult_t* _WsDbExecuteResult,
            const axutil_env_t *env);

        
        

           
                                   
                                                                    
                                                   
                      
           
        int AXIS2_CALL
        adb_WsDbExecuteResult_get_updateCount(
            adb_WsDbExecuteResult_t* _WsDbExecuteResult,
            const axutil_env_t *env);

           
                                  
                                                                    
                                                   
                                     
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbExecuteResult_set_updateCount(
            adb_WsDbExecuteResult_t* _WsDbExecuteResult,
            const axutil_env_t *env,
            const int  arg_updateCount);

           
                                   
                                                                    
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbExecuteResult_reset_updateCount(
            adb_WsDbExecuteResult_t* _WsDbExecuteResult,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                            
                                                                    
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsDbExecuteResult_is_returnValue_nil(
                adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                const axutil_env_t *env);


        
           
                                                                
                                                                    
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbExecuteResult_set_returnValue_nil(
                adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                const axutil_env_t *env);
        

           
                                          
                                                                    
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsDbExecuteResult_is_resultSet_nil(
                adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                const axutil_env_t *env);


        
           
                                                              
                                                                    
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbExecuteResult_set_resultSet_nil(
                adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                const axutil_env_t *env);
        

           
                                            
                                                                    
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsDbExecuteResult_is_updateCount_nil(
                adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                    
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsDbExecuteResult_deserialize(
            adb_WsDbExecuteResult_t* _WsDbExecuteResult,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                    
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsDbExecuteResult_declare_parent_namespaces(
                    adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                    
                                                   
                                                                  
                                                                               
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsDbExecuteResult_serialize(
            adb_WsDbExecuteResult_t* _WsDbExecuteResult,
            const axutil_env_t *env,
            axiom_node_t* WsDbExecuteResult_om_node, axiom_element_t *WsDbExecuteResult_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                   
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsDbExecuteResult_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                              
    

