/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_TRANSFORMATION_UTILS_TRANSFORM_FCN_UTILS_H
#define ETLFORGE_TRANSFORMATION_UTILS_TRANSFORM_FCN_UTILS_H

                                                        

#include <string>

#include <konga/Utils/KString.h>
#include <konga/DataElement/DataElement.h>

namespace konga {

	KString lpad(const KString& str,int ncolumn);
	KString rpad(const KString& str,int ncolumn);

	int CountSubString(const char*str,const char*sub_str);

	int IndexOfString(const char*str,const char*sub_str,int n);
	int IndexOfStringUtf8Character(const char* utf8_str,const char* utf8_sub_str,int n);

	const char*find_st(const char*str,int k);

	DataElement* SplitStringReturnDataElementArray(const std::string& s, const std::string& delim);
}

#endif                                                       

