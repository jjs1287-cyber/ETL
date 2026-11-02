/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XML_PARSING_ERRORS_H
#define KONGA_XML_PARSING_ERRORS_H

#include <boost/noncopyable.hpp>

#include <map>
#include <vector>

namespace konga {
namespace xml {

	class XmlParsingError {
	public:
		XmlParsingError(long lineNumber, long columnNumber,
			const std::string& sContext, const std::string& sMessage):
		m_lineNumber(lineNumber), m_columnNumber(columnNumber),
			m_sContext(sContext), m_sMessage(sMessage) {}
		~XmlParsingError(void) {}

		std::string getPrettyErrorString() const;

	private:
		long m_lineNumber;
		long m_columnNumber;
		std::string m_sContext;
		std::string m_sMessage;
	};

	class XmlParsingErrorsImpl;

	class XmlParsingErrors : boost::noncopyable {
	public:
		XmlParsingErrors(void);
		~XmlParsingErrors(void);

		void AddError(const std::string& sFileName, long lineNumber, long columnNumber, const std::string& sMessage);

		void resetErrors();

		bool empty() const;

		                                                                         
		std::string getPrettyErrorString(bool bError) const;

	private:
		void deallocate();
		XmlParsingErrorsImpl* m_pImpl;
		                                                
		typedef std::vector<XmlParsingError*> ParsingErrorsT;
		typedef std::map<std::string, ParsingErrorsT> ParsingErrorsMapT;
		ParsingErrorsMapT m_parsingErrorsPerFile;
	};
}
}

#endif                              

