/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <memory>	                
#include <vector>
#include <iostream>
#include <string>

#include <boost/thread/thread.hpp>
#include <boost/thread/mutex.hpp>

#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/util.h>
#include <konga/Utils/ProcessUtils.h>
#include <konga/Utils/DBConnectionInfo.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>

using namespace konga;
using std::vector;
using std::string;
using boost::thread;


namespace {

	boost::mutex g_write_mx;

	class RunThis {
	public:
		RunThis(int i, const string& connStr) : m_iThreadId(i), m_connStr(connStr) {}
		~RunThis() {}

		void operator()() {
			try {
				Clock cl;

				SmartDbPtr<DbConnection> pDbConn( DbConnection::MakeInstance(m_connStr) );
				konga::MilliSleep(500);

				AutoRollbackTransaction transaction(pDbConn);			

				std::cout << "Lock " << m_iThreadId;
				const long sessionId = konga::GetNextId("operationdataelementtab", "sessionid", *pDbConn);
				std::cout << "Thread " << m_iThreadId << " aquired lock" << std::endl;

				pDbConn->ExecuteSql("insert into operationdataelementtab values (" + konga::toString(sessionId)
					+ ", 'name', '-1', 'value')");

				std::cout << "Thread " << m_iThreadId << " about to release lock" << std::endl;
				transaction.Commit();

				konga::MilliSleep(500);


				boost::mutex::scoped_lock lock(g_write_mx);
				std::cout << "Thread " << m_iThreadId << " finished with sessionId= " << sessionId << std::endl;
			}
			catch( const CrException& e ) {
				std::cerr << "Error : " + e.GetErrorString() << std::endl;
			}
		}

	private:
		int m_iThreadId;
		string m_connStr;
	};
}

int testGetNextIdMT() {
	try {
		vector<thread*> vThreads;
		const int Nthreads = 16;
		const string connStr( DBConnectionInfo("IDb").connectionString() );

		for( int i = 0; i < Nthreads; ++i ) {
			RunThis f(i, connStr);
			thread* p = new boost::thread(f);
			if ( p == 0 )
				throw CrException("There is not enough memory left to create a new thread.");
			vThreads.push_back(p);
		}

		for( vector<thread*>::iterator it = vThreads.begin(); it != vThreads.end(); ++it ) {
			thread* p = *it;
			p->join();
			delete p;
		}
	}
	catch( const CrException& e ) {
		std::cout << e.GetErrorString() << std::endl;
		return 1;
	}
	catch( const boost::thread_resource_error& e ) {
		std::cout << "Not enough resources to create a new thread: " << e.what() << std::endl;
		return 1;
	}
	catch( const std::exception& e ) {
		std::cout << "Failed to create a new thread: " << e.what() << std::endl;
		return 1;
	}
	return 0;
}
