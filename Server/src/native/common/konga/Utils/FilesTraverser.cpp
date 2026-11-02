/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#include <filesystem>

#include "CrException.h"
#include "FileUtils.h"
#include "FilesTraverser.h"

using namespace konga;
using namespace std::filesystem;
using std::string;

void FilesTraverser::traverseDirectory(const std::string& sDirectory, bool bRecurseSubDirs) {
	if ( !IsDirectory(sDirectory) )
		return;
	try {
		directory_iterator end;
		path dir = u8path(sDirectory);
		directory_iterator dit(dir);
		for( ; dit != end; ++dit ) {
			const string stPath = dit->path().u8string();
			if ( IsDirectory(stPath) ) {
				if ( bRecurseSubDirs )
					traverseDirectory(stPath, true);
				else
					continue;
			}

			if ( m_matcher.isMatch(stPath) ) {
				m_action.doAction(stPath);
			}
		}
	}
	catch( filesystem_error& ex ) {
		                                                                                   
		throw CrException(string("Filesystem error occurred: ") + ex.what());
	}
}


