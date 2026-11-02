/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_CHECKOUT_OPERATION_TREE_CHECKOUT_SPECIAL_HANDLING_FOR_OPERATION_H
#define KONGA_WS_CHECKOUT_OPERATION_TREE_CHECKOUT_SPECIAL_HANDLING_FOR_OPERATION_H

#include "CheckoutSpecialHandling.h"

namespace konga {
namespace interchange_checkout {

	class OperationTreeCheckoutSpecialHandlingForInterchangeProject : public CheckoutSpecialHandling {
	public:
		OperationTreeCheckoutSpecialHandlingForInterchangeProject(void) {}
		virtual ~OperationTreeCheckoutSpecialHandlingForInterchangeProject(void) {}

		virtual void HandleColumn(vColumnValuesT& vColumnValues);

	};
}
}

#endif                                                                              

