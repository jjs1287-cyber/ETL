/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <cassert>
#include <iostream>
#include <memory>		           

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/util.h>			                
#include <konga/Utils/PortDefs.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>

#include <konga/DataElement/DataElement.h>

#include <konga/OperationContext/ErrorReporter.h>
#include <konga/OperationContext/OperationDataElementHolder.h>

#include "Operation.h"
#include "ProcessedFiles.h"
#include "TransferHistory.h"
#include "OperationEngineException.h"

#include "OperationEngineContext.h"

using namespace konga;
using std::string;
using std::map;

namespace {
		const char* dataElementsToHandle[] = {
			"etlforge.source.locator",
			"etlforge.source.filename",
			"etlforge.source.size",
			"etlforge.operation.name",
			"etlforge.operation.last_error",
			"etlforge.operation.error",
			"etlforge.operation.guid",
			0
	};
}

OperationEngineContext::OperationEngineContext(const UserInfo& currentUser, Debuggee* pDebuggee, OperationDataElementHolder& deHolder)
	: DefaultOperationContext(currentUser, pDebuggee),
	m_deHolder(deHolder),
	m_pOperation(0),
	m_lSourceSize(0),
	m_pDbConnection(0)
{
	const char* p;
	for( int i = 0; (p=dataElementsToHandle[i]) != 0; ++i ) {
		m_deHolder.registerDataElementHandler(p, this);
	}
}

OperationEngineContext::~OperationEngineContext(void) {
	const char* p;
	for( int i = 0; (p=dataElementsToHandle[i]) != 0; ++i ) {
		m_deHolder.registerDataElementHandler(p, 0);
	}
}

std::string OperationEngineContext::getOperationName() const {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);	
	const string tmp(m_pOperation ? m_pOperation->getName() : "");
	return tmp;
}

long OperationEngineContext::getOperationId() const {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	const long tmp = m_pOperation ? m_pOperation->getId() : -1;
	return tmp;
}

std::string OperationEngineContext::getOperationGuid() const {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	if ( m_OperationGuid.empty() ) {
		DbGuidToIdMapper guidMapper(m_pDbConnection);
		m_OperationGuid = guidMapper.GetGuid(getOperationId(), OperationEntityId);
	}
	return string(m_OperationGuid);
}

DbConnection* OperationEngineContext::getTranDbConnection() const {
	return this->m_pDbConnection;
}

DataElement* OperationEngineContext::getDataElement(const std::string& sDataElementName) const {
	DataElement* de = 0;
	{
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	if ( strcasecmp(sDataElementName.c_str(), dataElementsToHandle[0]) == 0 ) {
		de = new DataElement(DataElement::t_string, m_sSourceLocator.c_str());
	}
	else if ( strcasecmp(sDataElementName.c_str(), dataElementsToHandle[1]) == 0 ) {
		de = new DataElement(DataElement::t_string, GetLeafPath(m_sSourceLocator).c_str());
	}
	else if ( strcasecmp(sDataElementName.c_str(), dataElementsToHandle[2]) == 0 ) {
		long long ll = m_lSourceSize;
		de = new DataElement(DataElement::t_long, ll);
	}
	else if ( strcasecmp(sDataElementName.c_str(), dataElementsToHandle[3]) == 0 ) {
		de = new DataElement(DataElement::t_string, getOperationName().c_str());
	}
	else if ( strcasecmp(sDataElementName.c_str(), dataElementsToHandle[4]) == 0 ) {
		de = new DataElement(DataElement::t_string, getLastError().c_str());
	}
	else if ( strcasecmp(sDataElementName.c_str(), dataElementsToHandle[5]) == 0 ) {
		de = new DataElement(DataElement::t_string, getError().c_str());
	}
	else if ( strcasecmp(sDataElementName.c_str(), dataElementsToHandle[6]) == 0 ) {
		de = new DataElement(DataElement::t_string, getOperationGuid().c_str());
	}
	}

	if ( de ) de->setName(sDataElementName.c_str());
	return de;
}

void OperationEngineContext::setSourceLocator(const std::string& sSourceLocator) {
	m_sSourceLocator = sSourceLocator;
}

void OperationEngineContext::setSourceSize(long lSourceSize) {
	m_lSourceSize = lSourceSize;
}

bool OperationEngineContext::isAlreadyTransferred(ProcessedFile& file) const {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	if ( m_pOperation != 0 ) {
		if( m_pOperation->ignoreHistory() )
			return false;
		return TransferHistory(m_pDbConnection).IsAlreadyTransferred(getOperationId(), file);
	}
	throw OperationEngineException("Error: OperationEngine: No Operation set when IsAlreadyTransferred was called.");
}

std::string OperationEngineContext::getLastError() const {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	const ErrorReporter* const p = getErrorReporter();
	if ( p ) {
		const string tmp(p->getLastError());
		return tmp;
	}
	return string();
}

std::string OperationEngineContext::getError() const {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	const ErrorReporter* const p = getErrorReporter();
	if ( p ) {
		const string tmp(p->getError());
		return tmp;
	}
	return string();
}

