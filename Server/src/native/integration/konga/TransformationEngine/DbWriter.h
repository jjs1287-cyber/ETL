/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef DBTARGET_H
#define DBTARGET_H

#include "SqlForWrite_Builder.h"
#include "DbAccess.h"

class TreeMapper;
class CROM;
class CDbAccess;
class KString;

namespace konga {
	class FileContents;
	class Utf8Conv;
}

class DbWriter
{
public:
	DbWriter(TreeMapper* pTreeMapper, const bool bIsTarget=true);
	~DbWriter();
	void setDebugLog(const std::string& debug_log_file_name);

	void TextDoc2DB(const std::string& fileName, CROM* pCROMRoot, bool &bErroccurred, int& iSuccessOperations, const std::string& pre_sql);
	void SetDoCommit(bool doCommit){m_Db.SetDoCommit(doCommit);}

	size_t readDbLine(konga::FileContents& fc, int pos, std::vector<std::string>& value_list, std::vector<char>& type_list);
	static void writeCromElement(FILE*out, CROM* pTableNode, const bool bIsFlat, bool bPrintFKValue);

private:
	TreeMapper* getTreeMapper(){return m_pTreeMapper;}
	bool enterRecord(CROM* pCrom, bool& bErroccurred, CROM*& pIdentityField);
	void cvtCromData2CromValueMap(CROMData*pDataNode, std::map<CROM*,std::string>& values);
	void fillColumnValues(const CROM*pTableNode, const std::vector<std::string>& value_list, const std::vector<char>& isNull_list);
	void debugLogPrepareStatement(const char* title, const char* sql, const std::vector<CROM*>*croms);
	void defineForeignKeyValues(CROM* pDataNode);
	void preSqlExe(const CROM* pDataNode, CROM*& pIdentityField);
	void postSqlExe(const CROM* pTableNode, CROM* pIdentityField);
	KString doDbCountRecords(const KString& countRowSql);
	bool doDbInsertRecord(const KString& InsertSql, const char* desciption, const std::vector<CROM*>& ParamCroms, bool& bErroccurred);
	bool doDbUpdateRecord(const KString& UpdateSql, const char* desciption, const std::vector<CROM*>& UpdateCroms, bool& bErroccurred);
	bool doDbDeleteRecord(const KString& DeleteSql, bool& bErroccurred);
	KString getWhereClauseForSequencePK(const CROM* pTable, const CROM* pPK);
	                                                                                                                                               


private:
	static const char cDelimiter;
	static const char cStrQual;
	int m_iMode;
	                                                                                                                         
	char m_overrideModeChar;
	SqlForWrite_Builder* m_sqlBuilder;
	CDbAccess m_Db;
	bool m_bTarget;
	bool m_bFlat;
	bool m_bIsOracle;
	bool m_bTransaction;

	TreeMapper* m_pTreeMapper;
	konga::Utf8Conv* m_conv;
	CErrorLog* m_errlog;
	CViewLog* m_viewlog;
	std::string m_sBeginQuote, m_sEndQuote;
	FILE*m_debug_log;
};



#endif              
