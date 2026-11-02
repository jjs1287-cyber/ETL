

           
                                        
          
                                                 
                                                                                                
           

        #include "adb_startNewBulkLoadSession.h"
        
                 
                                                                                                                                                    
                  
           


        struct adb_startNewBulkLoadSession
        {
            
                axutil_qname_t* qname;
            axis2_char_t* property_etlforgeUser;

                
                axis2_bool_t is_valid_etlforgeUser;


            axis2_char_t* property_md5Pwd;

                
                axis2_bool_t is_valid_md5Pwd;


            
        };


                                                                                               
        

                axis2_status_t AXIS2_CALL
                adb_startNewBulkLoadSession_set_etlforgeUser_nil(
                        adb_startNewBulkLoadSession_t* _startNewBulkLoadSession,
                        const axutil_env_t *env);
            

                axis2_status_t AXIS2_CALL
                adb_startNewBulkLoadSession_set_md5Pwd_nil(
                        adb_startNewBulkLoadSession_t* _startNewBulkLoadSession,
                        const axutil_env_t *env);
            


                                                                                           
        adb_startNewBulkLoadSession_t* AXIS2_CALL
        adb_startNewBulkLoadSession_create(
            const axutil_env_t *env)
        {
            adb_startNewBulkLoadSession_t *_startNewBulkLoadSession = NULL;
            
                axutil_qname_t* qname = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _startNewBulkLoadSession = (adb_startNewBulkLoadSession_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_startNewBulkLoadSession_t));

            if(NULL == _startNewBulkLoadSession)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_startNewBulkLoadSession, 0, sizeof(adb_startNewBulkLoadSession_t));

            _startNewBulkLoadSession->property_etlforgeUser  = NULL;
                  _startNewBulkLoadSession->is_valid_etlforgeUser  = AXIS2_FALSE;
            _startNewBulkLoadSession->property_md5Pwd  = NULL;
                  _startNewBulkLoadSession->is_valid_md5Pwd  = AXIS2_FALSE;
            
                  qname =  axutil_qname_create (env,
                        "startNewBulkLoadSession",
                        "http://bulkload.webservice.implementation.server.integration.etlforge.invalid",
                        NULL);
                _startNewBulkLoadSession->qname = qname;
            

            return _startNewBulkLoadSession;
        }

        axis2_status_t AXIS2_CALL
        adb_startNewBulkLoadSession_free (
                adb_startNewBulkLoadSession_t* _startNewBulkLoadSession,
                const axutil_env_t *env)
        {
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _startNewBulkLoadSession, AXIS2_FAILURE);

            adb_startNewBulkLoadSession_reset_etlforgeUser(_startNewBulkLoadSession, env);
            adb_startNewBulkLoadSession_reset_md5Pwd(_startNewBulkLoadSession, env);
            
              if(_startNewBulkLoadSession->qname)
              {
                  axutil_qname_free (_startNewBulkLoadSession->qname, env);
                  _startNewBulkLoadSession->qname = NULL;
              }
            

            if(_startNewBulkLoadSession)
            {
                AXIS2_FREE(env->allocator, _startNewBulkLoadSession);
                _startNewBulkLoadSession = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_startNewBulkLoadSession_deserialize(
                adb_startNewBulkLoadSession_t* _startNewBulkLoadSession,
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
            AXIS2_PARAM_CHECK(env->error, _startNewBulkLoadSession, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for startNewBulkLoadSession : "
                            "NULL elemenet can not be passed to deserialize");
                return AXIS2_FAILURE;
              }
              

                    current_element = (axiom_element_t *)axiom_node_get_data_element(parent, env);
                    qname = axiom_element_get_qname(current_element, env, parent);
                    if (axutil_qname_equals(qname, env, _startNewBulkLoadSession-> qname))
                    {
                        
                          first_node = axiom_node_get_first_child(parent, env);
                          
                    }
                    else
                    {
                        AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                              "Failed in building adb object for startNewBulkLoadSession : "
                              "Expected %s but returned %s",
                              axutil_qname_to_string(_startNewBulkLoadSession-> qname, env),
                              axutil_qname_to_string(qname, env));
                        
                        return AXIS2_FAILURE;
                    }
                    

                     
                       
                                                     
                        
                     
                     
                     
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
                                            status = adb_startNewBulkLoadSession_set_etlforgeUser(_startNewBulkLoadSession, env,
                                                               text_value);
                                      }
                                      
                                      else
                                      {
                                              
                                                                           
                                                                                   
                                               
                                                                                                                                           
                                                                                                                    
                                               
                                                                                                                                                 

                                            axiom_attribute_t *the_attri = NULL;
                                            axis2_char_t *attrib_text = NULL;
                                            axutil_hash_t *attribute_hash = NULL;

                                            attribute_hash = axiom_element_get_all_attributes(current_element, env);

                                            attrib_text = NULL;
                                            if(attribute_hash)
                                            {
                                                 axutil_hash_index_t *hi;
                                                 void *val;
                                                 const void *key;
                                        
                                                 for (hi = axutil_hash_first(attribute_hash, env); hi; hi = axutil_hash_next(env, hi)) 
                                                 {
                                                     axutil_hash_this(hi, &key, NULL, &val);
                                                     
                                                     if(strstr((axis2_char_t*)key, "nil|http://www.w3.org/2001/XMLSchema-instance"))
                                                     {
                                                         the_attri = (axiom_attribute_t*)val;
                                                         break;
                                                     }
                                                 }
                                            }

                                            if(the_attri)
                                            {
                                                attrib_text = axiom_attribute_get_value(the_attri, env);
                                            }
                                            else
                                            {
                                                                                                                                                                                            
                                                attrib_text = axiom_element_get_attribute_value_by_name(current_element, env, "nil");
                                            }

                                            if(attrib_text && 0 == axutil_strcmp(attrib_text, "1"))
                                            {
                                                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "NULL value is set to a non nillable element etlforgeUser");
                                                status = AXIS2_FAILURE;
                                            }
                                            else
                                            {
                                                                                                
                                                status = adb_startNewBulkLoadSession_set_etlforgeUser(_startNewBulkLoadSession, env,
                                                                   "");
                                            }
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
                                            status = adb_startNewBulkLoadSession_set_md5Pwd(_startNewBulkLoadSession, env,
                                                               text_value);
                                      }
                                      
                                      else
                                      {
                                              
                                                                           
                                                                                   
                                               
                                                                                                                                           
                                                                                                                    
                                               
                                                                                                                                                 

                                            axiom_attribute_t *the_attri = NULL;
                                            axis2_char_t *attrib_text = NULL;
                                            axutil_hash_t *attribute_hash = NULL;

                                            attribute_hash = axiom_element_get_all_attributes(current_element, env);

                                            attrib_text = NULL;
                                            if(attribute_hash)
                                            {
                                                 axutil_hash_index_t *hi;
                                                 void *val;
                                                 const void *key;
                                        
                                                 for (hi = axutil_hash_first(attribute_hash, env); hi; hi = axutil_hash_next(env, hi)) 
                                                 {
                                                     axutil_hash_this(hi, &key, NULL, &val);
                                                     
                                                     if(strstr((axis2_char_t*)key, "nil|http://www.w3.org/2001/XMLSchema-instance"))
                                                     {
                                                         the_attri = (axiom_attribute_t*)val;
                                                         break;
                                                     }
                                                 }
                                            }

                                            if(the_attri)
                                            {
                                                attrib_text = axiom_attribute_get_value(the_attri, env);
                                            }
                                            else
                                            {
                                                                                                                                                                                            
                                                attrib_text = axiom_element_get_attribute_value_by_name(current_element, env, "nil");
                                            }

                                            if(attrib_text && 0 == axutil_strcmp(attrib_text, "1"))
                                            {
                                                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "NULL value is set to a non nillable element md5Pwd");
                                                status = AXIS2_FAILURE;
                                            }
                                            else
                                            {
                                                                                                
                                                status = adb_startNewBulkLoadSession_set_md5Pwd(_startNewBulkLoadSession, env,
                                                                   "");
                                            }
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
                 
          return status;
       }

          axis2_bool_t AXIS2_CALL
          adb_startNewBulkLoadSession_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_startNewBulkLoadSession_declare_parent_namespaces(
                    adb_startNewBulkLoadSession_t* _startNewBulkLoadSession,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_startNewBulkLoadSession_serialize(
                adb_startNewBulkLoadSession_t* _startNewBulkLoadSession,
                const axutil_env_t *env, axiom_node_t *parent, axiom_element_t *parent_element, int parent_tag_closed, axutil_hash_t *namespaces, int *next_ns_index)
        {
            
            
         
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

             
                int next_ns_index_value = 0;
            

            AXIS2_ENV_CHECK(env, NULL);
            AXIS2_PARAM_CHECK(env->error, _startNewBulkLoadSession, NULL);
            
             
                    namespaces = axutil_hash_make(env);
                    next_ns_index = &next_ns_index_value;
                     
                           ns1 = axiom_namespace_create (env,
                                             "http://bulkload.webservice.implementation.server.integration.etlforge.invalid",
                                             "n"); 
                           axutil_hash_set(namespaces, "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, axutil_strdup(env, "n"));
                       
                     
                    parent_element = axiom_element_create (env, NULL, "startNewBulkLoadSession", ns1 , &parent);
                    
                    
                    axiom_element_set_namespace(parent_element, env, ns1, parent);


            
                    data_source = axiom_data_source_create(env, parent, &current_node);
                    stream = axiom_data_source_get_stream(data_source, env);
                  
                       if(!(p_prefix = (axis2_char_t*)axutil_hash_get(namespaces, "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING)))
                       {
                           p_prefix = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof (axis2_char_t) * ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT);
                           sprintf(p_prefix, "n%d", (*next_ns_index)++);
                           axutil_hash_set(namespaces, "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, p_prefix);
                           
                           axiom_element_declare_namespace_assume_param_ownership(parent_element, env, axiom_namespace_create (env,
                                            "http://bulkload.webservice.implementation.server.integration.etlforge.invalid",
                                            p_prefix));
                       }
                      

                   if (!_startNewBulkLoadSession->is_valid_etlforgeUser)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property etlforgeUser");
                            return NULL;
                          
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
                    
                           text_value_1 = _startNewBulkLoadSession->property_etlforgeUser;
                           
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
                      

                   if (!_startNewBulkLoadSession->is_valid_md5Pwd)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property md5Pwd");
                            return NULL;
                          
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
                    
                           text_value_2 = _startNewBulkLoadSession->property_md5Pwd;
                           
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

                 
                   if(namespaces)
                   {
                       axutil_hash_index_t *hi;
                       void *val;
                       for (hi = axutil_hash_first(namespaces, env); hi; hi = axutil_hash_next(env, hi)) 
                       {
                           axutil_hash_this(hi, NULL, NULL, &val);
                           AXIS2_FREE(env->allocator, val);
                       }
                       axutil_hash_free(namespaces, env);
                   }
                

            return parent;
        }


        

               
                                       
               
            axis2_char_t* AXIS2_CALL
            adb_startNewBulkLoadSession_get_etlforgeUser(
                    adb_startNewBulkLoadSession_t* _startNewBulkLoadSession,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _startNewBulkLoadSession, NULL);
                  

                return _startNewBulkLoadSession->property_etlforgeUser;
             }

               
                                      
               
            axis2_status_t AXIS2_CALL
            adb_startNewBulkLoadSession_set_etlforgeUser(
                    adb_startNewBulkLoadSession_t* _startNewBulkLoadSession,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_etlforgeUser)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _startNewBulkLoadSession, AXIS2_FAILURE);
                
                if(_startNewBulkLoadSession->is_valid_etlforgeUser &&
                        arg_etlforgeUser == _startNewBulkLoadSession->property_etlforgeUser)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                
                  if(NULL == arg_etlforgeUser)
                  {
                      AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "etlforgeUser is being set to NULL, but it is not a nullable element");
                      return AXIS2_FAILURE;
                  }
                adb_startNewBulkLoadSession_reset_etlforgeUser(_startNewBulkLoadSession, env);

                
                if(NULL == arg_etlforgeUser)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _startNewBulkLoadSession->property_etlforgeUser = (axis2_char_t *)axutil_strdup(env, arg_etlforgeUser);
                        if(NULL == _startNewBulkLoadSession->property_etlforgeUser)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for etlforgeUser");
                            return AXIS2_FAILURE;
                        }
                        _startNewBulkLoadSession->is_valid_etlforgeUser = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                       
              
           axis2_status_t AXIS2_CALL
           adb_startNewBulkLoadSession_reset_etlforgeUser(
                   adb_startNewBulkLoadSession_t* _startNewBulkLoadSession,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _startNewBulkLoadSession, AXIS2_FAILURE);
               

               
            
                
                if(_startNewBulkLoadSession->property_etlforgeUser != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _startNewBulkLoadSession->property_etlforgeUser);
                     _startNewBulkLoadSession->property_etlforgeUser = NULL;
                }
            
                
                
                _startNewBulkLoadSession->is_valid_etlforgeUser = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                
              
           axis2_bool_t AXIS2_CALL
           adb_startNewBulkLoadSession_is_etlforgeUser_nil(
                   adb_startNewBulkLoadSession_t* _startNewBulkLoadSession,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _startNewBulkLoadSession, AXIS2_TRUE);
               
               return !_startNewBulkLoadSession->is_valid_etlforgeUser;
           }

              
                                                                    
              
           axis2_status_t AXIS2_CALL
           adb_startNewBulkLoadSession_set_etlforgeUser_nil(
                   adb_startNewBulkLoadSession_t* _startNewBulkLoadSession,
                   const axutil_env_t *env)
           {
               return adb_startNewBulkLoadSession_reset_etlforgeUser(_startNewBulkLoadSession, env);
           }

           

               
                                 
               
            axis2_char_t* AXIS2_CALL
            adb_startNewBulkLoadSession_get_md5Pwd(
                    adb_startNewBulkLoadSession_t* _startNewBulkLoadSession,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _startNewBulkLoadSession, NULL);
                  

                return _startNewBulkLoadSession->property_md5Pwd;
             }

               
                                
               
            axis2_status_t AXIS2_CALL
            adb_startNewBulkLoadSession_set_md5Pwd(
                    adb_startNewBulkLoadSession_t* _startNewBulkLoadSession,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_md5Pwd)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _startNewBulkLoadSession, AXIS2_FAILURE);
                
                if(_startNewBulkLoadSession->is_valid_md5Pwd &&
                        arg_md5Pwd == _startNewBulkLoadSession->property_md5Pwd)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                
                  if(NULL == arg_md5Pwd)
                  {
                      AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "md5Pwd is being set to NULL, but it is not a nullable element");
                      return AXIS2_FAILURE;
                  }
                adb_startNewBulkLoadSession_reset_md5Pwd(_startNewBulkLoadSession, env);

                
                if(NULL == arg_md5Pwd)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _startNewBulkLoadSession->property_md5Pwd = (axis2_char_t *)axutil_strdup(env, arg_md5Pwd);
                        if(NULL == _startNewBulkLoadSession->property_md5Pwd)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for md5Pwd");
                            return AXIS2_FAILURE;
                        }
                        _startNewBulkLoadSession->is_valid_md5Pwd = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                 
              
           axis2_status_t AXIS2_CALL
           adb_startNewBulkLoadSession_reset_md5Pwd(
                   adb_startNewBulkLoadSession_t* _startNewBulkLoadSession,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _startNewBulkLoadSession, AXIS2_FAILURE);
               

               
            
                
                if(_startNewBulkLoadSession->property_md5Pwd != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _startNewBulkLoadSession->property_md5Pwd);
                     _startNewBulkLoadSession->property_md5Pwd = NULL;
                }
            
                
                
                _startNewBulkLoadSession->is_valid_md5Pwd = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                          
              
           axis2_bool_t AXIS2_CALL
           adb_startNewBulkLoadSession_is_md5Pwd_nil(
                   adb_startNewBulkLoadSession_t* _startNewBulkLoadSession,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _startNewBulkLoadSession, AXIS2_TRUE);
               
               return !_startNewBulkLoadSession->is_valid_md5Pwd;
           }

              
                                                              
              
           axis2_status_t AXIS2_CALL
           adb_startNewBulkLoadSession_set_md5Pwd_nil(
                   adb_startNewBulkLoadSession_t* _startNewBulkLoadSession,
                   const axutil_env_t *env)
           {
               return adb_startNewBulkLoadSession_reset_md5Pwd(_startNewBulkLoadSession, env);
           }

           

