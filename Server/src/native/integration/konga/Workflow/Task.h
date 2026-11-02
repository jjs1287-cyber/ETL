/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef WF_TASK_H
#define WF_TASK_H

                
#include <string>
#include <vector>

#include <konga/Messaging/MsgPriority.h>	                    

#include <konga/Utils/CrException.h>	  
#include <konga/Utils/StringUtils.h>	             

#include "Process.h"
#include "Document.h"
#include "Activity.h"
#include "ActivityRule.h"
#include "RouteRule.h"

#include "TaskTypeEnum.h"
#include "WorkflowIdTypes.h"

namespace konga {

class Task {
public:
	Task(Process& process, task_id_t id = -1) : m_Process(process), m_id(id), 
		m_priority(MSG_PRIORITY_NORMAL), m_TaskType(ttREGULAR), 
		m_iErrorHandlerId(0), m_iActivityRuleId(0), m_iPreRoleActivityRuleId(0),
		m_iRouteRuleId(0), m_iEscalationId(0) {}
	virtual ~Task() { }

	void SetId(task_id_t id) { m_id = id; }
	task_id_t GetId() const { return m_id; }

	Process& GetProcess() const { return m_Process; }

	void SetErrorHandler(activity_rule_id_t id) { m_iErrorHandlerId = id; }
	activity_rule_id_t GetErrorHandler() const { return m_iErrorHandlerId; }

	void SetActivityRule(activity_rule_id_t id) { m_iActivityRuleId = id; }
	activity_rule_id_t GetActivityRule() const { return m_iActivityRuleId; }

	void SetPreRoleActivityRule(activity_rule_id_t id) { m_iPreRoleActivityRuleId = id; }
	activity_rule_id_t GetPreRoleActivityRule() const { return m_iPreRoleActivityRuleId; }

	void SetRouteRule(route_rule_id_t id) { m_iRouteRuleId = id; }
	route_rule_id_t GetRouteRule() const { return m_iRouteRuleId; }

	void SetName(const std::string& stName) { m_stName = stName; }
	std::string GetName() const { return m_stName; }

	void SetDescription(const std::string& stDescription) { m_stDescription = stDescription; }
	std::string GetDescription() const { return m_stDescription; }

	void SetTaskRole(const std::string& stTaskRole) { m_stTaskRole = stTaskRole; }
	std::string GetTaskRole() const { return m_stTaskRole; }

	void AddDataElement(workflow::DataElement* pDataElement);
	void AddDataElement(data_element_id_t id) { m_vDataElements.push_back(id); }
	workflow::DataElement& GetDataElement(data_element_id_t id) const { return m_Process.GetDataElement(id); }
	std::vector<const workflow::DataElement*> GetDataElements() const;

	void AddDocument(document_id_t id) { m_vDocuments.push_back(id); }
	Document& GetDocument(document_id_t id) const { return m_Process.GetDocument(id); }
	std::string GetDocumentIdListAsString() const;

	void AddActivity(Activity* pActivity);

	ActivityRule& GetActivityRule(activity_rule_id_t id) const { return m_Process.GetActivityRule(id); }

	void SetPriority(MSG_PRIORITY priority) { m_priority = priority; }
	MSG_PRIORITY GetPriority() const { return m_priority; }

	void SetTaskType(long tt) { m_TaskType = TaskTypeTag(tt); }
	int GetTaskType() const { return m_TaskType; }
	void AddTaskType(long tt) { m_TaskType |= tt; }
	void RemoveTaskType(long tt) { m_TaskType &= ~tt; }

	                                                                              
	bool HasDataElement(const std::string& stName);

	bool IsEscalatingTask() { return (m_TaskType & ttESCALATION) != 0; }
	bool IsInitialTask() { return (m_TaskType & ttINITIAL) == ttINITIAL; }
	bool IsTerminatingTask() { return (m_TaskType & ttTERMINATING) == ttTERMINATING; }
	bool IsNotificationTask() { return (m_TaskType & ttNOTIFICATION) == ttNOTIFICATION; }
	bool IsMachineSpecificTask() { return (m_TaskType & ttMACHINE_SPECIFIC) == ttMACHINE_SPECIFIC; }
	bool IsCrUpdateTask() { return (m_TaskType & ttCR_UPDATE) == ttCR_UPDATE; }	
	bool IsDebugEnabled() { if( m_Process.IsDebugEnabled() ) return true; return (m_TaskType & ttDEBUG) == ttDEBUG; }	
	bool IsAutoReRun() { return m_Process.IsAutoReRun(); }

	escalation_id_t GetEscalationId() const { return m_iEscalationId; }
	void SetEscalationId(escalation_id_t iEscalationId) { m_iEscalationId = iEscalationId; }

private:
	Process& m_Process;
	task_id_t m_id;

	MSG_PRIORITY m_priority;
	long m_TaskType;

	activity_rule_id_t m_iErrorHandlerId;		                                          
	activity_rule_id_t m_iActivityRuleId;	                                                                                                               
	activity_rule_id_t m_iPreRoleActivityRuleId;	                                                                                                                
	route_rule_id_t m_iRouteRuleId;	                                                                                                    
	
	std::string m_stName, m_stDescription;
	std::string m_stTaskRole;	                                     

	escalation_id_t m_iEscalationId;

	typedef std::vector<document_id_t> DocumentListT;
	DocumentListT m_vDocuments;

	typedef std::vector<data_element_id_t> DataElementListT;
	DataElementListT m_vDataElements;

	typedef std::vector<activity_id_t> ActivityListT;
	ActivityListT m_vActivities;
};

}                   

#endif             
