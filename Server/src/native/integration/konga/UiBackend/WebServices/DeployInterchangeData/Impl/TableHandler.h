/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_TABLE_HANDLER_H
#define KONGA_WS_DEPLOY_TABLE_HANDLER_H

#include <string>
#include <vector>
#include <deque>
#include <map>

#include <konga/DbRelations/DbRelations.h>
#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include <konga/ItemAccess/UserAccess.h>

#include "DeployUtils.h"
#include "SpecialHandlingDeploy.h"
#include "InterchangeDataStructs.h"
#include "ItemWarningHolder.h"
#include "RenamedItemStruct.h"
#include "FolderItemStruct.h"

#include "../InterchangeDataTable.hpp"
#include "../InterchangeDataRow.hpp"

namespace konga {

	class InterchangeProject;

namespace interchange_deploy {

	class TableHandler : public ItemWarningHolder {
	public:
		TableHandler(DbConnection& dbConnection,
			const DbRelations& tranDbRelations,
			GuidToIdMapper& guidToIdMapper,
			const TypeInfoLoader& typeInfoLoader,
			NextIdFinder& nextIdFinder,
			const UserInfo& userInfo,
			UserAccess& userAccess,
			const FolderItemStruct& folderItemStruct,
			InterchangeProject& interchangeProject) :
		  m_dbConnection(dbConnection),
		  m_tranDbRelations(tranDbRelations),
		  m_guidToIdMapper(guidToIdMapper),
		  m_typeInfoLoader(typeInfoLoader),
		  m_nextIdFinder(nextIdFinder),
		  m_userInfo(userInfo),
		  m_userAccess(userAccess),
		  m_folderItemStruct(folderItemStruct),
		  m_interchangeProject(interchangeProject),
		  m_pSpecialHandlingInsert(0),
		  m_pSpecialHandlingUpdate(0),
		  m_bHasModTimeCol(false),
		  m_iEntityTypeId(-1),
		  m_iPrimaryKeyIndex(-1),
		  m_lNextId(-1),
		  m_nChangedItems(0)
		{}

		~TableHandler(void) {}

		void HandleTable(const std::string& stName, InterchangeDataTableStruct& tableStruct,
						 SpecialHandlingDeploy* const pSpecialHandlingInsert,
						 SpecialHandlingDeploy* const pSpecialHandlingUpdate);

		void HandleTable(const std::string& stName, const InterchangeDataTable* const pTable,
						 SpecialHandlingDeploy* const pSpecialHandlingInsert,
						 SpecialHandlingDeploy* const pSpecialHandlingUpdate);

		                                                                                      
		int GetNumberOfChangedItems() const {
			return m_nChangedItems;
		}

		const std::vector<RenamedItemStruct>& GetRenamedItems() const {
			return m_vRenamedItems;
		}

	protected:
		void ExecuteSql(const std::string& stSql);

		int GetEntityTypeForPrimaryKey(const std::string& stTablename);

		void InsertRow(InterchangeDataRowStruct& row);
		void UpdateRow(InterchangeDataRowStruct& row);
		bool HasModColumn() const;

		void AddRenamedItem(int iEntityTypeId, const std::string& stGuid,
			const std::string& stOldName, const std::string& stNewName)
		{
			m_vRenamedItems.push_back( RenamedItemStruct(iEntityTypeId, stGuid, stOldName, stNewName) );
		}

	private:
		                             
		konga::DbConnection& m_dbConnection;
		const DbRelations& m_tranDbRelations;
		GuidToIdMapper& m_guidToIdMapper;
		const TypeInfoLoader& m_typeInfoLoader;
		NextIdFinder& m_nextIdFinder;
		const UserInfo& m_userInfo;
		UserAccess& m_userAccess;
		const FolderItemStruct& m_folderItemStruct;
		InterchangeProject& m_interchangeProject;

		SpecialHandlingDeploy* m_pSpecialHandlingInsert;
		SpecialHandlingDeploy* m_pSpecialHandlingUpdate;

		                   
		std::string m_stName, m_stTableName;
		bool m_bHasModTimeCol;
		int m_iEntityTypeId;
		int m_iPrimaryKeyIndex;
		long m_lNextId;
		std::vector<std::string> m_vsColNames;
		std::deque<bool> m_vIsColString;

		struct KeyIndex {
			KeyIndex(void) {}	                                                      
			KeyIndex(int index, bool bIsPrimary, bool bIsDependent, int iEntityTypeId) :
				m_iIndex(index),
				m_bIsPrimary(bIsPrimary),
				m_bIsDependent(bIsDependent),
				m_iEntityTypeId(iEntityTypeId) {}
			int m_iIndex;
			bool m_bIsPrimary, m_bIsDependent;
			int m_iEntityTypeId;
		};
		                                 
		std::map<unsigned int, KeyIndex> m_keyIndexMap;

		const KeyIndex* GetKeyIndex(unsigned int iCol) const;

		std::vector<RenamedItemStruct> m_vRenamedItems;

		int m_nChangedItems;
	};

}                   
}                                


#endif                                   

