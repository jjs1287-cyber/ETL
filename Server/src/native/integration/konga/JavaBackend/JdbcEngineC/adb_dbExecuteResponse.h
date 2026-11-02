

        #ifndef ADB_DBEXECUTERESPONSE_H
        #define ADB_DBEXECUTERESPONSE_H

          
                                 
         
                                                
                                                                                         
          

          
                                      
          
        typedef struct adb_dbExecuteResponse adb_dbExecuteResponse_t;

        
          #include "adb_WsDbExecuteResult.h"
          

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
        

                                                                                                     

           
                                                           
                                                   
                                                               
           
        adb_dbExecuteResponse_t* AXIS2_CALL
        adb_dbExecuteResponse_create(
            const axutil_env_t *env );

           
                                              
                                                                            
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_dbExecuteResponse_free (
            adb_dbExecuteResponse_t* _dbExecuteResponse,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                       
                                                                    
                                                   
                                           
           
        adb_WsDbExecuteResult_t* AXIS2_CALL
        adb_dbExecuteResponse_get_dbExecuteReturn(
            adb_dbExecuteResponse_t* _dbExecuteResponse,
            const axutil_env_t *env);

           
                                      
                                                                    
                                                   
                                                              
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_dbExecuteResponse_set_dbExecuteReturn(
            adb_dbExecuteResponse_t* _dbExecuteResponse,
            const axutil_env_t *env,
            adb_WsDbExecuteResult_t*  arg_dbExecuteReturn);

           
                                       
                                                                    
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_dbExecuteResponse_reset_dbExecuteReturn(
            adb_dbExecuteResponse_t* _dbExecuteResponse,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                                
                                                                    
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_dbExecuteResponse_is_dbExecuteReturn_nil(
                adb_dbExecuteResponse_t* _dbExecuteResponse,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                    
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_dbExecuteResponse_deserialize(
            adb_dbExecuteResponse_t* _dbExecuteResponse,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                    
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_dbExecuteResponse_declare_parent_namespaces(
                    adb_dbExecuteResponse_t* _dbExecuteResponse,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                    
                                                   
                                                                  
                                                                               
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_dbExecuteResponse_serialize(
            adb_dbExecuteResponse_t* _dbExecuteResponse,
            const axutil_env_t *env,
            axiom_node_t* dbExecuteResponse_om_node, axiom_element_t *dbExecuteResponse_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                   
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_dbExecuteResponse_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                              
    

