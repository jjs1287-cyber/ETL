/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <iomanip>
#include <sstream>

#include <boost/algorithm/string/replace.hpp>
#include <boost/algorithm/string/find.hpp>

#include <boost/date_time/posix_time/posix_time.hpp>
#include <boost/date_time/gregorian/gregorian.hpp>

#include <konga/Utils/util.h>

#include "RenameFile.h"

using namespace std;
using namespace boost;
using namespace boost::algorithm;
using namespace konga;

                     
namespace {
	const char FILE_PAT[] = "[file]";
	const char EXT_PAT[]  = "[ext]";
	const char DATE_PAT[] = "[date]";
	const char TIME_PAT[] = "[time]";
	const char UNIQUE_PAT[] = "[unique]";

	                                                                                                                                
	void find_and_replace_all(bool& bFound, string& stPat, const char* PAT) {
		if ( ifind_first<string, string>(stPat, PAT) ) {
			bFound =  true;
			ireplace_all(stPat, PAT, PAT);
		}
		else
			bFound = false;
	}
}

RenameFile::RenameFile(const std::string& stRenamePattern)
	: m_bHasFile(false), m_bHasExt(false), m_bHasDate(false), m_bHasTime(false), m_bHasUnique(false), m_bHasSubstitutions(false) {
	init(stRenamePattern);
}

RenameFile::~RenameFile(void) { }

void RenameFile::init(const std::string& stRenamePattern) {
	if ( stRenamePattern.empty() )
		return;

	                                                               
	string stPat(stRenamePattern);

	find_and_replace_all(m_bHasFile, stPat, FILE_PAT);

	find_and_replace_all(m_bHasExt, stPat, EXT_PAT);

	find_and_replace_all(m_bHasDate, stPat, DATE_PAT);

	find_and_replace_all(m_bHasTime, stPat, TIME_PAT);

	find_and_replace_all(m_bHasUnique, stPat, UNIQUE_PAT);
	m_bHasSubstitutions = (m_bHasFile || m_bHasExt || m_bHasDate || m_bHasTime || m_bHasUnique);

	m_stPattern = stPat;
}

bool RenameFile::empty() const {
	return m_stPattern.empty();
}

std::string RenameFile::getPattern() const {
	return m_stPattern;
}

bool RenameFile::hasSubstitutions() const {
	return m_bHasSubstitutions;
}

std::string RenameFile::getRenamedFileName(const std::string& stFileName) const {
	using namespace boost::posix_time;
	using namespace boost::gregorian;


	if ( m_stPattern.empty() )
		return stFileName;

	string stRenamedFile( m_stPattern );
	if ( m_bHasFile || m_bHasExt ) {
		string::size_type iPos = stFileName.find_last_of('.');
		if ( m_bHasFile ) {
			string stFile;
			if ( iPos != string::npos )
				stFile = stFileName.substr(0, iPos);
			else
				stFile = stFileName;

			ireplace_all(stRenamedFile, FILE_PAT, stFile);
		}

		if ( m_bHasExt ) {
			string stExt;
			if ( iPos != string::npos && iPos != stFileName.size()-1 )
				stExt = stFileName.substr(iPos+1);

			ireplace_all(stRenamedFile, EXT_PAT, stExt);
		}
	}

	if ( m_bHasDate || m_bHasTime ) {
		ptime now = second_clock::local_time();
		if ( m_bHasDate ) {
			const date& d = now.date();
			stringstream sDate;
			sDate << d.year() << '-' << d.month().as_short_string() << '-' << d.day().as_number();
			ireplace_all(stRenamedFile, DATE_PAT, sDate.str());
		}
		if ( m_bHasTime ) {
			const time_duration& t = now.time_of_day();
			stringstream sTime;
			sTime << setfill('0') << setw(2) << t.hours();
			sTime << setfill('0') << setw(2) << t.minutes();
			sTime << setfill('0') << setw(2) << t.seconds();
			ireplace_all(stRenamedFile, TIME_PAT, sTime.str());
		}
	}

	if ( m_bHasUnique ) {
		string stGUID;
		GetGUIDString(stGUID);
		ireplace_all(stRenamedFile, UNIQUE_PAT, stGUID);
	}

	return stRenamedFile;
}

