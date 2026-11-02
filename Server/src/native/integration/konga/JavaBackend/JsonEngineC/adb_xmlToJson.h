

        #ifndef ADB_XMLTOJSON_H
        #define ADB_XMLTOJSON_H

          
                         
         
                                                
                                                                                         
          

          
                              
          
        typedef struct adb_xmlToJson adb_xmlToJson_t;

        
          #include "adb_WsJsonSessionId.h"
          
          #include "adb_WsConversionParams.h"
          

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
        

                                                                                                     

           
                                                   
                                                   
                                                       
           
        adb_xmlToJson_t* AXIS2_CALL
        adb_xmlToJson_create(
            const axutil_env_t *env );

           
                                      
                                                            
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_xmlToJson_free (
            adb_xmlToJson_t* _xmlToJson,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                 
                                                    
                                                   
                                         
           
        adb_WsJsonSessionId_t* AXIS2_CALL
        adb_xmlToJson_get_SessionId(
            adb_xmlToJson_t* _xmlToJson,
            const axutil_env_t *env);

           
                                
                                                    
                                                   
                                                      
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_xmlToJson_set_SessionId(
            adb_xmlToJson_t* _xmlToJson,
            const axutil_env_t *env,
            adb_WsJsonSessionId_t*  arg_SessionId);

           
                                 
                                                    
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_xmlToJson_reset_SessionId(
            adb_xmlToJson_t* _xmlToJson,
            const axutil_env_t *env);

        
        

           
                                        
                                                    
                                                   
                                            
           
        adb_WsConversionParams_t* AXIS2_CALL
        adb_xmlToJson_get_ConversionParams(
            adb_xmlToJson_t* _xmlToJson,
            const axutil_env_t *env);

           
                                       
                                                    
                                                   
                                                                
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_xmlToJson_set_ConversionParams(
            adb_xmlToJson_t* _xmlToJson,
            const axutil_env_t *env,
            adb_WsConversionParams_t*  arg_ConversionParams);

           
                                        
                                                    
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_xmlToJson_reset_ConversionParams(
            adb_xmlToJson_t* _xmlToJson,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                          
                                                    
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_xmlToJson_is_SessionId_nil(
                adb_xmlToJson_t* _xmlToJson,
                const axutil_env_t *env);


        

           
                                                 
                                                    
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_xmlToJson_is_ConversionParams_nil(
                adb_xmlToJson_t* _xmlToJson,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                    
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_xmlToJson_deserialize(
            adb_xmlToJson_t* _xmlToJson,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                    
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_xmlToJson_declare_parent_namespaces(
                    adb_xmlToJson_t* _xmlToJson,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                    
                                                   
                                                          
                                                                       
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_xmlToJson_serialize(
            adb_xmlToJson_t* _xmlToJson,
            const axutil_env_t *env,
            axiom_node_t* xmlToJson_om_node, axiom_element_t *xmlToJson_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                           
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_xmlToJson_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                      
    

