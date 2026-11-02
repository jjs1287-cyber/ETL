/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/DatabaseTableNameMappings.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>
#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "FolderSpecialHandling.h"

using namespace konga;
using namespace konga::interchange_checkout;
using std::string;

namespace {
	const string stParentFolderIdColName("parentfolder_id");
	const string stEntityTypeIdColName("entitytype_id");
	const string stItemIdColName("item_id");
	const string stFolderIdColName("folder_id");
	const string stInterchangeProjectId("interchangeproject_id");
}

void FolderSpecialHandling::HandleColumn(vColumnValuesT& vColumnValues) {

	                                                                                                                      
	                                   
	NullableString& parentFolderId = GetColumnValueRef(stParentFolderIdColName, vColumnValues);
	if ( parentFolderId == 0 )
		throw CrException("Internal Error: " + stParentFolderIdColName + " is NULL.");

	const string& stValue = parentFolderId.str();
	long lParentFolderId = atol(stValue.c_str());

	                                                                                       
	if ( lParentFolderId == 0 ) {
		parentFolderId = "";
	}
	else {
		const string& stGuid = m_guidToIdMapper.GetGuid(lParentFolderId, FolderEntityId);
		if ( stGuid.empty() ) {
			UIBACKEND_TRACE("Can't find parent folder id: No GUID is defined for FolderId = " + stValue + ". Internal inconsistency ignored - item will be placed in root folder.");
			parentFolderId = "";
		}
		else {
			parentFolderId = stGuid;
		}
	}

	if ( m_bIsItem ) {

		                                                                     
		const NullableString& entityTypeId = GetColumnValueRef(stEntityTypeIdColName, vColumnValues);
		if ( entityTypeId == 0 )
			throw CrException("Internal Error: " + stEntityTypeIdColName + " is NULL.");
		const long lEntityTypeId = atol(entityTypeId.str().c_str());

		NullableString& itemId = GetColumnValueRef(stItemIdColName, vColumnValues);
		if ( itemId == 0 )
			throw CrException("Internal Error: " + stItemIdColName + " is NULL. Bad data in the backend database");
		const long lItemId = atol(itemId.str().c_str());
		const string& stGuid = m_guidToIdMapper.GetGuid(lItemId, lEntityTypeId);
		if ( stGuid.empty() )
			throw CrException("Internal Error: No GUID defined for entity type '"
			+ GetEntityNameByEntityIdSingular(lEntityTypeId) + "' and ItemId = " + toString(lItemId) +
			" when translating GUIDs for a FolderItem. This is probably a bug or inconsistent data in the database.");

		itemId = stGuid;
	}
	else {
		                                  
		NullableString& folderId = GetColumnValueRef(stFolderIdColName, vColumnValues);
		if ( folderId == 0 )
			throw CrException("Internal Error: " + stFolderIdColName + " is NULL.");
		
		const long lFolderId = atol(folderId.str().c_str());
		const string& stGuid = m_guidToIdMapper.GetGuid(lFolderId, FolderEntityId);
		if ( stGuid.empty() )
			throw CrException("Internal Error: No GUID defined for Folder with Folder_ID = " + toString(lFolderId) + ".");

		folderId = stGuid;

		                                                                   
		if ( m_bHasInterchangeProject ) {
			try {
				NullableString& ipId = GetColumnValueRef(stInterchangeProjectId, vColumnValues);
				if ( ipId != 0 ) {
					const long id = atol(ipId.str().c_str());
					const string stGuid = m_guidToIdMapper.GetGuid(id, InterchangeProjectEntityId);
					if ( stGuid.empty() )
						throw CrException("Internal Error: Failed to find GUID for InterchangeProject with Id = " + toString(id));
					ipId = stGuid;
				}
			}
			catch( const CrException& ) {
				                                                                
				m_bHasInterchangeProject = false;
			}
		}
	}
}

