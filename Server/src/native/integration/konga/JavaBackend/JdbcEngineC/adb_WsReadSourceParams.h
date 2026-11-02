

        #ifndef ADB_WSREADSOURCEPARAMS_H
        #define ADB_WSREADSOURCEPARAMS_H

          
                                  
         
                                                
                                                                                         
          

          
                                       
          
        typedef struct adb_WsReadSourceParams adb_WsReadSourceParams_t;

        
          #include "adb_WsConnectionParams.h"
          
          #include "adb_ArrayOfWsKeyValue.h"
          

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
        

                                                                                                     

           
                                                            
                                                   
                                                                
           
        adb_WsReadSourceParams_t* AXIS2_CALL
        adb_WsReadSourceParams_create(
            const axutil_env_t *env );

           
                                               
                                                                              
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_free (
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                  
                                                                      
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsReadSourceParams_get_sourceGuid(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env);

           
                                 
                                                                      
                                                   
                                              
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_set_sourceGuid(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_sourceGuid);

           
                                  
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_reset_sourceGuid(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env);

        
        

           
                                        
                                                                      
                                                   
                                            
           
        adb_WsConnectionParams_t* AXIS2_CALL
        adb_WsReadSourceParams_get_connectionParams(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env);

           
                                       
                                                                      
                                                   
                                                                
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_set_connectionParams(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env,
            adb_WsConnectionParams_t*  arg_connectionParams);

           
                                        
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_reset_connectionParams(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env);

        
        

           
                                   
                                                                      
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsReadSourceParams_get_jtrFileName(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env);

           
                                  
                                                                      
                                                   
                                               
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_set_jtrFileName(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_jtrFileName);

           
                                   
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_reset_jtrFileName(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env);

        
        

           
                                    
                                                                      
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsReadSourceParams_get_dataFileName(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env);

           
                                   
                                                                      
                                                   
                                                
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_set_dataFileName(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_dataFileName);

           
                                    
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_reset_dataFileName(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env);

        
        

           
                                         
                                                                      
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsReadSourceParams_get_structureFileName(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env);

           
                                        
                                                                      
                                                   
                                                     
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_set_structureFileName(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_structureFileName);

           
                                         
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_reset_structureFileName(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env);

        
        

           
                                   
                                                                      
                                                   
                                
           
        axis2_char_t* AXIS2_CALL
        adb_WsReadSourceParams_get_adHocFilter(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env);

           
                                  
                                                                      
                                                   
                                               
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_set_adHocFilter(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env,
            const axis2_char_t*  arg_adHocFilter);

           
                                   
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_reset_adHocFilter(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env);

        
        

           
                                 
                                                                      
                                                   
                      
           
        int AXIS2_CALL
        adb_WsReadSourceParams_get_fetchSize(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env);

           
                                
                                                                      
                                                   
                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_set_fetchSize(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env,
            const int  arg_fetchSize);

           
                                 
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_reset_fetchSize(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env);

        
        

           
                                 
                                                                      
                                                   
                                           
           
        adb_ArrayOfWsKeyValue_t* AXIS2_CALL
        adb_WsReadSourceParams_get_keyValues(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env);

           
                                
                                                                      
                                                   
                                                        
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_set_keyValues(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env,
            adb_ArrayOfWsKeyValue_t*  arg_keyValues);

           
                                 
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_reset_keyValues(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                           
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsReadSourceParams_is_sourceGuid_nil(
                adb_WsReadSourceParams_t* _WsReadSourceParams,
                const axutil_env_t *env);


        
           
                                                               
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_set_sourceGuid_nil(
                adb_WsReadSourceParams_t* _WsReadSourceParams,
                const axutil_env_t *env);
        

           
                                                 
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsReadSourceParams_is_connectionParams_nil(
                adb_WsReadSourceParams_t* _WsReadSourceParams,
                const axutil_env_t *env);


        
           
                                                                     
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_set_connectionParams_nil(
                adb_WsReadSourceParams_t* _WsReadSourceParams,
                const axutil_env_t *env);
        

           
                                            
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsReadSourceParams_is_jtrFileName_nil(
                adb_WsReadSourceParams_t* _WsReadSourceParams,
                const axutil_env_t *env);


        
           
                                                                
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_set_jtrFileName_nil(
                adb_WsReadSourceParams_t* _WsReadSourceParams,
                const axutil_env_t *env);
        

           
                                             
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsReadSourceParams_is_dataFileName_nil(
                adb_WsReadSourceParams_t* _WsReadSourceParams,
                const axutil_env_t *env);


        
           
                                                                 
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_set_dataFileName_nil(
                adb_WsReadSourceParams_t* _WsReadSourceParams,
                const axutil_env_t *env);
        

           
                                                  
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsReadSourceParams_is_structureFileName_nil(
                adb_WsReadSourceParams_t* _WsReadSourceParams,
                const axutil_env_t *env);


        
           
                                                                      
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_set_structureFileName_nil(
                adb_WsReadSourceParams_t* _WsReadSourceParams,
                const axutil_env_t *env);
        

           
                                            
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsReadSourceParams_is_adHocFilter_nil(
                adb_WsReadSourceParams_t* _WsReadSourceParams,
                const axutil_env_t *env);


        
           
                                                                
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_set_adHocFilter_nil(
                adb_WsReadSourceParams_t* _WsReadSourceParams,
                const axutil_env_t *env);
        

           
                                          
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsReadSourceParams_is_fetchSize_nil(
                adb_WsReadSourceParams_t* _WsReadSourceParams,
                const axutil_env_t *env);


        

           
                                          
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsReadSourceParams_is_keyValues_nil(
                adb_WsReadSourceParams_t* _WsReadSourceParams,
                const axutil_env_t *env);


        
           
                                                              
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_set_keyValues_nil(
                adb_WsReadSourceParams_t* _WsReadSourceParams,
                const axutil_env_t *env);
        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                      
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsReadSourceParams_deserialize(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                      
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsReadSourceParams_declare_parent_namespaces(
                    adb_WsReadSourceParams_t* _WsReadSourceParams,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                      
                                                   
                                                                   
                                                                                
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsReadSourceParams_serialize(
            adb_WsReadSourceParams_t* _WsReadSourceParams,
            const axutil_env_t *env,
            axiom_node_t* WsReadSourceParams_om_node, axiom_element_t *WsReadSourceParams_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                    
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsReadSourceParams_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                               
    

