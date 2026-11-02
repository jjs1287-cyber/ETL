/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <string>

#ifndef ETLFORGE_TRANS_ENG_USER_RAISED_ERROR_H
#define ETLFORGE_TRANS_ENG_USER_RAISED_ERROR_H

namespace konga {

	                                                                        
	                                                                                               
	                                                                                       
	                                        
	                                                                                                                     
	class RaiseErrorException {
	public:
		RaiseErrorException(const std::string& stError) : m_stError(stError) {
		}
		~RaiseErrorException(void) {}

		                                                                            
		std::string GetError() const {
			return m_stError + "\nThis error was raised by a call to the RaiseError function.";
		}

	private:
		std::string m_stError;
	};
}

#endif                                          

