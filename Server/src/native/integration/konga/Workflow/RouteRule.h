/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef ROUTE_RULE_H
#define ROUTE_RULE_H

#include <string>

#include "WorkflowIdTypes.h"

namespace konga {

	class RouteRule {
	public:
		RouteRule(route_rule_id_t id, task_id_t destinationTaskId, const std::string& stSequenceStack = "") :
			m_id(id), m_destinationTaskId(destinationTaskId), m_escalationDestinationTaskId(-1),
			m_stMinutesToEscalation(""), m_stSequenceStack(stSequenceStack) {}
		virtual ~RouteRule() {}

		route_rule_id_t GetId() const { return m_id; }
		void SetId(route_rule_id_t id) { m_id = id; }

		task_id_t GetDestinationTaskId() { return m_destinationTaskId; }
		void SetDestinationTaskId(task_id_t destinationTaskId) { m_destinationTaskId = destinationTaskId; }

		task_id_t GetEscalationDestinationTaskId() { return m_escalationDestinationTaskId; }
		void SetEscalationDestinationTaskId(task_id_t escalationDestinationTaskId) { m_escalationDestinationTaskId = escalationDestinationTaskId; }

		std::string GetSequenceStack() const { return m_stSequenceStack; }

		std::string GetMinutesToEscalation() { return m_stMinutesToEscalation; }
		void SetMinutesToEscalation(const std::string& stMinutesToEscalation) { m_stMinutesToEscalation = stMinutesToEscalation; }

	private:
		route_rule_id_t m_id;
		task_id_t m_destinationTaskId, m_escalationDestinationTaskId;
		std::string m_stMinutesToEscalation;            
		std::string m_stSequenceStack;
	};

}                   

#endif                
