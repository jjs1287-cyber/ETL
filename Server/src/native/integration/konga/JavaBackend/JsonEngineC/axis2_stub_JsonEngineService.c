

         
                                       
        
                                                                                                                                                  
                                                                                        
         

      #include "axis2_stub_JsonEngineService.h"
      #include <axis2_msg.h>

         
                                                      
         

      axis2_stub_t*
      axis2_stub_create_JsonEngineService(const axutil_env_t *env,
                                      axis2_char_t *client_home,
                                      axis2_char_t *endpoint_uri)
      {
         axis2_stub_t *stub = NULL;
         axis2_endpoint_ref_t *endpoint_ref = NULL;
         AXIS2_FUNC_PARAM_CHECK (client_home, env, NULL)

         if (NULL == endpoint_uri)
         {
            endpoint_uri = axis2_stub_get_endpoint_uri_of_JsonEngineService(env);
         }

         endpoint_ref = axis2_endpoint_ref_create(env, endpoint_uri);

         stub = axis2_stub_create_with_endpoint_ref_and_client_home (env, endpoint_ref, client_home);

         if (NULL == stub)
         {
            if(NULL != endpoint_ref)
            {
                axis2_endpoint_ref_free(endpoint_ref, env);
            }
            return NULL;
         }


         axis2_stub_populate_services_for_JsonEngineService(stub, env);
         return stub;
      }


      void
      axis2_stub_populate_services_for_JsonEngineService(axis2_stub_t *stub, const axutil_env_t *env)
      {
         axis2_svc_client_t *svc_client = NULL;
         axutil_qname_t *svc_qname =  NULL;
         axutil_qname_t *op_qname =  NULL;
         axis2_svc_t *svc = NULL;
         axis2_op_t *op = NULL;
         axis2_op_t *annon_op = NULL;
         axis2_msg_t *msg_out = NULL;
         axis2_msg_t *msg_in = NULL;
         axis2_msg_t *msg_out_fault = NULL;
         axis2_msg_t *msg_in_fault = NULL;


                                    
         svc_client = axis2_stub_get_svc_client (stub, env );
         svc = (axis2_svc_t*)axis2_svc_client_get_svc( svc_client, env );

         annon_op = axis2_svc_get_op_with_name(svc, env, AXIS2_ANON_OUT_IN_OP);
         msg_out = axis2_op_get_msg(annon_op, env, AXIS2_MSG_OUT);
         msg_in = axis2_op_get_msg(annon_op, env, AXIS2_MSG_IN);
         msg_out_fault = axis2_op_get_msg(annon_op, env, AXIS2_MSG_OUT_FAULT);
         msg_in_fault = axis2_op_get_msg(annon_op, env, AXIS2_MSG_IN_FAULT);

         svc_qname = axutil_qname_create(env,"JsonEngineService" ,NULL, NULL);
         axis2_svc_set_qname (svc, env, svc_qname);

                                     

         
           op_qname = axutil_qname_create(env,
                                         "jsonToXml" ,
                                         "http://json.webservice.backend.server.integration.etlforge.invalid",
                                         NULL);
           op = axis2_op_create_with_qname(env, op_qname);
           
               axis2_op_set_msg_exchange_pattern(op, env, AXIS2_MEP_URI_OUT_IN);
             
           axis2_msg_increment_ref(msg_out, env);
           axis2_msg_increment_ref(msg_in, env);
           axis2_msg_increment_ref(msg_out_fault, env);
           axis2_msg_increment_ref(msg_in_fault, env);
           axis2_op_add_msg(op, env, AXIS2_MSG_OUT, msg_out);
           axis2_op_add_msg(op, env, AXIS2_MSG_IN, msg_in);
           axis2_op_add_msg(op, env, AXIS2_MSG_OUT_FAULT, msg_out_fault);
           axis2_op_add_msg(op, env, AXIS2_MSG_IN_FAULT, msg_in_fault);
           
           axis2_svc_add_op(svc, env, op);

         
           op_qname = axutil_qname_create(env,
                                         "xmlToJson" ,
                                         "http://json.webservice.backend.server.integration.etlforge.invalid",
                                         NULL);
           op = axis2_op_create_with_qname(env, op_qname);
           
               axis2_op_set_msg_exchange_pattern(op, env, AXIS2_MEP_URI_OUT_IN);
             
           axis2_msg_increment_ref(msg_out, env);
           axis2_msg_increment_ref(msg_in, env);
           axis2_msg_increment_ref(msg_out_fault, env);
           axis2_msg_increment_ref(msg_in_fault, env);
           axis2_op_add_msg(op, env, AXIS2_MSG_OUT, msg_out);
           axis2_op_add_msg(op, env, AXIS2_MSG_IN, msg_in);
           axis2_op_add_msg(op, env, AXIS2_MSG_OUT_FAULT, msg_out_fault);
           axis2_op_add_msg(op, env, AXIS2_MSG_IN_FAULT, msg_in_fault);
           
           axis2_svc_add_op(svc, env, op);

         
           op_qname = axutil_qname_create(env,
                                         "startNewJsonSession" ,
                                         "http://json.webservice.backend.server.integration.etlforge.invalid",
                                         NULL);
           op = axis2_op_create_with_qname(env, op_qname);
           
               axis2_op_set_msg_exchange_pattern(op, env, AXIS2_MEP_URI_OUT_IN);
             
           axis2_msg_increment_ref(msg_out, env);
           axis2_msg_increment_ref(msg_in, env);
           axis2_msg_increment_ref(msg_out_fault, env);
           axis2_msg_increment_ref(msg_in_fault, env);
           axis2_op_add_msg(op, env, AXIS2_MSG_OUT, msg_out);
           axis2_op_add_msg(op, env, AXIS2_MSG_IN, msg_in);
           axis2_op_add_msg(op, env, AXIS2_MSG_OUT_FAULT, msg_out_fault);
           axis2_op_add_msg(op, env, AXIS2_MSG_IN_FAULT, msg_in_fault);
           
           axis2_svc_add_op(svc, env, op);

         
           op_qname = axutil_qname_create(env,
                                         "closeJsonSession" ,
                                         "http://json.webservice.backend.server.integration.etlforge.invalid",
                                         NULL);
           op = axis2_op_create_with_qname(env, op_qname);
           
               axis2_op_set_msg_exchange_pattern(op, env, AXIS2_MEP_URI_OUT_IN);
             
           axis2_msg_increment_ref(msg_out, env);
           axis2_msg_increment_ref(msg_in, env);
           axis2_msg_increment_ref(msg_out_fault, env);
           axis2_msg_increment_ref(msg_in_fault, env);
           axis2_op_add_msg(op, env, AXIS2_MSG_OUT, msg_out);
           axis2_op_add_msg(op, env, AXIS2_MSG_IN, msg_in);
           axis2_op_add_msg(op, env, AXIS2_MSG_OUT_FAULT, msg_out_fault);
           axis2_op_add_msg(op, env, AXIS2_MSG_IN_FAULT, msg_in_fault);
           
           axis2_svc_add_op(svc, env, op);

         
      }

         
                                         
         
      axis2_char_t*
      axis2_stub_get_endpoint_uri_of_JsonEngineService( const axutil_env_t *env )
      {
        axis2_char_t *endpoint_uri = NULL;
                                       
        
              endpoint_uri = "http://localhost/axis/konga_json_engine";
            
        return endpoint_uri;
      }


  
            
                                           
                                                                                                         
           
                             
           
                                            
            
         adb_jsonToXmlResponse_t* 
         axis2_stub_op_JsonEngineService_jsonToXml( axis2_stub_t *stub, const axutil_env_t *env,
                                              adb_jsonToXml_t* _jsonToXml)
         {
            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;
            axiom_node_t *ret_node = NULL;

            const axis2_char_t *soap_action = NULL;
            axutil_qname_t *op_qname =  NULL;
            axiom_node_t *payload = NULL;
            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            adb_jsonToXmlResponse_t* ret_val = NULL;
            
                       payload = adb_jsonToXml_serialize(_jsonToXml, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   
            svc_client = axis2_stub_get_svc_client(stub, env );
            
           
            
            

            options = axis2_stub_get_options( stub, env);
            if (NULL == options)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_INVALID_NULL_PARAM, AXIS2_FAILURE);
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "options is null in stub");
                return NULL;
            }
            soap_action = axis2_options_get_action( options, env );
            if (NULL == soap_action)
            {
              is_soap_act_set = AXIS2_FALSE;
              soap_action = "jsonToXml";
              
              soap_act = axutil_string_create(env, "jsonToXml");
              axis2_options_set_soap_action(options, env, soap_act);    
              
              axis2_options_set_action( options, env, soap_action );
            }
            
            axis2_options_set_soap_version(options, env, AXIOM_SOAP11 );
            
            ret_node =  axis2_svc_client_send_receive_with_op_qname( svc_client, env, op_qname, payload);
 
            if (!is_soap_act_set)
            {
              
              axis2_options_set_soap_action(options, env, NULL);    
              
              axis2_options_set_action( options, env, NULL);
            }

            
            
                    if ( NULL == ret_node )
                    {
                        return NULL;
                    }
                    ret_val = adb_jsonToXmlResponse_create(env);

                    if(adb_jsonToXmlResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                    {
                        if(ret_val != NULL)
                        {
                            adb_jsonToXmlResponse_free(ret_val, env);
                        }

                        AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the jsonToXmlResponse_deserialize: "
                                                                "This should be due to an invalid XML");
                        return NULL;
                    }
                    return ret_val;
                
        }
        
            
                                           
                                                                                                         
           
                             
           
                                            
            
         adb_xmlToJsonResponse_t* 
         axis2_stub_op_JsonEngineService_xmlToJson( axis2_stub_t *stub, const axutil_env_t *env,
                                              adb_xmlToJson_t* _xmlToJson)
         {
            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;
            axiom_node_t *ret_node = NULL;

            const axis2_char_t *soap_action = NULL;
            axutil_qname_t *op_qname =  NULL;
            axiom_node_t *payload = NULL;
            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            adb_xmlToJsonResponse_t* ret_val = NULL;
            
                       payload = adb_xmlToJson_serialize(_xmlToJson, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   
            svc_client = axis2_stub_get_svc_client(stub, env );
            
           
            
            

            options = axis2_stub_get_options( stub, env);
            if (NULL == options)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_INVALID_NULL_PARAM, AXIS2_FAILURE);
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "options is null in stub");
                return NULL;
            }
            soap_action = axis2_options_get_action( options, env );
            if (NULL == soap_action)
            {
              is_soap_act_set = AXIS2_FALSE;
              soap_action = "xmlToJson";
              
              soap_act = axutil_string_create(env, "xmlToJson");
              axis2_options_set_soap_action(options, env, soap_act);    
              
              axis2_options_set_action( options, env, soap_action );
            }
            
            axis2_options_set_soap_version(options, env, AXIOM_SOAP11 );
            
            ret_node =  axis2_svc_client_send_receive_with_op_qname( svc_client, env, op_qname, payload);
 
            if (!is_soap_act_set)
            {
              
              axis2_options_set_soap_action(options, env, NULL);    
              
              axis2_options_set_action( options, env, NULL);
            }

            
            
                    if ( NULL == ret_node )
                    {
                        return NULL;
                    }
                    ret_val = adb_xmlToJsonResponse_create(env);

                    if(adb_xmlToJsonResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                    {
                        if(ret_val != NULL)
                        {
                            adb_xmlToJsonResponse_free(ret_val, env);
                        }

                        AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the xmlToJsonResponse_deserialize: "
                                                                "This should be due to an invalid XML");
                        return NULL;
                    }
                    return ret_val;
                
        }
        
            
                                           
                                                                                                                   
           
                                       
           
                                                      
            
         adb_startNewJsonSessionResponse_t* 
         axis2_stub_op_JsonEngineService_startNewJsonSession( axis2_stub_t *stub, const axutil_env_t *env,
                                              adb_startNewJsonSession_t* _startNewJsonSession)
         {
            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;
            axiom_node_t *ret_node = NULL;

            const axis2_char_t *soap_action = NULL;
            axutil_qname_t *op_qname =  NULL;
            axiom_node_t *payload = NULL;
            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            adb_startNewJsonSessionResponse_t* ret_val = NULL;
            
                       payload = adb_startNewJsonSession_serialize(_startNewJsonSession, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   
            svc_client = axis2_stub_get_svc_client(stub, env );
            
           
            
            

            options = axis2_stub_get_options( stub, env);
            if (NULL == options)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_INVALID_NULL_PARAM, AXIS2_FAILURE);
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "options is null in stub");
                return NULL;
            }
            soap_action = axis2_options_get_action( options, env );
            if (NULL == soap_action)
            {
              is_soap_act_set = AXIS2_FALSE;
              soap_action = "startNewJsonSession";
              
              soap_act = axutil_string_create(env, "startNewJsonSession");
              axis2_options_set_soap_action(options, env, soap_act);    
              
              axis2_options_set_action( options, env, soap_action );
            }
            
            axis2_options_set_soap_version(options, env, AXIOM_SOAP11 );
            
            ret_node =  axis2_svc_client_send_receive_with_op_qname( svc_client, env, op_qname, payload);
 
            if (!is_soap_act_set)
            {
              
              axis2_options_set_soap_action(options, env, NULL);    
              
              axis2_options_set_action( options, env, NULL);
            }

            
            
                    if ( NULL == ret_node )
                    {
                        return NULL;
                    }
                    ret_val = adb_startNewJsonSessionResponse_create(env);

                    if(adb_startNewJsonSessionResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                    {
                        if(ret_val != NULL)
                        {
                            adb_startNewJsonSessionResponse_free(ret_val, env);
                        }

                        AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the startNewJsonSessionResponse_deserialize: "
                                                                "This should be due to an invalid XML");
                        return NULL;
                    }
                    return ret_val;
                
        }
        
            
                                           
                                                                                                                
           
                                    
           
                                                   
            
         adb_closeJsonSessionResponse_t* 
         axis2_stub_op_JsonEngineService_closeJsonSession( axis2_stub_t *stub, const axutil_env_t *env,
                                              adb_closeJsonSession_t* _closeJsonSession)
         {
            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;
            axiom_node_t *ret_node = NULL;

            const axis2_char_t *soap_action = NULL;
            axutil_qname_t *op_qname =  NULL;
            axiom_node_t *payload = NULL;
            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            adb_closeJsonSessionResponse_t* ret_val = NULL;
            
                       payload = adb_closeJsonSession_serialize(_closeJsonSession, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   
            svc_client = axis2_stub_get_svc_client(stub, env );
            
           
            
            

            options = axis2_stub_get_options( stub, env);
            if (NULL == options)
            {
                AXIS2_ERROR_SET(env->error, AXIS2_ERROR_INVALID_NULL_PARAM, AXIS2_FAILURE);
                AXIS2_LOG_ERROR(env->log, AXIS2_LOG_SI, "options is null in stub");
                return NULL;
            }
            soap_action = axis2_options_get_action( options, env );
            if (NULL == soap_action)
            {
              is_soap_act_set = AXIS2_FALSE;
              soap_action = "closeJsonSession";
              
              soap_act = axutil_string_create(env, "closeJsonSession");
              axis2_options_set_soap_action(options, env, soap_act);    
              
              axis2_options_set_action( options, env, soap_action );
            }
            
            axis2_options_set_soap_version(options, env, AXIOM_SOAP11 );
            
            ret_node =  axis2_svc_client_send_receive_with_op_qname( svc_client, env, op_qname, payload);
 
            if (!is_soap_act_set)
            {
              
              axis2_options_set_soap_action(options, env, NULL);    
              
              axis2_options_set_action( options, env, NULL);
            }

            
            
                    if ( NULL == ret_node )
                    {
                        return NULL;
                    }
                    ret_val = adb_closeJsonSessionResponse_create(env);

                    if(adb_closeJsonSessionResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                    {
                        if(ret_val != NULL)
                        {
                            adb_closeJsonSessionResponse_free(ret_val, env);
                        }

                        AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the closeJsonSessionResponse_deserialize: "
                                                                "This should be due to an invalid XML");
                        return NULL;
                    }
                    return ret_val;
                
        }
        

        struct axis2_stub_JsonEngineService_jsonToXml_callback_data
        {   
            void *data;
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_jsonToXmlResponse_t* _jsonToXmlResponse, void *data);
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data);
        };

        static axis2_status_t AXIS2_CALL axis2_stub_on_error_JsonEngineService_jsonToXml(axis2_callback_t *callback, const axutil_env_t *env, int exception)
        {
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int, void *data);
            struct axis2_stub_JsonEngineService_jsonToXml_callback_data* callback_data = NULL;
            void *user_data = NULL;

            axis2_status_t status;
        
            callback_data = (struct axis2_stub_JsonEngineService_jsonToXml_callback_data*)axis2_callback_get_data(callback);
        
            user_data = callback_data->data;
            on_error = callback_data->on_error;
        
            status = on_error(env, exception, user_data);

            if(callback_data)
            {
                AXIS2_FREE(env->allocator, callback_data);
            }
            return status;
        } 

        axis2_status_t AXIS2_CALL axis2_stub_on_complete_JsonEngineService_jsonToXml(axis2_callback_t *callback, const axutil_env_t *env)
        {
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_jsonToXmlResponse_t* _jsonToXmlResponse, void *data);
            struct axis2_stub_JsonEngineService_jsonToXml_callback_data* callback_data = NULL;
            void *user_data = NULL;
            axis2_status_t status = AXIS2_SUCCESS;
 
            adb_jsonToXmlResponse_t* ret_val = NULL;
            

            axiom_node_t *ret_node = NULL;
            axiom_soap_envelope_t *soap_envelope = NULL;

            

            callback_data = (struct axis2_stub_JsonEngineService_jsonToXml_callback_data*)axis2_callback_get_data(callback);

            soap_envelope = axis2_callback_get_envelope(callback, env);
            if(soap_envelope)
            {
                axiom_soap_body_t *soap_body;
                soap_body = axiom_soap_envelope_get_body(soap_envelope, env);
                if(soap_body)
                {
                    axiom_node_t *body_node = axiom_soap_body_get_base_node(soap_body, env);
                    if(body_node)
                    {
                        ret_node = axiom_node_get_first_child(body_node, env);
                    }
                }
                
                
            }

            user_data = callback_data->data;
            on_complete = callback_data->on_complete;

            
                    if(ret_node != NULL)
                    {
                        ret_val = adb_jsonToXmlResponse_create(env);
     
                        if(adb_jsonToXmlResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                        {
                            AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the LendResponse_deserialize: "
                                                                    "This should be due to an invalid XML");
                            adb_jsonToXmlResponse_free(ret_val, env);
                            ret_val = NULL;
                        }
                     }
                     else
                     {
                         ret_val = NULL; 
                     }
                     status = on_complete(env, ret_val, user_data);
                
 
            if(callback_data)
            {
                AXIS2_FREE(env->allocator, callback_data);
            }
            return status;
        }

           
                                                                        
                                                                                                         
          
           
                             
                                                             
                                                       
            
         void axis2_stub_start_op_JsonEngineService_jsonToXml( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_jsonToXml_t* _jsonToXml,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_jsonToXmlResponse_t* _jsonToXmlResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) )
         {

            axis2_callback_t *callback = NULL;

            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;

            const axis2_char_t *soap_action = NULL;
            axiom_node_t *payload = NULL;

            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            
            
            struct axis2_stub_JsonEngineService_jsonToXml_callback_data *callback_data;

            callback_data = (struct axis2_stub_JsonEngineService_jsonToXml_callback_data*) AXIS2_MALLOC(env->allocator, 
                                    sizeof(struct axis2_stub_JsonEngineService_jsonToXml_callback_data));
            if(NULL == callback_data)
            {
                AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "Can not allocate memeory for the callback data structures");
                return;
            }
            
            
                       payload = adb_jsonToXml_serialize(_jsonToXml, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   



            options = axis2_stub_get_options( stub, env);
            if (NULL == options)
            {
              AXIS2_ERROR_SET(env->error, AXIS2_ERROR_INVALID_NULL_PARAM, AXIS2_FAILURE);
              AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "options is null in stub");
              return;
            }
            svc_client = axis2_stub_get_svc_client (stub, env);
            soap_action =axis2_options_get_action (options, env);
            if (NULL == soap_action)
            {
              is_soap_act_set = AXIS2_FALSE;
              soap_action = "jsonToXml";
              
              soap_act = axutil_string_create(env, "jsonToXml");
              axis2_options_set_soap_action(options, env, soap_act);
              
              axis2_options_set_action( options, env, soap_action);
            }
            
            axis2_options_set_soap_version(options, env, AXIOM_SOAP11);
             

            callback = axis2_callback_create(env);
                                                                             
            axis2_callback_set_on_complete(callback, axis2_stub_on_complete_JsonEngineService_jsonToXml);
                                                                          
            axis2_callback_set_on_error(callback, axis2_stub_on_error_JsonEngineService_jsonToXml);

            callback_data-> data = user_data;
            callback_data-> on_complete = on_complete;
            callback_data-> on_error = on_error;

            axis2_callback_set_data(callback, (void*)callback_data);

                              
            axis2_svc_client_send_receive_non_blocking(svc_client, env, payload, callback);
            
            if (!is_soap_act_set)
            {
              
              axis2_options_set_soap_action(options, env, NULL);
              
              axis2_options_set_action(options, env, NULL);
            }
         }

         

        struct axis2_stub_JsonEngineService_xmlToJson_callback_data
        {   
            void *data;
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_xmlToJsonResponse_t* _xmlToJsonResponse, void *data);
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data);
        };

        static axis2_status_t AXIS2_CALL axis2_stub_on_error_JsonEngineService_xmlToJson(axis2_callback_t *callback, const axutil_env_t *env, int exception)
        {
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int, void *data);
            struct axis2_stub_JsonEngineService_xmlToJson_callback_data* callback_data = NULL;
            void *user_data = NULL;

            axis2_status_t status;
        
            callback_data = (struct axis2_stub_JsonEngineService_xmlToJson_callback_data*)axis2_callback_get_data(callback);
        
            user_data = callback_data->data;
            on_error = callback_data->on_error;
        
            status = on_error(env, exception, user_data);

            if(callback_data)
            {
                AXIS2_FREE(env->allocator, callback_data);
            }
            return status;
        } 

        axis2_status_t AXIS2_CALL axis2_stub_on_complete_JsonEngineService_xmlToJson(axis2_callback_t *callback, const axutil_env_t *env)
        {
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_xmlToJsonResponse_t* _xmlToJsonResponse, void *data);
            struct axis2_stub_JsonEngineService_xmlToJson_callback_data* callback_data = NULL;
            void *user_data = NULL;
            axis2_status_t status = AXIS2_SUCCESS;
 
            adb_xmlToJsonResponse_t* ret_val = NULL;
            

            axiom_node_t *ret_node = NULL;
            axiom_soap_envelope_t *soap_envelope = NULL;

            

            callback_data = (struct axis2_stub_JsonEngineService_xmlToJson_callback_data*)axis2_callback_get_data(callback);

            soap_envelope = axis2_callback_get_envelope(callback, env);
            if(soap_envelope)
            {
                axiom_soap_body_t *soap_body;
                soap_body = axiom_soap_envelope_get_body(soap_envelope, env);
                if(soap_body)
                {
                    axiom_node_t *body_node = axiom_soap_body_get_base_node(soap_body, env);
                    if(body_node)
                    {
                        ret_node = axiom_node_get_first_child(body_node, env);
                    }
                }
                
                
            }

            user_data = callback_data->data;
            on_complete = callback_data->on_complete;

            
                    if(ret_node != NULL)
                    {
                        ret_val = adb_xmlToJsonResponse_create(env);
     
                        if(adb_xmlToJsonResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                        {
                            AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the LendResponse_deserialize: "
                                                                    "This should be due to an invalid XML");
                            adb_xmlToJsonResponse_free(ret_val, env);
                            ret_val = NULL;
                        }
                     }
                     else
                     {
                         ret_val = NULL; 
                     }
                     status = on_complete(env, ret_val, user_data);
                
 
            if(callback_data)
            {
                AXIS2_FREE(env->allocator, callback_data);
            }
            return status;
        }

           
                                                                        
                                                                                                         
          
           
                             
                                                             
                                                       
            
         void axis2_stub_start_op_JsonEngineService_xmlToJson( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_xmlToJson_t* _xmlToJson,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_xmlToJsonResponse_t* _xmlToJsonResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) )
         {

            axis2_callback_t *callback = NULL;

            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;

            const axis2_char_t *soap_action = NULL;
            axiom_node_t *payload = NULL;

            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            
            
            struct axis2_stub_JsonEngineService_xmlToJson_callback_data *callback_data;

            callback_data = (struct axis2_stub_JsonEngineService_xmlToJson_callback_data*) AXIS2_MALLOC(env->allocator, 
                                    sizeof(struct axis2_stub_JsonEngineService_xmlToJson_callback_data));
            if(NULL == callback_data)
            {
                AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "Can not allocate memeory for the callback data structures");
                return;
            }
            
            
                       payload = adb_xmlToJson_serialize(_xmlToJson, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   



            options = axis2_stub_get_options( stub, env);
            if (NULL == options)
            {
              AXIS2_ERROR_SET(env->error, AXIS2_ERROR_INVALID_NULL_PARAM, AXIS2_FAILURE);
              AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "options is null in stub");
              return;
            }
            svc_client = axis2_stub_get_svc_client (stub, env);
            soap_action =axis2_options_get_action (options, env);
            if (NULL == soap_action)
            {
              is_soap_act_set = AXIS2_FALSE;
              soap_action = "xmlToJson";
              
              soap_act = axutil_string_create(env, "xmlToJson");
              axis2_options_set_soap_action(options, env, soap_act);
              
              axis2_options_set_action( options, env, soap_action);
            }
            
            axis2_options_set_soap_version(options, env, AXIOM_SOAP11);
             

            callback = axis2_callback_create(env);
                                                                             
            axis2_callback_set_on_complete(callback, axis2_stub_on_complete_JsonEngineService_xmlToJson);
                                                                          
            axis2_callback_set_on_error(callback, axis2_stub_on_error_JsonEngineService_xmlToJson);

            callback_data-> data = user_data;
            callback_data-> on_complete = on_complete;
            callback_data-> on_error = on_error;

            axis2_callback_set_data(callback, (void*)callback_data);

                              
            axis2_svc_client_send_receive_non_blocking(svc_client, env, payload, callback);
            
            if (!is_soap_act_set)
            {
              
              axis2_options_set_soap_action(options, env, NULL);
              
              axis2_options_set_action(options, env, NULL);
            }
         }

         

        struct axis2_stub_JsonEngineService_startNewJsonSession_callback_data
        {   
            void *data;
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_startNewJsonSessionResponse_t* _startNewJsonSessionResponse, void *data);
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data);
        };

        static axis2_status_t AXIS2_CALL axis2_stub_on_error_JsonEngineService_startNewJsonSession(axis2_callback_t *callback, const axutil_env_t *env, int exception)
        {
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int, void *data);
            struct axis2_stub_JsonEngineService_startNewJsonSession_callback_data* callback_data = NULL;
            void *user_data = NULL;

            axis2_status_t status;
        
            callback_data = (struct axis2_stub_JsonEngineService_startNewJsonSession_callback_data*)axis2_callback_get_data(callback);
        
            user_data = callback_data->data;
            on_error = callback_data->on_error;
        
            status = on_error(env, exception, user_data);

            if(callback_data)
            {
                AXIS2_FREE(env->allocator, callback_data);
            }
            return status;
        } 

        axis2_status_t AXIS2_CALL axis2_stub_on_complete_JsonEngineService_startNewJsonSession(axis2_callback_t *callback, const axutil_env_t *env)
        {
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_startNewJsonSessionResponse_t* _startNewJsonSessionResponse, void *data);
            struct axis2_stub_JsonEngineService_startNewJsonSession_callback_data* callback_data = NULL;
            void *user_data = NULL;
            axis2_status_t status = AXIS2_SUCCESS;
 
            adb_startNewJsonSessionResponse_t* ret_val = NULL;
            

            axiom_node_t *ret_node = NULL;
            axiom_soap_envelope_t *soap_envelope = NULL;

            

            callback_data = (struct axis2_stub_JsonEngineService_startNewJsonSession_callback_data*)axis2_callback_get_data(callback);

            soap_envelope = axis2_callback_get_envelope(callback, env);
            if(soap_envelope)
            {
                axiom_soap_body_t *soap_body;
                soap_body = axiom_soap_envelope_get_body(soap_envelope, env);
                if(soap_body)
                {
                    axiom_node_t *body_node = axiom_soap_body_get_base_node(soap_body, env);
                    if(body_node)
                    {
                        ret_node = axiom_node_get_first_child(body_node, env);
                    }
                }
                
                
            }

            user_data = callback_data->data;
            on_complete = callback_data->on_complete;

            
                    if(ret_node != NULL)
                    {
                        ret_val = adb_startNewJsonSessionResponse_create(env);
     
                        if(adb_startNewJsonSessionResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                        {
                            AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the LendResponse_deserialize: "
                                                                    "This should be due to an invalid XML");
                            adb_startNewJsonSessionResponse_free(ret_val, env);
                            ret_val = NULL;
                        }
                     }
                     else
                     {
                         ret_val = NULL; 
                     }
                     status = on_complete(env, ret_val, user_data);
                
 
            if(callback_data)
            {
                AXIS2_FREE(env->allocator, callback_data);
            }
            return status;
        }

           
                                                                        
                                                                                                                   
          
           
                                       
                                                             
                                                       
            
         void axis2_stub_start_op_JsonEngineService_startNewJsonSession( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_startNewJsonSession_t* _startNewJsonSession,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_startNewJsonSessionResponse_t* _startNewJsonSessionResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) )
         {

            axis2_callback_t *callback = NULL;

            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;

            const axis2_char_t *soap_action = NULL;
            axiom_node_t *payload = NULL;

            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            
            
            struct axis2_stub_JsonEngineService_startNewJsonSession_callback_data *callback_data;

            callback_data = (struct axis2_stub_JsonEngineService_startNewJsonSession_callback_data*) AXIS2_MALLOC(env->allocator, 
                                    sizeof(struct axis2_stub_JsonEngineService_startNewJsonSession_callback_data));
            if(NULL == callback_data)
            {
                AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "Can not allocate memeory for the callback data structures");
                return;
            }
            
            
                       payload = adb_startNewJsonSession_serialize(_startNewJsonSession, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   



            options = axis2_stub_get_options( stub, env);
            if (NULL == options)
            {
              AXIS2_ERROR_SET(env->error, AXIS2_ERROR_INVALID_NULL_PARAM, AXIS2_FAILURE);
              AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "options is null in stub");
              return;
            }
            svc_client = axis2_stub_get_svc_client (stub, env);
            soap_action =axis2_options_get_action (options, env);
            if (NULL == soap_action)
            {
              is_soap_act_set = AXIS2_FALSE;
              soap_action = "startNewJsonSession";
              
              soap_act = axutil_string_create(env, "startNewJsonSession");
              axis2_options_set_soap_action(options, env, soap_act);
              
              axis2_options_set_action( options, env, soap_action);
            }
            
            axis2_options_set_soap_version(options, env, AXIOM_SOAP11);
             

            callback = axis2_callback_create(env);
                                                                             
            axis2_callback_set_on_complete(callback, axis2_stub_on_complete_JsonEngineService_startNewJsonSession);
                                                                          
            axis2_callback_set_on_error(callback, axis2_stub_on_error_JsonEngineService_startNewJsonSession);

            callback_data-> data = user_data;
            callback_data-> on_complete = on_complete;
            callback_data-> on_error = on_error;

            axis2_callback_set_data(callback, (void*)callback_data);

                              
            axis2_svc_client_send_receive_non_blocking(svc_client, env, payload, callback);
            
            if (!is_soap_act_set)
            {
              
              axis2_options_set_soap_action(options, env, NULL);
              
              axis2_options_set_action(options, env, NULL);
            }
         }

         

        struct axis2_stub_JsonEngineService_closeJsonSession_callback_data
        {   
            void *data;
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_closeJsonSessionResponse_t* _closeJsonSessionResponse, void *data);
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data);
        };

        static axis2_status_t AXIS2_CALL axis2_stub_on_error_JsonEngineService_closeJsonSession(axis2_callback_t *callback, const axutil_env_t *env, int exception)
        {
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int, void *data);
            struct axis2_stub_JsonEngineService_closeJsonSession_callback_data* callback_data = NULL;
            void *user_data = NULL;

            axis2_status_t status;
        
            callback_data = (struct axis2_stub_JsonEngineService_closeJsonSession_callback_data*)axis2_callback_get_data(callback);
        
            user_data = callback_data->data;
            on_error = callback_data->on_error;
        
            status = on_error(env, exception, user_data);

            if(callback_data)
            {
                AXIS2_FREE(env->allocator, callback_data);
            }
            return status;
        } 

        axis2_status_t AXIS2_CALL axis2_stub_on_complete_JsonEngineService_closeJsonSession(axis2_callback_t *callback, const axutil_env_t *env)
        {
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_closeJsonSessionResponse_t* _closeJsonSessionResponse, void *data);
            struct axis2_stub_JsonEngineService_closeJsonSession_callback_data* callback_data = NULL;
            void *user_data = NULL;
            axis2_status_t status = AXIS2_SUCCESS;
 
            adb_closeJsonSessionResponse_t* ret_val = NULL;
            

            axiom_node_t *ret_node = NULL;
            axiom_soap_envelope_t *soap_envelope = NULL;

            

            callback_data = (struct axis2_stub_JsonEngineService_closeJsonSession_callback_data*)axis2_callback_get_data(callback);

            soap_envelope = axis2_callback_get_envelope(callback, env);
            if(soap_envelope)
            {
                axiom_soap_body_t *soap_body;
                soap_body = axiom_soap_envelope_get_body(soap_envelope, env);
                if(soap_body)
                {
                    axiom_node_t *body_node = axiom_soap_body_get_base_node(soap_body, env);
                    if(body_node)
                    {
                        ret_node = axiom_node_get_first_child(body_node, env);
                    }
                }
                
                
            }

            user_data = callback_data->data;
            on_complete = callback_data->on_complete;

            
                    if(ret_node != NULL)
                    {
                        ret_val = adb_closeJsonSessionResponse_create(env);
     
                        if(adb_closeJsonSessionResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                        {
                            AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the LendResponse_deserialize: "
                                                                    "This should be due to an invalid XML");
                            adb_closeJsonSessionResponse_free(ret_val, env);
                            ret_val = NULL;
                        }
                     }
                     else
                     {
                         ret_val = NULL; 
                     }
                     status = on_complete(env, ret_val, user_data);
                
 
            if(callback_data)
            {
                AXIS2_FREE(env->allocator, callback_data);
            }
            return status;
        }

           
                                                                        
                                                                                                                
          
           
                                    
                                                             
                                                       
            
         void axis2_stub_start_op_JsonEngineService_closeJsonSession( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_closeJsonSession_t* _closeJsonSession,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_closeJsonSessionResponse_t* _closeJsonSessionResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) )
         {

            axis2_callback_t *callback = NULL;

            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;

            const axis2_char_t *soap_action = NULL;
            axiom_node_t *payload = NULL;

            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            
            
            struct axis2_stub_JsonEngineService_closeJsonSession_callback_data *callback_data;

            callback_data = (struct axis2_stub_JsonEngineService_closeJsonSession_callback_data*) AXIS2_MALLOC(env->allocator, 
                                    sizeof(struct axis2_stub_JsonEngineService_closeJsonSession_callback_data));
            if(NULL == callback_data)
            {
                AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "Can not allocate memeory for the callback data structures");
                return;
            }
            
            
                       payload = adb_closeJsonSession_serialize(_closeJsonSession, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   



            options = axis2_stub_get_options( stub, env);
            if (NULL == options)
            {
              AXIS2_ERROR_SET(env->error, AXIS2_ERROR_INVALID_NULL_PARAM, AXIS2_FAILURE);
              AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "options is null in stub");
              return;
            }
            svc_client = axis2_stub_get_svc_client (stub, env);
            soap_action =axis2_options_get_action (options, env);
            if (NULL == soap_action)
            {
              is_soap_act_set = AXIS2_FALSE;
              soap_action = "closeJsonSession";
              
              soap_act = axutil_string_create(env, "closeJsonSession");
              axis2_options_set_soap_action(options, env, soap_act);
              
              axis2_options_set_action( options, env, soap_action);
            }
            
            axis2_options_set_soap_version(options, env, AXIOM_SOAP11);
             

            callback = axis2_callback_create(env);
                                                                             
            axis2_callback_set_on_complete(callback, axis2_stub_on_complete_JsonEngineService_closeJsonSession);
                                                                          
            axis2_callback_set_on_error(callback, axis2_stub_on_error_JsonEngineService_closeJsonSession);

            callback_data-> data = user_data;
            callback_data-> on_complete = on_complete;
            callback_data-> on_error = on_error;

            axis2_callback_set_data(callback, (void*)callback_data);

                              
            axis2_svc_client_send_receive_non_blocking(svc_client, env, payload, callback);
            
            if (!is_soap_act_set)
            {
              
              axis2_options_set_soap_action(options, env, NULL);
              
              axis2_options_set_action(options, env, NULL);
            }
         }

            


        
                                                
        
     


        
                                                 
        
     

