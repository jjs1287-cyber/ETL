

           
                               
          
                                                 
                                                                                                
           

        #include "adb_WsResultSetRow.h"
        
                  
                                                                            
                                        
                                                                                                     
                                         
                   
           


        struct adb_WsResultSetRow
        {
            adb_ArrayOf_xsd_string_t* property_columns;

                
                axis2_bool_t is_valid_columns;


            
        };


                                                                                               
        


                                                                                           
        adb_WsResultSetRow_t* AXIS2_CALL
        adb_WsResultSetRow_create(
            const axutil_env_t *env)
        {
            adb_WsResultSetRow_t *_WsResultSetRow = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _WsResultSetRow = (adb_WsResultSetRow_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_WsResultSetRow_t));

            if(NULL == _WsResultSetRow)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_WsResultSetRow, 0, sizeof(adb_WsResultSetRow_t));

            _WsResultSetRow->property_columns  = NULL;
                  _WsResultSetRow->is_valid_columns  = AXIS2_FALSE;
            

            return _WsResultSetRow;
        }

        axis2_status_t AXIS2_CALL
        adb_WsResultSetRow_free (
                adb_WsResultSetRow_t* _WsResultSetRow,
                const axutil_env_t *env)
        {
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _WsResultSetRow, AXIS2_FAILURE);

            adb_WsResultSetRow_reset_columns(_WsResultSetRow, env);
            

            if(_WsResultSetRow)
            {
                AXIS2_FREE(env->allocator, _WsResultSetRow);
                _WsResultSetRow = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_WsResultSetRow_deserialize(
                adb_WsResultSetRow_t* _WsResultSetRow,
                const axutil_env_t *env,
                axiom_node_t **dp_parent,
                axis2_bool_t *dp_is_early_node_valid,
                axis2_bool_t dont_care_minoccurs)
        {
          axiom_node_t *parent = *dp_parent;
          
          axis2_status_t status = AXIS2_SUCCESS;
          
              void *element = NULL;
           
             axis2_char_t* text_value = NULL;
             axutil_qname_t *qname = NULL;
          
            axutil_qname_t *element_qname = NULL; 
            
               axiom_node_t *first_node = NULL;
               axis2_bool_t is_early_node_valid = AXIS2_TRUE;
               axiom_node_t *current_node = NULL;
               axiom_element_t *current_element = NULL;
            
            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _WsResultSetRow, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for WsResultSetRow : "
                            "NULL elemenet can not be passed to deserialize");
                return AXIS2_FAILURE;
              }
              
                      
                      first_node = axiom_node_get_first_child(parent, env);
                      
                    

                     
                       
                                                
                        
                     
                     
                     
                                   current_node = first_node;
                                   is_early_node_valid = AXIS2_FALSE;
                                   
                                   
                                    while(current_node && axiom_node_get_node_type(current_node, env) != AXIOM_ELEMENT)
                                    {
                                        current_node = axiom_node_get_next_sibling(current_node, env);
                                    }
                                    if(current_node != NULL)
                                    {
                                        current_element = (axiom_element_t *)axiom_node_get_data_element(current_node, env);
                                        qname = axiom_element_get_qname(current_element, env, current_node);
                                    }
                                   
                                 element_qname = axutil_qname_create(env, "columns", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

                           if (adb_ArrayOf_xsd_string_is_particle() ||  
                                (current_node   && current_element && (axutil_qname_equals(element_qname, env, qname))))
                           {
                              if( current_node   && current_element && (axutil_qname_equals(element_qname, env, qname)))
                              {
                                is_early_node_valid = AXIS2_TRUE;
                              }
                              
                                 
                                      element = (void*)adb_ArrayOf_xsd_string_create(env);

                                      status =  adb_ArrayOf_xsd_string_deserialize((adb_ArrayOf_xsd_string_t*)element,
                                                                            env, &current_node, &is_early_node_valid, AXIS2_FALSE);
                                      if(AXIS2_FAILURE == status)
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in building adb object for element columns");
                                      }
                                      else
                                      {
                                          status = adb_WsResultSetRow_set_columns(_WsResultSetRow, env,
                                                                   (adb_ArrayOf_xsd_string_t*)element);
                                      }
                                    
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for columns ");
                                     if(element_qname)
                                     {
                                         axutil_qname_free(element_qname, env);
                                     }
                                     return AXIS2_FAILURE;
                                 }
                              }
                           
                              else if(!dont_care_minoccurs)
                              {
                                  if(element_qname)
                                  {
                                      axutil_qname_free(element_qname, env);
                                  }
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element columns missing");
                                  return AXIS2_FAILURE;
                              }
                           
                  if(element_qname)
                  {
                     axutil_qname_free(element_qname, env);
                     element_qname = NULL;
                  }
                 
          return status;
       }

          axis2_bool_t AXIS2_CALL
          adb_WsResultSetRow_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_WsResultSetRow_declare_parent_namespaces(
                    adb_WsResultSetRow_t* _WsResultSetRow,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_WsResultSetRow_serialize(
                adb_WsResultSetRow_t* _WsResultSetRow,
                const axutil_env_t *env, axiom_node_t *parent, axiom_element_t *parent_element, int parent_tag_closed, axutil_hash_t *namespaces, int *next_ns_index)
        {
            
            
             axis2_char_t *string_to_stream;
            
         
         axiom_node_t *current_node = NULL;
         int tag_closed = 0;

         
         
                axiom_namespace_t *ns1 = NULL;

                axis2_char_t *qname_uri = NULL;
                axis2_char_t *qname_prefix = NULL;
                axis2_char_t *p_prefix = NULL;
                axis2_bool_t ns_already_defined;
            
                    axis2_char_t text_value_1[64];
                    
               axis2_char_t *start_input_str = NULL;
               axis2_char_t *end_input_str = NULL;
               unsigned int start_input_str_len = 0;
               unsigned int end_input_str_len = 0;
            
            
               axiom_data_source_t *data_source = NULL;
               axutil_stream_t *stream = NULL;

            

            AXIS2_ENV_CHECK(env, NULL);
            AXIS2_PARAM_CHECK(env->error, _WsResultSetRow, NULL);
            
            
                    current_node = parent;
                    data_source = (axiom_data_source_t *)axiom_node_get_data_element(current_node, env);
                    if (!data_source)
                        return NULL;
                    stream = axiom_data_source_get_stream(data_source, env);                                           
                    if (!stream)
                        return NULL;
                  
            if(!parent_tag_closed)
            {
            
              string_to_stream = ">"; 
              axutil_stream_write(stream, env, string_to_stream, axutil_strlen(string_to_stream));
              tag_closed = 1;
            
            }
            
                       if(!(p_prefix = (axis2_char_t*)axutil_hash_get(namespaces, "http://jdbc.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING)))
                       {
                           p_prefix = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof (axis2_char_t) * ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT);
                           sprintf(p_prefix, "n%d", (*next_ns_index)++);
                           axutil_hash_set(namespaces, "http://jdbc.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, p_prefix);
                           
                           axiom_element_declare_namespace_assume_param_ownership(parent_element, env, axiom_namespace_create (env,
                                            "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
                                            p_prefix));
                       }
                      

                   if (!_WsResultSetRow->is_valid_columns)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("columns") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%scolumns xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("columns"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("columns")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                               
                        

                    
                    
                            sprintf(start_input_str, "<%s%scolumns",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":""); 
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%scolumns>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                     
                            if(!adb_ArrayOf_xsd_string_is_particle())
                            {
                                axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                            }
                            
                            adb_ArrayOf_xsd_string_serialize(_WsResultSetRow->property_columns, 
                                                                                 env, current_node, parent_element,
                                                                                 adb_ArrayOf_xsd_string_is_particle() || AXIS2_FALSE, namespaces, next_ns_index);
                            
                            if(!adb_ArrayOf_xsd_string_is_particle())
                            {
                                axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                            }
                            
                     
                     AXIS2_FREE(env->allocator,start_input_str);
                     AXIS2_FREE(env->allocator,end_input_str);
                 } 

                 

            return parent;
        }


        

               
                                  
               
            adb_ArrayOf_xsd_string_t* AXIS2_CALL
            adb_WsResultSetRow_get_columns(
                    adb_WsResultSetRow_t* _WsResultSetRow,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsResultSetRow, NULL);
                  

                return _WsResultSetRow->property_columns;
             }

               
                                 
               
            axis2_status_t AXIS2_CALL
            adb_WsResultSetRow_set_columns(
                    adb_WsResultSetRow_t* _WsResultSetRow,
                    const axutil_env_t *env,
                    adb_ArrayOf_xsd_string_t*  arg_columns)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsResultSetRow, AXIS2_FAILURE);
                
                if(_WsResultSetRow->is_valid_columns &&
                        arg_columns == _WsResultSetRow->property_columns)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsResultSetRow_reset_columns(_WsResultSetRow, env);

                
                if(NULL == arg_columns)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsResultSetRow->property_columns = arg_columns;
                        _WsResultSetRow->is_valid_columns = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                  
              
           axis2_status_t AXIS2_CALL
           adb_WsResultSetRow_reset_columns(
                   adb_WsResultSetRow_t* _WsResultSetRow,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsResultSetRow, AXIS2_FAILURE);
               

               
            
                
                if(_WsResultSetRow->property_columns != NULL)
                {
                   
                   
                        adb_ArrayOf_xsd_string_free(_WsResultSetRow->property_columns, env);
                     _WsResultSetRow->property_columns = NULL;
                }
            
                
                
                _WsResultSetRow->is_valid_columns = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                           
              
           axis2_bool_t AXIS2_CALL
           adb_WsResultSetRow_is_columns_nil(
                   adb_WsResultSetRow_t* _WsResultSetRow,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsResultSetRow, AXIS2_TRUE);
               
               return !_WsResultSetRow->is_valid_columns;
           }

              
                                                               
              
           axis2_status_t AXIS2_CALL
           adb_WsResultSetRow_set_columns_nil(
                   adb_WsResultSetRow_t* _WsResultSetRow,
                   const axutil_env_t *env)
           {
               return adb_WsResultSetRow_reset_columns(_WsResultSetRow, env);
           }

           

