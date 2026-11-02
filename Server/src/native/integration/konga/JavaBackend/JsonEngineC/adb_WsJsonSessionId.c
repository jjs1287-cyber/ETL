

           
                                
          
                                                 
                                                                                                
           

        #include "adb_WsJsonSessionId.h"
        
                  
                                                                            
                                         
                                                                                                     
                                         
                   
           


        struct adb_WsJsonSessionId
        {
            axis2_char_t* property_ETLForgeUser;

                
                axis2_bool_t is_valid_ETLForgeUser;


            axis2_char_t* property_Md5Pwd;

                
                axis2_bool_t is_valid_Md5Pwd;


            axis2_char_t* property_SessionGuid;

                
                axis2_bool_t is_valid_SessionGuid;


            
        };


                                                                                               
        


                                                                                           
        adb_WsJsonSessionId_t* AXIS2_CALL
        adb_WsJsonSessionId_create(
            const axutil_env_t *env)
        {
            adb_WsJsonSessionId_t *_WsJsonSessionId = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _WsJsonSessionId = (adb_WsJsonSessionId_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_WsJsonSessionId_t));

            if(NULL == _WsJsonSessionId)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_WsJsonSessionId, 0, sizeof(adb_WsJsonSessionId_t));

            _WsJsonSessionId->property_ETLForgeUser  = NULL;
                  _WsJsonSessionId->is_valid_ETLForgeUser  = AXIS2_FALSE;
            _WsJsonSessionId->property_Md5Pwd  = NULL;
                  _WsJsonSessionId->is_valid_Md5Pwd  = AXIS2_FALSE;
            _WsJsonSessionId->property_SessionGuid  = NULL;
                  _WsJsonSessionId->is_valid_SessionGuid  = AXIS2_FALSE;
            

            return _WsJsonSessionId;
        }

        axis2_status_t AXIS2_CALL
        adb_WsJsonSessionId_free (
                adb_WsJsonSessionId_t* _WsJsonSessionId,
                const axutil_env_t *env)
        {
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _WsJsonSessionId, AXIS2_FAILURE);

            adb_WsJsonSessionId_reset_ETLForgeUser(_WsJsonSessionId, env);
            adb_WsJsonSessionId_reset_Md5Pwd(_WsJsonSessionId, env);
            adb_WsJsonSessionId_reset_SessionGuid(_WsJsonSessionId, env);
            

            if(_WsJsonSessionId)
            {
                AXIS2_FREE(env->allocator, _WsJsonSessionId);
                _WsJsonSessionId = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_WsJsonSessionId_deserialize(
                adb_WsJsonSessionId_t* _WsJsonSessionId,
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
            AXIS2_PARAM_CHECK(env->error, _WsJsonSessionId, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for WsJsonSessionId : "
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
                                   
                                 element_qname = axutil_qname_create(env, "ETLForgeUser", "http://json.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsJsonSessionId_set_ETLForgeUser(_WsJsonSessionId, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for ETLForgeUser ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element ETLForgeUser missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "Md5Pwd", "http://json.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsJsonSessionId_set_Md5Pwd(_WsJsonSessionId, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for Md5Pwd ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element Md5Pwd missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "SessionGuid", "http://json.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsJsonSessionId_set_SessionGuid(_WsJsonSessionId, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for SessionGuid ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element SessionGuid missing");
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
          adb_WsJsonSessionId_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_WsJsonSessionId_declare_parent_namespaces(
                    adb_WsJsonSessionId_t* _WsJsonSessionId,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_WsJsonSessionId_serialize(
                adb_WsJsonSessionId_t* _WsJsonSessionId,
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
                    
                    axis2_char_t *text_value_3;
                    axis2_char_t *text_value_3_temp;
                    
               axis2_char_t *start_input_str = NULL;
               axis2_char_t *end_input_str = NULL;
               unsigned int start_input_str_len = 0;
               unsigned int end_input_str_len = 0;
            
            
               axiom_data_source_t *data_source = NULL;
               axutil_stream_t *stream = NULL;

            

            AXIS2_ENV_CHECK(env, NULL);
            AXIS2_PARAM_CHECK(env->error, _WsJsonSessionId, NULL);
            
            
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
                      

                   if (!_WsJsonSessionId->is_valid_ETLForgeUser)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("ETLForgeUser") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%sETLForgeUser xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("ETLForgeUser"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("ETLForgeUser")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                    
                        

                    
                    
                            sprintf(start_input_str, "<%s%sETLForgeUser>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sETLForgeUser>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_1 = _WsJsonSessionId->property_ETLForgeUser;
                           
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
                      

                   if (!_WsJsonSessionId->is_valid_Md5Pwd)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("Md5Pwd") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%sMd5Pwd xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("Md5Pwd"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("Md5Pwd")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                              
                        

                    
                    
                            sprintf(start_input_str, "<%s%sMd5Pwd>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sMd5Pwd>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_2 = _WsJsonSessionId->property_Md5Pwd;
                           
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
                      

                   if (!_WsJsonSessionId->is_valid_SessionGuid)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("SessionGuid") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%sSessionGuid xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("SessionGuid"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("SessionGuid")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                   
                        

                    
                    
                            sprintf(start_input_str, "<%s%sSessionGuid>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sSessionGuid>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_3 = _WsJsonSessionId->property_SessionGuid;
                           
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


        

               
                                       
               
            axis2_char_t* AXIS2_CALL
            adb_WsJsonSessionId_get_ETLForgeUser(
                    adb_WsJsonSessionId_t* _WsJsonSessionId,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsJsonSessionId, NULL);
                  

                return _WsJsonSessionId->property_ETLForgeUser;
             }

               
                                      
               
            axis2_status_t AXIS2_CALL
            adb_WsJsonSessionId_set_ETLForgeUser(
                    adb_WsJsonSessionId_t* _WsJsonSessionId,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_ETLForgeUser)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsJsonSessionId, AXIS2_FAILURE);
                
                if(_WsJsonSessionId->is_valid_ETLForgeUser &&
                        arg_ETLForgeUser == _WsJsonSessionId->property_ETLForgeUser)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsJsonSessionId_reset_ETLForgeUser(_WsJsonSessionId, env);

                
                if(NULL == arg_ETLForgeUser)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsJsonSessionId->property_ETLForgeUser = (axis2_char_t *)axutil_strdup(env, arg_ETLForgeUser);
                        if(NULL == _WsJsonSessionId->property_ETLForgeUser)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for ETLForgeUser");
                            return AXIS2_FAILURE;
                        }
                        _WsJsonSessionId->is_valid_ETLForgeUser = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                       
              
           axis2_status_t AXIS2_CALL
           adb_WsJsonSessionId_reset_ETLForgeUser(
                   adb_WsJsonSessionId_t* _WsJsonSessionId,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsJsonSessionId, AXIS2_FAILURE);
               

               
            
                
                if(_WsJsonSessionId->property_ETLForgeUser != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsJsonSessionId->property_ETLForgeUser);
                     _WsJsonSessionId->property_ETLForgeUser = NULL;
                }
            
                
                
                _WsJsonSessionId->is_valid_ETLForgeUser = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                
              
           axis2_bool_t AXIS2_CALL
           adb_WsJsonSessionId_is_ETLForgeUser_nil(
                   adb_WsJsonSessionId_t* _WsJsonSessionId,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsJsonSessionId, AXIS2_TRUE);
               
               return !_WsJsonSessionId->is_valid_ETLForgeUser;
           }

              
                                                                    
              
           axis2_status_t AXIS2_CALL
           adb_WsJsonSessionId_set_ETLForgeUser_nil(
                   adb_WsJsonSessionId_t* _WsJsonSessionId,
                   const axutil_env_t *env)
           {
               return adb_WsJsonSessionId_reset_ETLForgeUser(_WsJsonSessionId, env);
           }

           

               
                                 
               
            axis2_char_t* AXIS2_CALL
            adb_WsJsonSessionId_get_Md5Pwd(
                    adb_WsJsonSessionId_t* _WsJsonSessionId,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsJsonSessionId, NULL);
                  

                return _WsJsonSessionId->property_Md5Pwd;
             }

               
                                
               
            axis2_status_t AXIS2_CALL
            adb_WsJsonSessionId_set_Md5Pwd(
                    adb_WsJsonSessionId_t* _WsJsonSessionId,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_Md5Pwd)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsJsonSessionId, AXIS2_FAILURE);
                
                if(_WsJsonSessionId->is_valid_Md5Pwd &&
                        arg_Md5Pwd == _WsJsonSessionId->property_Md5Pwd)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsJsonSessionId_reset_Md5Pwd(_WsJsonSessionId, env);

                
                if(NULL == arg_Md5Pwd)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsJsonSessionId->property_Md5Pwd = (axis2_char_t *)axutil_strdup(env, arg_Md5Pwd);
                        if(NULL == _WsJsonSessionId->property_Md5Pwd)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for Md5Pwd");
                            return AXIS2_FAILURE;
                        }
                        _WsJsonSessionId->is_valid_Md5Pwd = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                 
              
           axis2_status_t AXIS2_CALL
           adb_WsJsonSessionId_reset_Md5Pwd(
                   adb_WsJsonSessionId_t* _WsJsonSessionId,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsJsonSessionId, AXIS2_FAILURE);
               

               
            
                
                if(_WsJsonSessionId->property_Md5Pwd != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsJsonSessionId->property_Md5Pwd);
                     _WsJsonSessionId->property_Md5Pwd = NULL;
                }
            
                
                
                _WsJsonSessionId->is_valid_Md5Pwd = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                          
              
           axis2_bool_t AXIS2_CALL
           adb_WsJsonSessionId_is_Md5Pwd_nil(
                   adb_WsJsonSessionId_t* _WsJsonSessionId,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsJsonSessionId, AXIS2_TRUE);
               
               return !_WsJsonSessionId->is_valid_Md5Pwd;
           }

              
                                                              
              
           axis2_status_t AXIS2_CALL
           adb_WsJsonSessionId_set_Md5Pwd_nil(
                   adb_WsJsonSessionId_t* _WsJsonSessionId,
                   const axutil_env_t *env)
           {
               return adb_WsJsonSessionId_reset_Md5Pwd(_WsJsonSessionId, env);
           }

           

               
                                      
               
            axis2_char_t* AXIS2_CALL
            adb_WsJsonSessionId_get_SessionGuid(
                    adb_WsJsonSessionId_t* _WsJsonSessionId,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsJsonSessionId, NULL);
                  

                return _WsJsonSessionId->property_SessionGuid;
             }

               
                                     
               
            axis2_status_t AXIS2_CALL
            adb_WsJsonSessionId_set_SessionGuid(
                    adb_WsJsonSessionId_t* _WsJsonSessionId,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_SessionGuid)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsJsonSessionId, AXIS2_FAILURE);
                
                if(_WsJsonSessionId->is_valid_SessionGuid &&
                        arg_SessionGuid == _WsJsonSessionId->property_SessionGuid)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsJsonSessionId_reset_SessionGuid(_WsJsonSessionId, env);

                
                if(NULL == arg_SessionGuid)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsJsonSessionId->property_SessionGuid = (axis2_char_t *)axutil_strdup(env, arg_SessionGuid);
                        if(NULL == _WsJsonSessionId->property_SessionGuid)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for SessionGuid");
                            return AXIS2_FAILURE;
                        }
                        _WsJsonSessionId->is_valid_SessionGuid = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                      
              
           axis2_status_t AXIS2_CALL
           adb_WsJsonSessionId_reset_SessionGuid(
                   adb_WsJsonSessionId_t* _WsJsonSessionId,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsJsonSessionId, AXIS2_FAILURE);
               

               
            
                
                if(_WsJsonSessionId->property_SessionGuid != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsJsonSessionId->property_SessionGuid);
                     _WsJsonSessionId->property_SessionGuid = NULL;
                }
            
                
                
                _WsJsonSessionId->is_valid_SessionGuid = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                               
              
           axis2_bool_t AXIS2_CALL
           adb_WsJsonSessionId_is_SessionGuid_nil(
                   adb_WsJsonSessionId_t* _WsJsonSessionId,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsJsonSessionId, AXIS2_TRUE);
               
               return !_WsJsonSessionId->is_valid_SessionGuid;
           }

              
                                                                   
              
           axis2_status_t AXIS2_CALL
           adb_WsJsonSessionId_set_SessionGuid_nil(
                   adb_WsJsonSessionId_t* _WsJsonSessionId,
                   const axutil_env_t *env)
           {
               return adb_WsJsonSessionId_reset_SessionGuid(_WsJsonSessionId, env);
           }

           

