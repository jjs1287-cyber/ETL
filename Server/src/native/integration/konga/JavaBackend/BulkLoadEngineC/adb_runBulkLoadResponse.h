

        #ifndef ADB_RUNBULKLOADRESPONSE_H
        #define ADB_RUNBULKLOADRESPONSE_H

          
                                   
         
                                                
                                                                                         
          

          
                                        
          
        typedef struct adb_runBulkLoadResponse adb_runBulkLoadResponse_t;

        
          #include "adb_WsBulkLoadResult.h"
          

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
        

                                                                                                     

           
                                                             
                                                   
                                                                 
           
        adb_runBulkLoadResponse_t* AXIS2_CALL
        adb_runBulkLoadResponse_create(
            const axutil_env_t *env );

           
                                                
                                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoadResponse_free (
            adb_runBulkLoadResponse_t* _runBulkLoadResponse,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                         
                                                                        
                                                   
                                          
           
        adb_WsBulkLoadResult_t* AXIS2_CALL
        adb_runBulkLoadResponse_get_runBulkLoadReturn(
            adb_runBulkLoadResponse_t* _runBulkLoadResponse,
            const axutil_env_t *env);

           
                                        
                                                                        
                                                   
                                                               
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoadResponse_set_runBulkLoadReturn(
            adb_runBulkLoadResponse_t* _runBulkLoadResponse,
            const axutil_env_t *env,
            adb_WsBulkLoadResult_t*  arg_runBulkLoadReturn);

           
                                         
                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoadResponse_reset_runBulkLoadReturn(
            adb_runBulkLoadResponse_t* _runBulkLoadResponse,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                                  
                                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_runBulkLoadResponse_is_runBulkLoadReturn_nil(
                adb_runBulkLoadResponse_t* _runBulkLoadResponse,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                        
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_runBulkLoadResponse_deserialize(
            adb_runBulkLoadResponse_t* _runBulkLoadResponse,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                        
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_runBulkLoadResponse_declare_parent_namespaces(
                    adb_runBulkLoadResponse_t* _runBulkLoadResponse,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                        
                                                   
                                                                    
                                                                                 
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_runBulkLoadResponse_serialize(
            adb_runBulkLoadResponse_t* _runBulkLoadResponse,
            const axutil_env_t *env,
            axiom_node_t* runBulkLoadResponse_om_node, axiom_element_t *runBulkLoadResponse_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                     
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_runBulkLoadResponse_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                                
    

