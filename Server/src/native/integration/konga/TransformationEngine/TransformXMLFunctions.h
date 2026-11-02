/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef TRANSFORMATION_XML_FUNCTIONS_H
#define TRANSFORMATION_XML_FUNCTIONS_H

#include <konga/Xml/DOMNodePtr.h>

#include <string>
#include <vector>

class CRXmlData;

                                                   
namespace konga {

	class DataElement;

	std::string getXmlString(CRData* s1, const KString& path);

	bool isPresent(CRData* s1, const KString& path);
	bool isNil(CRData* s1, const KString& path);

	std::string CreateNode(const char* namespace_, const char* name_,
		const std::vector<std::string>& v);

	std::string Attribute(const char* name, const char* value);

	                                                                                                                    
	                                              
	DataElement* SelectNodeFromXmlAny(const std::string& node_name, const DataElement* const any_collection);

	xml::DOMNodePtr createDOMNode(const std::string& str);

	                                                                         
	DataElement* SelectNodesFromXmlAny(const std::string& xpath_query,
		const DataElement* const any_collection,
		const std::vector<std::string>& vnamespaces);

	DataElement* SelectSingleNode(DataElement* de, const std::string& xpath,
		const std::vector<std::string>& vnamespaces, int& err);

	DataElement* SelectNodes(DataElement* de, const std::string& xpath,
		const std::vector<std::string>& vnamespaces, int& err);

	std::string ArrayToMultipleValuesXML(const DataElement& de);
}

#endif                                  

