/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_WEBSERVICE_DEPLOY_HELPER_H
#define KONGA_WS_WEBSERVICE_DEPLOY_HELPER_H

#include <string>
#include <map>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>
#include <konga/UiBackend/UploadedFilesManager/UploadedFilesManager.h>

#include "DeployUtils.h"
#include "SpecialHandlingDeploy.h"
#include "ItemWarningHolder.h"

#include "../InterchangeDataTable.hpp"

class ArrayOf_xsd_string;

namespace konga {
	
class DbConnection;
class FunctionIdMapper;
class UserInfo;

namespace interchange_deploy {

class ExtendedItemPropertiesCollection;

   
                                                             
                                                                                     
    
class WebService : public SpecialHandlingDeploy, public ItemWarningHolder {

public:
	WebService(DbConnection& dbConnection,
			   GuidToIdMapper& guidToIdMapper,
			   const ArrayOf_xsd_string& columnNames,
			   NextIdFinder& nextIdFinder,
			   FunctionIdMapper& functionIdMapper,
			   const UserInfo& userInfo,
			   const ExtendedItemPropertiesCollection& itemPropertiesCollection) :
			m_iIdIndex(-1), m_iWsdlIdIndex(-1),
			m_iBindingIndex(-1), m_iOperationIndex(-1),
			m_iSoapStyleIndex(-1), m_iBodyUseIndex(-1),
			m_iInputXsdIndex(-1), m_iOutputXsdIndex(-1),
			m_iHttpUserNameIndex(-1), m_iHttpPasswordIndex(-1),
			m_iWebServiceNameIndex(-1),
			m_lNextFunctionId(0), m_lNextTransformationId(0),
			m_iCurrentWsdlRow(0),
			m_dbConnection(dbConnection), m_guidToIdMapper(guidToIdMapper),
			m_nextIdFinder(nextIdFinder), m_functionIdMapper(functionIdMapper),
			m_userInfo(userInfo), m_itemPropertiesCollection(itemPropertiesCollection),
			m_fileManager(dbConnection)
	{
		init(columnNames);
	}

	virtual ~WebService() {}

	                                           
	virtual bool HandleRow(InterchangeDataRowStruct& row);

	virtual bool HandleRowAfterInsert(long newWebServiceId, InterchangeDataRowStruct& row);

protected:
	long GetNextFunctionId();
	long GetNextTransformationId();


	long GetNextId(const std::string& stTableName, const std::string& stIdName) {
		return m_nextIdFinder.GetNextId(stTableName, stIdName);
	}

	void ExecuteSql(const std::string& stSql);

private:
	void init(const ArrayOf_xsd_string& columnNames);

private:
	int m_iIdIndex, m_iWsdlIdIndex;
	int m_iBindingIndex, m_iOperationIndex, m_iSoapStyleIndex, m_iBodyUseIndex;
	int m_iInputXsdIndex, m_iOutputXsdIndex;
	int m_iHttpUserNameIndex, m_iHttpPasswordIndex;
	int m_iWebServiceNameIndex;
	long m_lNextFunctionId, m_lNextTransformationId;
	long m_iCurrentWsdlRow;

	DbConnection& m_dbConnection;
	GuidToIdMapper& m_guidToIdMapper;
	NextIdFinder& m_nextIdFinder;
	FunctionIdMapper& m_functionIdMapper;
	const UserInfo& m_userInfo;
	const ExtendedItemPropertiesCollection& m_itemPropertiesCollection;

	UploadedFilesManager m_fileManager;

	                                                
	  
	class IOSchemasKey {
	public:
		IOSchemasKey(const std::string& stAbsoluteWsdlFilePath,
			const std::string& stBinding, const std::string& stOperation, const std::string& stSoapStyle, const std::string& stBodyUse,
			const std::string& stOperationInput, const std::string& stOperationOutput) : m_stAbsoluteWsdlFilePath(stAbsoluteWsdlFilePath),
			m_stBinding(stBinding), m_stOperation(stOperation), m_stSoapStyle(stSoapStyle), m_stBodyUse(stBodyUse),
			m_stOperationInput(stOperationInput), m_stOperationOutput(stOperationOutput),
			m_hash(m_stAbsoluteWsdlFilePath+m_stBinding+m_stOperation+m_stSoapStyle+m_stBodyUse+m_stOperationInput+m_stOperationOutput) {}

		~IOSchemasKey(void) {}

		bool lt(const IOSchemasKey& rhs) const {
			return m_hash < rhs.m_hash;
		}

	private:
		const std::string m_stAbsoluteWsdlFilePath;
		const std::string m_stBinding, m_stOperation, m_stSoapStyle, m_stBodyUse;
		const std::string m_stOperationInput, m_stOperationOutput;
		const std::string m_hash;
	};

	                                           
	struct ltIOSchemasKey {
		bool operator()(const IOSchemasKey& lhs, const IOSchemasKey& rhs) const {
			return lhs.lt(rhs);
		}
	};
	
	class IOSchemas {
	public:
		IOSchemas(const std::string& stInputXsdFile, const std::string& stOutputXsdFile)
			: m_stInputXsdFile(stInputXsdFile), m_stOutputXsdFile(stOutputXsdFile) {}
		~IOSchemas(void) {}

		void GetIOSchemas(std::string& stInputXsdFile, std::string& stOutputXsdFile) const {
			stInputXsdFile = m_stInputXsdFile;
			stOutputXsdFile = m_stOutputXsdFile;
		}

	private:
		const std::string m_stInputXsdFile, m_stOutputXsdFile;
	};

	                                             
	typedef std::map<IOSchemasKey, IOSchemas, ltIOSchemasKey> IOSchemasMapT;
	IOSchemasMapT m_IOSchemasCache;
};

}                   
}                                

#endif                                       

