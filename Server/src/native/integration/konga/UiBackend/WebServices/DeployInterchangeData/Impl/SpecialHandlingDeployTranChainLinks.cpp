/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <sstream>	               

#include "SpecialHandlingDeployTranChainLinks.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::string;
using std::stringstream;

#include <konga/Utils/StringUtils.h>	           
#include <konga/Utils/CrException.h>
#include <konga/Utils/PortDefs.h>		                 

#include <konga/Db/DbUtils/DbConnection.h>

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/FunctionIdMapper.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "../InterchangeDataRow.hpp"


long SpecialHandlingDeployTranChainLinks::getDbTranId(const string& stInputTranId) {

	                                     
	const long lTranId = m_guidToIdMapper.GetId(stInputTranId, TransformationEntityId);
	if ( lTranId != GuidToIdMapper::invalid_id )
		return lTranId;

	                                   
	const long lWsId = m_guidToIdMapper.GetId(stInputTranId, WebServiceEntityId);
	if ( lWsId != GuidToIdMapper::invalid_id )
		return getTranIdFromWsId(lWsId);

	throw CrException("The GUID '" + stInputTranId + "' could not be translated to either TransformationId or WebServiceId. "
		+ "This probably means that the web service has to be deployed again. Try to deploy the whole model again to this server.");
}

long SpecialHandlingDeployTranChainLinks::getTranIdFromWsId(long lWsId) {
	return m_functionIdMapper.GetTransformationIdForWebService(lWsId);
}

long SpecialHandlingDeployTranChainLinks::getDbOperationId(const string& stOperationId) {
	const long lOperationId = m_guidToIdMapper.GetId(stOperationId, OperationEntityId);
	if ( lOperationId == GuidToIdMapper::invalid_id ) {
		throw CrException("Failed to find the database id for the Operation with GUID = " + stOperationId);
	}
	return lOperationId;
}


bool SpecialHandlingDeployTranChainLinks::HandleRow(InterchangeDataRowStruct& row) {
	vFieldsT& fields = row.m_vFields;

	                                                                                                     

	const NullableString& tranId = fields.at(1);
	if ( tranId == 0 || tranId.str().empty() )
		throw CrException("Bad Input Data: Invalid value for TransformationId in TransformationChainLink.");

	const NullableString& nextTranId = fields.at(2);
	if ( nextTranId == 0 || nextTranId.str().empty() )
		throw CrException("Bad Input Data: Invalid value for NextTransformationId in TransformationChainLink.");

	const long lTranId = getDbTranId(tranId.str());
	const long lNextTranId = getDbTranId(nextTranId.str());

	fields[1] = toString(lTranId);
	fields[2] = toString(lNextTranId);

	                                                                                                        
	                                          
	if ( fields[0] == 0) {
		                                                                                                
		                  
		stringstream ssSql;
		ssSql << "DELETE FROM " << getTableName(nameTransformationChainLinks) << " WHERE operation_id is null AND "
			<< "transformation_id = " << lTranId
			<< " AND nexttransformation_id = " << lNextTranId;

		UIBACKEND_TRACE("SpecialHandlingDeployTranChainLinks::HandleRow - " + ssSql.str());

		m_dbConnection.ExecuteSql(ssSql.str());
	}

	return true;
}

