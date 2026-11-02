/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <string>

#ifdef WIN32
	#include <windows.h>
#endif

#include <konga/Utils/CrException.h>
#include <konga/Utils/DbTypeUtils.h>	                              
#include <konga/Utils/FileUtils.h>		                                     
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/Settings.h>		                           
#include <konga/Utils/util.h>			                  

#include <konga/Zip/ZipUtils.h>			                              
#include <konga/Crypto/Crypto.h>		        

#include <konga/Db/DbUtils/DbUtils.h>		               
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>	                       
#include <konga/Db/DbUtils/SmartDbPtr.h>			               

#include "FileUploadUtils.h"
#include "UploadedFilesManager.h"

using namespace konga;
using namespace std;
using namespace boost;

namespace {

	                                                                                                                  
	                                                                                         
	                               
	string CreateUniqueSubDir(const std::string& stPath) {
		string sTmpDir( getTmpDir() );
		AppendPathSeparator(sTmpDir);
		sTmpDir += GetGUIDString();
		kCreateDirectory(sTmpDir);
		AppendPathSeparator(sTmpDir);
		return sTmpDir;
	}

	                                                                   
	                                                                       
	                      
	void CreateUserFilesDirectory(const string& stBaseDirectory, const string& stPathRelativeBaseDirectory) {
		try {
			kCreateDirectory(stBaseDirectory + stPathRelativeBaseDirectory);
		}
		catch( const CrException& ex ) {
			throw CrException("Possible server configuration error. Failed to create the directory \""
				+ stBaseDirectory + stPathRelativeBaseDirectory + "\". Make sure the directory \"" +
				stBaseDirectory + "\" exists and is writable by the current user. The system error was: " + ex.GetErrorString());
		}
	}

}                       

  
                       
                                                                                     
           
                   
                                                
                                                                                           
                                         
                                                    
  
void UploadedFilesManager::AddFile(const ZippedUploadedFile& file) {
	if( m_dbConnection.GetTransactionIsolationLevel() != SERIALIZABLE )
		throw CrException("Adding files to uploaded files manager, must be done in a serializable transaction.");
	
	PlaceFileOnDisk(file);
	DoAddFile(file);
}

void UploadedFilesManager::DoAddFile(const ZippedUploadedFile& file) {
	long lFileId = GetNextId("UploadedFilesTab", "FileId", m_dbConnection);

	string sSql = "INSERT INTO UploadedFilesTab VALUES(" +
		toString(lFileId) + ", " + 
		toString(file.GetFileType()) + ", '" +  
		file.GetGuid() + "', '" +
		file.GetCanonicalPath() + "', '" +
		file.GetName() + "', '" +
		file.GetMd5 () + "', '" +
		GetISO8601TimeStampString(file.GetModifiedDate()) + "')";

	m_dbConnection.ExecuteSql(sSql);
}

void UploadedFilesManager::UpdateFile(const ZippedUploadedFile& file) {
	if( m_dbConnection.GetTransactionIsolationLevel() != SERIALIZABLE )
		throw CrException("Updating files to uploaded files manager, must be done in a serializable transaction.");
	
	PlaceFileOnDisk(file);
	DoUpdateFile(file);
}

  
                                                                   
                                
  
void UploadedFilesManager::DoUpdateFile(const ZippedUploadedFile& file) {
	string sSql = "UPDATE UploadedFilesTab Set FileTypeId=" +
		toString(file.GetFileType()) + ", Guid='" +  
		file.GetGuid() + "', Path='" +
		file.GetCanonicalPath() + "', FileName='" +
		file.GetName() + "', MD5='" +
		file.GetMd5 () + "', ModifiedDate='" +
		GetISO8601TimeStampString(file.GetModifiedDate()) + "' WHERE " +
		"Guid='" + SqlEscape(file.GetGuid()) + "' OR (Path='" +
		SqlEscape(file.GetCanonicalPath()) + "' AND FileName='" + SqlEscape(file.GetName()) + "')";

	m_dbConnection.ExecuteSql(sSql);
}

void UploadedFilesManager::PlaceFileOnDisk(const ZippedUploadedFile& file) {
	                                               
	string sTmpDir( CreateUniqueSubDir(getTmpDir()) );
	AutoCleanupTempDir adopt_sTmpDir(sTmpDir);                                                        

	VerifyPathForUploadedFile(file.GetPath());
	VerifyPathForUploadedFile(file.GetName());

	string sUnzippedFileName = sTmpDir + file.GetName();
	string sMd5( ZipUtils::DecodeUnzipAndWriteToFile(sUnzippedFileName, file.GetZippedBase64Contents()) );

	                  
	if( sMd5 != file.GetMd5() ) {
		throw CrException("MD5 hash value mismatch for uploaded file. Claims to be '" + file.GetMd5() + 
			"', but really is '" + sMd5 + "'.");
	}

	CreateUserFilesDirectory(GetServerHomeWithSlash() + GetUploadedFilesDir(), file.GetCanonicalPath());
	kMoveFile(sUnzippedFileName, file.GetFullAbsolutePath(), true);
}

                                                                     
                                                                              
string UploadedFilesManager::GetFile(ZippedUploadedFile& fileToReturn, char*& pZippedAndBase64) const {
	                                                     
	                                    
	                                                 
	UploadedFile fileFromDb;
	                                                                    
	if( !ExistsFile(fileToReturn, fileFromDb, false) )
		throw CrException("Could not find an uploaded file with the file name '" +
			fileToReturn.GetFullAbsolutePath() + "'.");

	fileToReturn = fileFromDb;
	return ZipUtils::ZipAndBase64EncodeFile(fileToReturn.GetFullAbsolutePath(), pZippedAndBase64);
}

void UploadedFilesManager::RemoveFile(const UploadedFile& file) {
	if( m_dbConnection.GetTransactionIsolationLevel() != SERIALIZABLE )
		throw CrException("Removing files from uploaded files manager, must be done in a serializable transaction.");

	string sSql = "DELETE FROM UploadedFilesTab WHERE Guid = '" + SqlEscape(file.GetGuid()) + 
		"' OR (Path = '" + SqlEscape(file.GetCanonicalPath()) + "' AND FileName = '" + SqlEscape(file.GetName()) + "')";
	m_dbConnection.ExecuteSql(sSql);

	kDeleteFile(file.GetFullAbsolutePath());
}

bool UploadedFilesManager::ExistsFile(const UploadedFile& incomingFile, UploadedFile& existingFile,
									  bool bHasToExistOnDisk) const {
	string sSql = "SELECT * FROM UploadedFilesTab WHERE Guid = '" + SqlEscape(incomingFile.GetGuid()) + 
		"' OR (Path = '" + SqlEscape(incomingFile.GetCanonicalPath())
		+ "' AND FileName = '" + SqlEscape(incomingFile.GetName()) + "')";

	SmartDbPtr<DynamicDbRecordSet> pRecordSet(DynamicDbRecordSet::MakeInstance(&m_dbConnection, sSql));

	pRecordSet->Open();
	if( pRecordSet->Next() ) {
		UploadedFileType type = static_cast<UploadedFileType>(atoi(pRecordSet->GetFieldValue("FileTypeId").c_str()));
		existingFile.SetFileType(type);
		existingFile.SetGuid(pRecordSet->GetFieldValue("Guid"));
		existingFile.SetPath(pRecordSet->GetFieldValue("Path"));
		existingFile.SetName(pRecordSet->GetFieldValue("FileName"));
		existingFile.SetMd5(pRecordSet->GetFieldValue("MD5"));
		existingFile.SetModifiedDate(pRecordSet->GetFieldValueAsTimeStampStruct(
			pRecordSet->GetFieldIndexByName("ModifiedDate")));


		if ( !existingFile.FileExistsOnDisk() ) {
			if ( bHasToExistOnDisk )
				return false;
			else {
				                                                                                             
				                                                                                              
				  
				existingFile.SetMd5( md5("") );
			}
		}
		return true;
	}
	return false;
}
