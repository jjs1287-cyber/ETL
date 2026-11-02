/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_IDB_SPINSERTROUTETAB_H
#define KONGA_DB_IDB_SPINSERTROUTETAB_H

namespace konga
{
    class DbConnection;
}

namespace IDb
{

struct SpInsertRouteTabParams
{
    int OldDestinationTaskInstanceId;
    int NewDestinationTaskInstanceId;
};

class SpInsertRouteTab
{
public:
    static SpInsertRouteTab* MakeInstance(konga::DbConnection* pConn);

    SpInsertRouteTab() {}
    virtual ~SpInsertRouteTab() {}

    virtual void SetOldDestinationTaskInstanceId(int id) = 0;
    virtual void SetNewDestinationTaskInstanceId(int id) = 0;

    virtual void Execute() = 0;
    virtual void Close() = 0;


protected:
    SpInsertRouteTabParams  m_params;
};


}                     


#endif                                   

