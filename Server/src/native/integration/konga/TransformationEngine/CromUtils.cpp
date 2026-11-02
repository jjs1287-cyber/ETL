/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>

#include "CromUtils.h"
#include "crom.h"

using namespace std;
using namespace konga;

bool CromUtils::isDerivedTypesHolder(CROM* node)
{
	return (node && (node->m_Type & CROM_t_DerivedTypeHolder) != 0);
}

bool CromUtils::isTypeDefinition(CROM* node)
{
	return ( node && (node->m_Type & CROM_t_TypeDefinition) != 0);
}

bool CromUtils::isSubstitutionGroupHead(CROM* node)
{
	return (node && (node->m_Type & CROM_t_SubstitutionGroupHead) != 0);
}


CROM* CromUtils::findSchemaTypeDefinition(CROM* typeHolder, 
										  const string& stTypeNamespace, 
										  const string& stLocalName)
{
	if (!isDerivedTypesHolder(typeHolder)) {
		return 0;
	}
	CROM* child = typeHolder->GetFirstChild();
	while (child != 0) {
		if (isTypeDefinition(child)) {
			if (Equals(stTypeNamespace, child->GetSchemaTypeNamespace()) &&
				Equals(stLocalName, child->GetSchemaType())) {
					return child;
			}
		}
		child = child->GetNextSibling();
	}
	return 0;
}

void CromUtils::addAsFirstChild(CROM* parent, CROM* child)
{
	CROM* existingFirstChild = parent->GetFirstChild();
	if (existingFirstChild == 0) {
		CROM::AddToParent(parent, child);
	}
	else {
		child->SetParent(parent);
		parent->SetFirstChild(child);
		child->SetNextSibling(existingFirstChild);
		existingFirstChild->SetPreviousSibling(child);
	}
}
