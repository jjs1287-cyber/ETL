/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <vector>
#include <memory>

#include <konga/Utils/util.h>	                
#include <konga/Utils/CrException.h>

#include <konga/DataElement/DataElement.h>

#include <konga/TransformationEngine/TransformationEngine.h>
#include <konga/TransformationEngine/RaiseErrorException.h>

#include <konga/TransformationUtils/FileTypeTargetCache.h>
#include <konga/TransformationUtils/DbLookupCache.h>

#include <konga/OperationObjects/OperationEngineException.h>

#include "OperationScriptRunner.h"

using namespace konga;
using std::vector;
using std::string;

OperationScriptRunner::OperationScriptRunner(boost::shared_ptr<OperationPipeline> pipeline,
											 OperationContext& context,
											 OperationDataElementHolder& deHolder,
											 DbConnection& dbConnection) :
m_pTransformationEngine(0), m_debug_log(0),
m_context(context),
m_deHolder(deHolder),
m_pipeline(pipeline),
m_scriptCache(&dbConnection) {}

OperationScriptRunner::~OperationScriptRunner(void) {
	DeleteLogs();
}

void OperationScriptRunner::RunScriptsBefore(ActivityType targetActivity) {
	if ( m_pipeline->empty() )
		return;

	vector<OperationActivity> scriptsToRun;
	OperationActivity activity = m_pipeline->getFirstActivity();
	while( activity.getActivityType() != targetActivity ) {
		const ActivityType type = activity.getActivityType();
		if ( type == ActivityType_Script )
			scriptsToRun.push_back(activity);
		else
			scriptsToRun.clear();

		if( !m_pipeline->hasMoreActivities() ) {
			scriptsToRun.clear();
			break;
		}
		activity = m_pipeline->getNextActivity();
	}

	if ( scriptsToRun.empty() )
		return;
	RunScripts(scriptsToRun);
}

void OperationScriptRunner::RunFinalScripts() {
	if ( m_pipeline->empty() )
		return;

	vector<OperationActivity> scriptsToRun;
	OperationActivity activity = m_pipeline->getFirstActivity();
	while( true ) {
		if ( activity.getActivityType() == ActivityType_Script )
			scriptsToRun.push_back(activity);
		else
			scriptsToRun.clear();
		if ( !m_pipeline->hasMoreActivities() )
			break;
		activity = m_pipeline->getNextActivity();
	}

	if ( scriptsToRun.empty() )
		return;
	RunScripts(scriptsToRun);
}

void OperationScriptRunner::DeleteLogs() {
	if ( m_pTransformationEngine == 0 )
		return;

	m_pTransformationEngine->getViewLog()->Delete();
	delete m_pTransformationEngine;
	m_pTransformationEngine = 0;
	m_debug_log = 0;
	m_sLogFileName.clear();
}

void OperationScriptRunner::CollectScriptWarnings(std::string& sWarnings) {
	if ( m_pTransformationEngine == 0 )
		return;

	m_pTransformationEngine->getViewLog()->SetLogFileName(m_sLogFileName);
	m_pTransformationEngine->getViewLog()->GetLogContents(sWarnings);
	DeleteLogs();
}

void OperationScriptRunner::RunScripts(const vector<OperationActivity>& scriptsToRun) {
	vector<OperationActivity>::const_iterator cit = scriptsToRun.begin();
	for(; cit != scriptsToRun.end(); ++cit ) {
		if ( cit->getActivityType() != ActivityType_Script )
			continue;
		RunScript(cit->getActivityId());
	}
}

void OperationScriptRunner::RunScript(long scriptId) {
	if ( m_pTransformationEngine == 0 ) {
		m_pTransformationEngine = new TransformationEngine(m_context.getCurrentUser(), &m_context, &m_deHolder);
		m_pTransformationEngine->getViewLog()->Initialize("RunScript", m_context.getOperationId(), m_debug_log, true, false, 0, 0, false, GetGUIDString());
		m_sLogFileName = m_pTransformationEngine->getViewLog()->GetLogFileName();
	}

	try {
		                                                            
		std::auto_ptr<DataElement> de( m_pTransformationEngine->RunScript(scriptId, m_scriptCache) );
		m_pTransformationEngine->getFileTargetCache().FlushAll(m_context.getCurrentUser());
		m_pTransformationEngine->closeDbLookup();
	}
	catch( const RaiseErrorException& ex ) {
		throw OperationEngineException(ex.GetError(), true, "RunScript");
	}
	catch( const CrException& ex ) {
		throw OperationEngineException(ex.GetErrorString(), true, "RunScript");
	}
#ifndef _DEBUG
	catch( const std::exception& ex ) {
		throw OperationEngineException(std::string("Unexpected script failure: ")+ex.what(), true, "RunScript");

	}
	catch(...) {
		throw OperationEngineException("Unexpected native script failure", true, "RunScript");
	}
#endif
}

