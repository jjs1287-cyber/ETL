

           
                                        
         
                                                                                                                                                   
                                                                                         
          

        #include <stdio.h>
        #include <axiom.h>
        #include <axutil_utils.h>
        #include <axiom_soap.h>
        #include <axis2_client.h>
        #include <axis2_stub.h>

       
         #include "adb_jsonToXml.h"
        
         #include "adb_jsonToXmlResponse.h"
        
         #include "adb_xmlToJson.h"
        
         #include "adb_xmlToJsonResponse.h"
        
         #include "adb_startNewJsonSession.h"
        
         #include "adb_startNewJsonSessionResponse.h"
        
         #include "adb_closeJsonSession.h"
        
         #include "adb_closeJsonSessionResponse.h"
        

	#ifdef __cplusplus
	extern "C" {
	#endif

                                                                               
           
                                              
                                                             
                                              
                                                        
                                                                                                       
                                            
           
        axis2_stub_t*
        axis2_stub_create_JsonEngineService(const axutil_env_t *env,
                                        axis2_char_t *client_home,
                                        axis2_char_t *endpoint_uri);
           
                                                             
                                                                   
                               
                                              
           
        void axis2_stub_populate_services_for_JsonEngineService( axis2_stub_t *stub, const axutil_env_t *env);
           
                                                           
                                                   
                                              
                                                
           
        axis2_char_t *
        axis2_stub_get_endpoint_uri_of_JsonEngineService(const axutil_env_t *env);

        
               
                                                  
                                                                                                            
                                                  
                                                  
              
                                
              
                                               
               


            adb_jsonToXmlResponse_t* 
            axis2_stub_op_JsonEngineService_jsonToXml( axis2_stub_t *stub, const axutil_env_t *env,
                                                        adb_jsonToXml_t* _jsonToXml);
          
               
                                                  
                                                                                                            
                                                  
                                                  
              
                                
              
                                               
               


            adb_xmlToJsonResponse_t* 
            axis2_stub_op_JsonEngineService_xmlToJson( axis2_stub_t *stub, const axutil_env_t *env,
                                                        adb_xmlToJson_t* _xmlToJson);
          
               
                                                  
                                                                                                                      
                                                  
                                                  
              
                                          
              
                                                         
               


            adb_startNewJsonSessionResponse_t* 
            axis2_stub_op_JsonEngineService_startNewJsonSession( axis2_stub_t *stub, const axutil_env_t *env,
                                                        adb_startNewJsonSession_t* _startNewJsonSession);
          
               
                                                  
                                                                                                                   
                                                  
                                                  
              
                                       
              
                                                      
               


            adb_closeJsonSessionResponse_t* 
            axis2_stub_op_JsonEngineService_closeJsonSession( axis2_stub_t *stub, const axutil_env_t *env,
                                                        adb_closeJsonSession_t* _closeJsonSession);
          
           
                                                               
                                                                                                        
                               
                                              
         
                            
                                                                     
                                                            
                                                      
           

        

        void axis2_stub_start_op_JsonEngineService_jsonToXml( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_jsonToXml_t* _jsonToXml,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_jsonToXmlResponse_t* _jsonToXmlResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) );

        
           
                                                               
                                                                                                        
                               
                                              
         
                            
                                                                     
                                                            
                                                      
           

        

        void axis2_stub_start_op_JsonEngineService_xmlToJson( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_xmlToJson_t* _xmlToJson,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_xmlToJsonResponse_t* _xmlToJsonResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) );

        
           
                                                               
                                                                                                                  
                               
                                              
         
                                      
                                                                     
                                                            
                                                      
           

        

        void axis2_stub_start_op_JsonEngineService_startNewJsonSession( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_startNewJsonSession_t* _startNewJsonSession,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_startNewJsonSessionResponse_t* _startNewJsonSessionResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) );

        
           
                                                               
                                                                                                               
                               
                                              
         
                                   
                                                                     
                                                            
                                                      
           

        

        void axis2_stub_start_op_JsonEngineService_closeJsonSession( axis2_stub_t *stub, const axutil_env_t *env,
                                                    adb_closeJsonSession_t* _closeJsonSession,
                                                  void *user_data,
                                                  axis2_status_t ( AXIS2_CALL *on_complete ) (const axutil_env_t *, adb_closeJsonSessionResponse_t* _closeJsonSessionResponse, void *data) ,
                                                  axis2_status_t ( AXIS2_CALL *on_error ) (const axutil_env_t *, int exception, void *data) );

          

        
                                                
                                           
        
     

        
                                                 
                                           
        
     

	#ifdef __cplusplus
	}
	#endif
   

