/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef WF_XML_READER_H
#define WF_XML_READER_H

#include <string>

namespace konga {

	class Process;

	namespace WfXmlReader {

		extern const char* type_str_long[];
		extern const char* type_str_short[];

		std::string DataTypeName_LongToShort(const std::string& sLongName);
		std::string DataTypeName_ShortToLong(const std::string& stShortName);

		Process* ParseXml(const std::string& stXmlFilePath);
	}
}                   

#endif                   
