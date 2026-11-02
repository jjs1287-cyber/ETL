

           
                                 
          
                                                 
                                                                                                
           

        #include "adb_WsBulkLoadParams.h"
        
                  
                                                                            
                                          
                                                                                                                
                                         
                   
           


        struct adb_WsBulkLoadParams
        {
            int64_t property_operationInstanceId;

                
                axis2_bool_t is_valid_operationInstanceId;


            int64_t property_lastOperationInstanceId;

                
                axis2_bool_t is_valid_lastOperationInstanceId;


            int64_t property_bulkLoadId;

                
                axis2_bool_t is_valid_bulkLoadId;


            axis2_char_t* property_sourceFilePath;

                
                axis2_bool_t is_valid_sourceFilePath;


            
        };


                                                                                               
        

                axis2_status_t AXIS2_CALL
                adb_WsBulkLoadParams_set_operationInstanceId_nil(
                        adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                        const axutil_env_t *env);
            

                axis2_status_t AXIS2_CALL
                adb_WsBulkLoadParams_set_lastOperationInstanceId_nil(
                        adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                        const axutil_env_t *env);
            

                axis2_status_t AXIS2_CALL
                adb_WsBulkLoadParams_set_bulkLoadId_nil(
                        adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                        const axutil_env_t *env);
            


                                                                                           
        adb_WsBulkLoadParams_t* AXIS2_CALL
        adb_WsBulkLoadParams_create(
            const axutil_env_t *env)
        {
            adb_WsBulkLoadParams_t *_WsBulkLoadParams = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _WsBulkLoadParams = (adb_WsBulkLoadParams_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_WsBulkLoadParams_t));

            if(NULL == _WsBulkLoadParams)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_WsBulkLoadParams, 0, sizeof(adb_WsBulkLoadParams_t));

            _WsBulkLoadParams->is_valid_operationInstanceId  = AXIS2_FALSE;
            _WsBulkLoadParams->is_valid_lastOperationInstanceId  = AXIS2_FALSE;
            _WsBulkLoadParams->is_valid_bulkLoadId  = AXIS2_FALSE;
            _WsBulkLoadParams->property_sourceFilePath  = NULL;
                  _WsBulkLoadParams->is_valid_sourceFilePath  = AXIS2_FALSE;
            

            return _WsBulkLoadParams;
        }

        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadParams_free (
                adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                const axutil_env_t *env)
        {
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, AXIS2_FAILURE);

            adb_WsBulkLoadParams_reset_operationInstanceId(_WsBulkLoadParams, env);
            adb_WsBulkLoadParams_reset_lastOperationInstanceId(_WsBulkLoadParams, env);
            adb_WsBulkLoadParams_reset_bulkLoadId(_WsBulkLoadParams, env);
            adb_WsBulkLoadParams_reset_sourceFilePath(_WsBulkLoadParams, env);
            

            if(_WsBulkLoadParams)
            {
                AXIS2_FREE(env->allocator, _WsBulkLoadParams);
                _WsBulkLoadParams = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadParams_deserialize(
                adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                const axutil_env_t *env,
                axiom_node_t **dp_parent,
                axis2_bool_t *dp_is_early_node_valid,
                axis2_bool_t dont_care_minoccurs)
        {
          axiom_node_t *parent = *dp_parent;
          
          axis2_status_t status = AXIS2_SUCCESS;
           
             axis2_char_t* text_value = NULL;
             axutil_qname_t *qname = NULL;
          
            axutil_qname_t *element_qname = NULL; 
            
               axiom_node_t *first_node = NULL;
               axis2_bool_t is_early_node_valid = AXIS2_TRUE;
               axiom_node_t *current_node = NULL;
               axiom_element_t *current_element = NULL;
            
            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for WsBulkLoadParams : "
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
                                   
                                 element_qname = axutil_qname_create(env, "operationInstanceId", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsBulkLoadParams_set_operationInstanceId(_WsBulkLoadParams, env,
                                                                   axutil_strtol(text_value, (char**)NULL, 0));
                                      }
                                      
                                      else
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "NULL value is set to a non nillable element operationInstanceId");
                                          status = AXIS2_FAILURE;
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for operationInstanceId ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element operationInstanceId missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "lastOperationInstanceId", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsBulkLoadParams_set_lastOperationInstanceId(_WsBulkLoadParams, env,
                                                                   axutil_strtol(text_value, (char**)NULL, 0));
                                      }
                                      
                                      else
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "NULL value is set to a non nillable element lastOperationInstanceId");
                                          status = AXIS2_FAILURE;
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for lastOperationInstanceId ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element lastOperationInstanceId missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "bulkLoadId", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsBulkLoadParams_set_bulkLoadId(_WsBulkLoadParams, env,
                                                                   axutil_strtol(text_value, (char**)NULL, 0));
                                      }
                                      
                                      else
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "NULL value is set to a non nillable element bulkLoadId");
                                          status = AXIS2_FAILURE;
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for bulkLoadId ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element bulkLoadId missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "sourceFilePath", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsBulkLoadParams_set_sourceFilePath(_WsBulkLoadParams, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for sourceFilePath ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element sourceFilePath missing");
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
          adb_WsBulkLoadParams_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_WsBulkLoadParams_declare_parent_namespaces(
                    adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_WsBulkLoadParams_serialize(
                adb_WsBulkLoadParams_t* _WsBulkLoadParams,
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
                    
                    axis2_char_t *text_value_4;
                    axis2_char_t *text_value_4_temp;
                    
               axis2_char_t *start_input_str = NULL;
               axis2_char_t *end_input_str = NULL;
               unsigned int start_input_str_len = 0;
               unsigned int end_input_str_len = 0;
            
            
               axiom_data_source_t *data_source = NULL;
               axutil_stream_t *stream = NULL;

            

            AXIS2_ENV_CHECK(env, NULL);
            AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, NULL);
            
            
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
            
                       if(!(p_prefix = (axis2_char_t*)axutil_hash_get(namespaces, "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING)))
                       {
                           p_prefix = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof (axis2_char_t) * ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT);
                           sprintf(p_prefix, "n%d", (*next_ns_index)++);
                           axutil_hash_set(namespaces, "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, p_prefix);
                           
                           axiom_element_declare_namespace_assume_param_ownership(parent_element, env, axiom_namespace_create (env,
                                            "http://bulkload.webservice.implementation.server.integration.etlforge.invalid",
                                            p_prefix));
                       }
                      

                   if (!_WsBulkLoadParams->is_valid_operationInstanceId)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property operationInstanceId");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("operationInstanceId"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("operationInstanceId")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                           
                        

                    
                    
                            sprintf(start_input_str, "<%s%soperationInstanceId>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%soperationInstanceId>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                               sprintf (text_value_1, AXIS2_PRINTF_INT64_FORMAT_SPECIFIER, (int64_t) _WsBulkLoadParams->property_operationInstanceId);
                             
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                           axutil_stream_write(stream, env, text_value_1, axutil_strlen(text_value_1));
                           
                           axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                           
                     
                     AXIS2_FREE(env->allocator,start_input_str);
                     AXIS2_FREE(env->allocator,end_input_str);
                 } 

                 
                       if(!(p_prefix = (axis2_char_t*)axutil_hash_get(namespaces, "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING)))
                       {
                           p_prefix = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof (axis2_char_t) * ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT);
                           sprintf(p_prefix, "n%d", (*next_ns_index)++);
                           axutil_hash_set(namespaces, "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, p_prefix);
                           
                           axiom_element_declare_namespace_assume_param_ownership(parent_element, env, axiom_namespace_create (env,
                                            "http://bulkload.webservice.implementation.server.integration.etlforge.invalid",
                                            p_prefix));
                       }
                      

                   if (!_WsBulkLoadParams->is_valid_lastOperationInstanceId)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property lastOperationInstanceId");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("lastOperationInstanceId"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("lastOperationInstanceId")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                               
                        

                    
                    
                            sprintf(start_input_str, "<%s%slastOperationInstanceId>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%slastOperationInstanceId>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                               sprintf (text_value_2, AXIS2_PRINTF_INT64_FORMAT_SPECIFIER, (int64_t) _WsBulkLoadParams->property_lastOperationInstanceId);
                             
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                           axutil_stream_write(stream, env, text_value_2, axutil_strlen(text_value_2));
                           
                           axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                           
                     
                     AXIS2_FREE(env->allocator,start_input_str);
                     AXIS2_FREE(env->allocator,end_input_str);
                 } 

                 
                       if(!(p_prefix = (axis2_char_t*)axutil_hash_get(namespaces, "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING)))
                       {
                           p_prefix = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof (axis2_char_t) * ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT);
                           sprintf(p_prefix, "n%d", (*next_ns_index)++);
                           axutil_hash_set(namespaces, "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, p_prefix);
                           
                           axiom_element_declare_namespace_assume_param_ownership(parent_element, env, axiom_namespace_create (env,
                                            "http://bulkload.webservice.implementation.server.integration.etlforge.invalid",
                                            p_prefix));
                       }
                      

                   if (!_WsBulkLoadParams->is_valid_bulkLoadId)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property bulkLoadId");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("bulkLoadId"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("bulkLoadId")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                  
                        

                    
                    
                            sprintf(start_input_str, "<%s%sbulkLoadId>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sbulkLoadId>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                               sprintf (text_value_3, AXIS2_PRINTF_INT64_FORMAT_SPECIFIER, (int64_t) _WsBulkLoadParams->property_bulkLoadId);
                             
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                           axutil_stream_write(stream, env, text_value_3, axutil_strlen(text_value_3));
                           
                           axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                           
                     
                     AXIS2_FREE(env->allocator,start_input_str);
                     AXIS2_FREE(env->allocator,end_input_str);
                 } 

                 
                       if(!(p_prefix = (axis2_char_t*)axutil_hash_get(namespaces, "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING)))
                       {
                           p_prefix = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof (axis2_char_t) * ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT);
                           sprintf(p_prefix, "n%d", (*next_ns_index)++);
                           axutil_hash_set(namespaces, "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, p_prefix);
                           
                           axiom_element_declare_namespace_assume_param_ownership(parent_element, env, axiom_namespace_create (env,
                                            "http://bulkload.webservice.implementation.server.integration.etlforge.invalid",
                                            p_prefix));
                       }
                      

                   if (!_WsBulkLoadParams->is_valid_sourceFilePath)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("sourceFilePath") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%ssourceFilePath xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("sourceFilePath"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("sourceFilePath")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                      
                        

                    
                    
                            sprintf(start_input_str, "<%s%ssourceFilePath>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%ssourceFilePath>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_4 = _WsBulkLoadParams->property_sourceFilePath;
                           
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                            
                           text_value_4_temp = axutil_xml_quote_string(env, text_value_4, AXIS2_TRUE);
                           if (text_value_4_temp)
                           {
                               axutil_stream_write(stream, env, text_value_4_temp, axutil_strlen(text_value_4_temp));
                               AXIS2_FREE(env->allocator, text_value_4_temp);
                           }
                           else
                           {
                               axutil_stream_write(stream, env, text_value_4, axutil_strlen(text_value_4));
                           }
                           
                           axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                           
                     
                     AXIS2_FREE(env->allocator,start_input_str);
                     AXIS2_FREE(env->allocator,end_input_str);
                 } 

                 

            return parent;
        }


        

               
                                              
               
            int64_t AXIS2_CALL
            adb_WsBulkLoadParams_get_operationInstanceId(
                    adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, (int64_t)0);
                    AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, (int64_t)0);
                  

                return _WsBulkLoadParams->property_operationInstanceId;
             }

               
                                             
               
            axis2_status_t AXIS2_CALL
            adb_WsBulkLoadParams_set_operationInstanceId(
                    adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                    const axutil_env_t *env,
                    const int64_t  arg_operationInstanceId)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, AXIS2_FAILURE);
                
                if(_WsBulkLoadParams->is_valid_operationInstanceId &&
                        arg_operationInstanceId == _WsBulkLoadParams->property_operationInstanceId)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsBulkLoadParams_reset_operationInstanceId(_WsBulkLoadParams, env);

                _WsBulkLoadParams->property_operationInstanceId = arg_operationInstanceId;
                        _WsBulkLoadParams->is_valid_operationInstanceId = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                              
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadParams_reset_operationInstanceId(
                   adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, AXIS2_FAILURE);
               

               _WsBulkLoadParams->is_valid_operationInstanceId = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                       
              
           axis2_bool_t AXIS2_CALL
           adb_WsBulkLoadParams_is_operationInstanceId_nil(
                   adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, AXIS2_TRUE);
               
               return !_WsBulkLoadParams->is_valid_operationInstanceId;
           }

              
                                                                           
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadParams_set_operationInstanceId_nil(
                   adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                   const axutil_env_t *env)
           {
               return adb_WsBulkLoadParams_reset_operationInstanceId(_WsBulkLoadParams, env);
           }

           

               
                                                  
               
            int64_t AXIS2_CALL
            adb_WsBulkLoadParams_get_lastOperationInstanceId(
                    adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, (int64_t)0);
                    AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, (int64_t)0);
                  

                return _WsBulkLoadParams->property_lastOperationInstanceId;
             }

               
                                                 
               
            axis2_status_t AXIS2_CALL
            adb_WsBulkLoadParams_set_lastOperationInstanceId(
                    adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                    const axutil_env_t *env,
                    const int64_t  arg_lastOperationInstanceId)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, AXIS2_FAILURE);
                
                if(_WsBulkLoadParams->is_valid_lastOperationInstanceId &&
                        arg_lastOperationInstanceId == _WsBulkLoadParams->property_lastOperationInstanceId)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsBulkLoadParams_reset_lastOperationInstanceId(_WsBulkLoadParams, env);

                _WsBulkLoadParams->property_lastOperationInstanceId = arg_lastOperationInstanceId;
                        _WsBulkLoadParams->is_valid_lastOperationInstanceId = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                                  
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadParams_reset_lastOperationInstanceId(
                   adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, AXIS2_FAILURE);
               

               _WsBulkLoadParams->is_valid_lastOperationInstanceId = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                           
              
           axis2_bool_t AXIS2_CALL
           adb_WsBulkLoadParams_is_lastOperationInstanceId_nil(
                   adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, AXIS2_TRUE);
               
               return !_WsBulkLoadParams->is_valid_lastOperationInstanceId;
           }

              
                                                                               
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadParams_set_lastOperationInstanceId_nil(
                   adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                   const axutil_env_t *env)
           {
               return adb_WsBulkLoadParams_reset_lastOperationInstanceId(_WsBulkLoadParams, env);
           }

           

               
                                     
               
            int64_t AXIS2_CALL
            adb_WsBulkLoadParams_get_bulkLoadId(
                    adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, (int64_t)0);
                    AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, (int64_t)0);
                  

                return _WsBulkLoadParams->property_bulkLoadId;
             }

               
                                    
               
            axis2_status_t AXIS2_CALL
            adb_WsBulkLoadParams_set_bulkLoadId(
                    adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                    const axutil_env_t *env,
                    const int64_t  arg_bulkLoadId)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, AXIS2_FAILURE);
                
                if(_WsBulkLoadParams->is_valid_bulkLoadId &&
                        arg_bulkLoadId == _WsBulkLoadParams->property_bulkLoadId)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsBulkLoadParams_reset_bulkLoadId(_WsBulkLoadParams, env);

                _WsBulkLoadParams->property_bulkLoadId = arg_bulkLoadId;
                        _WsBulkLoadParams->is_valid_bulkLoadId = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                     
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadParams_reset_bulkLoadId(
                   adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, AXIS2_FAILURE);
               

               _WsBulkLoadParams->is_valid_bulkLoadId = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                              
              
           axis2_bool_t AXIS2_CALL
           adb_WsBulkLoadParams_is_bulkLoadId_nil(
                   adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, AXIS2_TRUE);
               
               return !_WsBulkLoadParams->is_valid_bulkLoadId;
           }

              
                                                                  
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadParams_set_bulkLoadId_nil(
                   adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                   const axutil_env_t *env)
           {
               return adb_WsBulkLoadParams_reset_bulkLoadId(_WsBulkLoadParams, env);
           }

           

               
                                         
               
            axis2_char_t* AXIS2_CALL
            adb_WsBulkLoadParams_get_sourceFilePath(
                    adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, NULL);
                  

                return _WsBulkLoadParams->property_sourceFilePath;
             }

               
                                        
               
            axis2_status_t AXIS2_CALL
            adb_WsBulkLoadParams_set_sourceFilePath(
                    adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_sourceFilePath)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, AXIS2_FAILURE);
                
                if(_WsBulkLoadParams->is_valid_sourceFilePath &&
                        arg_sourceFilePath == _WsBulkLoadParams->property_sourceFilePath)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsBulkLoadParams_reset_sourceFilePath(_WsBulkLoadParams, env);

                
                if(NULL == arg_sourceFilePath)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsBulkLoadParams->property_sourceFilePath = (axis2_char_t *)axutil_strdup(env, arg_sourceFilePath);
                        if(NULL == _WsBulkLoadParams->property_sourceFilePath)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for sourceFilePath");
                            return AXIS2_FAILURE;
                        }
                        _WsBulkLoadParams->is_valid_sourceFilePath = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                         
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadParams_reset_sourceFilePath(
                   adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsBulkLoadParams->property_sourceFilePath != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsBulkLoadParams->property_sourceFilePath);
                     _WsBulkLoadParams->property_sourceFilePath = NULL;
                }
            
                
                
                _WsBulkLoadParams->is_valid_sourceFilePath = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                  
              
           axis2_bool_t AXIS2_CALL
           adb_WsBulkLoadParams_is_sourceFilePath_nil(
                   adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadParams, AXIS2_TRUE);
               
               return !_WsBulkLoadParams->is_valid_sourceFilePath;
           }

              
                                                                      
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadParams_set_sourceFilePath_nil(
                   adb_WsBulkLoadParams_t* _WsBulkLoadParams,
                   const axutil_env_t *env)
           {
               return adb_WsBulkLoadParams_reset_sourceFilePath(_WsBulkLoadParams, env);
           }

           

