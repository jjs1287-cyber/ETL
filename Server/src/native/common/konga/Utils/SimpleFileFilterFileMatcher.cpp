/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include "StringUtils.h"	                                                      

#include "SimpleFileFilterFileMatcher.h"

using namespace konga;
using namespace boost;
using std::string;

namespace {
	                                                                                                           
	                                                            
	string getRegexFromInputFileFilter(const string& stFileFilter, bool bCaseSensitive, bool bUseRegEx) {
		if ( bUseRegEx ) {
			                                                           
			return stFileFilter;
		}
		string in(stFileFilter);

		                                                         
		AddEscapeSequences(in, "+.^$()[]{}|\\");

		                                        
		string out;
		ReplaceCharWithString(in, out, '*', "(.*)");
		ReplaceCharWithChar(out, '?', '.');
		if ( !bCaseSensitive )
			ToLower(out);
		return out;	
	}
}

SimpleFileFilterFileMatcher::SimpleFileFilterFileMatcher(const std::string& stFileFilter, bool bCaseSensitive, bool bUseRegEx)
	: m_bCaseSensitive(bCaseSensitive), m_bUseRegEx(bUseRegEx), m_stSimpleFileFilter(stFileFilter),
	m_regex( getRegexFromInputFileFilter(stFileFilter, bCaseSensitive, bUseRegEx) )
{
	if ( !m_bUseRegEx && !m_bCaseSensitive ) {
		ToLower(m_stSimpleFileFilter);
	}
}

SimpleFileFilterFileMatcher::~SimpleFileFilterFileMatcher(void) {}

bool SimpleFileFilterFileMatcher::isMatch(const string& sFileName) const {
	cmatch what;
	if ( m_bUseRegEx ) {
		return regex_match(sFileName.c_str(), what, m_regex);
	}
	else {
		if ( m_bCaseSensitive ) {
			return ( sFileName == m_stSimpleFileFilter || regex_match(sFileName.c_str(), what, m_regex) );
		}
		else {
			string sLowerCase(sFileName);
			ToLower(sLowerCase);
			return ( sLowerCase == m_stSimpleFileFilter || regex_match(sLowerCase.c_str(), what, m_regex) );
		}
	}
}
