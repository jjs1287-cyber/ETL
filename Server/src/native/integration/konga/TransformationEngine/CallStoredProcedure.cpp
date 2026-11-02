/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

                                                          
                                
                          

#include <vector>
#include <string>
#include <memory>

#include <konga/DataElement/DataElement.h>

#include <konga/Db/DbUtils/StoredProcedureCaller.h>

#include <konga/OperationContext/OperationDataElementHolder.h>
#include <konga/OperationContext/OperationContext.h>

#include <konga/TransformationUtils/TranDBUtils.h>

#include "Transform.h"

using namespace konga;
using std::string;
using std::vector;

namespace {
	                                                                  
	StoredProcedureCaller::SPParamType GetSPParamType(const DataElement& de ) {
		if ( de.IsNull() )
			return StoredProcedureCaller::PARAM_TYPE_NULL;
		switch(de.getType()) {
			case DataElement::t_bit:
			case DataElement::t_bool:
			case DataElement::t_int:
			case DataElement::t_long:
				return StoredProcedureCaller::PARAM_TYPE_INTEGER;
			case DataElement::t_double:
			case DataElement::t_float:
				return StoredProcedureCaller::PARAM_TYPE_REAL;
			case DataElement::t_date:
				return StoredProcedureCaller::PARAM_TYPE_DATETIME;
			case DataElement::t_unknown:
				return StoredProcedureCaller::PARAM_TYPE_NULL;
			default:
				return StoredProcedureCaller::PARAM_TYPE_STRING;
		}
	}

	                                                           
	DataElement::Type GetDataElementType(const StoredProcedureCaller::SPParamType type) {
		switch(type) {
			case StoredProcedureCaller::PARAM_TYPE_DATETIME:
				return DataElement::t_date;
			case StoredProcedureCaller::PARAM_TYPE_INTEGER:
				return DataElement::t_long;
			case StoredProcedureCaller::PARAM_TYPE_REAL:
				return DataElement::t_double;
			case StoredProcedureCaller::PARAM_TYPE_NULL:
				return DataElement::t_unknown;
			default:
				return DataElement::t_string;
		}
	}

	DataElement* CreateDataElement(const StoredProcedureCaller::SPParam& spParam) {
		DataElement::Type type = GetDataElementType(spParam.type);
		if ( type == DataElement::t_unknown ) {
			return new DataElement();
		}
		return new DataElement(GetDataElementType(spParam.type), spParam.value.c_str());
	}
}

DataElement* CTransform::CallStoredProcedure(const int argn, const Token::FNC*f, Token*ts[],
											 vector<int>&ind, konga::AutoDeleteStack<DataElement>& tmp,
											 const OperationDataElementHolder* const pDEHolder)
{
	if (argn < 3 ) {
		string stErr("Too few arguments passed to CallStoredProcudure method. " \
			"At least 3 are required, " + toString(argn) + " were supplied.");
		throw CrException(stErr);
	}
	                                                      
	DataElement* de0 = getDataElement(1,f,ts,ind,tmp);
	int err = 0;
	const string stDbGuid( (const char*)de0->GetString(err) );
	if(err) handle_err(de0, err);

	de0 = getDataElement(2,f,ts,ind,tmp);
	const string stSPName( de0->GetString(err) );
	if(err) handle_err(de0, err);

	const UserInfo& currentUser = getTransformationDataHolder()->getOperationContext()->getCurrentUser();
	DbConnectionInfoStruct dbConnInfo;
	const string& stConnectionString = getConnectionStringFromDb(0, currentUser, stDbGuid, pDEHolder, dbConnInfo);
	if ( dbConnInfo.driverType != ODBC ) {
		throw CrException("CallStoredProcedure has only been implemented for ODBC.");
	}

	DataElement* const resultSetDE = getDataElement(3,f,ts,ind,tmp);
	                                                 

	                           
	StoredProcedureCaller::SPParamsT parameters;
	vector<DataElement*> deParameters;
	for( int iArgPos = 4; iArgPos <= argn; ++iArgPos ) {
		de0 = getDataElement(iArgPos,f,ts,ind,tmp);
		const string value ((const char*)de0->GetString(err));
		
		                                                                           

		StoredProcedureCaller::SPParamType paramType = GetSPParamType(*de0);
		if ( err || paramType == StoredProcedureCaller::PARAM_TYPE_NULL ) {
			parameters.push_back(StoredProcedureCaller::SPParam());
			err = 0;
		}
		else {
			parameters.push_back( StoredProcedureCaller::SPParam(paramType, value) );
		}
		deParameters.push_back(de0);
	}

	                      
	StoredProcedureCaller caller(stConnectionString, dbConnInfo.txnIsolationLevel);
	StoredProcedureCaller::SPParam returnValue;
	caller.CallStoredProcedure(stSPName, parameters, returnValue);

	                                
	StoredProcedureCaller::SPParamsT::const_iterator citParameters = parameters.begin();
	vector<DataElement*>::iterator itDeParameters = deParameters.begin();
	for(; citParameters != parameters.end(); ++citParameters, ++itDeParameters ) {
		DataElement* deParam = *itDeParameters;
		if ( deParam->isGlobalDE() ) {
			DataElement::Type deType = GetDataElementType(citParameters->type);
			if ( deType != DataElement::t_unknown ) {
				deParam->setType(deType);
				deParam->setValue( citParameters->value.c_str() );
			}
			else {
				DataElement nullDe;
				deParam->set(&nullDe);
			}
		}
	}

	                    
	DataElement* de = CreateDataElement(returnValue);
	tmp.push( de );
	return de;
}
