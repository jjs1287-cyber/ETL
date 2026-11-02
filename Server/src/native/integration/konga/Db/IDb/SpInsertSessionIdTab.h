/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_IDB_SPINSERTSESSIONIDTAB_H
#define KONGA_DB_IDB_SPINSERTSESSIONIDTAB_H

#include <string>

#ifdef WIN32
	#include <windows.h>
#endif
#include <sqltypes.h>

namespace konga
{
    class DbConnection;
}

namespace IDb
{

class SpInsertSessionIdTab
{
public:
    static SpInsertSessionIdTab* MakeInstance(konga::DbConnection* pConn);

    SpInsertSessionIdTab() {}
    virtual ~SpInsertSessionIdTab() {}

    virtual void SetNote(const std::string& stUser) = 0;
    virtual void SetNoteToNull() = 0;

	                   
    virtual int GetSessionId() const = 0;

    virtual void Execute() = 0;
    virtual void Close() = 0;
};

}                     

#endif                                       
