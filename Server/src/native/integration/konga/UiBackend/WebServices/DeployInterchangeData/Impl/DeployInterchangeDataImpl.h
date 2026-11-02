/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_DEPLOYINTERCHANGEDATAIMPL_H
#define KONGA_WS_DEPLOY_DEPLOYINTERCHANGEDATAIMPL_H

#include <string>
#include <vector>
#include <map>

#include <boost/noncopyable.hpp>

#include <konga/Db/DbUtils/SmartDbPtr.h>	                              
#include <konga/Db/DbUtils/DbConnection.h>

#include <konga/DbRelations/DbRelations.h>
#include <konga/ItemAccess/UserInfo.h>

#include <konga/UiBackend/UploadedFilesManager/UploadedFilesManager.h>

#include <konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.h>

#include "../DeployInterchangeData.hpp"
#include "../CDeployResults.hpp"

#include "InterchangeDataStructs.h"
#include "ItemWarningHolder.h"
#include "RenamedItemStruct.h"
#include "DeployUtils.h"
#include "DatabaseDriverManager.h"

namespace konga {

	class UserAccess;
	class GuidToIdMapper;
	class AutoDeleteFileSet;
	class InterchangeProject;

namespace interchange_deploy {

	class SpecialHandlingDeploy;
	class SpecialHandlingDeployDelete;
	class ExtendedItemPropertiesCollection;
	
	class DeployInterchangeDataImpl : private boost::noncopyable, public ItemWarningHolder, public WsImplBaseAuthenticator {
	public:
	
		DeployInterchangeDataImpl(const char* userName, const char* password);
		virtual ~DeployInterchangeDataImpl(void);

		CDeployProjectCheckResult* CheckProjectForDeploy(const char* projectGUID, const CIntegrationEntity_Array* const entities);
	
		                                                    
		CDeployResults* DeployInterchangeData(const InterchangeIdHolder* const pDeletedData,
											  const InterchangeDataHolder* const pChangedData);

		CFileCheckResult* CheckRequiredFiles(const CFileDependencies* const pFileDependencies,
			const CRequiredFiles* pRequiredFiles);

		CDeployResults* UploadJtrFiles(CJtrFiles* const pJtrFiles);

		CDeployResults* ProjectBackup(CUploadedJitterpak* const pUploadedJitterpak);

		CDeployResults* DeleteInterchangeProjects(xsd__string_Array * projectIds);

	private:
		InterchangeProject* GetInterchangeProject(const std::string& project_guid, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper);
		InterchangeProject* GetInterchangeProject(const InterchangeDataHolder* const pChangedData, DbConnection& dbConnection, GuidToIdMapper& guidToIdMapper);

		void DeleteInterchangeProject(long lProjectId, ItemWarningHolder& warnings);

		CDeployResults* AllocateDeployResults(int iStatus, const ItemWarningHolder& itemWarningHolder,
			const vector<RenamedItemStruct>& vRenamedItems) const;
		CDeployResults* AllocateDeployResults(int iStatus, const ItemWarningHolder& itemWarningHolder) const;

		                                                      
		                                                                         
		                                                                
		                                              
		bool HandleRequiredFiles(const CRequiredFiles& requiredFiles,
			const CFileDependencies* const pFileDependencies,
			std::vector<UploadedFile>& existingFiles);

		void HandleDeletedData(const InterchangeIdHolder* const DeletedData);
		void HandleDeletedDataImpl(const std::map<int, std::vector<long> >& deletedData);

		InterchangeIdHolder* HandleInsertedData(const InterchangeDataHolder* const InsertedData);
		void HandleChangedData(const InterchangeDataHolder* const ChangedData, const ExtendedItemPropertiesCollection& itemPropertiesCollection);

		void FillExtendedItemProperties(ExtendedItemPropertiesCollection& propertiesCollection, const InterchangeDataTable* const pEntityProperties);

		void HandleOperationPipelines(const InterchangeDataTable* const pOperationPipelines);

		                           
		void HandleExtendedItemPropertiesPreDeploy(const ExtendedItemPropertiesCollection& propertiesCollection);

		                                                                           
		void InsertExtendedItemProperties(const ExtendedItemPropertiesCollection& propertiesCollection);

		                                          
		int DeleteFromTable(const std::string& stName, const std::vector<long>& idsToDelete,
			SpecialHandlingDeployDelete* pSpecialHandling = 0);

		void UpdateScheduler() const;
		void UpdateJmsEngine() const;

		                                      
		                                                 
		void init();

		void Check();

	private:
		UserAccess* m_pUserAccess;
		GuidToIdMapper* m_pGuidToIdMapper;
		GuidToIdMapper* m_pDbGuidToIdMapper;
		DbRelations* m_pTranDbRelations;
		AutoDeleteFileSet* m_pAutoDeleteFileSet;

		konga::SmartDbPtr<konga::DbConnection> m_pTranDb;
		NextIdFinder* m_pHighestIdFinder;
		TypeInfoLoader* m_pTypeInfoLoader;

		InterchangeDataTableStruct m_tranTabStruct;
		std::vector<RenamedItemStruct> m_vRenamedItems;

		InterchangeProject* m_pInterchangeProject;

		bool m_bCallSchedulerUpdate;
		bool m_bCallJmsEngineUpdate;
		std::vector<long> m_vUpdatedDocuments;
		DatabaseDriverManager m_databaseDriverManager;

		                                                      
		std::map<int, std::string> m_oldSchedulePolicyForOperation;
	};

}                   
}                                

#endif                                               

