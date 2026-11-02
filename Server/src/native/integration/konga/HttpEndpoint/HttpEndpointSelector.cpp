/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <memory>

#include <konga/Utils/StringUtils.h>	           

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbUtils.h>		            

#include "HttpEndpointSelector.h"

using namespace konga;
using std::string;

HttpEndpointSelector::HttpEndpointSelector(DbConnection* pDbConn) : DbConnectionHolder(pDbConn) {
	this->SetInternalDbName("TranDb");
}

namespace {
	const string selectSql(
		"SELECT httpendpoint_id, httpendpoint_name, response_type, request_variable, response_variable, response_content_type, base_url, relative_url_variable " \
		"FROM httpendpointstab"
		);

	HttpEndpoint* fromRecordSet(DynamicDbRecordSet& rs1, DbConnection& dbConn) {
		const int endpointId = atoi(rs1.GetFieldValue(0).c_str());
		const string name( rs1.IsFieldNull(1) ? "" : rs1.GetFieldValue(1) );

		const int responseMode = atoi(rs1.GetFieldValue(2).c_str());
		
		const string requestVariable( rs1.IsFieldNull(3) ? "" : rs1.GetFieldValue(3) );
		const string responseVariable( rs1.IsFieldNull(4) ? "" : rs1.GetFieldValue(4) );

		const string responseContentType( rs1.IsFieldNull(5) ? "" : rs1.GetFieldValue(5) );

		std::auto_ptr<HttpEndpoint> pEndpoint( new HttpEndpoint(name, konga::getHttpEndpointResponseMode(responseMode), requestVariable, responseVariable) );

		                                        
		const string sql("SELECT operation_id, synchronous, final_target_as_response, http_verb FROM httpendpointoperationstab" \
			" WHERE httpendpoint_id = " + toString(endpointId)
			+ " ORDER BY httpendpointoperation_id");
		SmartDbPtr<DynamicDbRecordSet> rs2( DynamicDbRecordSet::MakeInstance(&dbConn, sql) );
		rs2->Open();
		if ( !rs2->Next() )
			throw CrException("Definition of HTTP Endpoint '" + name + "' is incomplete. It has no triggered operations defined.");

		do {
			long operationId = atoi(rs2->GetFieldValue(0).c_str());
			int sync = atoi(rs2->GetFieldValue(1).c_str());
			int final_target_as_response = rs2->IsFieldNull(2) ? 0 : atoi(rs2->GetFieldValue(2).c_str());
			const string verb( rs2->IsFieldNull(3) ? "" : rs2->GetFieldValue(3) );

			pEndpoint->addOperation( new HttpEndpointOperation(operationId, sync != 0, final_target_as_response != 0, verb) );
		} while ( rs2->Next() );

		return pEndpoint.release();
	}
}

HttpEndpoint* HttpEndpointSelector::GetHttpEndpoint(long httpEndointId) {
	string sql(selectSql + " WHERE httpendpoint_id = " + toString(httpEndointId));
	SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&GetDbConnection(), sql) );
	rs->Open();
	if ( !rs->Next() )
		return 0;

	return fromRecordSet(*rs, GetDbConnection());
}
