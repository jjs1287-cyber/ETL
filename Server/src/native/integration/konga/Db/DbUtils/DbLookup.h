/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef DBUTILS_DBLOOKUP_H
#define DBUTILS_DBLOOKUP_H

#include <string>
#include <vector>

#include "DbRowSetData.h"	               

namespace konga
{

                                                                                        
enum DbLookupResultState { DBLRS_LOOKUP_SUCCESS = 0, DBLRS_OPEN_DB_FAILED = 173, DBLRS_NULL_RESULT = 232, DBLRS_UNKNOWN = 9999 };

                                       
class DbLookup {
public:
	DbLookup(void) {}

	virtual ~DbLookup(void) {}

	virtual bool IsOpen() = 0;
	virtual void Close() = 0;

	virtual DbLookupResultState LookupSingleValue(const std::string& stSql, std::string& value) = 0;

	virtual DbLookupResultState Lookup(const std::string& stSql, 
									   DbRowSetData& rowset,
									   unsigned long nMaxRows, long timeOutInSeconds = -1) = 0;
		
	virtual long GetRowCount() = 0;

	virtual long UseAutoCommit(bool b) = 0;

	virtual void CommitTransaction() = 0;
	virtual void RollbackTransaction() = 0;
};

}                   

#endif                      

