/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <map>
#include <iostream>

#include "VectorUtils.h"

using namespace konga::VectorUtils;
using std::string;
using std::vector;
using std::map;

void konga::VectorUtils::GetUniqueStrings(const vector<string>& v1,
								   const vector<string>& v2,
								   vector<string>& vStringsInV1ButNotInV2,
								   bool bIgnoreDuplicates           )
{
	map<string, bool> m2, mOut;
	for(size_t i = 0, iMax = v2.size(); i < iMax; ++i )
	{
		m2[ v2[i] ] = true;
	}
	for(size_t i = 0, iMax = v1.size(); i < iMax; ++i )
	{
		string s1 = v1[i];
		if( m2.find(s1) == m2.end() )
		{
			if( bIgnoreDuplicates )
			{
				if( mOut.find(s1) != mOut.end() )
				{
					continue;
				}
				mOut[s1] = true;
			}
			vStringsInV1ButNotInV2.push_back(s1);
		}
	}
}

void konga::VectorUtils::PrintToStandardOut(const vector<string>& v, const string& stHeader         )
{
	using namespace std;
	if( !stHeader.empty() )
		cout << stHeader.c_str() << endl;
	for(size_t i = 0; i < v.size(); ++i )
		cout << v[i].c_str() << endl;
}
