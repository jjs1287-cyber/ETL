#include <konga/OperationObjects/EndpointCompression.h>
/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <memory>
#include <konga/CharacterEncodingConversion/Utf8TextInput.h>
#include <konga/SystemVariables/SystemVariables.h>

#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/util.h>
#include <konga/Utils/BinaryDataFileIo.h>

#include <konga/Crypto/Crypto.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/ItemAccess/UserAccess.h>

#include <konga/OperationObjects/LocationCreatorDb.h>
#include <konga/OperationObjects/FileTypeSourceLocation.h>
#include <konga/OperationObjects/FileTypeTargetLocation.h>
#include <konga/OperationObjects/OperationEngineContext.h>
#include <konga/OperationObjects/ProcessedFiles.h>
#include <konga/OperationObjects/OperationEngineException.h>

#include "TranDBUtils.h"
#include "ReadFileImpl.h"

using namespace konga;
using std::string;

namespace {
	void ReadFileFromSourceToLocalFile(OperationEngineContext& context,
		const string& sPrefixedSourceGUID,
		OperationDataElementHolder* pDEHolder,
		const string& sOverrideFileFilter,
		const UserInfo& currentUser,
		string& stFileName)
	{
		if ( sPrefixedSourceGUID.size() < 5 )
			throw CrException("Internal Error: Invalid source_id: " + sPrefixedSourceGUID);

		if ( sPrefixedSourceGUID.substr(0,2) == "t." )
			throw CrException("Cannot read from a target, please specify a source as your source_id");

		SmartDbPtr<DbConnection> pConn( konga::GetBackEndDbConnection("TranDb") );
		const long sourceId = getLocationId(sPrefixedSourceGUID, *pConn);

		LocationCreatorDb creator;
		std::auto_ptr<SourceDataLocation> pSource( creator.getSourceLocation(sourceId, *pConn, *pDEHolder, 0, false) );
		
		FileTypeSourceLocation* pFileSource = dynamic_cast<FileTypeSourceLocation*>(pSource.get());
		if ( pFileSource == 0 ) {
			throw CrException("The specified source (\"" + pSource->getSourceName()
			+ "\") is not a file type source. Only file-type sources can be used for reading a file.");
		}

		if ( !konga::UserHasExecutePermission(currentUser.GetId(), sourceId, SourceEntityId) ) {
			throw CrException("The current user does not have execute permission on the source '"
				+ pFileSource->getSourceName() + "'.");
		}

		try {
			ProcessedFiles files;
			if ( !sOverrideFileFilter.empty() )
				pFileSource->setOverrideFileFilter(sOverrideFileFilter);

			pFileSource->getFirstSourceFile(context, files);
            expandEndpointArchives(*pFileSource, files);
            if(files.getProcessedFilesByRef().size()>1)throw OperationEngineException("ReadFile requires one archive member; use an operation source to process every member.");
			if ( files.empty() )
				throw CrException("No source files were found, please use a different file filter.");

			stFileName = files.getProcessedFilesByRef().at(0).getLocalSourceLocation();
		}
		catch( const OperationEngineException& e ) {
			throw CrException("Failed to read file from the source \"" + pSource->getSourceName()
				+ "\".\nReason: " + e.GetErrorString());
		}
	}

}

void konga::ReadFromSource(const string& sPrefixedSourceGUID,
						   string& stContents,
						   OperationDataElementHolder* pDEHolder,
						   const string& sOverrideFileFilter,
						   const UserInfo& currentUser)
{
	string stFileName;
	OperationEngineContext opContext(currentUser, 0, *pDEHolder);
	ReadFileFromSourceToLocalFile(opContext, sPrefixedSourceGUID, pDEHolder, sOverrideFileFilter, currentUser, stFileName);

	                               
	stContents.clear();
	try {
		etlforge::Utf8TextInput input(stFileName, GetSystemVariable("etlforge.source.text.character_encoding",pDEHolder,""));
		read_file_to_string(input.path(), stContents);
		etlforge::xmlDeclarationToUtf8(stContents);
	} catch(const std::exception& failure) { throw CrException(failure.what()); }
}

void konga::ReadFromSourceAndBase64Encode(const string& sPrefixedSourceGUID,
								   string& stBase64EncodedContents,
								   OperationDataElementHolder* pDEHolder,
								   const string& sOverrideFileFilter,
								   const UserInfo& currentUser)
{
	string stFileName;
	OperationEngineContext opContext(currentUser, 0, *pDEHolder);
	ReadFileFromSourceToLocalFile(opContext, sPrefixedSourceGUID, pDEHolder, sOverrideFileFilter, currentUser, stFileName);

	                                          
	stBase64EncodedContents.clear();
	BinaryData data;
	ReadBinaryDataFromFile(data, stFileName);

	unsigned long size = 0;
	const unsigned char* pc = reinterpret_cast<const unsigned char*>( data.GetPointerToData(size) );
	stBase64EncodedContents = konga::EncodeBase64(const_cast<unsigned char*>(pc), size);
}

void konga::GetFileNameListFromSource(const string& sPrefixedSourceGUID,
						OperationDataElementHolder* pDEHolder,
						const UserInfo& currentUser,
						std::vector<std::string>&list)
{
	list.clear();
	if ( sPrefixedSourceGUID.size() < 5 )
		throw CrException("Internal Error: Invalid source_id: " + sPrefixedSourceGUID);

	if ( sPrefixedSourceGUID.substr(0,2) == "s." ){
		SmartDbPtr<DbConnection> pConn( konga::GetBackEndDbConnection("TranDb") );
		const long sourceId = getLocationId(sPrefixedSourceGUID, *pConn);
		LocationCreatorDb creator;
		std::auto_ptr<SourceDataLocation> pSource( creator.getSourceLocation(sourceId, *pConn, *pDEHolder, 0, false) );
		
		FileTypeSourceLocation* pFileSource = dynamic_cast<FileTypeSourceLocation*>(pSource.get());
		if ( pFileSource == 0 ) {
			throw CrException("The specified source (\"" + pSource->getSourceName()
			+ "\") is not a file type source. Only file-type sources can be used for listing filenames.");
		}
		if ( !konga::UserHasExecutePermission(currentUser.GetId(), sourceId, SourceEntityId) ) {
			throw CrException("The current user does not have execute permission on the source '"
				+ pFileSource->getSourceName() + "'.");
		}

		try{
			list=pFileSource->getFileList();
		}
		catch( const OperationEngineException& e ) {
			throw CrException("Failed to get the list of filenames from the source \"" + pSource->getSourceName()
				+ "\".\nReason: " + e.GetErrorString());
		}
	}
	else {
		throw CrException("Only file-type sources can be used for listing filenames.");
	}
}

