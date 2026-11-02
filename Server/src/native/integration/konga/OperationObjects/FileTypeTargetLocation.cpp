/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include "ProcessedFiles.h"
#include "RenameFile.h"
#include "FileTypeTargetLocation.h"

using namespace konga;
using std::string;

bool FileTypeTargetLocation::doNotTransfer(const ProcessedFile& file) {
	return ( file.hasError() || file.isInHistory() || file.getLocalTargetLocation().empty() );
}

string FileTypeTargetLocation::getRenamedRemoteName(const RenameFile& renamer, const ProcessedFile& file) const {
	if (!file.archiveTargetName.empty()) return file.archiveTargetName;
	if ( renamer.hasSubstitutions() ) {
		                                                                                
		string leaf(file.getRemoteSourceLeafName());
		if ( leaf.empty() ) {
			                                                           
			leaf = file.getLocalSourceLeafName();
		}
		return renamer.getRenamedFileName(leaf);
	}
	else {
		                                           
		return renamer.getPattern();
	}
}
