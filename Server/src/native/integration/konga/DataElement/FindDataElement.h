/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_DE_COMPARER_H
#define ETLFORGE_DE_COMPARER_H

namespace konga {
	class DataElement;

	                                                       
	class DataElementComparer {
	public:
		DataElementComparer(void) {}
		virtual ~DataElementComparer(void) {}

		virtual bool Compare(const DataElement& lhs, const DataElement& rhs) const = 0;
	};

	                                                                   
	                                                                            
	                                                         
	                                                                          
	                                                                                
	                                                                        
	                                                                                     
	DataElement* FindDataElement(const DataElement& collection,
		const DataElementComparer& comparer);
}

#endif                          
