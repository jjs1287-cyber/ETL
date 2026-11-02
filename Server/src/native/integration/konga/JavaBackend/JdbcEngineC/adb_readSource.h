

        #ifndef ADB_READSOURCE_H
        #define ADB_READSOURCE_H

          
                          
         
                                                
                                                                                         
          

          
                               
          
        typedef struct adb_readSource adb_readSource_t;

        
          #include "adb_WsJdbcSessionId.h"
          
          #include "adb_WsReadSourceParams.h"
          

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
        

                                                                                                     

           
                                                    
                                                   
                                                        
           
        adb_readSource_t* AXIS2_CALL
        adb_readSource_create(
            const axutil_env_t *env );

           
                                       
                                                              
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_readSource_free (
            adb_readSource_t* _readSource,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                           
                                                      
                                                   
                                         
           
        adb_WsJdbcSessionId_t* AXIS2_CALL
        adb_readSource_get_in0(
            adb_readSource_t* _readSource,
            const axutil_env_t *env);

           
                          
                                                      
                                                   
                                                
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_readSource_set_in0(
            adb_readSource_t* _readSource,
            const axutil_env_t *env,
            adb_WsJdbcSessionId_t*  arg_in0);

           
                           
                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_readSource_reset_in0(
            adb_readSource_t* _readSource,
            const axutil_env_t *env);

        
        

           
                           
                                                      
                                                   
                                            
           
        adb_WsReadSourceParams_t* AXIS2_CALL
        adb_readSource_get_in1(
            adb_readSource_t* _readSource,
            const axutil_env_t *env);

           
                          
                                                      
                                                   
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_readSource_set_in1(
            adb_readSource_t* _readSource,
            const axutil_env_t *env,
            adb_WsReadSourceParams_t*  arg_in1);

           
                           
                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_readSource_reset_in1(
            adb_readSource_t* _readSource,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                    
                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_readSource_is_in0_nil(
                adb_readSource_t* _readSource,
                const axutil_env_t *env);


        

           
                                    
                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_readSource_is_in1_nil(
                adb_readSource_t* _readSource,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                      
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_readSource_deserialize(
            adb_readSource_t* _readSource,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                      
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_readSource_declare_parent_namespaces(
                    adb_readSource_t* _readSource,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                      
                                                   
                                                           
                                                                        
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_readSource_serialize(
            adb_readSource_t* _readSource,
            const axutil_env_t *env,
            axiom_node_t* readSource_om_node, axiom_element_t *readSource_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                            
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_readSource_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                       
    

