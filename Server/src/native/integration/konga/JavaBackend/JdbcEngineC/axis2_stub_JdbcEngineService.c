

         
                                       
        
                                                                                                                                                  
                                                                                        
         

      #include "axis2_stub_JdbcEngineService.h"
      #include <axis2_msg.h>

         
                                                      
         

      axis2_stub_t*
      axis2_stub_create_JdbcEngineService(const axutil_env_t *env,
                                      axis2_char_t *client_home,
                                      axis2_char_t *endpoint_uri)
      {
         axis2_stub_t *stub = NULL;
         axis2_endpoint_ref_t *endpoint_ref = NULL;
         AXIS2_FUNC_PARAM_CHECK (client_home, env, NULL)

         if (NULL == endpoint_uri)
         {
            endpoint_uri = axis2_stub_get_endpoint_uri_of_JdbcEngineService(env);
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


         axis2_stub_populate_services_for_JdbcEngineService(stub, env);
         return stub;
      }


      void
      axis2_stub_populate_services_for_JdbcEngineService(axis2_stub_t *stub, const axutil_env_t *env)
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

         svc_qname = axutil_qname_create(env,"JdbcEngineService" ,NULL, NULL);
         axis2_svc_set_qname (svc, env, svc_qname);

                                     

         
           op_qname = axutil_qname_create(env,
                                         "cachedDbLookup" ,
                                         "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
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
                                         "writeTarget" ,
                                         "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
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
                                         "dbExecute" ,
                                         "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
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
                                         "dbLookup" ,
                                         "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
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
                                         "closeSession" ,
                                         "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
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
                                         "readSource" ,
                                         "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
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
                                         "rollbackTransactions" ,
                                         "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
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
                                         "startNewSession" ,
                                         "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
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
                                         "commitTransactions" ,
                                         "http://jdbc.webservice.backend.server.integration.etlforge.invalid",
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
      axis2_stub_get_endpoint_uri_of_JdbcEngineService( const axutil_env_t *env )
      {
        axis2_char_t *endpoint_uri = NULL;
                                       
        
              endpoint_uri = "http://localhost/axis/konga_jdbc_engine";
            
        return endpoint_uri;
      }


  
            
                                           
                                                                                                              
           
                                  
           
                                                 
            
         adb_cachedDbLookupResponse_t* 
         axis2_stub_op_JdbcEngineService_cachedDbLookup( axis2_stub_t *stub, const axutil_env_t *env,
                                              adb_cachedDbLookup_t* _cachedDbLookup)
         {
            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;
            axiom_node_t *ret_node = NULL;

            const axis2_char_t *soap_action = NULL;
            axutil_qname_t *op_qname =  NULL;
            axiom_node_t *payload = NULL;
            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            adb_cachedDbLookupResponse_t* ret_val = NULL;
            
                       payload = adb_cachedDbLookup_serialize(_cachedDbLookup, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   
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
              soap_action = "cachedDbLookup";
              
              soap_act = axutil_string_create(env, "cachedDbLookup");
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
                    ret_val = adb_cachedDbLookupResponse_create(env);

                    if(adb_cachedDbLookupResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                    {
                        if(ret_val != NULL)
                        {
                            adb_cachedDbLookupResponse_free(ret_val, env);
                        }

                        AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the cachedDbLookupResponse_deserialize: "
                                                                "This should be due to an invalid XML");
                        return NULL;
                    }
                    return ret_val;
                
        }
        
            
                                           
                                                                                                           
           
                               
           
                                              
            
         adb_writeTargetResponse_t* 
         axis2_stub_op_JdbcEngineService_writeTarget( axis2_stub_t *stub, const axutil_env_t *env,
                                              adb_writeTarget_t* _writeTarget)
         {
            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;
            axiom_node_t *ret_node = NULL;

            const axis2_char_t *soap_action = NULL;
            axutil_qname_t *op_qname =  NULL;
            axiom_node_t *payload = NULL;
            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            adb_writeTargetResponse_t* ret_val = NULL;
            
                       payload = adb_writeTarget_serialize(_writeTarget, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   
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
              soap_action = "writeTarget";
              
              soap_act = axutil_string_create(env, "writeTarget");
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
                    ret_val = adb_writeTargetResponse_create(env);

                    if(adb_writeTargetResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                    {
                        if(ret_val != NULL)
                        {
                            adb_writeTargetResponse_free(ret_val, env);
                        }

                        AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the writeTargetResponse_deserialize: "
                                                                "This should be due to an invalid XML");
                        return NULL;
                    }
                    return ret_val;
                
        }
        
            
                                           
                                                                                                         
           
                             
           
                                            
            
         adb_dbExecuteResponse_t* 
         axis2_stub_op_JdbcEngineService_dbExecute( axis2_stub_t *stub, const axutil_env_t *env,
                                              adb_dbExecute_t* _dbExecute)
         {
            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;
            axiom_node_t *ret_node = NULL;

            const axis2_char_t *soap_action = NULL;
            axutil_qname_t *op_qname =  NULL;
            axiom_node_t *payload = NULL;
            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            adb_dbExecuteResponse_t* ret_val = NULL;
            
                       payload = adb_dbExecute_serialize(_dbExecute, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   
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
              soap_action = "dbExecute";
              
              soap_act = axutil_string_create(env, "dbExecute");
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
                    ret_val = adb_dbExecuteResponse_create(env);

                    if(adb_dbExecuteResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                    {
                        if(ret_val != NULL)
                        {
                            adb_dbExecuteResponse_free(ret_val, env);
                        }

                        AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the dbExecuteResponse_deserialize: "
                                                                "This should be due to an invalid XML");
                        return NULL;
                    }
                    return ret_val;
                
        }
        
            
                                           
                                                                                                        
           
                            
           
                                           
            
         adb_dbLookupResponse_t* 
         axis2_stub_op_JdbcEngineService_dbLookup( axis2_stub_t *stub, const axutil_env_t *env,
                                              adb_dbLookup_t* _dbLookup)
         {
            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;
            axiom_node_t *ret_node = NULL;

            const axis2_char_t *soap_action = NULL;
            axutil_qname_t *op_qname =  NULL;
            axiom_node_t *payload = NULL;
            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            adb_dbLookupResponse_t* ret_val = NULL;
            
                       payload = adb_dbLookup_serialize(_dbLookup, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   
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
              soap_action = "dbLookup";
              
              soap_act = axutil_string_create(env, "dbLookup");
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
                    ret_val = adb_dbLookupResponse_create(env);

                    if(adb_dbLookupResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                    {
                        if(ret_val != NULL)
                        {
                            adb_dbLookupResponse_free(ret_val, env);
                        }

                        AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the dbLookupResponse_deserialize: "
                                                                "This should be due to an invalid XML");
                        return NULL;
                    }
                    return ret_val;
                
        }
        
            
                                           
                                                                                                            
           
                                
           
                                               
            
         adb_closeSessionResponse_t* 
         axis2_stub_op_JdbcEngineService_closeSession( axis2_stub_t *stub, const axutil_env_t *env,
                                              adb_closeSession_t* _closeSession)
         {
            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;
            axiom_node_t *ret_node = NULL;

            const axis2_char_t *soap_action = NULL;
            axutil_qname_t *op_qname =  NULL;
            axiom_node_t *payload = NULL;
            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            adb_closeSessionResponse_t* ret_val = NULL;
            
                       payload = adb_closeSession_serialize(_closeSession, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   
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
              soap_action = "closeSession";
              
              soap_act = axutil_string_create(env, "closeSession");
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
                    ret_val = adb_closeSessionResponse_create(env);

                    if(adb_closeSessionResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                    {
                        if(ret_val != NULL)
                        {
                            adb_closeSessionResponse_free(ret_val, env);
                        }

                        AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the closeSessionResponse_deserialize: "
                                                                "This should be due to an invalid XML");
                        return NULL;
                    }
                    return ret_val;
                
        }
        
            
                                           
                                                                                                          
           
                              
           
                                             
            
         adb_readSourceResponse_t* 
         axis2_stub_op_JdbcEngineService_readSource( axis2_stub_t *stub, const axutil_env_t *env,
                                              adb_readSource_t* _readSource)
         {
            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;
            axiom_node_t *ret_node = NULL;

            const axis2_char_t *soap_action = NULL;
            axutil_qname_t *op_qname =  NULL;
            axiom_node_t *payload = NULL;
            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            adb_readSourceResponse_t* ret_val = NULL;
            
                       payload = adb_readSource_serialize(_readSource, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   
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
              soap_action = "readSource";
              
              soap_act = axutil_string_create(env, "readSource");
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
                    ret_val = adb_readSourceResponse_create(env);

                    if(adb_readSourceResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                    {
                        if(ret_val != NULL)
                        {
                            adb_readSourceResponse_free(ret_val, env);
                        }

                        AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the readSourceResponse_deserialize: "
                                                                "This should be due to an invalid XML");
                        return NULL;
                    }
                    return ret_val;
                
        }
        
            
                                           
                                                                                                                    
           
                                        
           
                                                       
            
         adb_rollbackTransactionsResponse_t* 
         axis2_stub_op_JdbcEngineService_rollbackTransactions( axis2_stub_t *stub, const axutil_env_t *env,
                                              adb_rollbackTransactions_t* _rollbackTransactions)
         {
            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;
            axiom_node_t *ret_node = NULL;

            const axis2_char_t *soap_action = NULL;
            axutil_qname_t *op_qname =  NULL;
            axiom_node_t *payload = NULL;
            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            adb_rollbackTransactionsResponse_t* ret_val = NULL;
            
                       payload = adb_rollbackTransactions_serialize(_rollbackTransactions, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   
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
              soap_action = "rollbackTransactions";
              
              soap_act = axutil_string_create(env, "rollbackTransactions");
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
                    ret_val = adb_rollbackTransactionsResponse_create(env);

                    if(adb_rollbackTransactionsResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                    {
                        if(ret_val != NULL)
                        {
                            adb_rollbackTransactionsResponse_free(ret_val, env);
                        }

                        AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the rollbackTransactionsResponse_deserialize: "
                                                                "This should be due to an invalid XML");
                        return NULL;
                    }
                    return ret_val;
                
        }
        
            
                                           
                                                                                                               
           
                                   
           
                                                  
            
         adb_startNewSessionResponse_t* 
         axis2_stub_op_JdbcEngineService_startNewSession( axis2_stub_t *stub, const axutil_env_t *env,
                                              adb_startNewSession_t* _startNewSession)
         {
            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;
            axiom_node_t *ret_node = NULL;

            const axis2_char_t *soap_action = NULL;
            axutil_qname_t *op_qname =  NULL;
            axiom_node_t *payload = NULL;
            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            adb_startNewSessionResponse_t* ret_val = NULL;
            
                       payload = adb_startNewSession_serialize(_startNewSession, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   
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
              soap_action = "startNewSession";
              
              soap_act = axutil_string_create(env, "startNewSession");
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
                    ret_val = adb_startNewSessionResponse_create(env);

                    if(adb_startNewSessionResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                    {
                        if(ret_val != NULL)
                        {
                            adb_startNewSessionResponse_free(ret_val, env);
                        }

                        AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the startNewSessionResponse_deserialize: "
                                                                "This should be due to an invalid XML");
                        return NULL;
                    }
                    return ret_val;
                
        }
        
            
                                           
                                                                                                                  
           
                                      
           
                                                     
            
         adb_commitTransactionsResponse_t* 
         axis2_stub_op_JdbcEngineService_commitTransactions( axis2_stub_t *stub, const axutil_env_t *env,
                                              adb_commitTransactions_t* _commitTransactions)
         {
            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;
            axiom_node_t *ret_node = NULL;

            const axis2_char_t *soap_action = NULL;
            axutil_qname_t *op_qname =  NULL;
            axiom_node_t *payload = NULL;
            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            adb_commitTransactionsResponse_t* ret_val = NULL;
            
                       payload = adb_commitTransactions_serialize(_commitTransactions, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   
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
              soap_action = "commitTransactions";
              
              soap_act = axutil_string_create(env, "commitTransactions");
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
                    ret_val = adb_commitTransactionsResponse_create(env);

                    if(adb_commitTransactionsResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                    {
                        if(ret_val != NULL)
                        {
                            adb_commitTransactionsResponse_free(ret_val, env);
                        }

                        AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the commitTransactionsResponse_deserialize: "
                                                                "This should be due to an invalid XML");
                        return NULL;
                    }
                    return ret_val;
                
        }
        

        struct axis2_stub_JdbcEngineService_cachedDbLookup_callback_data
        {   
            void *data;
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_cachedDbLookupResponse_t* _cachedDbLookupResponse, void *data);
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data);
        };

        static axis2_status_t AXIS2_CALL axis2_stub_on_error_JdbcEngineService_cachedDbLookup(axis2_callback_t *callback, const axutil_env_t *env, int exception)
        {
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int, void *data);
            struct axis2_stub_JdbcEngineService_cachedDbLookup_callback_data* callback_data = NULL;
            void *user_data = NULL;

            axis2_status_t status;
        
            callback_data = (struct axis2_stub_JdbcEngineService_cachedDbLookup_callback_data*)axis2_callback_get_data(callback);
        
            user_data = callback_data->data;
            on_error = callback_data->on_error;
        
            status = on_error(env, exception, user_data);

            if(callback_data)
            {
                AXIS2_FREE(env->allocator, callback_data);
            }
            return status;
        } 

        axis2_status_t AXIS2_CALL axis2_stub_on_complete_JdbcEngineService_cachedDbLookup(axis2_callback_t *callback, const axutil_env_t *env)
        {
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_cachedDbLookupResponse_t* _cachedDbLookupResponse, void *data);
            struct axis2_stub_JdbcEngineService_cachedDbLookup_callback_data* callback_data = NULL;
            void *user_data = NULL;
            axis2_status_t status = AXIS2_SUCCESS;
 
            adb_cachedDbLookupResponse_t* ret_val = NULL;
            

            axiom_node_t *ret_node = NULL;
            axiom_soap_envelope_t *soap_envelope = NULL;

            

            callback_data = (struct axis2_stub_JdbcEngineService_cachedDbLookup_callback_data*)axis2_callback_get_data(callback);

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
                        ret_val = adb_cachedDbLookupResponse_create(env);
     
                        if(adb_cachedDbLookupResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                        {
                            AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the LendResponse_deserialize: "
                                                                    "This should be due to an invalid XML");
                            adb_cachedDbLookupResponse_free(ret_val, env);
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

           
                                                                        
                                                                                                              
          
           
                                  
                                                             
                                                       
            
         void axis2_stub_start_op_JdbcEngineService_cachedDbLookup( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_cachedDbLookup_t* _cachedDbLookup,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_cachedDbLookupResponse_t* _cachedDbLookupResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) )
         {

            axis2_callback_t *callback = NULL;

            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;

            const axis2_char_t *soap_action = NULL;
            axiom_node_t *payload = NULL;

            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            
            
            struct axis2_stub_JdbcEngineService_cachedDbLookup_callback_data *callback_data;

            callback_data = (struct axis2_stub_JdbcEngineService_cachedDbLookup_callback_data*) AXIS2_MALLOC(env->allocator, 
                                    sizeof(struct axis2_stub_JdbcEngineService_cachedDbLookup_callback_data));
            if(NULL == callback_data)
            {
                AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "Can not allocate memeory for the callback data structures");
                return;
            }
            
            
                       payload = adb_cachedDbLookup_serialize(_cachedDbLookup, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   



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
              soap_action = "cachedDbLookup";
              
              soap_act = axutil_string_create(env, "cachedDbLookup");
              axis2_options_set_soap_action(options, env, soap_act);
              
              axis2_options_set_action( options, env, soap_action);
            }
            
            axis2_options_set_soap_version(options, env, AXIOM_SOAP11);
             

            callback = axis2_callback_create(env);
                                                                             
            axis2_callback_set_on_complete(callback, axis2_stub_on_complete_JdbcEngineService_cachedDbLookup);
                                                                          
            axis2_callback_set_on_error(callback, axis2_stub_on_error_JdbcEngineService_cachedDbLookup);

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

         

        struct axis2_stub_JdbcEngineService_writeTarget_callback_data
        {   
            void *data;
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_writeTargetResponse_t* _writeTargetResponse, void *data);
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data);
        };

        static axis2_status_t AXIS2_CALL axis2_stub_on_error_JdbcEngineService_writeTarget(axis2_callback_t *callback, const axutil_env_t *env, int exception)
        {
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int, void *data);
            struct axis2_stub_JdbcEngineService_writeTarget_callback_data* callback_data = NULL;
            void *user_data = NULL;

            axis2_status_t status;
        
            callback_data = (struct axis2_stub_JdbcEngineService_writeTarget_callback_data*)axis2_callback_get_data(callback);
        
            user_data = callback_data->data;
            on_error = callback_data->on_error;
        
            status = on_error(env, exception, user_data);

            if(callback_data)
            {
                AXIS2_FREE(env->allocator, callback_data);
            }
            return status;
        } 

        axis2_status_t AXIS2_CALL axis2_stub_on_complete_JdbcEngineService_writeTarget(axis2_callback_t *callback, const axutil_env_t *env)
        {
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_writeTargetResponse_t* _writeTargetResponse, void *data);
            struct axis2_stub_JdbcEngineService_writeTarget_callback_data* callback_data = NULL;
            void *user_data = NULL;
            axis2_status_t status = AXIS2_SUCCESS;
 
            adb_writeTargetResponse_t* ret_val = NULL;
            

            axiom_node_t *ret_node = NULL;
            axiom_soap_envelope_t *soap_envelope = NULL;

            

            callback_data = (struct axis2_stub_JdbcEngineService_writeTarget_callback_data*)axis2_callback_get_data(callback);

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
                        ret_val = adb_writeTargetResponse_create(env);
     
                        if(adb_writeTargetResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                        {
                            AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the LendResponse_deserialize: "
                                                                    "This should be due to an invalid XML");
                            adb_writeTargetResponse_free(ret_val, env);
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

           
                                                                        
                                                                                                           
          
           
                               
                                                             
                                                       
            
         void axis2_stub_start_op_JdbcEngineService_writeTarget( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_writeTarget_t* _writeTarget,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_writeTargetResponse_t* _writeTargetResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) )
         {

            axis2_callback_t *callback = NULL;

            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;

            const axis2_char_t *soap_action = NULL;
            axiom_node_t *payload = NULL;

            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            
            
            struct axis2_stub_JdbcEngineService_writeTarget_callback_data *callback_data;

            callback_data = (struct axis2_stub_JdbcEngineService_writeTarget_callback_data*) AXIS2_MALLOC(env->allocator, 
                                    sizeof(struct axis2_stub_JdbcEngineService_writeTarget_callback_data));
            if(NULL == callback_data)
            {
                AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "Can not allocate memeory for the callback data structures");
                return;
            }
            
            
                       payload = adb_writeTarget_serialize(_writeTarget, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   



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
              soap_action = "writeTarget";
              
              soap_act = axutil_string_create(env, "writeTarget");
              axis2_options_set_soap_action(options, env, soap_act);
              
              axis2_options_set_action( options, env, soap_action);
            }
            
            axis2_options_set_soap_version(options, env, AXIOM_SOAP11);
             

            callback = axis2_callback_create(env);
                                                                             
            axis2_callback_set_on_complete(callback, axis2_stub_on_complete_JdbcEngineService_writeTarget);
                                                                          
            axis2_callback_set_on_error(callback, axis2_stub_on_error_JdbcEngineService_writeTarget);

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

         

        struct axis2_stub_JdbcEngineService_dbExecute_callback_data
        {   
            void *data;
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_dbExecuteResponse_t* _dbExecuteResponse, void *data);
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data);
        };

        static axis2_status_t AXIS2_CALL axis2_stub_on_error_JdbcEngineService_dbExecute(axis2_callback_t *callback, const axutil_env_t *env, int exception)
        {
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int, void *data);
            struct axis2_stub_JdbcEngineService_dbExecute_callback_data* callback_data = NULL;
            void *user_data = NULL;

            axis2_status_t status;
        
            callback_data = (struct axis2_stub_JdbcEngineService_dbExecute_callback_data*)axis2_callback_get_data(callback);
        
            user_data = callback_data->data;
            on_error = callback_data->on_error;
        
            status = on_error(env, exception, user_data);

            if(callback_data)
            {
                AXIS2_FREE(env->allocator, callback_data);
            }
            return status;
        } 

        axis2_status_t AXIS2_CALL axis2_stub_on_complete_JdbcEngineService_dbExecute(axis2_callback_t *callback, const axutil_env_t *env)
        {
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_dbExecuteResponse_t* _dbExecuteResponse, void *data);
            struct axis2_stub_JdbcEngineService_dbExecute_callback_data* callback_data = NULL;
            void *user_data = NULL;
            axis2_status_t status = AXIS2_SUCCESS;
 
            adb_dbExecuteResponse_t* ret_val = NULL;
            

            axiom_node_t *ret_node = NULL;
            axiom_soap_envelope_t *soap_envelope = NULL;

            

            callback_data = (struct axis2_stub_JdbcEngineService_dbExecute_callback_data*)axis2_callback_get_data(callback);

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
                        ret_val = adb_dbExecuteResponse_create(env);
     
                        if(adb_dbExecuteResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                        {
                            AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the LendResponse_deserialize: "
                                                                    "This should be due to an invalid XML");
                            adb_dbExecuteResponse_free(ret_val, env);
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

           
                                                                        
                                                                                                         
          
           
                             
                                                             
                                                       
            
         void axis2_stub_start_op_JdbcEngineService_dbExecute( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_dbExecute_t* _dbExecute,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_dbExecuteResponse_t* _dbExecuteResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) )
         {

            axis2_callback_t *callback = NULL;

            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;

            const axis2_char_t *soap_action = NULL;
            axiom_node_t *payload = NULL;

            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            
            
            struct axis2_stub_JdbcEngineService_dbExecute_callback_data *callback_data;

            callback_data = (struct axis2_stub_JdbcEngineService_dbExecute_callback_data*) AXIS2_MALLOC(env->allocator, 
                                    sizeof(struct axis2_stub_JdbcEngineService_dbExecute_callback_data));
            if(NULL == callback_data)
            {
                AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "Can not allocate memeory for the callback data structures");
                return;
            }
            
            
                       payload = adb_dbExecute_serialize(_dbExecute, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   



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
              soap_action = "dbExecute";
              
              soap_act = axutil_string_create(env, "dbExecute");
              axis2_options_set_soap_action(options, env, soap_act);
              
              axis2_options_set_action( options, env, soap_action);
            }
            
            axis2_options_set_soap_version(options, env, AXIOM_SOAP11);
             

            callback = axis2_callback_create(env);
                                                                             
            axis2_callback_set_on_complete(callback, axis2_stub_on_complete_JdbcEngineService_dbExecute);
                                                                          
            axis2_callback_set_on_error(callback, axis2_stub_on_error_JdbcEngineService_dbExecute);

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

         

        struct axis2_stub_JdbcEngineService_dbLookup_callback_data
        {   
            void *data;
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_dbLookupResponse_t* _dbLookupResponse, void *data);
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data);
        };

        static axis2_status_t AXIS2_CALL axis2_stub_on_error_JdbcEngineService_dbLookup(axis2_callback_t *callback, const axutil_env_t *env, int exception)
        {
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int, void *data);
            struct axis2_stub_JdbcEngineService_dbLookup_callback_data* callback_data = NULL;
            void *user_data = NULL;

            axis2_status_t status;
        
            callback_data = (struct axis2_stub_JdbcEngineService_dbLookup_callback_data*)axis2_callback_get_data(callback);
        
            user_data = callback_data->data;
            on_error = callback_data->on_error;
        
            status = on_error(env, exception, user_data);

            if(callback_data)
            {
                AXIS2_FREE(env->allocator, callback_data);
            }
            return status;
        } 

        axis2_status_t AXIS2_CALL axis2_stub_on_complete_JdbcEngineService_dbLookup(axis2_callback_t *callback, const axutil_env_t *env)
        {
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_dbLookupResponse_t* _dbLookupResponse, void *data);
            struct axis2_stub_JdbcEngineService_dbLookup_callback_data* callback_data = NULL;
            void *user_data = NULL;
            axis2_status_t status = AXIS2_SUCCESS;
 
            adb_dbLookupResponse_t* ret_val = NULL;
            

            axiom_node_t *ret_node = NULL;
            axiom_soap_envelope_t *soap_envelope = NULL;

            

            callback_data = (struct axis2_stub_JdbcEngineService_dbLookup_callback_data*)axis2_callback_get_data(callback);

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
                        ret_val = adb_dbLookupResponse_create(env);
     
                        if(adb_dbLookupResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                        {
                            AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the LendResponse_deserialize: "
                                                                    "This should be due to an invalid XML");
                            adb_dbLookupResponse_free(ret_val, env);
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

           
                                                                        
                                                                                                        
          
           
                            
                                                             
                                                       
            
         void axis2_stub_start_op_JdbcEngineService_dbLookup( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_dbLookup_t* _dbLookup,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_dbLookupResponse_t* _dbLookupResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) )
         {

            axis2_callback_t *callback = NULL;

            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;

            const axis2_char_t *soap_action = NULL;
            axiom_node_t *payload = NULL;

            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            
            
            struct axis2_stub_JdbcEngineService_dbLookup_callback_data *callback_data;

            callback_data = (struct axis2_stub_JdbcEngineService_dbLookup_callback_data*) AXIS2_MALLOC(env->allocator, 
                                    sizeof(struct axis2_stub_JdbcEngineService_dbLookup_callback_data));
            if(NULL == callback_data)
            {
                AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "Can not allocate memeory for the callback data structures");
                return;
            }
            
            
                       payload = adb_dbLookup_serialize(_dbLookup, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   



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
              soap_action = "dbLookup";
              
              soap_act = axutil_string_create(env, "dbLookup");
              axis2_options_set_soap_action(options, env, soap_act);
              
              axis2_options_set_action( options, env, soap_action);
            }
            
            axis2_options_set_soap_version(options, env, AXIOM_SOAP11);
             

            callback = axis2_callback_create(env);
                                                                             
            axis2_callback_set_on_complete(callback, axis2_stub_on_complete_JdbcEngineService_dbLookup);
                                                                          
            axis2_callback_set_on_error(callback, axis2_stub_on_error_JdbcEngineService_dbLookup);

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

         

        struct axis2_stub_JdbcEngineService_closeSession_callback_data
        {   
            void *data;
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_closeSessionResponse_t* _closeSessionResponse, void *data);
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data);
        };

        static axis2_status_t AXIS2_CALL axis2_stub_on_error_JdbcEngineService_closeSession(axis2_callback_t *callback, const axutil_env_t *env, int exception)
        {
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int, void *data);
            struct axis2_stub_JdbcEngineService_closeSession_callback_data* callback_data = NULL;
            void *user_data = NULL;

            axis2_status_t status;
        
            callback_data = (struct axis2_stub_JdbcEngineService_closeSession_callback_data*)axis2_callback_get_data(callback);
        
            user_data = callback_data->data;
            on_error = callback_data->on_error;
        
            status = on_error(env, exception, user_data);

            if(callback_data)
            {
                AXIS2_FREE(env->allocator, callback_data);
            }
            return status;
        } 

        axis2_status_t AXIS2_CALL axis2_stub_on_complete_JdbcEngineService_closeSession(axis2_callback_t *callback, const axutil_env_t *env)
        {
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_closeSessionResponse_t* _closeSessionResponse, void *data);
            struct axis2_stub_JdbcEngineService_closeSession_callback_data* callback_data = NULL;
            void *user_data = NULL;
            axis2_status_t status = AXIS2_SUCCESS;
 
            adb_closeSessionResponse_t* ret_val = NULL;
            

            axiom_node_t *ret_node = NULL;
            axiom_soap_envelope_t *soap_envelope = NULL;

            

            callback_data = (struct axis2_stub_JdbcEngineService_closeSession_callback_data*)axis2_callback_get_data(callback);

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
                        ret_val = adb_closeSessionResponse_create(env);
     
                        if(adb_closeSessionResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                        {
                            AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the LendResponse_deserialize: "
                                                                    "This should be due to an invalid XML");
                            adb_closeSessionResponse_free(ret_val, env);
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

           
                                                                        
                                                                                                            
          
           
                                
                                                             
                                                       
            
         void axis2_stub_start_op_JdbcEngineService_closeSession( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_closeSession_t* _closeSession,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_closeSessionResponse_t* _closeSessionResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) )
         {

            axis2_callback_t *callback = NULL;

            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;

            const axis2_char_t *soap_action = NULL;
            axiom_node_t *payload = NULL;

            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            
            
            struct axis2_stub_JdbcEngineService_closeSession_callback_data *callback_data;

            callback_data = (struct axis2_stub_JdbcEngineService_closeSession_callback_data*) AXIS2_MALLOC(env->allocator, 
                                    sizeof(struct axis2_stub_JdbcEngineService_closeSession_callback_data));
            if(NULL == callback_data)
            {
                AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "Can not allocate memeory for the callback data structures");
                return;
            }
            
            
                       payload = adb_closeSession_serialize(_closeSession, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   



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
              soap_action = "closeSession";
              
              soap_act = axutil_string_create(env, "closeSession");
              axis2_options_set_soap_action(options, env, soap_act);
              
              axis2_options_set_action( options, env, soap_action);
            }
            
            axis2_options_set_soap_version(options, env, AXIOM_SOAP11);
             

            callback = axis2_callback_create(env);
                                                                             
            axis2_callback_set_on_complete(callback, axis2_stub_on_complete_JdbcEngineService_closeSession);
                                                                          
            axis2_callback_set_on_error(callback, axis2_stub_on_error_JdbcEngineService_closeSession);

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

         

        struct axis2_stub_JdbcEngineService_readSource_callback_data
        {   
            void *data;
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_readSourceResponse_t* _readSourceResponse, void *data);
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data);
        };

        static axis2_status_t AXIS2_CALL axis2_stub_on_error_JdbcEngineService_readSource(axis2_callback_t *callback, const axutil_env_t *env, int exception)
        {
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int, void *data);
            struct axis2_stub_JdbcEngineService_readSource_callback_data* callback_data = NULL;
            void *user_data = NULL;

            axis2_status_t status;
        
            callback_data = (struct axis2_stub_JdbcEngineService_readSource_callback_data*)axis2_callback_get_data(callback);
        
            user_data = callback_data->data;
            on_error = callback_data->on_error;
        
            status = on_error(env, exception, user_data);

            if(callback_data)
            {
                AXIS2_FREE(env->allocator, callback_data);
            }
            return status;
        } 

        axis2_status_t AXIS2_CALL axis2_stub_on_complete_JdbcEngineService_readSource(axis2_callback_t *callback, const axutil_env_t *env)
        {
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_readSourceResponse_t* _readSourceResponse, void *data);
            struct axis2_stub_JdbcEngineService_readSource_callback_data* callback_data = NULL;
            void *user_data = NULL;
            axis2_status_t status = AXIS2_SUCCESS;
 
            adb_readSourceResponse_t* ret_val = NULL;
            

            axiom_node_t *ret_node = NULL;
            axiom_soap_envelope_t *soap_envelope = NULL;

            

            callback_data = (struct axis2_stub_JdbcEngineService_readSource_callback_data*)axis2_callback_get_data(callback);

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
                        ret_val = adb_readSourceResponse_create(env);
     
                        if(adb_readSourceResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                        {
                            AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the LendResponse_deserialize: "
                                                                    "This should be due to an invalid XML");
                            adb_readSourceResponse_free(ret_val, env);
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

           
                                                                        
                                                                                                          
          
           
                              
                                                             
                                                       
            
         void axis2_stub_start_op_JdbcEngineService_readSource( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_readSource_t* _readSource,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_readSourceResponse_t* _readSourceResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) )
         {

            axis2_callback_t *callback = NULL;

            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;

            const axis2_char_t *soap_action = NULL;
            axiom_node_t *payload = NULL;

            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            
            
            struct axis2_stub_JdbcEngineService_readSource_callback_data *callback_data;

            callback_data = (struct axis2_stub_JdbcEngineService_readSource_callback_data*) AXIS2_MALLOC(env->allocator, 
                                    sizeof(struct axis2_stub_JdbcEngineService_readSource_callback_data));
            if(NULL == callback_data)
            {
                AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "Can not allocate memeory for the callback data structures");
                return;
            }
            
            
                       payload = adb_readSource_serialize(_readSource, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   



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
              soap_action = "readSource";
              
              soap_act = axutil_string_create(env, "readSource");
              axis2_options_set_soap_action(options, env, soap_act);
              
              axis2_options_set_action( options, env, soap_action);
            }
            
            axis2_options_set_soap_version(options, env, AXIOM_SOAP11);
             

            callback = axis2_callback_create(env);
                                                                             
            axis2_callback_set_on_complete(callback, axis2_stub_on_complete_JdbcEngineService_readSource);
                                                                          
            axis2_callback_set_on_error(callback, axis2_stub_on_error_JdbcEngineService_readSource);

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

         

        struct axis2_stub_JdbcEngineService_rollbackTransactions_callback_data
        {   
            void *data;
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_rollbackTransactionsResponse_t* _rollbackTransactionsResponse, void *data);
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data);
        };

        static axis2_status_t AXIS2_CALL axis2_stub_on_error_JdbcEngineService_rollbackTransactions(axis2_callback_t *callback, const axutil_env_t *env, int exception)
        {
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int, void *data);
            struct axis2_stub_JdbcEngineService_rollbackTransactions_callback_data* callback_data = NULL;
            void *user_data = NULL;

            axis2_status_t status;
        
            callback_data = (struct axis2_stub_JdbcEngineService_rollbackTransactions_callback_data*)axis2_callback_get_data(callback);
        
            user_data = callback_data->data;
            on_error = callback_data->on_error;
        
            status = on_error(env, exception, user_data);

            if(callback_data)
            {
                AXIS2_FREE(env->allocator, callback_data);
            }
            return status;
        } 

        axis2_status_t AXIS2_CALL axis2_stub_on_complete_JdbcEngineService_rollbackTransactions(axis2_callback_t *callback, const axutil_env_t *env)
        {
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_rollbackTransactionsResponse_t* _rollbackTransactionsResponse, void *data);
            struct axis2_stub_JdbcEngineService_rollbackTransactions_callback_data* callback_data = NULL;
            void *user_data = NULL;
            axis2_status_t status = AXIS2_SUCCESS;
 
            adb_rollbackTransactionsResponse_t* ret_val = NULL;
            

            axiom_node_t *ret_node = NULL;
            axiom_soap_envelope_t *soap_envelope = NULL;

            

            callback_data = (struct axis2_stub_JdbcEngineService_rollbackTransactions_callback_data*)axis2_callback_get_data(callback);

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
                        ret_val = adb_rollbackTransactionsResponse_create(env);
     
                        if(adb_rollbackTransactionsResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                        {
                            AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the LendResponse_deserialize: "
                                                                    "This should be due to an invalid XML");
                            adb_rollbackTransactionsResponse_free(ret_val, env);
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

           
                                                                        
                                                                                                                    
          
           
                                        
                                                             
                                                       
            
         void axis2_stub_start_op_JdbcEngineService_rollbackTransactions( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_rollbackTransactions_t* _rollbackTransactions,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_rollbackTransactionsResponse_t* _rollbackTransactionsResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) )
         {

            axis2_callback_t *callback = NULL;

            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;

            const axis2_char_t *soap_action = NULL;
            axiom_node_t *payload = NULL;

            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            
            
            struct axis2_stub_JdbcEngineService_rollbackTransactions_callback_data *callback_data;

            callback_data = (struct axis2_stub_JdbcEngineService_rollbackTransactions_callback_data*) AXIS2_MALLOC(env->allocator, 
                                    sizeof(struct axis2_stub_JdbcEngineService_rollbackTransactions_callback_data));
            if(NULL == callback_data)
            {
                AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "Can not allocate memeory for the callback data structures");
                return;
            }
            
            
                       payload = adb_rollbackTransactions_serialize(_rollbackTransactions, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   



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
              soap_action = "rollbackTransactions";
              
              soap_act = axutil_string_create(env, "rollbackTransactions");
              axis2_options_set_soap_action(options, env, soap_act);
              
              axis2_options_set_action( options, env, soap_action);
            }
            
            axis2_options_set_soap_version(options, env, AXIOM_SOAP11);
             

            callback = axis2_callback_create(env);
                                                                             
            axis2_callback_set_on_complete(callback, axis2_stub_on_complete_JdbcEngineService_rollbackTransactions);
                                                                          
            axis2_callback_set_on_error(callback, axis2_stub_on_error_JdbcEngineService_rollbackTransactions);

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

         

        struct axis2_stub_JdbcEngineService_startNewSession_callback_data
        {   
            void *data;
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_startNewSessionResponse_t* _startNewSessionResponse, void *data);
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data);
        };

        static axis2_status_t AXIS2_CALL axis2_stub_on_error_JdbcEngineService_startNewSession(axis2_callback_t *callback, const axutil_env_t *env, int exception)
        {
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int, void *data);
            struct axis2_stub_JdbcEngineService_startNewSession_callback_data* callback_data = NULL;
            void *user_data = NULL;

            axis2_status_t status;
        
            callback_data = (struct axis2_stub_JdbcEngineService_startNewSession_callback_data*)axis2_callback_get_data(callback);
        
            user_data = callback_data->data;
            on_error = callback_data->on_error;
        
            status = on_error(env, exception, user_data);

            if(callback_data)
            {
                AXIS2_FREE(env->allocator, callback_data);
            }
            return status;
        } 

        axis2_status_t AXIS2_CALL axis2_stub_on_complete_JdbcEngineService_startNewSession(axis2_callback_t *callback, const axutil_env_t *env)
        {
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_startNewSessionResponse_t* _startNewSessionResponse, void *data);
            struct axis2_stub_JdbcEngineService_startNewSession_callback_data* callback_data = NULL;
            void *user_data = NULL;
            axis2_status_t status = AXIS2_SUCCESS;
 
            adb_startNewSessionResponse_t* ret_val = NULL;
            

            axiom_node_t *ret_node = NULL;
            axiom_soap_envelope_t *soap_envelope = NULL;

            

            callback_data = (struct axis2_stub_JdbcEngineService_startNewSession_callback_data*)axis2_callback_get_data(callback);

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
                        ret_val = adb_startNewSessionResponse_create(env);
     
                        if(adb_startNewSessionResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                        {
                            AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the LendResponse_deserialize: "
                                                                    "This should be due to an invalid XML");
                            adb_startNewSessionResponse_free(ret_val, env);
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

           
                                                                        
                                                                                                               
          
           
                                   
                                                             
                                                       
            
         void axis2_stub_start_op_JdbcEngineService_startNewSession( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_startNewSession_t* _startNewSession,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_startNewSessionResponse_t* _startNewSessionResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) )
         {

            axis2_callback_t *callback = NULL;

            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;

            const axis2_char_t *soap_action = NULL;
            axiom_node_t *payload = NULL;

            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            
            
            struct axis2_stub_JdbcEngineService_startNewSession_callback_data *callback_data;

            callback_data = (struct axis2_stub_JdbcEngineService_startNewSession_callback_data*) AXIS2_MALLOC(env->allocator, 
                                    sizeof(struct axis2_stub_JdbcEngineService_startNewSession_callback_data));
            if(NULL == callback_data)
            {
                AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "Can not allocate memeory for the callback data structures");
                return;
            }
            
            
                       payload = adb_startNewSession_serialize(_startNewSession, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   



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
              soap_action = "startNewSession";
              
              soap_act = axutil_string_create(env, "startNewSession");
              axis2_options_set_soap_action(options, env, soap_act);
              
              axis2_options_set_action( options, env, soap_action);
            }
            
            axis2_options_set_soap_version(options, env, AXIOM_SOAP11);
             

            callback = axis2_callback_create(env);
                                                                             
            axis2_callback_set_on_complete(callback, axis2_stub_on_complete_JdbcEngineService_startNewSession);
                                                                          
            axis2_callback_set_on_error(callback, axis2_stub_on_error_JdbcEngineService_startNewSession);

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

         

        struct axis2_stub_JdbcEngineService_commitTransactions_callback_data
        {   
            void *data;
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_commitTransactionsResponse_t* _commitTransactionsResponse, void *data);
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data);
        };

        static axis2_status_t AXIS2_CALL axis2_stub_on_error_JdbcEngineService_commitTransactions(axis2_callback_t *callback, const axutil_env_t *env, int exception)
        {
            axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int, void *data);
            struct axis2_stub_JdbcEngineService_commitTransactions_callback_data* callback_data = NULL;
            void *user_data = NULL;

            axis2_status_t status;
        
            callback_data = (struct axis2_stub_JdbcEngineService_commitTransactions_callback_data*)axis2_callback_get_data(callback);
        
            user_data = callback_data->data;
            on_error = callback_data->on_error;
        
            status = on_error(env, exception, user_data);

            if(callback_data)
            {
                AXIS2_FREE(env->allocator, callback_data);
            }
            return status;
        } 

        axis2_status_t AXIS2_CALL axis2_stub_on_complete_JdbcEngineService_commitTransactions(axis2_callback_t *callback, const axutil_env_t *env)
        {
            axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_commitTransactionsResponse_t* _commitTransactionsResponse, void *data);
            struct axis2_stub_JdbcEngineService_commitTransactions_callback_data* callback_data = NULL;
            void *user_data = NULL;
            axis2_status_t status = AXIS2_SUCCESS;
 
            adb_commitTransactionsResponse_t* ret_val = NULL;
            

            axiom_node_t *ret_node = NULL;
            axiom_soap_envelope_t *soap_envelope = NULL;

            

            callback_data = (struct axis2_stub_JdbcEngineService_commitTransactions_callback_data*)axis2_callback_get_data(callback);

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
                        ret_val = adb_commitTransactionsResponse_create(env);
     
                        if(adb_commitTransactionsResponse_deserialize(ret_val, env, &ret_node, NULL, AXIS2_FALSE ) == AXIS2_FAILURE)
                        {
                            AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "NULL returnted from the LendResponse_deserialize: "
                                                                    "This should be due to an invalid XML");
                            adb_commitTransactionsResponse_free(ret_val, env);
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

           
                                                                        
                                                                                                                  
          
           
                                      
                                                             
                                                       
            
         void axis2_stub_start_op_JdbcEngineService_commitTransactions( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_commitTransactions_t* _commitTransactions,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_commitTransactionsResponse_t* _commitTransactionsResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) )
         {

            axis2_callback_t *callback = NULL;

            axis2_svc_client_t *svc_client = NULL;
            axis2_options_t *options = NULL;

            const axis2_char_t *soap_action = NULL;
            axiom_node_t *payload = NULL;

            axis2_bool_t is_soap_act_set = AXIS2_TRUE;
            
            axutil_string_t *soap_act = NULL;
            
            
            struct axis2_stub_JdbcEngineService_commitTransactions_callback_data *callback_data;

            callback_data = (struct axis2_stub_JdbcEngineService_commitTransactions_callback_data*) AXIS2_MALLOC(env->allocator, 
                                    sizeof(struct axis2_stub_JdbcEngineService_commitTransactions_callback_data));
            if(NULL == callback_data)
            {
                AXIS2_LOG_ERROR( env->log, AXIS2_LOG_SI, "Can not allocate memeory for the callback data structures");
                return;
            }
            
            
                       payload = adb_commitTransactions_serialize(_commitTransactions, env, NULL, NULL, AXIS2_TRUE, NULL, NULL);
                   



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
              soap_action = "commitTransactions";
              
              soap_act = axutil_string_create(env, "commitTransactions");
              axis2_options_set_soap_action(options, env, soap_act);
              
              axis2_options_set_action( options, env, soap_action);
            }
            
            axis2_options_set_soap_version(options, env, AXIOM_SOAP11);
             

            callback = axis2_callback_create(env);
                                                                             
            axis2_callback_set_on_complete(callback, axis2_stub_on_complete_JdbcEngineService_commitTransactions);
                                                                          
            axis2_callback_set_on_error(callback, axis2_stub_on_error_JdbcEngineService_commitTransactions);

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

            


        
                                                
        
     


        
                                                 
        
     

