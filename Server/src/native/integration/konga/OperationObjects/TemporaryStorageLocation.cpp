/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/Settings.h>

#include "OperationEngineException.h"
#include "TemporaryStorageLocation.h"

using namespace konga;
using std::string;
using std::vector;

string TemporaryStorageLocation::GetTmpDirectoryWithSlash() const {
	                                                        
	string tmpDir(konga::GetOptionAsString("OperationEngine", "TemporaryStorageRootDirectory", ""));
	if( tmpDir.empty() || !IsDirectory(tmpDir) ) {
		tmpDir = konga::getTmpDir();
		tmpDir += "TemporaryStorage";
		kCreateDirectory(tmpDir);
	}
	AppendPathSeparator(tmpDir);
	return tmpDir;
}

namespace {
	const string FORBIDDEN("\"?*%:<>|");

	const char* winForbidden[] = {
		"AUX",
		"CLOCK$",
		"COM1",
		"COM2",
		"COM3",
		"COM4",
		"COM5",
		"COM6",
		"COM7",
		"COM8",
		"COM9",
		"CON",
		"LPT1",
		"LPT2",
		"LPT3",
		"LPT4",
		"LPT5",
		"LPT6",
		"LPT7",
		"LPT8",
		"LPT9",
		"NUL",
		"PRN",
		0
	};

	bool validateWinPath(const string& path, string& invalid) {
		if ( path.empty() ) return true;
		string path_test( TemporaryStorageLocation::ToCanonicPath(path) );

		vector<string> atoms = konga::SplitString(path_test, "/");
		if ( atoms.empty() ) return true;

		for( vector<string>::const_iterator cit = atoms.begin(); cit != atoms.end(); ++cit ) {
			string atom = *cit;
			konga::ToUpper(atom);
			for ( int i = 0; winForbidden[i] != 0; ++i) {
				if ( atom.compare(winForbidden[i]) == 0 ) {
					invalid = atom;
					return false;
				}
			}
		}
		return true;
	}
}

void TemporaryStorageLocation::ValidatePath(const std::string& path) const {
	if ( path.empty() ) return;

	const string::size_type ipos = path.find_first_of(FORBIDDEN);
	if( ipos != string::npos ) {
		throw OperationEngineException("Invalid temporary storage path: '" + path + "'. Contains invalid character '"
			+ path.at(ipos) + "'.");
	}
	if ( path.find("..") != string::npos ) {
		throw OperationEngineException("Invalid temporary storage path: '" + path + "'. \"..\" is not permitted. '");
	}
	if ( path.find("\\\\") != string::npos ) {
		throw OperationEngineException("Invalid temporary storage path: '" + path + "'. \"\\\\\" is not permitted. '");
	}

#ifdef WIN32
	string invalid;
	if ( !validateWinPath(path, invalid) ) {
		throw OperationEngineException("Invalid temporary storage path. The invalid part is " + invalid);
	}
#endif
}

string TemporaryStorageLocation::ToCanonicPath(const std::string& path) {
	string tmp( konga::TrimCharsFromString(path, "\t \\/:") );
	konga::ReplaceCharWithChar(tmp, '\\', '/');
	return tmp;
}

string TemporaryStorageLocation::GetRelativePath() const {
	                                                                       
	                                                                      
	if (m_CommonData.m_type == LocalFileSourceTypeId) return "";
	                              
	string stRelativePath( replace_de(m_CommonData.m_stLocator) );
	stRelativePath = ToCanonicPath(stRelativePath);
	ValidatePath(stRelativePath);
	return stRelativePath;
}

string TemporaryStorageLocation::GetAbsoluteRootPathWithSlash() const {
	if (m_CommonData.m_type == LocalFileSourceTypeId) {
		string root(replace_de(m_CommonData.m_stLocator));
		ReplaceCharWithChar(root, '\\', '/');
		const bool drive = root.size() >= 3 &&
			((root[0] >= 'A' && root[0] <= 'Z') || (root[0] >= 'a' && root[0] <= 'z')) &&
			root[1] == ':' && root[2] == '/';
		if (!drive || root.find_first_of("\"?*<>|") != string::npos ||
			root.find(':', 2) != string::npos || root.find('\0') != string::npos)
			throw OperationEngineException("Local-file folder must be an absolute Windows drive path, for example C:/temp: '" + root + "'.");
		if (!IsDirectory(root)) kCreateDirectory(root);
		AppendPathSeparator(root);
		return root;
	}
	const string stRelativePath = GetRelativePath();

	                               
	string stAbsolutePath(GetTmpDirectoryWithSlash() + stRelativePath);
	kCreateDirectory(stAbsolutePath);
	AppendPathSeparator(stAbsolutePath);
	return stAbsolutePath;
}

void TemporaryStorageLocation::CreateParentDirectory(const std::string& file_path) const {
	string branchPath( GetBranchPath(file_path) );
	branchPath.erase(branchPath.end()-1);
	if( !IsDirectory(branchPath) ) {
		kCreateDirectory(branchPath);
	}
}
