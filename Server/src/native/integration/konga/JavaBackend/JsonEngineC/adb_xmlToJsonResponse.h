

        #ifndef ADB_XMLTOJSONRESPONSE_H
        #define ADB_XMLTOJSONRESPONSE_H

          
                                 
         
                                                
                                                                                         
          

          
                                      
          
        typedef struct adb_xmlToJsonResponse adb_xmlToJsonResponse_t;

        
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
        

                                                                                                     

           
                                                           
                                                   
                                                               
           
        adb_xmlToJsonResponse_t* AXIS2_CALL
        adb_xmlToJsonResponse_create(
            const axutil_env_t *env );

           
                                              
                                                                            
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_xmlToJsonResponse_free (
            adb_xmlToJsonResponse_t* _xmlToJsonResponse,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                                       
                                                                    
                                                   
                                     
           
        adb_WsJsonError_t* AXIS2_CALL
        adb_xmlToJsonResponse_get_xmlToJsonReturn(
            adb_xmlToJsonResponse_t* _xmlToJsonResponse,
            const axutil_env_t *env);

           
                                      
                                                                    
                                                   
                                                        
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_xmlToJsonResponse_set_xmlToJsonReturn(
            adb_xmlToJsonResponse_t* _xmlToJsonResponse,
            const axutil_env_t *env,
            adb_WsJsonError_t*  arg_xmlToJsonReturn);

           
                                       
                                                                    
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_xmlToJsonResponse_reset_xmlToJsonReturn(
            adb_xmlToJsonResponse_t* _xmlToJsonResponse,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                                
                                                                    
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_xmlToJsonResponse_is_xmlToJsonReturn_nil(
                adb_xmlToJsonResponse_t* _xmlToJsonResponse,
                const axutil_env_t *env);


        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                    
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_xmlToJsonResponse_deserialize(
            adb_xmlToJsonResponse_t* _xmlToJsonResponse,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                    
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_xmlToJsonResponse_declare_parent_namespaces(
                    adb_xmlToJsonResponse_t* _xmlToJsonResponse,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                    
                                                   
                                                                  
                                                                               
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_xmlToJsonResponse_serialize(
            adb_xmlToJsonResponse_t* _xmlToJsonResponse,
            const axutil_env_t *env,
            axiom_node_t* xmlToJsonResponse_om_node, axiom_element_t *xmlToJsonResponse_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                   
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_xmlToJsonResponse_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                              
    

