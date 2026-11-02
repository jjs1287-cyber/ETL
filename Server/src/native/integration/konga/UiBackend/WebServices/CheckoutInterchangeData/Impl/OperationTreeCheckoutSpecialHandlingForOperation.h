/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef WS_KONGA_CHECKOUT_OPERATION_TREE_CHECKOUT_SPECIAL_HANDLING_FOR_OPERATION_H
#define WS_KONGA_CHECKOUT_OPERATION_TREE_CHECKOUT_SPECIAL_HANDLING_FOR_OPERATION_H

#include "DefaultCheckoutSpecialHandling.h"
#include <konga/DbRelations/DatabaseTableNameMappings.h>

namespace konga {
	class GuidToIdMapper;

namespace interchange_checkout {

	class OperationTreeCheckoutSpecialHandlingForOperation : public DefaultCheckoutSpecialHandling {
	public:
		OperationTreeCheckoutSpecialHandlingForOperation(GuidToIdMapper& guidToIdMapper)
			: DefaultCheckoutSpecialHandling(guidToIdMapper, getIdName(nameOperations)),
			m_myGuidToIdMapper(guidToIdMapper) {}
		virtual ~OperationTreeCheckoutSpecialHandlingForOperation(void) {}

		virtual void HandleColumn(vColumnValuesT& vColumnValues);

	private:
		GuidToIdMapper& m_myGuidToIdMapper;
	};

}
}
#endif                                                                              

