/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <cassert>
#include <map>

#include <konga/Utils/Settings.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/KString.h>

#include <konga/DataElement/DataElement.h>

#include <konga/OperationContext/OperationDataElementHolder.h>
#include <konga/OperationContext/OperationContext.h>

#include <konga/OperationDataElements/OperationDataElementsManager.h>

#include "OperationEngine.h"

using namespace konga;
using std::string;
using std::vector;
using std::map;

namespace {
	const string COUNTERS_DE("__COUNTERS");
	const string SEPARATOR("-");
	const string OPERATION_CHAIN_INSTANCE_GUID("__OPERATION_CHAIN_INSTANCE_GUID");
}

void OperationEngine::GetOperationDataElements(const long lSessionId) {
	if ( lSessionId > 0 ) {
		OperationDataElementsManager mgr;
		OperationDataElementHolder& deHolder = getOperationDataElementHolder();
		mgr.GetDataElements(deHolder, lSessionId);
		m_lSessionId = lSessionId;
		if ( !m_bIsDebugEnabled ) {
			mgr.DeleteSession(lSessionId);
		}

		                                        
		if ( deHolder.isOperationDataElementDefined(COUNTERS_DE) ) {
			DataElement* de = deHolder.getOperationDataElement(COUNTERS_DE);
			int err;
			const string& counters = (const char*)de->GetString(err);
			if ( !counters.empty() ) {
				vector<string> vCounters = konga::SplitString(counters, SEPARATOR);
				for( vector<string>::const_iterator cit = vCounters.begin(); cit != vCounters.end(); ++cit ) {
					const string& counter = *cit;
					DataElement* deInitialValue = deHolder.getOperationDataElement(counter);
					const long long initialValue = deInitialValue->GetLong(err);
					deHolder.InitCounter(counter, initialValue);
				}
			}
			deHolder.removeOperationDataElement(COUNTERS_DE);
		}

		                                  
		if ( deHolder.isOperationDataElementDefined(OPERATION_CHAIN_INSTANCE_GUID) ) {
			DataElement* de = deHolder.getOperationDataElement(OPERATION_CHAIN_INSTANCE_GUID);
			int err;
			const string guid( (const char*)de->GetString(err) );
			if ( !guid.empty() ) {
				getOperationContext().setOperationChainInstanceGuid(guid);
			}
			deHolder.removeOperationDataElement(OPERATION_CHAIN_INSTANCE_GUID);
		}		
	}
	else
		m_lSessionId = 0;
	
	                                                               
	getOperationContext().getOperationChainInstanceGuid();
}

void OperationEngine::SetOperationDataElements() {
	                                                                           
	OperationDataElementHolder& deHolder = getOperationDataElementHolder();
	deHolder.removeOperationDataElement("etlforge.operation.name");
	deHolder.removeOperationDataElement("etlforge.operation.guid");

	                     
	const string& counters = deHolder.getCountersAsString(SEPARATOR);
	if ( !counters.empty() ) {
		DataElement* de = deHolder.getOperationDataElement(COUNTERS_DE);
		de->setType(DataElement::t_string);
		de->setValue(counters.c_str());
	}
	else {
		deHolder.removeOperationDataElement(COUNTERS_DE);
	}

	if ( m_pContext ) {
		                                   
		DataElement* de = deHolder.getOperationDataElement(OPERATION_CHAIN_INSTANCE_GUID);
		de->setType(DataElement::t_string);
		de->setValue(getOperationContext().getOperationChainInstanceGuid().c_str());
	}

	OperationDataElementsManager mgr;
	m_lSessionId = mgr.AddDataElements(deHolder);
	if ( m_lSessionId <= 0 )
		m_lSessionId = 0;
}

std::string OperationEngine::replace_de(const std::string& s) {
	return getOperationDataElementHolder().replaceOperationDataElementInString(s);
}
