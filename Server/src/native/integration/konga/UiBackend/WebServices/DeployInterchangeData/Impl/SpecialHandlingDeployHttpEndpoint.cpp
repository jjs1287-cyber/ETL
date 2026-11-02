/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/Db/TranDbUtils/TranDbUtils.h>

#include <konga/HttpEndpoint/HttpTrigger.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include "DeployUtils.h"

#include "SpecialHandlingDeployHttpEndpoint.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::string;


namespace {
	unsigned int GetColPos(const string& sColName, const xsd__string_Array& columnNames) {
		vector<string> vColName(1);
		vColName[0] = sColName;
		vColPosT colPos = GetFieldIndexes(columnNames, vColName);
		return colPos.at(0);
	}
}

SpecialHandlingDeployHttpEndpoint::SpecialHandlingDeployHttpEndpoint(const xsd__string_Array& columnNames,
																	 DbConnection& dbConnection,
																	 GuidToIdMapper& guidToIdMapper) :

m_iHttpEndpointIdColPos(GetColPos("httpendpoint_id", columnNames)),
m_iUrlColPos(GetColPos("endpoint_url", columnNames)),
m_dbConnection(dbConnection), m_guidToIdMapper(guidToIdMapper)
{
}

bool SpecialHandlingDeployHttpEndpoint::HandleRow(InterchangeDataRowStruct& row) {
	return false;
}

void SpecialHandlingDeployHttpEndpoint::HandleUrl(long httpEndpointId, InterchangeDataRowStruct& row) {
	if ( row.m_vFields.size() - 1 < m_iHttpEndpointIdColPos || row.m_vFields.size() - 1 < m_iUrlColPos )
		throw InternalErrorException("Internal Error: Not enough fields received for HttpEndpointsTab.");

	                                                                  
	NullableString& url = row.m_vFields.at(m_iUrlColPos);
	HttpTrigger thisHttpTrigger;
	if ( url != 0 ) {
		const string& sUrl = url.str();
		thisHttpTrigger.Url = sUrl;
		thisHttpTrigger.Type = HttpTriggerType_HttpEndpoint;;
		thisHttpTrigger.ItemId = httpEndpointId;
		
		HttpTrigger existingHttpTrigger = konga::selectHttpTrigger(sUrl, m_dbConnection);
		if ( existingHttpTrigger.Type != HttpTriggerType_Unknown ) {
			if ( thisHttpTrigger != existingHttpTrigger && thisHttpTrigger.Url == existingHttpTrigger.Url ) {
				                                                                         
				const string& sHttpEndpointGuid = m_guidToIdMapper.GetGuid(httpEndpointId, HttpEndpointEntityId);
				const string& sOtherTriggerType = konga::getHttpTriggerTypeName(existingHttpTrigger.Type);
				string sWarning("The URL '" + sUrl + "' is already used by another http trigger of type " + sOtherTriggerType);
				if ( existingHttpTrigger.Type == HttpTriggerType_HttpEndpoint ) {
					sWarning += ". The other " + sOtherTriggerType + " is called '" + konga::getItemName(existingHttpTrigger.ItemId, HttpEndpointEntityId, m_dbConnection) + "'.";
				}
				sWarning += " This trigger will not be active until this conflict has been resolved.";
				AddWarning(HttpEndpointEntityId, sHttpEndpointGuid, sWarning);

				                                                                        
				url = "";
			}
		}
	}

	                                                                
	if ( url == 0 || url.str().empty() ) {
		removeHttpTrigger(thisHttpTrigger, m_dbConnection);
	}
	else {
		addHttpTrigger(thisHttpTrigger, m_dbConnection);
	}
}

bool SpecialHandlingDeployHttpEndpoint::HandleRowAfterInsert(long httpEndpointId, InterchangeDataRowStruct& row) {
	HandleUrl(httpEndpointId, row);
	return true;
}

bool SpecialHandlingDeployHttpEndpoint::HandleRowAfterUpdate(long httpEndpointId, InterchangeDataRowStruct& row) {
	HandleUrl(httpEndpointId, row);
	return true;
}

