/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <string>

#include <konga/Utils/KeyValues.h>
#include <konga/Utils/OdbcConnectionStringUtils.h>
#include <konga/Utils/StringUtils.h>		           

#include <konga/Utils/StatusReceiver.h>

#include <konga/Crypto/Crypto.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/Db/TranDb/SourcesTab.h>
#include <konga/Db/TranDb/TargetsTab.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/OperationContext/DefaultOperationDataElementHolder.h>

#include <konga/OperationObjects/OperationObjectCreatorKeyValues.h>
#include <konga/OperationObjects/FileTypeSourceLocation.h>
#include <konga/OperationObjects/FileTypeTargetLocation.h>
#include <konga/OperationObjects/DatabaseLocation.h>
#include <konga/OperationObjects/LdapLocation.h>

#include <konga/ItemProperties/Definitions.h>

#include <konga/UiBackend/GuidMappings/GuidMappings.h>
#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include <konga/UiBackend/WebServices/Utils/WsAllocations.h>
#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "TestConnectionImpl.h"

using namespace konga;
using std::string;

TestConnectionImpl::TestConnectionImpl(const char* user, const char* password, bool bIsSource, const char* id)
: WsImplBaseAuthenticator(user, password), m_bIsSource(bIsSource), m_lId(0)
{
	if ( id == 0 )
		throw InternalErrorException("Bad Data: Id is NULL.");
	m_stId = id;
}

namespace {
	void fillKeyValues(KeyValues& kv, const xsd__string_Array& keys, const xsd__string_Array& values) {
		int iKeys = -1;
		int iVals = -1;
		const xsd__string* arKeys = keys.get(iKeys);
		const xsd__string* arVals = values.get(iVals);
		if ( iKeys != iVals )
			throw CrException("Bad Data: The keys and values arrays have different sizes.");

		kv.Clear();
		for( int i = 0; i < iKeys; ++i ) {
			if ( arKeys[i] &&  arVals[i] ) {
				kv.AddString(arKeys[i], arVals[i]);
				UIBACKEND_TRACE("Added key=" + string(arKeys[i]) + ", value=" + string(arVals[i]));
			}
		}
	}

	string getEncryptedPasswordFromDb(const long lId, bool bIsSource) {
		SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );

		if ( bIsSource ) {
			const string stSourceId(toString(lId));
			SmartDbPtr<TranDb::SourcesTab> pSourcesTab( TranDb::SourcesTab::MakeInstance(pTranDb) );
			pSourcesTab->Select("WHERE Source_ID = " + stSourceId);
			if ( pSourcesTab->Next() )
				return pSourcesTab->IsPasswordNull() ? "" : pSourcesTab->GetPassword();
			else
				throw CrException("No source with Source_ID = " + stSourceId + " has been defined.");
		}
		else {
			const string stTargetId(toString(lId));
			SmartDbPtr<TranDb::TargetsTab> pTargetsTab( TranDb::TargetsTab::MakeInstance(pTranDb) );
			pTargetsTab->Select("WHERE Target_ID = " + stTargetId);
			if ( pTargetsTab->Next() )
				return pTargetsTab->IsPasswordNull() ? "" : pTargetsTab->GetPassword();
			else
				throw CrException("No Target with Target_ID = " + stTargetId + " has been defined.");

		}
	}

	                                
	void checkConnectionInfo(const ConnectionInfo* pConnectionInfo) {
		if ( !pConnectionInfo )
			throw CrException("Bad Data: ConnectionInfo is NULL.");

		if ( !pConnectionInfo->Keys || !pConnectionInfo->KeyValues )
			throw CrException("Bad Data: Keys or KeyValues are null.");

		if ( !pConnectionInfo->Keys->item || !pConnectionInfo->KeyValues->item )
			throw CrException("Bad Data: Keys or KeyValues have no items.");
	}

	namespace {
		const string stPasswordKey("password");
		const string stConnectStringKey("connectstring");
		const string stIdKey("item_id");
	}

	                                                 
	void checkPassword(KeyValues& keyValues, bool bIsSource, const string& stId, bool& bClientPwdSent) {
		bClientPwdSent = true;
		                                      
		                                                                                       
		                                         
		if ( keyValues.IsKeyDefined(stPasswordKey) ) {
			                                           
			keyValues.AddString(stPasswordKey, EncryptString(keyValues.GetString(stPasswordKey)));
		}
		else {
			                                                                            
			if ( !stId.empty() ) {
				const long lId = GetItemIdForGuid(stId, bIsSource? SourceEntityId : TargetEntityId, false, GuidToIdMapper::invalid_id);
				if ( lId != GuidToIdMapper::invalid_id ) {
					keyValues.AddString(stPasswordKey, getEncryptedPasswordFromDb(lId, bIsSource));
					keyValues.AddString(stIdKey, toString(lId));
					bClientPwdSent = false;
				}
			}
		}
	}
}

FileList* TestConnectionImpl::TestFileTypeConnection(ConnectionInfo* pConnectionInfo) {
	checkConnectionInfo(pConnectionInfo);
	KeyValues keyValues;
	fillKeyValues(keyValues, *pConnectionInfo->Keys->item, *pConnectionInfo->KeyValues->item);
	bool bClientPwdSent = true;
	checkPassword(keyValues, m_bIsSource, m_stId, bClientPwdSent);

	vector<string> vFileList;
	OperationObjectCreatorKeyValues objectCreator;
	DefaultOperationDataElementHolder deHolder(m_UserInfo);
	if ( m_bIsSource ) {
		auto_ptr<FileTypeSourceLocation> pSource( objectCreator.getFileTypeSourceLocation(keyValues, deHolder) );
		if ( pSource.get() == 0 )
			throw CrException("Internal Error: getFileTypeSourceLocation returned NULL.");
		if ( !bClientPwdSent ) {
			if ( !pSource->userHasReadPermission(m_UserInfo) ) {
				throw CrException("The current user does not have read permission for this source.");
			}
		}
		vFileList = pSource->getFileList();
	}
	else {
		auto_ptr<FileTypeTargetLocation> pTarget( objectCreator.getFileTypeTargetLocation(keyValues, deHolder) );
		if ( pTarget.get() == 0 )
			throw CrException("Internal Error: getFileTypeTargetLocation returned NULL.");
		if ( !bClientPwdSent ) {
			if ( !pTarget->userHasReadPermission(m_UserInfo) ) {
				throw CrException("The current user does not have read permission for this target.");
			}
		}
		pTarget->testFileTargetConnection(false);
	}

	auto_ptr<FileList> pFileList( new FileList() );
	pFileList->Files = new ArrayOf_xsd_string();
	allocate_xsd__string_Array(vFileList, *pFileList->Files->item);
	return pFileList.release();
}

namespace {
	class DebugLogStatusReceiver : public StatusReceiver {
	public:
		DebugLogStatusReceiver(void) { }
		~DebugLogStatusReceiver(void) {}

		virtual void Status(const std::string& stStatus) {
			this->log(stStatus);
		}

		                                                                      
		                                        
		virtual void Progress(float prog) {
			              
		}

		                                                                    
		virtual void ReportError(const std::string& stError) {
			this->log("ERROR: " + stError);
		}

	private:
		void log(const string& msg) {
			UIBACKEND_TRACE(msg);
		}

	};
}

xsd__string TestConnectionImpl::TestDatabaseConnection(ConnectionInfo* pConnectionInfo) {
	checkConnectionInfo(pConnectionInfo);

	KeyValues keyValues;
	fillKeyValues(keyValues, *pConnectionInfo->Keys->item, *pConnectionInfo->KeyValues->item);
	bool bClientPwdSent = true;
	checkPassword(keyValues, m_bIsSource, m_stId, bClientPwdSent);

	OperationObjectCreatorKeyValues objectCreator;
	DefaultOperationDataElementHolder deHolder(m_UserInfo);
	auto_ptr<DatabaseLocation> pDbLoc( objectCreator.getDatabaseLocation(keyValues, deHolder, m_bIsSource) );

	if ( !bClientPwdSent ) {
		if ( m_bIsSource ) {
			SourceDataLocation* pSource = dynamic_cast<SourceDataLocation*>(pDbLoc.get());
			if ( pSource ) {
				if ( !pSource->userHasReadPermission(m_UserInfo) ) {
					throw CrException("The current user does not have read permission for this database source.");
				}
			}
		}
		else {
			TargetDataLocation* pTarget = dynamic_cast<TargetDataLocation*>(pDbLoc.get());
			if ( pTarget ) {
				if ( !pTarget->userHasReadPermission(m_UserInfo) ) {
					throw CrException("The current user does not have read permission for this database target.");
				}
			}
		}
	}

	DebugLogStatusReceiver sr;
	pDbLoc->testConnection(sr);
	return allocate_xsd__string("OK");	                                                              
}

xsd__string TestConnectionImpl::TestLdapConnection(ConnectionInfo* pConnectionInfo) {
	checkConnectionInfo(pConnectionInfo);

	KeyValues keyValues;
	fillKeyValues(keyValues, *pConnectionInfo->Keys->item, *pConnectionInfo->KeyValues->item);
	bool bClientPwdSent = true;
	checkPassword(keyValues, m_bIsSource, m_stId, bClientPwdSent);

	OperationObjectCreatorKeyValues objectCreator;
	DefaultOperationDataElementHolder deHolder(m_UserInfo);
	auto_ptr<LdapLocation> pLdapLoc( objectCreator.getLdapLocation(keyValues, deHolder, m_bIsSource) );

	if ( !bClientPwdSent ) {
		if ( m_bIsSource ) {
			SourceDataLocation* pSource = dynamic_cast<SourceDataLocation*>(pLdapLoc.get());
			if ( pSource ) {
				if ( !pSource->userHasReadPermission(m_UserInfo) ) {
					throw CrException("The current user does not have read permission for this LDAP source.");
				}
			}
		}
		else {
			TargetDataLocation* pTarget = dynamic_cast<TargetDataLocation*>(pLdapLoc.get());
			if ( pTarget ) {
				if ( !pTarget->userHasReadPermission(m_UserInfo) ) {
					throw CrException("The current user does not have read permission for this LDAP target.");
				}
			}
		}
	}
	pLdapLoc->testConnection();

	return allocate_xsd__string("OK");	                                                              
}

