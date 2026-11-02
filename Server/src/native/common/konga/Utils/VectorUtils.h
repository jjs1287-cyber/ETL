/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_VECTOR_UTILS_H
#define KONGA_VECTOR_UTILS_H

#include <vector>
#include <string>

                                             

namespace konga {
namespace VectorUtils
{
	void GetUniqueStrings(const std::vector<std::string>& v1,
		const std::vector<std::string>& v2,
		std::vector<std::string>& vStringsInV1ButNotInV2,
		bool bIgnoreDuplicates = true);

	void PrintToStandardOut(const std::vector<std::string>& v,
		const std::string& stHeader = "");
};
}                   

#endif                        

