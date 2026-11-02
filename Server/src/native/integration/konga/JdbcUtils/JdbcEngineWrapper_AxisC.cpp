/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifdef WIN32
 #define _WINSOCKAPI_                                                  
#endif

#include <cassert>
#include <exception>
#include <memory>

#include <boost/thread/mutex.hpp>

#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/ProcessUtils.h>
#include <konga/Utils/StringUtils.h>	                 

#include <konga/Crypto/Crypto.h>

#include <konga/Db/DbUtils/DbRowSetData.h>

#include <konga/JavaBackend/JavaBackendConfiguration/ConfigUtils.h>
#include <konga/JavaBackend/JavaBackendConfiguration/ServiceNames.h>

#include <konga/JavaBackend/JdbcEngineC/axis2_stub_JdbcEngineService.h>

#include "KeyValues.h"
#include "JdbcEngineWrapper.h"

using namespace konga;
using std::string;

namespace {
	axis2_char_t* allocate_axis2_char_t(const string& s) {
		axis2_char_t* pc = new axis2_char_t[s.size()+1];
		strcpy(pc, s.c_str());
		return pc;
	}

	                                                 
	void throwOnError(const axutil_env_t* env, const string& header) {
		const axis2_status_t status = axutil_env_check_status(env);
		if ( status != AXIS2_SUCCESS ) {
			const axis2_char_t* err = axutil_error_get_message(env->error);
			if ( err ) {
				throw CrException(header + " " + err);
			}
			else {
				throw CrException(header + " Axis2 returned non-success status code but no error was reported.");
			}
		}
	}

	void throwOnError(adb_WsJdbcError_t* err, const axutil_env_t* env, const string& header) {
		if ( err == 0 ) {
			throw InternalErrorException(header + " Failed to check error information. Error is NULL.");
		}
		int errorCode = adb_WsJdbcError_get_errorCode(err, env);
		if ( errorCode != 0 ) {
			const axis2_char_t* const errorStr = adb_WsJdbcError_get_error(err, env);
			if ( errorStr == 0 || *errorStr == 0 ) {
				throw InternalErrorException(header + " Operation failed but no error was reported.");
			}
			throw CrException(header + errorStr);
		}
	}

	string getWarnings(adb_WsJdbcError_t* err, const axutil_env_t* env) {
		if( err == 0 || env == 0 ) return string("");
		const axis2_char_t* const warnStr = adb_WsJdbcError_get_warnings(err, env);
		if ( warnStr == 0 || *warnStr == 0 ) {
			return string("");
		}
		return string(warnStr);
	}

	boost::mutex g_JdbcEngineCWrapperCtor;
}

namespace konga {

	                                                                  
	class JdbcEngineWrapper_Impl {
	public:
		JdbcEngineWrapper_Impl() : fail_counter(0), env(0), jdbcEngine(0), home(0), uri(0)
		{
		}

		~JdbcEngineWrapper_Impl() {
			CleanAll();
		}

		void InitCurrent() {
			boost::mutex::scoped_lock lock(g_JdbcEngineCWrapperCtor);
			try {
				                                   
				if( logFile.empty() ) {
					logFile = getTmpDir();
					logFile += "axis2_log";
					kCreateDirectory(logFile);
					logFile = getTmpFileName("axis2log-", logFile);
					logFile += ".txt";
				}
				
				if ( env == 0 ) {
					env = axutil_env_create_all(logFile.c_str(), AXIS2_LOG_LEVEL_WARNING);
				}
				if ( env == 0 ) {
					throw InternalErrorException("Failed to allocate Axis2C environment. Check the file '" + logFile
						+ "' for possible error messages.");
				}

				setHome();
				setUri();

				if ( jdbcEngine == 0 ) {
					jdbcEngine = axis2_stub_create_JdbcEngineService(env, home, uri);

					axis2_status_t status = axutil_env_check_status(env);
					throwOnError(env, "axutil_env_check_status failed.");

					if ( jdbcEngine == 0 ) {
						throw CrException("Failed to allocate Axis2C stub for JdbcEngine. No failure status.");
					}

					                      
					long timeOutMs = konga::GetOptionAsInt("JdbcEngine", "ClientSessionTimeout", 180);	                      
					                               
					timeOutMs*=60000;
					status = axis2_options_set_timeout_in_milli_seconds(
						axis2_stub_get_options(jdbcEngine, env),
						env, timeOutMs);
				}
			}
			catch(...) {
				CleanCurrent();
			}
		}

		void CleanCurrent() {
			if ( jdbcEngine != 0 ) {
				axis2_stub_free(jdbcEngine, env);
				jdbcEngine = 0;
			}
		}

		void throwOnNullResponse(void* p, const std::string& header) {
			if ( p == 0 ) {
				CleanCurrent();
				throw InternalErrorException(header + string(" NULL response was returned. The operation was attempted " + konga::toString(fail_counter) + " times.\n")
					+ "Check for errors in logfile on server: '" + logFile + "'.");
			}
		}

		int fail_counter;

		axutil_env_t* env;
		axis2_stub_t* jdbcEngine;

		axis2_char_t* home;
		axis2_char_t * uri;

		string logFile;
		string userName;
		string md5Pwd;
		string sessionGuid;

	private:
		void CleanAll() {

			CleanCurrent();

			if ( env != 0 ) {
				axutil_env_free(env);
				env = 0;
			}

			if ( home != 0 ) {
				delete[] home;
				home = 0;
			}

			if ( uri != 0 ) {
				delete[] uri;
				uri = 0;
			}

			                            
			if ( !NonEmptyFileExists(logFile) ) {
				try {
					kDeleteFile(logFile);
				} catch(...) {
					                
				}
			}
		}

		void setHome() {
			if( home == 0 ) {
				home = allocate_axis2_char_t( konga::GetAXIS2C_HOME() );
			}
		}

		void setUri() {
			if ( uri == 0 ) {
				uri = allocate_axis2_char_t( konga::GetEndpointUrlForService(JDBC_ENGINE) );
			}
		}
	};
}                   

namespace {
	adb_WsJdbcSessionId_t* createWsJdbcSessionId(const axutil_env_t *env, const string& userName, const string& md5Pwd, const string& sessionGuid) {
		adb_WsJdbcSessionId_t* _wsJdbcSessionId = adb_WsJdbcSessionId_create(env);
		adb_WsJdbcSessionId_set_etlforgeUser(_wsJdbcSessionId, env, userName.c_str());
		adb_WsJdbcSessionId_set_md5Pwd(_wsJdbcSessionId, env, md5Pwd.c_str());
		adb_WsJdbcSessionId_set_sessionGuid(_wsJdbcSessionId, env, sessionGuid.c_str());
		return _wsJdbcSessionId;
	}

	adb_WsConnectionParams_t* createWsConnectionParams(const axutil_env_t *env, const DbConnectionInfoStruct& connectionParams) {
		adb_WsConnectionParams_t* _conParams = adb_WsConnectionParams_create(env);

		adb_WsConnectionParams_set_additionalParams(_conParams, env, connectionParams.additionalParams.c_str());
		adb_WsConnectionParams_set_database(_conParams, env, connectionParams.database.c_str());
		adb_WsConnectionParams_set_driverName(_conParams, env, connectionParams.driverName.c_str());
		adb_WsConnectionParams_set_manualConnectionString(_conParams, env, connectionParams.manualConnectionString.c_str());
		adb_WsConnectionParams_set_password(_conParams, env, connectionParams.password.c_str());
		adb_WsConnectionParams_set_port(_conParams, env, connectionParams.port);
		adb_WsConnectionParams_set_server(_conParams, env, connectionParams.server.c_str());
		adb_WsConnectionParams_set_timeout(_conParams, env, connectionParams.timeOutSeconds);
		adb_WsConnectionParams_set_transactionIsolationLevel(_conParams, env, (int)connectionParams.txnIsolationLevel);
		adb_WsConnectionParams_set_user(_conParams, env, connectionParams.user.c_str());

		return _conParams;
	}

	adb_WsDbLookupParams_t* createWsDbLookupParams(const axutil_env_t *env, const DbConnectionInfoStruct& connectionParams, const string& locationGUID,
		const bool isSource, const bool bAutoCommit, const string& sql)
	{
		adb_WsDbLookupParams_t* _wsDbLookupParams = adb_WsDbLookupParams_create(env);

		adb_WsDbLookupParams_set_autoCommit(_wsDbLookupParams, env, bAutoCommit);
		adb_WsDbLookupParams_set_connectionParams(_wsDbLookupParams, env, createWsConnectionParams(env, connectionParams));

		if ( isSource ) {
			adb_WsDbLookupParams_set_sourceGuid(_wsDbLookupParams, env, locationGUID.c_str());
		}
		else {
			adb_WsDbLookupParams_set_targetGuid(_wsDbLookupParams, env, locationGUID.c_str());
		}

		string b64sql("b64:");
		b64sql += konga::EncodeBase64((unsigned char*)(sql.c_str()), sql.size());
		adb_WsDbLookupParams_set_sql(_wsDbLookupParams, env, b64sql.c_str());
		return _wsDbLookupParams;
	}
}

JdbcEngineWrapper::JdbcEngineWrapper(const string& userName, const string& md5Pwd) : m_pImpl(new JdbcEngineWrapper_Impl()) {
	m_pImpl->userName = userName;
	m_pImpl->md5Pwd = md5Pwd;
}

JdbcEngineWrapper::~JdbcEngineWrapper(void) {
	try {
		                               
		if ( m_pImpl && !m_pImpl->sessionGuid.empty() ) {
			m_pImpl->InitCurrent();
			const axutil_env_t *env = m_pImpl->env;

			adb_closeSession_t* _closeSession = adb_closeSession_create(env);
			adb_closeSession_set_in0(_closeSession, env, createWsJdbcSessionId(env, m_pImpl->userName, m_pImpl->md5Pwd, m_pImpl->sessionGuid));

			adb_closeSessionResponse_t* response = axis2_stub_op_JdbcEngineService_closeSession(m_pImpl->jdbcEngine, env, _closeSession);
			adb_closeSession_free(_closeSession, env);
			m_pImpl->sessionGuid.clear();

			throwOnError(adb_closeSessionResponse_get_closeSessionReturn(response, env), env, "Failed to close JDBC session.");
			if ( response ) {
				adb_closeSessionResponse_free(response, env);
				m_pImpl->CleanCurrent();
			}
		}
	}
	catch(...) {
		                                                    
		if ( m_pImpl ) {
			delete m_pImpl;
			m_pImpl = 0;
		}
		throw;
	}
	if ( m_pImpl ) {
		delete m_pImpl;
		m_pImpl = 0;
	}
}

namespace {
	string getDataFileNameHack(const string& sDataFileName_hack, const string& sDebugLogFile_hack) {
		if ( sDebugLogFile_hack.empty() ) return sDataFileName_hack;
		return string( sDataFileName_hack + "|" + sDebugLogFile_hack );
	}

	                                                                                            
	const int MAX_FAILURES = 50;
	const int MS_SLEEP_AFTER_FAILURE = 10000;
}

void JdbcEngineWrapper::readSource(const DbConnectionInfoStruct& connectionParams,
								   const string& sSourceGUID,
								   const string& sAdHocFilter,
								   const string& sJtrFileName,
								   const string& sDataFileName_hack,
								   const string& sStructureFileName,
								   const string& sDebugLogFile_hack)
{
	Check();
	const axutil_env_t *env = m_pImpl->env;

	const string& sDataFileName = getDataFileNameHack(sDataFileName_hack, sDebugLogFile_hack);

	                       
	adb_WsReadSourceParams_t* _wsReadSourceParams = adb_WsReadSourceParams_create(env);

	adb_WsReadSourceParams_set_adHocFilter(_wsReadSourceParams, env, sAdHocFilter.c_str());
	adb_WsReadSourceParams_set_connectionParams(_wsReadSourceParams, env, createWsConnectionParams(env, connectionParams));
	adb_WsReadSourceParams_set_dataFileName(_wsReadSourceParams, env, sDataFileName.c_str());
	adb_WsReadSourceParams_set_jtrFileName(_wsReadSourceParams, env, sJtrFileName.c_str());
	adb_WsReadSourceParams_set_sourceGuid(_wsReadSourceParams, env, sSourceGUID.c_str());
	adb_WsReadSourceParams_set_structureFileName(_wsReadSourceParams, env, sStructureFileName.c_str());
	adb_WsReadSourceParams_set_fetchSize(_wsReadSourceParams, env, connectionParams.fetchSize);

	adb_readSource_t* _readSource = adb_readSource_create(env);
	adb_readSource_set_in0(_readSource, env, createWsJdbcSessionId(env, m_pImpl->userName, m_pImpl->md5Pwd, m_pImpl->sessionGuid));
	adb_readSource_set_in1(_readSource, env, _wsReadSourceParams);

	       
	m_pImpl->fail_counter = 0;
	adb_readSourceResponse_t* response = 0;
	do {
		response = axis2_stub_op_JdbcEngineService_readSource(m_pImpl->jdbcEngine, env, _readSource);
		if ( response == 0 ) {
			std::cout << "readSource returned NULL, fail_counter = " << m_pImpl->fail_counter << std::endl;
			m_pImpl->CleanCurrent();
			konga::MilliSleep(MS_SLEEP_AFTER_FAILURE);
			m_pImpl->InitCurrent();
			++m_pImpl->fail_counter;
		}
	} while ( response == 0 && m_pImpl->fail_counter < MAX_FAILURES );

	adb_readSource_free(_readSource, env);
	_readSource = 0;

	throwOnError(env, "Failed to read JDBC source.");
	m_pImpl->throwOnNullResponse((void*)response, "Failed to read JDBC source.");
	throwOnError(adb_readSourceResponse_get_readSourceReturn(response, env), env, "Failed to read JDBC source.");
	adb_readSourceResponse_free(response, env);

	m_pImpl->CleanCurrent();
}

void JdbcEngineWrapper::writeTarget(const DbConnectionInfoStruct& connectionParams, const string& sTargetGUID,
									int insertUpdateModeFlag,  bool bTransaction, const string& sPreTargetSql, 
									bool bIncludeNulls, const string& sJtrFileName, const string& sDataFileName_hack,
									const string& sDebugLogFile_hack,
									string& sWarnings)
{
	Check();
	const axutil_env_t *env = m_pImpl->env;

	const string& sDataFileName = getDataFileNameHack(sDataFileName_hack, sDebugLogFile_hack);

	                       
	adb_WsWriteTargetParams_t* _writeTargetParams = adb_WsWriteTargetParams_create(env);

	adb_WsWriteTargetParams_set_dataFileName(_writeTargetParams, env, sDataFileName.c_str());
	adb_WsWriteTargetParams_set_insertUpdateMode(_writeTargetParams, env, insertUpdateModeFlag);
	adb_WsWriteTargetParams_set_jtrFileName(_writeTargetParams, env, sJtrFileName.c_str());
	adb_WsWriteTargetParams_set_targetGuid(_writeTargetParams, env, sTargetGUID.c_str());
	adb_WsWriteTargetParams_set_transaction(_writeTargetParams, env, bTransaction ? AXIS2_TRUE : AXIS2_FALSE);

	                 
	adb_ArrayOfWsKeyValue_t* _keyValueArray = adb_ArrayOfWsKeyValue_create(env);

	                 
	adb_WsKeyValue_t* _preTargetSqlKeyValue = adb_WsKeyValue_create(env);
	adb_WsKeyValue_set_key(_preTargetSqlKeyValue, env, TARGET_PRE_SQL);
	adb_WsKeyValue_set_value(_preTargetSqlKeyValue, env, sPreTargetSql.c_str());	
	adb_ArrayOfWsKeyValue_add_item(_keyValueArray, env, _preTargetSqlKeyValue);

	                 
	adb_WsKeyValue_t* _includeNullsKeyValue = adb_WsKeyValue_create(env);
	adb_WsKeyValue_set_key(_includeNullsKeyValue, env, TARGET_INCLUDE_NULL_IN_SQL_STATEMENT);
	adb_WsKeyValue_set_value(_includeNullsKeyValue, env, bIncludeNulls ? "true" : "false");
	adb_ArrayOfWsKeyValue_add_item(_keyValueArray, env, _includeNullsKeyValue);

	adb_WsWriteTargetParams_set_keyValues(_writeTargetParams, env, _keyValueArray);

	adb_WsWriteTargetParams_set_connectionParams(_writeTargetParams, env, createWsConnectionParams(env, connectionParams));

	adb_writeTarget_t* _writeTarget = adb_writeTarget_create(env);
	adb_writeTarget_set_in0(_writeTarget, env, createWsJdbcSessionId(env, m_pImpl->userName, m_pImpl->md5Pwd, m_pImpl->sessionGuid));
	adb_writeTarget_set_in1(_writeTarget, env, _writeTargetParams);

	       
	m_pImpl->fail_counter = 0;
	adb_writeTargetResponse_t* response = 0;
	do {
		response = axis2_stub_op_JdbcEngineService_writeTarget(m_pImpl->jdbcEngine, env, _writeTarget);
		if ( response == 0 ) {
			std::cout << "writeTarget returned NULL, fail_counter = " << m_pImpl->fail_counter << std::endl;
			m_pImpl->CleanCurrent();
			konga::MilliSleep(MS_SLEEP_AFTER_FAILURE);
			m_pImpl->InitCurrent();
			++m_pImpl->fail_counter;
		}
	} while ( response == 0 && m_pImpl->fail_counter < MAX_FAILURES );

	adb_writeTarget_free(_writeTarget, env);
	_writeTarget = 0;
	throwOnError(env, "Failed to write JDBC target.");
	m_pImpl->throwOnNullResponse((void*)response, "Failed to write JDBC target.");

	throwOnError(adb_writeTargetResponse_get_writeTargetReturn(response, env), env, "Failed to write JDBC target.");
	sWarnings = getWarnings(adb_writeTargetResponse_get_writeTargetReturn(response, env), env);

	adb_writeTargetResponse_free(response, env);

	m_pImpl->CleanCurrent();
}

namespace {

	int getRowCount(const int updateCount) {
		if ( updateCount >= 0 ) {
			return updateCount;
		}
		else {
			return 0;
		}
	}
}

string JdbcEngineWrapper::dbLookup(const DbConnectionInfoStruct& connectionParams, const string& locationGUID,
								   const bool isSource, const bool bAutoCommit,
								   const string& sql, int& rowCount, bool& nullRetVal)
{
	Check();
	const axutil_env_t *env = m_pImpl->env;

	                       
	adb_dbLookup_t* _dbLookup = adb_dbLookup_create(env);
	adb_dbLookup_set_in0(_dbLookup, env, createWsJdbcSessionId(env, m_pImpl->userName, m_pImpl->md5Pwd, m_pImpl->sessionGuid));
	adb_dbLookup_set_in1(_dbLookup, env, createWsDbLookupParams(env, connectionParams, locationGUID, isSource, bAutoCommit, sql));

	        
	                                                                                                                
	                                                                                                 
	m_pImpl->fail_counter = 0;
	adb_dbLookupResponse_t* response = 0;
	do {
		response = axis2_stub_op_JdbcEngineService_dbLookup(m_pImpl->jdbcEngine, env, _dbLookup);
		if ( response == 0 ) {
			std::cout << "dbLookup returned NULL, fail_counter = " << m_pImpl->fail_counter << std::endl;
			m_pImpl->CleanCurrent();
			konga::MilliSleep(MS_SLEEP_AFTER_FAILURE);
			m_pImpl->InitCurrent();
			++m_pImpl->fail_counter;
		}
	} while ( response == 0 && m_pImpl->fail_counter < MAX_FAILURES );

	adb_dbLookup_free(_dbLookup, env);
	_dbLookup = 0;
	throwOnError(env, "");
	m_pImpl->throwOnNullResponse((void*)response, "Failed to execute SQL.");

	                        
	adb_WsDbLookupResult_t* result = adb_dbLookupResponse_get_dbLookupReturn(response, env);
	throwOnError(adb_WsDbLookupResult_get_returnValue(result, env), env, "");

	axis2_char_t* _value = adb_WsDbLookupResult_get_value(result, env);
	nullRetVal = (adb_WsDbLookupResult_is_value_nil(result, env) == AXIS2_TRUE);
	const int updateCount = adb_WsDbLookupResult_get_updateCount(result, env);
	const string value( _value != 0 && nullRetVal == false ? konga::translate_html(_value) : "" );

	rowCount = getRowCount(updateCount);
	adb_dbLookupResponse_free(response, env);

	return value;
}

void JdbcEngineWrapper::dbExecute(const DbConnectionInfoStruct& connectionParams, const string& locationGUID,
								  const bool isSource, const bool bAutoCommit,
								  const string& sql, const int max_rows,
								  DbRowSetData& rowset, int& rowCount, bool& nullRetVal)
{
	Check();
	const axutil_env_t *env = m_pImpl->env;

	                       
	adb_dbExecute_t* _dbExecute = adb_dbExecute_create(env);
	adb_dbExecute_set_in0(_dbExecute, env, createWsJdbcSessionId(env, m_pImpl->userName, m_pImpl->md5Pwd, m_pImpl->sessionGuid));
	adb_dbExecute_set_in1(_dbExecute, env, createWsDbLookupParams(env, connectionParams, locationGUID, isSource, bAutoCommit, sql));
	adb_dbExecute_set_in2(_dbExecute, env, max_rows);

	       
	                                
	m_pImpl->fail_counter = 0;
	adb_dbExecuteResponse_t* response = 0;
	do {
		response = axis2_stub_op_JdbcEngineService_dbExecute(m_pImpl->jdbcEngine, env, _dbExecute);
		if ( response == 0 ) {
			std::cout << "dbExecute returned NULL, fail_counter = " << m_pImpl->fail_counter << std::endl;
			m_pImpl->CleanCurrent();
			konga::MilliSleep(MS_SLEEP_AFTER_FAILURE);
			m_pImpl->InitCurrent();
			++m_pImpl->fail_counter;
		}
	} while ( response == 0 && m_pImpl->fail_counter < MAX_FAILURES );

	adb_dbExecute_free(_dbExecute, env);
	_dbExecute = 0;
	throwOnError(env, "");
	m_pImpl->throwOnNullResponse((void*)response, "");

	adb_WsDbExecuteResult_t* result = adb_dbExecuteResponse_get_dbExecuteReturn(response, env);
	throwOnError(adb_WsDbExecuteResult_get_returnValue(result, env), env, "");

	const int updateCount = adb_WsDbExecuteResult_get_updateCount(result, env);
	adb_WsResultSet_t* wsResultSet = adb_WsDbExecuteResult_get_resultSet(result, env);
	adb_ArrayOfWsResultSetRow_t* wsRows = adb_WsResultSet_get_rows(wsResultSet, env);

	rowCount = getRowCount(updateCount);

	const int nRows = adb_ArrayOfWsResultSetRow_sizeof_item(wsRows, env);
	if ( nRows > 0 ) rowCount = nRows;

	                      
	for( int iRow = 0; iRow < nRows; ++iRow ) {
		std::auto_ptr<DbRowData> row(new DbRowData());

		adb_WsResultSetRow_t* wsRow = adb_ArrayOfWsResultSetRow_get_item_at(wsRows, env, iRow);
		adb_ArrayOf_xsd_string_t* wsValues = adb_WsResultSetRow_get_columns(wsRow, env);
		const int nValues = adb_ArrayOf_xsd_string_sizeof_item(wsValues, env);

		                        
		for( int iValue = 0; iValue < nValues; ++iValue ) {
			const axis2_char_t* value = adb_ArrayOf_xsd_string_get_item_at(wsValues, env, iValue);
			                                                                
			row->push_back( (value == 0) ? new DbData() : new DbData(string(konga::translate_html(value))) );
		}

		               
		rowset.push_back(row.release());
	}
	adb_dbExecuteResponse_free(response, env);
	nullRetVal = false;	                                                                    
}

void JdbcEngineWrapper::commitTransactions() {
	Check();
	const axutil_env_t* env = m_pImpl->env;

	adb_commitTransactions_t* _commitTransactions = adb_commitTransactions_create(env);
	adb_commitTransactions_set_in0(_commitTransactions, env, createWsJdbcSessionId(env, m_pImpl->userName, m_pImpl->md5Pwd, m_pImpl->sessionGuid));

	       
	adb_commitTransactionsResponse_t* response = axis2_stub_op_JdbcEngineService_commitTransactions(m_pImpl->jdbcEngine, env, _commitTransactions);
	adb_commitTransactions_free(_commitTransactions, env);
	_commitTransactions = 0;

	throwOnError(env, "Failed to commit JDBC transactions.");
	                                         
	if ( response == 0 ) return;

	throwOnError(adb_commitTransactionsResponse_get_commitTransactionsReturn(response, env), env, "Failed to commit JDBC transactions.");
	adb_commitTransactionsResponse_free(response, env);
}

void JdbcEngineWrapper::rollBackTransactions() {
	                                            
	throw CrException("rollBackTransactions() for AxisC has not been implemented yet!");
}


                                                                    
                                                            
void JdbcEngineWrapper::Check() {
	m_pImpl->InitCurrent();
	assert(m_pImpl);
	assert(m_pImpl->env);

	                               
	if ( m_pImpl->sessionGuid.empty() ) {
		const axutil_env_t* env = m_pImpl->env;
		adb_startNewSession_t* _startNewSession = adb_startNewSession_create(env);
		adb_startNewSession_set_in0(_startNewSession, env, m_pImpl->userName.c_str());
		adb_startNewSession_set_in1(_startNewSession, env, m_pImpl->md5Pwd.c_str());

		       
		m_pImpl->fail_counter = 0;
		adb_startNewSessionResponse_t* response = 0;
		do {
			response = axis2_stub_op_JdbcEngineService_startNewSession(m_pImpl->jdbcEngine, env, _startNewSession);
			if ( response == 0 ) {
				std::cout << "startNewSession returned NULL, fail_counter = " << m_pImpl->fail_counter << std::endl;
				m_pImpl->CleanCurrent();
				konga::MilliSleep(MS_SLEEP_AFTER_FAILURE);
				m_pImpl->InitCurrent();
				++m_pImpl->fail_counter;
			}
		} while ( response == 0 && m_pImpl->fail_counter < MAX_FAILURES );

		adb_startNewSession_free(_startNewSession, env);
		m_pImpl->throwOnNullResponse((void*)response, "Failed to start new session.");

		adb_WsJdbcStartNewSessionResult_t* result = adb_startNewSessionResponse_get_startNewSessionReturn(response, env);
		throwOnError(adb_WsJdbcStartNewSessionResult_get_returnValue(result, env), env, "Failed to start JDBC session.");

		axis2_char_t* sessionId = adb_WsJdbcStartNewSessionResult_get_sessionId(result, env);
		if ( sessionId ) {
			m_pImpl->sessionGuid = sessionId;
			if ( m_pImpl->sessionGuid.empty() ) {
				throw InternalErrorException("An empty session ID was returned from the JDBC engine!");
			}
		}
		else {
			throw InternalErrorException("A NULL session ID was returned!");
		}
		adb_startNewSessionResponse_free(response, env);
	}
}
