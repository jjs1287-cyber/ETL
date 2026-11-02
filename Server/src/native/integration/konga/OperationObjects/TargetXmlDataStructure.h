/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_Target_XML_DATA_STRUCTURE_H
#define KONGA_OPERATION_Target_XML_DATA_STRUCTURE_H

#include "XmlDataStructure.h"
#include "TargetDataStructure.h"

namespace konga {

	class TargetXmlDataStructure : public XmlDataStructure, public TargetDataStructure {
	protected:
		TargetXmlDataStructure(void) { }
	public:
		virtual ~TargetXmlDataStructure(void) { }

		virtual void doPreTransformation(const OperationEngineContext& context) const;

	protected:
		std::string getSchemaUrl() const {
			return m_stSchemaUrl;
		}

		friend class LocationCreatorDb;

	private:
		std::string m_stSchemaUrl;
	};
}
#endif                                               

