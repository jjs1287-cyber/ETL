/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>

#include "ProcessedFiles.h"
#include "RenameFile.h"
#include "OperationEngineException.h"
#include "TemporaryStorageTarget.h"

using namespace konga;
using std::string;
using std::vector;

std::string TemporaryStorageTarget::getFileName() const {
	string tmp(getOverrideFileName());
	if ( tmp.empty() ) {
		tmp = replace_de( m_CommonData.m_stFileName );
		tmp = ToCanonicPath(tmp);
	}

	ValidatePath(tmp);
	return tmp;
}

void TemporaryStorageTarget::doTransfer(const OperationDataElementHolder& deHolder, ProcessedFiles& files) const {
	try {
		const string& stRootDir = GetAbsoluteRootPathWithSlash();
		vector<ProcessedFile>& transfered_files = files.getProcessedFilesByRef();
		RenameFile renamer( getFileName() );
		for( vector<ProcessedFile>::iterator it = transfered_files.begin();
			 it != transfered_files.end(); ++it ) {
			ProcessedFile& file = *it;
			if ( doNotTransfer(file) )
				continue;

			const string& stRemoteName = getRenamedRemoteName(renamer, file);
			if ( stRemoteName.empty() ) throw OperationEngineException("No file name was specified for temporary storage target \"" + this->getName()
				+ "\". If you use global data elements in the file name, make sure it evaluates to a non-empty value.");

			const string& stLocalFile = file.getLocalTargetLocation();
			const string& stAbsoluteRemotePath(stRootDir + stRemoteName);
			if ( konga::IsDirectory(stAbsoluteRemotePath) ) {
				throw OperationEngineException("Can not copy to local temporary storage path '" + stRemoteName + "' since it is a directory.");
			}
			try {
				                                                                 
				CreateParentDirectory(stAbsoluteRemotePath);
				kCopyFile(stLocalFile, stAbsoluteRemotePath, true);
				file.setRemoteTargetLocation(stRemoteName);
			}
			catch( const CrException& ex ) {
				string stErr("Failed to copy local file\n");
				stErr += stLocalFile + "\n";
				stErr += "to local temporary storage at \n";
				stErr += stAbsoluteRemotePath + "\nReason: ";
				stErr += ex.GetErrorString();
				throw OperationEngineException(stErr);
			}
		}
	}
	catch( const std::exception& ex ) {
		throw OperationEngineException(string("Unexpected exception while copying to a temporary storage target: ") + ex.what());
	}
	catch( const CrException& ex ) {
		throw OperationEngineException(ex.GetErrorString());
	}
}

void TemporaryStorageTarget::testFileTargetConnection(bool bCopyTestFile) const {
	                                                    
	GetAbsoluteRootPathWithSlash();
}

