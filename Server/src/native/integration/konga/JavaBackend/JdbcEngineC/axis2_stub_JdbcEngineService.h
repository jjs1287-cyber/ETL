

           
                                        
         
                                                                                                                                                   
                                                                                         
          

        #include <stdio.h>
        #include <axiom.h>
        #include <axutil_utils.h>
        #include <axiom_soap.h>
        #include <axis2_client.h>
        #include <axis2_stub.h>

       
         #include "adb_cachedDbLookup.h"
        
         #include "adb_cachedDbLookupResponse.h"
        
         #include "adb_writeTarget.h"
        
         #include "adb_writeTargetResponse.h"
        
         #include "adb_dbExecute.h"
        
         #include "adb_dbExecuteResponse.h"
        
         #include "adb_dbLookup.h"
        
         #include "adb_dbLookupResponse.h"
        
         #include "adb_closeSession.h"
        
         #include "adb_closeSessionResponse.h"
        
         #include "adb_readSource.h"
        
         #include "adb_readSourceResponse.h"
        
         #include "adb_rollbackTransactions.h"
        
         #include "adb_rollbackTransactionsResponse.h"
        
         #include "adb_startNewSession.h"
        
         #include "adb_startNewSessionResponse.h"
        
         #include "adb_commitTransactions.h"
        
         #include "adb_commitTransactionsResponse.h"
        

	#ifdef __cplusplus
	extern "C" {
	#endif

                                                                               
           
                                              
                                                             
                                              
                                                        
                                                                                                       
                                            
           
        axis2_stub_t*
        axis2_stub_create_JdbcEngineService(const axutil_env_t *env,
                                        axis2_char_t *client_home,
                                        axis2_char_t *endpoint_uri);
           
                                                             
                                                                   
                               
                                              
           
        void axis2_stub_populate_services_for_JdbcEngineService( axis2_stub_t *stub, const axutil_env_t *env);
           
                                                           
                                                   
                                              
                                                
           
        axis2_char_t *
        axis2_stub_get_endpoint_uri_of_JdbcEngineService(const axutil_env_t *env);

        
               
                                                  
                                                                                                                 
                                                  
                                                  
              
                                     
              
                                                    
               


            adb_cachedDbLookupResponse_t* 
            axis2_stub_op_JdbcEngineService_cachedDbLookup( axis2_stub_t *stub, const axutil_env_t *env,
                                                        adb_cachedDbLookup_t* _cachedDbLookup);
          
               
                                                  
                                                                                                              
                                                  
                                                  
              
                                  
              
                                                 
               


            adb_writeTargetResponse_t* 
            axis2_stub_op_JdbcEngineService_writeTarget( axis2_stub_t *stub, const axutil_env_t *env,
                                                        adb_writeTarget_t* _writeTarget);
          
               
                                                  
                                                                                                            
                                                  
                                                  
              
                                
              
                                               
               


            adb_dbExecuteResponse_t* 
            axis2_stub_op_JdbcEngineService_dbExecute( axis2_stub_t *stub, const axutil_env_t *env,
                                                        adb_dbExecute_t* _dbExecute);
          
               
                                                  
                                                                                                           
                                                  
                                                  
              
                               
              
                                              
               


            adb_dbLookupResponse_t* 
            axis2_stub_op_JdbcEngineService_dbLookup( axis2_stub_t *stub, const axutil_env_t *env,
                                                        adb_dbLookup_t* _dbLookup);
          
               
                                                  
                                                                                                               
                                                  
                                                  
              
                                   
              
                                                  
               


            adb_closeSessionResponse_t* 
            axis2_stub_op_JdbcEngineService_closeSession( axis2_stub_t *stub, const axutil_env_t *env,
                                                        adb_closeSession_t* _closeSession);
          
               
                                                  
                                                                                                             
                                                  
                                                  
              
                                 
              
                                                
               


            adb_readSourceResponse_t* 
            axis2_stub_op_JdbcEngineService_readSource( axis2_stub_t *stub, const axutil_env_t *env,
                                                        adb_readSource_t* _readSource);
          
               
                                                  
                                                                                                                       
                                                  
                                                  
              
                                           
              
                                                          
               


            adb_rollbackTransactionsResponse_t* 
            axis2_stub_op_JdbcEngineService_rollbackTransactions( axis2_stub_t *stub, const axutil_env_t *env,
                                                        adb_rollbackTransactions_t* _rollbackTransactions);
          
               
                                                  
                                                                                                                  
                                                  
                                                  
              
                                      
              
                                                     
               


            adb_startNewSessionResponse_t* 
            axis2_stub_op_JdbcEngineService_startNewSession( axis2_stub_t *stub, const axutil_env_t *env,
                                                        adb_startNewSession_t* _startNewSession);
          
               
                                                  
                                                                                                                     
                                                  
                                                  
              
                                         
              
                                                        
               


            adb_commitTransactionsResponse_t* 
            axis2_stub_op_JdbcEngineService_commitTransactions( axis2_stub_t *stub, const axutil_env_t *env,
                                                        adb_commitTransactions_t* _commitTransactions);
          
           
                                                               
                                                                                                             
                               
                                              
         
                                 
                                                                     
                                                            
                                                      
           

        

        void axis2_stub_start_op_JdbcEngineService_cachedDbLookup( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_cachedDbLookup_t* _cachedDbLookup,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_cachedDbLookupResponse_t* _cachedDbLookupResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) );

        
           
                                                               
                                                                                                          
                               
                                              
         
                              
                                                                     
                                                            
                                                      
           

        

        void axis2_stub_start_op_JdbcEngineService_writeTarget( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_writeTarget_t* _writeTarget,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_writeTargetResponse_t* _writeTargetResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) );

        
           
                                                               
                                                                                                        
                               
                                              
         
                            
                                                                     
                                                            
                                                      
           

        

        void axis2_stub_start_op_JdbcEngineService_dbExecute( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_dbExecute_t* _dbExecute,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_dbExecuteResponse_t* _dbExecuteResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) );

        
           
                                                               
                                                                                                       
                               
                                              
         
                           
                                                                     
                                                            
                                                      
           

        

        void axis2_stub_start_op_JdbcEngineService_dbLookup( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_dbLookup_t* _dbLookup,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_dbLookupResponse_t* _dbLookupResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) );

        
           
                                                               
                                                                                                           
                               
                                              
         
                               
                                                                     
                                                            
                                                      
           

        

        void axis2_stub_start_op_JdbcEngineService_closeSession( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_closeSession_t* _closeSession,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_closeSessionResponse_t* _closeSessionResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) );

        
           
                                                               
                                                                                                         
                               
                                              
         
                             
                                                                     
                                                            
                                                      
           

        

        void axis2_stub_start_op_JdbcEngineService_readSource( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_readSource_t* _readSource,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_readSourceResponse_t* _readSourceResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) );

        
           
                                                               
                                                                                                                   
                               
                                              
         
                                       
                                                                     
                                                            
                                                      
           

        

        void axis2_stub_start_op_JdbcEngineService_rollbackTransactions( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_rollbackTransactions_t* _rollbackTransactions,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_rollbackTransactionsResponse_t* _rollbackTransactionsResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) );

        
           
                                                               
                                                                                                              
                               
                                              
         
                                  
                                                                     
                                                            
                                                      
           

        

        void axis2_stub_start_op_JdbcEngineService_startNewSession( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_startNewSession_t* _startNewSession,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_startNewSessionResponse_t* _startNewSessionResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) );

        
           
                                                               
                                                                                                                 
                               
                                              
         
                                     
                                                                     
                                                            
                                                      
           

        

        void axis2_stub_start_op_JdbcEngineService_commitTransactions( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_commitTransactions_t* _commitTransactions,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_commitTransactionsResponse_t* _commitTransactionsResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) );

          

        
                                                
                                           
        
     

        
                                                 
                                           
        
     

	#ifdef __cplusplus
	}
	#endif
   

