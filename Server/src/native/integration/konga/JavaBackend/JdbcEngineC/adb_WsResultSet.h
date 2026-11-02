

        #ifndef ADB_WSRESULTSET_H
        #define ADB_WSRESULTSET_H

          
                           
         
                                                
                                                                                         
          

          
                                
          
        typedef struct adb_WsResultSet adb_WsResultSet_t;

        
          #include "adb_ArrayOfWsResultSetRow.h"
          

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
        

                                                                                                     

           
                                                     
                                                   
                                                         
           
        adb_WsResultSet_t* AXIS2_CALL
        adb_WsResultSet_create(
            const axutil_env_t *env );

           
                                        
                                                                
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsResultSet_free (
            adb_WsResultSet_t* _WsResultSet,
            const axutil_env_t *env);



                                                                                                       
        
        

           
                            
                                                        
                                                   
                                               
           
        adb_ArrayOfWsResultSetRow_t* AXIS2_CALL
        adb_WsResultSet_get_rows(
            adb_WsResultSet_t* _WsResultSet,
            const axutil_env_t *env);

           
                           
                                                        
                                                   
                                                       
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsResultSet_set_rows(
            adb_WsResultSet_t* _WsResultSet,
            const axutil_env_t *env,
            adb_ArrayOfWsResultSetRow_t*  arg_rows);

           
                            
                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsResultSet_reset_rows(
            adb_WsResultSet_t* _WsResultSet,
            const axutil_env_t *env);

        


                                                                                                           
        

           
                                                                  
           

        

           
                                     
                                                        
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_WsResultSet_is_rows_nil(
                adb_WsResultSet_t* _WsResultSet,
                const axutil_env_t *env);


        
           
                                                         
                                                        
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsResultSet_set_rows_nil(
                adb_WsResultSet_t* _WsResultSet,
                const axutil_env_t *env);
        

                                                                                                      
                                                                                                      

        
           
                                            
                                                        
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_WsResultSet_deserialize(
            adb_WsResultSet_t* _WsResultSet,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                        
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_WsResultSet_declare_parent_namespaces(
                    adb_WsResultSet_t* _WsResultSet,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                        
                                                   
                                                            
                                                                         
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_WsResultSet_serialize(
            adb_WsResultSet_t* _WsResultSet,
            const axutil_env_t *env,
            axiom_node_t* WsResultSet_om_node, axiom_element_t *WsResultSet_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                             
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_WsResultSet_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                        
    

