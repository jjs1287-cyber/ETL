/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef ACTIVITY_RULE_H
#define ACTIVITY_RULE_H

#include <string>
#include <vector>

#include "Process.h"
#include "ActivityItem.h"
#include "WorkflowIdTypes.h"

namespace konga {

	                                                                 
	                                
	class ActivityRule : public ActivityItem {
	public:
		ActivityRule(Process& process, activity_id_t aid, activity_rule_id_t arid)
			: m_process(process), m_id(arid), m_activityId(aid) {}
		virtual ~ActivityRule() {}

		activity_rule_id_t GetId() const { return m_id; }
		void SetId(activity_rule_id_t id) { m_id = id; }

		Activity& GetActivity() { return m_process.GetActivity(m_activityId); };
		activity_id_t GetActivityId() { return m_activityId; };

		void AddToTrueList(const ActivityItem* item) { m_TrueActivityList.push_back(item); }
		void AddToFalseList(const ActivityItem* item) { m_FalseActivityList.push_back(item); }

		std::string GetTrueActivityListAsString() const;
		std::string GetFalseActivityListAsString() const;

		std::string GetActivityListItemAsString() const;

	private:
		Process& m_process;
		activity_rule_id_t m_id;
		activity_id_t m_activityId;

		                                           
		std::vector<const ActivityItem*> m_TrueActivityList;                                                              
		std::vector<const ActivityItem*> m_FalseActivityList;                                                               
	};

}                   

#endif                   
