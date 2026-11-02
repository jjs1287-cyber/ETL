/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_SOURCE_XML_DATA_STRUCTURE_H
#define KONGA_OPERATION_SOURCE_XML_DATA_STRUCTURE_H

#include "SourceDataStructure.h"
#include "XmlDataStructure.h"

namespace konga {

	class SourceXmlDataStructure : public XmlDataStructure, public SourceDataStructure {
	protected:
		SourceXmlDataStructure(void) { }
	public:
		virtual ~SourceXmlDataStructure(void) { }

		virtual void doPreTransformation(const OperationEngineContext& context) const;

		friend class LocationCreatorDb;
	};
}
#endif                                               

