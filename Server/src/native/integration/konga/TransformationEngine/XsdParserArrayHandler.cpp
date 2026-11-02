/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include <string>
#include <konga/Xml/ExpandedName.h>
#include "crom.h"
#include "XsdParserArrayHandler.h"

using namespace std;
using namespace konga::xml;

void XsdParserArrayHandler::HandleArraysInCromTree(CROM* root) const {
	                                      
	CROM* arrayMarker = FindArrayMarker(root);
	if (arrayMarker != 0) {
		TransferArrayMarkerInformationToParent(root, arrayMarker);
	}
	CROM* child = root->GetFirstChild();
	while (child != 0) {
		HandleArraysInCromTree(child);
		child = child->GetNextSibling();
	}
}

CROM* XsdParserArrayHandler::FindArrayMarker(CROM* parent) const {
	CROM* child = parent->GetFirstChild();
	while (child != 0) {
		if (child->IsHidden() && (child->m_Name == "arrayType")) {
			return child;
		}
		child = child->GetNextSibling();
	}
	return 0;
}

void XsdParserArrayHandler::TransferArrayMarkerInformationToParent(CROM* parent, CROM* arrayMarker) const {
	                                                     
	                                                                       
	                        
	                                        
	CROM* item = parent->GetFirstChild();
	const string typeNamespace(item->GetSchemaTypeNamespace());
	const string typeName(item->GetSchemaType());
	item->SetNextSibling(0);
	arrayMarker->SetPreviousSibling(0);
	arrayMarker->SetParent(0);
	arrayMarker->DeleteNode();
	ExpandedName en(typeNamespace, typeName);
	parent->SetArray(en.toString());
}
