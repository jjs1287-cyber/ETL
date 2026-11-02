

           
                            
          
                                                 
                                                                                                
           

        #include "adb_WsJsonError.h"
        
                  
                                                                            
                                     
                                                                                                     
                                         
                   
           


        struct adb_WsJsonError
        {
            int property_ErrorCode;

                
                axis2_bool_t is_valid_ErrorCode;


            axis2_char_t* property_Error;

                
                axis2_bool_t is_valid_Error;


            axis2_char_t* property_Warnings;

                
                axis2_bool_t is_valid_Warnings;


            
        };


                                                                                               
        

                axis2_status_t AXIS2_CALL
                adb_WsJsonError_set_ErrorCode_nil(
                        adb_WsJsonError_t* _WsJsonError,
                        const axutil_env_t *env);
            


                                                                                           
        adb_WsJsonError_t* AXIS2_CALL
        adb_WsJsonError_create(
            const axutil_env_t *env)
        {
            adb_WsJsonError_t *_WsJsonError = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _WsJsonError = (adb_WsJsonError_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_WsJsonError_t));

            if(NULL == _WsJsonError)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_WsJsonError, 0, sizeof(adb_WsJsonError_t));

            _WsJsonError->is_valid_ErrorCode  = AXIS2_FALSE;
            _WsJsonError->property_Error  = NULL;
                  _WsJsonError->is_valid_Error  = AXIS2_FALSE;
            _WsJsonError->property_Warnings  = NULL;
                  _WsJsonError->is_valid_Warnings  = AXIS2_FALSE;
            

            return _WsJsonError;
        }

        axis2_status_t AXIS2_CALL
        adb_WsJsonError_free (
                adb_WsJsonError_t* _WsJsonError,
                const axutil_env_t *env)
        {
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _WsJsonError, AXIS2_FAILURE);

            adb_WsJsonError_reset_ErrorCode(_WsJsonError, env);
            adb_WsJsonError_reset_Error(_WsJsonError, env);
            adb_WsJsonError_reset_Warnings(_WsJsonError, env);
            

            if(_WsJsonError)
            {
                AXIS2_FREE(env->allocator, _WsJsonError);
                _WsJsonError = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_WsJsonError_deserialize(
                adb_WsJsonError_t* _WsJsonError,
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
            AXIS2_PARAM_CHECK(env->error, _WsJsonError, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for WsJsonError : "
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
                                   
                                 element_qname = axutil_qname_create(env, "ErrorCode", "http://json.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsJsonError_set_ErrorCode(_WsJsonError, env,
                                                                   atoi(text_value));
                                      }
                                      
                                      else
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "NULL value is set to a non nillable element ErrorCode");
                                          status = AXIS2_FAILURE;
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for ErrorCode ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element ErrorCode missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "Error", "http://json.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsJsonError_set_Error(_WsJsonError, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for Error ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element Error missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "Warnings", "http://json.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsJsonError_set_Warnings(_WsJsonError, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for Warnings ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element Warnings missing");
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
          adb_WsJsonError_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_WsJsonError_declare_parent_namespaces(
                    adb_WsJsonError_t* _WsJsonError,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_WsJsonError_serialize(
                adb_WsJsonError_t* _WsJsonError,
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
                    
                    axis2_char_t *text_value_2;
                    axis2_char_t *text_value_2_temp;
                    
                    axis2_char_t *text_value_3;
                    axis2_char_t *text_value_3_temp;
                    
               axis2_char_t *start_input_str = NULL;
               axis2_char_t *end_input_str = NULL;
               unsigned int start_input_str_len = 0;
               unsigned int end_input_str_len = 0;
            
            
               axiom_data_source_t *data_source = NULL;
               axutil_stream_t *stream = NULL;

            

            AXIS2_ENV_CHECK(env, NULL);
            AXIS2_PARAM_CHECK(env->error, _WsJsonError, NULL);
            
            
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
                      

                   if (!_WsJsonError->is_valid_ErrorCode)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property ErrorCode");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("ErrorCode"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("ErrorCode")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                 
                        

                    
                    
                            sprintf(start_input_str, "<%s%sErrorCode>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sErrorCode>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                               sprintf (text_value_1, AXIS2_PRINTF_INT32_FORMAT_SPECIFIER, _WsJsonError->property_ErrorCode);
                             
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                           axutil_stream_write(stream, env, text_value_1, axutil_strlen(text_value_1));
                           
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
                      

                   if (!_WsJsonError->is_valid_Error)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("Error") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%sError xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("Error"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("Error")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                             
                        

                    
                    
                            sprintf(start_input_str, "<%s%sError>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sError>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_2 = _WsJsonError->property_Error;
                           
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
                      

                   if (!_WsJsonError->is_valid_Warnings)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("Warnings") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%sWarnings xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("Warnings"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("Warnings")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                
                        

                    
                    
                            sprintf(start_input_str, "<%s%sWarnings>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sWarnings>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_3 = _WsJsonError->property_Warnings;
                           
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                            
                           text_value_3_temp = axutil_xml_quote_string(env, text_value_3, AXIS2_TRUE);
                           if (text_value_3_temp)
                           {
                               axutil_stream_write(stream, env, text_value_3_temp, axutil_strlen(text_value_3_temp));
                               AXIS2_FREE(env->allocator, text_value_3_temp);
                           }
                           else
                           {
                               axutil_stream_write(stream, env, text_value_3, axutil_strlen(text_value_3));
                           }
                           
                           axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                           
                     
                     AXIS2_FREE(env->allocator,start_input_str);
                     AXIS2_FREE(env->allocator,end_input_str);
                 } 

                 

            return parent;
        }


        

               
                                    
               
            int AXIS2_CALL
            adb_WsJsonError_get_ErrorCode(
                    adb_WsJsonError_t* _WsJsonError,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, (int)0);
                    AXIS2_PARAM_CHECK(env->error, _WsJsonError, (int)0);
                  

                return _WsJsonError->property_ErrorCode;
             }

               
                                   
               
            axis2_status_t AXIS2_CALL
            adb_WsJsonError_set_ErrorCode(
                    adb_WsJsonError_t* _WsJsonError,
                    const axutil_env_t *env,
                    const int  arg_ErrorCode)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsJsonError, AXIS2_FAILURE);
                
                if(_WsJsonError->is_valid_ErrorCode &&
                        arg_ErrorCode == _WsJsonError->property_ErrorCode)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsJsonError_reset_ErrorCode(_WsJsonError, env);

                _WsJsonError->property_ErrorCode = arg_ErrorCode;
                        _WsJsonError->is_valid_ErrorCode = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                    
              
           axis2_status_t AXIS2_CALL
           adb_WsJsonError_reset_ErrorCode(
                   adb_WsJsonError_t* _WsJsonError,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsJsonError, AXIS2_FAILURE);
               

               _WsJsonError->is_valid_ErrorCode = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                             
              
           axis2_bool_t AXIS2_CALL
           adb_WsJsonError_is_ErrorCode_nil(
                   adb_WsJsonError_t* _WsJsonError,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsJsonError, AXIS2_TRUE);
               
               return !_WsJsonError->is_valid_ErrorCode;
           }

              
                                                                 
              
           axis2_status_t AXIS2_CALL
           adb_WsJsonError_set_ErrorCode_nil(
                   adb_WsJsonError_t* _WsJsonError,
                   const axutil_env_t *env)
           {
               return adb_WsJsonError_reset_ErrorCode(_WsJsonError, env);
           }

           

               
                                
               
            axis2_char_t* AXIS2_CALL
            adb_WsJsonError_get_Error(
                    adb_WsJsonError_t* _WsJsonError,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsJsonError, NULL);
                  

                return _WsJsonError->property_Error;
             }

               
                               
               
            axis2_status_t AXIS2_CALL
            adb_WsJsonError_set_Error(
                    adb_WsJsonError_t* _WsJsonError,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_Error)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsJsonError, AXIS2_FAILURE);
                
                if(_WsJsonError->is_valid_Error &&
                        arg_Error == _WsJsonError->property_Error)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsJsonError_reset_Error(_WsJsonError, env);

                
                if(NULL == arg_Error)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsJsonError->property_Error = (axis2_char_t *)axutil_strdup(env, arg_Error);
                        if(NULL == _WsJsonError->property_Error)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for Error");
                            return AXIS2_FAILURE;
                        }
                        _WsJsonError->is_valid_Error = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                
              
           axis2_status_t AXIS2_CALL
           adb_WsJsonError_reset_Error(
                   adb_WsJsonError_t* _WsJsonError,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsJsonError, AXIS2_FAILURE);
               

               
            
                
                if(_WsJsonError->property_Error != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsJsonError->property_Error);
                     _WsJsonError->property_Error = NULL;
                }
            
                
                
                _WsJsonError->is_valid_Error = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                         
              
           axis2_bool_t AXIS2_CALL
           adb_WsJsonError_is_Error_nil(
                   adb_WsJsonError_t* _WsJsonError,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsJsonError, AXIS2_TRUE);
               
               return !_WsJsonError->is_valid_Error;
           }

              
                                                             
              
           axis2_status_t AXIS2_CALL
           adb_WsJsonError_set_Error_nil(
                   adb_WsJsonError_t* _WsJsonError,
                   const axutil_env_t *env)
           {
               return adb_WsJsonError_reset_Error(_WsJsonError, env);
           }

           

               
                                   
               
            axis2_char_t* AXIS2_CALL
            adb_WsJsonError_get_Warnings(
                    adb_WsJsonError_t* _WsJsonError,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsJsonError, NULL);
                  

                return _WsJsonError->property_Warnings;
             }

               
                                  
               
            axis2_status_t AXIS2_CALL
            adb_WsJsonError_set_Warnings(
                    adb_WsJsonError_t* _WsJsonError,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_Warnings)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsJsonError, AXIS2_FAILURE);
                
                if(_WsJsonError->is_valid_Warnings &&
                        arg_Warnings == _WsJsonError->property_Warnings)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsJsonError_reset_Warnings(_WsJsonError, env);

                
                if(NULL == arg_Warnings)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsJsonError->property_Warnings = (axis2_char_t *)axutil_strdup(env, arg_Warnings);
                        if(NULL == _WsJsonError->property_Warnings)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for Warnings");
                            return AXIS2_FAILURE;
                        }
                        _WsJsonError->is_valid_Warnings = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                   
              
           axis2_status_t AXIS2_CALL
           adb_WsJsonError_reset_Warnings(
                   adb_WsJsonError_t* _WsJsonError,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsJsonError, AXIS2_FAILURE);
               

               
            
                
                if(_WsJsonError->property_Warnings != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsJsonError->property_Warnings);
                     _WsJsonError->property_Warnings = NULL;
                }
            
                
                
                _WsJsonError->is_valid_Warnings = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                            
              
           axis2_bool_t AXIS2_CALL
           adb_WsJsonError_is_Warnings_nil(
                   adb_WsJsonError_t* _WsJsonError,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsJsonError, AXIS2_TRUE);
               
               return !_WsJsonError->is_valid_Warnings;
           }

              
                                                                
              
           axis2_status_t AXIS2_CALL
           adb_WsJsonError_set_Warnings_nil(
                   adb_WsJsonError_t* _WsJsonError,
                   const axutil_env_t *env)
           {
               return adb_WsJsonError_reset_Warnings(_WsJsonError, env);
           }

           

