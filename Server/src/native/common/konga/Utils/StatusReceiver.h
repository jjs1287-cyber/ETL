/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_UTILS_STATUS_RECEIVER_H
#define ETLFORGE_UTILS_STATUS_RECEIVER_H

#include <string>

namespace konga {

	                                                             
	                                                
	class StatusReceiver {
	public:
		StatusReceiver(void) {}
		virtual ~StatusReceiver(void) {}

		                                          
		                                                            
		                                                              
		virtual void Status(const std::string& stStatus) = 0;

		                                                                      
		                                        
		virtual void Progress(float prog) = 0;

		                                                                    
		virtual void ReportError(const std::string& stError) = 0;
	};
}

#endif                                    

