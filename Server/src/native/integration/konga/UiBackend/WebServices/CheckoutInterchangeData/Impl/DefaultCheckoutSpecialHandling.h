/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEFAULT_CHECKOUT_SPECIAL_HANDLING_H
#define KONGA_WS_DEFAULT_CHECKOUT_SPECIAL_HANDLING_H

#include <string>

#include "CheckoutSpecialHandling.h"

namespace konga {
	class GuidToIdMapper;

namespace interchange_checkout {

	                                                                                           
	                                                                        
	class DefaultCheckoutSpecialHandling : public CheckoutSpecialHandling
	{
	public:

		DefaultCheckoutSpecialHandling(GuidToIdMapper& guidToIdMapper,
			const std::string& guid_column_name) : m_guidToIdMapper(guidToIdMapper), m_sGuidColumnName(guid_column_name) { }

		virtual ~DefaultCheckoutSpecialHandling(void) { }

		virtual void HandleColumn(vColumnValuesT& vColumnValues);

	private:
		GuidToIdMapper& m_guidToIdMapper;
		const std::string m_sGuidColumnName;
	};
}                        
}         

#endif                                                

