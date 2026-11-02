/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <sstream>	               

#include <konga/Utils/StringUtils.h>	             

#include <konga/SystemVariables/SystemVariables.h>

#include "ProcessedFiles.h"
#include "OperationEngineException.h"
#include "FtpTarget.h"
#include "RenameFile.h"

using namespace std;
using namespace konga;

FtpTarget::FtpTarget(void) {
}

FtpTarget::~FtpTarget(void) {
}

std::string FtpTarget::getFileName() const {
	const string& tmp = getOverrideFileName();
	if ( !tmp.empty() )
		return tmp;
	return replace_de( TrimString(m_CommonData.m_stFileName, '/') );
}

long FtpTarget::getTransferTimeOutSeconds() const {
	return konga::GetSystemVariableAsLong("etlforge.target.ftp.transfer_timeout", m_pDataElementHolder, 3600);
}

bool FtpTarget::disableRename() const {
	return ( m_CommonData.m_lSecured == 1 ||
			 getFtpSecurityOption() == FTP_SECURITY_SFTP );
}

void FtpTarget::doTransfer(const OperationDataElementHolder& deHolder, ProcessedFiles& files) const {
	try {
		Ftp ftp;
		const string& stPath = initFtp(ftp);

		RenameFile renamer(getFileName());
		vector<ProcessedFile>& transfered_files = files.getProcessedFilesByRef();
		for( vector<ProcessedFile>::iterator it = transfered_files.begin();
			 it != transfered_files.end(); ++it ) {
			ProcessedFile& file = *it;
			if ( doNotTransfer(file) )
				continue;

			const string& stRemoteName = getRenamedRemoteName(renamer, file);
			string stRemoteNameUploaded(stRemoteName);

			if ( disableRename() ) {
				ftp.setFtpUploadRenameFileNames("", "");
			}
			else {
				stRemoteNameUploaded += "-jbupload";
				ftp.setFtpUploadRenameFileNames(stRemoteNameUploaded, stRemoteName);
			}

			const string& stLocalFile = file.getLocalTargetLocation();

			ftp.setPath( stPath + stRemoteNameUploaded );
			if ( ftp.PutAsFile(stLocalFile) != Protocol::SUCCESS ) {
				std::stringstream sErr;
				sErr << "Failed to put the local file " << endl << endl << stLocalFile << endl << endl;
				sErr << "to the url " << endl << endl << ftp.getParsedUrl() <<endl << endl;
				sErr << ftp.GetLastError();
				throw OperationEngineException(sErr.str());
			}

			string url(ftp.getParsedUrl());
			  
                                                
                                                                           
                         
                        

                                                                                          
                            
                                                                                                                                                                    
                                      
                                                
     
                                                             
                                 
                                                                  
     
    
     
			file.setRemoteTargetLocation(url);
		}
	}
	catch( const CrException& ex ) {
		throw OperationEngineException(ex.GetErrorString());
	}
}

void FtpTarget::testFileTargetConnection(bool bCopyTestFile) const {
	Ftp ftp;
	initFtp(ftp);
	vdirentry filelist;
	getFileListImpl(ftp, filelist, false);
}

