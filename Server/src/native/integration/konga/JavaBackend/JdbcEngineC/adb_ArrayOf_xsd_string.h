

        #ifndef ADB_ARRAYOF_XSD_STRING_H
        #define ADB_ARRAYOF_XSD_STRING_H

          
                                  
         
                                                
                                                                                         
          

          
                                       
          
        typedef struct adb_ArrayOf_xsd_string adb_ArrayOf_xsd_string_t;

        

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
        

                                                                                                     

           
                                                            
                                                   
                                                                
           
        adb_ArrayOf_xsd_string_t* AXIS2_CALL
        adb_ArrayOf_xsd_string_create(
            const axutil_env_t *env );

           
                                               
                                                                              
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOf_xsd_string_free (
            adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
            const axutil_env_t *env);



                                                                                                       
                                                                                                       
        

           
                                                                                                      
                                                                      
                                                   
                                           
           
        axutil_array_list_t* AXIS2_CALL
        adb_ArrayOf_xsd_string_get_item(
            adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
            const axutil_env_t *env);

           
                                                                                             
                                                      
                                                                      
                                                   
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOf_xsd_string_set_item(
            adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
            const axutil_env_t *env,
            axutil_array_list_t*  arg_item);

           
                            
                                                                      
                                                   
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOf_xsd_string_reset_item(
            adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
            const axutil_env_t *env);

        
                                                                                                          
                                                                                                          

           
                                                     
          
                                                                              
            
                                                               
                                                                                 
          
                                                                 
                                                                                
          
                                                                                           
                                                                          
          
                                                      
                                                                  
               
            
          
           

        
        
           
                                       
                                                                      
                                                   
                                               
                                                 
           
        axis2_char_t* AXIS2_CALL
        adb_ArrayOf_xsd_string_get_item_at(
                adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                const axutil_env_t *env, int i);

           
                                                                                       
                                                                      
                                                   
                                               
                                                                    
                                                 
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOf_xsd_string_set_item_at(
                adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                const axutil_env_t *env, int i,
                const axis2_char_t* arg_item);


           
                       
                                                                      
                                                   
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOf_xsd_string_add_item(
                adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                const axutil_env_t *env,
                const axis2_char_t* arg_item);

           
                                          
                                                                      
                                                    
                                              
           
        int AXIS2_CALL
        adb_ArrayOf_xsd_string_sizeof_item(
                    adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                    const axutil_env_t *env);

           
                                          
                                                                      
                                                   
                                               
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOf_xsd_string_remove_item_at(
                adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                const axutil_env_t *env, int i);

        


                                                                                                           
                                                                                                           

           
                                                                  
           

        

           
                                     
                                                                      
                                                   
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_ArrayOf_xsd_string_is_item_nil(
                adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                const axutil_env_t *env);


        
                                                                                                               

           
                                                                          
                                                                                                                                      
           
        
           
                                          
                                                                      
                                                    
                                                
                                                                            
           
        axis2_bool_t AXIS2_CALL
        adb_ArrayOf_xsd_string_is_item_nil_at(
                adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                const axutil_env_t *env, int i);
 
       
           
                                
                                                                        
                                                    
                                             
                                                                        
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOf_xsd_string_set_item_nil_at(
                adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string, 
                const axutil_env_t *env, int i);

        

                                                                                                      
                                                                                                      

        
           
                                            
                                                                      
                                                   
                                                                            
                                                                                        
                                                                               
                                                                    
                                                               
           
        axis2_status_t AXIS2_CALL
        adb_ArrayOf_xsd_string_deserialize(
            adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
            const axutil_env_t *env,
            axiom_node_t** dp_parent,
            axis2_bool_t *dp_is_early_node_valid,
            axis2_bool_t dont_care_minoccurs);
                            
            

          
                                                     
                                                                      
                                                   
                                               
                                                            
                                                                                        
           
       void AXIS2_CALL
       adb_ArrayOf_xsd_string_declare_parent_namespaces(
                    adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index);

        

           
                                                   
                                                                      
                                                   
                                                                   
                                                                                
                                                                    
                                                            
                                                                             
                                                               
           
        axiom_node_t* AXIS2_CALL
        adb_ArrayOf_xsd_string_serialize(
            adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
            const axutil_env_t *env,
            axiom_node_t* ArrayOf_xsd_string_om_node, axiom_element_t *ArrayOf_xsd_string_om_element, int tag_closed, axutil_hash_t *namespaces, int *next_ns_index);

           
                                                                                                    
                                                    
           
        axis2_bool_t AXIS2_CALL
        adb_ArrayOf_xsd_string_is_particle();


     #ifdef __cplusplus
     }
     #endif

     #endif                               
    

