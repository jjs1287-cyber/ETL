/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_TRANSFORMATION_ENGINE_RUN_OPERATION_IMPL
#define KONGA_TRANSFORMATION_ENGINE_RUN_OPERATION_IMPL

#include <string>

namespace konga {
	class OperationDataElementHolder;
	class Debuggee;
	class OperationContext;

	                                                     
	                                                                                                             
	bool RunOperationImpl(const std::string& op_tag, const bool bSync,
							konga::OperationDataElementHolder* const deHolder, const konga::OperationContext& context,
							Debuggee* pDebuggee, std::string& sError);

}

#endif                                                  

