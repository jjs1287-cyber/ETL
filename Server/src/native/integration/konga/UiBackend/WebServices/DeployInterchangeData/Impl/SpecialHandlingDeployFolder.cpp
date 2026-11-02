/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <vector>

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>	             

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/InterchangeProject/InterchangeProject.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "../InterchangeDataRow.hpp"
#include "SpecialHandlingDeployFolder.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::string;

namespace {
	const string stEmpty("");
}

void konga::interchange_deploy::DeleteFolderItem(int iEntityTypeId, long lItemId, const InterchangeProject& interchangeProject, DbConnection& dbConnection) {
	                                   
	                                                             
	                                                  
	                                                                             
	if (iEntityTypeId == InterchangeProjectEntityId) return;	                                    

	string stSql;
	const string& sTableName = getTableName(nameFolderItem);
	const string sSelectSql("SELECT FolderItem_ID, ParentFolder_ID FROM " + sTableName
		+ " WHERE EntityType_ID = " + toString(iEntityTypeId) +
		+ " AND Item_ID = " + toString(lItemId));
	SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&dbConnection, sSelectSql) );
	rs->Open();

	                                                                                                       
	                                       
	while (rs->Next() ) {
		const long lThisFolderItemId = atol(rs->GetFieldValue(0).c_str());
		const long lThisParentFolderId = atol(rs->GetFieldValue(1).c_str());
		                                                            
		if ( lThisParentFolderId != 0 && stSql.empty() ) {
			if ( interchangeProject.ItemBelongsToProject(FolderEntityId, lThisParentFolderId) ) {
				stSql = "DELETE FROM " + sTableName + " WHERE FolderItem_ID = " + toString(lThisFolderItemId);
				break;
			}
		}
	}

	if ( !stSql.empty() ) {
		UIBACKEND_TRACE("interchange_deploy::DeleteFolderItem - " + stSql);
		dbConnection.ExecuteSql(stSql);
	}
}

bool FolderItem::HandleRow(InterchangeDataRowStruct& row) {

	                       
	if( row.m_vFields.size() < 3 )
		throw CrException("Bad Data: To few columns given for FolderItems.");

	vFieldsT values = row.m_vFields;
	const string& stParentFolderId = values.at(m_iParentFolderIdIndex)==0? stEmpty : values.at(m_iParentFolderIdIndex).str();
	const string& stEntityTypeId = values.at(m_iEntityTypeIdIndex)==0? stEmpty : values.at(m_iEntityTypeIdIndex).str();
	const string& stItemId = values.at(m_iItemIdIndex)==0? stEmpty : values.at(m_iItemIdIndex).str();

	if ( stParentFolderId.empty() )
		throw CrException("Bad Data: ParentFolderId was NULL or empty for FolderItem.");
	long lParentFolderId = -1;
	if ( stParentFolderId == "0" )
		lParentFolderId = 0;
	else {
		lParentFolderId = m_guidToIdMapper.GetId(stParentFolderId, FolderEntityId);
		if ( lParentFolderId == GuidToIdMapper::invalid_id )
			throw CrException("There is no folder defined for FolderId = " + stParentFolderId);
	}

	if ( stEntityTypeId.empty() )
		throw CrException("Bad Data: EntityTypeId is NULL or empty for ParentFolderId = " + toString(lParentFolderId));
	const int iEntityTypeId = atoi(stEntityTypeId.c_str());

	if ( stItemId.empty() )
		throw CrException("Bad Data: ItemId is NULL or empty for ParentFolderId = " + toString(lParentFolderId) + " and EntityTypeId = " + stEntityTypeId);
	const long lItemId = m_guidToIdMapper.GetId(stItemId, iEntityTypeId);
	if ( lItemId == GuidToIdMapper::invalid_id )
		throw CrException("Bad Data for ItemId: There is no item with EntityTypeId=" + stEntityTypeId + " with GUID=" + stItemId);

	DeleteFolderItem(iEntityTypeId, lItemId, m_interchangeProject, m_dbConnection);
	                                            
	  
	if ( lParentFolderId > 0 ) {
		row.m_vFields[m_iParentFolderIdIndex] = toString(lParentFolderId);
	}

	row.m_vFields[m_iItemIdIndex] = toString(lItemId);
	return true;
}

