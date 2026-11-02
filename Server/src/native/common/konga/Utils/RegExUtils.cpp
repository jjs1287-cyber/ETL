/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#include <boost/regex.hpp>
#include <boost/regex/pattern_except.hpp>

#include <konga/Utils/CrException.h>

#include "RegExUtils.h"

using namespace konga;
using namespace boost;
using std::string;
using std::vector;

void konga::RegExMatch(const string& sInput,
					  const string& sRegEx,
					  vector<string>& matches)
{
	try {
		cmatch what;
		const regex expression(sRegEx);
		if( regex_match(sInput.c_str(), what, expression) ) {
			for(int iMatch=1; iMatch<what.length(); ++iMatch)
			{
				if( !what[iMatch].matched )
					continue;
				matches.push_back(string(what[iMatch].first, what[iMatch].second));
			}
		}
	} catch( const boost::regex_error& e) {
		throw CrException("Failed to match regular expression." \
			" Error: " + string(e.what()) + "\nPosition: " + konga::toString(e.position()));
	}
}

void konga::RegExReplace(const string& sInput, const string& sRegEx,
				  const string& sFormat, string& sResult)
{
	try {
		regex e(sRegEx);
		sResult = regex_replace(sInput, e, sFormat);
	}
	catch( const boost::bad_pattern& e ) {
		throw CrException(string("Failed to replace using regular expression. ") + e.what());
	}
}
