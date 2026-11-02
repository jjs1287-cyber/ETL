

           
                           
          
                                                 
                                                                                                
           

        #include "adb_readSource.h"
        
                 
                                                                                                                            
                  
           


        struct adb_readSource
        {
            
                axutil_qname_t* qname;
            adb_WsJdbcSessionId_t* property_in0;

                
                axis2_bool_t is_valid_in0;


            adb_WsReadSourceParams_t* property_in1;

                
                axis2_bool_t is_valid_in1;


            
        };


                                                                                               
        

                axis2_status_t AXIS2_CALL
                adb_readSource_set_in0_nil(
                        adb_readSource_t* _readSource,
                        const axutil_env_t *env);
            

                axis2_status_t AXIS2_CALL
                adb_readSource_set_in1_nil(
                        adb_readSource_t* _readSource,
                        const axutil_env_t *env);
            


                                                                                           
        adb_readSource_t* AXIS2_CALL
        adb_readSource_create(
            const axutil_env_t *env)
        {
            adb_readSource_t *_readSource = NULL;
            
                axutil_qname_t* qname = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _readSource = (adb_readSource_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_readSource_t));

            if(NULL == _readSource)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_readSource, 0, sizeof(adb_readSource_t));

            _readSource->property_in0  = NULL;
                  _readSource->is_valid_in0  = AXIS2_FALSE;
            _readSource->property_in1  = NULL;
                  _readSource->is_valid_in1  = AXIS2_FALSE;
            
                  qname =  axutil_qname_create (env,
                        "readSource",
                        "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
                        NULL);
                _readSource->qname = qname;
            

            return _readSource;
        }

        axis2_status_t AXIS2_CALL
        adb_readSource_free (
                adb_readSource_t* _readSource,
                const axutil_env_t *env)
        {
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _readSource, AXIS2_FAILURE);

            adb_readSource_reset_in0(_readSource, env);
            adb_readSource_reset_in1(_readSource, env);
            
              if(_readSource->qname)
              {
                  axutil_qname_free (_readSource->qname, env);
                  _readSource->qname = NULL;
              }
            

            if(_readSource)
            {
                AXIS2_FREE(env->allocator, _readSource);
                _readSource = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_readSource_deserialize(
                adb_readSource_t* _readSource,
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
            AXIS2_PARAM_CHECK(env->error, _readSource, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for readSource : "
                            "NULL elemenet can not be passed to deserialize");
                return AXIS2_FAILURE;
              }
              

                    current_element = (axiom_element_t *)axiom_node_get_data_element(parent, env);
                    qname = axiom_element_get_qname(current_element, env, parent);
                    if (axutil_qname_equals(qname, env, _readSource-> qname))
                    {
                        
                          first_node = axiom_node_get_first_child(parent, env);
                          
                    }
                    else
                    {
                        AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                              "Failed in building adb object for readSource : "
                              "Expected %s but returned %s",
                              axutil_qname_to_string(_readSource-> qname, env),
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
                                   
                                 element_qname = axutil_qname_create(env, "in0", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

                           if (adb_WsJdbcSessionId_is_particle() ||  
                                (current_node   && current_element && (axutil_qname_equals(element_qname, env, qname))))
                           {
                              if( current_node   && current_element && (axutil_qname_equals(element_qname, env, qname)))
                              {
                                is_early_node_valid = AXIS2_TRUE;
                              }
                              
                                 
                                      element = (void*)adb_WsJdbcSessionId_create(env);

                                      status =  adb_WsJdbcSessionId_deserialize((adb_WsJdbcSessionId_t*)element,
                                                                            env, &current_node, &is_early_node_valid, AXIS2_FALSE);
                                      if(AXIS2_FAILURE == status)
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in building adb object for element in0");
                                      }
                                      else
                                      {
                                          status = adb_readSource_set_in0(_readSource, env,
                                                                   (adb_WsJdbcSessionId_t*)element);
                                      }
                                    
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for in0 ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element in0 missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "in1", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

                           if (adb_WsReadSourceParams_is_particle() ||  
                                (current_node   && current_element && (axutil_qname_equals(element_qname, env, qname))))
                           {
                              if( current_node   && current_element && (axutil_qname_equals(element_qname, env, qname)))
                              {
                                is_early_node_valid = AXIS2_TRUE;
                              }
                              
                                 
                                      element = (void*)adb_WsReadSourceParams_create(env);

                                      status =  adb_WsReadSourceParams_deserialize((adb_WsReadSourceParams_t*)element,
                                                                            env, &current_node, &is_early_node_valid, AXIS2_FALSE);
                                      if(AXIS2_FAILURE == status)
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in building adb object for element in1");
                                      }
                                      else
                                      {
                                          status = adb_readSource_set_in1(_readSource, env,
                                                                   (adb_WsReadSourceParams_t*)element);
                                      }
                                    
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for in1 ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element in1 missing");
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
          adb_readSource_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_readSource_declare_parent_namespaces(
                    adb_readSource_t* _readSource,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_readSource_serialize(
                adb_readSource_t* _readSource,
                const axutil_env_t *env, axiom_node_t *parent, axiom_element_t *parent_element, int parent_tag_closed, axutil_hash_t *namespaces, int *next_ns_index)
        {
            
            
         
         axiom_node_t *current_node = NULL;
         int tag_closed = 0;

         
         
                axiom_namespace_t *ns1 = NULL;

                axis2_char_t *qname_uri = NULL;
                axis2_char_t *qname_prefix = NULL;
                axis2_char_t *p_prefix = NULL;
                axis2_bool_t ns_already_defined;
            
                    axis2_char_t text_value_1[64];
                    
                    axis2_char_t text_value_2[64];
                    
               axis2_char_t *start_input_str = NULL;
               axis2_char_t *end_input_str = NULL;
               unsigned int start_input_str_len = 0;
               unsigned int end_input_str_len = 0;
            
            
               axiom_data_source_t *data_source = NULL;
               axutil_stream_t *stream = NULL;

             
                int next_ns_index_value = 0;
            

            AXIS2_ENV_CHECK(env, NULL);
            AXIS2_PARAM_CHECK(env->error, _readSource, NULL);
            
             
                    namespaces = axutil_hash_make(env);
                    next_ns_index = &next_ns_index_value;
                     
                           ns1 = axiom_namespace_create (env,
                                             "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
                                             "n"); 
                           axutil_hash_set(namespaces, "http://jdbc.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, axutil_strdup(env, "n"));
                       
                     
                    parent_element = axiom_element_create (env, NULL, "readSource", ns1 , &parent);
                    
                    
                    axiom_element_set_namespace(parent_element, env, ns1, parent);


            
                    data_source = axiom_data_source_create(env, parent, &current_node);
                    stream = axiom_data_source_get_stream(data_source, env);
                  
                       if(!(p_prefix = (axis2_char_t*)axutil_hash_get(namespaces, "http://jdbc.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING)))
                       {
                           p_prefix = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof (axis2_char_t) * ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT);
                           sprintf(p_prefix, "n%d", (*next_ns_index)++);
                           axutil_hash_set(namespaces, "http://jdbc.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, p_prefix);
                           
                           axiom_element_declare_namespace_assume_param_ownership(parent_element, env, axiom_namespace_create (env,
                                            "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
                                            p_prefix));
                       }
                      

                   if (!_readSource->is_valid_in0)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property in0");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("in0"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("in0")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                           
                        

                    
                    
                            sprintf(start_input_str, "<%s%sin0",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":""); 
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sin0>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                     
                            if(!adb_WsJdbcSessionId_is_particle())
                            {
                                axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                            }
                            
                            adb_WsJdbcSessionId_serialize(_readSource->property_in0, 
                                                                                 env, current_node, parent_element,
                                                                                 adb_WsJdbcSessionId_is_particle() || AXIS2_FALSE, namespaces, next_ns_index);
                            
                            if(!adb_WsJdbcSessionId_is_particle())
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
                      

                   if (!_readSource->is_valid_in1)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property in1");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("in1"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("in1")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                           
                        

                    
                    
                            sprintf(start_input_str, "<%s%sin1",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":""); 
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sin1>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                     
                            if(!adb_WsReadSourceParams_is_particle())
                            {
                                axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                            }
                            
                            adb_WsReadSourceParams_serialize(_readSource->property_in1, 
                                                                                 env, current_node, parent_element,
                                                                                 adb_WsReadSourceParams_is_particle() || AXIS2_FALSE, namespaces, next_ns_index);
                            
                            if(!adb_WsReadSourceParams_is_particle())
                            {
                                axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                            }
                            
                     
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


        

               
                              
               
            adb_WsJdbcSessionId_t* AXIS2_CALL
            adb_readSource_get_in0(
                    adb_readSource_t* _readSource,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _readSource, NULL);
                  

                return _readSource->property_in0;
             }

               
                             
               
            axis2_status_t AXIS2_CALL
            adb_readSource_set_in0(
                    adb_readSource_t* _readSource,
                    const axutil_env_t *env,
                    adb_WsJdbcSessionId_t*  arg_in0)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _readSource, AXIS2_FAILURE);
                
                if(_readSource->is_valid_in0 &&
                        arg_in0 == _readSource->property_in0)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                
                  if(NULL == arg_in0)
                  {
                      AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "in0 is being set to NULL, but it is not a nullable element");
                      return AXIS2_FAILURE;
                  }
                adb_readSource_reset_in0(_readSource, env);

                
                if(NULL == arg_in0)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _readSource->property_in0 = arg_in0;
                        _readSource->is_valid_in0 = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                              
              
           axis2_status_t AXIS2_CALL
           adb_readSource_reset_in0(
                   adb_readSource_t* _readSource,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _readSource, AXIS2_FAILURE);
               

               
            
                
                if(_readSource->property_in0 != NULL)
                {
                   
                   
                        adb_WsJdbcSessionId_free(_readSource->property_in0, env);
                     _readSource->property_in0 = NULL;
                }
            
                
                
                _readSource->is_valid_in0 = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                       
              
           axis2_bool_t AXIS2_CALL
           adb_readSource_is_in0_nil(
                   adb_readSource_t* _readSource,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _readSource, AXIS2_TRUE);
               
               return !_readSource->is_valid_in0;
           }

              
                                                           
              
           axis2_status_t AXIS2_CALL
           adb_readSource_set_in0_nil(
                   adb_readSource_t* _readSource,
                   const axutil_env_t *env)
           {
               return adb_readSource_reset_in0(_readSource, env);
           }

           

               
                              
               
            adb_WsReadSourceParams_t* AXIS2_CALL
            adb_readSource_get_in1(
                    adb_readSource_t* _readSource,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _readSource, NULL);
                  

                return _readSource->property_in1;
             }

               
                             
               
            axis2_status_t AXIS2_CALL
            adb_readSource_set_in1(
                    adb_readSource_t* _readSource,
                    const axutil_env_t *env,
                    adb_WsReadSourceParams_t*  arg_in1)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _readSource, AXIS2_FAILURE);
                
                if(_readSource->is_valid_in1 &&
                        arg_in1 == _readSource->property_in1)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                
                  if(NULL == arg_in1)
                  {
                      AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "in1 is being set to NULL, but it is not a nullable element");
                      return AXIS2_FAILURE;
                  }
                adb_readSource_reset_in1(_readSource, env);

                
                if(NULL == arg_in1)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _readSource->property_in1 = arg_in1;
                        _readSource->is_valid_in1 = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                              
              
           axis2_status_t AXIS2_CALL
           adb_readSource_reset_in1(
                   adb_readSource_t* _readSource,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _readSource, AXIS2_FAILURE);
               

               
            
                
                if(_readSource->property_in1 != NULL)
                {
                   
                   
                        adb_WsReadSourceParams_free(_readSource->property_in1, env);
                     _readSource->property_in1 = NULL;
                }
            
                
                
                _readSource->is_valid_in1 = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                       
              
           axis2_bool_t AXIS2_CALL
           adb_readSource_is_in1_nil(
                   adb_readSource_t* _readSource,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _readSource, AXIS2_TRUE);
               
               return !_readSource->is_valid_in1;
           }

              
                                                           
              
           axis2_status_t AXIS2_CALL
           adb_readSource_set_in1_nil(
                   adb_readSource_t* _readSource,
                   const axutil_env_t *env)
           {
               return adb_readSource_reset_in1(_readSource, env);
           }

           

