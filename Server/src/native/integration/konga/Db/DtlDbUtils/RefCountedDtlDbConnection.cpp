/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <cassert>

#include "RefCountedDtlDbConnection.h"

long RefCountedDtlDbConnection::s_nConnections = 0;
boost::mutex RefCountedDtlDbConnection::s_DbEnvMutex;

void RefCountedDtlDbConnection::RefCountedConnect(const std::string& stConnectString,
												  bool auto_commit_setting              )
{
	assert(s_nConnections>=0);
	boost::mutex::scoped_lock lock(s_DbEnvMutex);
	this->dtl::DBConnection::Connect(stConnectString, false                 , auto_commit_setting);
	++s_nConnections;
}

void RefCountedDtlDbConnection::RefCountedRelease() {
	assert(s_nConnections>0);
	boost::mutex::scoped_lock lock(s_DbEnvMutex);		
	this->dtl::DBConnection::Release();
	if ( --s_nConnections == 0 )
		dtl::DBConnection::GetDefaultEnvironment().Release();
}

