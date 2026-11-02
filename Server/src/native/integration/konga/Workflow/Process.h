/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef WS_PROCESS_H
#define WS_PROCESS_H

#include <map>
#include <string>
#include <vector>

#include <boost/noncopyable.hpp>

#include <konga/Messaging/MsgPriority.h>	                    

#include "TaskTypeEnum.h"		                      
#include "WorkflowIdTypes.h"

namespace konga {

class Task;
namespace workflow {
class DataElement;
}
class Document;
class Activity;
class ActivityRule;
class Route;
class Escalation;
class RouteRule;
class CreateScheduledEventActivity;

class Process : boost::noncopyable {
public:
	Process(process_id_t id = -1, process_version_t iVersion = -1) : 
	  m_id(id), m_iVersion(iVersion), m_priority(MSG_PRIORITY_NORMAL), m_ProcessType(ptREGULAR),
	  m_iActivityIdCounter(0), m_iActivityRuleIdCounter(0), m_iRouteIdCounter(0),
	  m_iRouteRuleIdCounter(0), m_iDataElementIdCounter(0), m_iEscalationIdCounter(0),
	  m_iDocumentIdCounter(0) {}
	virtual ~Process();

	void SetId(process_id_t id) { m_id = id; }
	process_id_t GetId() const { return m_id; }

	void SetVersion(process_version_t iVersion) { m_iVersion = iVersion; }
	process_version_t GetVersion() { return m_iVersion; }

	void SetPriority(MSG_PRIORITY priority) { m_priority = priority; }
	MSG_PRIORITY GetPriority() { return m_priority; }

	void SetProcessType(long pt) { m_ProcessType = ProcessTypeTag(pt); }
	void AddProcessType(long pt) { m_ProcessType |= pt; }
	void RemoveProcessType(long pt) { m_ProcessType &= ~pt; }
	long GetProcessType() const { return m_ProcessType; }

	bool IsDebugEnabled() { return (m_ProcessType & ptDEBUG) == ptDEBUG; }	
	bool IsAutoReRun() { return (m_ProcessType & ptAUTO_RERUN) == ptAUTO_RERUN; }	

	void SetName(const std::string& stName) { m_stName = stName; }
	std::string GetName() const { return m_stName; }

	void SetDescription(const std::string& stDescription) { m_stDescription = stDescription; }
	std::string GetDescription() const { return m_stDescription; }

	void SetOwner(const std::string& stOwner) { m_stOwner = stOwner; }
	std::string GetOwner() const { return m_stOwner; }

	void AddTask(Task* pTask);
	void AddDataElement(workflow::DataElement* pDataElement);
	void AddDocument(Document* pDocument);
	void AddActivity(Activity* pActivity);
	void AddActivityRule(ActivityRule* pActivityRule);
	void AddRoute(Route* pRoute);
	void AddRouteRule(RouteRule* pRouteRule);
	void AddEscalation(Escalation* pEscalation);

	                                                                                              
	void AddProcessDataElement(workflow::DataElement* pDataElement);
	bool IsProcessDataElement(const std::string& stName);

	std::vector<const workflow::DataElement*> GetProcessDataElements() const;

	                                                                          
	Task& GetTask(task_id_t id) const ;
	Task& GetTaskByName(const std::string& stTaskName) const;
	workflow::DataElement& GetDataElement(data_element_id_t id) const ;
	workflow::DataElement& GetDataElementByName(const std::string& stDataElementName) const;
	Document& GetDocument(document_id_t id) const ;
	Activity& GetActivity(activity_id_t id) const ;
	ActivityRule& GetActivityRule(activity_rule_id_t id) const ;
	Route& GetRoute(route_id_t id) const ;
	RouteRule& GetRouteRule(route_rule_id_t id) const ;
	Escalation& GetEscalation(escalation_id_t id) const ;

	                                                          
	activity_id_t IncrementActivityId() { return ++m_iActivityIdCounter; }
	activity_rule_id_t IncrementActivityRuleId() { return ++m_iActivityRuleIdCounter; }
	route_id_t IncrementRouteId() { return ++m_iRouteIdCounter; }
	route_rule_id_t IncrementRouteRuleId() { return ++m_iRouteRuleIdCounter; }
	escalation_id_t IncrementEscalationId() { return ++m_iEscalationIdCounter; }
	document_id_t IncrementDocumentId() { return ++m_iDocumentIdCounter; }

	data_element_id_t IncrementDataElementId() { return ++m_iDataElementIdCounter; }

	                                                                                 
	void ConnectRoutes();
	bool IsRoutePresent(task_id_t origTaskId, task_id_t destTaskId) const;
	bool IsRoutePresent(task_id_t origTaskId, std::string stDestTaskName) const;

	void AddPendingScheduledEvent(CreateScheduledEventActivity* pCreateScheduledEventActivity) {
		m_vUnfinishedPendingEvents.push_back(pCreateScheduledEventActivity);
	}

	void CompleteScheduledEvents();

	                 
	void PrintRoutes();
private:
	process_id_t m_id;
	process_version_t m_iVersion;

	MSG_PRIORITY m_priority;
	long m_ProcessType;

	std::string m_stName;
	std::string m_stDescription;
	std::string m_stOwner;	                                                     

	                                                                                  
	typedef std::map<task_id_t, Task*> TaskMapT;
	TaskMapT m_TaskMap;

	typedef std::map<data_element_id_t, workflow::DataElement*> DataElementMapT;
	DataElementMapT m_DataElementMap;

	typedef std::map<document_id_t, Document*> DocumentMapT;
	DocumentMapT m_DocumentMap;

	typedef std::map<route_id_t, Route*> RouteMapT;
	RouteMapT m_RouteMap;

	typedef std::map<escalation_id_t, Escalation*> EscalationMapT;
	EscalationMapT m_EscalationMap;

	typedef std::map<route_rule_id_t, RouteRule*> RouteRuleMapT;
	RouteRuleMapT m_RouteRuleMap;

	typedef std::map<activity_rule_id_t, ActivityRule*> ActivityRuleMapT;
	ActivityRuleMapT m_ActivityRuleMap;

	typedef std::map<activity_id_t, Activity*> ActivityMapT;
	ActivityMapT m_ActivityMap;

	std::vector<data_element_id_t> m_vProcessDataElements;

	std::vector<CreateScheduledEventActivity*> m_vUnfinishedPendingEvents;

	                                       
	activity_id_t m_iActivityIdCounter;
	activity_rule_id_t m_iActivityRuleIdCounter;
	route_id_t m_iRouteIdCounter;
	route_rule_id_t m_iRouteRuleIdCounter;
	data_element_id_t m_iDataElementIdCounter;
	escalation_id_t m_iEscalationIdCounter;
	document_id_t m_iDocumentIdCounter;
};

}                   

#endif                
