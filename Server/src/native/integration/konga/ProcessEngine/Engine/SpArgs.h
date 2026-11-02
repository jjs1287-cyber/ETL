/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef CR_SPARGS_H
#define CR_SPARGS_H

#ifdef DS_USE_SP

#include <string>               
#include <string.h>

#ifdef FLOAT_SIZE_MAX
#undef FLOAT_SIZE_MAX
#endif
#define FLOAT_SIZE_MAX 24

#ifdef INT_SIZE_MAX
#undef INT_SIZE_MAX
#endif
#define INT_SIZE_MAX 20		                             

#ifdef MAX_BUFFER_SIZE_FOR_CONVERSION
#undef MAX_BUFFER_SIZE_FOR_CONVERSION
#endif
#define MAX_BUFFER_SIZE_FOR_CONVERSION 64

typedef struct spArgs_
{
	std::string argDataType;
	int argDataSubType;
	std::string argName;
	std::string argParamType;
	unsigned int dataSize;
	void* data;
	bool bIsNull;
} spArgs;

#endif             

#endif               
