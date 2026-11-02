/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#include <stdlib.h>                  

#include <vector>

#include <math.h>

#include "CrException.h"
#include "StringUtils.h"	         

#include "Version.h"

using namespace konga;
using std::string;
using std::vector;

namespace {
	const long lMAJOR_MULT = 10000000;
	const long lMINOR_MULT = 10000;
	const long lPATCH_MULT = 1;

	const long lMAX_MAJOR = 400;
	const long lMAX_MINOR = 999;
	const long lMAX_PATCH = 9999;
}

Version::Version(int major, int minor, int patch, int build, const std::string& stVersionComment)
: m_ulVersionNumber(0), m_stVersionComment(stVersionComment) {
	fromVersionAtoms(major, minor, patch, build);
}

Version::Version(unsigned long VersionNumber) : m_ulVersionNumber(VersionNumber), m_iBuild(0) { }

Version::Version(const string& stVersion) : m_ulVersionNumber(0), m_iBuild(0) {
	fromVersionString(stVersion);
}

void Version::fromVersionString(const std::string& stVersion) {
	if ( stVersion.empty() )
		throw CrException("Version cannot be constructed from an empty string.");

	                                                 
	if ( stVersion.find_first_of(".") == string::npos ) {
		                                                                       
		long lVersion = toLong(stVersion);
		if ( lVersion > lMAX_MAJOR ) {
			m_ulVersionNumber = lVersion;
			m_iBuild = 0;
		}
		else {
			fromVersionAtoms(lVersion, 0, 0, 0);
		}
		return;
	}

	vector<string> tokens = konga::SplitString(stVersion, ".");
	vector<long> versions;
	string sComment;
	for( vector<string>::const_iterator cit = tokens.begin(); cit != tokens.end(); ++cit ) {
		const string& token = *cit;
		if ( sComment.empty() ) {
			                                                          
			char* pChar = 0;
			const char* ch = token.c_str();
			long l = strtol(ch, &pChar, 10);
			versions.push_back(l);
			if ( pChar && *pChar ) {
				                           
				sComment += pChar;
			}
		}
		else {
			sComment += token;
		}
	}

	m_stVersionComment = sComment;

	int major = 0, minor = 0, patch = 0, build = 0;
	for( vector<long>::size_type i = 0, n = versions.size(); i < n; ++i ) {
		switch(i) {
			case 0:
				major = versions[i];
				break;
			case 1:
				minor = versions[i];
				break;
			case 2:
				patch = versions[i];
				break;
			case 3:
				build = versions[i];
				break;
			default:
				throw CrException("The version string '" + stVersion + "' contains too many version atoms.");
		};
	}
	fromVersionAtoms(major, minor, patch, build);
}

void Version::fromVersionAtoms(int major, int minor, int patch, int build) {
	if ( major < 0 || major > lMAX_MAJOR ) {
		throw CrException("Illegal Major version number. Restriction: 0 <= Major <= " + toString(lMAX_MAJOR) + ". " +
			"Specified Major version number was " + toString(major));
	}

	if ( minor < 0 || minor > lMAX_MINOR ) {
		throw CrException("Illegal Minor version number. Restriction: 0 <= Minor <= " + toString(lMAX_MINOR) + ". " +
			"Specified Major version number was " + toString(minor));
	}

	if ( patch < 0 || patch > lMAX_PATCH ) {
		throw CrException("Illegal Patch Level number. Restriction: 0 <= Patch Level <= " + toString(lMAX_PATCH) + ". " +
			"Specified Patch Level was " + toString(patch));
	}

	m_ulVersionNumber = lMAJOR_MULT * major + lMINOR_MULT * minor + lPATCH_MULT * patch;
	m_iBuild = build;
}

int Version::GetMajor() const {
	double d = m_ulVersionNumber;
	d = d / lMAJOR_MULT;
	                                                 
	return static_cast<int>(floor(d));
}

int Version::GetMinor() const {
	int iMajor = GetMajor();
	double d = m_ulVersionNumber - iMajor * lMAJOR_MULT;
	d = d / lMINOR_MULT;
	                                                 
	return static_cast<int>(floor(d));
}

int Version::GetPatchLevel() const {
	int iMajor = GetMajor();
	int iMinor = GetMinor();
	double d = m_ulVersionNumber - iMajor * lMAJOR_MULT - iMinor * lMINOR_MULT;
	d = d / lPATCH_MULT;
	                                                 
	return static_cast<int>(floor(d));
}

int Version::GetBuildNumber() const {
	return m_iBuild;
}

string Version::GetVersionComment() const {
	return m_stVersionComment;
}

string Version::GetShortVersionString() const {
	return toString(GetMajor()) + "."
		 + toString(GetMinor()) + "."
		 + toString(GetPatchLevel()) + "."
		 + toString(GetBuildNumber());
}

string Version::GetLongVersionString() const {
	return GetShortVersionString() + GetVersionComment();
}

std::string Version::GetVersionString(int nAtoms) const {
	if ( nAtoms < 1 )
		nAtoms = 1;
	else if ( nAtoms > 4 )
		nAtoms = 4;

	vector<int> atoms;
	atoms.push_back(GetMajor());
	atoms.push_back(GetMinor());
	atoms.push_back(GetPatchLevel());
	atoms.push_back(GetBuildNumber());
	string sRetVal;
	for( int i = 0; i < nAtoms; ++i ) {
		if ( i != 0 )
			sRetVal += ".";
		sRetVal += toString(atoms.at(i));
	}
	return sRetVal;
}

unsigned long Version::GetVersionNumber() const {
	return m_ulVersionNumber;
}

bool konga::operator==(const Version& lhs, const Version& rhs) {
	return ( lhs.GetVersionNumber() == rhs.GetVersionNumber() &&
			 lhs.GetBuildNumber() == rhs.GetBuildNumber() );
}

bool konga::operator!=(const Version& lhs, const Version& rhs) {
	return !(lhs==rhs);
}

bool konga::operator<(const Version& lhs, const Version& rhs) {
	return ( lhs.GetVersionNumber() < rhs.GetVersionNumber() ||
			(lhs.GetVersionNumber() == rhs.GetVersionNumber() &&
			 lhs.GetBuildNumber() < rhs.GetBuildNumber()) );
}

bool konga::operator<=(const Version& lhs, const Version& rhs) {
	return !(rhs<lhs);
}

bool konga::operator>(const Version& lhs, const Version& rhs) {
	return ( lhs.GetVersionNumber() > rhs.GetVersionNumber() ||
			(lhs.GetVersionNumber() == rhs.GetVersionNumber() &&
			 lhs.GetBuildNumber() > rhs.GetBuildNumber()) );
}

bool konga::operator>=(const Version& lhs, const Version& rhs) {
	return !(rhs>lhs);
}
