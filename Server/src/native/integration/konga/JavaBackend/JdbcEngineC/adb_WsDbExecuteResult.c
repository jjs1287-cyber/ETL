

           
                                  
          
                                                 
                                                                                                
           

        #include "adb_WsDbExecuteResult.h"
        
                  
                                                                            
                                           
                                                                                                     
                                         
                   
           


        struct adb_WsDbExecuteResult
        {
            adb_WsJdbcError_t* property_returnValue;

                
                axis2_bool_t is_valid_returnValue;


            adb_WsResultSet_t* property_resultSet;

                
                axis2_bool_t is_valid_resultSet;


            int property_updateCount;

                
                axis2_bool_t is_valid_updateCount;


            
        };


                                                                                               
        

                axis2_status_t AXIS2_CALL
                adb_WsDbExecuteResult_set_updateCount_nil(
                        adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                        const axutil_env_t *env);
            


                                                                                           
        adb_WsDbExecuteResult_t* AXIS2_CALL
        adb_WsDbExecuteResult_create(
            const axutil_env_t *env)
        {
            adb_WsDbExecuteResult_t *_WsDbExecuteResult = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _WsDbExecuteResult = (adb_WsDbExecuteResult_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_WsDbExecuteResult_t));

            if(NULL == _WsDbExecuteResult)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_WsDbExecuteResult, 0, sizeof(adb_WsDbExecuteResult_t));

            _WsDbExecuteResult->property_returnValue  = NULL;
                  _WsDbExecuteResult->is_valid_returnValue  = AXIS2_FALSE;
            _WsDbExecuteResult->property_resultSet  = NULL;
                  _WsDbExecuteResult->is_valid_resultSet  = AXIS2_FALSE;
            _WsDbExecuteResult->is_valid_updateCount  = AXIS2_FALSE;
            

            return _WsDbExecuteResult;
        }

        axis2_status_t AXIS2_CALL
        adb_WsDbExecuteResult_free (
                adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                const axutil_env_t *env)
        {
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _WsDbExecuteResult, AXIS2_FAILURE);

            adb_WsDbExecuteResult_reset_returnValue(_WsDbExecuteResult, env);
            adb_WsDbExecuteResult_reset_resultSet(_WsDbExecuteResult, env);
            adb_WsDbExecuteResult_reset_updateCount(_WsDbExecuteResult, env);
            

            if(_WsDbExecuteResult)
            {
                AXIS2_FREE(env->allocator, _WsDbExecuteResult);
                _WsDbExecuteResult = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_WsDbExecuteResult_deserialize(
                adb_WsDbExecuteResult_t* _WsDbExecuteResult,
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
            AXIS2_PARAM_CHECK(env->error, _WsDbExecuteResult, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for WsDbExecuteResult : "
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
                                   
                                 element_qname = axutil_qname_create(env, "returnValue", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

                           if (adb_WsJdbcError_is_particle() ||  
                                (current_node   && current_element && (axutil_qname_equals(element_qname, env, qname))))
                           {
                              if( current_node   && current_element && (axutil_qname_equals(element_qname, env, qname)))
                              {
                                is_early_node_valid = AXIS2_TRUE;
                              }
                              
                                 
                                      element = (void*)adb_WsJdbcError_create(env);

                                      status =  adb_WsJdbcError_deserialize((adb_WsJdbcError_t*)element,
                                                                            env, &current_node, &is_early_node_valid, AXIS2_FALSE);
                                      if(AXIS2_FAILURE == status)
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in building adb object for element returnValue");
                                      }
                                      else
                                      {
                                          status = adb_WsDbExecuteResult_set_returnValue(_WsDbExecuteResult, env,
                                                                   (adb_WsJdbcError_t*)element);
                                      }
                                    
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for returnValue ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element returnValue missing");
                                  return AXIS2_FAILURE;
                              }
                           
                  if(element_qname)
                  {
                     axutil_qname_free(element_qname, env);
                     element_qname = NULL;
                  }
                 

                     
                       
                                                  
                        
                     
                     
                     
                                      
                                                                                   
                                       
                                  
                                   
                                   if(current_node != NULL && is_early_node_valid)
                                   {
                                       current_node = axiom_node_get_next_sibling(current_node, env);
                                       
                                       
                                        while(current_node && axiom_node_get_node_type(current_node, env) != AXIOM_ELEMENT)
                                        {
                                            current_node = axiom_node_get_next_sibling(current_node, env);
                                        }
                                        if(current_node != NULL)
                                        {
                                            current_element = (axiom_element_t *)axiom_node_get_data_element(current_node, env);
                                            qname = axiom_element_get_qname(current_element, env, current_node);
                                        }
                                       
                                   }
                                   is_early_node_valid = AXIS2_FALSE;
                                 
                                 element_qname = axutil_qname_create(env, "resultSet", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

                           if (adb_WsResultSet_is_particle() ||  
                                (current_node   && current_element && (axutil_qname_equals(element_qname, env, qname))))
                           {
                              if( current_node   && current_element && (axutil_qname_equals(element_qname, env, qname)))
                              {
                                is_early_node_valid = AXIS2_TRUE;
                              }
                              
                                 
                                      element = (void*)adb_WsResultSet_create(env);

                                      status =  adb_WsResultSet_deserialize((adb_WsResultSet_t*)element,
                                                                            env, &current_node, &is_early_node_valid, AXIS2_FALSE);
                                      if(AXIS2_FAILURE == status)
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in building adb object for element resultSet");
                                      }
                                      else
                                      {
                                          status = adb_WsDbExecuteResult_set_resultSet(_WsDbExecuteResult, env,
                                                                   (adb_WsResultSet_t*)element);
                                      }
                                    
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for resultSet ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element resultSet missing");
                                  return AXIS2_FAILURE;
                              }
                           
                  if(element_qname)
                  {
                     axutil_qname_free(element_qname, env);
                     element_qname = NULL;
                  }
                 

                     
                       
                                                    
                        
                     
                     
                     
                                      
                                                                                   
                                       
                                  
                                   
                                   if(current_node != NULL && is_early_node_valid)
                                   {
                                       current_node = axiom_node_get_next_sibling(current_node, env);
                                       
                                       
                                        while(current_node && axiom_node_get_node_type(current_node, env) != AXIOM_ELEMENT)
                                        {
                                            current_node = axiom_node_get_next_sibling(current_node, env);
                                        }
                                        if(current_node != NULL)
                                        {
                                            current_element = (axiom_element_t *)axiom_node_get_data_element(current_node, env);
                                            qname = axiom_element_get_qname(current_element, env, current_node);
                                        }
                                       
                                   }
                                   is_early_node_valid = AXIS2_FALSE;
                                 
                                 element_qname = axutil_qname_create(env, "updateCount", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

                           if ( 
                                (current_node   && current_element && (axutil_qname_equals(element_qname, env, qname))))
                           {
                              if( current_node   && current_element && (axutil_qname_equals(element_qname, env, qname)))
                              {
                                is_early_node_valid = AXIS2_TRUE;
                              }
                              
                                 
                                      text_value = axiom_element_get_text(current_element, env, current_node);
                                      if(text_value != NULL)
                                      {
                                            status = adb_WsDbExecuteResult_set_updateCount(_WsDbExecuteResult, env,
                                                                   atoi(text_value));
                                      }
                                      
                                      else
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "NULL value is set to a non nillable element updateCount");
                                          status = AXIS2_FAILURE;
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for updateCount ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element updateCount missing");
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
          adb_WsDbExecuteResult_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_WsDbExecuteResult_declare_parent_namespaces(
                    adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_WsDbExecuteResult_serialize(
                adb_WsDbExecuteResult_t* _WsDbExecuteResult,
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
                    
                    axis2_char_t text_value_2[64];
                    
                    axis2_char_t text_value_3[64];
                    
               axis2_char_t *start_input_str = NULL;
               axis2_char_t *end_input_str = NULL;
               unsigned int start_input_str_len = 0;
               unsigned int end_input_str_len = 0;
            
            
               axiom_data_source_t *data_source = NULL;
               axutil_stream_t *stream = NULL;

            

            AXIS2_ENV_CHECK(env, NULL);
            AXIS2_PARAM_CHECK(env->error, _WsDbExecuteResult, NULL);
            
            
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
                      

                   if (!_WsDbExecuteResult->is_valid_returnValue)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("returnValue") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%sreturnValue xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("returnValue"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("returnValue")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                   
                        

                    
                    
                            sprintf(start_input_str, "<%s%sreturnValue",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":""); 
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sreturnValue>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                     
                            if(!adb_WsJdbcError_is_particle())
                            {
                                axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                            }
                            
                            adb_WsJdbcError_serialize(_WsDbExecuteResult->property_returnValue, 
                                                                                 env, current_node, parent_element,
                                                                                 adb_WsJdbcError_is_particle() || AXIS2_FALSE, namespaces, next_ns_index);
                            
                            if(!adb_WsJdbcError_is_particle())
                            {
                                axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                            }
                            
                     
                     AXIS2_FREE(env->allocator,start_input_str);
                     AXIS2_FREE(env->allocator,end_input_str);
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
                      

                   if (!_WsDbExecuteResult->is_valid_resultSet)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("resultSet") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%sresultSet xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("resultSet"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("resultSet")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                 
                        

                    
                    
                            sprintf(start_input_str, "<%s%sresultSet",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":""); 
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sresultSet>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                     
                            if(!adb_WsResultSet_is_particle())
                            {
                                axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                            }
                            
                            adb_WsResultSet_serialize(_WsDbExecuteResult->property_resultSet, 
                                                                                 env, current_node, parent_element,
                                                                                 adb_WsResultSet_is_particle() || AXIS2_FALSE, namespaces, next_ns_index);
                            
                            if(!adb_WsResultSet_is_particle())
                            {
                                axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                            }
                            
                     
                     AXIS2_FREE(env->allocator,start_input_str);
                     AXIS2_FREE(env->allocator,end_input_str);
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
                      

                   if (!_WsDbExecuteResult->is_valid_updateCount)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property updateCount");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("updateCount"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("updateCount")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                   
                        

                    
                    
                            sprintf(start_input_str, "<%s%supdateCount>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%supdateCount>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                               sprintf (text_value_3, AXIS2_PRINTF_INT32_FORMAT_SPECIFIER, _WsDbExecuteResult->property_updateCount);
                             
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                           axutil_stream_write(stream, env, text_value_3, axutil_strlen(text_value_3));
                           
                           axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                           
                     
                     AXIS2_FREE(env->allocator,start_input_str);
                     AXIS2_FREE(env->allocator,end_input_str);
                 } 

                 

            return parent;
        }


        

               
                                      
               
            adb_WsJdbcError_t* AXIS2_CALL
            adb_WsDbExecuteResult_get_returnValue(
                    adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsDbExecuteResult, NULL);
                  

                return _WsDbExecuteResult->property_returnValue;
             }

               
                                     
               
            axis2_status_t AXIS2_CALL
            adb_WsDbExecuteResult_set_returnValue(
                    adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                    const axutil_env_t *env,
                    adb_WsJdbcError_t*  arg_returnValue)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsDbExecuteResult, AXIS2_FAILURE);
                
                if(_WsDbExecuteResult->is_valid_returnValue &&
                        arg_returnValue == _WsDbExecuteResult->property_returnValue)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsDbExecuteResult_reset_returnValue(_WsDbExecuteResult, env);

                
                if(NULL == arg_returnValue)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsDbExecuteResult->property_returnValue = arg_returnValue;
                        _WsDbExecuteResult->is_valid_returnValue = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                      
              
           axis2_status_t AXIS2_CALL
           adb_WsDbExecuteResult_reset_returnValue(
                   adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsDbExecuteResult, AXIS2_FAILURE);
               

               
            
                
                if(_WsDbExecuteResult->property_returnValue != NULL)
                {
                   
                   
                        adb_WsJdbcError_free(_WsDbExecuteResult->property_returnValue, env);
                     _WsDbExecuteResult->property_returnValue = NULL;
                }
            
                
                
                _WsDbExecuteResult->is_valid_returnValue = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                               
              
           axis2_bool_t AXIS2_CALL
           adb_WsDbExecuteResult_is_returnValue_nil(
                   adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsDbExecuteResult, AXIS2_TRUE);
               
               return !_WsDbExecuteResult->is_valid_returnValue;
           }

              
                                                                   
              
           axis2_status_t AXIS2_CALL
           adb_WsDbExecuteResult_set_returnValue_nil(
                   adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                   const axutil_env_t *env)
           {
               return adb_WsDbExecuteResult_reset_returnValue(_WsDbExecuteResult, env);
           }

           

               
                                    
               
            adb_WsResultSet_t* AXIS2_CALL
            adb_WsDbExecuteResult_get_resultSet(
                    adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsDbExecuteResult, NULL);
                  

                return _WsDbExecuteResult->property_resultSet;
             }

               
                                   
               
            axis2_status_t AXIS2_CALL
            adb_WsDbExecuteResult_set_resultSet(
                    adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                    const axutil_env_t *env,
                    adb_WsResultSet_t*  arg_resultSet)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsDbExecuteResult, AXIS2_FAILURE);
                
                if(_WsDbExecuteResult->is_valid_resultSet &&
                        arg_resultSet == _WsDbExecuteResult->property_resultSet)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsDbExecuteResult_reset_resultSet(_WsDbExecuteResult, env);

                
                if(NULL == arg_resultSet)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsDbExecuteResult->property_resultSet = arg_resultSet;
                        _WsDbExecuteResult->is_valid_resultSet = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                    
              
           axis2_status_t AXIS2_CALL
           adb_WsDbExecuteResult_reset_resultSet(
                   adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsDbExecuteResult, AXIS2_FAILURE);
               

               
            
                
                if(_WsDbExecuteResult->property_resultSet != NULL)
                {
                   
                   
                        adb_WsResultSet_free(_WsDbExecuteResult->property_resultSet, env);
                     _WsDbExecuteResult->property_resultSet = NULL;
                }
            
                
                
                _WsDbExecuteResult->is_valid_resultSet = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                             
              
           axis2_bool_t AXIS2_CALL
           adb_WsDbExecuteResult_is_resultSet_nil(
                   adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsDbExecuteResult, AXIS2_TRUE);
               
               return !_WsDbExecuteResult->is_valid_resultSet;
           }

              
                                                                 
              
           axis2_status_t AXIS2_CALL
           adb_WsDbExecuteResult_set_resultSet_nil(
                   adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                   const axutil_env_t *env)
           {
               return adb_WsDbExecuteResult_reset_resultSet(_WsDbExecuteResult, env);
           }

           

               
                                      
               
            int AXIS2_CALL
            adb_WsDbExecuteResult_get_updateCount(
                    adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, (int)0);
                    AXIS2_PARAM_CHECK(env->error, _WsDbExecuteResult, (int)0);
                  

                return _WsDbExecuteResult->property_updateCount;
             }

               
                                     
               
            axis2_status_t AXIS2_CALL
            adb_WsDbExecuteResult_set_updateCount(
                    adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                    const axutil_env_t *env,
                    const int  arg_updateCount)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsDbExecuteResult, AXIS2_FAILURE);
                
                if(_WsDbExecuteResult->is_valid_updateCount &&
                        arg_updateCount == _WsDbExecuteResult->property_updateCount)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsDbExecuteResult_reset_updateCount(_WsDbExecuteResult, env);

                _WsDbExecuteResult->property_updateCount = arg_updateCount;
                        _WsDbExecuteResult->is_valid_updateCount = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                      
              
           axis2_status_t AXIS2_CALL
           adb_WsDbExecuteResult_reset_updateCount(
                   adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsDbExecuteResult, AXIS2_FAILURE);
               

               _WsDbExecuteResult->is_valid_updateCount = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                               
              
           axis2_bool_t AXIS2_CALL
           adb_WsDbExecuteResult_is_updateCount_nil(
                   adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsDbExecuteResult, AXIS2_TRUE);
               
               return !_WsDbExecuteResult->is_valid_updateCount;
           }

              
                                                                   
              
           axis2_status_t AXIS2_CALL
           adb_WsDbExecuteResult_set_updateCount_nil(
                   adb_WsDbExecuteResult_t* _WsDbExecuteResult,
                   const axutil_env_t *env)
           {
               return adb_WsDbExecuteResult_reset_updateCount(_WsDbExecuteResult, env);
           }

           

