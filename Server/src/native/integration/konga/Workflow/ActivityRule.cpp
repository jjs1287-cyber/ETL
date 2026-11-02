/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#include <sstream>

#include "Activity.h"
#include "ActivityRule.h"

using namespace std;
using namespace konga;

                    
namespace {
	string getActivityList(const vector<const ActivityItem*> items) {
		stringstream sList;
		for( vector<const ActivityItem*>::const_iterator it = items.begin(); it != items.end(); ++it ) {
			sList << (*it)->GetActivityListItemAsString() << ";";
		}
		return sList.str();
	}
}

string ActivityRule::GetTrueActivityListAsString() const {
	return getActivityList(m_TrueActivityList);
}

string ActivityRule::GetFalseActivityListAsString() const {
	return getActivityList(m_FalseActivityList);
}

string ActivityRule::GetActivityListItemAsString() const {
	stringstream s;
	s << "A" << m_process.GetActivityRule(GetId()).GetId();
	return s.str();
}
