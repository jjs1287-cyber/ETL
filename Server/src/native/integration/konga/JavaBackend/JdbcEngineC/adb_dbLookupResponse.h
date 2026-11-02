

        #ifndef ADB_DBLOOKUPRESPONSE_H
        #define ADB_DBLOOKUPRESPONSE_H

          
                                
         
                                                
                                                                                         
          

          
                                     
          
        typedef struct adb_dbLookupResponse adb_dbLookupResponse_t;

        
          #include "adb_WsDbLookupResult.h"
          

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
        

                                                                                                     

           
                                                          
                                                   
                                                              
           
        adb_dbLookupResponse_t* AXIS2_CALL
        adb_dbLookupResponse_create(
            const axutil_env_t *env );

           
                                             
                                                                          
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_dbLookupResponse_free (
            adb_dbLookupResponse_t* _dbLookupResponse,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                      
                                                                  
                                                   
                                          
           
        adb_WsDbLookupResult_t* AXIS2_CALL
        adb_dbLookupResponse_get_dbLookupReturn(
            adb_dbLookupResponse_t* _dbLookupResponse,
            const axutil_env_t *env);

           
                                     
                                                                  
                                                   
                                                            
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_dbLookupResponse_set_dbLookupReturn(
            adb_dbLookupResponse_t* _dbLookupResponse,
            const axutil_env_t *env,
            adb_WsDbLookupResult_t*  arg_dbLookupReturn);

           
                                      
                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_dbLookupResponse_reset_dbLookupReturn(
            adb_dbLookupResponse_t* _dbLookupResponse,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                               
                                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_dbLookupResponse_is_dbLookupReturn_nil(
                adb_dbLookupResponse_t* _dbLookupResponse,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                  
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_dbLookupResponse_deserialize(
            adb_dbLookupResponse_t* _dbLookupResponse,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                  
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_dbLookupResponse_declare_parent_namespaces(
                    adb_dbLookupResponse_t* _dbLookupResponse,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                  
                                                   
                                                                 
                                                                              
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_dbLookupResponse_serialize(
            adb_dbLookupResponse_t* _dbLookupResponse,
            const axutil_env_t *env,
            axiom_node_t* dbLookupResponse_om_node, axiom_element_t *dbLookupResponse_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                  
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_dbLookupResponse_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                             
    

