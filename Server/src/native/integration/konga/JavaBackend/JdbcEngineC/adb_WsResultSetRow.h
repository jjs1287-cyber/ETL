

        #ifndef ADB_WSRESULTSETROW_H
        #define ADB_WSRESULTSETROW_H

          
                              
         
                                                
                                                                                         
          

          
                                   
          
        typedef struct adb_WsResultSetRow adb_WsResultSetRow_t;

        
          #include "adb_ArrayOf_xsd_string.h"
          

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
        

                                                                                                     

           
                                                        
                                                   
                                                            
           
        adb_WsResultSetRow_t* AXIS2_CALL
        adb_WsResultSetRow_create(
            const axutil_env_t *env );

           
                                           
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsResultSetRow_free (
            adb_WsResultSetRow_t* _WsResultSetRow,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                               
                                                              
                                                   
                                            
           
        adb_ArrayOf_xsd_string_t* AXIS2_CALL
        adb_WsResultSetRow_get_columns(
            adb_WsResultSetRow_t* _WsResultSetRow,
            const axutil_env_t *env);

           
                              
                                                              
                                                   
                                                       
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsResultSetRow_set_columns(
            adb_WsResultSetRow_t* _WsResultSetRow,
            const axutil_env_t *env,
            adb_ArrayOf_xsd_string_t*  arg_columns);

           
                               
                                                              
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsResultSetRow_reset_columns(
            adb_WsResultSetRow_t* _WsResultSetRow,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                        
                                                              
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsResultSetRow_is_columns_nil(
                adb_WsResultSetRow_t* _WsResultSetRow,
                const axutil_env_t *env);


        
           
                                                            
                                                              
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsResultSetRow_set_columns_nil(
                adb_WsResultSetRow_t* _WsResultSetRow,
                const axutil_env_t *env);
        

                                                                                                      
                                                                                                      

        
           
                                            
                                                              
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsResultSetRow_deserialize(
            adb_WsResultSetRow_t* _WsResultSetRow,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                              
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsResultSetRow_declare_parent_namespaces(
                    adb_WsResultSetRow_t* _WsResultSetRow,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                              
                                                   
                                                               
                                                                            
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsResultSetRow_serialize(
            adb_WsResultSetRow_t* _WsResultSetRow,
            const axutil_env_t *env,
            axiom_node_t* WsResultSetRow_om_node, axiom_element_t *WsResultSetRow_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsResultSetRow_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                           
    

