/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_DATA_ELEMENT_DATAELEMENTSERIALIZER_H
#define ETLFORGE_DATA_ELEMENT_DATAELEMENTSERIALIZER_H

#include <string>

namespace konga {

	class DataElement;

	  
                                                                   
                                    
                                              
    
	class DataElementSerializer {
	public:
		DataElementSerializer(void) {}
		virtual ~DataElementSerializer(void) {}

		                                                    
		virtual std::string Serialize(const std::string& deName, const DataElement& de) const = 0;

		                                                                    
		                                     
		virtual DataElement* DeSerialize(const std::string& stDe) const = 0;
	};
}

#endif                                                 
