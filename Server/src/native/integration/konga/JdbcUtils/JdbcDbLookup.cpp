/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Db/TranDbUtils/TranDbUtils.h>

#include <konga/JavaBackend/JavaBackendConfiguration/ConfigUtils.h>

#include "JdbcEngineWrapper.h"
#include "JdbcDbLookup.h"

using namespace konga;
using std::string;
using std::vector;

JdbcDbLookup::JdbcDbLookup(const std::string& userName, const std::string& md5Pwd,
						   const std::string& locationGUID, const DbConnectionInfoStruct& connectionParameters, bool isSource) :
m_bIsSource(isSource), m_bAutoCommit(true), m_lRowCount(0), m_pJdbcEngine(0),
m_userName(userName), m_md5Pwd(md5Pwd),
m_locationGUID(locationGUID), m_connectionParameters(connectionParameters) {}

JdbcDbLookup::~JdbcDbLookup(void) {
	Close();
}

DbLookup* JdbcDbLookup::createJdbcDbLookup(DbConnection& tranDbConn, const UserInfo& userInfo,
										   const std::string& prefixedLocationGuid,
										   const DbConnectionInfoStruct& connectionParameters)
{
	string userName, md5Pwd;
	konga::GetCredentials(tranDbConn, userInfo, userName, md5Pwd);

	bool isSource = true;
	const string& locationGUID = konga::getLocationGuid(prefixedLocationGuid, isSource);

	return new JdbcDbLookup(userName, md5Pwd, locationGUID, connectionParameters, isSource);
}

bool JdbcDbLookup::IsOpen() {
	return m_pJdbcEngine != 0;
}

void JdbcDbLookup::Close() {
	if ( m_pJdbcEngine ) {
		delete m_pJdbcEngine;
		m_pJdbcEngine = 0;
	}
}

void JdbcDbLookup::Connect() {
	if ( !IsOpen() ) {
		m_pJdbcEngine = new JdbcEngineWrapper(m_userName, m_md5Pwd);
	}
}

DbLookupResultState JdbcDbLookup::LookupSingleValue(const std::string& stSql, std::string& value) {
	Connect();

	int rowCount = 0;
	bool nullRetVal = true;
	value = m_pJdbcEngine->dbLookup(m_connectionParameters, m_locationGUID, m_bIsSource, m_bAutoCommit, stSql, rowCount, nullRetVal);

	m_lRowCount = (rowCount > 0) ? rowCount : 0;
	return nullRetVal ? DBLRS_NULL_RESULT : DBLRS_LOOKUP_SUCCESS;
}

DbLookupResultState JdbcDbLookup::Lookup(const std::string& stSql, DbRowSetData& rowset, unsigned long nMaxRows, long timeOutInSeconds) {
	Connect();

	int rowCount = 0;
	bool bNullRetVal = true;
	m_pJdbcEngine->dbExecute(m_connectionParameters, m_locationGUID, m_bIsSource, m_bAutoCommit, stSql, nMaxRows, rowset, rowCount, bNullRetVal);

	m_lRowCount = (rowCount > 0) ? rowCount : 0;
	if ( bNullRetVal )
		return DBLRS_NULL_RESULT;

	return DBLRS_LOOKUP_SUCCESS;
}

long JdbcDbLookup::GetRowCount() {
	return m_lRowCount;
}

long JdbcDbLookup::UseAutoCommit(bool b) {
	m_bAutoCommit = b;
	return 0;
}

void JdbcDbLookup::CommitTransaction() {
	if ( IsOpen() ) {
		m_pJdbcEngine->commitTransactions();
	}
}

void JdbcDbLookup::RollbackTransaction() {
	if ( IsOpen() ) {
		m_pJdbcEngine->rollBackTransactions();
	}
}
