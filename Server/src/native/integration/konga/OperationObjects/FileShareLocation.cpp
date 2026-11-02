/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <sstream>	               

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>

#include "OperationEngineException.h"
#include "FileShareLocation.h"

using namespace konga;
using std::string;
using std::stringstream;
using std::endl;

string FileShareLocation::getUncPath() const {
	string stUncPath( m_CommonData.m_stLocator );
	stUncPath = TrimString( stUncPath );
	AppendPathSeparator(stUncPath);
	if ( !stUncPath.empty() ) {
		*(stUncPath.end()-1) = '\\';
	}
	return replace_de( stUncPath );
}

void FileShareLocation::initSmb(Samba& smb) const {
	const string& stUserName = getUserName();
	if ( !stUserName.empty() ) {
		smb.setUserName(stUserName);
		smb.setPassword(getDecryptedPassword());
	}
}

void FileShareLocation::GetSmbDirectoryList(Samba& smb, vdirentry& dirlist, int nMaxFiles) const {
	const string& stUncPath = getUncPath();
	try {
		smb.GetUncDirectoryListing(stUncPath, dirlist, nMaxFiles);
	}
	catch( const CrException& ex ) {
		stringstream sErr;
		sErr << "Failed to get directory listing for " << stUncPath << endl << ex.GetErrorString();
		throw OperationEngineException(sErr.str());
	}
}
