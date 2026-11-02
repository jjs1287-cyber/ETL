

        #ifndef ADB_CLOSEBULKLOADSESSIONRESPONSE_H
        #define ADB_CLOSEBULKLOADSESSIONRESPONSE_H

          
                                            
         
                                                
                                                                                         
          

          
                                                 
          
        typedef struct adb_closeBulkLoadSessionResponse adb_closeBulkLoadSessionResponse_t;

        
          #include "adb_WsBulkLoadError.h"
          

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
        

                                                                                                     

           
                                                                      
                                                   
                                                                          
           
        adb_closeBulkLoadSessionResponse_t* AXIS2_CALL
        adb_closeBulkLoadSessionResponse_create(
            const axutil_env_t *env );

           
                                                         
                                                                                                  
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeBulkLoadSessionResponse_free (
            adb_closeBulkLoadSessionResponse_t* _closeBulkLoadSessionResponse,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                                  
                                                                                          
                                                   
                                         
           
        adb_WsBulkLoadError_t* AXIS2_CALL
        adb_closeBulkLoadSessionResponse_get_closeBulkLoadSessionReturn(
            adb_closeBulkLoadSessionResponse_t* _closeBulkLoadSessionResponse,
            const axutil_env_t *env);

           
                                                 
                                                                                          
                                                   
                                                                       
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeBulkLoadSessionResponse_set_closeBulkLoadSessionReturn(
            adb_closeBulkLoadSessionResponse_t* _closeBulkLoadSessionResponse,
            const axutil_env_t *env,
            adb_WsBulkLoadError_t*  arg_closeBulkLoadSessionReturn);

           
                                                  
                                                                                          
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeBulkLoadSessionResponse_reset_closeBulkLoadSessionReturn(
            adb_closeBulkLoadSessionResponse_t* _closeBulkLoadSessionResponse,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                                           
                                                                                          
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_closeBulkLoadSessionResponse_is_closeBulkLoadSessionReturn_nil(
                adb_closeBulkLoadSessionResponse_t* _closeBulkLoadSessionResponse,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                                          
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_closeBulkLoadSessionResponse_deserialize(
            adb_closeBulkLoadSessionResponse_t* _closeBulkLoadSessionResponse,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                                          
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_closeBulkLoadSessionResponse_declare_parent_namespaces(
                    adb_closeBulkLoadSessionResponse_t* _closeBulkLoadSessionResponse,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                                          
                                                   
                                                                             
                                                                                          
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_closeBulkLoadSessionResponse_serialize(
            adb_closeBulkLoadSessionResponse_t* _closeBulkLoadSessionResponse,
            const axutil_env_t *env,
            axiom_node_t* closeBulkLoadSessionResponse_om_node, axiom_element_t *closeBulkLoadSessionResponse_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                              
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_closeBulkLoadSessionResponse_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                                         
    

