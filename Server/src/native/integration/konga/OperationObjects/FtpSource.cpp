/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <math.h>	        

#include <sstream>		                    
#include <exception>	                 

#include <konga/Utils/SimpleFileFilterFileMatcher.h>
#include <konga/Utils/StringUtils.h>	             
#include <konga/Utils/FileUtils.h>		                

#include <konga/ItemProperties/Definitions.h>

#include <konga/SystemVariables/SystemVariables.h>

#include <konga/OperationLog/OperationProgress.h>

#include "OperationEngineException.h"
#include "OperationEngineContext.h"
#include "FtpSource.h"
#include "ProcessedFiles.h"
#include "RenameFile.h"

using namespace konga;
using std::string;
using std::vector;
using std::stringstream;
using std::endl;

FtpSource::FtpSource(void) {
}

FtpSource::~FtpSource(void) {
}

bool FtpSource::listFileNamesOnly() const {
	return m_extendedProperties.GetBool(FTP_SOURCE_PROPERTY_NLST, false);
}

string FtpSource::getFileFilter() const {
	const string& tmp = getOverrideFileFilter();
	if ( !tmp.empty() )
		return tmp;
	return replace_de( TrimString(TrimString(m_CommonData.m_stFileName, ' '), '/') );
}

void FtpSource::getFilteredFileList(Ftp& ftp, vdirentry& filelist) const
{
	const string& stFileFilter = getFileFilter();
	if ( stFileFilter.empty() )
		throw OperationEngineException("File filter is empty.");

	vdirentry vFullFileList;
	getFileListImpl(ftp, vFullFileList, listFileNamesOnly());

	SimpleFileFilterFileMatcher matcher(stFileFilter, !useCaseInsensitiveFileFilter(), useRegExFileFilter());
	vdirentry::const_iterator cit = vFullFileList.begin();
	for(; cit != vFullFileList.end(); ++cit ) {
		const direntry& entry = *cit;
		if ( matcher.isMatch(entry.name) ) {
			filelist.push_back(entry);
		}
	}
}

vector<string> FtpSource::getFileList() const {
	Ftp ftp;
	initFtp(ftp);
	vdirentry filelist;
	getFilteredFileList(ftp, filelist);
	
	vector<string> vFileList;
	vdirentry::const_iterator cit = filelist.begin();
	for(; cit != filelist.end(); ++cit ) {
		vFileList.push_back(cit->name);
	}
	return vFileList;
}

long FtpSource::getTransferTimeOutSeconds() const {
	return konga::GetSystemVariableAsLong("etlforge.source.ftp.transfer_timeout", m_pDataElementHolder, 3600);
}

namespace {
	string GetUnescapedUrl(const Ftp& ftp, const string& stPath, const string& stName) {
		string url( ftp.isSftp() ? "sftp://" : "ftp://" );

		url += ftp.getHost();
		const long lPort = ftp.getPort();
		if ( lPort > 0 ) {
			url += ':';
			url += toString(lPort);
		}
		url += stPath;
		url += stName;
		return url;
	}
}

void FtpSource::getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const {
	Ftp ftp;
	const string& stPath = initFtp(ftp);

	vdirentry filelist;
	getFilteredFileList(ftp, filelist);

	try {
		const string& stTmpDir = context.getLocalTmpDirForOperation();
		vdirentry::const_iterator cit = filelist.begin();
		if ( cit != filelist.end() ) {
			const direntry& entry = *cit;
			const string& stUrl = GetUnescapedUrl(ftp, stPath, entry.name);

			ftp.setPath(stPath + entry.name);
			const string stLocalSourceFile(stTmpDir + entry.name);
			if ( ftp.GetAsFile(stLocalSourceFile) != Protocol::SUCCESS ) {
				stringstream sErr;
				sErr << "Failed to get \"" + ftp.getParsedUrl() + "\"." << endl;
				sErr << ftp.GetLastError();
				throw OperationEngineException(sErr.str());
			}

			ProcessedFile file;
			file.setFileChangeTimeNow();
			file.setInHistory(false);
			file.setLocalSourceLocation(stLocalSourceFile);
			file.setRemoteSourceLocation(stUrl);
			long lErr = 0;
			const long long size = GetFileSize(stLocalSourceFile, lErr);
			file.setSourceSize(size);
			file.setTransferedNow();
			files.add(file);
			return;
		}
	}
	catch( const std::exception& ex ) {
		stringstream sErr;
		sErr << "Failed to match files. The specified file pattern \"" << getFileFilter() << "\" is not valid. " << ex.what();
		throw OperationEngineException(sErr.str());
	}

	                                                 
	throw OperationEngineException("No files match the file filter \"" + getFileFilter() + "\".");
}

void FtpSource::doTransferImpl(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const {

	Ftp ftp;
	const string& stPath = initFtp(ftp);

	try {
		const string stSection("Fetching ftp source files");
		progress.UpdateProgress(stSection, "Fetching ftp directory listing using file filter " + getFileFilter(), 0, -1);

		vector<Ftp::FtpDirectoryListEntry> filelist;
		getFilteredFileList(ftp, filelist);

		const string& stTmpDir = context.getLocalTmpDirForOperation();
		vdirentry::const_iterator it = filelist.begin();
		const int maxFilesToGet = getFileListLimit(m_pDataElementHolder);
		const int progressFiles = ( maxFilesToGet > 0 && filelist.size() > maxFilesToGet  ? maxFilesToGet : filelist.size() );
		int iFile = 0;
		for(; it != filelist.end(); ++it ) {
			if ( maxFilesToGet > 0 && iFile >= maxFilesToGet )
				break;

			const Ftp::FtpDirectoryListEntry& entry = *it;
			const string& stUrl = GetUnescapedUrl(ftp, stPath, entry.name);
			int iTimeOrYear = entry.seconds;
			if ( iTimeOrYear > 0 ) {
				double d = iTimeOrYear;
				double hours = floor(d/3600);
				d = floor(d - hours*3600);	                                 
				double minutes = floor(d/60);
				double seconds = floor(d - minutes*60);
				iTimeOrYear = static_cast<int>(floor(hours*10000 + minutes*100 + seconds));
			}
			else
				iTimeOrYear = entry.year;

			ProcessedFile file(stUrl, entry.size, entry.month, entry.day, iTimeOrYear);
			if ( context.isAlreadyTransferred(file) ) {
				                                                                
				files.add(file);
				continue;
			}

			ftp.setPath(stPath + entry.name);
			const string stLocalFile( stTmpDir + toString(++iFile) + "_" + entry.name );
			progress.UpdateProgress(stSection, "Fetching file " + stUrl + " (" + toString(iFile) + " of " + toString(progressFiles) + ")",
				(iFile-1)/progressFiles, -1);
			if ( ftp.GetAsFile(stLocalFile) != Protocol::SUCCESS ) {
				stringstream sErr;
				sErr << "Failed to get \"" + ftp.getParsedUrl() + "\"." << endl;
				sErr << ftp.GetLastError();
				throw OperationEngineException(sErr.str());
			}
			file.setLocalSourceLocation(stLocalFile);
			files.add(file);
		}

		progress.UpdateProgress(stSection, "Fetched " + toString(iFile) + " files.", 100, 0);
	}
	catch( const std::exception& ex ) {
		stringstream sErr;
		sErr << "Failed to match files. The specified file pattern \"" << getFileFilter() << "\" is not valid. " << ex.what();
		throw OperationEngineException(sErr.str());
	}
	catch( const CrException& ex ) {
		throw OperationEngineException(ex.GetErrorString());
	}
}

void FtpSource::doPostTransformation(const OperationEngineContext& context, ProcessedFiles& files) const {

	files.clearSourceProcessedFlags();
	                                                
	const bool bDelete = deleteAfterProcessing();
	const RenameFile renamer( getRenameFilePattern() );
	if ( bDelete || !renamer.empty() ) {
		Ftp ftp;
		initFtp(ftp);

		vector<ProcessedFile>& vFiles = files.getProcessedFilesByRef();
		for( vector<ProcessedFile>::iterator it = vFiles.begin(); it != vFiles.end(); ++it ) {
			ProcessedFile& file = *it;
			if ( file.hasError() || file.isInHistory() )
				continue;

			if ( files.hasSourceFileBeenProcessed(file) )
				continue;

			                                                                                     
			try {
				if ( bDelete ) {
					string stUrl(file.getRemoteSourceLocation());
					if ( stUrl.empty() )
						continue;

					string::size_type ipos = stUrl.find_last_of("/");
					if ( ipos == string::npos )
						throw OperationEngineException("Cannot delete ftp file at url " + stUrl + " since it seems to be invalid.");
					else if ( ipos == stUrl.size()-1 )
						throw OperationEngineException("Cannot delete ftp file at url " + stUrl + " since it seems to be a directory.");

					const string& stFileName = stUrl.substr(ipos+1);
					stUrl = stUrl.substr(0, ipos+1);
					konga::UrlEscape(stUrl, false, false);
					ftp.SetUrl(stUrl);

					if ( ftp.Delete(stFileName) != Protocol::SUCCESS ) {
						throw OperationEngineException("Failed to delete source file. " + ftp.GetLastError());
					}
				}
				else {
					const string& stOldName = file.getRemoteSourceLeafName();
					if ( stOldName.empty() )
						continue;

					const string& stNewName = renamer.getRenamedFileName(stOldName);
					if ( ftp.RenameFile(stOldName, stNewName) != Protocol::SUCCESS ) {
						stringstream sErr;
						sErr << "Failed to rename '" + stOldName << "' to '" << stNewName
							<< "' for the ftp address " << ftp.getParsedUrl() << "." << endl;
						sErr << ftp.GetLastError();
						throw OperationEngineException(sErr.str());
					}
				}
			}
			catch ( const CrException& e ) {
				file.addWarning(e.GetErrorString());
			}
			catch( const std::exception& e ) {
				file.addWarning("Failed to " + string(bDelete?"delete":"rename") + " source file '" + file.getRemoteSourceLocation()
					+ "'. Reason: " + string(e.what()));
			}
			files.flagSourceFileAsProcessed(file);
		}
	}
}

