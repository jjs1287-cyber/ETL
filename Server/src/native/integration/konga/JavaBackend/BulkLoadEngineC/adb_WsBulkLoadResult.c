

           
                                 
          
                                                 
                                                                                                
           

        #include "adb_WsBulkLoadResult.h"
        
                  
                                                                            
                                          
                                                                                                                
                                         
                   
           


        struct adb_WsBulkLoadResult
        {
            axis2_bool_t property_allBatchesWereSent;

                
                axis2_bool_t is_valid_allBatchesWereSent;


            adb_WsBulkLoadError_t* property_error;

                
                axis2_bool_t is_valid_error;


            axis2_char_t* property_logFile;

                
                axis2_bool_t is_valid_logFile;


            
        };


                                                                                               
        

                axis2_status_t AXIS2_CALL
                adb_WsBulkLoadResult_set_allBatchesWereSent_nil(
                        adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                        const axutil_env_t *env);
            


                                                                                           
        adb_WsBulkLoadResult_t* AXIS2_CALL
        adb_WsBulkLoadResult_create(
            const axutil_env_t *env)
        {
            adb_WsBulkLoadResult_t *_WsBulkLoadResult = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _WsBulkLoadResult = (adb_WsBulkLoadResult_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_WsBulkLoadResult_t));

            if(NULL == _WsBulkLoadResult)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_WsBulkLoadResult, 0, sizeof(adb_WsBulkLoadResult_t));

            _WsBulkLoadResult->is_valid_allBatchesWereSent  = AXIS2_FALSE;
            _WsBulkLoadResult->property_error  = NULL;
                  _WsBulkLoadResult->is_valid_error  = AXIS2_FALSE;
            _WsBulkLoadResult->property_logFile  = NULL;
                  _WsBulkLoadResult->is_valid_logFile  = AXIS2_FALSE;
            

            return _WsBulkLoadResult;
        }

        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadResult_free (
                adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                const axutil_env_t *env)
        {
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _WsBulkLoadResult, AXIS2_FAILURE);

            adb_WsBulkLoadResult_reset_allBatchesWereSent(_WsBulkLoadResult, env);
            adb_WsBulkLoadResult_reset_error(_WsBulkLoadResult, env);
            adb_WsBulkLoadResult_reset_logFile(_WsBulkLoadResult, env);
            

            if(_WsBulkLoadResult)
            {
                AXIS2_FREE(env->allocator, _WsBulkLoadResult);
                _WsBulkLoadResult = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_WsBulkLoadResult_deserialize(
                adb_WsBulkLoadResult_t* _WsBulkLoadResult,
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
            AXIS2_PARAM_CHECK(env->error, _WsBulkLoadResult, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for WsBulkLoadResult : "
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
                                   
                                 element_qname = axutil_qname_create(env, "allBatchesWereSent", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", NULL);
                                 

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
                                                status = adb_WsBulkLoadResult_set_allBatchesWereSent(_WsBulkLoadResult, env,
                                                                 AXIS2_TRUE);
                                            }
                                            else
                                            {
                                                status = adb_WsBulkLoadResult_set_allBatchesWereSent(_WsBulkLoadResult, env,
                                                                      AXIS2_FALSE);
                                            }
                                      }
                                      
                                      else
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "NULL value is set to a non nillable element allBatchesWereSent");
                                          status = AXIS2_FAILURE;
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for allBatchesWereSent ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element allBatchesWereSent missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "error", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", NULL);
                                 

                           if (adb_WsBulkLoadError_is_particle() ||  
                                (current_node   && current_element && (axutil_qname_equals(element_qname, env, qname))))
                           {
                              if( current_node   && current_element && (axutil_qname_equals(element_qname, env, qname)))
                              {
                                is_early_node_valid = AXIS2_TRUE;
                              }
                              
                                 
                                      element = (void*)adb_WsBulkLoadError_create(env);

                                      status =  adb_WsBulkLoadError_deserialize((adb_WsBulkLoadError_t*)element,
                                                                            env, &current_node, &is_early_node_valid, AXIS2_FALSE);
                                      if(AXIS2_FAILURE == status)
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in building adb object for element error");
                                      }
                                      else
                                      {
                                          status = adb_WsBulkLoadResult_set_error(_WsBulkLoadResult, env,
                                                                   (adb_WsBulkLoadError_t*)element);
                                      }
                                    
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for error ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element error missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "logFile", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsBulkLoadResult_set_logFile(_WsBulkLoadResult, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for logFile ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element logFile missing");
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
          adb_WsBulkLoadResult_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_WsBulkLoadResult_declare_parent_namespaces(
                    adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_WsBulkLoadResult_serialize(
                adb_WsBulkLoadResult_t* _WsBulkLoadResult,
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
                    
                    axis2_char_t text_value_2[64];
                    
                    axis2_char_t *text_value_3;
                    axis2_char_t *text_value_3_temp;
                    
               axis2_char_t *start_input_str = NULL;
               axis2_char_t *end_input_str = NULL;
               unsigned int start_input_str_len = 0;
               unsigned int end_input_str_len = 0;
            
            
               axiom_data_source_t *data_source = NULL;
               axutil_stream_t *stream = NULL;

            

            AXIS2_ENV_CHECK(env, NULL);
            AXIS2_PARAM_CHECK(env->error, _WsBulkLoadResult, NULL);
            
            
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
                      

                   if (!_WsBulkLoadResult->is_valid_allBatchesWereSent)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property allBatchesWereSent");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("allBatchesWereSent"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("allBatchesWereSent")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                          
                        

                    
                    
                            sprintf(start_input_str, "<%s%sallBatchesWereSent>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sallBatchesWereSent>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           strcpy(text_value_1, (_WsBulkLoadResult->property_allBatchesWereSent)?"true":"false");
                           
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                           axutil_stream_write(stream, env, text_value_1, axutil_strlen(text_value_1));
                           
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
                      

                   if (!_WsBulkLoadResult->is_valid_error)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("error") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%serror xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("error"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("error")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                             
                        

                    
                    
                            sprintf(start_input_str, "<%s%serror",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":""); 
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%serror>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                     
                            if(!adb_WsBulkLoadError_is_particle())
                            {
                                axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                            }
                            
                            adb_WsBulkLoadError_serialize(_WsBulkLoadResult->property_error, 
                                                                                 env, current_node, parent_element,
                                                                                 adb_WsBulkLoadError_is_particle() || AXIS2_FALSE, namespaces, next_ns_index);
                            
                            if(!adb_WsBulkLoadError_is_particle())
                            {
                                axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                            }
                            
                     
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
                      

                   if (!_WsBulkLoadResult->is_valid_logFile)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("logFile") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%slogFile xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("logFile"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("logFile")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                               
                        

                    
                    
                            sprintf(start_input_str, "<%s%slogFile>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%slogFile>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_3 = _WsBulkLoadResult->property_logFile;
                           
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


        

               
                                             
               
            axis2_bool_t AXIS2_CALL
            adb_WsBulkLoadResult_get_allBatchesWereSent(
                    adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, (axis2_bool_t)0);
                    AXIS2_PARAM_CHECK(env->error, _WsBulkLoadResult, (axis2_bool_t)0);
                  

                return _WsBulkLoadResult->property_allBatchesWereSent;
             }

               
                                            
               
            axis2_status_t AXIS2_CALL
            adb_WsBulkLoadResult_set_allBatchesWereSent(
                    adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                    const axutil_env_t *env,
                    axis2_bool_t  arg_allBatchesWereSent)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsBulkLoadResult, AXIS2_FAILURE);
                
                if(_WsBulkLoadResult->is_valid_allBatchesWereSent &&
                        arg_allBatchesWereSent == _WsBulkLoadResult->property_allBatchesWereSent)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsBulkLoadResult_reset_allBatchesWereSent(_WsBulkLoadResult, env);

                _WsBulkLoadResult->property_allBatchesWereSent = arg_allBatchesWereSent;
                        _WsBulkLoadResult->is_valid_allBatchesWereSent = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                             
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadResult_reset_allBatchesWereSent(
                   adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadResult, AXIS2_FAILURE);
               

               _WsBulkLoadResult->is_valid_allBatchesWereSent = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                      
              
           axis2_bool_t AXIS2_CALL
           adb_WsBulkLoadResult_is_allBatchesWereSent_nil(
                   adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadResult, AXIS2_TRUE);
               
               return !_WsBulkLoadResult->is_valid_allBatchesWereSent;
           }

              
                                                                          
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadResult_set_allBatchesWereSent_nil(
                   adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                   const axutil_env_t *env)
           {
               return adb_WsBulkLoadResult_reset_allBatchesWereSent(_WsBulkLoadResult, env);
           }

           

               
                                
               
            adb_WsBulkLoadError_t* AXIS2_CALL
            adb_WsBulkLoadResult_get_error(
                    adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsBulkLoadResult, NULL);
                  

                return _WsBulkLoadResult->property_error;
             }

               
                               
               
            axis2_status_t AXIS2_CALL
            adb_WsBulkLoadResult_set_error(
                    adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                    const axutil_env_t *env,
                    adb_WsBulkLoadError_t*  arg_error)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsBulkLoadResult, AXIS2_FAILURE);
                
                if(_WsBulkLoadResult->is_valid_error &&
                        arg_error == _WsBulkLoadResult->property_error)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsBulkLoadResult_reset_error(_WsBulkLoadResult, env);

                
                if(NULL == arg_error)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsBulkLoadResult->property_error = arg_error;
                        _WsBulkLoadResult->is_valid_error = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadResult_reset_error(
                   adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadResult, AXIS2_FAILURE);
               

               
            
                
                if(_WsBulkLoadResult->property_error != NULL)
                {
                   
                   
                        adb_WsBulkLoadError_free(_WsBulkLoadResult->property_error, env);
                     _WsBulkLoadResult->property_error = NULL;
                }
            
                
                
                _WsBulkLoadResult->is_valid_error = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                         
              
           axis2_bool_t AXIS2_CALL
           adb_WsBulkLoadResult_is_error_nil(
                   adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadResult, AXIS2_TRUE);
               
               return !_WsBulkLoadResult->is_valid_error;
           }

              
                                                             
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadResult_set_error_nil(
                   adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                   const axutil_env_t *env)
           {
               return adb_WsBulkLoadResult_reset_error(_WsBulkLoadResult, env);
           }

           

               
                                  
               
            axis2_char_t* AXIS2_CALL
            adb_WsBulkLoadResult_get_logFile(
                    adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsBulkLoadResult, NULL);
                  

                return _WsBulkLoadResult->property_logFile;
             }

               
                                 
               
            axis2_status_t AXIS2_CALL
            adb_WsBulkLoadResult_set_logFile(
                    adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_logFile)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsBulkLoadResult, AXIS2_FAILURE);
                
                if(_WsBulkLoadResult->is_valid_logFile &&
                        arg_logFile == _WsBulkLoadResult->property_logFile)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsBulkLoadResult_reset_logFile(_WsBulkLoadResult, env);

                
                if(NULL == arg_logFile)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsBulkLoadResult->property_logFile = (axis2_char_t *)axutil_strdup(env, arg_logFile);
                        if(NULL == _WsBulkLoadResult->property_logFile)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for logFile");
                            return AXIS2_FAILURE;
                        }
                        _WsBulkLoadResult->is_valid_logFile = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                  
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadResult_reset_logFile(
                   adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadResult, AXIS2_FAILURE);
               

               
            
                
                if(_WsBulkLoadResult->property_logFile != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsBulkLoadResult->property_logFile);
                     _WsBulkLoadResult->property_logFile = NULL;
                }
            
                
                
                _WsBulkLoadResult->is_valid_logFile = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                           
              
           axis2_bool_t AXIS2_CALL
           adb_WsBulkLoadResult_is_logFile_nil(
                   adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsBulkLoadResult, AXIS2_TRUE);
               
               return !_WsBulkLoadResult->is_valid_logFile;
           }

              
                                                               
              
           axis2_status_t AXIS2_CALL
           adb_WsBulkLoadResult_set_logFile_nil(
                   adb_WsBulkLoadResult_t* _WsBulkLoadResult,
                   const axutil_env_t *env)
           {
               return adb_WsBulkLoadResult_reset_logFile(_WsBulkLoadResult, env);
           }

           

