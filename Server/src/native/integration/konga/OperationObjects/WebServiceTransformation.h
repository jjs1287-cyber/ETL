/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_WEBSERVICE_TRANSFORMATION_H
#define KONGA_OPERATION_WEBSERVICE_TRANSFORMATION_H

#include "FunctionTransformation.h"

namespace konga {

	class WebServiceTransformation : public FunctionTransformation {
	public:
		WebServiceTransformation(long lFunctionId)
			: FunctionTransformation(lFunctionId, "WebService") { }
		~WebServiceTransformation(void) { }
	};
}
#endif                                               

