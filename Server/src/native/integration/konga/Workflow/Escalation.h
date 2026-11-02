/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef WF_ESCALATION_H
#define WF_ESCALATION_H

#include <string>

#include <konga/Utils/StringUtils.h>

#include "WorkflowIdTypes.h"

namespace konga {

class Escalation {
public:
	Escalation(escalation_id_t id) : m_id(id), m_nMinutesToEscalation(-1), m_escalationTask(-1), m_escalatedTask(-1) {}
	virtual ~Escalation() {}

	static int GetTimeFromString(const std::string& stTime);

	escalation_id_t GetId() const { return m_id; }
	void SetId(escalation_id_t id) { m_id = id; }

	std::string GetEscalationTaskName() const { return m_stEscalationTaskName; }
	void SetEscalationTaskName(const std::string& stEscalationTaskName) { m_stEscalationTaskName = stEscalationTaskName; }

	task_id_t GetEscalationTask() const { return m_escalationTask; }
	void SetEscalationTask(task_id_t escalationTask) { m_escalationTask = escalationTask; }

	task_id_t GetEscalatedTask() const { return m_escalatedTask; }
	void SetEscalatedTask(task_id_t escalatedTask) { m_escalatedTask = escalatedTask; }

	                                   
	unsigned int GetMinutesToEscalation() const { return m_nMinutesToEscalation; }
	std::string GetMinutesToEscalationAsString() const ;

	void SetMinutesToEscalation(unsigned int nMinutesToEscalation) { m_nMinutesToEscalation = nMinutesToEscalation; }
	void SetMinutesToEscalation(const std::string& stMinutesToEscalation);

private:
	escalation_id_t m_id;

	std::string m_stMinutesToEscalation;                                                             
	long m_nMinutesToEscalation;                                                         

	task_id_t m_escalationTask;                                                    
	task_id_t m_escalatedTask;	                                                                                  
	std::string m_stEscalationTaskName;                                                                                                      
};

}                   

#endif                   

