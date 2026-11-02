

           
                                         
          
                                                 
                                                                                                
           

        #include "adb_closeJsonSessionResponse.h"
        
                 
                                                                                                                                          
                  
           


        struct adb_closeJsonSessionResponse
        {
            
                axutil_qname_t* qname;
            adb_WsJsonError_t* property_closeJsonSessionReturn;

                
                axis2_bool_t is_valid_closeJsonSessionReturn;


            
        };


                                                                                               
        

                axis2_status_t AXIS2_CALL
                adb_closeJsonSessionResponse_set_closeJsonSessionReturn_nil(
                        adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
                        const axutil_env_t *env);
            


                                                                                           
        adb_closeJsonSessionResponse_t* AXIS2_CALL
        adb_closeJsonSessionResponse_create(
            const axutil_env_t *env)
        {
            adb_closeJsonSessionResponse_t *_closeJsonSessionResponse = NULL;
            
                axutil_qname_t* qname = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _closeJsonSessionResponse = (adb_closeJsonSessionResponse_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_closeJsonSessionResponse_t));

            if(NULL == _closeJsonSessionResponse)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_closeJsonSessionResponse, 0, sizeof(adb_closeJsonSessionResponse_t));

            _closeJsonSessionResponse->property_closeJsonSessionReturn  = NULL;
                  _closeJsonSessionResponse->is_valid_closeJsonSessionReturn  = AXIS2_FALSE;
            
                  qname =  axutil_qname_create (env,
                        "closeJsonSessionResponse",
                        "http://json.webservice.backend.server.integration.etlforge.invalid",
                        NULL);
                _closeJsonSessionResponse->qname = qname;
            

            return _closeJsonSessionResponse;
        }

        axis2_status_t AXIS2_CALL
        adb_closeJsonSessionResponse_free (
                adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
                const axutil_env_t *env)
        {
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _closeJsonSessionResponse, AXIS2_FAILURE);

            adb_closeJsonSessionResponse_reset_closeJsonSessionReturn(_closeJsonSessionResponse, env);
            
              if(_closeJsonSessionResponse->qname)
              {
                  axutil_qname_free (_closeJsonSessionResponse->qname, env);
                  _closeJsonSessionResponse->qname = NULL;
              }
            

            if(_closeJsonSessionResponse)
            {
                AXIS2_FREE(env->allocator, _closeJsonSessionResponse);
                _closeJsonSessionResponse = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_closeJsonSessionResponse_deserialize(
                adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
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
            AXIS2_PARAM_CHECK(env->error, _closeJsonSessionResponse, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for closeJsonSessionResponse : "
                            "NULL elemenet can not be passed to deserialize");
                return AXIS2_FAILURE;
              }
              

                    current_element = (axiom_element_t *)axiom_node_get_data_element(parent, env);
                    qname = axiom_element_get_qname(current_element, env, parent);
                    if (axutil_qname_equals(qname, env, _closeJsonSessionResponse-> qname))
                    {
                        
                          first_node = axiom_node_get_first_child(parent, env);
                          
                    }
                    else
                    {
                        AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                              "Failed in building adb object for closeJsonSessionResponse : "
                              "Expected %s but returned %s",
                              axutil_qname_to_string(_closeJsonSessionResponse-> qname, env),
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
                                   
                                 element_qname = axutil_qname_create(env, "closeJsonSessionReturn", "http://json.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

                           if (adb_WsJsonError_is_particle() ||  
                                (current_node   && current_element && (axutil_qname_equals(element_qname, env, qname))))
                           {
                              if( current_node   && current_element && (axutil_qname_equals(element_qname, env, qname)))
                              {
                                is_early_node_valid = AXIS2_TRUE;
                              }
                              
                                 
                                      element = (void*)adb_WsJsonError_create(env);

                                      status =  adb_WsJsonError_deserialize((adb_WsJsonError_t*)element,
                                                                            env, &current_node, &is_early_node_valid, AXIS2_FALSE);
                                      if(AXIS2_FAILURE == status)
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in building adb object for element closeJsonSessionReturn");
                                      }
                                      else
                                      {
                                          status = adb_closeJsonSessionResponse_set_closeJsonSessionReturn(_closeJsonSessionResponse, env,
                                                                   (adb_WsJsonError_t*)element);
                                      }
                                    
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for closeJsonSessionReturn ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element closeJsonSessionReturn missing");
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
          adb_closeJsonSessionResponse_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_closeJsonSessionResponse_declare_parent_namespaces(
                    adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_closeJsonSessionResponse_serialize(
                adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
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
                    
               axis2_char_t *start_input_str = NULL;
               axis2_char_t *end_input_str = NULL;
               unsigned int start_input_str_len = 0;
               unsigned int end_input_str_len = 0;
            
            
               axiom_data_source_t *data_source = NULL;
               axutil_stream_t *stream = NULL;

             
                int next_ns_index_value = 0;
            

            AXIS2_ENV_CHECK(env, NULL);
            AXIS2_PARAM_CHECK(env->error, _closeJsonSessionResponse, NULL);
            
             
                    namespaces = axutil_hash_make(env);
                    next_ns_index = &next_ns_index_value;
                     
                           ns1 = axiom_namespace_create (env,
                                             "http://json.webservice.backend.server.integration.etlforge.invalid",
                                             "n"); 
                           axutil_hash_set(namespaces, "http://json.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, axutil_strdup(env, "n"));
                       
                     
                    parent_element = axiom_element_create (env, NULL, "closeJsonSessionResponse", ns1 , &parent);
                    
                    
                    axiom_element_set_namespace(parent_element, env, ns1, parent);


            
                    data_source = axiom_data_source_create(env, parent, &current_node);
                    stream = axiom_data_source_get_stream(data_source, env);
                  
                       if(!(p_prefix = (axis2_char_t*)axutil_hash_get(namespaces, "http://json.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING)))
                       {
                           p_prefix = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof (axis2_char_t) * ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT);
                           sprintf(p_prefix, "n%d", (*next_ns_index)++);
                           axutil_hash_set(namespaces, "http://json.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, p_prefix);
                           
                           axiom_element_declare_namespace_assume_param_ownership(parent_element, env, axiom_namespace_create (env,
                                            "http://json.webservice.backend.server.integration.etlforge.invalid",
                                            p_prefix));
                       }
                      

                   if (!_closeJsonSessionResponse->is_valid_closeJsonSessionReturn)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property closeJsonSessionReturn");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("closeJsonSessionReturn"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("closeJsonSessionReturn")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                              
                        

                    
                    
                            sprintf(start_input_str, "<%s%scloseJsonSessionReturn",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":""); 
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%scloseJsonSessionReturn>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                     
                            if(!adb_WsJsonError_is_particle())
                            {
                                axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                            }
                            
                            adb_WsJsonError_serialize(_closeJsonSessionResponse->property_closeJsonSessionReturn, 
                                                                                 env, current_node, parent_element,
                                                                                 adb_WsJsonError_is_particle() || AXIS2_FALSE, namespaces, next_ns_index);
                            
                            if(!adb_WsJsonError_is_particle())
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


        

               
                                                 
               
            adb_WsJsonError_t* AXIS2_CALL
            adb_closeJsonSessionResponse_get_closeJsonSessionReturn(
                    adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _closeJsonSessionResponse, NULL);
                  

                return _closeJsonSessionResponse->property_closeJsonSessionReturn;
             }

               
                                                
               
            axis2_status_t AXIS2_CALL
            adb_closeJsonSessionResponse_set_closeJsonSessionReturn(
                    adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
                    const axutil_env_t *env,
                    adb_WsJsonError_t*  arg_closeJsonSessionReturn)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _closeJsonSessionResponse, AXIS2_FAILURE);
                
                if(_closeJsonSessionResponse->is_valid_closeJsonSessionReturn &&
                        arg_closeJsonSessionReturn == _closeJsonSessionResponse->property_closeJsonSessionReturn)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                
                  if(NULL == arg_closeJsonSessionReturn)
                  {
                      AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "closeJsonSessionReturn is being set to NULL, but it is not a nullable element");
                      return AXIS2_FAILURE;
                  }
                adb_closeJsonSessionResponse_reset_closeJsonSessionReturn(_closeJsonSessionResponse, env);

                
                if(NULL == arg_closeJsonSessionReturn)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _closeJsonSessionResponse->property_closeJsonSessionReturn = arg_closeJsonSessionReturn;
                        _closeJsonSessionResponse->is_valid_closeJsonSessionReturn = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                                 
              
           axis2_status_t AXIS2_CALL
           adb_closeJsonSessionResponse_reset_closeJsonSessionReturn(
                   adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _closeJsonSessionResponse, AXIS2_FAILURE);
               

               
            
                
                if(_closeJsonSessionResponse->property_closeJsonSessionReturn != NULL)
                {
                   
                   
                        adb_WsJsonError_free(_closeJsonSessionResponse->property_closeJsonSessionReturn, env);
                     _closeJsonSessionResponse->property_closeJsonSessionReturn = NULL;
                }
            
                
                
                _closeJsonSessionResponse->is_valid_closeJsonSessionReturn = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                          
              
           axis2_bool_t AXIS2_CALL
           adb_closeJsonSessionResponse_is_closeJsonSessionReturn_nil(
                   adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _closeJsonSessionResponse, AXIS2_TRUE);
               
               return !_closeJsonSessionResponse->is_valid_closeJsonSessionReturn;
           }

              
                                                                              
              
           axis2_status_t AXIS2_CALL
           adb_closeJsonSessionResponse_set_closeJsonSessionReturn_nil(
                   adb_closeJsonSessionResponse_t* _closeJsonSessionResponse,
                   const axutil_env_t *env)
           {
               return adb_closeJsonSessionResponse_reset_closeJsonSessionReturn(_closeJsonSessionResponse, env);
           }

           

