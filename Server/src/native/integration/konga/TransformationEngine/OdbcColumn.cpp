/*
 * Copyright (c) 2010-2010 Jitterbit, Inc.
 *
 */
#ifdef WIN32
	#include <windows.h>
#endif
#include <sqltypes.h>
#include <sqlext.h>

#include <boost/scoped_array.hpp>

#include <konga/Utils/CsvUtils.h>
#include <konga/Utils/CrException.h>

#include <konga/CharacterEncodingConversion/TextUnicodeConversion.h>	                    
#include <konga/CharacterEncodingConversion/Utf8Conv.h>

#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>

#include "ErrorCodes/resource.h"
#include "crom.h"
#include "OdbcTable.h"
#include "OdbcColumn.h"

using std::string;

#define MAXBUFLEN 2048
#define MAXPARAMLEN 4096
#define LONGDATALEN	65536

namespace {
	void convertToInternalFormat(KString& value, char* pc, konga::Utf8Conv* conv) {
		if ( pc != 0 && *pc != 0 ) {
			if ( conv != 0 ) {
				value = conv->from(pc);
			}
			else {
				value = pc;
			}
		}
		else {
			value.Empty();
		}
	}

	void convertWideStringToInternalFormat(KString& sValue, wchar_t* pwc) {
		if ( pwc != 0 && *pwc != 0 ) {
			std::wstring input(pwc);
			konga::TextUnicodeConversion TUC;
			string output=TUC.ConvertUtf16ToUtf8(input);
			sValue = output;
		}
		else {
			sValue.Empty();
		}
	}

	void convertBinaryToHex(KString& sValue, char* pbd, int size) {
		if(size>0 && pbd!=0){
			sValue=konga::getHexString(pbd, size);
		}
		else {
			sValue.Empty();
		}
	}

}

KString OdbcColumn::GetValueAsString(int& iErr, konga::Utf8Conv* conv) const
{
	KString sValue;
	iErr = 0;

	switch(m_ColType){
	case t_short:
		if(m_ColumnValue.ps)
			sValue.Format("%d", *m_ColumnValue.ps);
		break;
	case t_int:
		if(m_ColumnValue.pi)
			sValue.Format("%d", *m_ColumnValue.pi);
		break;
	case t_long:
		if(m_ColumnValue.pl)
			sValue.Format("%lld", *m_ColumnValue.pl);
		break;
	case t_float:
		if(m_ColumnValue.pf){
			std::stringstream ss;
			ss.precision(7);
			ss << *m_ColumnValue.pf << '\0';
			sValue = ss.str();
		}
		break;
	case t_double:
		if(m_ColumnValue.pd){
			std::stringstream ss;
			ss.precision(14);
			ss << *m_ColumnValue.pd << '\0';
			sValue = ss.str();
		}
		break;
	case t_string:
		convertToInternalFormat(sValue, m_ColumnValue.pc, conv);
		break;
	case t_widestring:
		convertWideStringToInternalFormat(sValue, m_ColumnValue.pwc);
		break;
	case t_binary:
		if(m_ColumnValue.pc){
			SQLLEN size= GetColValueSize();
			convertBinaryToHex(sValue, m_ColumnValue.pc, size);
		}
		break;
	default:
		break;
	}

	return sValue;
}

const char*OdbcColumn::getDriverName(){
	return m_odbcTable->getDriverName();
}

CViewLog* OdbcColumn::getViewLog(){
	return m_odbcTable->getViewLog();
}

CErrorLog* OdbcColumn::getErrorLog(){
	return m_odbcTable->getErrorLog();
}

OdbcColumn::OdbcColumn(OdbcTable*prepStmt, CROM*crom) : 
	m_odbcTable(prepStmt), m_column(crom), m_ColType(t_notype), m_ColValueSize(0), m_iStringSize(0)
{
	memset(&m_ColumnValue, 0, sizeof(m_ColumnValue));
}

OdbcColumn::~OdbcColumn()
{
	this->Delete();
}


void OdbcColumn::Delete()
{
	switch(m_ColType){
	case t_short:
		if(m_ColumnValue.ps){
			delete m_ColumnValue.ps;
			m_ColumnValue.ps = 0;
		}
		break;
	case t_int:
		if(m_ColumnValue.pi){
			delete m_ColumnValue.pi;
			m_ColumnValue.pi = 0;
		}
		break;
	case t_long:
		if(m_ColumnValue.pl){
			delete m_ColumnValue.pl;
			m_ColumnValue.pl = 0;
		}
		break;
	case t_float:
		if(m_ColumnValue.pf){
			delete m_ColumnValue.pf;
			m_ColumnValue.pf = 0;
		}
		break;
	case t_double:
		if(m_ColumnValue.pd){
			delete m_ColumnValue.pd;
			m_ColumnValue.pd = 0;
		}
		break;
	case t_string:
		if(m_ColumnValue.pc){
			delete[] m_ColumnValue.pc;
			m_ColumnValue.pc = 0;
		}
		break;
	case t_widestring:
		if(m_ColumnValue.pwc){
			delete[] m_ColumnValue.pwc;
			m_ColumnValue.pwc = 0;
		}
		break;
	case t_binary:
		if(m_ColumnValue.pc){
			delete[] m_ColumnValue.pc;
			m_ColumnValue.pc = 0;
		}
		break;
	default:
		break;
	}
	m_ColType = t_notype;
	m_ColValueSize = 0;
	m_iStringSize = 0;
}

void OdbcColumn::Init(OdbcColumn::ColValueType type, int size         )
{
	if(m_ColType != t_notype){
		this->Delete();
	}
	m_ColType = type;
	m_ColValueSize = 0;
	switch(type){
	case t_short:
		m_ColumnValue.ps = new short;
		break;
	case t_int:
		m_ColumnValue.pi = new long;
		break;
	case t_long:
		m_ColumnValue.pl = new long long;
		break;
	case t_float:
		m_ColumnValue.pf = new float;
		break;
	case t_double:
		m_ColumnValue.pd = new double;
		break;
	case t_string:
		if(size > 0 && size <= MAXBUFLEN){
			m_ColumnValue.pc = new char[size+1];
			m_iStringSize = size;
			m_ColumnValue.pc[size]=0;
		} else m_ColType = t_notype;
		break;
	case t_widestring:
		if(size > 0 && size <= MAXBUFLEN){
			m_ColumnValue.pwc = new wchar_t[size+1];
			m_iStringSize = size;
			m_ColumnValue.pwc[size]=0;
		} else m_ColType = t_notype;
		break;
	case t_binary:
		if(size>0  && size <= (MAXBUFLEN/2)){
			m_ColumnValue.pc = new char[size*2+1];
			m_iStringSize = size;
			m_ColumnValue.pc[size*2]=0;
		} else m_ColType = t_notype;
		break;
	default:
		m_ColType = t_notype;
		m_ColumnValue.pc=0;
		break;
	}
}


int OdbcColumn::SetShort(short i)
{
	if(m_ColType != t_short && m_ColType != t_notype)
		return 1;

	if(m_ColType == t_notype)
		m_ColType = t_short;

	if(m_ColumnValue.ps == 0)
		m_ColumnValue.ps = new short;

	*m_ColumnValue.ps = i;

	return 0;
}

int OdbcColumn::SetInt(long i)
{
	if(m_ColType != t_int && m_ColType != t_notype)
		return 1;

	if(m_ColType == t_notype)
		m_ColType = t_int;

	if(m_ColumnValue.pi == 0)
		m_ColumnValue.pi = new long;

	*m_ColumnValue.pi = i;

	return 0;
}

int OdbcColumn::SetLong(long long l)
{
	if(m_ColType != t_long && m_ColType != t_notype)
		return 1;

	if(m_ColType == t_notype)
		m_ColType = t_long;

	if(m_ColumnValue.pl == 0)
		m_ColumnValue.pl = new long long;

	*m_ColumnValue.pl = l;

	return 0;
}

int OdbcColumn::SetFloat(float f)
{
	if(m_ColType != t_float && m_ColType != t_notype)
		return 1;

	if(m_ColType == t_notype)
		m_ColType = t_float;

	if(m_ColumnValue.pf == 0)
		m_ColumnValue.pf = new float;

	*m_ColumnValue.pf = f;

	return 0;
}

int OdbcColumn::SetDouble(double d)
{
	if(m_ColType != t_double && m_ColType != t_notype)
		return 1;

	if(m_ColType == t_notype)
		m_ColType = t_double;

	if(m_ColumnValue.pd == 0)
		m_ColumnValue.pd = new double;

	*m_ColumnValue.pd = d;

	return 0;
}

int OdbcColumn::SetString(const char* pc)
{
	if(pc == 0 || (m_ColType != t_string) || m_ColumnValue.pc == 0 || m_iStringSize < 1)
		return 1;
	
	int size=m_iStringSize;
	strncpy(m_ColumnValue.pc, pc, size);

	if(size <= (int)strlen(pc))
		m_ColumnValue.pc[size] = 0;

	return 1;
}

int OdbcColumn::SetHexString(const char* pc)
{
	if(pc == 0 || (m_ColType!=t_binary) || m_ColumnValue.pc == 0 || m_iStringSize < 1)
		return 1;
	
	int size=m_iStringSize*2;
	strncpy(m_ColumnValue.pc, pc, size);

	if(size <= (int)strlen(pc))
		m_ColumnValue.pc[size] = 0;

	return 1;
}


int OdbcColumn::SetWideString(const wchar_t* pwc)
{
	if(pwc == 0 || m_ColType != t_widestring || m_ColumnValue.pwc == 0 || m_iStringSize < 1)
		return 1;
	
	wcsncpy(m_ColumnValue.pwc, pwc, m_iStringSize);

	if(m_iStringSize <= (int)wcslen(pwc))
		m_ColumnValue.pwc[m_iStringSize] = L'\0';

	return 1;
}


void OdbcColumn::bindColumn(int i){
	SQLRETURN rc=SQL_SUCCESS;
	SQLSMALLINT SQLColType = m_column->get_SqlDataType();
	const int SQLColSize = m_column->get_DbColSize();
	switch(SQLColType)
	{
		case SQL_CHAR:
		case SQL_VARCHAR:
		case SQL_LONGVARCHAR:
			Init(t_string, SQLColSize);
			SetColValueSize(SQL_NTS);
			if(SQLColSize <= MAXBUFLEN)rc = SQLBindCol(m_odbcTable->m_stmtHandle, i, SQL_C_CHAR, m_ColumnValue.pc, SQLColSize + 1, GetColValueSizePtr());
			break;
		case SQL_WCHAR:
		case SQL_WVARCHAR:
		case SQL_WLONGVARCHAR:
			Init(t_widestring, SQLColSize);
			SetColValueSize(SQL_NTS);
			if(SQLColSize <= MAXBUFLEN)rc = SQLBindCol(m_odbcTable->m_stmtHandle, i, SQL_C_WCHAR, m_ColumnValue.pwc, SQLColSize*2+2, GetColValueSizePtr());
			break;
		case SQL_BINARY:
			Init(t_binary, SQLColSize);
			if(SQLColSize <= (MAXBUFLEN/2))rc = SQLBindCol(m_odbcTable->m_stmtHandle, i, SQL_C_BINARY, m_ColumnValue.pc, SQLColSize, GetColValueSizePtr());
			break;
		case SQL_VARBINARY:
		case SQL_LONGVARBINARY:
			m_ColType = t_notype;
			break;
		case SQL_INTEGER:
			Init(t_int);
			rc = SQLBindCol(m_odbcTable->m_stmtHandle, i, SQL_C_LONG, m_ColumnValue.pi, 0, GetColValueSizePtr());
			break;
		case SQL_BIGINT:
			Init(t_long);
			rc = SQLBindCol(m_odbcTable->m_stmtHandle, i, SQL_C_SBIGINT, m_ColumnValue.pl, 0, GetColValueSizePtr());
			break;
		case SQL_SMALLINT:
		case SQL_TINYINT:
		case SQL_BIT:
			Init(t_short);
			rc = SQLBindCol(m_odbcTable->m_stmtHandle, i, SQL_C_SHORT, m_ColumnValue.ps, 0, GetColValueSizePtr());
			break;
		case SQL_REAL:
			Init(t_float);
			rc = SQLBindCol(m_odbcTable->m_stmtHandle, i, SQL_C_FLOAT, m_ColumnValue.pf, 0, GetColValueSizePtr());
			break;
		case SQL_FLOAT:
		case SQL_DOUBLE:
		case SQL_DECIMAL:
		case SQL_NUMERIC:
			Init(t_double);
			rc = SQLBindCol(m_odbcTable->m_stmtHandle, i, SQL_C_DOUBLE, m_ColumnValue.pd, 0, GetColValueSizePtr());
			break;
		default:
			Init(t_string, MAXBUFLEN);
			SetColValueSize(SQL_NTS);
			rc = SQLBindCol(m_odbcTable->m_stmtHandle, i, SQL_C_CHAR, m_ColumnValue.pc, MAXBUFLEN + 1, GetColValueSizePtr());
			break;
	}
	if(rc != SQL_SUCCESS)
	{
		                                                    
		const KString& sColName(m_column->GetDisplayName());
		std::string sErr=konga::GetError(SQL_HANDLE_STMT, m_odbcTable->m_stmtHandle);
		getErrorLog()->MakeError(__FILE__, __LINE__, ID_BINDCOLUMNS_SQLBINDCOL_ERROR, (const char*)sColName, sErr.c_str());
		throw konga::DbException((const char*)getErrorLog()->GetLastErr());
	}
}

void OdbcColumn::bindMarker(int iParam)
{
	RETCODE retcode;
	
	SQLUINTEGER		ColSize;
	SQLSMALLINT		Decimals = m_column->get_DbDecimalDigits();
	SQLINTEGER		BufferSize;

	                                                                       
	SQLSMALLINT SQLColType = m_column->get_SqlDataType();

	switch(SQLColType)
	{
	case SQL_CHAR:
	case SQL_VARCHAR:
	case SQL_LONGVARCHAR:
		ColSize = (m_column->get_DbColSize() < MAXPARAMLEN) ? m_column->get_DbColSize() : MAXPARAMLEN;
		Init(t_string, ColSize);
		BufferSize = ColSize + 1;
		SetColValueSize(SQL_NTS);
		retcode = SQLBindParameter(m_odbcTable->m_stmtHandle,
									iParam,
									SQL_PARAM_INPUT,
									SQL_C_CHAR,
									SQLColType,
									ColSize,
									Decimals,
									m_ColumnValue.pc,
									BufferSize,
									GetColValueSizePtr());
		break;
	case SQL_WCHAR:
	case SQL_WVARCHAR:
	case SQL_WLONGVARCHAR:
		ColSize = (m_column->get_DbColSize() < MAXPARAMLEN) ? m_column->get_DbColSize() : MAXPARAMLEN;
		Init(t_widestring, ColSize);
		BufferSize = ColSize + 1;
		SetColValueSize(SQL_NTS);
		retcode = SQLBindParameter(m_odbcTable->m_stmtHandle,
									iParam,
									SQL_PARAM_INPUT,
									SQL_C_WCHAR,
									SQLColType,
									ColSize,
									Decimals,
									m_ColumnValue.pwc,
									BufferSize * 2,
									GetColValueSizePtr());
		break;
	case SQL_INTEGER:
		Init(t_int);
		ColSize = 0;
		BufferSize = 0;
		retcode = SQLBindParameter(m_odbcTable->m_stmtHandle,
									iParam,
									SQL_PARAM_INPUT,
									SQL_C_LONG,
									SQLColType,
									ColSize,
									Decimals,
									m_ColumnValue.pi,
									BufferSize,
									0);
		break;
	case SQL_BIGINT:
		Init(t_long);
		ColSize = 0;
		BufferSize = 0;
		retcode = SQLBindParameter(m_odbcTable->m_stmtHandle,
									iParam,
									SQL_PARAM_INPUT,
									SQL_C_SBIGINT,
									SQLColType,
									ColSize,
									Decimals,
									m_ColumnValue.pl,
									BufferSize,
									0);
		break;
	case SQL_SMALLINT:
	case SQL_TINYINT:
	case SQL_BIT:
		Init(t_short);
		ColSize = 0;
		BufferSize = 0;
		retcode = SQLBindParameter(m_odbcTable->m_stmtHandle,
									iParam,
									SQL_PARAM_INPUT,
									SQL_C_SHORT,
									SQLColType,
									ColSize,
									Decimals,
									m_ColumnValue.ps,
									BufferSize,
									0);
		break;
	case SQL_REAL:
		Init(t_float);
		ColSize = 0;
		BufferSize = 0;
		retcode = SQLBindParameter(m_odbcTable->m_stmtHandle,
									iParam,
									SQL_PARAM_INPUT,
									SQL_C_FLOAT,
									SQLColType,
									ColSize,
									Decimals,
									m_ColumnValue.pf,
									BufferSize,
									0);
		break;
	case SQL_FLOAT:
	case SQL_DOUBLE:
	case SQL_DECIMAL:
	case SQL_NUMERIC:
		Init(t_double);
		ColSize = 0;
		BufferSize = 0;
		retcode = SQLBindParameter(m_odbcTable->m_stmtHandle,
									iParam,
									SQL_PARAM_INPUT,
									SQL_C_DOUBLE,
									SQLColType,
									ColSize,
									Decimals,
									m_ColumnValue.pd,
									BufferSize,
									0);
		break;
	case SQL_BINARY:
	case SQL_VARBINARY:
	case SQL_LONGVARBINARY:
		ColSize = (m_column->get_DbColSize() < MAXPARAMLEN) ? m_column->get_DbColSize() : MAXPARAMLEN;
		Init(t_binary, ColSize);
		BufferSize = ColSize;
		SetColValueSize(SQL_NTS);
		retcode = SQLBindParameter(m_odbcTable->m_stmtHandle,
									iParam,
									SQL_PARAM_INPUT,
									SQL_C_CHAR,
									SQLColType,
									ColSize,
									Decimals,
									m_ColumnValue.pc,
									BufferSize,
									GetColValueSizePtr());
		break;
	default:		                                               
		                           
		ColSize = (m_column->get_DbColSize() < MAXPARAMLEN) ? m_column->get_DbColSize() : MAXPARAMLEN;
		Init(t_string, ColSize);
		BufferSize = ColSize + 1;
		SetColValueSize(SQL_NTS);
		retcode = SQLBindParameter(m_odbcTable->m_stmtHandle,
									iParam,
									SQL_PARAM_INPUT,
									SQL_C_CHAR,
									SQLColType,
									ColSize,
									Decimals,
									m_ColumnValue.pc,
									BufferSize,
									GetColValueSizePtr());
		break;
	}

	if (SQLERR(retcode)) 
	{
		std::string sErr="SQL:"+m_odbcTable->m_sql+"\nError:"+konga::GetError(SQL_HANDLE_STMT, m_odbcTable->m_stmtHandle);
		const KString& sColName(m_column->GetDisplayName());
		getErrorLog()->MakeError(__FILE__, __LINE__, ID_SQL_BIND_PARAMETER_ERROR, iParam, (const char*)sColName, sErr.c_str());
		throw konga::DbException((const char*)getErrorLog()->GetLastErr());
	}
}

void OdbcColumn::saveColumnValue(const char*value, bool bIsNull){
	m_column->SetValue(value);
	m_column->SetNullFlag(bIsNull);
}

namespace {
	char* hex[]={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
}

                                                     
                                                                                                            
                                                
int OdbcColumn::getColumnValue(int iCol, KString& value, bool bFirstRow, bool& bIsNull, konga::Utf8Conv* conv)
{
	SQLLEN StrLen = SQL_NULL_DATA;
	bIsNull = false;

	value.Empty();
	if(GetValueType() != OdbcColumn::t_notype) {
		                                                                                       
		                                                          
		                                                          
		                                                       
		if(GetColValueSize() == SQL_NULL_DATA) {                    
			bIsNull = true;
			return 0;
		}
		else {
			int iErr = 0;
			value = GetValueAsString(iErr, conv);
			return iErr;
		}
	}
	else {
		                                         
		                                                            
		                                      
		KString sColName = m_column->GetDisplayName();
		int sqlDataType=m_column->get_SqlDataType();
		KString sTmpValue;
		bool bValueAssigned = false;
		char buf[MAXBUFLEN];
		bool bBinaryData=false, bWideCharType=false;;
		int chunkSize=MAXBUFLEN;
		SQLRETURN retcode;

		if((bBinaryData=konga::IsBinaryData(sqlDataType))){
			retcode = ::SQLGetData(m_odbcTable->m_stmtHandle, iCol+1, 
										 SQL_C_BINARY, buf,
										 chunkSize, &StrLen);
		}
		else if((bWideCharType=konga::IsWideCharData(sqlDataType))){
			retcode = ::SQLGetData(m_odbcTable->m_stmtHandle, iCol+1, 
										 SQL_C_WCHAR, buf,
										 chunkSize, &StrLen);
		} else {
			retcode = ::SQLGetData(m_odbcTable->m_stmtHandle, iCol+1, 
										 SQL_C_CHAR, buf,
										 chunkSize, &StrLen);
		}
		if(retcode == SQL_NO_DATA)
			return 1;

		if(retcode == SQL_SUCCESS_WITH_INFO) {
			bValueAssigned = true;

			                                                                           
			                                                                                
			                                              
			                                                                                
			                                                  
			if( strlen(buf) == 1 && konga::DriverIsOracleODBC( getDriverName() )) {
				getViewLog()->DebugString("DBTreeSource::GetValueFromDB -- Warning : Unable to retrieve the complete value of the column '%s' in the table '%s'.\n", (const char*)sColName, (const char*)m_column->GetDisplayName());
				getViewLog()->DebugString("                             --    This problem is due to the Oracle ODBC Driver. If possible, use the Microsoft ODBC for Oracle driver instead.\n");
				value = buf;
				return 0;
			}

			char pState[6];
			retcode = m_odbcTable->GetSQLState(pState);
			if(retcode != SQL_SUCCESS) {
				getViewLog()->DebugString("OdbcTable::GetValueFromDB -- Warning : Unable to retrieve the complete value of the column '%s' in the table 's'.\n", (const char*)sColName, (const char*)m_column->GetParent()->GetDisplayName());
				value = buf;
				return 0;
			}

			if( strcmp(pState,"01004") == 0 ||
				(strcmp(pState,"01S01") == 0 && konga::DriverIsMSOracleODBC(m_odbcTable->getDriverName())))
			{
				                                                                                       
				                                                                                       
				                                                                                

				value = buf;
				if(StrLen == SQL_NO_TOTAL) {
					                                               
					long iCurSize = MAXBUFLEN + LONGDATALEN;
					char *pcChunk = value.GetBufferSetLength(iCurSize);
					memcpy(pcChunk,buf,MAXBUFLEN-1);

					long iNewPos = MAXBUFLEN - 1;

					while(true) {
						retcode = ::SQLGetData(m_odbcTable->m_stmtHandle, iCol+1, 
											 SQL_C_CHAR, &pcChunk[iNewPos],
											 LONGDATALEN, &StrLen);

						if(retcode == SQL_SUCCESS || retcode == SQL_SUCCESS_WITH_INFO) {
							iCurSize += LONGDATALEN;
							pcChunk = value.GetBufferSetLength(iCurSize);
							iNewPos = iNewPos + LONGDATALEN - 1;
						}
						else {
							retcode = SQL_SUCCESS;
							break;
						}
					}
				}
				else {
					                                         
					                                         
					if(bBinaryData){
						char *pcChunk = value.GetBufferSetLength(StrLen);
						memcpy(pcChunk,buf,chunkSize);
						char *pcChunk_next = &pcChunk[chunkSize];
						chunkSize = StrLen - chunkSize;
						SQLLEN StrLen1;
						retcode = ::SQLGetData(m_odbcTable->m_stmtHandle, iCol+1, 
							SQL_C_BINARY, pcChunk_next,
											 chunkSize, &StrLen1);
						convertBinaryToHex(value, pcChunk, StrLen);
					}
					else if(bWideCharType){
						char *pcChunk = value.GetBufferSetLength(StrLen+2);
						memcpy(pcChunk,buf,chunkSize-1);
						char *pcChunk_next = &pcChunk[chunkSize-2];
						chunkSize = StrLen - chunkSize + 2;
						SQLLEN StrLen1;
						retcode = ::SQLGetData(m_odbcTable->m_stmtHandle, iCol+1, 
											 SQL_C_WCHAR, pcChunk_next,
											 chunkSize+2, &StrLen1);
						convertWideStringToInternalFormat(value, (wchar_t*)pcChunk);
					}
					else {
						char *pcChunk = value.GetBufferSetLength(StrLen);
						memcpy(pcChunk,buf,MAXBUFLEN-1);
						char *pcChunk_next = &pcChunk[chunkSize-1];
						chunkSize = StrLen - chunkSize + 1;
						SQLLEN StrLen1;
						retcode = ::SQLGetData(m_odbcTable->m_stmtHandle, iCol+1, 
											 SQL_C_CHAR, pcChunk_next,
											 chunkSize+1, &StrLen1);
						convertToInternalFormat(value, pcChunk, conv);
					}
				}
			}
			else {
				getViewLog()->DebugString("DBTreeSource::GetValueFromDB -- Warning : Unable to retrieve the complete value of the column '%s' in the table 's'.\n", (const char*)sColName, (const char*)m_column->GetDisplayName());
				if(bBinaryData){
					convertBinaryToHex(value, buf, StrLen);
				}
				else if(bWideCharType){
					convertWideStringToInternalFormat(value, (wchar_t*)buf);
				}
				else {
					convertToInternalFormat(value, buf, conv);
				}
				return 0;
			}

		}
		else if(StrLen == SQL_NULL_DATA) {
			                                                                  
			bValueAssigned = true;
			bIsNull = true;
		}
		else if(retcode == SQL_SUCCESS){
			bValueAssigned=true;
			if(bBinaryData){
				convertBinaryToHex(value, buf, StrLen);
			}
			else if(bWideCharType){
				convertWideStringToInternalFormat(value, (wchar_t*)buf);
			}
			else {
				convertToInternalFormat(value, buf, conv);
			}
			return 0;
		}

		if(!(retcode == SQL_SUCCESS || retcode == SQL_SUCCESS_WITH_INFO)) {
			                                          
			                                         
			if (bFirstRow)
			{
				std::string sql_err = konga::GetError(SQL_HANDLE_STMT, m_odbcTable->m_stmtHandle);
				getViewLog()->DebugString("Warning : Unable to access the Column # '%d' in the table '%s' the type of this column is not supported. Setting the value of this column to 0\n", iCol, (const char*)m_column->GetDisplayName());
				getViewLog()->DebugString("%s", sql_err.c_str());
			}
			value.Empty();
			bValueAssigned = true;
		}

		if(!bValueAssigned) {
			if(bBinaryData){
				value=konga::getHexString((const char*)buf, StrLen);
			}
			else {
				value = buf;
			}
		}
	}

	return 0;
}

                          
                                                                                   
                                                        
                                                                              
                                                
                                                                                             
                                                                             
                                                                                            
                                                                                             
                                                                                         
                                                                                           
                                           
unsigned long OdbcColumn::setMarkerValue()
{
	CROM*fk=m_column;
	assert(fk->IsForeignKey());
	CROM*pk=fk->get_PkLink();
	if(pk==0){
		throw konga::CrException("setMarkerValue() failed, cannot find the link column");
	}
	if(pk->IsNull()){
		return 1;
	}
	const char* sLinkValue=(const char*)pk->GetValue();
	fk->SetValue(sLinkValue);
	fk->SetNullFlag(false);


	SQLSMALLINT SQLDataType = m_column->get_SqlDataType();
	switch(SQLDataType)
	{
	case SQL_CHAR:
	case SQL_VARCHAR:
	case SQL_LONGVARCHAR:
		SetString(sLinkValue);
		break;
	case SQL_WCHAR:
	case SQL_WVARCHAR:
	case SQL_WLONGVARCHAR:
		{
			int iLength = strlen(sLinkValue);
			wchar_t* pwc = new wchar_t[iLength+1];
			for(int i=0; i<iLength; ++i)
				pwc[i] = wchar_t(sLinkValue[i]);
			pwc[iLength] = L'\0';
			SetWideString(pwc);
			delete[] pwc;
			break;
		}
	case SQL_INTEGER:
		{
			long lValue = atol(sLinkValue);
			SetInt(lValue);
			break;
		}
	case SQL_BIGINT:
		{
			long long lValue = atoll(sLinkValue);
			SetLong(lValue);
			break;
		}
	case SQL_SMALLINT:
	case SQL_BIT:
	case SQL_TINYINT:
		{
			short iValue = atoi(sLinkValue);
			SetShort(iValue);
			break;
		}
	case SQL_REAL:
		{
			float fValue = (float)atof(sLinkValue);
			SetFloat(fValue);
			break;
		}
	case SQL_FLOAT:
	case SQL_DOUBLE:
	case SQL_DECIMAL:
	case SQL_NUMERIC:
		{
			double dValue = atof(sLinkValue);
			SetDouble(dValue);
			break;
		}
	case SQL_BINARY:
	case SQL_VARBINARY:
	case SQL_LONGVARBINARY:
	default:
		SetHexString(sLinkValue);
		break;
	}
	return S_OK;
}
