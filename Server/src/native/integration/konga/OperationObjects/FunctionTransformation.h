/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_FUNCTION_TRANSFORMATION_H
#define KONGA_OPERATION_FUNCTION_TRANSFORMATION_H

#include "Transformation.h"

namespace konga {

	class FunctionTransformation : public Transformation {
	public:
		FunctionTransformation(long lFunctionId, const std::string& stFunctionType)
			: m_lFunctionId(lFunctionId), m_stFunctionType(stFunctionType) { }
		virtual ~FunctionTransformation(void) { }

		long getFunctionId() const {
			return m_lFunctionId;
		}

		std::string getFunctionType() const {
			return m_stFunctionType;
		}

	private:
		long m_lFunctionId;
		std::string m_stFunctionType;
	};
}
#endif                                             

