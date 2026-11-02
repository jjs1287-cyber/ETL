/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_CHECKOUT_SCHEDULE_CHECKOUT_SPECIAL_HANDLING_H
#define KONGA_WS_CHECKOUT_SCHEDULE_CHECKOUT_SPECIAL_HANDLING_H

#include <string>
#include <vector>

#include "CheckoutSpecialHandling.h"

namespace konga {
namespace interchange_checkout {

	                                                                                  
	class ScheduleCheckoutSpecialHandling : public CheckoutSpecialHandling {
	public:
		ScheduleCheckoutSpecialHandling(void);
		~ScheduleCheckoutSpecialHandling(void) {}

		void HandleColumn(vColumnValuesT& vColumnValues);

	private:
		std::vector<std::string> m_vTimeStampColumnNames;
	};
}
}
#endif                                                          

