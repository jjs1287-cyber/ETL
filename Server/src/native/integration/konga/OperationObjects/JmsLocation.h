/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_JMS_LOCATION_H
#define KONGA_OPERATION_JMS_LOCATION_H

#include <string>
#include <vector>
#include <map>

#include "DataLocation.h"

namespace konga {

	                                                    
	class JmsLocation : public DataLocation {
	public:
		JmsLocation(void) {}
		virtual ~JmsLocation(void) {}

		friend class OperationObjectCreatorKeyValues;                                                        

	protected:
		                                            
		                                                          
		void testJmsLocation() const;

	private:
		std::map<std::string, std::string> m_locationProperties;
	};
}
#endif                                  
