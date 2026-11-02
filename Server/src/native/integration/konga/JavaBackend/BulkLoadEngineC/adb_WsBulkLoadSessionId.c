

           
                                    
          
                                                 
                                                                                                
           

        #include "adb_WsBulkLoadSessionId.h"
        
                  
                                                                            
                                             
                                                                                                                
                                         
                   
           


        struct adb_WsBulkLoadSessionId
        {
            axis2_char_t* property_etlforgeUser;

                
                axis2_bool_t is_valid_etlforgeUser;


            axis2_char_t* property_md5Pwd;

                
                axis2_bool_t is_valid_md5Pwd;


            axis2_char_t* property_sessionGuid;

                
                axis2_bool_t is_valid_sessionGuid;


            
        };


                                                                                               
        


                                                                                           
        adb_WsBulkLoadSessionId_t* AXIS2_CALL
        adb_WsBulkLoadSessionId_create(
            const axutil_env_t *env)
        {
            adb_WsBulkLoadSessionId_t *_WsBulkLoadSessionId = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _WsBulkLoadSessionId = (adb_WsBulkLoadSessionId_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_WsBulkLoadSessionId_t));

            if(NULL == _WsBulkLoadSessionId)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_WsBulkLoadSessionId, 0, sizeof(adb_WsBulkLoadSessionId_t));

            _WsBulkLoadSessionId->property_etlforgeUser  = NULL;
                  _WsBulkLoadSessionId->is_valid_etlforgeUser  = AXIS2_FALSE;
            _WsBulkLoadSessionId->property_md5Pwd  = NULL;
                  _WsBulkLoadSessionId->is_valid_md5Pwd  = AXIS2_FALSE;
            _WsBulkLoadSessionId->property_sessionGuid  = NULL;
                  _WsBulkLoadSessionId->is_valid_sessionGuid  = AXIS2_FALSE;
            

            return _WsBulkLoadSessionId;
        }

        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadSessionId_free (
                adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                const axutil_env_t *env)
        {
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _WsBulkLoadSessionId, AXIS2_FAILURE);

            adb_WsBulkLoadSessionId_reset_etlforgeUser(_WsBulkLoadSessionId, env);
            adb_WsBulkLoadSessionId_reset_md5Pwd(_WsBulkLoadSessionId, env);
            adb_WsBulkLoadSessionId_reset_sessionGuid(_WsBulkLoadSessionId, env);
            

            if(_WsBulkLoadSessionId)
            {
                AXIS2_FREE(env->allocator, _WsBulkLoadSessionId);
                _WsBulkLoadSessionId = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadSessionId_deserialize(
                adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
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
            AXIS2_PARAM_CHECK(env->error, _WsBulkLoadSessionId, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for WsBulkLoadSessionId : "
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
                                   
                                 element_qname = axutil_qname_create(env, "etlforgeUser", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsBulkLoadSessionId_set_etlforgeUser(_WsBulkLoadSessionId, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for etlforgeUser ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element etlforgeUser missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "md5Pwd", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsBulkLoadSessionId_set_md5Pwd(_WsBulkLoadSessionId, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for md5Pwd ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element md5Pwd missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "sessionGuid", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsBulkLoadSessionId_set_sessionGuid(_WsBulkLoadSessionId, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for sessionGuid ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element sessionGuid missing");
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
          adb_WsBulkLoadSessionId_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_WsBulkLoadSessionId_declare_parent_namespaces(
                    adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_WsBulkLoadSessionId_serialize(
                adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
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
            AXIS2_PARAM_CHECK(env->error, _WsBulkLoadSessionId, NULL);
            
            
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
                      

                   if (!_WsBulkLoadSessionId->is_valid_etlforgeUser)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("etlforgeUser") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%setlforgeUser xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("etlforgeUser"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("etlforgeUser")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                    
                        

                    
                    
                            sprintf(start_input_str, "<%s%setlforgeUser>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%setlforgeUser>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_1 = _WsBulkLoadSessionId->property_etlforgeUser;
                           
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
                      

                   if (!_WsBulkLoadSessionId->is_valid_md5Pwd)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("md5Pwd") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%smd5Pwd xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("md5Pwd"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("md5Pwd")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                              
                        

                    
                    
                            sprintf(start_input_str, "<%s%smd5Pwd>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%smd5Pwd>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_2 = _WsBulkLoadSessionId->property_md5Pwd;
                           
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

                 
                       if(!(p_prefix = (axis2_char_t*)axutil_hash_get(namespaces, "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING)))
                       {
                           p_prefix = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof (axis2_char_t) * ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT);
                           sprintf(p_prefix, "n%d", (*next_ns_index)++);
                           axutil_hash_set(namespaces, "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, p_prefix);
                           
                           axiom_element_declare_namespace_assume_param_ownership(parent_element, env, axiom_namespace_create (env,
                                            "http://bulkload.webservice.implementation.server.integration.etlforge.invalid",
                                            p_prefix));
                       }
                      

                   if (!_WsBulkLoadSessionId->is_valid_sessionGuid)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("sessionGuid") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%ssessionGuid xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("sessionGuid"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("sessionGuid")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                   
                        

                    
                    
                            sprintf(start_input_str, "<%s%ssessionGuid>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%ssessionGuid>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_3 = _WsBulkLoadSessionId->property_sessionGuid;
                           
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
            adb_WsBulkLoadSessionId_get_etlforgeUser(
                    adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsBulkLoadSessionId, NULL);
                  

                return _WsBulkLoadSessionId->property_etlforgeUser;
             }

               
                                      
               
            axis2_status_t AXIS2_CALL
            adb_WsBulkLoadSessionId_set_etlforgeUser(
                    adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_etlforgeUser)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsBulkLoadSessionId, AXIS2_FAILURE);
                
                if(_WsBulkLoadSessionId->is_valid_etlforgeUser &&
                        arg_etlforgeUser == _WsBulkLoadSessionId->property_etlforgeUser)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsBulkLoadSessionId_reset_etlforgeUser(_WsBulkLoadSessionId, env);

                
                if(NULL == arg_etlforgeUser)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsBulkLoadSessionId->property_etlforgeUser = (axis2_char_t *)axutil_strdup(env, arg_etlforgeUser);
                        if(NULL == _WsBulkLoadSessionId->property_etlforgeUser)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for etlforgeUser");
                            return AXIS2_FAILURE;
                        }
                        _WsBulkLoadSessionId->is_valid_etlforgeUser = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                       
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadSessionId_reset_etlforgeUser(
                   adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadSessionId, AXIS2_FAILURE);
               

               
            
                
                if(_WsBulkLoadSessionId->property_etlforgeUser != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsBulkLoadSessionId->property_etlforgeUser);
                     _WsBulkLoadSessionId->property_etlforgeUser = NULL;
                }
            
                
                
                _WsBulkLoadSessionId->is_valid_etlforgeUser = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                
              
           axis2_bool_t AXIS2_CALL
           adb_WsBulkLoadSessionId_is_etlforgeUser_nil(
                   adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadSessionId, AXIS2_TRUE);
               
               return !_WsBulkLoadSessionId->is_valid_etlforgeUser;
           }

              
                                                                    
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadSessionId_set_etlforgeUser_nil(
                   adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                   const axutil_env_t *env)
           {
               return adb_WsBulkLoadSessionId_reset_etlforgeUser(_WsBulkLoadSessionId, env);
           }

           

               
                                 
               
            axis2_char_t* AXIS2_CALL
            adb_WsBulkLoadSessionId_get_md5Pwd(
                    adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsBulkLoadSessionId, NULL);
                  

                return _WsBulkLoadSessionId->property_md5Pwd;
             }

               
                                
               
            axis2_status_t AXIS2_CALL
            adb_WsBulkLoadSessionId_set_md5Pwd(
                    adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_md5Pwd)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsBulkLoadSessionId, AXIS2_FAILURE);
                
                if(_WsBulkLoadSessionId->is_valid_md5Pwd &&
                        arg_md5Pwd == _WsBulkLoadSessionId->property_md5Pwd)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsBulkLoadSessionId_reset_md5Pwd(_WsBulkLoadSessionId, env);

                
                if(NULL == arg_md5Pwd)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsBulkLoadSessionId->property_md5Pwd = (axis2_char_t *)axutil_strdup(env, arg_md5Pwd);
                        if(NULL == _WsBulkLoadSessionId->property_md5Pwd)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for md5Pwd");
                            return AXIS2_FAILURE;
                        }
                        _WsBulkLoadSessionId->is_valid_md5Pwd = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                 
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadSessionId_reset_md5Pwd(
                   adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadSessionId, AXIS2_FAILURE);
               

               
            
                
                if(_WsBulkLoadSessionId->property_md5Pwd != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsBulkLoadSessionId->property_md5Pwd);
                     _WsBulkLoadSessionId->property_md5Pwd = NULL;
                }
            
                
                
                _WsBulkLoadSessionId->is_valid_md5Pwd = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                          
              
           axis2_bool_t AXIS2_CALL
           adb_WsBulkLoadSessionId_is_md5Pwd_nil(
                   adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadSessionId, AXIS2_TRUE);
               
               return !_WsBulkLoadSessionId->is_valid_md5Pwd;
           }

              
                                                              
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadSessionId_set_md5Pwd_nil(
                   adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                   const axutil_env_t *env)
           {
               return adb_WsBulkLoadSessionId_reset_md5Pwd(_WsBulkLoadSessionId, env);
           }

           

               
                                      
               
            axis2_char_t* AXIS2_CALL
            adb_WsBulkLoadSessionId_get_sessionGuid(
                    adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsBulkLoadSessionId, NULL);
                  

                return _WsBulkLoadSessionId->property_sessionGuid;
             }

               
                                     
               
            axis2_status_t AXIS2_CALL
            adb_WsBulkLoadSessionId_set_sessionGuid(
                    adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_sessionGuid)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsBulkLoadSessionId, AXIS2_FAILURE);
                
                if(_WsBulkLoadSessionId->is_valid_sessionGuid &&
                        arg_sessionGuid == _WsBulkLoadSessionId->property_sessionGuid)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsBulkLoadSessionId_reset_sessionGuid(_WsBulkLoadSessionId, env);

                
                if(NULL == arg_sessionGuid)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsBulkLoadSessionId->property_sessionGuid = (axis2_char_t *)axutil_strdup(env, arg_sessionGuid);
                        if(NULL == _WsBulkLoadSessionId->property_sessionGuid)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for sessionGuid");
                            return AXIS2_FAILURE;
                        }
                        _WsBulkLoadSessionId->is_valid_sessionGuid = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                      
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadSessionId_reset_sessionGuid(
                   adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadSessionId, AXIS2_FAILURE);
               

               
            
                
                if(_WsBulkLoadSessionId->property_sessionGuid != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsBulkLoadSessionId->property_sessionGuid);
                     _WsBulkLoadSessionId->property_sessionGuid = NULL;
                }
            
                
                
                _WsBulkLoadSessionId->is_valid_sessionGuid = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                               
              
           axis2_bool_t AXIS2_CALL
           adb_WsBulkLoadSessionId_is_sessionGuid_nil(
                   adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadSessionId, AXIS2_TRUE);
               
               return !_WsBulkLoadSessionId->is_valid_sessionGuid;
           }

              
                                                                   
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadSessionId_set_sessionGuid_nil(
                   adb_WsBulkLoadSessionId_t* _WsBulkLoadSessionId,
                   const axutil_env_t *env)
           {
               return adb_WsBulkLoadSessionId_reset_sessionGuid(_WsBulkLoadSessionId, env);
           }

           

