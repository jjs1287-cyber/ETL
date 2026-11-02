/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_AUTO_DB_CLEANUP
#define KONGA_AUTO_DB_CLEANUP

#include <boost/noncopyable.hpp>

namespace konga {
template<class DBCLASS>
class AutoCloseOledbConnection : boost::noncopyable
{
public:
	AutoCloseOledbConnection(DBCLASS* pC) : m_pC(pC) { }
	~AutoCloseOledbConnection() { m_pC->Close(); }
private:
	DBCLASS* m_pC;
};

template<class DBCLASS>
class AutoTransaction : boost::noncopyable
{
public:
	class BeginTransactionFailed {};
	AutoTransaction(DBCLASS& db) : m_cancel(false), m_db(db)
	{
		if( !m_db.BeginTrans() ) throw BeginTransactionFailed();
	}
	~AutoTransaction() { if(!m_cancel) m_db.CommitTrans(); }
	void Rollback() { m_db.Rollback(); m_cancel = true; }
private:
	bool m_cancel;
	DBCLASS& m_db;
};

template<class DBCLASS>
class AutoDbTransaction : boost::noncopyable
{
public:
	class BeginTransactionFailed {};
	AutoDbTransaction(DBCLASS& db) : m_cancel(false), m_db(db) {}
	~AutoDbTransaction() { 
		if(!m_cancel)
			m_db.Commit();
		m_db.SetAutoCommit(true);
	}
	void Rollback() { m_db.Rollback(); m_cancel = true; }
private:
	bool m_cancel;
	DBCLASS& m_db;
};

template<class DBCLASS>
class AutoAbortOledbTransaction : boost::noncopyable
{
public:
	AutoAbortOledbTransaction(DBCLASS& db) : m_cancel(false), m_db(db)	{}
	~AutoAbortOledbTransaction() { if(!m_cancel) m_db.Abort(); }
	void Ignore() { m_cancel = true; }
private:
	bool m_cancel;
	DBCLASS& m_db;
};

}                   

#endif                         

