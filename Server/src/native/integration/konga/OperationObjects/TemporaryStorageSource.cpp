/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/SimpleFileFilterFileMatcher.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FilesTraverser.h>

#include <konga/DataElement/DataElement.h>
#include <konga/OperationContext/OperationDataElementHolder.h>
#include <konga/OperationLog/OperationProgress.h>

#include <konga/SystemVariables/SystemVariables.h>	                       

#include "OperationEngineException.h"
#include "OperationEngineContext.h"
#include "RenameFile.h"
#include "ProcessedFiles.h"
#include "TemporaryStorageSource.h"

using namespace konga;
using std::string;
using std::vector;

string TemporaryStorageSource::getRenameFilePattern() const {
	return replace_de( m_SourceData.m_stRenameFileTo );
}

string TemporaryStorageSource::getFileFilter() const {
	string tmp = getOverrideFileFilter();
	if ( !tmp.empty() )
		return tmp;

	tmp = replace_de( TrimString(m_CommonData.m_stFileName) );
	return tmp;
}

namespace {
	class MyFileAction : public FileAction {
	public:
		MyFileAction(const Matcher& _matcher, vdirentry& _dirlist, const string& _rootDir, int _nMaxFilesToList)
			: nMaxFilesToList(_nMaxFilesToList), rootDir(_rootDir), dirlist(_dirlist), matcher(_matcher) {
		}
		~MyFileAction(void) {
		}

		                            
		virtual void doAction(const string& stFilePath) {
			if( nMaxFilesToList > 0 && (int)dirlist.size() >= nMaxFilesToList ) return;
			if( IsDirectory(stFilePath) ) return;

			const string& stRelativePath = stFilePath.substr(rootDir.size());
			if( matcher.isMatch(stRelativePath) ) {
				direntry entry;
				entry.absolutePath = stFilePath;
				entry.relativePath = stRelativePath;
				dirlist.push_back(entry);
			}
		}

	private:
		const int nMaxFilesToList;
		const string rootDir;
		vdirentry& dirlist;
		const Matcher& matcher;
	};
}

void TemporaryStorageSource::getFilteredDirectoryListImpl(vdirentry& dirlist, int nMaxFilesToList) const
{
	const string& stFileFilter = getFileFilter();
	if ( stFileFilter.empty() )
		throw OperationEngineException("File filter is empty.");

	const string& rootDir = GetAbsoluteRootPathWithSlash();
	SimpleFileFilterFileMatcher matcher(stFileFilter, !useCaseInsensitiveFileFilter(), useRegExFileFilter());
	MyFileAction action(matcher, dirlist, rootDir, nMaxFilesToList);

	                                                                             
	AllFilesMatcher matchAll;
	FilesTraverser traverser(action, matchAll);
	traverser.traverseDirectory(rootDir, false);
}

vector<string> TemporaryStorageSource::getFileList() const {
	vdirentry dirlist;
	getFilteredDirectoryListImpl(dirlist, 0);

	vector<string> vFileList;
	vdirentry::const_iterator cit = dirlist.begin();
	for(; cit != dirlist.end(); ++cit ) {
		const direntry& entry = *cit;
		vFileList.push_back(entry.relativePath);
	}
	return vFileList;
}

void TemporaryStorageSource::getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const {

	try {
		vdirentry dirlist;
		getFilteredDirectoryListImpl(dirlist, 1);

		vdirentry::const_iterator cit = dirlist.begin();
		if ( cit != dirlist.end() ) {
			const direntry& entry = *cit;
			const string stTmpFile(entry.absolutePath);
			const string& stTmpDir = context.getLocalTmpDirForOperation();
			const string stLocalSourceFile(stTmpDir + konga::GetLeafPath(entry.relativePath));
			kCopyFile(stTmpFile, stLocalSourceFile, true);

			ProcessedFile file;
			file.setFileChangeTimeNow();
			file.setInHistory(false);
			file.setLocalSourceLocation(stLocalSourceFile);
			file.setRemoteSourceLocation(entry.relativePath);
			long lErr = 0;
			const long long size = GetFileSize(stLocalSourceFile, lErr);
			file.setSourceSize(size);
			file.setTransferedNow();
			files.add(file);
			return;
		}
	}
	catch( const std::exception& ex ) {
		throw OperationEngineException(string("Unexpected exception while getting the first source file: ") + ex.what());
	}
	catch( const CrException& ex ) {
		throw OperationEngineException(ex.GetErrorString());
	}

	                                                            
	throw OperationEngineException("No files match the file filter \"" + getFileFilter() + "\".");
}

void TemporaryStorageSource::doTransferImpl(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const {
	try {
		vdirentry dirlist;
		getFilteredDirectoryListImpl(dirlist, getFileListLimit(m_pDataElementHolder));

		const string& stTmpDir = context.getLocalTmpDirForOperation();
		vdirentry::const_iterator cit = dirlist.begin();
		int iFile = 0;
		const string stSection("Copying local storage file.");
		for(; cit != dirlist.end(); ++cit ) {
			const direntry& entry = *cit;

			                                                                                     
			const string stLocalSourceFile( stTmpDir + toString(++iFile) + "_" + entry.relativePath );
			CreateParentDirectory(stLocalSourceFile);

			progress.UpdateProgress(stSection, "Copying local file '" + entry.relativePath + "' (" + konga::toString(iFile) + " of " + konga::toString(dirlist.size()) + ")",
				getPercentDone(iFile, dirlist.size()), -1);
			kCopyFile(entry.absolutePath, stLocalSourceFile);

			ProcessedFile file;
			file.setFileChangeTimeNow();
			file.setInHistory(false);
			long lErr = 0;
			const long long size = GetFileSize(stLocalSourceFile, lErr);
			file.setSourceSize(size);
			file.setTransferedNow();

			file.setRemoteSourceLocation(entry.relativePath);
			file.setLocalSourceLocation(stLocalSourceFile);
			files.add(file);
		}
		progress.UpdateProgress(stSection, "Copied " + konga::toString(iFile) + " files.", 100, -1);
	}
	catch( const std::exception& ex ) {
		string sErr("Failed to match files in temporary storage using file filter '" + getFileFilter() + "'. ");
		sErr += ex.what();
		throw OperationEngineException(sErr);
	}
	catch( const CrException& ex ) {
		throw OperationEngineException(ex.GetErrorString(), "Transferring temporary storage source.");
	}
}

void TemporaryStorageSource::ValidateRelativeFileName(const string& stSomePath) const {
	if ( stSomePath.empty() ) {
		throw OperationEngineException("File name is empty.");
	}
	if ( stSomePath.find("\\\\") != string::npos ) {
		throw OperationEngineException("Invalid relative file name: \"" + stSomePath + "\". \"\\\\\" is not allowed in the path.");
	}
	if ( stSomePath == "." || stSomePath == ".." ) {
		throw OperationEngineException("Invalid relative file name: \"" + stSomePath + "\". Not a file name");
	}
	string stRelativePath(GetRelativePath() + "/" + stSomePath);

	konga::ReplaceCharWithChar(stRelativePath, '\\', '/');

	vector<string> atoms_tmp = konga::SplitString(stRelativePath, "/"), atoms;
	if ( atoms_tmp.empty() ) {
		throw OperationEngineException("Invalid relative file name: " + stRelativePath + ". It appears to be empty.");
	}
	if ( atoms_tmp.back().empty() ) {
		throw OperationEngineException("Invalid relative file name: " + stRelativePath + ". There is no file name available.");
	}
	atoms_tmp.pop_back();	                        

	                                                 
	for( vector<string>::const_iterator cit = atoms_tmp.begin(); cit != atoms_tmp.end(); ++cit ) {
		if ( cit->empty() ) continue;
		atoms.push_back(*cit);
	}
	if ( atoms.empty() ) return;

	atoms_tmp.clear();
	int level = 0;

	for( vector<string>::const_iterator cit2 = atoms.begin(); cit2 != atoms.end(); ++cit2 ) {
		const string& atom = *cit2;
		if ( atom == "." ) {
			                  
		}
		else if ( atom == ".." ) {
			--level;
		}
		else if ( atom.find("..") != string::npos ) {
			                                                 
			throw OperationEngineException("Invalid relative file name: " + stSomePath + ". Too many .. in the wrong places.");
		}
		else {
			++level;
		}

		if (level < 0 ) {
			throw OperationEngineException("Invalid relative file name: " + stRelativePath + ". Writing outside of the temporary storage area is not allowed.");
		}
	}
}

void TemporaryStorageSource::doPostTransformation(const OperationEngineContext& context, ProcessedFiles& files) const {

	const bool bDelete = deleteAfterProcessing();
	                                                
	const RenameFile renamer( getRenameFilePattern() );
	if ( bDelete || !renamer.empty() ) {
		const string& stRootPath = GetAbsoluteRootPathWithSlash();
		vector<ProcessedFile>& vFiles = files.getProcessedFilesByRef();
		for( vector<ProcessedFile>::iterator it = vFiles.begin();
			 it != vFiles.end();
			 ++it )
		{
			ProcessedFile& file = *it;
			if ( file.hasError() || file.isInHistory() )
				continue;

			                                                                                     
			const string& remoteRelativePath = file.getRemoteSourceLocation();
			if ( remoteRelativePath.empty() ) {
				continue;
			}
			try {
				const string stOldFilePath(stRootPath + remoteRelativePath);
				if ( bDelete ) {
					kDeleteFile(stOldFilePath);
				}
				else {
					const string stRelativePath( renamer.getRenamedFileName(GetLeafPath(remoteRelativePath)) );
					ValidateRelativeFileName(stRelativePath);

					const string stNewFilePath(stRootPath + stRelativePath);
					CreateParentDirectory(stNewFilePath);
					kMoveFile(stOldFilePath, stNewFilePath, true);
				}
			}
			catch( const CrException& ex ) {
				const string stError( bDelete ? "Failed to delete source file. " : "Failed to rename source file. " );
				file.addWarning(stError + ex.GetErrorString());
			}
			catch( const std::exception& e ) {
				file.addWarning("Failed to " + string(bDelete?"delete":"rename") + " source file '" + remoteRelativePath
					+ "'. Reason: " + string(e.what()));
			}
		}
	}
}

