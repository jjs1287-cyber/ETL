/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef JDBC_UTILS_DBLOOKUP_H
#define JDBC_UTILS_DBLOOKUP_H

#include <konga/Db/DbUtils/DbLookup.h>
#include <konga/Db/TranDbUtils/DbConnectionInfoStruct.h>

namespace konga {

	class JdbcEngineWrapper;
	class DbConnection;
	class UserInfo;

	class JdbcDbLookup : public DbLookup {
	private:
		JdbcDbLookup(const std::string& userName, const std::string& md5Pwd, const std::string& locationGUID, const DbConnectionInfoStruct& connectionParameters, bool isSource);
		~JdbcDbLookup(void);

	public:		
		static DbLookup* createJdbcDbLookup(DbConnection& tranDbConn, const UserInfo& userInfo,
			const std::string& prefixedLocationGuid, const DbConnectionInfoStruct& connectionParameters);

		           
		virtual bool IsOpen();

		          
		virtual void Close();

		           
		virtual DbLookupResultState LookupSingleValue(const std::string& stSql, std::string& value);

		           
		virtual DbLookupResultState Lookup(const std::string& stSql, 
			DbRowSetData& rowset, unsigned long nMaxRows, long timeOutInSeconds = -1);

		           
		virtual long GetRowCount();

		           
		virtual long UseAutoCommit(bool b);

		           
		virtual void CommitTransaction();
		virtual void RollbackTransaction();

	private:
		void Connect();

	private:
		bool m_bIsSource;
		bool m_bAutoCommit;
		long m_lRowCount;
		JdbcEngineWrapper* m_pJdbcEngine;

		const std::string m_userName, m_md5Pwd;

		std::string m_locationGUID;
		DbConnectionInfoStruct m_connectionParameters;
	};
}

#endif                         

