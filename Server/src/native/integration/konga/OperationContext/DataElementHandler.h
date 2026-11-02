/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#ifndef KONGA_OPERATION_CONTEXT_DATA_ELEMENT_HANDLER_H
#define KONGA_OPERATION_CONTEXT_DATA_ELEMENT_HANDLER_H

#include <string>

namespace konga {

	class DataElement;

	                                                      
	                      
	class DataElementHandler {
	public:
		DataElementHandler(void) {}
		virtual ~DataElementHandler(void) {}

		                                                            
		                                                           
		virtual DataElement* getDataElement(const std::string& deName) const = 0;
	};
}

#endif                                                  

