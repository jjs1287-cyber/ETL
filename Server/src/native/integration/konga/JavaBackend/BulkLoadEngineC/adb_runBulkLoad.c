

           
                            
          
                                                 
                                                                                                
           

        #include "adb_runBulkLoad.h"
        
                 
                                                                                                                                        
                  
           


        struct adb_runBulkLoad
        {
            
                axutil_qname_t* qname;
            adb_WsBulkLoadSessionId_t* property_sessionId;

                
                axis2_bool_t is_valid_sessionId;


            adb_WsBulkLoadParams_t* property_bulkloadParams;

                
                axis2_bool_t is_valid_bulkloadParams;


            adb_WsSalesforceEndpoint_t* property_salesforceEndpoint;

                
                axis2_bool_t is_valid_salesforceEndpoint;


            axutil_array_list_t* property_in3;

                
                axis2_bool_t is_valid_in3;


            
        };


                                                                                               
        

                axis2_status_t AXIS2_CALL
                adb_runBulkLoad_set_sessionId_nil(
                        adb_runBulkLoad_t* _runBulkLoad,
                        const axutil_env_t *env);
            

                axis2_status_t AXIS2_CALL
                adb_runBulkLoad_set_bulkloadParams_nil(
                        adb_runBulkLoad_t* _runBulkLoad,
                        const axutil_env_t *env);
            

                axis2_status_t AXIS2_CALL
                adb_runBulkLoad_set_salesforceEndpoint_nil(
                        adb_runBulkLoad_t* _runBulkLoad,
                        const axutil_env_t *env);
            
                 axis2_status_t AXIS2_CALL
                 adb_runBulkLoad_set_in3_nil_at(
                        adb_runBulkLoad_t* _runBulkLoad, 
                        const axutil_env_t *env, int i);
                

                axis2_status_t AXIS2_CALL
                adb_runBulkLoad_set_in3_nil(
                        adb_runBulkLoad_t* _runBulkLoad,
                        const axutil_env_t *env);
            


                                                                                           
        adb_runBulkLoad_t* AXIS2_CALL
        adb_runBulkLoad_create(
            const axutil_env_t *env)
        {
            adb_runBulkLoad_t *_runBulkLoad = NULL;
            
                axutil_qname_t* qname = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _runBulkLoad = (adb_runBulkLoad_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_runBulkLoad_t));

            if(NULL == _runBulkLoad)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_runBulkLoad, 0, sizeof(adb_runBulkLoad_t));

            _runBulkLoad->property_sessionId  = NULL;
                  _runBulkLoad->is_valid_sessionId  = AXIS2_FALSE;
            _runBulkLoad->property_bulkloadParams  = NULL;
                  _runBulkLoad->is_valid_bulkloadParams  = AXIS2_FALSE;
            _runBulkLoad->property_salesforceEndpoint  = NULL;
                  _runBulkLoad->is_valid_salesforceEndpoint  = AXIS2_FALSE;
            _runBulkLoad->property_in3  = NULL;
                  _runBulkLoad->is_valid_in3  = AXIS2_FALSE;
            
                  qname =  axutil_qname_create (env,
                        "runBulkLoad",
                        "http://bulkload.webservice.implementation.server.integration.etlforge.invalid",
                        NULL);
                _runBulkLoad->qname = qname;
            

            return _runBulkLoad;
        }

        axis2_status_t AXIS2_CALL
        adb_runBulkLoad_free (
                adb_runBulkLoad_t* _runBulkLoad,
                const axutil_env_t *env)
        {
            
                int i = 0;
                int count = 0;
                void *element = NULL;
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_FAILURE);

            adb_runBulkLoad_reset_sessionId(_runBulkLoad, env);
            adb_runBulkLoad_reset_bulkloadParams(_runBulkLoad, env);
            adb_runBulkLoad_reset_salesforceEndpoint(_runBulkLoad, env);
            adb_runBulkLoad_reset_in3(_runBulkLoad, env);
            
              if(_runBulkLoad->qname)
              {
                  axutil_qname_free (_runBulkLoad->qname, env);
                  _runBulkLoad->qname = NULL;
              }
            

            if(_runBulkLoad)
            {
                AXIS2_FREE(env->allocator, _runBulkLoad);
                _runBulkLoad = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_runBulkLoad_deserialize(
                adb_runBulkLoad_t* _runBulkLoad,
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
          
               int i = 0;
               axutil_array_list_t *arr_list = NULL;
            
               int sequence_broken = 0;
               axiom_node_t *tmp_node = NULL;
            
            axutil_qname_t *element_qname = NULL; 
            
               axiom_node_t *first_node = NULL;
               axis2_bool_t is_early_node_valid = AXIS2_TRUE;
               axiom_node_t *current_node = NULL;
               axiom_element_t *current_element = NULL;
            
            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for runBulkLoad : "
                            "NULL elemenet can not be passed to deserialize");
                return AXIS2_FAILURE;
              }
              

                    current_element = (axiom_element_t *)axiom_node_get_data_element(parent, env);
                    qname = axiom_element_get_qname(current_element, env, parent);
                    if (axutil_qname_equals(qname, env, _runBulkLoad-> qname))
                    {
                        
                          first_node = axiom_node_get_first_child(parent, env);
                          
                    }
                    else
                    {
                        AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                              "Failed in building adb object for runBulkLoad : "
                              "Expected %s but returned %s",
                              axutil_qname_to_string(_runBulkLoad-> qname, env),
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
                                   
                                 element_qname = axutil_qname_create(env, "sessionId", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", NULL);
                                 

                           if (adb_WsBulkLoadSessionId_is_particle() ||  
                                (current_node   && current_element && (axutil_qname_equals(element_qname, env, qname))))
                           {
                              if( current_node   && current_element && (axutil_qname_equals(element_qname, env, qname)))
                              {
                                is_early_node_valid = AXIS2_TRUE;
                              }
                              
                                 
                                      element = (void*)adb_WsBulkLoadSessionId_create(env);

                                      status =  adb_WsBulkLoadSessionId_deserialize((adb_WsBulkLoadSessionId_t*)element,
                                                                            env, &current_node, &is_early_node_valid, AXIS2_FALSE);
                                      if(AXIS2_FAILURE == status)
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in building adb object for element sessionId");
                                      }
                                      else
                                      {
                                          status = adb_runBulkLoad_set_sessionId(_runBulkLoad, env,
                                                                   (adb_WsBulkLoadSessionId_t*)element);
                                      }
                                    
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for sessionId ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element sessionId missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "bulkloadParams", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", NULL);
                                 

                           if (adb_WsBulkLoadParams_is_particle() ||  
                                (current_node   && current_element && (axutil_qname_equals(element_qname, env, qname))))
                           {
                              if( current_node   && current_element && (axutil_qname_equals(element_qname, env, qname)))
                              {
                                is_early_node_valid = AXIS2_TRUE;
                              }
                              
                                 
                                      element = (void*)adb_WsBulkLoadParams_create(env);

                                      status =  adb_WsBulkLoadParams_deserialize((adb_WsBulkLoadParams_t*)element,
                                                                            env, &current_node, &is_early_node_valid, AXIS2_FALSE);
                                      if(AXIS2_FAILURE == status)
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in building adb object for element bulkloadParams");
                                      }
                                      else
                                      {
                                          status = adb_runBulkLoad_set_bulkloadParams(_runBulkLoad, env,
                                                                   (adb_WsBulkLoadParams_t*)element);
                                      }
                                    
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for bulkloadParams ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element bulkloadParams missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "salesforceEndpoint", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", NULL);
                                 

                           if (adb_WsSalesforceEndpoint_is_particle() ||  
                                (current_node   && current_element && (axutil_qname_equals(element_qname, env, qname))))
                           {
                              if( current_node   && current_element && (axutil_qname_equals(element_qname, env, qname)))
                              {
                                is_early_node_valid = AXIS2_TRUE;
                              }
                              
                                 
                                      element = (void*)adb_WsSalesforceEndpoint_create(env);

                                      status =  adb_WsSalesforceEndpoint_deserialize((adb_WsSalesforceEndpoint_t*)element,
                                                                            env, &current_node, &is_early_node_valid, AXIS2_FALSE);
                                      if(AXIS2_FAILURE == status)
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in building adb object for element salesforceEndpoint");
                                      }
                                      else
                                      {
                                          status = adb_runBulkLoad_set_salesforceEndpoint(_runBulkLoad, env,
                                                                   (adb_WsSalesforceEndpoint_t*)element);
                                      }
                                    
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for salesforceEndpoint ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element salesforceEndpoint missing");
                                  return AXIS2_FAILURE;
                              }
                           
                  if(element_qname)
                  {
                     axutil_qname_free(element_qname, env);
                     element_qname = NULL;
                  }
                 
                      
                                         
                       
                       arr_list = axutil_array_list_create(env, 10);
                   

                     
                       
                                            
                        
                     
                     
                     
                                    element_qname = axutil_qname_create(env, "in3", "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", NULL);
                                  
                               
                               for (i = 0, sequence_broken = 0, current_node = (is_early_node_valid?axiom_node_get_next_sibling(current_node, env):current_node); !sequence_broken && current_node != NULL;) 
                                             
                               {
                                  if(axiom_node_get_node_type(current_node, env) != AXIOM_ELEMENT)
                                  {
                                     current_node =axiom_node_get_next_sibling(current_node, env);
                                     is_early_node_valid = AXIS2_FALSE;
                                     continue;
                                  }
                                  
                                  current_element = (axiom_element_t *)axiom_node_get_data_element(current_node, env);
                                  qname = axiom_element_get_qname(current_element, env, current_node);

                                  if (axutil_qname_equals(element_qname, env, qname))
                                  {
                                  
                                      is_early_node_valid = AXIS2_TRUE;
                                      
                                     
                                          element = (void*)adb_WsKeyValue_create(env);
                                          
                                          status =  adb_WsKeyValue_deserialize((adb_WsKeyValue_t*)element, env,
                                                                                 &current_node, &is_early_node_valid, AXIS2_FALSE);
                                          
                                          if(AXIS2_FAILURE ==  status)
                                          {
                                              AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in building element in3 ");
                                          }
                                          else
                                          {
                                            axutil_array_list_add_at(arr_list, env, i, element);
                                          }
                                        
                                     if(AXIS2_FAILURE ==  status)
                                     {
                                         AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for in3 ");
                                         if(element_qname)
                                         {
                                            axutil_qname_free(element_qname, env);
                                         }
                                         if(arr_list)
                                         {
                                            axutil_array_list_free(arr_list, env);
                                         }
                                         return AXIS2_FAILURE;
                                     }

                                     i ++;
                                    current_node = axiom_node_get_next_sibling(current_node, env);
                                  }
                                  else
                                  {
                                      is_early_node_valid = AXIS2_FALSE;
                                      sequence_broken = 1;
                                  }
                                  
                               }

                               
                                   if (i < 1)
                                   {
                                                                     
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "in3 (@minOccurs = '1') only have %d elements", i);
                                     if(element_qname)
                                     {
                                        axutil_qname_free(element_qname, env);
                                     }
                                     if(arr_list)
                                     {
                                        axutil_array_list_free(arr_list, env);
                                     }
                                     return AXIS2_FAILURE;
                                   }
                               

                               if(0 == axutil_array_list_size(arr_list,env))
                               {
                                    axutil_array_list_free(arr_list, env);
                               }
                               else
                               {
                                    status = adb_runBulkLoad_set_in3(_runBulkLoad, env,
                                                                   arr_list);
                               }

                             
                  if(element_qname)
                  {
                     axutil_qname_free(element_qname, env);
                     element_qname = NULL;
                  }
                 
          return status;
       }

          axis2_bool_t AXIS2_CALL
          adb_runBulkLoad_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_runBulkLoad_declare_parent_namespaces(
                    adb_runBulkLoad_t* _runBulkLoad,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_runBulkLoad_serialize(
                adb_runBulkLoad_t* _runBulkLoad,
                const axutil_env_t *env, axiom_node_t *parent, axiom_element_t *parent_element, int parent_tag_closed, axutil_hash_t *namespaces, int *next_ns_index)
        {
            
            
         
         axiom_node_t *current_node = NULL;
         int tag_closed = 0;

         
         
                axiom_namespace_t *ns1 = NULL;

                axis2_char_t *qname_uri = NULL;
                axis2_char_t *qname_prefix = NULL;
                axis2_char_t *p_prefix = NULL;
                axis2_bool_t ns_already_defined;
            
               int i = 0;
               int count = 0;
               void *element = NULL;
             
                    axis2_char_t text_value_1[64];
                    
                    axis2_char_t text_value_2[64];
                    
                    axis2_char_t text_value_3[64];
                    
                    axis2_char_t text_value_4[64];
                    
               axis2_char_t *start_input_str = NULL;
               axis2_char_t *end_input_str = NULL;
               unsigned int start_input_str_len = 0;
               unsigned int end_input_str_len = 0;
            
            
               axiom_data_source_t *data_source = NULL;
               axutil_stream_t *stream = NULL;

             
                int next_ns_index_value = 0;
            

            AXIS2_ENV_CHECK(env, NULL);
            AXIS2_PARAM_CHECK(env->error, _runBulkLoad, NULL);
            
             
                    namespaces = axutil_hash_make(env);
                    next_ns_index = &next_ns_index_value;
                     
                           ns1 = axiom_namespace_create (env,
                                             "http://bulkload.webservice.implementation.server.integration.etlforge.invalid",
                                             "n"); 
                           axutil_hash_set(namespaces, "http://bulkload.webservice.implementation.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, axutil_strdup(env, "n"));
                       
                     
                    parent_element = axiom_element_create (env, NULL, "runBulkLoad", ns1 , &parent);
                    
                    
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
                      

                   if (!_runBulkLoad->is_valid_sessionId)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property sessionId");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("sessionId"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("sessionId")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                 
                        

                    
                    
                            sprintf(start_input_str, "<%s%ssessionId",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":""); 
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%ssessionId>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                     
                            if(!adb_WsBulkLoadSessionId_is_particle())
                            {
                                axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                            }
                            
                            adb_WsBulkLoadSessionId_serialize(_runBulkLoad->property_sessionId, 
                                                                                 env, current_node, parent_element,
                                                                                 adb_WsBulkLoadSessionId_is_particle() || AXIS2_FALSE, namespaces, next_ns_index);
                            
                            if(!adb_WsBulkLoadSessionId_is_particle())
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
                      

                   if (!_runBulkLoad->is_valid_bulkloadParams)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property bulkloadParams");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("bulkloadParams"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("bulkloadParams")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                      
                        

                    
                    
                            sprintf(start_input_str, "<%s%sbulkloadParams",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":""); 
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sbulkloadParams>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                     
                            if(!adb_WsBulkLoadParams_is_particle())
                            {
                                axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                            }
                            
                            adb_WsBulkLoadParams_serialize(_runBulkLoad->property_bulkloadParams, 
                                                                                 env, current_node, parent_element,
                                                                                 adb_WsBulkLoadParams_is_particle() || AXIS2_FALSE, namespaces, next_ns_index);
                            
                            if(!adb_WsBulkLoadParams_is_particle())
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
                      

                   if (!_runBulkLoad->is_valid_salesforceEndpoint)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property salesforceEndpoint");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("salesforceEndpoint"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("salesforceEndpoint")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                          
                        

                    
                    
                            sprintf(start_input_str, "<%s%ssalesforceEndpoint",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":""); 
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%ssalesforceEndpoint>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                     
                            if(!adb_WsSalesforceEndpoint_is_particle())
                            {
                                axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                            }
                            
                            adb_WsSalesforceEndpoint_serialize(_runBulkLoad->property_salesforceEndpoint, 
                                                                                 env, current_node, parent_element,
                                                                                 adb_WsSalesforceEndpoint_is_particle() || AXIS2_FALSE, namespaces, next_ns_index);
                            
                            if(!adb_WsSalesforceEndpoint_is_particle())
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
                      

                   if (!_runBulkLoad->is_valid_in3)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property in3");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("in3"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("in3")));
                                                                     
                                  
                     

                   
                   
                       
                                         
                        
                     if (_runBulkLoad->property_in3 != NULL)
                     {
                        

                            sprintf(start_input_str, "<%s%sin3",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                         start_input_str_len = axutil_strlen(start_input_str);

                         sprintf(end_input_str, "</%s%sin3>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                         end_input_str_len = axutil_strlen(end_input_str);

                         count = axutil_array_list_size(_runBulkLoad->property_in3, env);
                         for(i = 0; i < count; i ++)
                         {
                            element = axutil_array_list_get(_runBulkLoad->property_in3, env, i);

                            if(NULL == element) 
                            {
                                continue;
                            }
                    
                     
                       
                                           
                        

                    
                     
                            if(!adb_WsKeyValue_is_particle())
                            {
                                axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                            }
                            
                            adb_WsKeyValue_serialize((adb_WsKeyValue_t*)element, 
                                                                                 env, current_node, parent_element,
                                                                                 adb_WsKeyValue_is_particle() || AXIS2_FALSE, namespaces, next_ns_index);
                            
                            if(!adb_WsKeyValue_is_particle())
                            {
                                axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                            }
                            
                         }
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


        

               
                                    
               
            adb_WsBulkLoadSessionId_t* AXIS2_CALL
            adb_runBulkLoad_get_sessionId(
                    adb_runBulkLoad_t* _runBulkLoad,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _runBulkLoad, NULL);
                  

                return _runBulkLoad->property_sessionId;
             }

               
                                   
               
            axis2_status_t AXIS2_CALL
            adb_runBulkLoad_set_sessionId(
                    adb_runBulkLoad_t* _runBulkLoad,
                    const axutil_env_t *env,
                    adb_WsBulkLoadSessionId_t*  arg_sessionId)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_FAILURE);
                
                if(_runBulkLoad->is_valid_sessionId &&
                        arg_sessionId == _runBulkLoad->property_sessionId)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                
                  if(NULL == arg_sessionId)
                  {
                      AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "sessionId is being set to NULL, but it is not a nullable element");
                      return AXIS2_FAILURE;
                  }
                adb_runBulkLoad_reset_sessionId(_runBulkLoad, env);

                
                if(NULL == arg_sessionId)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _runBulkLoad->property_sessionId = arg_sessionId;
                        _runBulkLoad->is_valid_sessionId = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                    
              
           axis2_status_t AXIS2_CALL
           adb_runBulkLoad_reset_sessionId(
                   adb_runBulkLoad_t* _runBulkLoad,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_FAILURE);
               

               
            
                
                if(_runBulkLoad->property_sessionId != NULL)
                {
                   
                   
                        adb_WsBulkLoadSessionId_free(_runBulkLoad->property_sessionId, env);
                     _runBulkLoad->property_sessionId = NULL;
                }
            
                
                
                _runBulkLoad->is_valid_sessionId = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                             
              
           axis2_bool_t AXIS2_CALL
           adb_runBulkLoad_is_sessionId_nil(
                   adb_runBulkLoad_t* _runBulkLoad,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_TRUE);
               
               return !_runBulkLoad->is_valid_sessionId;
           }

              
                                                                 
              
           axis2_status_t AXIS2_CALL
           adb_runBulkLoad_set_sessionId_nil(
                   adb_runBulkLoad_t* _runBulkLoad,
                   const axutil_env_t *env)
           {
               return adb_runBulkLoad_reset_sessionId(_runBulkLoad, env);
           }

           

               
                                         
               
            adb_WsBulkLoadParams_t* AXIS2_CALL
            adb_runBulkLoad_get_bulkloadParams(
                    adb_runBulkLoad_t* _runBulkLoad,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _runBulkLoad, NULL);
                  

                return _runBulkLoad->property_bulkloadParams;
             }

               
                                        
               
            axis2_status_t AXIS2_CALL
            adb_runBulkLoad_set_bulkloadParams(
                    adb_runBulkLoad_t* _runBulkLoad,
                    const axutil_env_t *env,
                    adb_WsBulkLoadParams_t*  arg_bulkloadParams)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_FAILURE);
                
                if(_runBulkLoad->is_valid_bulkloadParams &&
                        arg_bulkloadParams == _runBulkLoad->property_bulkloadParams)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                
                  if(NULL == arg_bulkloadParams)
                  {
                      AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "bulkloadParams is being set to NULL, but it is not a nullable element");
                      return AXIS2_FAILURE;
                  }
                adb_runBulkLoad_reset_bulkloadParams(_runBulkLoad, env);

                
                if(NULL == arg_bulkloadParams)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _runBulkLoad->property_bulkloadParams = arg_bulkloadParams;
                        _runBulkLoad->is_valid_bulkloadParams = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                         
              
           axis2_status_t AXIS2_CALL
           adb_runBulkLoad_reset_bulkloadParams(
                   adb_runBulkLoad_t* _runBulkLoad,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_FAILURE);
               

               
            
                
                if(_runBulkLoad->property_bulkloadParams != NULL)
                {
                   
                   
                        adb_WsBulkLoadParams_free(_runBulkLoad->property_bulkloadParams, env);
                     _runBulkLoad->property_bulkloadParams = NULL;
                }
            
                
                
                _runBulkLoad->is_valid_bulkloadParams = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                  
              
           axis2_bool_t AXIS2_CALL
           adb_runBulkLoad_is_bulkloadParams_nil(
                   adb_runBulkLoad_t* _runBulkLoad,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_TRUE);
               
               return !_runBulkLoad->is_valid_bulkloadParams;
           }

              
                                                                      
              
           axis2_status_t AXIS2_CALL
           adb_runBulkLoad_set_bulkloadParams_nil(
                   adb_runBulkLoad_t* _runBulkLoad,
                   const axutil_env_t *env)
           {
               return adb_runBulkLoad_reset_bulkloadParams(_runBulkLoad, env);
           }

           

               
                                             
               
            adb_WsSalesforceEndpoint_t* AXIS2_CALL
            adb_runBulkLoad_get_salesforceEndpoint(
                    adb_runBulkLoad_t* _runBulkLoad,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _runBulkLoad, NULL);
                  

                return _runBulkLoad->property_salesforceEndpoint;
             }

               
                                            
               
            axis2_status_t AXIS2_CALL
            adb_runBulkLoad_set_salesforceEndpoint(
                    adb_runBulkLoad_t* _runBulkLoad,
                    const axutil_env_t *env,
                    adb_WsSalesforceEndpoint_t*  arg_salesforceEndpoint)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_FAILURE);
                
                if(_runBulkLoad->is_valid_salesforceEndpoint &&
                        arg_salesforceEndpoint == _runBulkLoad->property_salesforceEndpoint)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                
                  if(NULL == arg_salesforceEndpoint)
                  {
                      AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "salesforceEndpoint is being set to NULL, but it is not a nullable element");
                      return AXIS2_FAILURE;
                  }
                adb_runBulkLoad_reset_salesforceEndpoint(_runBulkLoad, env);

                
                if(NULL == arg_salesforceEndpoint)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _runBulkLoad->property_salesforceEndpoint = arg_salesforceEndpoint;
                        _runBulkLoad->is_valid_salesforceEndpoint = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                             
              
           axis2_status_t AXIS2_CALL
           adb_runBulkLoad_reset_salesforceEndpoint(
                   adb_runBulkLoad_t* _runBulkLoad,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_FAILURE);
               

               
            
                
                if(_runBulkLoad->property_salesforceEndpoint != NULL)
                {
                   
                   
                        adb_WsSalesforceEndpoint_free(_runBulkLoad->property_salesforceEndpoint, env);
                     _runBulkLoad->property_salesforceEndpoint = NULL;
                }
            
                
                
                _runBulkLoad->is_valid_salesforceEndpoint = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                      
              
           axis2_bool_t AXIS2_CALL
           adb_runBulkLoad_is_salesforceEndpoint_nil(
                   adb_runBulkLoad_t* _runBulkLoad,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_TRUE);
               
               return !_runBulkLoad->is_valid_salesforceEndpoint;
           }

              
                                                                          
              
           axis2_status_t AXIS2_CALL
           adb_runBulkLoad_set_salesforceEndpoint_nil(
                   adb_runBulkLoad_t* _runBulkLoad,
                   const axutil_env_t *env)
           {
               return adb_runBulkLoad_reset_salesforceEndpoint(_runBulkLoad, env);
           }

           

               
                              
               
            axutil_array_list_t* AXIS2_CALL
            adb_runBulkLoad_get_in3(
                    adb_runBulkLoad_t* _runBulkLoad,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _runBulkLoad, NULL);
                  

                return _runBulkLoad->property_in3;
             }

               
                             
               
            axis2_status_t AXIS2_CALL
            adb_runBulkLoad_set_in3(
                    adb_runBulkLoad_t* _runBulkLoad,
                    const axutil_env_t *env,
                    axutil_array_list_t*  arg_in3)
             {
                
                 int size = 0;
                 int i = 0;
                 axis2_bool_t non_nil_exists = AXIS2_FALSE;
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_FAILURE);
                
                if(_runBulkLoad->is_valid_in3 &&
                        arg_in3 == _runBulkLoad->property_in3)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                
                 size = axutil_array_list_size(arg_in3, env);
                 
                 if (size < 1)
                 {
                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "in3 has less than minOccurs(1)");
                     return AXIS2_FAILURE;
                 }
                 for(i = 0; i < size; i ++ )
                 {
                     if(NULL != axutil_array_list_get(arg_in3, env, i))
                     {
                         non_nil_exists = AXIS2_TRUE;
                         break;
                     }
                 }

                 
                    if(!non_nil_exists)
                    {
                        AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "All the elements in the array of in3 is being set to NULL, but it is not a nullable or minOccurs=0 element");
                        return AXIS2_FAILURE;
                    }
                 
                  if(NULL == arg_in3)
                  {
                      AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "in3 is being set to NULL, but it is not a nullable element");
                      return AXIS2_FAILURE;
                  }
                adb_runBulkLoad_reset_in3(_runBulkLoad, env);

                
                if(NULL == arg_in3)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _runBulkLoad->property_in3 = arg_in3;
                        if(non_nil_exists)
                        {
                            _runBulkLoad->is_valid_in3 = AXIS2_TRUE;
                        }
                        
                    
                return AXIS2_SUCCESS;
             }

            
               
                                      
               
            adb_WsKeyValue_t* AXIS2_CALL
            adb_runBulkLoad_get_in3_at(
                    adb_runBulkLoad_t* _runBulkLoad,
                    const axutil_env_t *env, int i)
            {
                adb_WsKeyValue_t* ret_val;

                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _runBulkLoad, NULL);
                  

                if(_runBulkLoad->property_in3 == NULL)
                {
                    return (adb_WsKeyValue_t*)0;
                }
                ret_val = (adb_WsKeyValue_t*)axutil_array_list_get(_runBulkLoad->property_in3, env, i);
                
                    return ret_val;
                  
            }

               
                                          
               
            axis2_status_t AXIS2_CALL
            adb_runBulkLoad_set_in3_at(
                    adb_runBulkLoad_t* _runBulkLoad,
                    const axutil_env_t *env, int i,
                    adb_WsKeyValue_t* arg_in3)
            {
                void *element = NULL;
                int size = 0;
                int j;
                int k;
                axis2_bool_t non_nil_exists = AXIS2_FALSE;

                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_FAILURE);
                
                if( _runBulkLoad->is_valid_in3 &&
                    _runBulkLoad->property_in3 &&
                
                    arg_in3 == (adb_WsKeyValue_t*)axutil_array_list_get(_runBulkLoad->property_in3, env, i))
                  
                {
                    
                    return AXIS2_SUCCESS; 
                }

                
                    if(NULL == arg_in3)
                    {
                        if(_runBulkLoad->property_in3 != NULL)
                        {
                            size = axutil_array_list_size(_runBulkLoad->property_in3, env);
                            for(j = 0, k = 0; j < size; j ++ )
                            {
                                if(i == j) continue; 
                                if(NULL != axutil_array_list_get(_runBulkLoad->property_in3, env, i))
                                {
                                    k ++;
                                    non_nil_exists = AXIS2_TRUE;
                                    if(k >= 1)
                                    {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        non_nil_exists = AXIS2_TRUE;
                    }
                  
                   if(!non_nil_exists)
                   {
                       AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "All the elements in the array of in3 is being set to NULL, but it is not a nullable or minOccurs=0 element");
                       return AXIS2_FAILURE;
                   }
                
                if( k < 1)
                {
                       AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Size of the array of in3 is beinng set to be smaller than the specificed number of minOccurs(1)");
                       return AXIS2_FAILURE;
                }

                if(_runBulkLoad->property_in3 == NULL)
                {
                    _runBulkLoad->property_in3 = axutil_array_list_create(env, 10);
                }
                
                                                                  
                element = axutil_array_list_get(_runBulkLoad->property_in3, env, i);
                if(NULL != element)
                {
                  
                  
                  
                        adb_WsKeyValue_free((adb_WsKeyValue_t*)element, env);
                     
                }

                
                   axutil_array_list_set(_runBulkLoad->property_in3 , env, i, arg_in3);
                  _runBulkLoad->is_valid_in3 = AXIS2_TRUE;
                
                return AXIS2_SUCCESS;
            }

               
                          
               
            axis2_status_t AXIS2_CALL
            adb_runBulkLoad_add_in3(
                    adb_runBulkLoad_t* _runBulkLoad,
                    const axutil_env_t *env,
                    adb_WsKeyValue_t* arg_in3)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_FAILURE);

                
                    if(NULL == arg_in3)
                    {
                      
                           AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "All the elements in the array of in3 is being set to NULL, but it is not a nullable or minOccurs=0 element");
                           return AXIS2_FAILURE;
                        
                    }
                  

                if(_runBulkLoad->property_in3 == NULL)
                {
                    _runBulkLoad->property_in3 = axutil_array_list_create(env, 10);
                }
                if(_runBulkLoad->property_in3 == NULL)
                {
                    AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Failed in allocatting memory for in3");
                    return AXIS2_FAILURE;
                    
                }
                
                   axutil_array_list_add(_runBulkLoad->property_in3 , env, arg_in3);
                  _runBulkLoad->is_valid_in3 = AXIS2_TRUE;
                return AXIS2_SUCCESS;
             }

               
                                             
               
            int AXIS2_CALL
            adb_runBulkLoad_sizeof_in3(
                    adb_runBulkLoad_t* _runBulkLoad,
                    const axutil_env_t *env)
            {
                AXIS2_ENV_CHECK(env, -1);
                AXIS2_PARAM_CHECK(env->error, _runBulkLoad, -1);
                if(_runBulkLoad->property_in3 == NULL)
                {
                    return 0;
                }
                return axutil_array_list_size(_runBulkLoad->property_in3, env);
            }

               
                                                          
               
            axis2_status_t AXIS2_CALL
            adb_runBulkLoad_remove_in3_at(
                    adb_runBulkLoad_t* _runBulkLoad,
                    const axutil_env_t *env, int i)
            {
                return adb_runBulkLoad_set_in3_nil_at(_runBulkLoad, env, i);
            }

            

              
                              
              
           axis2_status_t AXIS2_CALL
           adb_runBulkLoad_reset_in3(
                   adb_runBulkLoad_t* _runBulkLoad,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_FAILURE);
               

               
                  if (_runBulkLoad->property_in3 != NULL)
                  {
                      count = axutil_array_list_size(_runBulkLoad->property_in3, env);
                      for(i = 0; i < count; i ++)
                      {
                         element = axutil_array_list_get(_runBulkLoad->property_in3, env, i);
                
            
                
                if(element != NULL)
                {
                   
                   
                        adb_WsKeyValue_free((adb_WsKeyValue_t*)element, env);
                     element = NULL;
                }
            
                
                
                
                      }
                      axutil_array_list_free(_runBulkLoad->property_in3, env);
                  }
                _runBulkLoad->is_valid_in3 = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                       
              
           axis2_bool_t AXIS2_CALL
           adb_runBulkLoad_is_in3_nil(
                   adb_runBulkLoad_t* _runBulkLoad,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_TRUE);
               
               return !_runBulkLoad->is_valid_in3;
           }

              
                                                           
              
           axis2_status_t AXIS2_CALL
           adb_runBulkLoad_set_in3_nil(
                   adb_runBulkLoad_t* _runBulkLoad,
                   const axutil_env_t *env)
           {
               return adb_runBulkLoad_reset_in3(_runBulkLoad, env);
           }

           
              
                                            
              
           axis2_bool_t AXIS2_CALL
           adb_runBulkLoad_is_in3_nil_at(
                   adb_runBulkLoad_t* _runBulkLoad,
                   const axutil_env_t *env, int i)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_TRUE);
               
               return (_runBulkLoad->is_valid_in3 == AXIS2_FALSE ||
                        NULL == _runBulkLoad->property_in3 || 
                        NULL == axutil_array_list_get(_runBulkLoad->property_in3, env, i));
           }

              
                                  
              
           axis2_status_t AXIS2_CALL
           adb_runBulkLoad_set_in3_nil_at(
                   adb_runBulkLoad_t* _runBulkLoad,
                   const axutil_env_t *env, int i)
           {
                void *element = NULL;
                int size = 0;
                int j;
                axis2_bool_t non_nil_exists = AXIS2_FALSE;

                int k = 0;

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _runBulkLoad, AXIS2_FAILURE);

                if(_runBulkLoad->property_in3 == NULL ||
                            _runBulkLoad->is_valid_in3 == AXIS2_FALSE)
                {
                    
                    non_nil_exists = AXIS2_FALSE;
                }
                else
                {
                    size = axutil_array_list_size(_runBulkLoad->property_in3, env);
                    for(j = 0, k = 0; j < size; j ++ )
                    {
                        if(i == j) continue; 
                        if(NULL != axutil_array_list_get(_runBulkLoad->property_in3, env, i))
                        {
                            k ++;
                            non_nil_exists = AXIS2_TRUE;
                            if( k >= 1)
                            {
                                break;
                            }
                        }
                    }
                }
                
                   if(!non_nil_exists)
                   {
                       AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "All the elements in the array of in3 is being set to NULL, but it is not a nullable or minOccurs=0 element");
                       return AXIS2_FAILURE;
                   }
                

                if( k < 1)
                {
                       AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Size of the array of in3 is beinng set to be smaller than the specificed number of minOccurs(1)");
                       return AXIS2_FAILURE;
                }
 
                if(_runBulkLoad->property_in3 == NULL)
                {
                    _runBulkLoad->is_valid_in3 = AXIS2_FALSE;
                    
                    return AXIS2_SUCCESS;
                }

                                                                  
                element = axutil_array_list_get(_runBulkLoad->property_in3, env, i);
                if(NULL != element)
                {
                  
                  
                  
                        adb_WsKeyValue_free((adb_WsKeyValue_t*)element, env);
                     
                }

                

                
                axutil_array_list_set(_runBulkLoad->property_in3 , env, i, NULL);
                
                return AXIS2_SUCCESS;

           }

           

