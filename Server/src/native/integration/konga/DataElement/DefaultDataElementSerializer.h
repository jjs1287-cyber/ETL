/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_DATA_ELEMENT_DEFAULTDATAELEMENTSERIALIZER_H
#define ETLFORGE_DATA_ELEMENT_DEFAULTDATAELEMENTSERIALIZER_H

#include "DataElementSerializer.h"
namespace konga {

	  
                                                                                       
                                                                                      
                                                                                         
                                                                                         
                                                                                                  
   
	class DefaultDataElementSerializer : public DataElementSerializer {
	public:
		DefaultDataElementSerializer(void);
		virtual ~DefaultDataElementSerializer(void);

		                                                                                  
		virtual std::string Serialize(const std::string& deName, const DataElement& de) const;

		virtual DataElement* DeSerialize(const std::string& stDe) const;
	};

}

#endif                                                        

