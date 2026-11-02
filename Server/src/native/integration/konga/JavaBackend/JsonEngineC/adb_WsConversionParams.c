

           
                                   
          
                                                 
                                                                                                
           

        #include "adb_WsConversionParams.h"
        
                  
                                                                            
                                            
                                                                                                     
                                         
                   
           


        struct adb_WsConversionParams
        {
            axis2_char_t* property_JsonFilePath;

                
                axis2_bool_t is_valid_JsonFilePath;


            axis2_char_t* property_XmlFilePath;

                
                axis2_bool_t is_valid_XmlFilePath;


            int64_t property_TransformationId;

                
                axis2_bool_t is_valid_TransformationId;


            
        };


                                                                                               
        

                axis2_status_t AXIS2_CALL
                adb_WsConversionParams_set_TransformationId_nil(
                        adb_WsConversionParams_t* _WsConversionParams,
                        const axutil_env_t *env);
            


                                                                                           
        adb_WsConversionParams_t* AXIS2_CALL
        adb_WsConversionParams_create(
            const axutil_env_t *env)
        {
            adb_WsConversionParams_t *_WsConversionParams = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _WsConversionParams = (adb_WsConversionParams_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_WsConversionParams_t));

            if(NULL == _WsConversionParams)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_WsConversionParams, 0, sizeof(adb_WsConversionParams_t));

            _WsConversionParams->property_JsonFilePath  = NULL;
                  _WsConversionParams->is_valid_JsonFilePath  = AXIS2_FALSE;
            _WsConversionParams->property_XmlFilePath  = NULL;
                  _WsConversionParams->is_valid_XmlFilePath  = AXIS2_FALSE;
            _WsConversionParams->is_valid_TransformationId  = AXIS2_FALSE;
            

            return _WsConversionParams;
        }

        axis2_status_t AXIS2_CALL
        adb_WsConversionParams_free (
                adb_WsConversionParams_t* _WsConversionParams,
                const axutil_env_t *env)
        {
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _WsConversionParams, AXIS2_FAILURE);

            adb_WsConversionParams_reset_JsonFilePath(_WsConversionParams, env);
            adb_WsConversionParams_reset_XmlFilePath(_WsConversionParams, env);
            adb_WsConversionParams_reset_TransformationId(_WsConversionParams, env);
            

            if(_WsConversionParams)
            {
                AXIS2_FREE(env->allocator, _WsConversionParams);
                _WsConversionParams = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_WsConversionParams_deserialize(
                adb_WsConversionParams_t* _WsConversionParams,
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
            AXIS2_PARAM_CHECK(env->error, _WsConversionParams, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for WsConversionParams : "
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
                                   
                                 element_qname = axutil_qname_create(env, "JsonFilePath", "http://json.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsConversionParams_set_JsonFilePath(_WsConversionParams, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for JsonFilePath ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element JsonFilePath missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "XmlFilePath", "http://json.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsConversionParams_set_XmlFilePath(_WsConversionParams, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for XmlFilePath ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element XmlFilePath missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "TransformationId", "http://json.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsConversionParams_set_TransformationId(_WsConversionParams, env,
                                                                   axutil_strtol(text_value, (char**)NULL, 0));
                                      }
                                      
                                      else
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "NULL value is set to a non nillable element TransformationId");
                                          status = AXIS2_FAILURE;
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for TransformationId ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element TransformationId missing");
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
          adb_WsConversionParams_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_WsConversionParams_declare_parent_namespaces(
                    adb_WsConversionParams_t* _WsConversionParams,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_WsConversionParams_serialize(
                adb_WsConversionParams_t* _WsConversionParams,
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
                    
                    axis2_char_t text_value_3[64];
                    
               axis2_char_t *start_input_str = NULL;
               axis2_char_t *end_input_str = NULL;
               unsigned int start_input_str_len = 0;
               unsigned int end_input_str_len = 0;
            
            
               axiom_data_source_t *data_source = NULL;
               axutil_stream_t *stream = NULL;

            

            AXIS2_ENV_CHECK(env, NULL);
            AXIS2_PARAM_CHECK(env->error, _WsConversionParams, NULL);
            
            
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
            
                       if(!(p_prefix = (axis2_char_t*)axutil_hash_get(namespaces, "http://json.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING)))
                       {
                           p_prefix = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof (axis2_char_t) * ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT);
                           sprintf(p_prefix, "n%d", (*next_ns_index)++);
                           axutil_hash_set(namespaces, "http://json.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, p_prefix);
                           
                           axiom_element_declare_namespace_assume_param_ownership(parent_element, env, axiom_namespace_create (env,
                                            "http://json.webservice.backend.server.integration.etlforge.invalid",
                                            p_prefix));
                       }
                      

                   if (!_WsConversionParams->is_valid_JsonFilePath)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("JsonFilePath") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%sJsonFilePath xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("JsonFilePath"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("JsonFilePath")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                    
                        

                    
                    
                            sprintf(start_input_str, "<%s%sJsonFilePath>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sJsonFilePath>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_1 = _WsConversionParams->property_JsonFilePath;
                           
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

                 
                       if(!(p_prefix = (axis2_char_t*)axutil_hash_get(namespaces, "http://json.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING)))
                       {
                           p_prefix = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof (axis2_char_t) * ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT);
                           sprintf(p_prefix, "n%d", (*next_ns_index)++);
                           axutil_hash_set(namespaces, "http://json.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, p_prefix);
                           
                           axiom_element_declare_namespace_assume_param_ownership(parent_element, env, axiom_namespace_create (env,
                                            "http://json.webservice.backend.server.integration.etlforge.invalid",
                                            p_prefix));
                       }
                      

                   if (!_WsConversionParams->is_valid_XmlFilePath)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("XmlFilePath") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%sXmlFilePath xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("XmlFilePath"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("XmlFilePath")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                   
                        

                    
                    
                            sprintf(start_input_str, "<%s%sXmlFilePath>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sXmlFilePath>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_2 = _WsConversionParams->property_XmlFilePath;
                           
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

                 
                       if(!(p_prefix = (axis2_char_t*)axutil_hash_get(namespaces, "http://json.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING)))
                       {
                           p_prefix = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof (axis2_char_t) * ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT);
                           sprintf(p_prefix, "n%d", (*next_ns_index)++);
                           axutil_hash_set(namespaces, "http://json.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, p_prefix);
                           
                           axiom_element_declare_namespace_assume_param_ownership(parent_element, env, axiom_namespace_create (env,
                                            "http://json.webservice.backend.server.integration.etlforge.invalid",
                                            p_prefix));
                       }
                      

                   if (!_WsConversionParams->is_valid_TransformationId)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property TransformationId");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("TransformationId"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("TransformationId")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                        
                        

                    
                    
                            sprintf(start_input_str, "<%s%sTransformationId>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sTransformationId>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                               sprintf (text_value_3, AXIS2_PRINTF_INT64_FORMAT_SPECIFIER, (int64_t) _WsConversionParams->property_TransformationId);
                             
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                           axutil_stream_write(stream, env, text_value_3, axutil_strlen(text_value_3));
                           
                           axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                           
                     
                     AXIS2_FREE(env->allocator,start_input_str);
                     AXIS2_FREE(env->allocator,end_input_str);
                 } 

                 

            return parent;
        }


        

               
                                       
               
            axis2_char_t* AXIS2_CALL
            adb_WsConversionParams_get_JsonFilePath(
                    adb_WsConversionParams_t* _WsConversionParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsConversionParams, NULL);
                  

                return _WsConversionParams->property_JsonFilePath;
             }

               
                                      
               
            axis2_status_t AXIS2_CALL
            adb_WsConversionParams_set_JsonFilePath(
                    adb_WsConversionParams_t* _WsConversionParams,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_JsonFilePath)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsConversionParams, AXIS2_FAILURE);
                
                if(_WsConversionParams->is_valid_JsonFilePath &&
                        arg_JsonFilePath == _WsConversionParams->property_JsonFilePath)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsConversionParams_reset_JsonFilePath(_WsConversionParams, env);

                
                if(NULL == arg_JsonFilePath)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsConversionParams->property_JsonFilePath = (axis2_char_t *)axutil_strdup(env, arg_JsonFilePath);
                        if(NULL == _WsConversionParams->property_JsonFilePath)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for JsonFilePath");
                            return AXIS2_FAILURE;
                        }
                        _WsConversionParams->is_valid_JsonFilePath = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                       
              
           axis2_status_t AXIS2_CALL
           adb_WsConversionParams_reset_JsonFilePath(
                   adb_WsConversionParams_t* _WsConversionParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsConversionParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsConversionParams->property_JsonFilePath != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsConversionParams->property_JsonFilePath);
                     _WsConversionParams->property_JsonFilePath = NULL;
                }
            
                
                
                _WsConversionParams->is_valid_JsonFilePath = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                
              
           axis2_bool_t AXIS2_CALL
           adb_WsConversionParams_is_JsonFilePath_nil(
                   adb_WsConversionParams_t* _WsConversionParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsConversionParams, AXIS2_TRUE);
               
               return !_WsConversionParams->is_valid_JsonFilePath;
           }

              
                                                                    
              
           axis2_status_t AXIS2_CALL
           adb_WsConversionParams_set_JsonFilePath_nil(
                   adb_WsConversionParams_t* _WsConversionParams,
                   const axutil_env_t *env)
           {
               return adb_WsConversionParams_reset_JsonFilePath(_WsConversionParams, env);
           }

           

               
                                      
               
            axis2_char_t* AXIS2_CALL
            adb_WsConversionParams_get_XmlFilePath(
                    adb_WsConversionParams_t* _WsConversionParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsConversionParams, NULL);
                  

                return _WsConversionParams->property_XmlFilePath;
             }

               
                                     
               
            axis2_status_t AXIS2_CALL
            adb_WsConversionParams_set_XmlFilePath(
                    adb_WsConversionParams_t* _WsConversionParams,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_XmlFilePath)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsConversionParams, AXIS2_FAILURE);
                
                if(_WsConversionParams->is_valid_XmlFilePath &&
                        arg_XmlFilePath == _WsConversionParams->property_XmlFilePath)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsConversionParams_reset_XmlFilePath(_WsConversionParams, env);

                
                if(NULL == arg_XmlFilePath)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsConversionParams->property_XmlFilePath = (axis2_char_t *)axutil_strdup(env, arg_XmlFilePath);
                        if(NULL == _WsConversionParams->property_XmlFilePath)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for XmlFilePath");
                            return AXIS2_FAILURE;
                        }
                        _WsConversionParams->is_valid_XmlFilePath = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                      
              
           axis2_status_t AXIS2_CALL
           adb_WsConversionParams_reset_XmlFilePath(
                   adb_WsConversionParams_t* _WsConversionParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsConversionParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsConversionParams->property_XmlFilePath != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsConversionParams->property_XmlFilePath);
                     _WsConversionParams->property_XmlFilePath = NULL;
                }
            
                
                
                _WsConversionParams->is_valid_XmlFilePath = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                               
              
           axis2_bool_t AXIS2_CALL
           adb_WsConversionParams_is_XmlFilePath_nil(
                   adb_WsConversionParams_t* _WsConversionParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsConversionParams, AXIS2_TRUE);
               
               return !_WsConversionParams->is_valid_XmlFilePath;
           }

              
                                                                   
              
           axis2_status_t AXIS2_CALL
           adb_WsConversionParams_set_XmlFilePath_nil(
                   adb_WsConversionParams_t* _WsConversionParams,
                   const axutil_env_t *env)
           {
               return adb_WsConversionParams_reset_XmlFilePath(_WsConversionParams, env);
           }

           

               
                                           
               
            int64_t AXIS2_CALL
            adb_WsConversionParams_get_TransformationId(
                    adb_WsConversionParams_t* _WsConversionParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, (int64_t)0);
                    AXIS2_PARAM_CHECK(env->error, _WsConversionParams, (int64_t)0);
                  

                return _WsConversionParams->property_TransformationId;
             }

               
                                          
               
            axis2_status_t AXIS2_CALL
            adb_WsConversionParams_set_TransformationId(
                    adb_WsConversionParams_t* _WsConversionParams,
                    const axutil_env_t *env,
                    const int64_t  arg_TransformationId)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsConversionParams, AXIS2_FAILURE);
                
                if(_WsConversionParams->is_valid_TransformationId &&
                        arg_TransformationId == _WsConversionParams->property_TransformationId)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsConversionParams_reset_TransformationId(_WsConversionParams, env);

                _WsConversionParams->property_TransformationId = arg_TransformationId;
                        _WsConversionParams->is_valid_TransformationId = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                           
              
           axis2_status_t AXIS2_CALL
           adb_WsConversionParams_reset_TransformationId(
                   adb_WsConversionParams_t* _WsConversionParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsConversionParams, AXIS2_FAILURE);
               

               _WsConversionParams->is_valid_TransformationId = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                    
              
           axis2_bool_t AXIS2_CALL
           adb_WsConversionParams_is_TransformationId_nil(
                   adb_WsConversionParams_t* _WsConversionParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsConversionParams, AXIS2_TRUE);
               
               return !_WsConversionParams->is_valid_TransformationId;
           }

              
                                                                        
              
           axis2_status_t AXIS2_CALL
           adb_WsConversionParams_set_TransformationId_nil(
                   adb_WsConversionParams_t* _WsConversionParams,
                   const axutil_env_t *env)
           {
               return adb_WsConversionParams_reset_TransformationId(_WsConversionParams, env);
           }

           

