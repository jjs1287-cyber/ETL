/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef KONGA_EXE_FCN_EXCEPTION
#define KONGA_EXE_FCN_EXCEPTION

#include <konga/Utils/CrException.h>

namespace konga {

class ExeFcnException : public konga::CrException
{
public:
	ExeFcnException(const std::string&s) : konga::CrException(s){}
	virtual ~ExeFcnException(){}
};

class EvalException : public konga::CrException
{
public:
	EvalException(const std::string&s) : konga::CrException(s){}
	virtual ~EvalException(){}
};

class TokenSyntaxException : public konga::CrException
{
public:
	TokenSyntaxException(const std::string&s) : konga::CrException(s){}
	virtual ~TokenSyntaxException(){}
};

}                   

#endif                           

