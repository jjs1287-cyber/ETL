/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <sstream>

#include <konga/Utils/StringUtils.h>

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/DatabaseTableNameMappings.h>

#include <konga/ItemAccess/AccessSqlGenerator.h>

#include "OperationTreeCheckoutSqlGeneratorForFolder.h"

using namespace konga;
using namespace konga::interchange_checkout;
using std::string;
using std::stringstream;

namespace {
	string ContentsEntityTypeCondition() {
		return string(
			"(f.contentsentitytype_id = " + toString(OperationEntityId)
			+ " OR f.contentsentitytype_id = " + toString(ScheduleEntityId) + ")"
			);
	}
}

string OperationTreeCheckoutSqlGeneratorForFolder::GetSql() {


	                                                                           
	string stFoldersWhereClause(
		"WHERE f.folder_id > 0 AND " + ContentsEntityTypeCondition()
	);


	                                                                     
	const string stSelectFolders(
		"SELECT f.folder_id FROM folderstab f " + stFoldersWhereClause
	);

	if ( m_bIsItem ) {
		stringstream ssSql;
		ssSql << "SELECT parentfolder_id, entitytype_id, item_id FROM folderitemtab WHERE ("
			<< " (entitytype_id = " << OperationEntityId << " and parentfolder_id > 0) OR"
			<< " (entitytype_id = " << ScheduleEntityId << " and parentfolder_id > 0) OR"
			<< " (entitytype_id = " << FolderEntityId << " AND parentfolder_id IN ("
			<< stSelectFolders
			<< ")))"
			<< " AND (" << konga::GetAccessControlWhereClause(m_userInfo, "parentfolder_id", FolderEntityId) << ")";
		return ssSql.str();
	}
	else {
		const string stFoldersSelect(
			                                                        
			"SELECT f.Folder_ID, f.Folder_Name, f.ContentsEntityType_ID, fi.ParentFolder_Id, "
			"if.InterchangeProject_ID FROM FoldersTab f "
			"INNER JOIN InterchangeProjectFoldersTab if ON f.Folder_ID = if.Item_ID "
			"INNER JOIN FolderItemTab fi ON f.Folder_ID = fi.Item_ID "
			"AND fi.EntityType_ID = " + toString(FolderEntityId) + " "
			+ stFoldersWhereClause
			+ " AND (" + konga::GetAccessControlWhereClause(m_userInfo, getIdName(nameFolders), FolderEntityId) + string(")")
			+ " UNION " +
			                                                                                                      
			                                                     
			"SELECT f.Folder_ID, f.Folder_Name, f.ContentsEntityType_ID, 0, "
			"if.InterchangeProject_ID FROM FoldersTab f "
			"INNER JOIN InterchangeProjectFoldersTab if ON f.Folder_ID = if.Item_ID "
			+ stFoldersWhereClause
			+ " AND (" + konga::GetAccessControlWhereClause(m_userInfo, getIdName(nameFolders), FolderEntityId) + string(")")
			+ " AND f.Folder_ID NOT IN(SELECT Item_id FROM FolderItemTab WHERE EntityType_id=1)"
		);
		return stFoldersSelect;
	}
}

