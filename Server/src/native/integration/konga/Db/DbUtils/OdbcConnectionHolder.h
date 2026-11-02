/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_ODBC_CONNECTION_HOLDER_H
#define KONGA_ODBC_CONNECTION_HOLDER_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <sql.h>

#include <string>

namespace konga {
	class OdbcConnectionHolder {
	public:
		OdbcConnectionHolder(const std::string& stConnectionString = "")
			: m_hSqlEnv(0), m_hSqlDBC(0), m_hSqlStmt(0),
			m_bIsConnected(false), m_iTimeOut(300),
			m_fDriverVersion(0.f), m_stConnectionString(stConnectionString)
		{}

		virtual ~OdbcConnectionHolder() {
			Close();
		}

		bool IsConnected() const { return m_bIsConnected; }
		void Close();

		void SetTimeOut(int iTimeOut) { m_iTimeOut = iTimeOut; }
		float GetDriverVersion() const { return m_fDriverVersion; }

		std::string getQuoteString() const { return m_stQuoteChar; }

		std::string GetLastError() const { return m_stLastError; }

	protected:
		void SetConnectionString(const std::string& stConnectionString) {
			m_stConnectionString = stConnectionString;
		}

		bool Connect(bool bAllocateStmt = true);

		                                      
		bool ReAllocateStatementHandle();

		std::string GetConnectionString() const { return m_stConnectionString; }

		bool AllocateEnvironmentHandle();
		bool FreeStatementHandle();

		void AppendLastError(const std::string& err);
		std::string m_stLastError;
		std::string m_stLastWarning;

	private:
		bool AllocateStatementHandle();

		void DisconnectAndCleanUp(bool bFreeStatementHandle = true);

	protected:
		SQLHENV		m_hSqlEnv;
		SQLHDBC		m_hSqlDBC;
		SQLHSTMT	m_hSqlStmt;

	private:
		bool        m_bIsConnected;
		int			m_iTimeOut;

		float		m_fDriverVersion;
		std::string	m_stQuoteChar;

		std::string m_stConnectionString;

	};
}                   

#endif                                  

