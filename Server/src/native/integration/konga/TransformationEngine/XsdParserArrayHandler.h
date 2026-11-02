/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_TRANSFORMATION_ENGINE_XSDPARSER_ARRAY_HANDLER_H
#define ETLFORGE_TRANSFORMATION_ENGINE_XSDPARSER_ARRAY_HANDLER_H

class CROM;

class XsdParserArrayHandler {
public:
	XsdParserArrayHandler() {}
	~XsdParserArrayHandler() {}

	void HandleArraysInCromTree(CROM* root) const;

private:
	CROM* FindArrayMarker(CROM* parent) const;
	void TransferArrayMarkerInformationToParent(CROM* parent, CROM* arrayMarker) const;
};

#endif                                                            

