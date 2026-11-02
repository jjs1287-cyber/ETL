/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <cassert>
#include <exception>
#include <memory>
#include <iostream>

#include <boost/scoped_ptr.hpp>
#include <boost/thread/mutex.hpp>

#include <konga/Utils/AutoPtrCleanup.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>		                          

#include <konga/Crypto/Crypto.h>

#include <konga/Db/DbUtils/DbRowSetData.h>

#include <konga/JavaBackend/JdbcEngine/JdbcEngine.hpp>

#include <konga/JavaBackend/JavaBackendConfiguration/ConfigUtils.h>
#include <konga/JavaBackend/JavaBackendConfiguration/ServiceNames.h>
#include <konga/JavaBackend/JdbcEngine/ArrayOfWsKeyValue.hpp>
#include <konga/JavaBackend/JdbcEngine/WsKeyValue.hpp>
#include <konga/JavaBackend/JdbcEngine/WsKeyValue_Array.hpp>

#include "KeyValues.h"
#include "JdbcEngineWrapper.h"

using namespace konga;
using std::string;

namespace {
	string cleanAxisException(const std::exception& ex) {
		string in(ex.what()), out;
		ReplaceStringWithString(in, out, "java.rmi.RemoteException: ", "");
		return out;
	}
}

namespace konga {
	boost::mutex g_JdbcEngineWrapperCtor;

	class JdbcEngineWrapper_Impl {
	public:
		JdbcEngineWrapper_Impl(const string& userName, const string& md5Pwd) : m_pJdbcEngine(0),
			m_userName(userName), m_md5Pwd(md5Pwd)
		{
		}

		~JdbcEngineWrapper_Impl(void) {
			Close();
		}

		void Init() {
			if ( m_pJdbcEngine ) return;

			                                                                                                               
			boost::mutex::scoped_lock lock(g_JdbcEngineWrapperCtor);
			try {
				m_pJdbcEngine = new JdbcEngine();
			}
			catch ( const std::exception& ex ) {
				throw CrException(string("Failed to create JdbcEngine. ") + cleanAxisException(ex));
			}

			try {
				m_sessionId.setetlforgeUser(const_cast<xsd__string>(m_userName.c_str()), true);
				m_sessionId.setmd5Pwd(const_cast<xsd__string>(m_md5Pwd.c_str()), true);
				konga::ConfigureStub(*m_pJdbcEngine, JDBC_ENGINE);
				m_pJdbcEngine->setTransportProperty("Connection", "close");	                                                                      
			}
			catch(...) {
				                                                                                           
				delete m_pJdbcEngine;
				m_pJdbcEngine = 0;
				throw;
			}
			assert(m_pJdbcEngine);
		}

		void Close() {
			if ( m_pJdbcEngine ) delete m_pJdbcEngine;
			m_pJdbcEngine = 0;
		}

		JdbcEngine* m_pJdbcEngine;
		WsJdbcSessionId m_sessionId;
		
	private:
		const string m_userName, m_md5Pwd;
	};
}

JdbcEngineWrapper::JdbcEngineWrapper(const string& userName, const string& md5Pwd)
: m_pImpl(new JdbcEngineWrapper_Impl(userName, md5Pwd))
{ }

JdbcEngineWrapper::~JdbcEngineWrapper(void) {
	if ( m_pImpl ) {
		if ( m_pImpl->m_sessionId.getsessionGuid() != 0 ) {
			try {
				m_pImpl->Init();
				boost::scoped_ptr<WsJdbcError> err(
					m_pImpl->m_pJdbcEngine->closeSession(&m_pImpl->m_sessionId)
					);
				if ( err->geterrorCode() > 0 ) {
					std::cout << "closeSession " << m_pImpl->m_sessionId.getsessionGuid() << " failed" << std::endl;
					if ( err.get() ) {
						std::cout << err.get()->geterrorCode() << " " << err.get()->geterror() << std::endl;
					}
					else {
						std::cout << "No error reported!" << std::endl;
					}
				}
			}
			                                                                                                 
			catch( const std::exception& e ) {
				std::cout << "closeSession " << m_pImpl->m_sessionId.getsessionGuid() << " failed" << std::endl;
				std::cout << "std::exception caught: " << e.what() << std::endl;
			}
			catch(...) {
				std::cout << "closeSession " << m_pImpl->m_sessionId.getsessionGuid() << " failed" << std::endl;
				std::cout << "Unexpected exception." << std::endl;
			}
		}
		delete m_pImpl;
	}
}

namespace {

	                                                             
	void throwOnError(WsJdbcError& err) {
		xsd__int errorCode = err.geterrorCode();
		if ( errorCode != 0 ) {
			throw CrException(err.geterror());
		}
	}

	std::string get_xsd_string(xsd__string str) {
		return string( str==0 ? "" : str );
	}

	WsConnectionParams* AllocateWsConnectionParams(const DbConnectionInfoStruct& connectionParams) {
		WsConnectionParams* p = new WsConnectionParams();
		p->setadditionalParams(const_cast<xsd__string>(connectionParams.additionalParams.c_str()), true);
		p->setdatabase(const_cast<xsd__string>(connectionParams.database.c_str()), true);
		p->setdriverName(const_cast<xsd__string>(connectionParams.driverName.c_str()), true);
		p->setmanualConnectionString(const_cast<xsd__string>(connectionParams.manualConnectionString.c_str()), true);
		p->setpassword(const_cast<xsd__string>(connectionParams.password.c_str()), true);
		p->setport(connectionParams.port);
		p->setserver(const_cast<xsd__string>(connectionParams.server.c_str()), true);
		p->settimeout(connectionParams.timeOutSeconds);
		p->settransactionIsolationLevel(static_cast<xsd__int>(connectionParams.txnIsolationLevel));
		p->setuser(const_cast<xsd__string>(connectionParams.user.c_str()), true);

		return p;
	}

	string getDataFileNameHack(const string& sDataFileName_hack, const string& sDebugLogFile_hack) {
		if ( sDebugLogFile_hack.empty() ) return sDataFileName_hack;
		return string( sDataFileName_hack + "|" + sDebugLogFile_hack );
	}
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

	                                                                                                                 
	const string& sDataFileName = getDataFileNameHack(sDataFileName_hack, sDebugLogFile_hack);

	WsReadSourceParams params;
	params.setdataFileName(const_cast<xsd__string>(sDataFileName.c_str()), true);
	params.setjtrFileName(const_cast<xsd__string>(sJtrFileName.c_str()), true);
	params.setadHocFilter(const_cast<xsd__string>(sAdHocFilter.c_str()), true);
	params.setsourceGuid(const_cast<xsd__string>(sSourceGUID.c_str()), true);
	params.setstructureFileName(const_cast<xsd__string>(sStructureFileName.c_str()), true);
	params.setfetchSize(static_cast<xsd__int>(connectionParams.fetchSize));

	params.setconnectionParams( AllocateWsConnectionParams(connectionParams) );

	try {
		boost::scoped_ptr<WsJdbcError> err( m_pImpl->m_pJdbcEngine->readSource(&m_pImpl->m_sessionId, &params) );
		throwOnError(*err);
	}
	catch( const std::exception& ex ) {
		throw CrException("Failed to read JDBC source. " + cleanAxisException(ex));
	}
}

void JdbcEngineWrapper::writeTarget(const DbConnectionInfoStruct& connectionParams, const string& sTargetGUID,
									int insertUpdateModeFlag,  bool bTransaction, const string& sPreTargetSql, 
									bool bIncludeNulls, const string& sJtrFileName, const string& sDataFileName_hack,
									const string& sDebugLogFile_hack, string& sWarnings)
{
	Check();

	                  
	const string& sDataFileName = getDataFileNameHack(sDataFileName_hack, sDebugLogFile_hack);

	WsWriteTargetParams params;
	params.setdataFileName(const_cast<xsd__string>(sDataFileName.c_str()), true);
	params.setinsertUpdateMode(insertUpdateModeFlag);
	params.setjtrFileName(const_cast<xsd__string>(sJtrFileName.c_str()), true);
	params.settargetGuid(const_cast<xsd__string>(sTargetGUID.c_str()), true);
	params.settransaction( bTransaction ? true_ : false_);
	{
	WsKeyValue** keyValues = new WsKeyValue*[2];
	AutoArrayDelete<WsKeyValue*> adopt_keyValues(new WsKeyValue*[2]);
	std::auto_ptr<WsKeyValue> pPreTargetSql(new WsKeyValue());
	pPreTargetSql->setkey(const_cast<xsd__string>(TARGET_PRE_SQL), true);
	pPreTargetSql->setvalue(const_cast<xsd__string>(sPreTargetSql.c_str()), true);
	keyValues[0] = pPreTargetSql.release();
	std::auto_ptr<WsKeyValue> pIncludeNulls(new WsKeyValue());
	pIncludeNulls->setkey(const_cast<xsd__string>(TARGET_INCLUDE_NULL_IN_SQL_STATEMENT), true);
	pIncludeNulls->setvalue(const_cast<xsd__string>(bIncludeNulls ? "true" : "false"), true);
	keyValues[1] = pIncludeNulls.release();
	std::auto_ptr<WsKeyValue_Array> pKeyValuesArr(new WsKeyValue_Array());
	pKeyValuesArr->set(keyValues, 2);                                                                    
	std::auto_ptr<ArrayOfWsKeyValue> wsKeyValuesArr(new ArrayOfWsKeyValue());
	wsKeyValuesArr->setitem(pKeyValuesArr.get());                                                                   
	params.setkeyValues(wsKeyValuesArr.release());                                      
	}
	params.setconnectionParams( AllocateWsConnectionParams(connectionParams) );

	try {
		boost::scoped_ptr<WsJdbcError> err( m_pImpl->m_pJdbcEngine->writeTarget(&m_pImpl->m_sessionId, &params) );
		throwOnError(*err);
		sWarnings = get_xsd_string(err->getwarnings());
	}
	catch( const std::exception& ex ) {
		throw CrException("Failed to write JDBC target. " + cleanAxisException(ex));
	}
}

namespace {
	void fill_WsDbLookupParams(WsDbLookupParams& params, const DbConnectionInfoStruct& connectionParams, const string& locationGUID,
		const bool isSource, const bool bAutoCommit, const string& sql)
	{
		if ( isSource ) {
			params.setsourceGuid(const_cast<xsd__string>(locationGUID.c_str()), true);
		}
		else {
			params.settargetGuid(const_cast<xsd__string>(locationGUID.c_str()), true);
		}
		params.setautoCommit( bAutoCommit ? true_ : false_ );

		string b64sql("b64:");
		b64sql += konga::EncodeBase64((unsigned char*)(sql.c_str()), sql.size());

		params.setsql(const_cast<xsd__string>(b64sql.c_str()), true);

		params.setconnectionParams( AllocateWsConnectionParams(connectionParams) );
	}
}

string JdbcEngineWrapper::dbLookup(const DbConnectionInfoStruct& connectionParams, const string& locationGUID,
								   const bool isSource, const bool bAutoCommit,
								   const string& sql, int& rowCount, bool& nullRetVal) {
	Check();

	WsDbLookupParams params;
	fill_WsDbLookupParams(params, connectionParams, locationGUID, isSource, bAutoCommit, sql);

	nullRetVal = true;
	std::auto_ptr<WsDbLookupResult> pDbLookupResult;
	try {
		pDbLookupResult.reset( m_pImpl->m_pJdbcEngine->dbLookup(&m_pImpl->m_sessionId, &params) );
		throwOnError(*pDbLookupResult->getreturnValue());
	}
	catch( const std::exception& e ) {
		throw CrException( cleanAxisException(e) );
	}

	rowCount = 0;
	if ( pDbLookupResult.get() == 0 )
		return string();
	else {
		rowCount = pDbLookupResult->getupdateCount();
		xsd__string tmp = pDbLookupResult->getvalue();
		if ( tmp == 0 )
			return string();
		nullRetVal = false;
		return string(tmp);
	}
}

void JdbcEngineWrapper::dbExecute(const DbConnectionInfoStruct& connectionParams, const string& locationGUID,
								  const bool isSource, const bool bAutoCommit,
								  const string& sql, const int max_rows,
								  DbRowSetData& rowset, int& rowCount, bool& nullRetVal)
{
	Check();

	WsDbLookupParams params;
	fill_WsDbLookupParams(params, connectionParams, locationGUID, isSource, bAutoCommit, sql);

	std::auto_ptr<WsDbExecuteResult> wsResult;
	try {
		wsResult.reset( m_pImpl->m_pJdbcEngine->dbExecute(&m_pImpl->m_sessionId, &params, max_rows) );
		throwOnError(*wsResult->getreturnValue());
	}
	catch( const std::exception& e ) {
		throw CrException( cleanAxisException(e) );
	}

	nullRetVal = true;
	rowCount = 0;

	if ( wsResult.get() == 0 ) {
		return;
	}
	rowCount = wsResult->getupdateCount();

	WsResultSet* wsRS = wsResult->getresultSet();

	ArrayOfWsResultSetRow* arrRows = wsRS->getrows();
	if ( arrRows == 0 ) {
		return;
	}

	WsResultSetRow_Array* rows_arr = arrRows->getitem();
	if ( rows_arr == 0 ) {
		return;
	}

	                                                                                                                               
	nullRetVal = false;
	int nrows = 0;
	WsResultSetRow** rows = rows_arr->get(nrows);
	for( int irow = 0; irow < nrows; ++irow ) {
		WsResultSetRow* row = rows[irow];
		if ( row == 0 ) {
			rowset.push_back( new DbRowData() );
			continue;
		}
		ArrayOf_xsd_string* columns = row->getcolumns();
		if ( columns == 0 ) {
			rowset.push_back( new DbRowData() );
			continue;
		}
		xsd__string_Array* stringArray = columns->getitem();
		if ( stringArray == 0 ) {
			rowset.push_back( new DbRowData() );
			continue;
		}

		int ncolumns = 0;
		const xsd__string* arrColumns = stringArray->get(ncolumns);
		if ( arrColumns == 0 || ncolumns == 0) {
			rowset.push_back( new DbRowData() );
			continue;
		}

		std::auto_ptr<DbRowData> pRowData( new DbRowData() );
		for( int icol = 0; icol < ncolumns; ++icol ) {
			const xsd__string colValue = arrColumns[icol];
			if ( colValue == 0 ) {
				                       
				pRowData->push_back( new DbData() );
			}
			else {
				pRowData->push_back( new DbData(colValue) );
			}
		}
		rowset.push_back( pRowData.release() );
	}

#ifdef _DEBUG
	                                                                           
	                           
	wsResult.release();
#endif
}

void JdbcEngineWrapper::commitTransactions() {
	Check();
	boost::scoped_ptr<WsJdbcError> err( m_pImpl->m_pJdbcEngine->commitTransactions(&m_pImpl->m_sessionId) );
	throwOnError(*err);
}

void JdbcEngineWrapper::rollBackTransactions() {
	Check();
	boost::scoped_ptr<WsJdbcError> err( m_pImpl->m_pJdbcEngine->rollbackTransactions(&m_pImpl->m_sessionId) );
	throwOnError(*err);
}

                                                                    
                                                            
void JdbcEngineWrapper::Check() {
	assert(m_pImpl);
	m_pImpl->Init();

	if ( m_pImpl->m_sessionId.getsessionGuid() == 0 ) {
		try {
			boost::scoped_ptr<WsJdbcStartNewSessionResult> newSession(
				m_pImpl->m_pJdbcEngine->startNewSession(m_pImpl->m_sessionId.getetlforgeUser(), m_pImpl->m_sessionId.getmd5Pwd())
			);
			throwOnError(*newSession->getreturnValue());
			m_pImpl->m_sessionId.setsessionGuid(newSession->getsessionId(), true);
		}
		catch( const AXIS_CPP_NAMESPACE_PREFIX AxisException& ex ) {
			string err("Failed to start new JDBC session. ");
			err += ex.getMessage();
			err += "\nAxis Exception Code: ";
			err += ex.getExceptionCode();
			throw CrException(err);
		}
		catch( const std::exception& ex ) {
			throw CrException("Failed to start new JDBC session. " + cleanAxisException(ex));
		}
		catch( const CrException& ) {
			             
			throw;
		}
		catch( ... ) {
			                         
			throw InternalErrorException("Failed to start new JDBC session. Unknown exception thrown by startNewSession.");
		}
	}
}
