/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <string>
#include <sstream>	               

#include <konga/Workflow/Process.h>
#include <konga/Workflow/Task.h>
#include <konga/Workflow/Route.h>
#include <konga/Workflow/RouteRule.h>
#include <konga/Workflow/DataElement.h>
#include <konga/Workflow/Activity.h>
#include <konga/Workflow/ActivityRule.h>
#include <konga/Workflow/Document.h>

#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbException.h>
#include <konga/Db/DbUtils/DbUtils.h>	              

#include <konga/Db/PDb/ProcessTab.h>
#include <konga/Db/PDb/TaskTab.h>
#include <konga/Db/PDb/RouteTab.h>
#include <konga/Db/PDb/RoutingRuleTab.h>
#include <konga/Db/PDb/TaskDataElementsTab.h>
#include <konga/Db/PDb/ActivityTab.h>
#include <konga/Db/PDb/ActivityRuleTab.h>
#include <konga/Db/PDb/DocumentTab.h>

#include "WfDbWriter.h"

using namespace std;
using namespace konga;
using namespace konga::workflow;

WfDbWriter::WfDbWriter(void) :
	m_pDbConnection(0), m_iProcessId(0), m_iProcessVersion(0), m_bReplace(false), m_bDelete(false) { }

WfDbWriter::~WfDbWriter(void) { }

void WfDbWriter::WriteProcess(Process& process, const string& stConnectionString, WorkflowDbDeployType dt) {
	SmartDbPtr<DbConnection> pConnection(DbConnection::MakeInstance());
	try {
		m_pDbConnection = pConnection;	                                       
		m_pDbConnection->Open(stConnectionString);
		m_pDbConnection->SetAutoCommit(false);
		m_pDbConnection->SetTransactionIsolationLevel(READ_COMMITTED);

		WriteProcess(process, dt);

		m_pDbConnection->Commit();
	}
	catch( const DbException& ex ) {
		m_pDbConnection->Rollback();
		throw CrException(ex.GetErrorString());
	}
}

void WfDbWriter::WriteProcess(Process& process, WorkflowDbDeployType dt) {
	SmartDbPtr<PDb::ProcessTab> pProcessTab( PDb::ProcessTab::MakeInstance(m_pDbConnection) );

	                                                                                
	m_iProcessId = 1;
	m_iProcessVersion = 1;
	m_bDelete = false;
	m_bReplace = false;
	pProcessTab->Select( "WHERE ProcessName = '" + SqlEscape(process.GetName()) + "'" );
	if ( pProcessTab->Next() ) {
		m_iProcessId = pProcessTab->GetProcessId();
		switch(dt) {
			case dtNEW_VERSION:
				                                
				m_iProcessVersion = pProcessTab->GetProcessVersion() + 1;
				pProcessTab->Edit();
				break;
			case dtREPLACE_LAST_VERSION:
				                          
				m_iProcessVersion = pProcessTab->GetProcessVersion();
				pProcessTab->Edit();
				m_bReplace = true;
				break;
			case dtDELETE_PREVIOUS:
				                                                        
				m_iProcessVersion = pProcessTab->GetProcessVersion() + 1;
				pProcessTab->Delete();
				pProcessTab->AddNew();
				m_bDelete = true;
				break;
			default:
				throw CrException("Unknown deploy mode " + toString(static_cast<int>(dt)));
		}
	}
	else {
		                                                                 
		static const string stWhereMaxId("WHERE ProcessId IN (SELECT MAX(ProcessId) FROM ProcessTab)");
		pProcessTab->Select(stWhereMaxId);
		if ( pProcessTab->Next() )
			m_iProcessId = pProcessTab->GetProcessId() + 1;

		pProcessTab->AddNew();
	}

	pProcessTab->SetProcessId(m_iProcessId);
	pProcessTab->SetProcessVersion(m_iProcessVersion);
	pProcessTab->SetProcessName(process.GetName());
	pProcessTab->SetDescription(process.GetDescription());
	pProcessTab->SetErrorHandlerId(0);	                                                                     
	pProcessTab->SetOwner(process.GetOwner());
	pProcessTab->SetProcessType(process.GetProcessType());	                                                                                       

	pProcessTab->Update();

	DeleteFromTable("TaskDataElementsTab");
	WriteProcessDataElements(process);
	WriteTasks(process);
	WriteRoutes(process);
	WriteRoutingRules(process);
	WriteActivities(process);
	WriteActivityRules(process);
	WriteDocuments(process);

	process.SetId(m_iProcessId);
	process.SetVersion(m_iProcessVersion);
}

void WfDbWriter::WriteProcessDataElements(const Process& process) {
	typedef vector<const DataElement*> vDE;
	vDE vProcessDataElements( process.GetProcessDataElements());

	SmartDbPtr<PDb::TaskDataElementsTab> pTaskDETab( PDb::TaskDataElementsTab::MakeInstance(m_pDbConnection) );
	for( vDE::const_iterator it = vProcessDataElements.begin(); it != vProcessDataElements.end(); ++it ) {
		const DataElement* pDE = *it;
		pTaskDETab->AddNew();
		pTaskDETab->SetProcessId(m_iProcessId);
		pTaskDETab->SetProcessVersion(m_iProcessVersion);
		pTaskDETab->SetTaskId(0);
		pTaskDETab->SetDataElementId(static_cast<int>(pDE->GetId()));

		pTaskDETab->SetDataElementName(pDE->GetName());
		pTaskDETab->SetDataElementType(pDE->GetType());
		pTaskDETab->SetOriginType("Process");
		pTaskDETab->SetInitialValue(pDE->GetInitialValue());
		pTaskDETab->SetIsRequired(pDE->IsRequired() ? 1 : 0);
		pTaskDETab->SetDataElementDisplayName(pDE->GetDisplayName());
		pTaskDETab->Update();
	}
}

void WfDbWriter::DeleteFromTable(const std::string& stTableName) const {
	if ( m_bDelete || m_bReplace ) {
		stringstream sSql;
		sSql << "DELETE FROM " << stTableName << " WHERE  ProcessId=" << m_iProcessId;
		if ( m_bReplace )
			sSql << " AND ProcessVersion=" << m_iProcessVersion;
		m_pDbConnection->ExecuteSql(sSql.str());
	}
}

void WfDbWriter::WriteTasks(const Process& process) {
	DeleteFromTable("TaskTab");

	try {
		SmartDbPtr<PDb::TaskTab> pTaskTab( PDb::TaskTab::MakeInstance(m_pDbConnection) );
		for( task_id_t tid=1; true; ++tid ) {
			const Task& task = process.GetTask(tid);
			pTaskTab->AddNew();
			pTaskTab->SetProcessId(m_iProcessId);
			pTaskTab->SetProcessVersion(m_iProcessVersion);
			pTaskTab->SetTaskId(static_cast<int>(tid));
			pTaskTab->SetTaskName(task.GetName());
			pTaskTab->SetDescription(task.GetDescription());

			pTaskTab->SetErrorHandlerId(0);	                                                                     
			pTaskTab->SetTaskRole(task.GetTaskRole());
			pTaskTab->SetDelegationRoleToNull();	                                      
			pTaskTab->SetRoutingRuleId(static_cast<int>(task.GetRouteRule()));
			pTaskTab->SetDocumentIdList(task.GetDocumentIdListAsString());
			pTaskTab->SetActivityRuleId(static_cast<int>(task.GetActivityRule()));
			pTaskTab->SetPreRoleActivityRuleId(static_cast<int>(task.GetPreRoleActivityRule()));
			pTaskTab->SetTaskType(task.GetTaskType());
			pTaskTab->SetTaskPriority(task.GetPriority());

			pTaskTab->Update();

			WriteTaskDataElements(task);
		}
	}
	catch( const DbException& ex) {
		throw ex;
	}
	catch( const CrException&) {
		                         
	}

}

void WfDbWriter::WriteTaskDataElements(const Task& task) {
	                                         

	typedef vector<const DataElement*> vDE;
	vDE vTaskDataElements( task.GetDataElements());

	SmartDbPtr<PDb::TaskDataElementsTab> pTaskDETab( PDb::TaskDataElementsTab::MakeInstance(m_pDbConnection) );
	for( vDE::const_iterator it = vTaskDataElements.begin(); it != vTaskDataElements.end(); ++it ) {
		const DataElement* pDE = *it;
		pTaskDETab->AddNew();
		pTaskDETab->SetProcessId(m_iProcessId);
		pTaskDETab->SetProcessVersion(m_iProcessVersion);
		pTaskDETab->SetTaskId(static_cast<int>(task.GetId()));
		pTaskDETab->SetDataElementId(static_cast<int>(pDE->GetId()));

		pTaskDETab->SetDataElementName(pDE->GetName());
		pTaskDETab->SetDataElementDescription(pDE->GetDescription());
		pTaskDETab->SetDataElementType(pDE->GetType());
		pTaskDETab->SetOriginType(pDE->GetOriginType());
		pTaskDETab->SetInitialValue(pDE->GetInitialValue());
		pTaskDETab->SetIsRequired(pDE->IsRequired() ? 1 : 0);
		pTaskDETab->SetDataElementDisplayName(pDE->GetDisplayName());
		pTaskDETab->Update();
	}
}

void WfDbWriter::WriteRoutes(const Process& process) {
	DeleteFromTable("RouteTab");

	try {
		                                                                                                                            
		                                                                  
		                                                                                                                                    
		SmartDbPtr<PDb::RouteTab> pRouteTab( PDb::RouteTab::MakeInstance(m_pDbConnection) );
		for( route_id_t rid=1; true; ++rid ) {
			Route& route = process.GetRoute(rid);
			pRouteTab->AddNew();
			pRouteTab->SetProcessId(m_iProcessId);
			pRouteTab->SetProcessVersion(m_iProcessVersion);

			pRouteTab->SetOriginTaskId(static_cast<int>(route.GetOriginTaskId()));
			pRouteTab->SetDestinationTaskId(static_cast<int>(route.GetDestinationTaskId()));
			pRouteTab->SetDataElementList("");	                                                                                                    
			pRouteTab->Update();
		}
	}
	catch( const DbException& ex ) {
		throw ex;
	}
	catch( const CrException& ) {
		                        
	}
}

void WfDbWriter::WriteRoutingRules(const Process& process) {
	DeleteFromTable("RoutingRuleTab");

	try {
		SmartDbPtr<PDb::RoutingRuleTab> pRoutingRuleTab( PDb::RoutingRuleTab::MakeInstance(m_pDbConnection) );
		for( route_rule_id_t rrid=1; true; ++rrid ) {
			RouteRule& rule = process.GetRouteRule(rrid);
			pRoutingRuleTab->AddNew();
			pRoutingRuleTab->SetProcessId(m_iProcessId);
			pRoutingRuleTab->SetProcessVersion(m_iProcessVersion);
			pRoutingRuleTab->SetRoutingRuleId(static_cast<int>(rrid));

			pRoutingRuleTab->SetSequenceStack(rule.GetSequenceStack());
			pRoutingRuleTab->SetDestinationTaskId(static_cast<int>(rule.GetDestinationTaskId()));

			pRoutingRuleTab->SetTimeToEscalation(toString(rule.GetMinutesToEscalation()));
			pRoutingRuleTab->SetEscalationDestinationTaskId(static_cast<int>(rule.GetEscalationDestinationTaskId()));

			pRoutingRuleTab->Update();
		}
	}
	catch( const DbException& ex ) {
		throw ex;
	}
	catch( const CrException& ) {
		                        
	}
}

void WfDbWriter::WriteActivities(const Process& process) {
	DeleteFromTable("ActivityTab");

	try {
		SmartDbPtr<PDb::ActivityTab> pActivityTab( PDb::ActivityTab::MakeInstance(m_pDbConnection) );
		for( activity_id_t aid=1; true; ++aid ) {
			Activity& activity = process.GetActivity(aid);
			pActivityTab->AddNew();
			pActivityTab->SetProcessId(m_iProcessId);
			pActivityTab->SetProcessVersion(m_iProcessVersion);
			pActivityTab->SetActivityId(static_cast<int>(aid));
			
			pActivityTab->SetActivityName(activity.GetName());
			pActivityTab->SetActivityMethod(activity.GetMethod());
			pActivityTab->SetActivityLocation(activity.GetLocation());
			pActivityTab->SetErrorHandlerId(0);	                                                                     
			pActivityTab->SetHasInstance(activity.HasInstance() ? 1 : 0);

			pActivityTab->Update();
		}
	}
	catch( const DbException& ex ) {
		throw ex;
	}
	catch( const CrException& ) {
		                        
	}

}

void WfDbWriter::WriteActivityRules(const Process& process) {
	DeleteFromTable("ActivityRuleTab");

	try {
		SmartDbPtr<PDb::ActivityRuleTab> pActivityRuleTab( PDb::ActivityRuleTab::MakeInstance(m_pDbConnection) );
		for( activity_rule_id_t arid=1; true; ++arid ) {
			ActivityRule & rule = process.GetActivityRule(arid);
			pActivityRuleTab->AddNew();
			pActivityRuleTab->SetProcessId(m_iProcessId);
			pActivityRuleTab->SetProcessVersion(m_iProcessVersion);
			pActivityRuleTab->SetActivityRuleId( static_cast<int>(arid) );
			
			pActivityRuleTab->SetActivityId( static_cast<int>(rule.GetActivityId()) );
			pActivityRuleTab->SetTrueActivityList(rule.GetTrueActivityListAsString());
			pActivityRuleTab->SetFalseActivityList(rule.GetFalseActivityListAsString());
			pActivityRuleTab->SetErrorHandlerId(0);	                                                                     

			pActivityRuleTab->Update();
		}
	}
	catch( const DbException& ex ) {
		throw ex;
	}
	catch( const CrException& ) {
		                        
	}
}

void WfDbWriter::WriteDocuments(const Process& process) {
	DeleteFromTable("DocumentTab");
	try {
		SmartDbPtr<PDb::DocumentTab> pDocumentTab( PDb::DocumentTab::MakeInstance(m_pDbConnection) );
		for( document_id_t did=1; true; ++did ) {
			Document & doc = process.GetDocument(did);
			pDocumentTab->AddNew();
			pDocumentTab->SetProcessId(m_iProcessId);
			pDocumentTab->SetProcessVersion(m_iProcessVersion);
			pDocumentTab->SetDocumentId( static_cast<int>(doc.GetId()) );

			pDocumentTab->SetTaskId( static_cast<int>(doc.GetTaskId()) );

			pDocumentTab->SetMsgTemplateName( doc.GetMsgTemplateName() );
			pDocumentTab->SetMsgTemplateLocation( doc.GetMsgTemplateLocation() );
			pDocumentTab->SetURL( doc.GetUrl() );
			pDocumentTab->SetSendFormat( doc.GetSendFormat() );
			pDocumentTab->SetToDocDataElementList( doc.GetToDocDataElementsAsString() );
			pDocumentTab->SetFromDocDataElementList( doc.GetFromDocDataElementsAsString() );
			pDocumentTab->SetMsgSubject( doc.Subject() );
			pDocumentTab->SetMsgCC( doc.CC() );
			pDocumentTab->SetMsgBCC( doc.BCC() );
			pDocumentTab->SetMsgPriority( doc.GetPriority() );
			pDocumentTab->SetMsgReplyTo( doc.ReplyTo() );
			pDocumentTab->SetMsgFrom( doc.From() );

			pDocumentTab->Update();
		}
	}
	catch( const DbException& ex ) {
		throw ex;
	}
	catch( const CrException& ) {
		                        
	}
}

