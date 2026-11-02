/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef XSDPARSEEXCEPTION_H
#define XSDPARSEEXCEPTION_H

#include <konga/Utils/CrException.h>

#include "misc.h"

namespace konga {
	class XsdParserException : public CrException
	{
	public:
		XsdParserException(long errNum=E_FAIL, const std::string& errStr="") : CrException(errNum, errStr) {};
		XsdParserException(const std::string& errStr, long errNum = E_FAIL) : CrException(errStr, errNum) {};
		virtual ~XsdParserException() {};
	};
}

#endif                                      

