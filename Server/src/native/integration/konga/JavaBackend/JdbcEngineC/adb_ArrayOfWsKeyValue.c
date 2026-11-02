

           
                                  
          
                                                 
                                                                                                
           

        #include "adb_ArrayOfWsKeyValue.h"
        
                  
                                                                            
                                           
                                                                                                     
                                         
                   
           


        struct adb_ArrayOfWsKeyValue
        {
            axutil_array_list_t* property_item;

                
                axis2_bool_t is_valid_item;


            
        };


                                                                                               
        
                 axis2_status_t AXIS2_CALL
                 adb_ArrayOfWsKeyValue_set_item_nil_at(
                        adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue, 
                        const axutil_env_t *env, int i);
                

                axis2_status_t AXIS2_CALL
                adb_ArrayOfWsKeyValue_set_item_nil(
                        adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                        const axutil_env_t *env);
            


                                                                                           
        adb_ArrayOfWsKeyValue_t* AXIS2_CALL
        adb_ArrayOfWsKeyValue_create(
            const axutil_env_t *env)
        {
            adb_ArrayOfWsKeyValue_t *_ArrayOfWsKeyValue = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _ArrayOfWsKeyValue = (adb_ArrayOfWsKeyValue_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_ArrayOfWsKeyValue_t));

            if(NULL == _ArrayOfWsKeyValue)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_ArrayOfWsKeyValue, 0, sizeof(adb_ArrayOfWsKeyValue_t));

            _ArrayOfWsKeyValue->property_item  = NULL;
                  _ArrayOfWsKeyValue->is_valid_item  = AXIS2_FALSE;
            

            return _ArrayOfWsKeyValue;
        }

        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsKeyValue_free (
                adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                const axutil_env_t *env)
        {
            
                int i = 0;
                int count = 0;
                void *element = NULL;
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _ArrayOfWsKeyValue, AXIS2_FAILURE);

            adb_ArrayOfWsKeyValue_reset_item(_ArrayOfWsKeyValue, env);
            

            if(_ArrayOfWsKeyValue)
            {
                AXIS2_FREE(env->allocator, _ArrayOfWsKeyValue);
                _ArrayOfWsKeyValue = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_ArrayOfWsKeyValue_deserialize(
                adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
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
            AXIS2_PARAM_CHECK(env->error, _ArrayOfWsKeyValue, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for ArrayOfWsKeyValue : "
                            "NULL elemenet can not be passed to deserialize");
                return AXIS2_FAILURE;
              }
              
                      
                      first_node = axiom_node_get_first_child(parent, env);
                      
                    
                      
                                          
                       
                       arr_list = axutil_array_list_create(env, 10);
                   

                     
                       
                                             
                        
                     
                     
                     
                                    element_qname = axutil_qname_create(env, "item", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                  
                               
                               for (i = 0, sequence_broken = 0, current_node = first_node; !sequence_broken && current_node != NULL;) 
                                             
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
                                              AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in building element item ");
                                          }
                                          else
                                          {
                                            axutil_array_list_add_at(arr_list, env, i, element);
                                          }
                                        
                                     if(AXIS2_FAILURE ==  status)
                                     {
                                         AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for item ");
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

                               
                                   if (i < 0)
                                   {
                                                                     
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "item (@minOccurs = '0') only have %d elements", i);
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
                                    status = adb_ArrayOfWsKeyValue_set_item(_ArrayOfWsKeyValue, env,
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
          adb_ArrayOfWsKeyValue_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_ArrayOfWsKeyValue_declare_parent_namespaces(
                    adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_ArrayOfWsKeyValue_serialize(
                adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
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
            
               int i = 0;
               int count = 0;
               void *element = NULL;
             
                    axis2_char_t text_value_1[64];
                    
               axis2_char_t *start_input_str = NULL;
               axis2_char_t *end_input_str = NULL;
               unsigned int start_input_str_len = 0;
               unsigned int end_input_str_len = 0;
            
            
               axiom_data_source_t *data_source = NULL;
               axutil_stream_t *stream = NULL;

            

            AXIS2_ENV_CHECK(env, NULL);
            AXIS2_PARAM_CHECK(env->error, _ArrayOfWsKeyValue, NULL);
            
            
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
                      

                   if (!_ArrayOfWsKeyValue->is_valid_item)
                   {
                      
                                                                            
                            
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("item"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("item")));
                                                                     
                                  
                     

                   
                   
                       
                                          
                        
                     if (_ArrayOfWsKeyValue->property_item != NULL)
                     {
                        

                            sprintf(start_input_str, "<%s%sitem",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                         start_input_str_len = axutil_strlen(start_input_str);

                         sprintf(end_input_str, "</%s%sitem>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                         end_input_str_len = axutil_strlen(end_input_str);

                         count = axutil_array_list_size(_ArrayOfWsKeyValue->property_item, env);
                         for(i = 0; i < count; i ++)
                         {
                            element = axutil_array_list_get(_ArrayOfWsKeyValue->property_item, env, i);

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

                 

            return parent;
        }


        

               
                               
               
            axutil_array_list_t* AXIS2_CALL
            adb_ArrayOfWsKeyValue_get_item(
                    adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _ArrayOfWsKeyValue, NULL);
                  

                return _ArrayOfWsKeyValue->property_item;
             }

               
                              
               
            axis2_status_t AXIS2_CALL
            adb_ArrayOfWsKeyValue_set_item(
                    adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                    const axutil_env_t *env,
                    axutil_array_list_t*  arg_item)
             {
                
                 int size = 0;
                 int i = 0;
                 axis2_bool_t non_nil_exists = AXIS2_FALSE;
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _ArrayOfWsKeyValue, AXIS2_FAILURE);
                
                if(_ArrayOfWsKeyValue->is_valid_item &&
                        arg_item == _ArrayOfWsKeyValue->property_item)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                
                 size = axutil_array_list_size(arg_item, env);
                 
                 if (size < 0)
                 {
                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "item has less than minOccurs(0)");
                     return AXIS2_FAILURE;
                 }
                 for(i = 0; i < size; i ++ )
                 {
                     if(NULL != axutil_array_list_get(arg_item, env, i))
                     {
                         non_nil_exists = AXIS2_TRUE;
                         break;
                     }
                 }

                 adb_ArrayOfWsKeyValue_reset_item(_ArrayOfWsKeyValue, env);

                
                if(NULL == arg_item)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _ArrayOfWsKeyValue->property_item = arg_item;
                        if(non_nil_exists)
                        {
                            _ArrayOfWsKeyValue->is_valid_item = AXIS2_TRUE;
                        }
                        
                    
                return AXIS2_SUCCESS;
             }

            
               
                                       
               
            adb_WsKeyValue_t* AXIS2_CALL
            adb_ArrayOfWsKeyValue_get_item_at(
                    adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                    const axutil_env_t *env, int i)
            {
                adb_WsKeyValue_t* ret_val;

                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _ArrayOfWsKeyValue, NULL);
                  

                if(_ArrayOfWsKeyValue->property_item == NULL)
                {
                    return (adb_WsKeyValue_t*)0;
                }
                ret_val = (adb_WsKeyValue_t*)axutil_array_list_get(_ArrayOfWsKeyValue->property_item, env, i);
                
                    return ret_val;
                  
            }

               
                                           
               
            axis2_status_t AXIS2_CALL
            adb_ArrayOfWsKeyValue_set_item_at(
                    adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                    const axutil_env_t *env, int i,
                    adb_WsKeyValue_t* arg_item)
            {
                void *element = NULL;
                int size = 0;
                int j;
                int k;
                axis2_bool_t non_nil_exists = AXIS2_FALSE;

                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _ArrayOfWsKeyValue, AXIS2_FAILURE);
                
                if( _ArrayOfWsKeyValue->is_valid_item &&
                    _ArrayOfWsKeyValue->property_item &&
                
                    arg_item == (adb_WsKeyValue_t*)axutil_array_list_get(_ArrayOfWsKeyValue->property_item, env, i))
                  
                {
                    
                    return AXIS2_SUCCESS; 
                }

                
                    if(NULL == arg_item)
                    {
                        if(_ArrayOfWsKeyValue->property_item != NULL)
                        {
                            size = axutil_array_list_size(_ArrayOfWsKeyValue->property_item, env);
                            for(j = 0, k = 0; j < size; j ++ )
                            {
                                if(i == j) continue; 
                                if(NULL != axutil_array_list_get(_ArrayOfWsKeyValue->property_item, env, i))
                                {
                                    k ++;
                                    non_nil_exists = AXIS2_TRUE;
                                    if(k >= 0)
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
                  
                if( k < 0)
                {
                       AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Size of the array of item is beinng set to be smaller than the specificed number of minOccurs(0)");
                       return AXIS2_FAILURE;
                }

                if(_ArrayOfWsKeyValue->property_item == NULL)
                {
                    _ArrayOfWsKeyValue->property_item = axutil_array_list_create(env, 10);
                }
                
                                                                  
                element = axutil_array_list_get(_ArrayOfWsKeyValue->property_item, env, i);
                if(NULL != element)
                {
                  
                  
                  
                        adb_WsKeyValue_free((adb_WsKeyValue_t*)element, env);
                     
                }

                
                    if(!non_nil_exists)
                    {
                        
                        _ArrayOfWsKeyValue->is_valid_item = AXIS2_FALSE;
                        axutil_array_list_set(_ArrayOfWsKeyValue->property_item , env, i, NULL);
                        
                        return AXIS2_SUCCESS;
                    }
                
                   axutil_array_list_set(_ArrayOfWsKeyValue->property_item , env, i, arg_item);
                  _ArrayOfWsKeyValue->is_valid_item = AXIS2_TRUE;
                
                return AXIS2_SUCCESS;
            }

               
                           
               
            axis2_status_t AXIS2_CALL
            adb_ArrayOfWsKeyValue_add_item(
                    adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                    const axutil_env_t *env,
                    adb_WsKeyValue_t* arg_item)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _ArrayOfWsKeyValue, AXIS2_FAILURE);

                
                    if(NULL == arg_item)
                    {
                      
                           return AXIS2_SUCCESS; 
                        
                    }
                  

                if(_ArrayOfWsKeyValue->property_item == NULL)
                {
                    _ArrayOfWsKeyValue->property_item = axutil_array_list_create(env, 10);
                }
                if(_ArrayOfWsKeyValue->property_item == NULL)
                {
                    AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Failed in allocatting memory for item");
                    return AXIS2_FAILURE;
                    
                }
                
                   axutil_array_list_add(_ArrayOfWsKeyValue->property_item , env, arg_item);
                  _ArrayOfWsKeyValue->is_valid_item = AXIS2_TRUE;
                return AXIS2_SUCCESS;
             }

               
                                              
               
            int AXIS2_CALL
            adb_ArrayOfWsKeyValue_sizeof_item(
                    adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                    const axutil_env_t *env)
            {
                AXIS2_ENV_CHECK(env, -1);
                AXIS2_PARAM_CHECK(env->error, _ArrayOfWsKeyValue, -1);
                if(_ArrayOfWsKeyValue->property_item == NULL)
                {
                    return 0;
                }
                return axutil_array_list_size(_ArrayOfWsKeyValue->property_item, env);
            }

               
                                                          
               
            axis2_status_t AXIS2_CALL
            adb_ArrayOfWsKeyValue_remove_item_at(
                    adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                    const axutil_env_t *env, int i)
            {
                return adb_ArrayOfWsKeyValue_set_item_nil_at(_ArrayOfWsKeyValue, env, i);
            }

            

              
                               
              
           axis2_status_t AXIS2_CALL
           adb_ArrayOfWsKeyValue_reset_item(
                   adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _ArrayOfWsKeyValue, AXIS2_FAILURE);
               

               
                  if (_ArrayOfWsKeyValue->property_item != NULL)
                  {
                      count = axutil_array_list_size(_ArrayOfWsKeyValue->property_item, env);
                      for(i = 0; i < count; i ++)
                      {
                         element = axutil_array_list_get(_ArrayOfWsKeyValue->property_item, env, i);
                
            
                
                if(element != NULL)
                {
                   
                   
                        adb_WsKeyValue_free((adb_WsKeyValue_t*)element, env);
                     element = NULL;
                }
            
                
                
                
                      }
                      axutil_array_list_free(_ArrayOfWsKeyValue->property_item, env);
                  }
                _ArrayOfWsKeyValue->is_valid_item = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                        
              
           axis2_bool_t AXIS2_CALL
           adb_ArrayOfWsKeyValue_is_item_nil(
                   adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _ArrayOfWsKeyValue, AXIS2_TRUE);
               
               return !_ArrayOfWsKeyValue->is_valid_item;
           }

              
                                                            
              
           axis2_status_t AXIS2_CALL
           adb_ArrayOfWsKeyValue_set_item_nil(
                   adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                   const axutil_env_t *env)
           {
               return adb_ArrayOfWsKeyValue_reset_item(_ArrayOfWsKeyValue, env);
           }

           
              
                                             
              
           axis2_bool_t AXIS2_CALL
           adb_ArrayOfWsKeyValue_is_item_nil_at(
                   adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                   const axutil_env_t *env, int i)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _ArrayOfWsKeyValue, AXIS2_TRUE);
               
               return (_ArrayOfWsKeyValue->is_valid_item == AXIS2_FALSE ||
                        NULL == _ArrayOfWsKeyValue->property_item || 
                        NULL == axutil_array_list_get(_ArrayOfWsKeyValue->property_item, env, i));
           }

              
                                   
              
           axis2_status_t AXIS2_CALL
           adb_ArrayOfWsKeyValue_set_item_nil_at(
                   adb_ArrayOfWsKeyValue_t* _ArrayOfWsKeyValue,
                   const axutil_env_t *env, int i)
           {
                void *element = NULL;
                int size = 0;
                int j;
                axis2_bool_t non_nil_exists = AXIS2_FALSE;

                int k = 0;

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _ArrayOfWsKeyValue, AXIS2_FAILURE);

                if(_ArrayOfWsKeyValue->property_item == NULL ||
                            _ArrayOfWsKeyValue->is_valid_item == AXIS2_FALSE)
                {
                    
                    non_nil_exists = AXIS2_FALSE;
                }
                else
                {
                    size = axutil_array_list_size(_ArrayOfWsKeyValue->property_item, env);
                    for(j = 0, k = 0; j < size; j ++ )
                    {
                        if(i == j) continue; 
                        if(NULL != axutil_array_list_get(_ArrayOfWsKeyValue->property_item, env, i))
                        {
                            k ++;
                            non_nil_exists = AXIS2_TRUE;
                            if( k >= 0)
                            {
                                break;
                            }
                        }
                    }
                }
                

                if( k < 0)
                {
                       AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Size of the array of item is beinng set to be smaller than the specificed number of minOccurs(0)");
                       return AXIS2_FAILURE;
                }
 
                if(_ArrayOfWsKeyValue->property_item == NULL)
                {
                    _ArrayOfWsKeyValue->is_valid_item = AXIS2_FALSE;
                    
                    return AXIS2_SUCCESS;
                }

                                                                  
                element = axutil_array_list_get(_ArrayOfWsKeyValue->property_item, env, i);
                if(NULL != element)
                {
                  
                  
                  
                        adb_WsKeyValue_free((adb_WsKeyValue_t*)element, env);
                     
                }

                
                    if(!non_nil_exists)
                    {
                        
                        _ArrayOfWsKeyValue->is_valid_item = AXIS2_FALSE;
                        axutil_array_list_set(_ArrayOfWsKeyValue->property_item , env, i, NULL);
                        return AXIS2_SUCCESS;
                    }
                

                
                axutil_array_list_set(_ArrayOfWsKeyValue->property_item , env, i, NULL);
                
                return AXIS2_SUCCESS;

           }

           

