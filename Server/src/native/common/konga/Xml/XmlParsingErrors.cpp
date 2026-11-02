/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <stdio.h>

#include <cassert>

#include <boost/noncopyable.hpp>

#include <konga/Utils/StringUtils.h>		             
#include <konga/Utils/FileUtils.h>			               

#include "XmlParsingErrors.h"

using namespace konga;
using namespace konga::xml;
using std::string;
using std::vector;
using std::map;


string XmlParsingError::getPrettyErrorString() const {
	string msg;
	if ( m_lineNumber > 0 ) {
		msg += "Line " + toString(m_lineNumber) + ", Column " + toString(m_columnNumber);
		if ( !m_sContext.empty() ) {
			msg += " (near " + m_sContext + ")";
		}
	}
	if ( !msg.empty() ) msg += "\n";
	msg += m_sMessage;
	return msg;
}

namespace {
	const string::size_type maxContextLength = 128;
}

namespace konga {
namespace xml {

	class ParsedXmlFile : boost::noncopyable {
	public:
		ParsedXmlFile(const string& sFileName) : m_f(0), m_currentLine(1) {
			m_f = fopen(sFileName.c_str(), "r");
		}

		~ParsedXmlFile(void) {
			if ( m_f ) {
				fclose(m_f);
				m_f = 0;
			}
		}

		#define BIG_BUF_SIZE 512
		#define SMALL_BUF_SIZE 256

		                                                                     
		                      
		                                                                  
		FILE* gotoLine(long line) {
			if ( m_f == 0 )
				return 0;	                 
			if ( line < 1 )
				return 0;	                 

			if ( m_currentLine == line )
				return m_f;
			else if ( m_currentLine > line ) {
				rewind(m_f);
				m_currentLine = 1;
			}

			                            
			char buf[BIG_BUF_SIZE];
			while( fgets(buf, BIG_BUF_SIZE, m_f) ) {
				                                         
				const size_t len = strlen(buf);
				if ( len > 0 ) {
					if ( buf[len-1] == '\n' )
						++m_currentLine;
				}
				if ( m_currentLine == line )
					break;
			}
			if ( m_currentLine < line )
				return 0;	                                                           
			assert(m_currentLine == line);
			return m_f;
		}

	private:
		FILE* m_f;
		long m_currentLine;
	};

	typedef map<string, ParsedXmlFile*> FilesMapT;

	class XmlParsingErrorsImpl : boost::noncopyable {
	public:
		XmlParsingErrorsImpl(void) {}
		~XmlParsingErrorsImpl(void) {
			for( FilesMapT::iterator it = m_files.begin(); it != m_files.end(); ++it ) {
				if ( it->second ) {
					delete it->second;
					it->second = 0;
				}
			}
			m_files.clear();
		}

		string getContext(const string& sFileName, long line, long col) {
			FilesMapT::iterator it = m_files.find(sFileName);
			if ( it == m_files.end() ) {
				m_files.insert( FilesMapT::value_type(sFileName, new ParsedXmlFile(sFileName) ) );
				it = m_files.find(sFileName);
			}

			ParsedXmlFile& file = *it->second;
			FILE* f = file.gotoLine(line);
			if ( f == 0 )
				return string();

			                                      
			const long startFilePos = ftell(f);
			const long delta = SMALL_BUF_SIZE/2;
			const long startCol = col < delta ? 0 : col - delta;

			                                             
			if ( startCol > 0 ) {
				if ( fseek(f, startCol, SEEK_CUR) != 0 ) {
					return string();
				}
			}

			                                                              
			char buf[SMALL_BUF_SIZE];
			string s;
			if ( fgets(buf, SMALL_BUF_SIZE, f) != 0 ) {
				s = buf;
			}

			                                                                            
			const long d = startFilePos - ftell(f);
			if ( d != 0 ) {
				fseek(f, d, SEEK_CUR);
			}
			assert( startFilePos == ftell(f) );
			f = 0;

			                                                   
			s = TrimCharsFromString(s, " \t\n\r");
			if ( s.length() > maxContextLength ) {
				string::size_type trimLen = (s.length() - maxContextLength)/2;
				s.erase(0, trimLen);
				s.erase(s.length()-trimLen);
			}
			return s;
		}

		FilesMapT m_files;
	};
}
}

XmlParsingErrors::XmlParsingErrors(void) : m_pImpl(new XmlParsingErrorsImpl) {}

XmlParsingErrors::~XmlParsingErrors(void) {
	deallocate();
}

void XmlParsingErrors::AddError(const string& sFileName,
								long lineNumber,
								long columnNumber,
								const string& sMessage)
{
	string sContext;
	if ( FileExists(sFileName) && lineNumber > 0  ) {
		sContext = m_pImpl->getContext(sFileName, lineNumber, columnNumber);
	}

	ParsingErrorsMapT::iterator it = m_parsingErrorsPerFile.find(sFileName);
	if ( it == m_parsingErrorsPerFile.end() ) {
		              
		m_parsingErrorsPerFile.insert(ParsingErrorsMapT::value_type(sFileName, ParsingErrorsT()));
		it = m_parsingErrorsPerFile.find(sFileName);
	}

	it->second.push_back( new XmlParsingError(lineNumber, columnNumber, sContext, sMessage) );
}

bool XmlParsingErrors::empty() const {
	ParsingErrorsMapT::const_iterator fileIt = m_parsingErrorsPerFile.begin();
	for( ; fileIt != m_parsingErrorsPerFile.end(); ++fileIt ) {
		const ParsingErrorsT& parsingErrors = fileIt->second;
		ParsingErrorsT::const_iterator errIt = parsingErrors.begin();
		if ( errIt != parsingErrors.end() )
			return false;
	}
	return true;
}

void XmlParsingErrors::resetErrors() {
	deallocate();
	m_pImpl = new XmlParsingErrorsImpl();
}

void XmlParsingErrors::deallocate() {
	ParsingErrorsMapT::iterator fileIt = m_parsingErrorsPerFile.begin();
	for( ; fileIt != m_parsingErrorsPerFile.end(); ++fileIt ) {
		ParsingErrorsT& parsingErrors = fileIt->second;
		ParsingErrorsT::iterator errIt = parsingErrors.begin();
		for( ; errIt != parsingErrors.end(); ++errIt ) {
			delete (*errIt);
			*errIt = 0;
		}
		parsingErrors.clear();
	}
	if ( m_pImpl ) {
		delete m_pImpl;
		m_pImpl = 0;
	}
}

string XmlParsingErrors::getPrettyErrorString(bool bError) const {
	string msg;
	ParsingErrorsMapT::const_iterator fileIt = m_parsingErrorsPerFile.begin();
	for( ; fileIt != m_parsingErrorsPerFile.end(); ++fileIt ) {

		const ParsingErrorsT& parsingErrors = fileIt->second;
		ParsingErrorsT::const_iterator errIt = parsingErrors.begin();
		for( ; errIt != parsingErrors.end(); ++errIt ) {
			const XmlParsingError& parsingError = *(*errIt);
			msg += parsingError.getPrettyErrorString();
			msg += "\n";
		}

		msg += "\n";
		msg += "[Parse " + string(bError?"error":"warning") + "(s) in: " + fileIt->first + "]\n";
	}
	return msg;
}
