

        #ifndef ADB_STARTNEWBULKLOADSESSIONRESPONSE_H
        #define ADB_STARTNEWBULKLOADSESSIONRESPONSE_H

          
                                               
         
                                                
                                                                                         
          

          
                                                    
          
        typedef struct adb_startNewBulkLoadSessionResponse adb_startNewBulkLoadSessionResponse_t;

        
          #include "adb_WsStartNewBulkLoadSessionResult.h"
          

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
        

                                                                                                     

           
                                                                         
                                                   
                                                                             
           
        adb_startNewBulkLoadSessionResponse_t* AXIS2_CALL
        adb_startNewBulkLoadSessionResponse_create(
            const axutil_env_t *env );

           
                                                            
                                                                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewBulkLoadSessionResponse_free (
            adb_startNewBulkLoadSessionResponse_t* _startNewBulkLoadSessionResponse,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                                     
                                                                                                
                                                   
                                                         
           
        adb_WsStartNewBulkLoadSessionResult_t* AXIS2_CALL
        adb_startNewBulkLoadSessionResponse_get_startNewBulkLoadSessionReturn(
            adb_startNewBulkLoadSessionResponse_t* _startNewBulkLoadSessionResponse,
            const axutil_env_t *env);

           
                                                    
                                                                                                
                                                   
                                                                                          
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewBulkLoadSessionResponse_set_startNewBulkLoadSessionReturn(
            adb_startNewBulkLoadSessionResponse_t* _startNewBulkLoadSessionResponse,
            const axutil_env_t *env,
            adb_WsStartNewBulkLoadSessionResult_t*  arg_startNewBulkLoadSessionReturn);

           
                                                     
                                                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewBulkLoadSessionResponse_reset_startNewBulkLoadSessionReturn(
            adb_startNewBulkLoadSessionResponse_t* _startNewBulkLoadSessionResponse,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                                              
                                                                                                
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_startNewBulkLoadSessionResponse_is_startNewBulkLoadSessionReturn_nil(
                adb_startNewBulkLoadSessionResponse_t* _startNewBulkLoadSessionResponse,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                                                
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_startNewBulkLoadSessionResponse_deserialize(
            adb_startNewBulkLoadSessionResponse_t* _startNewBulkLoadSessionResponse,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                                                
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_startNewBulkLoadSessionResponse_declare_parent_namespaces(
                    adb_startNewBulkLoadSessionResponse_t* _startNewBulkLoadSessionResponse,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                                                
                                                   
                                                                                
                                                                                             
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_startNewBulkLoadSessionResponse_serialize(
            adb_startNewBulkLoadSessionResponse_t* _startNewBulkLoadSessionResponse,
            const axutil_env_t *env,
            axiom_node_t* startNewBulkLoadSessionResponse_om_node, axiom_element_t *startNewBulkLoadSessionResponse_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                                 
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_startNewBulkLoadSessionResponse_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                                            
    

