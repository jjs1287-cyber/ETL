

        #ifndef ADB_JSONTOXMLRESPONSE_H
        #define ADB_JSONTOXMLRESPONSE_H

          
                                 
         
                                                
                                                                                         
          

          
                                      
          
        typedef struct adb_jsonToXmlResponse adb_jsonToXmlResponse_t;

        
          #include "adb_WsJsonError.h"
          

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
        

                                                                                                     

           
                                                           
                                                   
                                                               
           
        adb_jsonToXmlResponse_t* AXIS2_CALL
        adb_jsonToXmlResponse_create(
            const axutil_env_t *env );

           
                                              
                                                                            
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_jsonToXmlResponse_free (
            adb_jsonToXmlResponse_t* _jsonToXmlResponse,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                       
                                                                    
                                                   
                                     
           
        adb_WsJsonError_t* AXIS2_CALL
        adb_jsonToXmlResponse_get_jsonToXmlReturn(
            adb_jsonToXmlResponse_t* _jsonToXmlResponse,
            const axutil_env_t *env);

           
                                      
                                                                    
                                                   
                                                        
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_jsonToXmlResponse_set_jsonToXmlReturn(
            adb_jsonToXmlResponse_t* _jsonToXmlResponse,
            const axutil_env_t *env,
            adb_WsJsonError_t*  arg_jsonToXmlReturn);

           
                                       
                                                                    
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_jsonToXmlResponse_reset_jsonToXmlReturn(
            adb_jsonToXmlResponse_t* _jsonToXmlResponse,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                                
                                                                    
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_jsonToXmlResponse_is_jsonToXmlReturn_nil(
                adb_jsonToXmlResponse_t* _jsonToXmlResponse,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                    
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_jsonToXmlResponse_deserialize(
            adb_jsonToXmlResponse_t* _jsonToXmlResponse,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                    
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_jsonToXmlResponse_declare_parent_namespaces(
                    adb_jsonToXmlResponse_t* _jsonToXmlResponse,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                    
                                                   
                                                                  
                                                                               
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_jsonToXmlResponse_serialize(
            adb_jsonToXmlResponse_t* _jsonToXmlResponse,
            const axutil_env_t *env,
            axiom_node_t* jsonToXmlResponse_om_node, axiom_element_t *jsonToXmlResponse_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                   
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_jsonToXmlResponse_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                              
    

