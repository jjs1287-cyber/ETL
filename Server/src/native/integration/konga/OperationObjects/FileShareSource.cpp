/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <sstream>		                    
#include <exception>	                 

#include <konga/Utils/SimpleFileFilterFileMatcher.h>
#include <konga/Utils/FileUtils.h>		                

#include <konga/FileTransfer/Samba.h>

#include <konga/DataElement/DataElement.h>
#include <konga/OperationContext/OperationDataElementHolder.h>

#include <konga/SystemVariables/SystemVariables.h>	                       

#include "OperationEngineException.h"
#include "OperationEngineContext.h"
#include "RenameFile.h"
#include "ProcessedFiles.h"
#include "FileShareSource.h"

using namespace konga;
using namespace std;

string FileShareSource::getRenameFilePattern() const {
	return replace_de( m_SourceData.m_stRenameFileTo );
}

std::string FileShareSource::getFileFilter() const {
	const string& tmp = getOverrideFileFilter();
	if ( !tmp.empty() )
		return tmp;
	return replace_de( TrimString(m_CommonData.m_stFileName) );
}

namespace {
	const string FILE_SHARE_LIST_LIMIT_DE_NAME("etlforge.source.file_share.file_list_limit");
}

int FileShareSource::getFileShareListLimit(const OperationDataElementHolder* pDeHolder) const {
	return konga::GetSystemVariableAsLong(FILE_SHARE_LIST_LIMIT_DE_NAME, pDeHolder, -1);
}

void FileShareSource::getFilteredDirectoryList(Samba& smb, vdirentry& dirlist) const
{
	const string& stFileFilter = getFileFilter();
	if ( stFileFilter.empty() )
		throw OperationEngineException("File filter is empty.");

	vdirentry fullDirList;
	GetSmbDirectoryList(smb, fullDirList, getFileShareListLimit(m_pDataElementHolder));

	SimpleFileFilterFileMatcher matcher(stFileFilter, !useCaseInsensitiveFileFilter(), useRegExFileFilter());
	vdirentry::const_iterator cit = fullDirList.begin();
	for( ; cit != fullDirList.end(); ++cit ) {
		const direntry& entry = *cit;
		if ( entry.isFile() && matcher.isMatch(entry.name) ) {
			dirlist.push_back(entry);
		}
	}
}

vector<string> FileShareSource::getFileList() const {
	Samba smb(false, false);
	initSmb(smb);
	vdirentry dirlist;
	getFilteredDirectoryList(smb, dirlist);

	                              
	vector<string> vFileList;
	vdirentry::const_iterator cit = dirlist.begin();
	for(; cit != dirlist.end(); ++cit ) {
		const direntry& entry = *cit;
		vFileList.push_back(entry.name);
	}
	return vFileList;
}

void FileShareSource::getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const {
	Samba smb(false, false);
	initSmb(smb);
	const string& stUncPath = getUncPath();
	vdirentry dirlist;
	getFilteredDirectoryList(smb, dirlist);

	try {
		const string& stTmpDir = context.getLocalTmpDirForOperation();
		smb.setUNCPath(stUncPath);
		vdirentry::const_iterator cit = dirlist.begin();
		if ( cit != dirlist.end() ) {
			const direntry& entry = *cit;
			const string stUncFile(stUncPath + entry.name);
			const string stLocalSourceFile(stTmpDir + entry.name);
			smb.CopyFromUncToLocalFile(stUncFile, stLocalSourceFile);
			ProcessedFile file;
			file.setFileChangeTimeNow();
			file.setInHistory(false);
			file.setLocalSourceLocation(stLocalSourceFile);
			file.setRemoteSourceLocation(stUncFile);
			long lErr = 0;
			const long long size = GetFileSize(stLocalSourceFile, lErr);
			file.setSourceSize(size);
			file.setTransferedNow();
			files.add(file);
			return;
		}
	}
	catch( const exception& ex ) {
		stringstream sErr;
		sErr << "Failed to match files in \"" << stUncPath << "\" to file filter \"" << getFileFilter() << endl;
		sErr << ex.what();
		throw OperationEngineException(sErr.str());
	}
	catch( const CrException& ex ) {
		stringstream sErr;
		sErr << "Failed to get previously found entry in " << stUncPath << endl << ex.GetErrorString();
		throw OperationEngineException(sErr.str());
	}

	                                                            
	throw OperationEngineException("No files match the file filter \"" + getFileFilter() + "\".");
}

void FileShareSource::doTransferImpl(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const {
	Samba smb(false, false);
	initSmb(smb);
	const string& stUncPath = getUncPath();
	vdirentry dirlist;
	getFilteredDirectoryList(smb, dirlist);

	const int maxFilesToGet = getFileListLimit(m_pDataElementHolder);

	bool bIsCheckingForAlreadyTransferred = false;	                                                                
	try {
		bIsCheckingForAlreadyTransferred = false;
		const string& stTmpDir = context.getLocalTmpDirForOperation();
		smb.setUNCPath(stUncPath);
		vdirentry::const_iterator cit = dirlist.begin();
		int iFile = 0;
		for(; cit != dirlist.end(); ++cit ) {
			const direntry& entry = *cit;
			const string stUncFile(stUncPath + entry.name);
			ProcessedFile file(stUncFile, entry.size, entry.month(), entry.day(), entry.time());
			bIsCheckingForAlreadyTransferred = true;
			if ( context.isAlreadyTransferred(file) ) {
				files.add(file);
				continue;
			}
			bIsCheckingForAlreadyTransferred = false;

			                                                                                     
			const string stLocalFile( stTmpDir + toString(++iFile) + "_" + entry.name );
			smb.CopyFromUncToLocalFile(stUncFile, stLocalFile);	
			file.setLocalSourceLocation(stLocalFile);
			files.add(file);
			if ( maxFilesToGet > 0 && files.getNumberOfProcessedFiles() >= maxFilesToGet ) break;
		}
	}
	catch( const exception& ex ) {
		stringstream sErr;
		sErr << "Failed to match files in \"" << stUncPath << "\" to file filter \"" << getFileFilter() << endl;
		sErr << ex.what();
		throw OperationEngineException(sErr.str());
	}
	catch( const CrException& ex ) {
		if ( bIsCheckingForAlreadyTransferred ) {
			throw OperationEngineException("Failed to check if the file was already transferred. " + ex.GetErrorString(), "Transferring file share source.");
		}
		throw OperationEngineException(ex.GetErrorString(), "Transferring file share source.");
	}
}

void FileShareSource::doPostTransformation(const OperationEngineContext& context, ProcessedFiles& files) const {

	const bool bDelete = deleteAfterProcessing();
	                                                
	const RenameFile renamer( getRenameFilePattern() );
	if ( bDelete || !renamer.empty() ) {
		Samba smb(false, false);
		const string& stUserName = getUserName();
		if ( !stUserName.empty() ) {
			smb.setUserName(stUserName);
			smb.setPassword(getDecryptedPassword());
		}

		string stRemotePath;
		try {
			stRemotePath = smb.setUNCPath(getUncPath());
		}
		catch( const CrException& ex ) {
			string stErr( bDelete ? "Failed to delete file share source file. " : "Failed to rename file share source file. " );
			stErr += ex.GetErrorString();
			throw OperationEngineException(stErr);
		}

		vector<ProcessedFile>& vFiles = files.getProcessedFilesByRef();
		for( vector<ProcessedFile>::iterator it = vFiles.begin();
			 it != vFiles.end();
			 ++it )
		{
			ProcessedFile& file = *it;
			if ( file.hasError() || file.isInHistory() )
				continue;

			                                                                                     
			const string& remoteLeaf = file.getRemoteSourceLeafName();
			if ( remoteLeaf.empty() ) {
				continue;
			}
			try {
				if ( bDelete ) {
					smb.DeleteFileOnShare( stRemotePath + remoteLeaf );
				}
				else {
					const string& stOldName = remoteLeaf;
					const string& stNewName = renamer.getRenamedFileName(stOldName);
					smb.MoveFileOnShare(stRemotePath + stOldName, stRemotePath + stNewName);
				}
			}
			catch( const CrException& ex ) {
				const string stError( bDelete ? "Failed to delete source file. " : "Failed to rename source file. " );
				file.addWarning(stError + ex.GetErrorString());
			}
			catch( const std::exception& e ) {
				file.addWarning("Failed to " + string(bDelete?"delete":"rename") + " source file '" + remoteLeaf
					+ "'. Reason: " + string(e.what()));
			}
		}
	}
}

