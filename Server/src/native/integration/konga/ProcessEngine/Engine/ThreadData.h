/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

                                                    
  
                                                                      

#ifndef THREAD_DATA_H
#define THREAD_DATA_H

#include <string>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

using namespace konga;

struct ThreadData
{
                                                                                                                            
   	ThreadData() : m_is_thread(true), m_pdb(konga::GetDbConnection()), m_idb(konga::GetDbConnection()), m_mdb(konga::GetDbConnection()),
						   m_apdb(m_pdb), m_aidb(m_idb), m_amdb(m_mdb) {}
	~ThreadData() {}

	ThreadData& operator=(const ThreadData& rhs)
	{
		m_is_thread = rhs.m_is_thread;
		if( m_is_thread ) Connect();
		return *this;
	}
	bool Connect(bool bReconnect = false);

	bool m_is_thread;

	DbConnection *m_pdb, *m_idb, *m_mdb;	
	SmartDbPtr<DbConnection> m_apdb, m_aidb, m_amdb;
};

#endif                 

