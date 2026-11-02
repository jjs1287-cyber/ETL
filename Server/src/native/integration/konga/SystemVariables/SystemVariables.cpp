/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/Utils/Settings.h>
#include <konga/Utils/StringUtils.h>

#include <konga/DataElement/DataElement.h>

#include <konga/OperationContext/OperationDataElementHolder.h>

#include "SystemVariables.h"

using namespace konga;
using std::string;

namespace {
	const string PREDEFINED_GLOBAL_VARIABLES_SECTION("PredefinedGlobalVariables");

	DataElement* GetSystemVariableDE(const std::string& sName, const OperationDataElementHolder* const pDEHolder) {
		if ( pDEHolder && pDEHolder->isOperationDataElementDefined(sName) ) {
			DataElement* de = pDEHolder->getOperationDataElement(sName);
			if ( !de->IsNull() && de->getType() != DataElement::t_unknown && de->getError() == 0 ) {
				return de;
			}
		}
		return 0;
	}

	string modify_name(const string& sName) {
		if ( sName.empty() )
			return "";
		if ( *(sName.begin()) == '$' )
			return sName.substr(1);
		return sName;
	}
}

std::string konga::GetSystemVariable(const std::string& sName_in, const OperationDataElementHolder* const pDEHolder, const std::string& sDefault) {
	const string& sName = modify_name(sName_in);
	DataElement* de = GetSystemVariableDE(sName, pDEHolder);
	if ( de == 0 )
		return konga::GetOptionAsString(PREDEFINED_GLOBAL_VARIABLES_SECTION, sName, sDefault);

	int err;
	return (const char*)de->GetString(err);
}

long konga::GetSystemVariableAsLong(const std::string& sName_in, const OperationDataElementHolder* const pDEHolder, long lDefault) {
	const string& sName = modify_name(sName_in);
	DataElement* de = GetSystemVariableDE(sName, pDEHolder);
	if ( de == 0 )
		return konga::GetOptionAsInt(PREDEFINED_GLOBAL_VARIABLES_SECTION, sName, lDefault);

	int err;
	return de->GetLong(err);
}

bool konga::GetSystemVariableAsBool(const std::string& sName_in, const OperationDataElementHolder* const pDEHolder, bool bDefault) {
	const string& sName = modify_name(sName_in);
	DataElement* de = GetSystemVariableDE(sName, pDEHolder);
	if ( de == 0 )
		return konga::GetOptionAsBoolean(PREDEFINED_GLOBAL_VARIABLES_SECTION, sName, bDefault);

	int err;
	return de->GetBool(err);
}

