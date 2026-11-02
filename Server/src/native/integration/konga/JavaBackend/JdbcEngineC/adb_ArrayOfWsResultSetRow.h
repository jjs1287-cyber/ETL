

        #ifndef ADB_ARRAYOFWSRESULTSETROW_H
        #define ADB_ARRAYOFWSRESULTSETROW_H

          
                                     
         
                                                
                                                                                         
          

          
                                          
          
        typedef struct adb_ArrayOfWsResultSetRow adb_ArrayOfWsResultSetRow_t;

        
          #include "adb_WsResultSetRow.h"
          

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
        

                                                                                                     

           
                                                               
                                                   
                                                                   
           
        adb_ArrayOfWsResultSetRow_t* AXIS2_CALL
        adb_ArrayOfWsResultSetRow_create(
            const axutil_env_t *env );

           
                                                  
                                                                                    
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsResultSetRow_free (
            adb_ArrayOfWsResultSetRow_t* _ArrayOfWsResultSetRow,
            const axutil_env_t *env);



                                                                                                       
                                                                                                       
        

           
                                                                                                         
                                                                            
                                                   
                                                   
           
        axutil_array_list_t* AXIS2_CALL
        adb_ArrayOfWsResultSetRow_get_item(
            adb_ArrayOfWsResultSetRow_t* _ArrayOfWsResultSetRow,
            const axutil_env_t *env);

           
                                                                                                
                                                         
                                                                            
                                                   
                                                           
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsResultSetRow_set_item(
            adb_ArrayOfWsResultSetRow_t* _ArrayOfWsResultSetRow,
            const axutil_env_t *env,
            axutil_array_list_t*  arg_item);

           
                            
                                                                            
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsResultSetRow_reset_item(
            adb_ArrayOfWsResultSetRow_t* _ArrayOfWsResultSetRow,
            const axutil_env_t *env);

        
                                                                                                          
                                                                                                          

           
                                                     
          
                                                                              
            
                                                               
                                                                                 
          
                                                                 
                                                                                
          
                                                                                           
                                                                          
          
                                                      
                                                                  
               
            
          
           

        
        
           
                                       
                                                                            
                                                   
                                               
                                                         
           
        adb_WsResultSetRow_t* AXIS2_CALL
        adb_ArrayOfWsResultSetRow_get_item_at(
                adb_ArrayOfWsResultSetRow_t* _ArrayOfWsResultSetRow,
                const axutil_env_t *env, int i);

           
                                                                                       
                                                                            
                                                   
                                               
                                                                            
                                                         
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsResultSetRow_set_item_at(
                adb_ArrayOfWsResultSetRow_t* _ArrayOfWsResultSetRow,
                const axutil_env_t *env, int i,
                adb_WsResultSetRow_t* arg_item);


           
                       
                                                                            
                                                   
                                                                            
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsResultSetRow_add_item(
                adb_ArrayOfWsResultSetRow_t* _ArrayOfWsResultSetRow,
                const axutil_env_t *env,
                adb_WsResultSetRow_t* arg_item);

           
                                          
                                                                            
                                                    
                                              
           
        int AXIS2_CALL
        adb_ArrayOfWsResultSetRow_sizeof_item(
                    adb_ArrayOfWsResultSetRow_t* _ArrayOfWsResultSetRow,
                    const axutil_env_t *env);

           
                                          
                                                                            
                                                   
                                               
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsResultSetRow_remove_item_at(
                adb_ArrayOfWsResultSetRow_t* _ArrayOfWsResultSetRow,
                const axutil_env_t *env, int i);

        


                                                                                                           
                                                                                                           

           
                                                                  
           

        

           
                                     
                                                                            
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_ArrayOfWsResultSetRow_is_item_nil(
                adb_ArrayOfWsResultSetRow_t* _ArrayOfWsResultSetRow,
                const axutil_env_t *env);


        
                                                                                                               

           
                                                                          
                                                                                                                                      
           
        
           
                                          
                                                                            
                                                    
                                                
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_ArrayOfWsResultSetRow_is_item_nil_at(
                adb_ArrayOfWsResultSetRow_t* _ArrayOfWsResultSetRow,
                const axutil_env_t *env, int i);
 
       
           
                                
                                                                              
                                                    
                                             
                                                                        
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsResultSetRow_set_item_nil_at(
                adb_ArrayOfWsResultSetRow_t* _ArrayOfWsResultSetRow, 
                const axutil_env_t *env, int i);

        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                            
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsResultSetRow_deserialize(
            adb_ArrayOfWsResultSetRow_t* _ArrayOfWsResultSetRow,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                            
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_ArrayOfWsResultSetRow_declare_parent_namespaces(
                    adb_ArrayOfWsResultSetRow_t* _ArrayOfWsResultSetRow,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                            
                                                   
                                                                      
                                                                                   
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_ArrayOfWsResultSetRow_serialize(
            adb_ArrayOfWsResultSetRow_t* _ArrayOfWsResultSetRow,
            const axutil_env_t *env,
            axiom_node_t* ArrayOfWsResultSetRow_om_node, axiom_element_t *ArrayOfWsResultSetRow_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                       
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_ArrayOfWsResultSetRow_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                                  
    

