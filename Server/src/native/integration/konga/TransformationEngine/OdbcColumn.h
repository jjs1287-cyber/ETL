/*
 * Copyright (c) 2010-2010 Jitterbit, Inc.
 *
 */

#ifndef ODBC_COLUMN_H
#define ODBC_COLUMN_H

#include <string>
#include <vector>

class DbRecord;
class OdbcSource;
class CROM;

namespace konga {
	class Utf8Conv;
}

class OdbcColumn {
	friend class OdbcTable;
public:
	enum ColValueType {t_string, t_widestring, t_short, t_int, t_long, t_float, t_double, t_notype, t_binary};

private:
	OdbcTable*m_odbcTable;
	CROM*m_column;
	ColValueType m_ColType;
	                                                                 
	union COLVALUE {short* ps; long* pi; long long* pl; float* pf; double* pd; char* pc; wchar_t* pwc;} m_ColumnValue;
	SQLLEN m_ColValueSize;
	int m_iStringSize;	                                                    

private:
	void Delete();
	void Init(ColValueType type, int size = 0);
	ColValueType GetValueType() const {return m_ColType;}
	void SetColValueSize(SQLLEN size){m_ColValueSize = size;}
	SQLLEN GetColValueSize() const {return m_ColValueSize;}
	SQLLEN* GetColValueSizePtr() {return &m_ColValueSize;}

	int SetShort(short i);
	int SetInt(long i);
	int SetLong(long long l);
	int SetFloat(float f);
	int SetDouble(double d);
	int SetString(const char* pc);
	int SetHexString(const char* pc);
	int SetWideString(const wchar_t* pc);
	KString GetValueAsString(int& iErr, konga::Utf8Conv* conv) const;

protected:
	OdbcColumn(OdbcTable*prepStmt, CROM*crom);
	~OdbcColumn();

	void bindColumn(int iColumn);
	void bindMarker(int iParam);

	int getColumnValue(int iCol, KString& sColValue, bool bFirstRow, bool& bIsNull, konga::Utf8Conv* conv);
	void saveColumnValue(const char*value, bool bIsNull);
	unsigned long setMarkerValue();
	CViewLog* getViewLog();
	CErrorLog* getErrorLog();
	const char*getDriverName();
};


#endif                 
