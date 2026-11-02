/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#ifndef TRANSFORMATION_DIFF_CONSTANTS_H
#define TRANSFORMATION_DIFF_CONSTANTS_H

#include <string>

namespace konga {
namespace diff {

	const std::string DIFF_GUID = "__DIFF_GUID";
	const std::string PK_LIST_DENAME = "__DIFF_PKLIST";
	const std::string ORDER_LIST_DENAME = "__DIFF_ORDER_LIST";
	const std::string ORDERED_DENAME = "__DIFF_ORDERED";
	const std::string STATUS_DENAME = "__DIFF_STATUS";
	const std::string CHUNK_SIZE_DENAME = "__DIFF_CHUNK_SIZE";

	enum DiffStatus {
		DiffStatus_None = 0,
		DiffStatus_Add = 1,
		DiffStatus_Update = 2,
		DiffStatus_Delete = 3
	};

	enum DiffResetAction {
		DiffResetAction_Reset = 0,
		DiffResetAction_Purge = 1
	};

}
}

#endif                                   

