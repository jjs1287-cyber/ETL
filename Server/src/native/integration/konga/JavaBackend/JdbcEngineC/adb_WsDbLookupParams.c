

           
                                 
          
                                                 
                                                                                                
           

        #include "adb_WsDbLookupParams.h"
        
                  
                                                                            
                                          
                                                                                                     
                                         
                   
           


        struct adb_WsDbLookupParams
        {
            axis2_char_t* property_sourceGuid;

                
                axis2_bool_t is_valid_sourceGuid;


            axis2_char_t* property_targetGuid;

                
                axis2_bool_t is_valid_targetGuid;


            adb_WsConnectionParams_t* property_connectionParams;

                
                axis2_bool_t is_valid_connectionParams;


            axis2_char_t* property_sql;

                
                axis2_bool_t is_valid_sql;


            axis2_bool_t property_autoCommit;

                
                axis2_bool_t is_valid_autoCommit;


            
        };


                                                                                               
        

                axis2_status_t AXIS2_CALL
                adb_WsDbLookupParams_set_autoCommit_nil(
                        adb_WsDbLookupParams_t* _WsDbLookupParams,
                        const axutil_env_t *env);
            


                                                                                           
        adb_WsDbLookupParams_t* AXIS2_CALL
        adb_WsDbLookupParams_create(
            const axutil_env_t *env)
        {
            adb_WsDbLookupParams_t *_WsDbLookupParams = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _WsDbLookupParams = (adb_WsDbLookupParams_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_WsDbLookupParams_t));

            if(NULL == _WsDbLookupParams)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_WsDbLookupParams, 0, sizeof(adb_WsDbLookupParams_t));

            _WsDbLookupParams->property_sourceGuid  = NULL;
                  _WsDbLookupParams->is_valid_sourceGuid  = AXIS2_FALSE;
            _WsDbLookupParams->property_targetGuid  = NULL;
                  _WsDbLookupParams->is_valid_targetGuid  = AXIS2_FALSE;
            _WsDbLookupParams->property_connectionParams  = NULL;
                  _WsDbLookupParams->is_valid_connectionParams  = AXIS2_FALSE;
            _WsDbLookupParams->property_sql  = NULL;
                  _WsDbLookupParams->is_valid_sql  = AXIS2_FALSE;
            _WsDbLookupParams->is_valid_autoCommit  = AXIS2_FALSE;
            

            return _WsDbLookupParams;
        }

        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_free (
                adb_WsDbLookupParams_t* _WsDbLookupParams,
                const axutil_env_t *env)
        {
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, AXIS2_FAILURE);

            adb_WsDbLookupParams_reset_sourceGuid(_WsDbLookupParams, env);
            adb_WsDbLookupParams_reset_targetGuid(_WsDbLookupParams, env);
            adb_WsDbLookupParams_reset_connectionParams(_WsDbLookupParams, env);
            adb_WsDbLookupParams_reset_sql(_WsDbLookupParams, env);
            adb_WsDbLookupParams_reset_autoCommit(_WsDbLookupParams, env);
            

            if(_WsDbLookupParams)
            {
                AXIS2_FREE(env->allocator, _WsDbLookupParams);
                _WsDbLookupParams = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_WsDbLookupParams_deserialize(
                adb_WsDbLookupParams_t* _WsDbLookupParams,
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
            AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for WsDbLookupParams : "
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
                                   
                                 element_qname = axutil_qname_create(env, "sourceGuid", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsDbLookupParams_set_sourceGuid(_WsDbLookupParams, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for sourceGuid ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element sourceGuid missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "targetGuid", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsDbLookupParams_set_targetGuid(_WsDbLookupParams, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for targetGuid ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element targetGuid missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "connectionParams", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

                           if (adb_WsConnectionParams_is_particle() ||  
                                (current_node   && current_element && (axutil_qname_equals(element_qname, env, qname))))
                           {
                              if( current_node   && current_element && (axutil_qname_equals(element_qname, env, qname)))
                              {
                                is_early_node_valid = AXIS2_TRUE;
                              }
                              
                                 
                                      element = (void*)adb_WsConnectionParams_create(env);

                                      status =  adb_WsConnectionParams_deserialize((adb_WsConnectionParams_t*)element,
                                                                            env, &current_node, &is_early_node_valid, AXIS2_FALSE);
                                      if(AXIS2_FAILURE == status)
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in building adb object for element connectionParams");
                                      }
                                      else
                                      {
                                          status = adb_WsDbLookupParams_set_connectionParams(_WsDbLookupParams, env,
                                                                   (adb_WsConnectionParams_t*)element);
                                      }
                                    
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for connectionParams ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element connectionParams missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "sql", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsDbLookupParams_set_sql(_WsDbLookupParams, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for sql ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element sql missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "autoCommit", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            if (!axutil_strcasecmp(text_value , "true"))
                                            {
                                                status = adb_WsDbLookupParams_set_autoCommit(_WsDbLookupParams, env,
                                                                 AXIS2_TRUE);
                                            }
                                            else
                                            {
                                                status = adb_WsDbLookupParams_set_autoCommit(_WsDbLookupParams, env,
                                                                      AXIS2_FALSE);
                                            }
                                      }
                                      
                                      else
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "NULL value is set to a non nillable element autoCommit");
                                          status = AXIS2_FAILURE;
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for autoCommit ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element autoCommit missing");
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
          adb_WsDbLookupParams_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_WsDbLookupParams_declare_parent_namespaces(
                    adb_WsDbLookupParams_t* _WsDbLookupParams,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_WsDbLookupParams_serialize(
                adb_WsDbLookupParams_t* _WsDbLookupParams,
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
                    
                    axis2_char_t *text_value_4;
                    axis2_char_t *text_value_4_temp;
                    
                    axis2_char_t text_value_5[64];
                    
               axis2_char_t *start_input_str = NULL;
               axis2_char_t *end_input_str = NULL;
               unsigned int start_input_str_len = 0;
               unsigned int end_input_str_len = 0;
            
            
               axiom_data_source_t *data_source = NULL;
               axutil_stream_t *stream = NULL;

            

            AXIS2_ENV_CHECK(env, NULL);
            AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, NULL);
            
            
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
            
                       if(!(p_prefix = (axis2_char_t*)axutil_hash_get(namespaces, "http://jdbc.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING)))
                       {
                           p_prefix = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof (axis2_char_t) * ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT);
                           sprintf(p_prefix, "n%d", (*next_ns_index)++);
                           axutil_hash_set(namespaces, "http://jdbc.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, p_prefix);
                           
                           axiom_element_declare_namespace_assume_param_ownership(parent_element, env, axiom_namespace_create (env,
                                            "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
                                            p_prefix));
                       }
                      

                   if (!_WsDbLookupParams->is_valid_sourceGuid)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("sourceGuid") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%ssourceGuid xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("sourceGuid"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("sourceGuid")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                  
                        

                    
                    
                            sprintf(start_input_str, "<%s%ssourceGuid>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%ssourceGuid>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_1 = _WsDbLookupParams->property_sourceGuid;
                           
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

                 
                       if(!(p_prefix = (axis2_char_t*)axutil_hash_get(namespaces, "http://jdbc.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING)))
                       {
                           p_prefix = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof (axis2_char_t) * ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT);
                           sprintf(p_prefix, "n%d", (*next_ns_index)++);
                           axutil_hash_set(namespaces, "http://jdbc.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, p_prefix);
                           
                           axiom_element_declare_namespace_assume_param_ownership(parent_element, env, axiom_namespace_create (env,
                                            "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
                                            p_prefix));
                       }
                      

                   if (!_WsDbLookupParams->is_valid_targetGuid)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("targetGuid") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%stargetGuid xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("targetGuid"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("targetGuid")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                  
                        

                    
                    
                            sprintf(start_input_str, "<%s%stargetGuid>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%stargetGuid>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_2 = _WsDbLookupParams->property_targetGuid;
                           
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

                 
                       if(!(p_prefix = (axis2_char_t*)axutil_hash_get(namespaces, "http://jdbc.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING)))
                       {
                           p_prefix = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof (axis2_char_t) * ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT);
                           sprintf(p_prefix, "n%d", (*next_ns_index)++);
                           axutil_hash_set(namespaces, "http://jdbc.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, p_prefix);
                           
                           axiom_element_declare_namespace_assume_param_ownership(parent_element, env, axiom_namespace_create (env,
                                            "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
                                            p_prefix));
                       }
                      

                   if (!_WsDbLookupParams->is_valid_connectionParams)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("connectionParams") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%sconnectionParams xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("connectionParams"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("connectionParams")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                        
                        

                    
                    
                            sprintf(start_input_str, "<%s%sconnectionParams",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":""); 
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sconnectionParams>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                     
                            if(!adb_WsConnectionParams_is_particle())
                            {
                                axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                            }
                            
                            adb_WsConnectionParams_serialize(_WsDbLookupParams->property_connectionParams, 
                                                                                 env, current_node, parent_element,
                                                                                 adb_WsConnectionParams_is_particle() || AXIS2_FALSE, namespaces, next_ns_index);
                            
                            if(!adb_WsConnectionParams_is_particle())
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
                      

                   if (!_WsDbLookupParams->is_valid_sql)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("sql") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%ssql xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("sql"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("sql")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                           
                        

                    
                    
                            sprintf(start_input_str, "<%s%ssql>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%ssql>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_4 = _WsDbLookupParams->property_sql;
                           
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

                 
                       if(!(p_prefix = (axis2_char_t*)axutil_hash_get(namespaces, "http://jdbc.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING)))
                       {
                           p_prefix = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof (axis2_char_t) * ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT);
                           sprintf(p_prefix, "n%d", (*next_ns_index)++);
                           axutil_hash_set(namespaces, "http://jdbc.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, p_prefix);
                           
                           axiom_element_declare_namespace_assume_param_ownership(parent_element, env, axiom_namespace_create (env,
                                            "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
                                            p_prefix));
                       }
                      

                   if (!_WsDbLookupParams->is_valid_autoCommit)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property autoCommit");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("autoCommit"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("autoCommit")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                  
                        

                    
                    
                            sprintf(start_input_str, "<%s%sautoCommit>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sautoCommit>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           strcpy(text_value_5, (_WsDbLookupParams->property_autoCommit)?"true":"false");
                           
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                           axutil_stream_write(stream, env, text_value_5, axutil_strlen(text_value_5));
                           
                           axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                           
                     
                     AXIS2_FREE(env->allocator,start_input_str);
                     AXIS2_FREE(env->allocator,end_input_str);
                 } 

                 

            return parent;
        }


        

               
                                     
               
            axis2_char_t* AXIS2_CALL
            adb_WsDbLookupParams_get_sourceGuid(
                    adb_WsDbLookupParams_t* _WsDbLookupParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, NULL);
                  

                return _WsDbLookupParams->property_sourceGuid;
             }

               
                                    
               
            axis2_status_t AXIS2_CALL
            adb_WsDbLookupParams_set_sourceGuid(
                    adb_WsDbLookupParams_t* _WsDbLookupParams,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_sourceGuid)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, AXIS2_FAILURE);
                
                if(_WsDbLookupParams->is_valid_sourceGuid &&
                        arg_sourceGuid == _WsDbLookupParams->property_sourceGuid)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsDbLookupParams_reset_sourceGuid(_WsDbLookupParams, env);

                
                if(NULL == arg_sourceGuid)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsDbLookupParams->property_sourceGuid = (axis2_char_t *)axutil_strdup(env, arg_sourceGuid);
                        if(NULL == _WsDbLookupParams->property_sourceGuid)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for sourceGuid");
                            return AXIS2_FAILURE;
                        }
                        _WsDbLookupParams->is_valid_sourceGuid = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                     
              
           axis2_status_t AXIS2_CALL
           adb_WsDbLookupParams_reset_sourceGuid(
                   adb_WsDbLookupParams_t* _WsDbLookupParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsDbLookupParams->property_sourceGuid != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsDbLookupParams->property_sourceGuid);
                     _WsDbLookupParams->property_sourceGuid = NULL;
                }
            
                
                
                _WsDbLookupParams->is_valid_sourceGuid = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                              
              
           axis2_bool_t AXIS2_CALL
           adb_WsDbLookupParams_is_sourceGuid_nil(
                   adb_WsDbLookupParams_t* _WsDbLookupParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, AXIS2_TRUE);
               
               return !_WsDbLookupParams->is_valid_sourceGuid;
           }

              
                                                                  
              
           axis2_status_t AXIS2_CALL
           adb_WsDbLookupParams_set_sourceGuid_nil(
                   adb_WsDbLookupParams_t* _WsDbLookupParams,
                   const axutil_env_t *env)
           {
               return adb_WsDbLookupParams_reset_sourceGuid(_WsDbLookupParams, env);
           }

           

               
                                     
               
            axis2_char_t* AXIS2_CALL
            adb_WsDbLookupParams_get_targetGuid(
                    adb_WsDbLookupParams_t* _WsDbLookupParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, NULL);
                  

                return _WsDbLookupParams->property_targetGuid;
             }

               
                                    
               
            axis2_status_t AXIS2_CALL
            adb_WsDbLookupParams_set_targetGuid(
                    adb_WsDbLookupParams_t* _WsDbLookupParams,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_targetGuid)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, AXIS2_FAILURE);
                
                if(_WsDbLookupParams->is_valid_targetGuid &&
                        arg_targetGuid == _WsDbLookupParams->property_targetGuid)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsDbLookupParams_reset_targetGuid(_WsDbLookupParams, env);

                
                if(NULL == arg_targetGuid)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsDbLookupParams->property_targetGuid = (axis2_char_t *)axutil_strdup(env, arg_targetGuid);
                        if(NULL == _WsDbLookupParams->property_targetGuid)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for targetGuid");
                            return AXIS2_FAILURE;
                        }
                        _WsDbLookupParams->is_valid_targetGuid = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                     
              
           axis2_status_t AXIS2_CALL
           adb_WsDbLookupParams_reset_targetGuid(
                   adb_WsDbLookupParams_t* _WsDbLookupParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsDbLookupParams->property_targetGuid != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsDbLookupParams->property_targetGuid);
                     _WsDbLookupParams->property_targetGuid = NULL;
                }
            
                
                
                _WsDbLookupParams->is_valid_targetGuid = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                              
              
           axis2_bool_t AXIS2_CALL
           adb_WsDbLookupParams_is_targetGuid_nil(
                   adb_WsDbLookupParams_t* _WsDbLookupParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, AXIS2_TRUE);
               
               return !_WsDbLookupParams->is_valid_targetGuid;
           }

              
                                                                  
              
           axis2_status_t AXIS2_CALL
           adb_WsDbLookupParams_set_targetGuid_nil(
                   adb_WsDbLookupParams_t* _WsDbLookupParams,
                   const axutil_env_t *env)
           {
               return adb_WsDbLookupParams_reset_targetGuid(_WsDbLookupParams, env);
           }

           

               
                                           
               
            adb_WsConnectionParams_t* AXIS2_CALL
            adb_WsDbLookupParams_get_connectionParams(
                    adb_WsDbLookupParams_t* _WsDbLookupParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, NULL);
                  

                return _WsDbLookupParams->property_connectionParams;
             }

               
                                          
               
            axis2_status_t AXIS2_CALL
            adb_WsDbLookupParams_set_connectionParams(
                    adb_WsDbLookupParams_t* _WsDbLookupParams,
                    const axutil_env_t *env,
                    adb_WsConnectionParams_t*  arg_connectionParams)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, AXIS2_FAILURE);
                
                if(_WsDbLookupParams->is_valid_connectionParams &&
                        arg_connectionParams == _WsDbLookupParams->property_connectionParams)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsDbLookupParams_reset_connectionParams(_WsDbLookupParams, env);

                
                if(NULL == arg_connectionParams)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsDbLookupParams->property_connectionParams = arg_connectionParams;
                        _WsDbLookupParams->is_valid_connectionParams = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                           
              
           axis2_status_t AXIS2_CALL
           adb_WsDbLookupParams_reset_connectionParams(
                   adb_WsDbLookupParams_t* _WsDbLookupParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsDbLookupParams->property_connectionParams != NULL)
                {
                   
                   
                        adb_WsConnectionParams_free(_WsDbLookupParams->property_connectionParams, env);
                     _WsDbLookupParams->property_connectionParams = NULL;
                }
            
                
                
                _WsDbLookupParams->is_valid_connectionParams = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                    
              
           axis2_bool_t AXIS2_CALL
           adb_WsDbLookupParams_is_connectionParams_nil(
                   adb_WsDbLookupParams_t* _WsDbLookupParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, AXIS2_TRUE);
               
               return !_WsDbLookupParams->is_valid_connectionParams;
           }

              
                                                                        
              
           axis2_status_t AXIS2_CALL
           adb_WsDbLookupParams_set_connectionParams_nil(
                   adb_WsDbLookupParams_t* _WsDbLookupParams,
                   const axutil_env_t *env)
           {
               return adb_WsDbLookupParams_reset_connectionParams(_WsDbLookupParams, env);
           }

           

               
                              
               
            axis2_char_t* AXIS2_CALL
            adb_WsDbLookupParams_get_sql(
                    adb_WsDbLookupParams_t* _WsDbLookupParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, NULL);
                  

                return _WsDbLookupParams->property_sql;
             }

               
                             
               
            axis2_status_t AXIS2_CALL
            adb_WsDbLookupParams_set_sql(
                    adb_WsDbLookupParams_t* _WsDbLookupParams,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_sql)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, AXIS2_FAILURE);
                
                if(_WsDbLookupParams->is_valid_sql &&
                        arg_sql == _WsDbLookupParams->property_sql)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsDbLookupParams_reset_sql(_WsDbLookupParams, env);

                
                if(NULL == arg_sql)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsDbLookupParams->property_sql = (axis2_char_t *)axutil_strdup(env, arg_sql);
                        if(NULL == _WsDbLookupParams->property_sql)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for sql");
                            return AXIS2_FAILURE;
                        }
                        _WsDbLookupParams->is_valid_sql = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                              
              
           axis2_status_t AXIS2_CALL
           adb_WsDbLookupParams_reset_sql(
                   adb_WsDbLookupParams_t* _WsDbLookupParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsDbLookupParams->property_sql != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsDbLookupParams->property_sql);
                     _WsDbLookupParams->property_sql = NULL;
                }
            
                
                
                _WsDbLookupParams->is_valid_sql = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                       
              
           axis2_bool_t AXIS2_CALL
           adb_WsDbLookupParams_is_sql_nil(
                   adb_WsDbLookupParams_t* _WsDbLookupParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, AXIS2_TRUE);
               
               return !_WsDbLookupParams->is_valid_sql;
           }

              
                                                           
              
           axis2_status_t AXIS2_CALL
           adb_WsDbLookupParams_set_sql_nil(
                   adb_WsDbLookupParams_t* _WsDbLookupParams,
                   const axutil_env_t *env)
           {
               return adb_WsDbLookupParams_reset_sql(_WsDbLookupParams, env);
           }

           

               
                                     
               
            axis2_bool_t AXIS2_CALL
            adb_WsDbLookupParams_get_autoCommit(
                    adb_WsDbLookupParams_t* _WsDbLookupParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, (axis2_bool_t)0);
                    AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, (axis2_bool_t)0);
                  

                return _WsDbLookupParams->property_autoCommit;
             }

               
                                    
               
            axis2_status_t AXIS2_CALL
            adb_WsDbLookupParams_set_autoCommit(
                    adb_WsDbLookupParams_t* _WsDbLookupParams,
                    const axutil_env_t *env,
                    axis2_bool_t  arg_autoCommit)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, AXIS2_FAILURE);
                
                if(_WsDbLookupParams->is_valid_autoCommit &&
                        arg_autoCommit == _WsDbLookupParams->property_autoCommit)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsDbLookupParams_reset_autoCommit(_WsDbLookupParams, env);

                _WsDbLookupParams->property_autoCommit = arg_autoCommit;
                        _WsDbLookupParams->is_valid_autoCommit = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                     
              
           axis2_status_t AXIS2_CALL
           adb_WsDbLookupParams_reset_autoCommit(
                   adb_WsDbLookupParams_t* _WsDbLookupParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, AXIS2_FAILURE);
               

               _WsDbLookupParams->is_valid_autoCommit = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                              
              
           axis2_bool_t AXIS2_CALL
           adb_WsDbLookupParams_is_autoCommit_nil(
                   adb_WsDbLookupParams_t* _WsDbLookupParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsDbLookupParams, AXIS2_TRUE);
               
               return !_WsDbLookupParams->is_valid_autoCommit;
           }

              
                                                                  
              
           axis2_status_t AXIS2_CALL
           adb_WsDbLookupParams_set_autoCommit_nil(
                   adb_WsDbLookupParams_t* _WsDbLookupParams,
                   const axutil_env_t *env)
           {
               return adb_WsDbLookupParams_reset_autoCommit(_WsDbLookupParams, env);
           }

           

