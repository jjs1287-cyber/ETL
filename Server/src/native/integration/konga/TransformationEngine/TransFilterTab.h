/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */


#ifndef TRANSFORMATION_ENGINE_TABLEFILTERS_H
#define TRANSFORMATION_ENGINE_TABLEFILTERS_H

#include <vector>
#include <konga/Utils/KString.h>

class CTableFilters
{
public:
	CTableFilters(const char* szTableName) : m_sTableName( szTableName != 0 ? szTableName : "") {}
	~CTableFilters(void) {}

	KString m_sTableName;
	KString m_sOverRideFilter;

	std::vector<KString> m_vColumnNames;
	std::vector<KString> m_vConditions;
	std::vector<KString> m_vOrderBy;
};

#endif                                        

