

           
                                            
         
                                                                                                                                                                  
                                                                                         
          

        #include <stdio.h>
        #include <axiom.h>
        #include <axutil_utils.h>
        #include <axiom_soap.h>
        #include <axis2_client.h>
        #include <axis2_stub.h>

       
         #include "adb_closeBulkLoadSession.h"
        
         #include "adb_closeBulkLoadSessionResponse.h"
        
         #include "adb_startNewBulkLoadSession.h"
        
         #include "adb_startNewBulkLoadSessionResponse.h"
        
         #include "adb_runBulkLoad.h"
        
         #include "adb_runBulkLoadResponse.h"
        

	#ifdef __cplusplus
	extern "C" {
	#endif

                                                                               
           
                                                  
                                                             
                                              
                                                        
                                                                                                       
                                            
           
        axis2_stub_t*
        axis2_stub_create_BulkLoadEngineService(const axutil_env_t *env,
                                        axis2_char_t *client_home,
                                        axis2_char_t *endpoint_uri);
           
                                                                 
                                                                   
                               
                                              
           
        void axis2_stub_populate_services_for_BulkLoadEngineService( axis2_stub_t *stub, const axutil_env_t *env);
           
                                                               
                                                   
                                              
                                                
           
        axis2_char_t *
        axis2_stub_get_endpoint_uri_of_BulkLoadEngineService(const axutil_env_t *env);

        
               
                                                  
                                                                                                                                  
                                                  
                                                  
              
                                           
              
                                                          
               


            adb_closeBulkLoadSessionResponse_t* 
            axis2_stub_op_BulkLoadEngineService_closeBulkLoadSession( axis2_stub_t *stub, const axutil_env_t *env,
                                                        adb_closeBulkLoadSession_t* _closeBulkLoadSession);
          
               
                                                  
                                                                                                                                     
                                                  
                                                  
              
                                              
              
                                                             
               


            adb_startNewBulkLoadSessionResponse_t* 
            axis2_stub_op_BulkLoadEngineService_startNewBulkLoadSession( axis2_stub_t *stub, const axutil_env_t *env,
                                                        adb_startNewBulkLoadSession_t* _startNewBulkLoadSession);
          
               
                                                  
                                                                                                                         
                                                  
                                                  
              
                                  
              
                                                 
               


            adb_runBulkLoadResponse_t* 
            axis2_stub_op_BulkLoadEngineService_runBulkLoad( axis2_stub_t *stub, const axutil_env_t *env,
                                                        adb_runBulkLoad_t* _runBulkLoad);
          
           
                                                               
                                                                                                                              
                               
                                              
         
                                       
                                                                     
                                                            
                                                      
           

        

        void axis2_stub_start_op_BulkLoadEngineService_closeBulkLoadSession( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_closeBulkLoadSession_t* _closeBulkLoadSession,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_closeBulkLoadSessionResponse_t* _closeBulkLoadSessionResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) );

        
           
                                                               
                                                                                                                                 
                               
                                              
         
                                          
                                                                     
                                                            
                                                      
           

        

        void axis2_stub_start_op_BulkLoadEngineService_startNewBulkLoadSession( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_startNewBulkLoadSession_t* _startNewBulkLoadSession,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_startNewBulkLoadSessionResponse_t* _startNewBulkLoadSessionResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) );

        
           
                                                               
                                                                                                                     
                               
                                              
         
                              
                                                                     
                                                            
                                                      
           

        

        void axis2_stub_start_op_BulkLoadEngineService_runBulkLoad( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_runBulkLoad_t* _runBulkLoad,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_runBulkLoadResponse_t* _runBulkLoadResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) );

          

        
                                                
                                           
        
     

        
                                                 
                                           
        
     

	#ifdef __cplusplus
	}
	#endif
   

