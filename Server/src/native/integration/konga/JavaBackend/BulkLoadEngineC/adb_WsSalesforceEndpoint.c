

           
                                     
          
                                                 
                                                                                                
           

        #include "adb_WsSalesforceEndpoint.h"
        
                  
                                                                            
                                              
                                                                                                                
                                         
                   
           


        struct adb_WsSalesforceEndpoint
        {
            axis2_char_t* property_url;

                
                axis2_bool_t is_valid_url;


            axis2_char_t* property_sessionId;

                
                axis2_bool_t is_valid_sessionId;


            
        };


                                                                                               
        


                                                                                           
        adb_WsSalesforceEndpoint_t* AXIS2_CALL
        adb_WsSalesforceEndpoint_create(
            const axutil_env_t *env)
        {
            adb_WsSalesforceEndpoint_t *_WsSalesforceEndpoint = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _WsSalesforceEndpoint = (adb_WsSalesforceEndpoint_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_WsSalesforceEndpoint_t));

            if(NULL == _WsSalesforceEndpoint)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_WsSalesforceEndpoint, 0, sizeof(adb_WsSalesforceEndpoint_t));

            _WsSalesforceEndpoint->property_url  = NULL;
                  _WsSalesforceEndpoint->is_valid_url  = AXIS2_FALSE;
            _WsSalesforceEndpoint->property_sessionId  = NULL;
                  _WsSalesforceEndpoint->is_valid_sessionId  = AXIS2_FALSE;
            

            return _WsSalesforceEndpoint;
        }

        axis2_status_t AXIS2_CALL
        adb_WsSalesforceEndpoint_free (
                adb_WsSalesforceEndpoint_t* _WsSalesforceEndpoint,
                const axutil_env_t *env)
        {
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _WsSalesforceEndpoint, AXIS2_FAILURE);

            adb_WsSalesforceEndpoint_reset_url(_WsSalesforceEndpoint, env);
            adb_WsSalesforceEndpoint_reset_sessionId(_WsSalesforceEndpoint, env);
            

            if(_WsSalesforceEndpoint)
            {
                AXIS2_FREE(env->allocator, _WsSalesforceEndpoint);
                _WsSalesforceEndpoint = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_WsSalesforceEndpoint_deserialize(
                adb_WsSalesforceEndpoint_t* _WsSalesforceEndpoint,
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
            AXIS2_PARAM_CHECK(env->error, _WsSalesforceEndpoint, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for WsSalesforceEndpoint : "
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
                                   
                                 element_qname = axutil_qname_create(env, "url", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsSalesforceEndpoint_set_url(_WsSalesforceEndpoint, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for url ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element url missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "sessionId", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsSalesforceEndpoint_set_sessionId(_WsSalesforceEndpoint, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for sessionId ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element sessionId missing");
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
          adb_WsSalesforceEndpoint_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_WsSalesforceEndpoint_declare_parent_namespaces(
                    adb_WsSalesforceEndpoint_t* _WsSalesforceEndpoint,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_WsSalesforceEndpoint_serialize(
                adb_WsSalesforceEndpoint_t* _WsSalesforceEndpoint,
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
            
                    axis2_char_t *text_value_1;
                    axis2_char_t *text_value_1_temp;
                    
                    axis2_char_t *text_value_2;
                    axis2_char_t *text_value_2_temp;
                    
               axis2_char_t *start_input_str = NULL;
               axis2_char_t *end_input_str = NULL;
               unsigned int start_input_str_len = 0;
               unsigned int end_input_str_len = 0;
            
            
               axiom_data_source_t *data_source = NULL;
               axutil_stream_t *stream = NULL;

            

            AXIS2_ENV_CHECK(env, NULL);
            AXIS2_PARAM_CHECK(env->error, _WsSalesforceEndpoint, NULL);
            
            
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
                      

                   if (!_WsSalesforceEndpoint->is_valid_url)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("url") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%surl xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("url"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("url")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                           
                        

                    
                    
                            sprintf(start_input_str, "<%s%surl>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%surl>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_1 = _WsSalesforceEndpoint->property_url;
                           
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                            
                           text_value_1_temp = axutil_xml_quote_string(env, text_value_1, AXIS2_TRUE);
                           if (text_value_1_temp)
                           {
                               axutil_stream_write(stream, env, text_value_1_temp, axutil_strlen(text_value_1_temp));
                               AXIS2_FREE(env->allocator, text_value_1_temp);
                           }
                           else
                           {
                               axutil_stream_write(stream, env, text_value_1, axutil_strlen(text_value_1));
                           }
                           
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
                      

                   if (!_WsSalesforceEndpoint->is_valid_sessionId)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("sessionId") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%ssessionId xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("sessionId"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("sessionId")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                 
                        

                    
                    
                            sprintf(start_input_str, "<%s%ssessionId>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%ssessionId>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_2 = _WsSalesforceEndpoint->property_sessionId;
                           
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                            
                           text_value_2_temp = axutil_xml_quote_string(env, text_value_2, AXIS2_TRUE);
                           if (text_value_2_temp)
                           {
                               axutil_stream_write(stream, env, text_value_2_temp, axutil_strlen(text_value_2_temp));
                               AXIS2_FREE(env->allocator, text_value_2_temp);
                           }
                           else
                           {
                               axutil_stream_write(stream, env, text_value_2, axutil_strlen(text_value_2));
                           }
                           
                           axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                           
                     
                     AXIS2_FREE(env->allocator,start_input_str);
                     AXIS2_FREE(env->allocator,end_input_str);
                 } 

                 

            return parent;
        }


        

               
                              
               
            axis2_char_t* AXIS2_CALL
            adb_WsSalesforceEndpoint_get_url(
                    adb_WsSalesforceEndpoint_t* _WsSalesforceEndpoint,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsSalesforceEndpoint, NULL);
                  

                return _WsSalesforceEndpoint->property_url;
             }

               
                             
               
            axis2_status_t AXIS2_CALL
            adb_WsSalesforceEndpoint_set_url(
                    adb_WsSalesforceEndpoint_t* _WsSalesforceEndpoint,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_url)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsSalesforceEndpoint, AXIS2_FAILURE);
                
                if(_WsSalesforceEndpoint->is_valid_url &&
                        arg_url == _WsSalesforceEndpoint->property_url)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsSalesforceEndpoint_reset_url(_WsSalesforceEndpoint, env);

                
                if(NULL == arg_url)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsSalesforceEndpoint->property_url = (axis2_char_t *)axutil_strdup(env, arg_url);
                        if(NULL == _WsSalesforceEndpoint->property_url)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for url");
                            return AXIS2_FAILURE;
                        }
                        _WsSalesforceEndpoint->is_valid_url = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                              
              
           axis2_status_t AXIS2_CALL
           adb_WsSalesforceEndpoint_reset_url(
                   adb_WsSalesforceEndpoint_t* _WsSalesforceEndpoint,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsSalesforceEndpoint, AXIS2_FAILURE);
               

               
            
                
                if(_WsSalesforceEndpoint->property_url != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsSalesforceEndpoint->property_url);
                     _WsSalesforceEndpoint->property_url = NULL;
                }
            
                
                
                _WsSalesforceEndpoint->is_valid_url = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                       
              
           axis2_bool_t AXIS2_CALL
           adb_WsSalesforceEndpoint_is_url_nil(
                   adb_WsSalesforceEndpoint_t* _WsSalesforceEndpoint,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsSalesforceEndpoint, AXIS2_TRUE);
               
               return !_WsSalesforceEndpoint->is_valid_url;
           }

              
                                                           
              
           axis2_status_t AXIS2_CALL
           adb_WsSalesforceEndpoint_set_url_nil(
                   adb_WsSalesforceEndpoint_t* _WsSalesforceEndpoint,
                   const axutil_env_t *env)
           {
               return adb_WsSalesforceEndpoint_reset_url(_WsSalesforceEndpoint, env);
           }

           

               
                                    
               
            axis2_char_t* AXIS2_CALL
            adb_WsSalesforceEndpoint_get_sessionId(
                    adb_WsSalesforceEndpoint_t* _WsSalesforceEndpoint,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsSalesforceEndpoint, NULL);
                  

                return _WsSalesforceEndpoint->property_sessionId;
             }

               
                                   
               
            axis2_status_t AXIS2_CALL
            adb_WsSalesforceEndpoint_set_sessionId(
                    adb_WsSalesforceEndpoint_t* _WsSalesforceEndpoint,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_sessionId)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsSalesforceEndpoint, AXIS2_FAILURE);
                
                if(_WsSalesforceEndpoint->is_valid_sessionId &&
                        arg_sessionId == _WsSalesforceEndpoint->property_sessionId)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsSalesforceEndpoint_reset_sessionId(_WsSalesforceEndpoint, env);

                
                if(NULL == arg_sessionId)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsSalesforceEndpoint->property_sessionId = (axis2_char_t *)axutil_strdup(env, arg_sessionId);
                        if(NULL == _WsSalesforceEndpoint->property_sessionId)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for sessionId");
                            return AXIS2_FAILURE;
                        }
                        _WsSalesforceEndpoint->is_valid_sessionId = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                    
              
           axis2_status_t AXIS2_CALL
           adb_WsSalesforceEndpoint_reset_sessionId(
                   adb_WsSalesforceEndpoint_t* _WsSalesforceEndpoint,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsSalesforceEndpoint, AXIS2_FAILURE);
               

               
            
                
                if(_WsSalesforceEndpoint->property_sessionId != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsSalesforceEndpoint->property_sessionId);
                     _WsSalesforceEndpoint->property_sessionId = NULL;
                }
            
                
                
                _WsSalesforceEndpoint->is_valid_sessionId = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                             
              
           axis2_bool_t AXIS2_CALL
           adb_WsSalesforceEndpoint_is_sessionId_nil(
                   adb_WsSalesforceEndpoint_t* _WsSalesforceEndpoint,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsSalesforceEndpoint, AXIS2_TRUE);
               
               return !_WsSalesforceEndpoint->is_valid_sessionId;
           }

              
                                                                 
              
           axis2_status_t AXIS2_CALL
           adb_WsSalesforceEndpoint_set_sessionId_nil(
                   adb_WsSalesforceEndpoint_t* _WsSalesforceEndpoint,
                   const axutil_env_t *env)
           {
               return adb_WsSalesforceEndpoint_reset_sessionId(_WsSalesforceEndpoint, env);
           }

           

