/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_SPECIAL_HANDLING_DEPLOY_FOLDER_H
#define KONGA_WS_SPECIAL_HANDLING_DEPLOY_FOLDER_H

#include <vector>

#include "SpecialHandlingDeploy.h"

namespace konga {

	class GuidToIdMapper;
	class DbConnection;
	class InterchangeProject;

namespace interchange_deploy {

                                                 
class FolderItem : public SpecialHandlingDeploy {
public:
	FolderItem(unsigned int iParentFolderIdIndex, unsigned int iEntityTypeIdIndex, unsigned int iItemIdIndex,
		GuidToIdMapper& guidToIdMapper, DbConnection& dbConnection,
		const InterchangeProject& interchangeProject)
		: m_iParentFolderIdIndex(iParentFolderIdIndex), m_iEntityTypeIdIndex(iEntityTypeIdIndex),
		m_iItemIdIndex(iItemIdIndex),
		m_guidToIdMapper(guidToIdMapper), m_dbConnection(dbConnection),
		m_interchangeProject(interchangeProject) {}

	virtual ~FolderItem() {};

	bool HandleRow(InterchangeDataRowStruct& row);

private:
	unsigned int m_iParentFolderIdIndex, m_iEntityTypeIdIndex, m_iItemIdIndex;
	GuidToIdMapper& m_guidToIdMapper;
	DbConnection& m_dbConnection;
	const InterchangeProject& m_interchangeProject;
};

void DeleteFolderItem(int iEntityTypeId, long lEntityId, const InterchangeProject& interchangeProject, DbConnection& dbConnection);

typedef FolderItem FolderItemInsert;
typedef FolderItem FolderItemUpdate;

}                   
}                                

#endif                                             

