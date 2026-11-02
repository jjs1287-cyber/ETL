

        #ifndef ADB_ARRAYOFWSKEYVALUE_H
        #define ADB_ARRAYOFWSKEYVALUE_H

          
                                 
         
                                                
                                                                                         
          

          
                                      
          
        typedef struct adb_ArrayOfWsKeyValue adb_ArrayOfWsKeyValue_t;

        
          #include "adb_WsKeyValue.h"
          

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
        

                                                                                                     

           
                                                           
                                                   
                                                               
           
        adb_ArrayOfWsKeyValue_t* AXIS2_CALL
        adb_ArrayOfWsKeyValue_create(
            const axutil_env_t *env );

           
                                              
                                                                            
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsKeyValue_free (
            adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
            const axutil_env_t *env);



                                                                                                       
                                                                                                       
        

           
                                                                                                     
                                                                    
                                                   
                                               
           
        axutil_array_list_t* AXIS2_CALL
        adb_ArrayOfWsKeyValue_get_item(
            adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
            const axutil_env_t *env);

           
                                                                                            
                                                     
                                                                    
                                                   
                                                       
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsKeyValue_set_item(
            adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
            const axutil_env_t *env,
            axutil_array_list_t*  arg_item);

           
                            
                                                                    
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsKeyValue_reset_item(
            adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
            const axutil_env_t *env);

        
                                                                                                          
                                                                                                          

           
                                                     
          
                                                                              
            
                                                               
                                                                                 
          
                                                                 
                                                                                
          
                                                                                           
                                                                          
          
                                                      
                                                                  
               
            
          
           

        
        
           
                                       
                                                                    
                                                   
                                               
                                                     
           
        adb_WsKeyValue_t* AXIS2_CALL
        adb_ArrayOfWsKeyValue_get_item_at(
                adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                const axutil_env_t *env, int i);

           
                                                                                       
                                                                    
                                                   
                                               
                                                                        
                                                     
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsKeyValue_set_item_at(
                adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                const axutil_env_t *env, int i,
                adb_WsKeyValue_t* arg_item);


           
                       
                                                                    
                                                   
                                                                        
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsKeyValue_add_item(
                adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                const axutil_env_t *env,
                adb_WsKeyValue_t* arg_item);

           
                                          
                                                                    
                                                    
                                              
           
        int AXIS2_CALL
        adb_ArrayOfWsKeyValue_sizeof_item(
                    adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                    const axutil_env_t *env);

           
                                          
                                                                    
                                                   
                                               
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsKeyValue_remove_item_at(
                adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                const axutil_env_t *env, int i);

        


                                                                                                           
                                                                                                           

           
                                                                  
           

        

           
                                     
                                                                    
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_ArrayOfWsKeyValue_is_item_nil(
                adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                const axutil_env_t *env);


        
                                                                                                               

           
                                                                          
                                                                                                                                      
           
        
           
                                          
                                                                    
                                                    
                                                
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_ArrayOfWsKeyValue_is_item_nil_at(
                adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                const axutil_env_t *env, int i);
 
       
           
                                
                                                                      
                                                    
                                             
                                                                        
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsKeyValue_set_item_nil_at(
                adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue, 
                const axutil_env_t *env, int i);

        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                    
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsKeyValue_deserialize(
            adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                    
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_ArrayOfWsKeyValue_declare_parent_namespaces(
                    adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                    
                                                   
                                                                  
                                                                               
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_ArrayOfWsKeyValue_serialize(
            adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
            const axutil_env_t *env,
            axiom_node_t* ArrayOfWsKeyValue_om_node, axiom_element_t *ArrayOfWsKeyValue_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                   
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_ArrayOfWsKeyValue_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                              
    

