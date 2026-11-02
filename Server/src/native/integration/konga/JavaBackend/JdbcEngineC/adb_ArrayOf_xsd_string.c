

           
                                   
          
                                                 
                                                                                                
           

        #include "adb_ArrayOf_xsd_string.h"
        
                  
                                                                            
                                            
                                                                                                     
                                         
                   
           


        struct adb_ArrayOf_xsd_string
        {
            axutil_array_list_t* property_item;

                
                axis2_bool_t is_valid_item;


            
        };


                                                                                               
        
                 axis2_status_t AXIS2_CALL
                 adb_ArrayOf_xsd_string_set_item_nil_at(
                        adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string, 
                        const axutil_env_t *env, int i);
                

                axis2_status_t AXIS2_CALL
                adb_ArrayOf_xsd_string_set_item_nil(
                        adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                        const axutil_env_t *env);
            


                                                                                           
        adb_ArrayOf_xsd_string_t* AXIS2_CALL
        adb_ArrayOf_xsd_string_create(
            const axutil_env_t *env)
        {
            adb_ArrayOf_xsd_string_t *_ArrayOf_xsd_string = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _ArrayOf_xsd_string = (adb_ArrayOf_xsd_string_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_ArrayOf_xsd_string_t));

            if(NULL == _ArrayOf_xsd_string)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_ArrayOf_xsd_string, 0, sizeof(adb_ArrayOf_xsd_string_t));

            _ArrayOf_xsd_string->property_item  = NULL;
                  _ArrayOf_xsd_string->is_valid_item  = AXIS2_FALSE;
            

            return _ArrayOf_xsd_string;
        }

        axis2_status_t AXIS2_CALL
        adb_ArrayOf_xsd_string_free (
                adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                const axutil_env_t *env)
        {
            
                int i = 0;
                int count = 0;
                void *element = NULL;
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _ArrayOf_xsd_string, AXIS2_FAILURE);

            adb_ArrayOf_xsd_string_reset_item(_ArrayOf_xsd_string, env);
            

            if(_ArrayOf_xsd_string)
            {
                AXIS2_FREE(env->allocator, _ArrayOf_xsd_string);
                _ArrayOf_xsd_string = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_ArrayOf_xsd_string_deserialize(
                adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                const axutil_env_t *env,
                axiom_node_t **dp_parent,
                axis2_bool_t *dp_is_early_node_valid,
                axis2_bool_t dont_care_minoccurs)
        {
          axiom_node_t *parent = *dp_parent;
          
          axis2_status_t status = AXIS2_SUCCESS;
           
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
            AXIS2_PARAM_CHECK(env->error, _ArrayOf_xsd_string, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for ArrayOf_xsd_string : "
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
                                      
                                     
                                          text_value = axiom_element_get_text(current_element, env, current_node);
                                          if(text_value != NULL)
                                          {
                                              axutil_array_list_add_at(arr_list, env, i, axutil_strdup(env, text_value));
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
                                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "NULL value is set to a non nillable element item");
                                                  status = AXIS2_FAILURE;
                                              }
                                              else
                                              {
                                                                                                  
                                                  axutil_array_list_add_at(arr_list, env, i, axutil_strdup(env, ""));
                                              }
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
                                    status = adb_ArrayOf_xsd_string_set_item(_ArrayOf_xsd_string, env,
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
          adb_ArrayOf_xsd_string_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_ArrayOf_xsd_string_declare_parent_namespaces(
                    adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_ArrayOf_xsd_string_serialize(
                adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
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
             
                    axis2_char_t *text_value_1;
                    axis2_char_t *text_value_1_temp;
                    
               axis2_char_t *start_input_str = NULL;
               axis2_char_t *end_input_str = NULL;
               unsigned int start_input_str_len = 0;
               unsigned int end_input_str_len = 0;
            
            
               axiom_data_source_t *data_source = NULL;
               axutil_stream_t *stream = NULL;

            

            AXIS2_ENV_CHECK(env, NULL);
            AXIS2_PARAM_CHECK(env->error, _ArrayOf_xsd_string, NULL);
            
            
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
                      

                   if (!_ArrayOf_xsd_string->is_valid_item)
                   {
                      
                                                                            
                            
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("item"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("item")));
                                                                     
                                  
                     

                   
                   
                       
                                          
                        
                     if (_ArrayOf_xsd_string->property_item != NULL)
                     {
                        
                            sprintf(start_input_str, "<%s%sitem>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":""); 
                            
                         start_input_str_len = axutil_strlen(start_input_str);

                         sprintf(end_input_str, "</%s%sitem>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                         end_input_str_len = axutil_strlen(end_input_str);

                         count = axutil_array_list_size(_ArrayOf_xsd_string->property_item, env);
                         for(i = 0; i < count; i ++)
                         {
                            element = axutil_array_list_get(_ArrayOf_xsd_string->property_item, env, i);

                            if(NULL == element) 
                            {
                                continue;
                            }
                    
                     
                       
                                            
                        

                    
                    
                           text_value_1 = (axis2_char_t*)element;
                           
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
                           
                         }
                     }
                   
                     
                     AXIS2_FREE(env->allocator,start_input_str);
                     AXIS2_FREE(env->allocator,end_input_str);
                 } 

                 

            return parent;
        }


        

               
                               
               
            axutil_array_list_t* AXIS2_CALL
            adb_ArrayOf_xsd_string_get_item(
                    adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _ArrayOf_xsd_string, NULL);
                  

                return _ArrayOf_xsd_string->property_item;
             }

               
                              
               
            axis2_status_t AXIS2_CALL
            adb_ArrayOf_xsd_string_set_item(
                    adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                    const axutil_env_t *env,
                    axutil_array_list_t*  arg_item)
             {
                
                 int size = 0;
                 int i = 0;
                 axis2_bool_t non_nil_exists = AXIS2_FALSE;
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _ArrayOf_xsd_string, AXIS2_FAILURE);
                
                if(_ArrayOf_xsd_string->is_valid_item &&
                        arg_item == _ArrayOf_xsd_string->property_item)
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

                 adb_ArrayOf_xsd_string_reset_item(_ArrayOf_xsd_string, env);

                
                if(NULL == arg_item)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _ArrayOf_xsd_string->property_item = arg_item;
                        if(non_nil_exists)
                        {
                            _ArrayOf_xsd_string->is_valid_item = AXIS2_TRUE;
                        }
                        
                    
                return AXIS2_SUCCESS;
             }

            
               
                                       
               
            axis2_char_t* AXIS2_CALL
            adb_ArrayOf_xsd_string_get_item_at(
                    adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                    const axutil_env_t *env, int i)
            {
                axis2_char_t* ret_val;

                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _ArrayOf_xsd_string, NULL);
                  

                if(_ArrayOf_xsd_string->property_item == NULL)
                {
                    return (axis2_char_t*)0;
                }
                ret_val = (axis2_char_t*)axutil_array_list_get(_ArrayOf_xsd_string->property_item, env, i);
                
                    return ret_val;
                  
            }

               
                                           
               
            axis2_status_t AXIS2_CALL
            adb_ArrayOf_xsd_string_set_item_at(
                    adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                    const axutil_env_t *env, int i,
                    const axis2_char_t* arg_item)
            {
                void *element = NULL;
                int size = 0;
                int j;
                int k;
                axis2_bool_t non_nil_exists = AXIS2_FALSE;

                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _ArrayOf_xsd_string, AXIS2_FAILURE);
                
                if( _ArrayOf_xsd_string->is_valid_item &&
                    _ArrayOf_xsd_string->property_item &&
                
                    arg_item == (axis2_char_t*)axutil_array_list_get(_ArrayOf_xsd_string->property_item, env, i))
                  
                {
                    
                    return AXIS2_SUCCESS; 
                }

                
                    if(NULL == arg_item)
                    {
                        if(_ArrayOf_xsd_string->property_item != NULL)
                        {
                            size = axutil_array_list_size(_ArrayOf_xsd_string->property_item, env);
                            for(j = 0, k = 0; j < size; j ++ )
                            {
                                if(i == j) continue; 
                                if(NULL != axutil_array_list_get(_ArrayOf_xsd_string->property_item, env, i))
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

                if(_ArrayOf_xsd_string->property_item == NULL)
                {
                    _ArrayOf_xsd_string->property_item = axutil_array_list_create(env, 10);
                }
                
                                                                  
                element = axutil_array_list_get(_ArrayOf_xsd_string->property_item, env, i);
                if(NULL != element)
                {
                  
                  
                  
                                                                                             
                     
                }

                
                    if(!non_nil_exists)
                    {
                        
                        _ArrayOf_xsd_string->is_valid_item = AXIS2_FALSE;
                        axutil_array_list_set(_ArrayOf_xsd_string->property_item , env, i, NULL);
                        
                        return AXIS2_SUCCESS;
                    }
                
                   axutil_array_list_set(_ArrayOf_xsd_string->property_item , env, i, axutil_strdup(env, arg_item));
                  _ArrayOf_xsd_string->is_valid_item = AXIS2_TRUE;
                
                return AXIS2_SUCCESS;
            }

               
                           
               
            axis2_status_t AXIS2_CALL
            adb_ArrayOf_xsd_string_add_item(
                    adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                    const axutil_env_t *env,
                    const axis2_char_t* arg_item)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _ArrayOf_xsd_string, AXIS2_FAILURE);

                
                    if(NULL == arg_item)
                    {
                      
                           return AXIS2_SUCCESS; 
                        
                    }
                  

                if(_ArrayOf_xsd_string->property_item == NULL)
                {
                    _ArrayOf_xsd_string->property_item = axutil_array_list_create(env, 10);
                }
                if(_ArrayOf_xsd_string->property_item == NULL)
                {
                    AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Failed in allocatting memory for item");
                    return AXIS2_FAILURE;
                    
                }
                
                   axutil_array_list_add(_ArrayOf_xsd_string->property_item , env, axutil_strdup(env, arg_item));
                  _ArrayOf_xsd_string->is_valid_item = AXIS2_TRUE;
                return AXIS2_SUCCESS;
             }

               
                                              
               
            int AXIS2_CALL
            adb_ArrayOf_xsd_string_sizeof_item(
                    adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                    const axutil_env_t *env)
            {
                AXIS2_ENV_CHECK(env, -1);
                AXIS2_PARAM_CHECK(env->error, _ArrayOf_xsd_string, -1);
                if(_ArrayOf_xsd_string->property_item == NULL)
                {
                    return 0;
                }
                return axutil_array_list_size(_ArrayOf_xsd_string->property_item, env);
            }

               
                                                          
               
            axis2_status_t AXIS2_CALL
            adb_ArrayOf_xsd_string_remove_item_at(
                    adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                    const axutil_env_t *env, int i)
            {
                return adb_ArrayOf_xsd_string_set_item_nil_at(_ArrayOf_xsd_string, env, i);
            }

            

              
                               
              
           axis2_status_t AXIS2_CALL
           adb_ArrayOf_xsd_string_reset_item(
                   adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _ArrayOf_xsd_string, AXIS2_FAILURE);
               

               
                  if (_ArrayOf_xsd_string->property_item != NULL)
                  {
                      count = axutil_array_list_size(_ArrayOf_xsd_string->property_item, env);
                      for(i = 0; i < count; i ++)
                      {
                         element = axutil_array_list_get(_ArrayOf_xsd_string->property_item, env, i);
                
            
                
                if(element != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, (axis2_char_t*)element);
                     element = NULL;
                }
            
                
                
                
                      }
                      axutil_array_list_free(_ArrayOf_xsd_string->property_item, env);
                  }
                _ArrayOf_xsd_string->is_valid_item = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                        
              
           axis2_bool_t AXIS2_CALL
           adb_ArrayOf_xsd_string_is_item_nil(
                   adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _ArrayOf_xsd_string, AXIS2_TRUE);
               
               return !_ArrayOf_xsd_string->is_valid_item;
           }

              
                                                            
              
           axis2_status_t AXIS2_CALL
           adb_ArrayOf_xsd_string_set_item_nil(
                   adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                   const axutil_env_t *env)
           {
               return adb_ArrayOf_xsd_string_reset_item(_ArrayOf_xsd_string, env);
           }

           
              
                                             
              
           axis2_bool_t AXIS2_CALL
           adb_ArrayOf_xsd_string_is_item_nil_at(
                   adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                   const axutil_env_t *env, int i)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _ArrayOf_xsd_string, AXIS2_TRUE);
               
               return (_ArrayOf_xsd_string->is_valid_item == AXIS2_FALSE ||
                        NULL == _ArrayOf_xsd_string->property_item || 
                        NULL == axutil_array_list_get(_ArrayOf_xsd_string->property_item, env, i));
           }

              
                                   
              
           axis2_status_t AXIS2_CALL
           adb_ArrayOf_xsd_string_set_item_nil_at(
                   adb_ArrayOf_xsd_string_t* _ArrayOf_xsd_string,
                   const axutil_env_t *env, int i)
           {
                void *element = NULL;
                int size = 0;
                int j;
                axis2_bool_t non_nil_exists = AXIS2_FALSE;

                int k = 0;

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _ArrayOf_xsd_string, AXIS2_FAILURE);

                if(_ArrayOf_xsd_string->property_item == NULL ||
                            _ArrayOf_xsd_string->is_valid_item == AXIS2_FALSE)
                {
                    
                    non_nil_exists = AXIS2_FALSE;
                }
                else
                {
                    size = axutil_array_list_size(_ArrayOf_xsd_string->property_item, env);
                    for(j = 0, k = 0; j < size; j ++ )
                    {
                        if(i == j) continue; 
                        if(NULL != axutil_array_list_get(_ArrayOf_xsd_string->property_item, env, i))
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
 
                if(_ArrayOf_xsd_string->property_item == NULL)
                {
                    _ArrayOf_xsd_string->is_valid_item = AXIS2_FALSE;
                    
                    return AXIS2_SUCCESS;
                }

                                                                  
                element = axutil_array_list_get(_ArrayOf_xsd_string->property_item, env, i);
                if(NULL != element)
                {
                  
                  
                  
                                                                                             
                     
                }

                
                    if(!non_nil_exists)
                    {
                        
                        _ArrayOf_xsd_string->is_valid_item = AXIS2_FALSE;
                        axutil_array_list_set(_ArrayOf_xsd_string->property_item , env, i, NULL);
                        return AXIS2_SUCCESS;
                    }
                

                
                axutil_array_list_set(_ArrayOf_xsd_string->property_item , env, i, NULL);
                
                return AXIS2_SUCCESS;

           }

           

