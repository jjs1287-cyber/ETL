/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_FOLDER_ITEM_STRUCT_H
#define KONGA_WS_DEPLOY_FOLDER_ITEM_STRUCT_H

#include <map>
#include <string>

#include "InterchangeDataStructs.h"

namespace konga {
namespace interchange_deploy {
	
	class FolderItemStruct {
	public:
		FolderItemStruct(const InterchangeDataTableStruct& folderItemTab);
		~FolderItemStruct(void) {}

		const std::string& GetParentFolderId(const std::string& stItemId) const;

	private:
		                                 
		typedef std::map<std::string, std::string> ItemIdParentFolderMap;
		ItemIdParentFolderMap m_itemIdParentFolderMap;

	};
}
}

#endif                                        

