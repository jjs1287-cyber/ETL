

        #ifndef ADB_DBLOOKUP_H
        #define ADB_DBLOOKUP_H

          
                        
         
                                                
                                                                                         
          

          
                             
          
        typedef struct adb_dbLookup adb_dbLookup_t;

        
          #include "adb_WsJdbcSessionId.h"
          
          #include "adb_WsDbLookupParams.h"
          

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
        

                                                                                                     

           
                                                  
                                                   
                                                      
           
        adb_dbLookup_t* AXIS2_CALL
        adb_dbLookup_create(
            const axutil_env_t *env );

           
                                     
                                                          
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_dbLookup_free (
            adb_dbLookup_t* _dbLookup,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                           
                                                  
                                                   
                                         
           
        adb_WsJdbcSessionId_t* AXIS2_CALL
        adb_dbLookup_get_in0(
            adb_dbLookup_t* _dbLookup,
            const axutil_env_t *env);

           
                          
                                                  
                                                   
                                                
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_dbLookup_set_in0(
            adb_dbLookup_t* _dbLookup,
            const axutil_env_t *env,
            adb_WsJdbcSessionId_t*  arg_in0);

           
                           
                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_dbLookup_reset_in0(
            adb_dbLookup_t* _dbLookup,
            const axutil_env_t *env);

        
        

           
                           
                                                  
                                                   
                                          
           
        adb_WsDbLookupParams_t* AXIS2_CALL
        adb_dbLookup_get_in1(
            adb_dbLookup_t* _dbLookup,
            const axutil_env_t *env);

           
                          
                                                  
                                                   
                                                 
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_dbLookup_set_in1(
            adb_dbLookup_t* _dbLookup,
            const axutil_env_t *env,
            adb_WsDbLookupParams_t*  arg_in1);

           
                           
                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_dbLookup_reset_in1(
            adb_dbLookup_t* _dbLookup,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                    
                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_dbLookup_is_in0_nil(
                adb_dbLookup_t* _dbLookup,
                const axutil_env_t *env);


        

           
                                    
                                                  
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_dbLookup_is_in1_nil(
                adb_dbLookup_t* _dbLookup,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                  
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_dbLookup_deserialize(
            adb_dbLookup_t* _dbLookup,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                  
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_dbLookup_declare_parent_namespaces(
                    adb_dbLookup_t* _dbLookup,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                  
                                                   
                                                         
                                                                      
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_dbLookup_serialize(
            adb_dbLookup_t* _dbLookup,
            const axutil_env_t *env,
            axiom_node_t* dbLookup_om_node, axiom_element_t *dbLookup_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                          
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_dbLookup_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                     
    

