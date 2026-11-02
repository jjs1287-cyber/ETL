/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_DTLDBUTILS_H
#define KONGA_DB_DTLDBUTILS_H

#include <string>
#include <vector>
#include <exception>

#include <dtl/DTL.h>

#include <konga/Db/DbUtils/DbException.h>
    
namespace konga_dtl
{
    void ThrowDbException(const dtl::DBException& ex, const std::string& stErr = "", long lErr = -1);
    void ThrowDbException(const dtl::RootException& ex, const std::string& stErr = "", long lErr = -1);
	void ThrowDbException(const std::exception& ex, const std::string& stErr = "", long lErr = -1);
}

#endif                         
