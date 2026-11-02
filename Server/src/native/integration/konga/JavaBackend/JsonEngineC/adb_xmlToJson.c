

           
                          
          
                                                 
                                                                                                
           

        #include "adb_xmlToJson.h"
        
                 
                                                                                                                           
                  
           


        struct adb_xmlToJson
        {
            
                axutil_qname_t* qname;
            adb_WsJsonSessionId_t* property_SessionId;

                
                axis2_bool_t is_valid_SessionId;


            adb_WsConversionParams_t* property_ConversionParams;

                
                axis2_bool_t is_valid_ConversionParams;


            
        };


                                                                                               
        

                axis2_status_t AXIS2_CALL
                adb_xmlToJson_set_SessionId_nil(
                        adb_xmlToJson_t* _xmlToJson,
                        const axutil_env_t *env);
            

                axis2_status_t AXIS2_CALL
                adb_xmlToJson_set_ConversionParams_nil(
                        adb_xmlToJson_t* _xmlToJson,
                        const axutil_env_t *env);
            


                                                                                           
        adb_xmlToJson_t* AXIS2_CALL
        adb_xmlToJson_create(
            const axutil_env_t *env)
        {
            adb_xmlToJson_t *_xmlToJson = NULL;
            
                axutil_qname_t* qname = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _xmlToJson = (adb_xmlToJson_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_xmlToJson_t));

            if(NULL == _xmlToJson)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_xmlToJson, 0, sizeof(adb_xmlToJson_t));

            _xmlToJson->property_SessionId  = NULL;
                  _xmlToJson->is_valid_SessionId  = AXIS2_FALSE;
            _xmlToJson->property_ConversionParams  = NULL;
                  _xmlToJson->is_valid_ConversionParams  = AXIS2_FALSE;
            
                  qname =  axutil_qname_create (env,
                        "xmlToJson",
                        "http://json.webservice.backend.server.integration.etlforge.invalid",
                        NULL);
                _xmlToJson->qname = qname;
            

            return _xmlToJson;
        }

        axis2_status_t AXIS2_CALL
        adb_xmlToJson_free (
                adb_xmlToJson_t* _xmlToJson,
                const axutil_env_t *env)
        {
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _xmlToJson, AXIS2_FAILURE);

            adb_xmlToJson_reset_SessionId(_xmlToJson, env);
            adb_xmlToJson_reset_ConversionParams(_xmlToJson, env);
            
              if(_xmlToJson->qname)
              {
                  axutil_qname_free (_xmlToJson->qname, env);
                  _xmlToJson->qname = NULL;
              }
            

            if(_xmlToJson)
            {
                AXIS2_FREE(env->allocator, _xmlToJson);
                _xmlToJson = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_xmlToJson_deserialize(
                adb_xmlToJson_t* _xmlToJson,
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
            AXIS2_PARAM_CHECK(env->error, _xmlToJson, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for xmlToJson : "
                            "NULL elemenet can not be passed to deserialize");
                return AXIS2_FAILURE;
              }
              

                    current_element = (axiom_element_t *)axiom_node_get_data_element(parent, env);
                    qname = axiom_element_get_qname(current_element, env, parent);
                    if (axutil_qname_equals(qname, env, _xmlToJson-> qname))
                    {
                        
                          first_node = axiom_node_get_first_child(parent, env);
                          
                    }
                    else
                    {
                        AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                              "Failed in building adb object for xmlToJson : "
                              "Expected %s but returned %s",
                              axutil_qname_to_string(_xmlToJson-> qname, env),
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
                                   
                                 element_qname = axutil_qname_create(env, "SessionId", "http://json.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

                           if (adb_WsJsonSessionId_is_particle() ||  
                                (current_node   && current_element && (axutil_qname_equals(element_qname, env, qname))))
                           {
                              if( current_node   && current_element && (axutil_qname_equals(element_qname, env, qname)))
                              {
                                is_early_node_valid = AXIS2_TRUE;
                              }
                              
                                 
                                      element = (void*)adb_WsJsonSessionId_create(env);

                                      status =  adb_WsJsonSessionId_deserialize((adb_WsJsonSessionId_t*)element,
                                                                            env, &current_node, &is_early_node_valid, AXIS2_FALSE);
                                      if(AXIS2_FAILURE == status)
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in building adb object for element SessionId");
                                      }
                                      else
                                      {
                                          status = adb_xmlToJson_set_SessionId(_xmlToJson, env,
                                                                   (adb_WsJsonSessionId_t*)element);
                                      }
                                    
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for SessionId ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element SessionId missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "ConversionParams", "http://json.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

                           if (adb_WsConversionParams_is_particle() ||  
                                (current_node   && current_element && (axutil_qname_equals(element_qname, env, qname))))
                           {
                              if( current_node   && current_element && (axutil_qname_equals(element_qname, env, qname)))
                              {
                                is_early_node_valid = AXIS2_TRUE;
                              }
                              
                                 
                                      element = (void*)adb_WsConversionParams_create(env);

                                      status =  adb_WsConversionParams_deserialize((adb_WsConversionParams_t*)element,
                                                                            env, &current_node, &is_early_node_valid, AXIS2_FALSE);
                                      if(AXIS2_FAILURE == status)
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in building adb object for element ConversionParams");
                                      }
                                      else
                                      {
                                          status = adb_xmlToJson_set_ConversionParams(_xmlToJson, env,
                                                                   (adb_WsConversionParams_t*)element);
                                      }
                                    
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for ConversionParams ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element ConversionParams missing");
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
          adb_xmlToJson_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_xmlToJson_declare_parent_namespaces(
                    adb_xmlToJson_t* _xmlToJson,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_xmlToJson_serialize(
                adb_xmlToJson_t* _xmlToJson,
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
            AXIS2_PARAM_CHECK(env->error, _xmlToJson, NULL);
            
             
                    namespaces = axutil_hash_make(env);
                    next_ns_index = &next_ns_index_value;
                     
                           ns1 = axiom_namespace_create (env,
                                             "http://json.webservice.backend.server.integration.etlforge.invalid",
                                             "n"); 
                           axutil_hash_set(namespaces, "http://json.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, axutil_strdup(env, "n"));
                       
                     
                    parent_element = axiom_element_create (env, NULL, "xmlToJson", ns1 , &parent);
                    
                    
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
                      

                   if (!_xmlToJson->is_valid_SessionId)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property SessionId");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("SessionId"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("SessionId")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                 
                        

                    
                    
                            sprintf(start_input_str, "<%s%sSessionId",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":""); 
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sSessionId>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                     
                            if(!adb_WsJsonSessionId_is_particle())
                            {
                                axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                            }
                            
                            adb_WsJsonSessionId_serialize(_xmlToJson->property_SessionId, 
                                                                                 env, current_node, parent_element,
                                                                                 adb_WsJsonSessionId_is_particle() || AXIS2_FALSE, namespaces, next_ns_index);
                            
                            if(!adb_WsJsonSessionId_is_particle())
                            {
                                axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                            }
                            
                     
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
                      

                   if (!_xmlToJson->is_valid_ConversionParams)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property ConversionParams");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("ConversionParams"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("ConversionParams")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                        
                        

                    
                    
                            sprintf(start_input_str, "<%s%sConversionParams",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":""); 
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sConversionParams>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                     
                            if(!adb_WsConversionParams_is_particle())
                            {
                                axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                            }
                            
                            adb_WsConversionParams_serialize(_xmlToJson->property_ConversionParams, 
                                                                                 env, current_node, parent_element,
                                                                                 adb_WsConversionParams_is_particle() || AXIS2_FALSE, namespaces, next_ns_index);
                            
                            if(!adb_WsConversionParams_is_particle())
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


        

               
                                    
               
            adb_WsJsonSessionId_t* AXIS2_CALL
            adb_xmlToJson_get_SessionId(
                    adb_xmlToJson_t* _xmlToJson,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _xmlToJson, NULL);
                  

                return _xmlToJson->property_SessionId;
             }

               
                                   
               
            axis2_status_t AXIS2_CALL
            adb_xmlToJson_set_SessionId(
                    adb_xmlToJson_t* _xmlToJson,
                    const axutil_env_t *env,
                    adb_WsJsonSessionId_t*  arg_SessionId)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _xmlToJson, AXIS2_FAILURE);
                
                if(_xmlToJson->is_valid_SessionId &&
                        arg_SessionId == _xmlToJson->property_SessionId)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                
                  if(NULL == arg_SessionId)
                  {
                      AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "SessionId is being set to NULL, but it is not a nullable element");
                      return AXIS2_FAILURE;
                  }
                adb_xmlToJson_reset_SessionId(_xmlToJson, env);

                
                if(NULL == arg_SessionId)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _xmlToJson->property_SessionId = arg_SessionId;
                        _xmlToJson->is_valid_SessionId = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                    
              
           axis2_status_t AXIS2_CALL
           adb_xmlToJson_reset_SessionId(
                   adb_xmlToJson_t* _xmlToJson,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _xmlToJson, AXIS2_FAILURE);
               

               
            
                
                if(_xmlToJson->property_SessionId != NULL)
                {
                   
                   
                        adb_WsJsonSessionId_free(_xmlToJson->property_SessionId, env);
                     _xmlToJson->property_SessionId = NULL;
                }
            
                
                
                _xmlToJson->is_valid_SessionId = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                             
              
           axis2_bool_t AXIS2_CALL
           adb_xmlToJson_is_SessionId_nil(
                   adb_xmlToJson_t* _xmlToJson,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _xmlToJson, AXIS2_TRUE);
               
               return !_xmlToJson->is_valid_SessionId;
           }

              
                                                                 
              
           axis2_status_t AXIS2_CALL
           adb_xmlToJson_set_SessionId_nil(
                   adb_xmlToJson_t* _xmlToJson,
                   const axutil_env_t *env)
           {
               return adb_xmlToJson_reset_SessionId(_xmlToJson, env);
           }

           

               
                                           
               
            adb_WsConversionParams_t* AXIS2_CALL
            adb_xmlToJson_get_ConversionParams(
                    adb_xmlToJson_t* _xmlToJson,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _xmlToJson, NULL);
                  

                return _xmlToJson->property_ConversionParams;
             }

               
                                          
               
            axis2_status_t AXIS2_CALL
            adb_xmlToJson_set_ConversionParams(
                    adb_xmlToJson_t* _xmlToJson,
                    const axutil_env_t *env,
                    adb_WsConversionParams_t*  arg_ConversionParams)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _xmlToJson, AXIS2_FAILURE);
                
                if(_xmlToJson->is_valid_ConversionParams &&
                        arg_ConversionParams == _xmlToJson->property_ConversionParams)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                
                  if(NULL == arg_ConversionParams)
                  {
                      AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "ConversionParams is being set to NULL, but it is not a nullable element");
                      return AXIS2_FAILURE;
                  }
                adb_xmlToJson_reset_ConversionParams(_xmlToJson, env);

                
                if(NULL == arg_ConversionParams)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _xmlToJson->property_ConversionParams = arg_ConversionParams;
                        _xmlToJson->is_valid_ConversionParams = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                           
              
           axis2_status_t AXIS2_CALL
           adb_xmlToJson_reset_ConversionParams(
                   adb_xmlToJson_t* _xmlToJson,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _xmlToJson, AXIS2_FAILURE);
               

               
            
                
                if(_xmlToJson->property_ConversionParams != NULL)
                {
                   
                   
                        adb_WsConversionParams_free(_xmlToJson->property_ConversionParams, env);
                     _xmlToJson->property_ConversionParams = NULL;
                }
            
                
                
                _xmlToJson->is_valid_ConversionParams = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                    
              
           axis2_bool_t AXIS2_CALL
           adb_xmlToJson_is_ConversionParams_nil(
                   adb_xmlToJson_t* _xmlToJson,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _xmlToJson, AXIS2_TRUE);
               
               return !_xmlToJson->is_valid_ConversionParams;
           }

              
                                                                        
              
           axis2_status_t AXIS2_CALL
           adb_xmlToJson_set_ConversionParams_nil(
                   adb_xmlToJson_t* _xmlToJson,
                   const axutil_env_t *env)
           {
               return adb_xmlToJson_reset_ConversionParams(_xmlToJson, env);
           }

           

