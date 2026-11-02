

           
                                    
          
                                                 
                                                                                                
           

        #include "adb_WsWriteTargetParams.h"
        
                  
                                                                            
                                             
                                                                                                     
                                         
                   
           


        struct adb_WsWriteTargetParams
        {
            axis2_char_t* property_targetGuid;

                
                axis2_bool_t is_valid_targetGuid;


            adb_WsConnectionParams_t* property_connectionParams;

                
                axis2_bool_t is_valid_connectionParams;


            axis2_char_t* property_jtrFileName;

                
                axis2_bool_t is_valid_jtrFileName;


            axis2_char_t* property_dataFileName;

                
                axis2_bool_t is_valid_dataFileName;


            axis2_bool_t property_transaction;

                
                axis2_bool_t is_valid_transaction;


            int property_insertUpdateMode;

                
                axis2_bool_t is_valid_insertUpdateMode;


            adb_ArrayOfWsKeyValue_t* property_keyValues;

                
                axis2_bool_t is_valid_keyValues;


            
        };


                                                                                               
        

                axis2_status_t AXIS2_CALL
                adb_WsWriteTargetParams_set_transaction_nil(
                        adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                        const axutil_env_t *env);
            

                axis2_status_t AXIS2_CALL
                adb_WsWriteTargetParams_set_insertUpdateMode_nil(
                        adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                        const axutil_env_t *env);
            


                                                                                           
        adb_WsWriteTargetParams_t* AXIS2_CALL
        adb_WsWriteTargetParams_create(
            const axutil_env_t *env)
        {
            adb_WsWriteTargetParams_t *_WsWriteTargetParams = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _WsWriteTargetParams = (adb_WsWriteTargetParams_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_WsWriteTargetParams_t));

            if(NULL == _WsWriteTargetParams)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_WsWriteTargetParams, 0, sizeof(adb_WsWriteTargetParams_t));

            _WsWriteTargetParams->property_targetGuid  = NULL;
                  _WsWriteTargetParams->is_valid_targetGuid  = AXIS2_FALSE;
            _WsWriteTargetParams->property_connectionParams  = NULL;
                  _WsWriteTargetParams->is_valid_connectionParams  = AXIS2_FALSE;
            _WsWriteTargetParams->property_jtrFileName  = NULL;
                  _WsWriteTargetParams->is_valid_jtrFileName  = AXIS2_FALSE;
            _WsWriteTargetParams->property_dataFileName  = NULL;
                  _WsWriteTargetParams->is_valid_dataFileName  = AXIS2_FALSE;
            _WsWriteTargetParams->is_valid_transaction  = AXIS2_FALSE;
            _WsWriteTargetParams->is_valid_insertUpdateMode  = AXIS2_FALSE;
            _WsWriteTargetParams->property_keyValues  = NULL;
                  _WsWriteTargetParams->is_valid_keyValues  = AXIS2_FALSE;
            

            return _WsWriteTargetParams;
        }

        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_free (
                adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                const axutil_env_t *env)
        {
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_FAILURE);

            adb_WsWriteTargetParams_reset_targetGuid(_WsWriteTargetParams, env);
            adb_WsWriteTargetParams_reset_connectionParams(_WsWriteTargetParams, env);
            adb_WsWriteTargetParams_reset_jtrFileName(_WsWriteTargetParams, env);
            adb_WsWriteTargetParams_reset_dataFileName(_WsWriteTargetParams, env);
            adb_WsWriteTargetParams_reset_transaction(_WsWriteTargetParams, env);
            adb_WsWriteTargetParams_reset_insertUpdateMode(_WsWriteTargetParams, env);
            adb_WsWriteTargetParams_reset_keyValues(_WsWriteTargetParams, env);
            

            if(_WsWriteTargetParams)
            {
                AXIS2_FREE(env->allocator, _WsWriteTargetParams);
                _WsWriteTargetParams = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_WsWriteTargetParams_deserialize(
                adb_WsWriteTargetParams_t* _WsWriteTargetParams,
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
            AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for WsWriteTargetParams : "
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
                                            status = adb_WsWriteTargetParams_set_targetGuid(_WsWriteTargetParams, env,
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
                                          status = adb_WsWriteTargetParams_set_connectionParams(_WsWriteTargetParams, env,
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
                                 
                                 element_qname = axutil_qname_create(env, "jtrFileName", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsWriteTargetParams_set_jtrFileName(_WsWriteTargetParams, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for jtrFileName ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element jtrFileName missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "dataFileName", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsWriteTargetParams_set_dataFileName(_WsWriteTargetParams, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for dataFileName ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element dataFileName missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "transaction", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                                status = adb_WsWriteTargetParams_set_transaction(_WsWriteTargetParams, env,
                                                                 AXIS2_TRUE);
                                            }
                                            else
                                            {
                                                status = adb_WsWriteTargetParams_set_transaction(_WsWriteTargetParams, env,
                                                                      AXIS2_FALSE);
                                            }
                                      }
                                      
                                      else
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "NULL value is set to a non nillable element transaction");
                                          status = AXIS2_FAILURE;
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for transaction ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element transaction missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "insertUpdateMode", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsWriteTargetParams_set_insertUpdateMode(_WsWriteTargetParams, env,
                                                                   atoi(text_value));
                                      }
                                      
                                      else
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "NULL value is set to a non nillable element insertUpdateMode");
                                          status = AXIS2_FAILURE;
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for insertUpdateMode ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element insertUpdateMode missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "keyValues", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

                           if (adb_ArrayOfWsKeyValue_is_particle() ||  
                                (current_node   && current_element && (axutil_qname_equals(element_qname, env, qname))))
                           {
                              if( current_node   && current_element && (axutil_qname_equals(element_qname, env, qname)))
                              {
                                is_early_node_valid = AXIS2_TRUE;
                              }
                              
                                 
                                      element = (void*)adb_ArrayOfWsKeyValue_create(env);

                                      status =  adb_ArrayOfWsKeyValue_deserialize((adb_ArrayOfWsKeyValue_t*)element,
                                                                            env, &current_node, &is_early_node_valid, AXIS2_FALSE);
                                      if(AXIS2_FAILURE == status)
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in building adb object for element keyValues");
                                      }
                                      else
                                      {
                                          status = adb_WsWriteTargetParams_set_keyValues(_WsWriteTargetParams, env,
                                                                   (adb_ArrayOfWsKeyValue_t*)element);
                                      }
                                    
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for keyValues ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element keyValues missing");
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
          adb_WsWriteTargetParams_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_WsWriteTargetParams_declare_parent_namespaces(
                    adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_WsWriteTargetParams_serialize(
                adb_WsWriteTargetParams_t* _WsWriteTargetParams,
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
                    
                    axis2_char_t text_value_2[64];
                    
                    axis2_char_t *text_value_3;
                    axis2_char_t *text_value_3_temp;
                    
                    axis2_char_t *text_value_4;
                    axis2_char_t *text_value_4_temp;
                    
                    axis2_char_t text_value_5[64];
                    
                    axis2_char_t text_value_6[64];
                    
                    axis2_char_t text_value_7[64];
                    
               axis2_char_t *start_input_str = NULL;
               axis2_char_t *end_input_str = NULL;
               unsigned int start_input_str_len = 0;
               unsigned int end_input_str_len = 0;
            
            
               axiom_data_source_t *data_source = NULL;
               axutil_stream_t *stream = NULL;

            

            AXIS2_ENV_CHECK(env, NULL);
            AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, NULL);
            
            
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
                      

                   if (!_WsWriteTargetParams->is_valid_targetGuid)
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
                    
                           text_value_1 = _WsWriteTargetParams->property_targetGuid;
                           
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
                      

                   if (!_WsWriteTargetParams->is_valid_connectionParams)
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
                            
                            adb_WsConnectionParams_serialize(_WsWriteTargetParams->property_connectionParams, 
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
                      

                   if (!_WsWriteTargetParams->is_valid_jtrFileName)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("jtrFileName") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%sjtrFileName xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("jtrFileName"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("jtrFileName")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                   
                        

                    
                    
                            sprintf(start_input_str, "<%s%sjtrFileName>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sjtrFileName>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_3 = _WsWriteTargetParams->property_jtrFileName;
                           
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

                 
                       if(!(p_prefix = (axis2_char_t*)axutil_hash_get(namespaces, "http://jdbc.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING)))
                       {
                           p_prefix = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof (axis2_char_t) * ADB_DEFAULT_NAMESPACE_PREFIX_LIMIT);
                           sprintf(p_prefix, "n%d", (*next_ns_index)++);
                           axutil_hash_set(namespaces, "http://jdbc.webservice.backend.server.integration.etlforge.invalid", AXIS2_HASH_KEY_STRING, p_prefix);
                           
                           axiom_element_declare_namespace_assume_param_ownership(parent_element, env, axiom_namespace_create (env,
                                            "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
                                            p_prefix));
                       }
                      

                   if (!_WsWriteTargetParams->is_valid_dataFileName)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("dataFileName") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%sdataFileName xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("dataFileName"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("dataFileName")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                    
                        

                    
                    
                            sprintf(start_input_str, "<%s%sdataFileName>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sdataFileName>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_4 = _WsWriteTargetParams->property_dataFileName;
                           
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
                      

                   if (!_WsWriteTargetParams->is_valid_transaction)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property transaction");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("transaction"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("transaction")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                   
                        

                    
                    
                            sprintf(start_input_str, "<%s%stransaction>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%stransaction>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           strcpy(text_value_5, (_WsWriteTargetParams->property_transaction)?"true":"false");
                           
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                           axutil_stream_write(stream, env, text_value_5, axutil_strlen(text_value_5));
                           
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
                      

                   if (!_WsWriteTargetParams->is_valid_insertUpdateMode)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property insertUpdateMode");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("insertUpdateMode"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("insertUpdateMode")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                        
                        

                    
                    
                            sprintf(start_input_str, "<%s%sinsertUpdateMode>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sinsertUpdateMode>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                               sprintf (text_value_6, AXIS2_PRINTF_INT32_FORMAT_SPECIFIER, _WsWriteTargetParams->property_insertUpdateMode);
                             
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                           axutil_stream_write(stream, env, text_value_6, axutil_strlen(text_value_6));
                           
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
                      

                   if (!_WsWriteTargetParams->is_valid_keyValues)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("keyValues") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%skeyValues xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("keyValues"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("keyValues")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                 
                        

                    
                    
                            sprintf(start_input_str, "<%s%skeyValues",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":""); 
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%skeyValues>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                     
                            if(!adb_ArrayOfWsKeyValue_is_particle())
                            {
                                axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                            }
                            
                            adb_ArrayOfWsKeyValue_serialize(_WsWriteTargetParams->property_keyValues, 
                                                                                 env, current_node, parent_element,
                                                                                 adb_ArrayOfWsKeyValue_is_particle() || AXIS2_FALSE, namespaces, next_ns_index);
                            
                            if(!adb_ArrayOfWsKeyValue_is_particle())
                            {
                                axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                            }
                            
                     
                     AXIS2_FREE(env->allocator,start_input_str);
                     AXIS2_FREE(env->allocator,end_input_str);
                 } 

                 

            return parent;
        }


        

               
                                     
               
            axis2_char_t* AXIS2_CALL
            adb_WsWriteTargetParams_get_targetGuid(
                    adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, NULL);
                  

                return _WsWriteTargetParams->property_targetGuid;
             }

               
                                    
               
            axis2_status_t AXIS2_CALL
            adb_WsWriteTargetParams_set_targetGuid(
                    adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_targetGuid)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_FAILURE);
                
                if(_WsWriteTargetParams->is_valid_targetGuid &&
                        arg_targetGuid == _WsWriteTargetParams->property_targetGuid)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsWriteTargetParams_reset_targetGuid(_WsWriteTargetParams, env);

                
                if(NULL == arg_targetGuid)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsWriteTargetParams->property_targetGuid = (axis2_char_t *)axutil_strdup(env, arg_targetGuid);
                        if(NULL == _WsWriteTargetParams->property_targetGuid)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for targetGuid");
                            return AXIS2_FAILURE;
                        }
                        _WsWriteTargetParams->is_valid_targetGuid = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                     
              
           axis2_status_t AXIS2_CALL
           adb_WsWriteTargetParams_reset_targetGuid(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsWriteTargetParams->property_targetGuid != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsWriteTargetParams->property_targetGuid);
                     _WsWriteTargetParams->property_targetGuid = NULL;
                }
            
                
                
                _WsWriteTargetParams->is_valid_targetGuid = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                              
              
           axis2_bool_t AXIS2_CALL
           adb_WsWriteTargetParams_is_targetGuid_nil(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_TRUE);
               
               return !_WsWriteTargetParams->is_valid_targetGuid;
           }

              
                                                                  
              
           axis2_status_t AXIS2_CALL
           adb_WsWriteTargetParams_set_targetGuid_nil(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               return adb_WsWriteTargetParams_reset_targetGuid(_WsWriteTargetParams, env);
           }

           

               
                                           
               
            adb_WsConnectionParams_t* AXIS2_CALL
            adb_WsWriteTargetParams_get_connectionParams(
                    adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, NULL);
                  

                return _WsWriteTargetParams->property_connectionParams;
             }

               
                                          
               
            axis2_status_t AXIS2_CALL
            adb_WsWriteTargetParams_set_connectionParams(
                    adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                    const axutil_env_t *env,
                    adb_WsConnectionParams_t*  arg_connectionParams)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_FAILURE);
                
                if(_WsWriteTargetParams->is_valid_connectionParams &&
                        arg_connectionParams == _WsWriteTargetParams->property_connectionParams)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsWriteTargetParams_reset_connectionParams(_WsWriteTargetParams, env);

                
                if(NULL == arg_connectionParams)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsWriteTargetParams->property_connectionParams = arg_connectionParams;
                        _WsWriteTargetParams->is_valid_connectionParams = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                           
              
           axis2_status_t AXIS2_CALL
           adb_WsWriteTargetParams_reset_connectionParams(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsWriteTargetParams->property_connectionParams != NULL)
                {
                   
                   
                        adb_WsConnectionParams_free(_WsWriteTargetParams->property_connectionParams, env);
                     _WsWriteTargetParams->property_connectionParams = NULL;
                }
            
                
                
                _WsWriteTargetParams->is_valid_connectionParams = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                    
              
           axis2_bool_t AXIS2_CALL
           adb_WsWriteTargetParams_is_connectionParams_nil(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_TRUE);
               
               return !_WsWriteTargetParams->is_valid_connectionParams;
           }

              
                                                                        
              
           axis2_status_t AXIS2_CALL
           adb_WsWriteTargetParams_set_connectionParams_nil(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               return adb_WsWriteTargetParams_reset_connectionParams(_WsWriteTargetParams, env);
           }

           

               
                                      
               
            axis2_char_t* AXIS2_CALL
            adb_WsWriteTargetParams_get_jtrFileName(
                    adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, NULL);
                  

                return _WsWriteTargetParams->property_jtrFileName;
             }

               
                                     
               
            axis2_status_t AXIS2_CALL
            adb_WsWriteTargetParams_set_jtrFileName(
                    adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_jtrFileName)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_FAILURE);
                
                if(_WsWriteTargetParams->is_valid_jtrFileName &&
                        arg_jtrFileName == _WsWriteTargetParams->property_jtrFileName)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsWriteTargetParams_reset_jtrFileName(_WsWriteTargetParams, env);

                
                if(NULL == arg_jtrFileName)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsWriteTargetParams->property_jtrFileName = (axis2_char_t *)axutil_strdup(env, arg_jtrFileName);
                        if(NULL == _WsWriteTargetParams->property_jtrFileName)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for jtrFileName");
                            return AXIS2_FAILURE;
                        }
                        _WsWriteTargetParams->is_valid_jtrFileName = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                      
              
           axis2_status_t AXIS2_CALL
           adb_WsWriteTargetParams_reset_jtrFileName(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsWriteTargetParams->property_jtrFileName != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsWriteTargetParams->property_jtrFileName);
                     _WsWriteTargetParams->property_jtrFileName = NULL;
                }
            
                
                
                _WsWriteTargetParams->is_valid_jtrFileName = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                               
              
           axis2_bool_t AXIS2_CALL
           adb_WsWriteTargetParams_is_jtrFileName_nil(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_TRUE);
               
               return !_WsWriteTargetParams->is_valid_jtrFileName;
           }

              
                                                                   
              
           axis2_status_t AXIS2_CALL
           adb_WsWriteTargetParams_set_jtrFileName_nil(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               return adb_WsWriteTargetParams_reset_jtrFileName(_WsWriteTargetParams, env);
           }

           

               
                                       
               
            axis2_char_t* AXIS2_CALL
            adb_WsWriteTargetParams_get_dataFileName(
                    adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, NULL);
                  

                return _WsWriteTargetParams->property_dataFileName;
             }

               
                                      
               
            axis2_status_t AXIS2_CALL
            adb_WsWriteTargetParams_set_dataFileName(
                    adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_dataFileName)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_FAILURE);
                
                if(_WsWriteTargetParams->is_valid_dataFileName &&
                        arg_dataFileName == _WsWriteTargetParams->property_dataFileName)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsWriteTargetParams_reset_dataFileName(_WsWriteTargetParams, env);

                
                if(NULL == arg_dataFileName)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsWriteTargetParams->property_dataFileName = (axis2_char_t *)axutil_strdup(env, arg_dataFileName);
                        if(NULL == _WsWriteTargetParams->property_dataFileName)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for dataFileName");
                            return AXIS2_FAILURE;
                        }
                        _WsWriteTargetParams->is_valid_dataFileName = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                       
              
           axis2_status_t AXIS2_CALL
           adb_WsWriteTargetParams_reset_dataFileName(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsWriteTargetParams->property_dataFileName != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsWriteTargetParams->property_dataFileName);
                     _WsWriteTargetParams->property_dataFileName = NULL;
                }
            
                
                
                _WsWriteTargetParams->is_valid_dataFileName = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                
              
           axis2_bool_t AXIS2_CALL
           adb_WsWriteTargetParams_is_dataFileName_nil(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_TRUE);
               
               return !_WsWriteTargetParams->is_valid_dataFileName;
           }

              
                                                                    
              
           axis2_status_t AXIS2_CALL
           adb_WsWriteTargetParams_set_dataFileName_nil(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               return adb_WsWriteTargetParams_reset_dataFileName(_WsWriteTargetParams, env);
           }

           

               
                                      
               
            axis2_bool_t AXIS2_CALL
            adb_WsWriteTargetParams_get_transaction(
                    adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, (axis2_bool_t)0);
                    AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, (axis2_bool_t)0);
                  

                return _WsWriteTargetParams->property_transaction;
             }

               
                                     
               
            axis2_status_t AXIS2_CALL
            adb_WsWriteTargetParams_set_transaction(
                    adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                    const axutil_env_t *env,
                    axis2_bool_t  arg_transaction)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_FAILURE);
                
                if(_WsWriteTargetParams->is_valid_transaction &&
                        arg_transaction == _WsWriteTargetParams->property_transaction)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsWriteTargetParams_reset_transaction(_WsWriteTargetParams, env);

                _WsWriteTargetParams->property_transaction = arg_transaction;
                        _WsWriteTargetParams->is_valid_transaction = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                      
              
           axis2_status_t AXIS2_CALL
           adb_WsWriteTargetParams_reset_transaction(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_FAILURE);
               

               _WsWriteTargetParams->is_valid_transaction = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                               
              
           axis2_bool_t AXIS2_CALL
           adb_WsWriteTargetParams_is_transaction_nil(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_TRUE);
               
               return !_WsWriteTargetParams->is_valid_transaction;
           }

              
                                                                   
              
           axis2_status_t AXIS2_CALL
           adb_WsWriteTargetParams_set_transaction_nil(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               return adb_WsWriteTargetParams_reset_transaction(_WsWriteTargetParams, env);
           }

           

               
                                           
               
            int AXIS2_CALL
            adb_WsWriteTargetParams_get_insertUpdateMode(
                    adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, (int)0);
                    AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, (int)0);
                  

                return _WsWriteTargetParams->property_insertUpdateMode;
             }

               
                                          
               
            axis2_status_t AXIS2_CALL
            adb_WsWriteTargetParams_set_insertUpdateMode(
                    adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                    const axutil_env_t *env,
                    const int  arg_insertUpdateMode)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_FAILURE);
                
                if(_WsWriteTargetParams->is_valid_insertUpdateMode &&
                        arg_insertUpdateMode == _WsWriteTargetParams->property_insertUpdateMode)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsWriteTargetParams_reset_insertUpdateMode(_WsWriteTargetParams, env);

                _WsWriteTargetParams->property_insertUpdateMode = arg_insertUpdateMode;
                        _WsWriteTargetParams->is_valid_insertUpdateMode = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                           
              
           axis2_status_t AXIS2_CALL
           adb_WsWriteTargetParams_reset_insertUpdateMode(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_FAILURE);
               

               _WsWriteTargetParams->is_valid_insertUpdateMode = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                    
              
           axis2_bool_t AXIS2_CALL
           adb_WsWriteTargetParams_is_insertUpdateMode_nil(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_TRUE);
               
               return !_WsWriteTargetParams->is_valid_insertUpdateMode;
           }

              
                                                                        
              
           axis2_status_t AXIS2_CALL
           adb_WsWriteTargetParams_set_insertUpdateMode_nil(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               return adb_WsWriteTargetParams_reset_insertUpdateMode(_WsWriteTargetParams, env);
           }

           

               
                                    
               
            adb_ArrayOfWsKeyValue_t* AXIS2_CALL
            adb_WsWriteTargetParams_get_keyValues(
                    adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, NULL);
                  

                return _WsWriteTargetParams->property_keyValues;
             }

               
                                   
               
            axis2_status_t AXIS2_CALL
            adb_WsWriteTargetParams_set_keyValues(
                    adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                    const axutil_env_t *env,
                    adb_ArrayOfWsKeyValue_t*  arg_keyValues)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_FAILURE);
                
                if(_WsWriteTargetParams->is_valid_keyValues &&
                        arg_keyValues == _WsWriteTargetParams->property_keyValues)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsWriteTargetParams_reset_keyValues(_WsWriteTargetParams, env);

                
                if(NULL == arg_keyValues)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsWriteTargetParams->property_keyValues = arg_keyValues;
                        _WsWriteTargetParams->is_valid_keyValues = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                    
              
           axis2_status_t AXIS2_CALL
           adb_WsWriteTargetParams_reset_keyValues(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsWriteTargetParams->property_keyValues != NULL)
                {
                   
                   
                        adb_ArrayOfWsKeyValue_free(_WsWriteTargetParams->property_keyValues, env);
                     _WsWriteTargetParams->property_keyValues = NULL;
                }
            
                
                
                _WsWriteTargetParams->is_valid_keyValues = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                             
              
           axis2_bool_t AXIS2_CALL
           adb_WsWriteTargetParams_is_keyValues_nil(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsWriteTargetParams, AXIS2_TRUE);
               
               return !_WsWriteTargetParams->is_valid_keyValues;
           }

              
                                                                 
              
           axis2_status_t AXIS2_CALL
           adb_WsWriteTargetParams_set_keyValues_nil(
                   adb_WsWriteTargetParams_t* _WsWriteTargetParams,
                   const axutil_env_t *env)
           {
               return adb_WsWriteTargetParams_reset_keyValues(_WsWriteTargetParams, env);
           }

           

