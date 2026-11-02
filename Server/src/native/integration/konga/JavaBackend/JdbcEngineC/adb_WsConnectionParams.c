

           
                                   
          
                                                 
                                                                                                
           

        #include "adb_WsConnectionParams.h"
        
                  
                                                                            
                                            
                                                                                                     
                                         
                   
           


        struct adb_WsConnectionParams
        {
            axis2_char_t* property_server;

                
                axis2_bool_t is_valid_server;


            int property_port;

                
                axis2_bool_t is_valid_port;


            axis2_char_t* property_database;

                
                axis2_bool_t is_valid_database;


            axis2_char_t* property_user;

                
                axis2_bool_t is_valid_user;


            axis2_char_t* property_password;

                
                axis2_bool_t is_valid_password;


            axis2_char_t* property_manualConnectionString;

                
                axis2_bool_t is_valid_manualConnectionString;


            axis2_char_t* property_additionalParams;

                
                axis2_bool_t is_valid_additionalParams;


            axis2_char_t* property_driverName;

                
                axis2_bool_t is_valid_driverName;


            int property_timeout;

                
                axis2_bool_t is_valid_timeout;


            int property_transactionIsolationLevel;

                
                axis2_bool_t is_valid_transactionIsolationLevel;


            
        };


                                                                                               
        

                axis2_status_t AXIS2_CALL
                adb_WsConnectionParams_set_port_nil(
                        adb_WsConnectionParams_t* _WsConnectionParams,
                        const axutil_env_t *env);
            

                axis2_status_t AXIS2_CALL
                adb_WsConnectionParams_set_timeout_nil(
                        adb_WsConnectionParams_t* _WsConnectionParams,
                        const axutil_env_t *env);
            

                axis2_status_t AXIS2_CALL
                adb_WsConnectionParams_set_transactionIsolationLevel_nil(
                        adb_WsConnectionParams_t* _WsConnectionParams,
                        const axutil_env_t *env);
            


                                                                                           
        adb_WsConnectionParams_t* AXIS2_CALL
        adb_WsConnectionParams_create(
            const axutil_env_t *env)
        {
            adb_WsConnectionParams_t *_WsConnectionParams = NULL;
            
            AXIS2_ENV_CHECK(env, NULL);

            _WsConnectionParams = (adb_WsConnectionParams_t *) AXIS2_MALLOC(env->
                allocator, sizeof(adb_WsConnectionParams_t));

            if(NULL == _WsConnectionParams)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_NO_MEMORY, AXIS2_FAILURE);
                return NULL;
            }

            memset(_WsConnectionParams, 0, sizeof(adb_WsConnectionParams_t));

            _WsConnectionParams->property_server  = NULL;
                  _WsConnectionParams->is_valid_server  = AXIS2_FALSE;
            _WsConnectionParams->is_valid_port  = AXIS2_FALSE;
            _WsConnectionParams->property_database  = NULL;
                  _WsConnectionParams->is_valid_database  = AXIS2_FALSE;
            _WsConnectionParams->property_user  = NULL;
                  _WsConnectionParams->is_valid_user  = AXIS2_FALSE;
            _WsConnectionParams->property_password  = NULL;
                  _WsConnectionParams->is_valid_password  = AXIS2_FALSE;
            _WsConnectionParams->property_manualConnectionString  = NULL;
                  _WsConnectionParams->is_valid_manualConnectionString  = AXIS2_FALSE;
            _WsConnectionParams->property_additionalParams  = NULL;
                  _WsConnectionParams->is_valid_additionalParams  = AXIS2_FALSE;
            _WsConnectionParams->property_driverName  = NULL;
                  _WsConnectionParams->is_valid_driverName  = AXIS2_FALSE;
            _WsConnectionParams->is_valid_timeout  = AXIS2_FALSE;
            _WsConnectionParams->is_valid_transactionIsolationLevel  = AXIS2_FALSE;
            

            return _WsConnectionParams;
        }

        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_free (
                adb_WsConnectionParams_t* _WsConnectionParams,
                const axutil_env_t *env)
        {
            

            AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
            AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);

            adb_WsConnectionParams_reset_server(_WsConnectionParams, env);
            adb_WsConnectionParams_reset_port(_WsConnectionParams, env);
            adb_WsConnectionParams_reset_database(_WsConnectionParams, env);
            adb_WsConnectionParams_reset_user(_WsConnectionParams, env);
            adb_WsConnectionParams_reset_password(_WsConnectionParams, env);
            adb_WsConnectionParams_reset_manualConnectionString(_WsConnectionParams, env);
            adb_WsConnectionParams_reset_additionalParams(_WsConnectionParams, env);
            adb_WsConnectionParams_reset_driverName(_WsConnectionParams, env);
            adb_WsConnectionParams_reset_timeout(_WsConnectionParams, env);
            adb_WsConnectionParams_reset_transactionIsolationLevel(_WsConnectionParams, env);
            

            if(_WsConnectionParams)
            {
                AXIS2_FREE(env->allocator, _WsConnectionParams);
                _WsConnectionParams = NULL;
            }
            return AXIS2_SUCCESS;
        }


        

        axis2_status_t AXIS2_CALL
        adb_WsConnectionParams_deserialize(
                adb_WsConnectionParams_t* _WsConnectionParams,
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
            AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);

            
              
              while(parent && axiom_node_get_node_type(parent, env) != AXIOM_ELEMENT)
              {
                  parent = axiom_node_get_next_sibling(parent, env);
              }
              if (NULL == parent)
              {
                                                              
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, 
                            "Failed in building adb object for WsConnectionParams : "
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
                                   
                                 element_qname = axutil_qname_create(env, "server", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsConnectionParams_set_server(_WsConnectionParams, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for server ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element server missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "port", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsConnectionParams_set_port(_WsConnectionParams, env,
                                                                   atoi(text_value));
                                      }
                                      
                                      else
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "NULL value is set to a non nillable element port");
                                          status = AXIS2_FAILURE;
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for port ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element port missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "database", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsConnectionParams_set_database(_WsConnectionParams, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for database ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element database missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "user", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsConnectionParams_set_user(_WsConnectionParams, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for user ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element user missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "password", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsConnectionParams_set_password(_WsConnectionParams, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for password ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element password missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "manualConnectionString", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsConnectionParams_set_manualConnectionString(_WsConnectionParams, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for manualConnectionString ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element manualConnectionString missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "additionalParams", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsConnectionParams_set_additionalParams(_WsConnectionParams, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for additionalParams ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element additionalParams missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "driverName", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsConnectionParams_set_driverName(_WsConnectionParams, env,
                                                               text_value);
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for driverName ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element driverName missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "timeout", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsConnectionParams_set_timeout(_WsConnectionParams, env,
                                                                   atoi(text_value));
                                      }
                                      
                                      else
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "NULL value is set to a non nillable element timeout");
                                          status = AXIS2_FAILURE;
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for timeout ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element timeout missing");
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
                                 
                                 element_qname = axutil_qname_create(env, "transactionIsolationLevel", "http://jdbc.webservice.backend.server.integration.etlforge.invalid", NULL);
                                 

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
                                            status = adb_WsConnectionParams_set_transactionIsolationLevel(_WsConnectionParams, env,
                                                                   atoi(text_value));
                                      }
                                      
                                      else
                                      {
                                          AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "NULL value is set to a non nillable element transactionIsolationLevel");
                                          status = AXIS2_FAILURE;
                                      }
                                      
                                 if(AXIS2_FAILURE ==  status)
                                 {
                                     AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "failed in setting the value for transactionIsolationLevel ");
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
                                                                     
                                  AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "non nillable or minOuccrs != 0 element transactionIsolationLevel missing");
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
          adb_WsConnectionParams_is_particle()
          {
            
                 return AXIS2_FALSE;
              
          }


          void AXIS2_CALL
          adb_WsConnectionParams_declare_parent_namespaces(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env, axiom_element_t *parent_element,
                    axutil_hash_t *namespaces, int *next_ns_index)
          {
            
                                                                          
                 
          }

        
        
        axiom_node_t* AXIS2_CALL
        adb_WsConnectionParams_serialize(
                adb_WsConnectionParams_t* _WsConnectionParams,
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
                    
                    axis2_char_t *text_value_5;
                    axis2_char_t *text_value_5_temp;
                    
                    axis2_char_t *text_value_6;
                    axis2_char_t *text_value_6_temp;
                    
                    axis2_char_t *text_value_7;
                    axis2_char_t *text_value_7_temp;
                    
                    axis2_char_t *text_value_8;
                    axis2_char_t *text_value_8_temp;
                    
                    axis2_char_t text_value_9[64];
                    
                    axis2_char_t text_value_10[64];
                    
               axis2_char_t *start_input_str = NULL;
               axis2_char_t *end_input_str = NULL;
               unsigned int start_input_str_len = 0;
               unsigned int end_input_str_len = 0;
            
            
               axiom_data_source_t *data_source = NULL;
               axutil_stream_t *stream = NULL;

            

            AXIS2_ENV_CHECK(env, NULL);
            AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, NULL);
            
            
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
                      

                   if (!_WsConnectionParams->is_valid_server)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("server") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%sserver xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("server"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("server")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                              
                        

                    
                    
                            sprintf(start_input_str, "<%s%sserver>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sserver>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_1 = _WsConnectionParams->property_server;
                           
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
                      

                   if (!_WsConnectionParams->is_valid_port)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property port");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("port"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("port")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                            
                        

                    
                    
                            sprintf(start_input_str, "<%s%sport>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sport>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                               sprintf (text_value_2, AXIS2_PRINTF_INT32_FORMAT_SPECIFIER, _WsConnectionParams->property_port);
                             
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                           axutil_stream_write(stream, env, text_value_2, axutil_strlen(text_value_2));
                           
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
                      

                   if (!_WsConnectionParams->is_valid_database)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("database") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%sdatabase xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("database"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("database")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                
                        

                    
                    
                            sprintf(start_input_str, "<%s%sdatabase>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sdatabase>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_3 = _WsConnectionParams->property_database;
                           
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
                      

                   if (!_WsConnectionParams->is_valid_user)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("user") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%suser xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("user"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("user")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                            
                        

                    
                    
                            sprintf(start_input_str, "<%s%suser>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%suser>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_4 = _WsConnectionParams->property_user;
                           
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
                      

                   if (!_WsConnectionParams->is_valid_password)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("password") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%spassword xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("password"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("password")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                
                        

                    
                    
                            sprintf(start_input_str, "<%s%spassword>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%spassword>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_5 = _WsConnectionParams->property_password;
                           
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                            
                           text_value_5_temp = axutil_xml_quote_string(env, text_value_5, AXIS2_TRUE);
                           if (text_value_5_temp)
                           {
                               axutil_stream_write(stream, env, text_value_5_temp, axutil_strlen(text_value_5_temp));
                               AXIS2_FREE(env->allocator, text_value_5_temp);
                           }
                           else
                           {
                               axutil_stream_write(stream, env, text_value_5, axutil_strlen(text_value_5));
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
                      

                   if (!_WsConnectionParams->is_valid_manualConnectionString)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("manualConnectionString") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%smanualConnectionString xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("manualConnectionString"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("manualConnectionString")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                              
                        

                    
                    
                            sprintf(start_input_str, "<%s%smanualConnectionString>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%smanualConnectionString>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_6 = _WsConnectionParams->property_manualConnectionString;
                           
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                            
                           text_value_6_temp = axutil_xml_quote_string(env, text_value_6, AXIS2_TRUE);
                           if (text_value_6_temp)
                           {
                               axutil_stream_write(stream, env, text_value_6_temp, axutil_strlen(text_value_6_temp));
                               AXIS2_FREE(env->allocator, text_value_6_temp);
                           }
                           else
                           {
                               axutil_stream_write(stream, env, text_value_6, axutil_strlen(text_value_6));
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
                      

                   if (!_WsConnectionParams->is_valid_additionalParams)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("additionalParams") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%sadditionalParams xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("additionalParams"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("additionalParams")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                        
                        

                    
                    
                            sprintf(start_input_str, "<%s%sadditionalParams>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sadditionalParams>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_7 = _WsConnectionParams->property_additionalParams;
                           
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                            
                           text_value_7_temp = axutil_xml_quote_string(env, text_value_7, AXIS2_TRUE);
                           if (text_value_7_temp)
                           {
                               axutil_stream_write(stream, env, text_value_7_temp, axutil_strlen(text_value_7_temp));
                               AXIS2_FREE(env->allocator, text_value_7_temp);
                           }
                           else
                           {
                               axutil_stream_write(stream, env, text_value_7, axutil_strlen(text_value_7));
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
                      

                   if (!_WsConnectionParams->is_valid_driverName)
                   {
                      
                            
                            start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                        (5 + axutil_strlen(p_prefix) + 
                                         axutil_strlen("driverName") + 
                                         axutil_strlen(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\""))); 
                                        
                            
                            sprintf(start_input_str, "<%s%sdriverName xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>",
                                        p_prefix?p_prefix:"",
                                        (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                                        
                            axutil_stream_write(stream, env, start_input_str, axutil_strlen(start_input_str));
                            AXIS2_FREE(env->allocator,start_input_str);
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("driverName"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("driverName")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                  
                        

                    
                    
                            sprintf(start_input_str, "<%s%sdriverName>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%sdriverName>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                           text_value_8 = _WsConnectionParams->property_driverName;
                           
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                            
                           text_value_8_temp = axutil_xml_quote_string(env, text_value_8, AXIS2_TRUE);
                           if (text_value_8_temp)
                           {
                               axutil_stream_write(stream, env, text_value_8_temp, axutil_strlen(text_value_8_temp));
                               AXIS2_FREE(env->allocator, text_value_8_temp);
                           }
                           else
                           {
                               axutil_stream_write(stream, env, text_value_8, axutil_strlen(text_value_8));
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
                      

                   if (!_WsConnectionParams->is_valid_timeout)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property timeout");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("timeout"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("timeout")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                               
                        

                    
                    
                            sprintf(start_input_str, "<%s%stimeout>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%stimeout>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                               sprintf (text_value_9, AXIS2_PRINTF_INT32_FORMAT_SPECIFIER, _WsConnectionParams->property_timeout);
                             
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                           axutil_stream_write(stream, env, text_value_9, axutil_strlen(text_value_9));
                           
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
                      

                   if (!_WsConnectionParams->is_valid_transactionIsolationLevel)
                   {
                      
                            
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Nil value found in non-nillable property transactionIsolationLevel");
                            return NULL;
                          
                   }
                   else
                   {
                     start_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (4 + axutil_strlen(p_prefix) + 
                                  axutil_strlen("transactionIsolationLevel"))); 
                                 
                                                                   
                     end_input_str = (axis2_char_t*)AXIS2_MALLOC(env->allocator, sizeof(axis2_char_t) *
                                 (5 + axutil_strlen(p_prefix) + axutil_strlen("transactionIsolationLevel")));
                                                                     
                                  
                     

                   
                   
                     
                       
                                                                 
                        

                    
                    
                            sprintf(start_input_str, "<%s%stransactionIsolationLevel>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                            
                        start_input_str_len = axutil_strlen(start_input_str);
                        sprintf(end_input_str, "</%s%stransactionIsolationLevel>",
                                 p_prefix?p_prefix:"",
                                 (p_prefix && axutil_strcmp(p_prefix, ""))?":":"");
                        end_input_str_len = axutil_strlen(end_input_str);
                    
                               sprintf (text_value_10, AXIS2_PRINTF_INT32_FORMAT_SPECIFIER, _WsConnectionParams->property_transactionIsolationLevel);
                             
                           axutil_stream_write(stream, env, start_input_str, start_input_str_len);
                           
                           axutil_stream_write(stream, env, text_value_10, axutil_strlen(text_value_10));
                           
                           axutil_stream_write(stream, env, end_input_str, end_input_str_len);
                           
                     
                     AXIS2_FREE(env->allocator,start_input_str);
                     AXIS2_FREE(env->allocator,end_input_str);
                 } 

                 

            return parent;
        }


        

               
                                 
               
            axis2_char_t* AXIS2_CALL
            adb_WsConnectionParams_get_server(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, NULL);
                  

                return _WsConnectionParams->property_server;
             }

               
                                
               
            axis2_status_t AXIS2_CALL
            adb_WsConnectionParams_set_server(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_server)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
                
                if(_WsConnectionParams->is_valid_server &&
                        arg_server == _WsConnectionParams->property_server)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsConnectionParams_reset_server(_WsConnectionParams, env);

                
                if(NULL == arg_server)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsConnectionParams->property_server = (axis2_char_t *)axutil_strdup(env, arg_server);
                        if(NULL == _WsConnectionParams->property_server)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for server");
                            return AXIS2_FAILURE;
                        }
                        _WsConnectionParams->is_valid_server = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                 
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_reset_server(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsConnectionParams->property_server != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsConnectionParams->property_server);
                     _WsConnectionParams->property_server = NULL;
                }
            
                
                
                _WsConnectionParams->is_valid_server = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                          
              
           axis2_bool_t AXIS2_CALL
           adb_WsConnectionParams_is_server_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_TRUE);
               
               return !_WsConnectionParams->is_valid_server;
           }

              
                                                              
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_set_server_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               return adb_WsConnectionParams_reset_server(_WsConnectionParams, env);
           }

           

               
                               
               
            int AXIS2_CALL
            adb_WsConnectionParams_get_port(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, (int)0);
                    AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, (int)0);
                  

                return _WsConnectionParams->property_port;
             }

               
                              
               
            axis2_status_t AXIS2_CALL
            adb_WsConnectionParams_set_port(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env,
                    const int  arg_port)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
                
                if(_WsConnectionParams->is_valid_port &&
                        arg_port == _WsConnectionParams->property_port)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsConnectionParams_reset_port(_WsConnectionParams, env);

                _WsConnectionParams->property_port = arg_port;
                        _WsConnectionParams->is_valid_port = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                               
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_reset_port(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
               

               _WsConnectionParams->is_valid_port = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                        
              
           axis2_bool_t AXIS2_CALL
           adb_WsConnectionParams_is_port_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_TRUE);
               
               return !_WsConnectionParams->is_valid_port;
           }

              
                                                            
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_set_port_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               return adb_WsConnectionParams_reset_port(_WsConnectionParams, env);
           }

           

               
                                   
               
            axis2_char_t* AXIS2_CALL
            adb_WsConnectionParams_get_database(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, NULL);
                  

                return _WsConnectionParams->property_database;
             }

               
                                  
               
            axis2_status_t AXIS2_CALL
            adb_WsConnectionParams_set_database(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_database)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
                
                if(_WsConnectionParams->is_valid_database &&
                        arg_database == _WsConnectionParams->property_database)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsConnectionParams_reset_database(_WsConnectionParams, env);

                
                if(NULL == arg_database)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsConnectionParams->property_database = (axis2_char_t *)axutil_strdup(env, arg_database);
                        if(NULL == _WsConnectionParams->property_database)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for database");
                            return AXIS2_FAILURE;
                        }
                        _WsConnectionParams->is_valid_database = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                   
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_reset_database(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsConnectionParams->property_database != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsConnectionParams->property_database);
                     _WsConnectionParams->property_database = NULL;
                }
            
                
                
                _WsConnectionParams->is_valid_database = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                            
              
           axis2_bool_t AXIS2_CALL
           adb_WsConnectionParams_is_database_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_TRUE);
               
               return !_WsConnectionParams->is_valid_database;
           }

              
                                                                
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_set_database_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               return adb_WsConnectionParams_reset_database(_WsConnectionParams, env);
           }

           

               
                               
               
            axis2_char_t* AXIS2_CALL
            adb_WsConnectionParams_get_user(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, NULL);
                  

                return _WsConnectionParams->property_user;
             }

               
                              
               
            axis2_status_t AXIS2_CALL
            adb_WsConnectionParams_set_user(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_user)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
                
                if(_WsConnectionParams->is_valid_user &&
                        arg_user == _WsConnectionParams->property_user)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsConnectionParams_reset_user(_WsConnectionParams, env);

                
                if(NULL == arg_user)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsConnectionParams->property_user = (axis2_char_t *)axutil_strdup(env, arg_user);
                        if(NULL == _WsConnectionParams->property_user)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for user");
                            return AXIS2_FAILURE;
                        }
                        _WsConnectionParams->is_valid_user = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                               
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_reset_user(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsConnectionParams->property_user != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsConnectionParams->property_user);
                     _WsConnectionParams->property_user = NULL;
                }
            
                
                
                _WsConnectionParams->is_valid_user = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                        
              
           axis2_bool_t AXIS2_CALL
           adb_WsConnectionParams_is_user_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_TRUE);
               
               return !_WsConnectionParams->is_valid_user;
           }

              
                                                            
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_set_user_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               return adb_WsConnectionParams_reset_user(_WsConnectionParams, env);
           }

           

               
                                   
               
            axis2_char_t* AXIS2_CALL
            adb_WsConnectionParams_get_password(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, NULL);
                  

                return _WsConnectionParams->property_password;
             }

               
                                  
               
            axis2_status_t AXIS2_CALL
            adb_WsConnectionParams_set_password(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_password)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
                
                if(_WsConnectionParams->is_valid_password &&
                        arg_password == _WsConnectionParams->property_password)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsConnectionParams_reset_password(_WsConnectionParams, env);

                
                if(NULL == arg_password)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsConnectionParams->property_password = (axis2_char_t *)axutil_strdup(env, arg_password);
                        if(NULL == _WsConnectionParams->property_password)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for password");
                            return AXIS2_FAILURE;
                        }
                        _WsConnectionParams->is_valid_password = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                   
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_reset_password(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsConnectionParams->property_password != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsConnectionParams->property_password);
                     _WsConnectionParams->property_password = NULL;
                }
            
                
                
                _WsConnectionParams->is_valid_password = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                            
              
           axis2_bool_t AXIS2_CALL
           adb_WsConnectionParams_is_password_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_TRUE);
               
               return !_WsConnectionParams->is_valid_password;
           }

              
                                                                
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_set_password_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               return adb_WsConnectionParams_reset_password(_WsConnectionParams, env);
           }

           

               
                                                 
               
            axis2_char_t* AXIS2_CALL
            adb_WsConnectionParams_get_manualConnectionString(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, NULL);
                  

                return _WsConnectionParams->property_manualConnectionString;
             }

               
                                                
               
            axis2_status_t AXIS2_CALL
            adb_WsConnectionParams_set_manualConnectionString(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_manualConnectionString)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
                
                if(_WsConnectionParams->is_valid_manualConnectionString &&
                        arg_manualConnectionString == _WsConnectionParams->property_manualConnectionString)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsConnectionParams_reset_manualConnectionString(_WsConnectionParams, env);

                
                if(NULL == arg_manualConnectionString)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsConnectionParams->property_manualConnectionString = (axis2_char_t *)axutil_strdup(env, arg_manualConnectionString);
                        if(NULL == _WsConnectionParams->property_manualConnectionString)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for manualConnectionString");
                            return AXIS2_FAILURE;
                        }
                        _WsConnectionParams->is_valid_manualConnectionString = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                                 
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_reset_manualConnectionString(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsConnectionParams->property_manualConnectionString != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsConnectionParams->property_manualConnectionString);
                     _WsConnectionParams->property_manualConnectionString = NULL;
                }
            
                
                
                _WsConnectionParams->is_valid_manualConnectionString = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                          
              
           axis2_bool_t AXIS2_CALL
           adb_WsConnectionParams_is_manualConnectionString_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_TRUE);
               
               return !_WsConnectionParams->is_valid_manualConnectionString;
           }

              
                                                                              
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_set_manualConnectionString_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               return adb_WsConnectionParams_reset_manualConnectionString(_WsConnectionParams, env);
           }

           

               
                                           
               
            axis2_char_t* AXIS2_CALL
            adb_WsConnectionParams_get_additionalParams(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, NULL);
                  

                return _WsConnectionParams->property_additionalParams;
             }

               
                                          
               
            axis2_status_t AXIS2_CALL
            adb_WsConnectionParams_set_additionalParams(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_additionalParams)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
                
                if(_WsConnectionParams->is_valid_additionalParams &&
                        arg_additionalParams == _WsConnectionParams->property_additionalParams)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsConnectionParams_reset_additionalParams(_WsConnectionParams, env);

                
                if(NULL == arg_additionalParams)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsConnectionParams->property_additionalParams = (axis2_char_t *)axutil_strdup(env, arg_additionalParams);
                        if(NULL == _WsConnectionParams->property_additionalParams)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for additionalParams");
                            return AXIS2_FAILURE;
                        }
                        _WsConnectionParams->is_valid_additionalParams = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                           
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_reset_additionalParams(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsConnectionParams->property_additionalParams != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsConnectionParams->property_additionalParams);
                     _WsConnectionParams->property_additionalParams = NULL;
                }
            
                
                
                _WsConnectionParams->is_valid_additionalParams = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                    
              
           axis2_bool_t AXIS2_CALL
           adb_WsConnectionParams_is_additionalParams_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_TRUE);
               
               return !_WsConnectionParams->is_valid_additionalParams;
           }

              
                                                                        
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_set_additionalParams_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               return adb_WsConnectionParams_reset_additionalParams(_WsConnectionParams, env);
           }

           

               
                                     
               
            axis2_char_t* AXIS2_CALL
            adb_WsConnectionParams_get_driverName(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, NULL);
                    AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, NULL);
                  

                return _WsConnectionParams->property_driverName;
             }

               
                                    
               
            axis2_status_t AXIS2_CALL
            adb_WsConnectionParams_set_driverName(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env,
                    const axis2_char_t*  arg_driverName)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
                
                if(_WsConnectionParams->is_valid_driverName &&
                        arg_driverName == _WsConnectionParams->property_driverName)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsConnectionParams_reset_driverName(_WsConnectionParams, env);

                
                if(NULL == arg_driverName)
                {
                                             
                    return AXIS2_SUCCESS;
                }
                _WsConnectionParams->property_driverName = (axis2_char_t *)axutil_strdup(env, arg_driverName);
                        if(NULL == _WsConnectionParams->property_driverName)
                        {
                            AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "Error allocating memeory for driverName");
                            return AXIS2_FAILURE;
                        }
                        _WsConnectionParams->is_valid_driverName = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                     
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_reset_driverName(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
               

               
            
                
                if(_WsConnectionParams->property_driverName != NULL)
                {
                   
                   
                        AXIS2_FREE(env-> allocator, _WsConnectionParams->property_driverName);
                     _WsConnectionParams->property_driverName = NULL;
                }
            
                
                
                _WsConnectionParams->is_valid_driverName = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                              
              
           axis2_bool_t AXIS2_CALL
           adb_WsConnectionParams_is_driverName_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_TRUE);
               
               return !_WsConnectionParams->is_valid_driverName;
           }

              
                                                                  
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_set_driverName_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               return adb_WsConnectionParams_reset_driverName(_WsConnectionParams, env);
           }

           

               
                                  
               
            int AXIS2_CALL
            adb_WsConnectionParams_get_timeout(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, (int)0);
                    AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, (int)0);
                  

                return _WsConnectionParams->property_timeout;
             }

               
                                 
               
            axis2_status_t AXIS2_CALL
            adb_WsConnectionParams_set_timeout(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env,
                    const int  arg_timeout)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
                
                if(_WsConnectionParams->is_valid_timeout &&
                        arg_timeout == _WsConnectionParams->property_timeout)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsConnectionParams_reset_timeout(_WsConnectionParams, env);

                _WsConnectionParams->property_timeout = arg_timeout;
                        _WsConnectionParams->is_valid_timeout = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                  
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_reset_timeout(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
               

               _WsConnectionParams->is_valid_timeout = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                           
              
           axis2_bool_t AXIS2_CALL
           adb_WsConnectionParams_is_timeout_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_TRUE);
               
               return !_WsConnectionParams->is_valid_timeout;
           }

              
                                                               
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_set_timeout_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               return adb_WsConnectionParams_reset_timeout(_WsConnectionParams, env);
           }

           

               
                                                    
               
            int AXIS2_CALL
            adb_WsConnectionParams_get_transactionIsolationLevel(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env)
             {
                
                    AXIS2_ENV_CHECK(env, (int)0);
                    AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, (int)0);
                  

                return _WsConnectionParams->property_transactionIsolationLevel;
             }

               
                                                   
               
            axis2_status_t AXIS2_CALL
            adb_WsConnectionParams_set_transactionIsolationLevel(
                    adb_WsConnectionParams_t* _WsConnectionParams,
                    const axutil_env_t *env,
                    const int  arg_transactionIsolationLevel)
             {
                

                AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
                AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
                
                if(_WsConnectionParams->is_valid_transactionIsolationLevel &&
                        arg_transactionIsolationLevel == _WsConnectionParams->property_transactionIsolationLevel)
                {
                    
                    return AXIS2_SUCCESS; 
                }

                adb_WsConnectionParams_reset_transactionIsolationLevel(_WsConnectionParams, env);

                _WsConnectionParams->property_transactionIsolationLevel = arg_transactionIsolationLevel;
                        _WsConnectionParams->is_valid_transactionIsolationLevel = AXIS2_TRUE;
                    
                return AXIS2_SUCCESS;
             }

             

              
                                                    
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_reset_transactionIsolationLevel(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               int i = 0;
               int count = 0;
               void *element = NULL;

               AXIS2_ENV_CHECK(env, AXIS2_FAILURE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_FAILURE);
               

               _WsConnectionParams->is_valid_transactionIsolationLevel = AXIS2_FALSE; 
               return AXIS2_SUCCESS;
           }

              
                                                             
              
           axis2_bool_t AXIS2_CALL
           adb_WsConnectionParams_is_transactionIsolationLevel_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               AXIS2_ENV_CHECK(env, AXIS2_TRUE);
               AXIS2_PARAM_CHECK(env->error, _WsConnectionParams, AXIS2_TRUE);
               
               return !_WsConnectionParams->is_valid_transactionIsolationLevel;
           }

              
                                                                                 
              
           axis2_status_t AXIS2_CALL
           adb_WsConnectionParams_set_transactionIsolationLevel_nil(
                   adb_WsConnectionParams_t* _WsConnectionParams,
                   const axutil_env_t *env)
           {
               return adb_WsConnectionParams_reset_transactionIsolationLevel(_WsConnectionParams, env);
           }

           

