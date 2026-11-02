/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef ROUTE_H
#define ROUTE_H

#include <string>
#include <vector>
#include <sstream>

#include "Task.h"
#include "Process.h"
#include "ActivityItem.h"
#include "WorkflowIdTypes.h"

namespace konga {

	class Route : public ActivityItem {
	public:
		Route(Process& process, route_id_t id = -1, task_id_t originTaskId = -1, task_id_t destinationTaskId = -1) :
			m_process(process), m_id(id),
			m_OriginTaskId(originTaskId), m_DestinationTaskId(destinationTaskId) {}
		Route(Process& process, route_id_t id, task_id_t originTaskId, const std::string& stDestinationTaskName) :
			m_process(process), m_id(id),
			m_OriginTaskId(originTaskId), m_DestinationTaskId(-1),
			m_stDestinationTaskName(stDestinationTaskName) {}
		virtual ~Route() {}

		route_id_t GetId() const { return m_id; }
		void SetId(route_id_t id) { m_id = id; }

		void SetOriginTaskId(task_id_t id) { m_OriginTaskId = id; }
		task_id_t GetOriginTaskId() { return m_OriginTaskId; }

		void SetDestinationTaskId(task_id_t id) { m_DestinationTaskId = id; }
		task_id_t GetDestinationTaskId() { return m_DestinationTaskId; }

		std::string getDestinationTaskName() const {
			if ( m_DestinationTaskId == -1 )
				return m_stDestinationTaskName;
			else {
				return m_process.GetTask(m_DestinationTaskId).GetName();
			}
		}

		void AddDataElement(data_element_id_t id) { m_vDataElementList.push_back(id); }

		std::string GetActivityListItemAsString() const {
			std::stringstream s;
			s << "R" << GetId();
			return s.str();
		}

	private:
		Process& m_process;
		route_id_t m_id;
		task_id_t m_OriginTaskId;
		task_id_t m_DestinationTaskId;
		std::string m_stDestinationTaskName;                                                                         

		std::vector<data_element_id_t> m_vDataElementList;
	};

}                   

#endif           
