/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef KONGA_CROM_UTILS
#define KONGA_CROM_UTILS

#include <string>

class CROM;

namespace konga {

	namespace CromUtils {

		bool isDerivedTypesHolder(CROM* crom);
		bool isTypeDefinition(CROM* node);
		bool isSubstitutionGroupHead(CROM* crom);
		bool isSubstitutionGroup(CROM* node);

		CROM* findSchemaTypeDefinition(CROM* typeHolder, 
									   const std::string& stTypeNamespace, 
									   const std::string& stLocalName);

		void addAsFirstChild(CROM* parent, CROM* child);
	}
}


#endif

