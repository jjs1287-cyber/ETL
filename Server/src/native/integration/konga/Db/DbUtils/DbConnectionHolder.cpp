/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <cassert>

#include <konga/Utils/CrException.h>
#include <konga/Utils/DBConnectionInfo.h>

#include <konga/Db/DbUtils/DbUtils.h>

#include "DbConnection.h"
#include "DbConnectionHolder.h"

using namespace konga;
using std::string;

DbConnectionHolder::~DbConnectionHolder(void) {
	CloseDbConnectionIfOwner();
}

DbConnection& DbConnectionHolder::GetDbConnection() const {
	if ( m_pDbConnection == 0 ) {
		                                      
		if ( m_stConnectionString.empty() ) {
			if ( m_stBackEndDbName.empty() )
				throw CrException("Cannot create database connection. No connection string and no internal database name specified.");
			m_pDbConnection = konga::GetBackEndDbConnection(m_stBackEndDbName);
		}
		else {
			m_pDbConnection = DbConnection::MakeInstance(m_stConnectionString);
		}
		m_bOwnsConnection = true;
	}

	if ( !m_pDbConnection->IsOpen() )
		throw CrException("The connection is no longer open.");
	return *m_pDbConnection;
}

void DbConnectionHolder::ExecuteSql(const std::string& stSql) const {
	GetDbConnection().ExecuteSql(stSql);
}

void DbConnectionHolder::CloseDbConnectionIfOwner() {
	if ( m_bOwnsConnection && m_pDbConnection ) {
		m_pDbConnection->Close();
		delete m_pDbConnection;
		m_bOwnsConnection = false;
	}
	m_pDbConnection = 0;
}
