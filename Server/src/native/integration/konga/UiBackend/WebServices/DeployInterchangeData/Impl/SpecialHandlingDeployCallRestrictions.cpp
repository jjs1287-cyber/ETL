/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>	             

#include <konga/Db/DbUtils/DbConnection.h>

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/FunctionIdMapper.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include <konga/UiBackend/WebServices/Utils/WsUtils.h>	                                   

#include "../InterchangeDataRow.hpp"
#include "SpecialHandlingDeployCallRestrictions.h"

#include <konga/Utils/PortDefs.h>	                 

using namespace konga;
using namespace konga::interchange_deploy;

void CallRestrictions::init() {
	                                                       
	                                                                        
	                        
}

bool CallRestrictions::HandleRow(InterchangeDataRowStruct& row) {
	const long webServiceId = GetWebServiceId(row);

	                                                                                         
	const long functionId = m_functionIdMapper.GetFunctionIdForWebService(webServiceId);

	const string& stFunctionId = toString(functionId);
	row.m_vFields[m_functionIdColumnIndex] = stFunctionId;

	                                                                                             
	const string stDeleteSql("DELETE FROM CallrestrictionsTab WHERE Function_ID = "
		+ stFunctionId + " AND FunctionType_ID = 1");
	m_dbConnection.ExecuteSql(stDeleteSql);
	return true;
}

                                                                  
long CallRestrictions::GetWebServiceId(InterchangeDataRowStruct& row) const {
	const NullableString& functionId = row.m_vFields.at(GetFunctionIdColumnIndex());
	if ( functionId == 0 || functionId.str().empty() ) {
		throw CrException("Bad Data: No FunctionID specified for CallRestriction.");
	}

	const string& stFunctionId = functionId.str();
	const long lWebServiceId = m_guidToIdMapper.GetId(stFunctionId, WebServiceEntityId);
	if ( lWebServiceId == GuidToIdMapper::invalid_id ) {
		throw CrException("Failed to find WebServiceId for GUID = " + stFunctionId);
	}
	return lWebServiceId;
}

