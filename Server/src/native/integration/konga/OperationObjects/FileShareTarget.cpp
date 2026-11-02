/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <sstream>	               

#include <konga/Utils/CrException.h>
#include <konga/FileTransfer/Samba.h>

#include <konga/SystemVariables/SystemVariables.h>

#include "FileShareTarget.h"
#include "ProcessedFiles.h"
#include "RenameFile.h"
#include "OperationEngineException.h"

using namespace std;
using namespace konga;

std::string FileShareTarget::getFileName() const {
	const string& tmp = getOverrideFileName();
	if ( !tmp.empty() )
		return tmp;
	return replace_de( m_CommonData.m_stFileName );
}

bool FileShareTarget::doTargetDirectoryCheck(const OperationDataElementHolder& deHolder) const {
	return konga::GetSystemVariableAsBool("etlforge.target.file_share.create_directories", &deHolder, false);
}

void FileShareTarget::doTransfer(const OperationDataElementHolder& deHolder, ProcessedFiles& files) const {
	Samba smb(this->doTargetDirectoryCheck(deHolder), false);
	initSmb(smb);

	string stUncPath;
	try {
		stUncPath = smb.setUNCPath(getUncPath());
	}
	catch ( const CrException& ex ) {
		throw OperationEngineException("Failed to write to file share target: " + ex.GetErrorString());
	}

	vector<ProcessedFile>& transfered_files = files.getProcessedFilesByRef();
	RenameFile renamer( getFileName() );
	for( vector<ProcessedFile>::iterator it = transfered_files.begin();
		 it != transfered_files.end(); ++it ) {
		ProcessedFile& file = *it;
		if ( doNotTransfer(file) )
			continue;

		const string& stRemoteName = getRenamedRemoteName(renamer, file);
		const string& stLocalFile = file.getLocalTargetLocation();
		try {
			smb.CopyFromLocalFileToShare(stLocalFile, stUncPath + stRemoteName);
			file.setRemoteTargetLocation("\\\\" + smb.getServer() + "\\" + smb.getShare() + "\\" + stUncPath + stRemoteName);
		}
		catch( const CrException& ex ) {
			stringstream sErr;
			sErr << "Failed to copy local file" << endl << endl;
			sErr << stLocalFile << endl << endl;
			sErr << "to file share at " << endl << endl;
			sErr << "\\\\" << smb.getServer() << "\\" << smb.getShare() << endl;
			sErr << ex.GetErrorString();
			throw OperationEngineException(sErr.str());
		}
	}
}

void FileShareTarget::testFileTargetConnection(bool bCopyTestFile) const {
	Samba smb(false, false);
	initSmb(smb);
	vdirentry dirlist;
	GetSmbDirectoryList(smb, dirlist, 1);
}

