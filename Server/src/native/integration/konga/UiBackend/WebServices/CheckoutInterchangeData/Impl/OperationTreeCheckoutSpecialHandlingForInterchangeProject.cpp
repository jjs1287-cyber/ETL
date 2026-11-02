/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include "OperationTreeCheckoutSpecialHandlingForInterchangeProject.h"

using namespace konga;
using namespace konga::interchange_checkout;

void OperationTreeCheckoutSpecialHandlingForInterchangeProject::HandleColumn(vColumnValuesT& vColumnValues) {
	NullableString& guid = GetColumnValueRef("guid", vColumnValues);
	NullableString& id = GetColumnValueRef("interchangeproject_id", vColumnValues);

	id = guid;
	guid = "";
}

