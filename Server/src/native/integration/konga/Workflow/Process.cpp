/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <iostream>
#include <map>

#include "Task.h"
#include "DataElement.h"
#include "Document.h"
#include "Activity.h"
#include "ActivityRule.h"
#include "Route.h"
#include "RouteRule.h"
#include "Escalation.h"
#include "Process.h"

using namespace konga;
using namespace konga::workflow;
using namespace std;

                     
namespace {
	                                                                                          
	template<class MapT>
	void DeleteMapEntries(MapT& m) {
		typename MapT::iterator it = m.begin();
		for( ; it != m.end(); ++it) {
			delete it->second;
		}
		m.clear();
	}
}

namespace konga {

Process::~Process() {
	                                                            
	DeleteMapEntries<TaskMapT>(m_TaskMap);
	DeleteMapEntries<DataElementMapT>(m_DataElementMap);
	DeleteMapEntries<DocumentMapT>(m_DocumentMap);
	DeleteMapEntries<RouteMapT>(m_RouteMap);
	DeleteMapEntries<EscalationMapT>(m_EscalationMap);
	DeleteMapEntries<RouteRuleMapT>(m_RouteRuleMap);
	DeleteMapEntries<ActivityRuleMapT>(m_ActivityRuleMap);
	DeleteMapEntries<ActivityMapT>(m_ActivityMap);
}

void Process::AddTask(Task* pTask) { m_TaskMap[ pTask->GetId() ] = pTask; }
void Process::AddActivity(Activity* pActivity) { m_ActivityMap[ pActivity->GetId() ] = pActivity; }
void Process::AddDataElement(DataElement* pDataElement) { m_DataElementMap[ pDataElement->GetId() ] = pDataElement; }
void Process::AddDocument(Document* pDocument) { m_DocumentMap[ pDocument->GetId() ] = pDocument; }

void Process::AddActivityRule(ActivityRule* pActivityRule) { m_ActivityRuleMap[ pActivityRule->GetId() ] = pActivityRule; }
void Process::AddRoute(Route* pRoute) { m_RouteMap[ pRoute->GetId() ] = pRoute; }
void Process::AddRouteRule(RouteRule* pRouteRule) { m_RouteRuleMap[ pRouteRule->GetId() ] = pRouteRule; }
void Process::AddEscalation(Escalation* pEscalation) { m_EscalationMap[ pEscalation->GetId() ] = pEscalation; }

void Process::AddProcessDataElement(DataElement* pDataElement) {
	AddDataElement(pDataElement);
	m_vProcessDataElements.push_back(pDataElement->GetId());
}

bool Process::IsProcessDataElement(const std::string& stName) {
	try {
		DataElement& de = GetDataElementByName(stName);
		data_element_id_t deId = de.GetId();
		for( vector<data_element_id_t>::const_iterator it = m_vProcessDataElements.begin();
			it != m_vProcessDataElements.end(); ++it ) {
				if ( deId == *it )
					return true;
			}
	}
	catch( const CrException& ) {
		                                                                        
		return false;
	}
	return false;
}

vector<const DataElement*> Process::GetProcessDataElements() const {
	vector<const DataElement*> vDE;
	for( vector<data_element_id_t>::const_iterator it = m_vProcessDataElements.begin(); it != m_vProcessDataElements.end(); ++it ) {
		vDE.push_back( &GetDataElement(*it) );
	}
	return vDE;
}

Task& Process::GetTask(task_id_t id) const {
	TaskMapT::const_iterator citTask = m_TaskMap.find(id);
	if( citTask != m_TaskMap.end() )
		return *citTask->second;
	throw CrException("Task with id = " + konga::toString(id) + " was not found in process with id = " + konga::toString(m_id) + ".");
}

Task& Process::GetTaskByName(const string& stTaskName) const {
	for( TaskMapT::const_iterator it = m_TaskMap.begin(); it != m_TaskMap.end(); ++it ) {
		if ( it->second->GetName() == stTaskName )
			return *it->second;
	}
	throw CrException("A Task with the name \"" + stTaskName + " was not found in process with id = " + konga::toString(m_id) + ".");
}

DataElement& Process::GetDataElement(data_element_id_t id) const {
	DataElementMapT::const_iterator citDataElement = m_DataElementMap.find(id);
	if( citDataElement != m_DataElementMap.end() )
		return *citDataElement->second;
	throw CrException("Data Element with id = " + konga::toString(id) + " was not found in process with id = " + konga::toString(m_id) + ".");
}

DataElement& Process::GetDataElementByName(const std::string& stDataElementName) const {
	for( DataElementMapT::const_iterator it = m_DataElementMap.begin(); it != m_DataElementMap.end(); ++it ) {
		if ( it->second->GetName() == stDataElementName ) {
			return *it->second;
		}
	}
	throw CrException("No Data Element with the name \"" + stDataElementName + "\" has been defined in the process.");
}

Document& Process::GetDocument(document_id_t id) const {
	DocumentMapT::const_iterator citDocument = m_DocumentMap.find(id);
	if( citDocument != m_DocumentMap.end() )
		return *citDocument->second;
	throw CrException("Document with id = " + konga::toString(id) + " was not found in process with id = " + konga::toString(m_id) + ".");
}

Activity& Process::GetActivity(activity_id_t id) const {
	ActivityMapT::const_iterator citActivity = m_ActivityMap.find(id);
	if( citActivity != m_ActivityMap.end() )
		return *citActivity->second;
	throw CrException("Activity with id = " + konga::toString(id) + " was not found in process with id = " + konga::toString(m_id) + ".");
}

ActivityRule& Process::GetActivityRule(activity_rule_id_t id) const {
	ActivityRuleMapT::const_iterator citActivityRule = m_ActivityRuleMap.find(id);
	if( citActivityRule != m_ActivityRuleMap.end() )
		return *citActivityRule->second;
	throw CrException("ActivityRule with id = " + konga::toString(id) + " was not found in process with id = " + konga::toString(m_id) + ".");
}

Route& Process::GetRoute(route_id_t id) const {
	RouteMapT::const_iterator citRoute = m_RouteMap.find(id);
	if( citRoute != m_RouteMap.end() )
		return *citRoute->second;
	throw CrException("Route with id = " + konga::toString(id) + " was not found in process with id = " + konga::toString(m_id) + ".");
}

Escalation& Process::GetEscalation(escalation_id_t id) const {
	EscalationMapT::const_iterator citEscalation = m_EscalationMap.find(id);
	if( citEscalation != m_EscalationMap.end() )
		return *citEscalation->second;
	throw CrException("Escalation with id = " + konga::toString(id) + " was not found in process with id = " + konga::toString(m_id) + ".");
}

RouteRule& Process::GetRouteRule(route_rule_id_t id) const {
	RouteRuleMapT::const_iterator citRouteRule = m_RouteRuleMap.find(id);
	if( citRouteRule != m_RouteRuleMap.end() )
		return *citRouteRule->second;
	throw CrException("RouteRule with id = " + konga::toString(id) + " was not found in process with id = " + konga::toString(m_id) + ".");
}

void Process::ConnectRoutes() {
	for( RouteMapT::const_iterator routeIt = m_RouteMap.begin(); routeIt != m_RouteMap.end(); ++routeIt ) {
		Route* route = routeIt->second;
		if ( route->GetDestinationTaskId() == -1 ) {
			                                           
			const string stDestinationTaskName(route->getDestinationTaskName());
			try {
				Task& task = GetTaskByName(stDestinationTaskName);
				route->SetDestinationTaskId(task.GetId());
			}
			catch( const CrException& ex ) {
				string stError("Failed to find the destination Task for Route from the Task \"" + GetTask(route->GetOriginTaskId()).GetName());
				stError += "\" .";
				stError += ex.GetErrorString();
				throw CrException(stError);
			}
		}
	}
}

bool Process::IsRoutePresent(task_id_t origTaskId, task_id_t destTaskId) const {
	for( RouteMapT::const_iterator routeIt = m_RouteMap.begin(); routeIt != m_RouteMap.end(); ++routeIt ) {
		Route* route = routeIt->second;
		if ( route->GetOriginTaskId() == origTaskId && route->GetDestinationTaskId() == origTaskId )
			return true;
	}
	return false;
}

bool Process::IsRoutePresent(task_id_t origTaskId, string stDestTaskName) const {
	for( RouteMapT::const_iterator routeIt = m_RouteMap.begin(); routeIt != m_RouteMap.end(); ++routeIt ) {
		Route* route = routeIt->second;
		if ( route->GetOriginTaskId() == origTaskId && route->getDestinationTaskName() == stDestTaskName )
			return true;
	}
	return false;
}

void Process::PrintRoutes() {
	for( RouteMapT::const_iterator routeIt = m_RouteMap.begin(); routeIt != m_RouteMap.end(); ++routeIt ) {
		Route* route = routeIt->second;
		cout << "Route " << route->GetId() << ": " << route->GetOriginTaskId() << " --> " << 
			route->GetDestinationTaskId() << " " << (route->getDestinationTaskName().empty()?"":route->getDestinationTaskName()) << endl;
	}
}

void Process::CompleteScheduledEvents() {
	for( vector<CreateScheduledEventActivity*>::const_iterator it = m_vUnfinishedPendingEvents.begin();
		it != m_vUnfinishedPendingEvents.end(); ++it ) {
			const string stTaskName( (*it)->GetTaskName() );
			Task& task = GetTaskByName(stTaskName);
			(*it)->SetTaskId(task.GetId());
	}
	m_vUnfinishedPendingEvents.erase(m_vUnfinishedPendingEvents.begin(), m_vUnfinishedPendingEvents.end());
}

}                   
