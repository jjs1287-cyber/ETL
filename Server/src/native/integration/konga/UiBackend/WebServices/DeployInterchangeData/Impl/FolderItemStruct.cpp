/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <konga/Utils/PortDefs.h>	                        
#include <konga/Utils/CrException.h>

#include "FolderItemStruct.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::string;

namespace {
	const std::string stParentFolderColName("parentfolder_id");
	const std::string stItemIdColName("item_id");
}

FolderItemStruct::FolderItemStruct(const InterchangeDataTableStruct& folderItemTab)
{
	if ( folderItemTab.m_vColumnNames.empty() )
		return;	                                                                                 
	int iParentFolderIndex = -1;
	int iItemIdIndex = -1;

	for( int index = 0, nCols = folderItemTab.m_vColumnNames.size(); index < nCols; ++index ) {
		if ( strcasecmp(folderItemTab.m_vColumnNames.at(index).c_str(), stParentFolderColName.c_str()) == 0 )
			iParentFolderIndex = index;
		else if ( strcasecmp(folderItemTab.m_vColumnNames.at(index).c_str(), stItemIdColName.c_str()) == 0 )
			iItemIdIndex = index;
	}

	if ( iParentFolderIndex == -1 || iItemIdIndex == -1 )
		throw CrException("Internal Error: Failed to find indexes for FolderItem. This is most definately a bug.");

	vInterchangeDataRowStructT::const_iterator cit = folderItemTab.m_vRows.begin();
	for(; cit != folderItemTab.m_vRows.end(); ++cit ) {
		const InterchangeDataRowStruct& row = *cit;
		const NullableString& parentFolder = row.m_vFields.at(iParentFolderIndex);
		if ( parentFolder == 0 )
			continue;

		const NullableString& itemId = row.m_vFields.at(iItemIdIndex);
		if ( itemId == 0 )
			continue;

		m_itemIdParentFolderMap[ itemId.str() ] = parentFolder.str();

	}
}

namespace {
	const string stEmpty("");
}

const string& FolderItemStruct::GetParentFolderId(const string& stItemId) const {
	ItemIdParentFolderMap::const_iterator cit = m_itemIdParentFolderMap.find(stItemId);
	if ( cit == m_itemIdParentFolderMap.end() ) {
		return stEmpty;
	}
	else
		return cit->second;
}

