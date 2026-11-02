/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_DYNAMICDBRECORDSET_H
#define KONGA_DB_DYNAMICDBRECORDSET_H

#include <sqltypes.h>

#include <string>

namespace konga
{

class DbConnection;

struct FieldInfo
{
	FieldInfo() : Name(""), SqlType(-1) {}
	std::string	Name;
	long		SqlType;
};


class DynamicDbRecordSet
{
public:
    static DynamicDbRecordSet* MakeInstance(DbConnection* pConn, const std::string& stSql = "");

    DynamicDbRecordSet() {}
	virtual ~DynamicDbRecordSet() { Close(); }

    virtual void Open() = 0;

    virtual std::string GetLastError() const = 0;
	virtual long GetLastErrorNumber() const = 0;

    virtual bool IsEOF() = 0;
    virtual bool Next() = 0;
    virtual void Close() {};

	virtual int GetFieldCount() = 0;

	                     
	virtual std::string GetFieldValue(unsigned int nColIndex) = 0;
	virtual TIMESTAMP_STRUCT GetFieldValueAsTimeStampStruct(unsigned int nColIndex) = 0;
    virtual bool IsFieldNull(unsigned int nColIndex) = 0;

	virtual FieldInfo GetFieldInfo(unsigned int nColIndex) = 0;
	virtual unsigned int GetFieldIndexByName(const std::string& stName) = 0;

	virtual std::string GetFieldValue(const std::string& stName);
	
	virtual void SetSqlStatement(const std::string& stSql) = 0;
    virtual std::string GetSqlStatement() const = 0;

    virtual void ClearParameters() = 0;
    virtual void BindString(unsigned int nParamIndex, const std::string& stVal) = 0;
    virtual void BindInteger(unsigned int nParamIndex, int iVal) = 0;
    virtual void BindLong(unsigned int nParamIndex, long long lVal) = 0;
    virtual void BindTimestamp(unsigned int nParamIndex, const TIMESTAMP_STRUCT &tsVal) = 0;
    virtual void BindNull(unsigned int nParamIndex) = 0;

};

}                       



#endif                                 
