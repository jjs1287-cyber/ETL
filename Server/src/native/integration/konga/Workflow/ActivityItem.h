/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_ACTIVITYITEM_H
#define KONGA_ACTIVITYITEM_H

#include <vector>

namespace konga {
	                                                                                                        
	class ActivityItem {
	public:
		ActivityItem(void) { }

		virtual ~ActivityItem(void) { }

		                                                                                 
		virtual std::string GetActivityListItemAsString() const = 0;
	};
}                   

#endif                        

