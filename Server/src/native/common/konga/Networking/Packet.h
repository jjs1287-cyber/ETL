/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_GEN_PACKET_H
#define KONGA_GEN_PACKET_H

#include <string>

namespace konga {
namespace networking {

  
                                                                      
                 
  
                        
  
                                                                                                                                     
                                                                        
                                                                                                    
                       
                                                                                                                                                  
                                              
                         
                                                                                     
  
                                                                         
  
                           
                      
                                 
                      
              
                                                                             
                   
              
                                                    
                   
          
        
                   
          
        
                   
              
  
                            
                                                    
  
                            
                   
           
  
                              
                                                                                                                       
                                                           
              
                                                          
                     
             
        
                     
             
        
                   
              
  
                               
                                                                    
  

class Packet {
public:
	static const unsigned long DIRECTION_REQUEST;
	static const unsigned long DIRECTION_RESPONSE;
public:
	Packet() {}
	virtual ~Packet() {}

	virtual std::string GetProtocolName() const = 0;
	virtual unsigned long GetProtocolVersion() const = 0;
};

}                   
}                        


#endif                      
