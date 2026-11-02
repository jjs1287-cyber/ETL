/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <sstream>	               

#include <konga/Utils/StringUtils.h>				             
#include <konga/Utils/CrException.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/TableColumnsOdbc.h>		                    

#include "FunctionIdMapper.h"

using namespace konga;
using std::map;
using std::string;
using std::stringstream;

void FunctionIdMapper::AddWebServiceToCache(long webServiceId, long functionId, long transformationId) {
	m_webServiceFunctionIdMapCache[webServiceId] = functionId;
	m_webServiceTransformationIdMapCache[webServiceId] = transformationId;
	m_transformationWebServiceIdMapCache[transformationId] = webServiceId;
}

long FunctionIdMapper::GetFunctionIdForWebService(long webServiceId) const {
	ididMap::const_iterator cit = m_webServiceFunctionIdMapCache.find(webServiceId);
	if ( cit != m_webServiceFunctionIdMapCache.end() )
		return cit->second;

	long functionId = GetFunctionIdFromDatabase(1, webServiceId);
	if ( functionId < 0 )
		throw CrException("FunctionIdMapper: Could not find the Function_ID for WebService_ID = " + toString(webServiceId));
	m_webServiceFunctionIdMapCache[webServiceId] = functionId;
	return functionId;
}

long FunctionIdMapper::GetTransformationIdForWebService(long webServiceId, bool throwOnError) const {
	ididMap::const_iterator cit = m_webServiceTransformationIdMapCache.find(webServiceId);
	if ( cit != m_webServiceTransformationIdMapCache.end() )
		return cit->second;

	long tranId = GetTransformationIdFromDatabase(webServiceId);
	if ( tranId < 0 ) {
		if ( throwOnError ) {
			throw CrException("FunctionIdMapper: Could not find the Transformation_ID for WebService_ID = " + toString(webServiceId));
		}
		return tranId;
	}
	m_webServiceTransformationIdMapCache[webServiceId] = tranId;
	m_transformationWebServiceIdMapCache[tranId] = webServiceId;
	return tranId;
}

long FunctionIdMapper::GetWebServiceIdForTransformation(long transformationId) const {
	ididMap::const_iterator cit = m_transformationWebServiceIdMapCache.find(transformationId);
	if ( cit != m_transformationWebServiceIdMapCache.end() )
		return cit->second;

	long webServiceId = GetWebServiceIdFromDatabase(transformationId);
	if ( webServiceId < 0 )
		throw CrException("FunctionIdMapper: Could not find the WebService_ID for TransformationId_ID = " + toString(transformationId));
	m_transformationWebServiceIdMapCache[transformationId] = webServiceId;
	m_webServiceTransformationIdMapCache[webServiceId] = transformationId;
	return webServiceId;
}

long FunctionIdMapper::GetTransformationIdFromDatabase(int iWebServiceId) const {
	const string& stSelect = ("SELECT transformation_id FROM webservicefunctiontransformationview WHERE webservice_id = " + toString(iWebServiceId));
	SmartDbPtr<DynamicDbRecordSet> rsSelect( DynamicDbRecordSet::MakeInstance(&m_dbConnection, stSelect) );
	rsSelect->Open();
	return ( rsSelect->Next() ) ? atol(rsSelect->GetFieldValue(0).c_str()) : -1;
}

long FunctionIdMapper::GetWebServiceIdFromDatabase(int iTransformationId) const {
	const string& stSelect = ("SELECT webservice_id FROM webservicefunctiontransformationview WHERE transformation_id = " + toString(iTransformationId));
	SmartDbPtr<DynamicDbRecordSet> rsSelect( DynamicDbRecordSet::MakeInstance(&m_dbConnection, stSelect) );
	rsSelect->Open();
	return ( rsSelect->Next() ) ? atol(rsSelect->GetFieldValue(0).c_str()) : -1;
}

namespace {
	const string stSelect( "SELECT function_id from functionstab WHERE functiontype_id = ");
}

long FunctionIdMapper::GetFunctionIdFromDatabase(int functionType, long foreignKey) const {
	stringstream ssSql;
	ssSql << stSelect << functionType << " AND foreignkeyfunction_id = " << foreignKey;
	SmartDbPtr<DynamicDbRecordSet>
		pSelectFunction( DynamicDbRecordSet::MakeInstance(&m_dbConnection, ssSql.str()) );
	pSelectFunction->Open();
	return ( pSelectFunction->Next() ) ? atol( pSelectFunction->GetFieldValue(0).c_str() ) : -1;
}
