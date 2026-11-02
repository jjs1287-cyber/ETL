/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#ifndef KONGA_DB_REF_COUNTED_DTL_DB_CONNECTION_H
#define KONGA_DB_REF_COUNTED_DTL_DB_CONNECTION_H

#include <string>

#include <boost/thread/mutex.hpp>

#include <dtl/DTL.h>

class RefCountedDtlDbConnection : public dtl::DBConnection {
public:
	RefCountedDtlDbConnection() {}

	void RefCountedConnect(const std::string& stConnectString, bool auto_commit_setting = false);
	void RefCountedRelease();

private:
	static long s_nConnections;                                                                                               
	static boost::mutex s_DbEnvMutex;	                                                    
};

#endif                                            
