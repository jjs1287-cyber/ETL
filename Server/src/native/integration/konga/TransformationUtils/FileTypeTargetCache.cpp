#include <konga/OperationObjects/EndpointCompression.h>
#include <konga/CharacterEncodingConversion/Utf8TextInput.h>
/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/util.h>
#include <konga/Utils/ErrorResolver.h>

#ifndef WIN32
#include <konga/Utils/PortDefs.h>	                                         
#endif

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/ItemAccess/UserAccess.h>

#include <konga/DataElement/DataElement.h>

#include <konga/OperationObjects/LocationCreatorDb.h>
#include <konga/OperationObjects/FileTypeTargetLocation.h>
#include <konga/OperationObjects/ProcessedFiles.h>
#include <konga/OperationObjects/OperationEngineException.h>

#include <konga/OperationContext/DefaultOperationDataElementHolder.h>

#include "TranDBUtils.h"
#include "FileTypeTargetCache.h"

using namespace konga;
using std::string;
using std::map;

TargetKey::TargetKey(long lTargetId,
					 const string& sOverrideTargetName) :
m_lTargetId(lTargetId), m_sOverrideTargetName(sOverrideTargetName)
{
}

long TargetBuffer::getTargetId() const {
	return m_target.getTargetId();
}

TargetBuffer::TargetBuffer(const UserInfo& currentUser, FileTypeTargetLocation& targetLocation, const string& sFileName) :
m_pBuffer(0), m_target(targetLocation), m_sFileName(sFileName), m_CurrentUser(currentUser)
{
	                                                     
	string sTmpFile(getTmpDir());
	sTmpFile += "CachedTargets";
	kCreateDirectory(sTmpFile);
	AppendPathSeparator(sTmpFile);
	sTmpFile += "T_" + toString(targetLocation.getTargetId());
	kCreateDirectory(sTmpFile);
	AppendPathSeparator(sTmpFile);
	sTmpFile += GetGUIDString();
	m_sLocalTargetFile = sTmpFile;
}

TargetBuffer::~TargetBuffer(void) {
	if ( m_pBuffer ) {
		fclose(m_pBuffer);
		m_pBuffer = 0;
	}

	                                          
	if ( FileExists(m_sLocalTargetFile) ) {
		try {
			kDeleteFile(m_sLocalTargetFile);
		}
		catch(...) {
			                       
		}
	}
}

namespace {
	string CustomizeFopenError(long lErr, const string& header) {
		string err;
		if ( lErr == 0 ) {
			return header + "The OS did not report any error. Maybe too many file buffers have been opened?";
		}
		return header + ErrorResolver(lErr).Resolve(lErr);
	}
}

void TargetBuffer::WriteToBuffer(const DataElement& contents) {
	boost::mutex::scoped_lock lock(m_AccessMutex);
	int err;
	KString text;
	if(contents.getType()!=DataElement::t_binary) {
		text=contents.GetString(err);
		try {etlforge::requireUtf8Text((const char*)text,text.GetLength());}
		catch(const std::exception& failure){throw CrException(failure.what());}
	}
	if ( !m_pBuffer ) {
		                                    
		m_pBuffer = fopen(m_sLocalTargetFile.c_str(), "wb");
		if ( m_pBuffer == 0 ) {
			const long lErr = GetLastError();
			throw CrException(CustomizeFopenError(lErr, "Failed to open local file '" + m_sLocalTargetFile + "' when writing to buffer: "));
		}
	}
	if ( contents.getType() == DataElement::t_binary ) {
		konga::BinaryData* bd = contents.GetBinaryDataPointer(err);
		unsigned long size = 0;
		const char* ptr = bd->GetPointerToData(size);
		if ( ptr != 0 && size > 0 ) {
			if ( fwrite(ptr, 1, size, m_pBuffer) < size ) {
				long lErr = GetLastError();
				throw CrException(CustomizeFopenError(lErr, "Failed to write binary data to file '" + m_sLocalTargetFile + "' when writing to buffer: "));
			}
		}
	}
	else {
		const KString& sContents = text;
		const char* ptr = (const char*)sContents;
		size_t size = sContents.GetLength();
		std::string normalized;
		if(_ftelli64(m_pBuffer)==0 && size>=5 && std::string(ptr,5)=="<?xml") {
			normalized.assign(ptr,size);etlforge::xmlDeclarationToUtf8(normalized);
			ptr=normalized.data();size=normalized.size();
		}
		if ( ptr != 0 && size > 0 ) {
			if ( fwrite(ptr, 1, size, m_pBuffer) < size ) {
				long lErr = GetLastError();
				throw CrException(CustomizeFopenError(lErr, "Failed to write character data to file '" + m_sLocalTargetFile + "' when writing to buffer: "));
			}
		}
	}
}

void TargetBuffer::FlushBuffer() {
	boost::mutex::scoped_lock lock(m_AccessMutex);
	if ( !m_pBuffer )
		return;

	fclose(m_pBuffer);
	m_pBuffer = 0;

	long lErr = 0;
	long long fileSize = GetFileSize(m_sLocalTargetFile, lErr);
	if ( lErr ) fileSize = 0;

	ProcessedFile file;
	file.setFileChangeTimeNow();
	file.setInHistory(false);
	file.setLocalTargetLocation(m_sLocalTargetFile);
	file.setTargetSize(fileSize);
	file.setTransferedNow();

	ProcessedFiles files;
	files.add(file);

	try {
		DefaultOperationDataElementHolder dh(m_CurrentUser);
		m_target.setOverrideFileName(m_sFileName);
		compressEndpointFiles(m_target,files);
        m_target.doTransfer(dh, files);
	}
	catch( const OperationEngineException& e ) {
		throw CrException("Failed to write to the target \"" + m_target.getTargetName()
			+ "\".\nReason: " + e.GetErrorString());
	}
}

FileTypeTargetCache::~FileTypeTargetCache(void) {
	                                            
	for( TargetBufferT::iterator it = m_TargetBuffers.begin(); it != m_TargetBuffers.end(); ++it )
	{
		delete it->second;
	}
	m_TargetBuffers.clear();
	
	for( CachedTargetsT::iterator it2 = m_CachedTargets.begin(); it2 != m_CachedTargets.end(); ++it2 )
	{
		delete it2->second;
	}
	m_CachedTargets.clear();
}

void FileTypeTargetCache::WriteToBuffer(const UserInfo& currentUser,
										const string& stTargetId,
										const string& sOverrideTargetName,
										OperationDataElementHolder* pDEHolder,
										const DataElement& contents)
{
	FileTypeTargetLocation* pTargetLocation = 0;
	TargetBuffer* pTargetBuffer = 0;
	{
	boost::mutex::scoped_lock cacheLock(m_CacheMutex);
	                                 
	pTargetLocation = getTargetLocation(currentUser, stTargetId, pDEHolder);
	pTargetBuffer = getTargetBuffer(currentUser, *pTargetLocation, sOverrideTargetName);
	}                              

	pTargetBuffer->WriteToBuffer(contents);
}

void FileTypeTargetCache::FlushBuffer(const UserInfo& currentUser,
									  const string& stTargetId,
									  const string& sOverrideTargetName)
{
	TargetBuffer* pTargetBuffer = 0;
	{
	boost::mutex::scoped_lock cacheLock(m_CacheMutex);
	FileTypeTargetLocation* const pTargetLocation = getTargetLocation(currentUser, stTargetId, 0, false);
	if ( pTargetLocation == 0 )
		return;	                                               

	pTargetBuffer = getTargetBuffer(currentUser, *pTargetLocation, sOverrideTargetName, false);
	if ( pTargetBuffer == 0 )
		return;	                                                        
	}                              

	pTargetBuffer->FlushBuffer();
}

void FileTypeTargetCache::FlushAll(const UserInfo& currentUser, const string& stTargetId) {
	boost::mutex::scoped_lock cacheLock(m_CacheMutex);
	FileTypeTargetLocation* pTargetLocation = 0;
	long lTargetId = -1;
	if ( !stTargetId.empty() ) {
		pTargetLocation = getTargetLocation(currentUser, stTargetId, 0, false);
		if ( pTargetLocation == 0 )
			return;	                                               
		lTargetId = pTargetLocation->getTargetId();
	}

	for( TargetBufferT::iterator it = m_TargetBuffers.begin();
		it != m_TargetBuffers.end(); ++it )
	{
		if ( pTargetLocation == 0 || it->second->getTargetId() == lTargetId ) {
			it->second->FlushBuffer();
		}
	}
}

FileTypeTargetLocation* FileTypeTargetCache::getTargetLocation(const UserInfo& currentUser,
															   const string& stTargetId,
															   OperationDataElementHolder* pDEHolder,
															   bool bCreateNonExisting)
{
	CachedTargetsT::iterator it = m_CachedTargets.find(stTargetId);
	if ( it != m_CachedTargets.end() )
		return it->second;
	
	if ( !bCreateNonExisting )
		return 0;

	                                                              
	                                     
	if ( stTargetId.size() < 5 )
		throw InternalErrorException("Invalid target_id: " + stTargetId);

	const string stTag(stTargetId.substr(0, 2));
	if ( EqualsIgnoreCase(stTag, "s.") )
		throw CrException("Failed to write to target. A source was specified when only targets are allowed.");

	const string target_guid(stTargetId.substr(2));

	SmartDbPtr<DbConnection> pConn( konga::GetBackEndDbConnection("TranDb") );
	const long targetId = konga::getLocationId(stTargetId, *pConn);

	LocationCreatorDb creator;
	TargetDataLocation* pTarget = creator.getTargetLocation(targetId, *pConn, *pDEHolder, 0, 0);

	FileTypeTargetLocation* const pFileTarget = dynamic_cast<FileTypeTargetLocation*>(pTarget);
	if ( pFileTarget == 0 )
		throw CrException("The specified Target (\"" + pTarget->getTargetName()
		+ "\") is not a file type target. Only file-type targets can be used for writing to a file.");

	UserAccess userAccess(currentUser, *pConn);
	if ( !userAccess.HasExecutePermission(targetId, TargetEntityId) ) {
		throw CrException("The current user does not have execute permission on the target '"
			+ pFileTarget->getTargetName() + "'.");
	}

	m_CachedTargets.insert( CachedTargetsT::value_type(stTargetId, pFileTarget) );
	return pFileTarget;
}

TargetBuffer* FileTypeTargetCache::getTargetBuffer(const UserInfo& currentUser,
												   FileTypeTargetLocation& target,
												   const string& sOverrideTargetName,
												   bool bCreateNonExisting)
{
	target.setOverrideFileName(sOverrideTargetName);
	const string stFileName(target.getFileName());

	TargetKey key(target.getTargetId(), stFileName);
	TargetBufferT::iterator it = m_TargetBuffers.find(key);
	if ( it != m_TargetBuffers.end() )
		return it->second;

	if ( !bCreateNonExisting )
		return 0;

	TargetBuffer* const pTargetBuffer = new TargetBuffer(currentUser, target, stFileName);
	m_TargetBuffers.insert( TargetBufferT::value_type(key, pTargetBuffer) );
	return pTargetBuffer;
}

bool konga::operator<(const konga::TargetKey& lhs, const konga::TargetKey& rhs) {
	if ( lhs.getTargetId() != rhs.getTargetId() )
		return ( lhs.getTargetId() < rhs.getTargetId() );

	                                                     
	return ( lhs.getOverrideTargetName() < rhs.getOverrideTargetName() );
}
