/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_CHECKOUT_OPERATION_TREE_CHECKOUT_SPECIAL_HANDLING_H
#define KONGA_WS_CHECKOUT_OPERATION_TREE_CHECKOUT_SPECIAL_HANDLING_H

#include <string>

#include "CheckoutSpecialHandling.h"

namespace konga {
	class DbRelations;
	class GuidToIdMapper;

namespace interchange_checkout {

	class OperationTreeCheckoutSpecialHandling : public CheckoutSpecialHandling {
	public:
		OperationTreeCheckoutSpecialHandling(const DbRelations& dbRelations,
			GuidToIdMapper& guidToIdMapper) : m_dbRelations(dbRelations), m_guidToIdMapper(guidToIdMapper) {}
		virtual ~OperationTreeCheckoutSpecialHandling(void) {}

		                                       
		                                  
		void SetName(const std::string& stName) {
			m_stName = stName;
		}

		virtual void HandleColumn(vColumnValuesT& vColumnValues);

	private:
		const DbRelations& m_dbRelations;
		GuidToIdMapper& m_guidToIdMapper;
		std::string m_stName;
	};

}                                  
}                   

#endif                                                                

