/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include <memory>	           

#include "DataElement.h"
#include "FindDataElement.h"
#include "TransException.h"

using namespace konga;

DataElement* konga::FindDataElement(const DataElement& collection,
	const DataElementComparer& comparer) 
{
	if(collection.getType()==DataElement::t_collection){
		int err=0;
		KString errmsg;
		DataElement::Type type=collection.getDataElementTypeForArray(err, errmsg);
		if(err!=0)throw konga::ExeFcnException((const char*)errmsg);
		const size_t n = collection.getDECollectionSize();
		if(type==DataElement::t_collection){
			std::auto_ptr<DataElement> retval( new DataElement() );
			retval->initCollection();
			for(size_t i=0; i<n; i++){
				DataElement* de = FindDataElement(*collection.getDECollectionOf(i), comparer);
				retval->addToCollection(de);
			}
			return retval.release();
		}
		if(n>0){
			const DataElement* deFirst=collection.getDECollectionOf(0);
			const DataElement* deFound = deFirst;
			for(size_t index=1; index<n;++index) {
				const DataElement* deThis = collection.getDECollectionOf(index);
				if ( comparer.Compare(*deThis, *deFound) )
					deFound = deThis;
			}
			return new DataElement(*deFound);
		}
	}
	return 0;
}
