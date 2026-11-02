/*
 * Copyright (c) 2010-2011 Jitterbit, Inc.
 *
 */
#ifndef DBSOURCE_H
#define DBSOURCE_H

#include <string>
#include <vector>
#include <map>

#include "DbAccess.h"

class TreeMapper;
class CROM;
class OdbcSource;

namespace konga {
	class Utf8Conv;
}

class DbRecord
{
	friend class OdbcSource;
private:
	DbRecord* parent;
	std::vector<CROM*>* attributes;
	std::vector<std::string> columnValues;
	std::vector<bool> columnIsNulls;
	std::vector<DbRecord*> childRecords;

protected:
	CROM* table;
public:
	DbRecord(DbRecord* parent, CROM* table, std::vector<CROM*>*attributes);
	~DbRecord();
	bool hasChildren(CROM* childTable) const;
	bool IsInnerJoin() const;
	std::vector<DbRecord*>& getChildRecords();
	void remove();
	void writeToText(FILE* out, bool isFlat);
	void loadColumns();
	std::string toString();
	void addColumn(const std::string& value, bool bIsNull);
};

class OdbcSource
{
	friend class OdbcTable;
private:
	TreeMapper* m_pTreeMapper;
	CDbAccess m_DbAccess, *m_pDbAccess2;
	konga::Utf8Conv* m_conv;
	CROM*m_pRootTable;
	FILE* m_debug_log;
	const std::string& m_sBeginQuote;
	const std::string& m_sEndQuote;
	bool m_bFlat;
	bool m_bIsOracle;
	KString m_sDriverName;
	CErrorLog*m_errlog;
	CViewLog* m_viewlog;

	std::map<CROM*, std::vector<CROM*>*> attributes_map;
	std::map<CROM*, std::vector<CROM*>*> childTables_map;
	std::map<CROM*, OdbcTable*> odbcTable_map;

	KString DSN, Username, Pwd, Driver, Server, DbName, ConnectString;
	konga::TransactionIsolationLevel lTxtIsolationLevel;
	int iTimeout;

public:
	                                      
	static long long createSourceTextDocumentFromODBC(CROM*root,
												const std::string& dataFileName,
												const std::string& adHocFilter);

	OdbcSource(TreeMapper*pTreeMapper);
	~OdbcSource();
	void setDebugLog(const std::string& debug_log_file_name);

private:
	int dumpSourceDataTree(CROM*root, const std::string& adhoc_filter, FILE*out);
	void createSourceTreeForTable(CROM* parentTable, std::vector<CROM*>* childTables, std::vector<DbRecord*>&parents, const std::string& header);

	std::string buildSqlForTable(CROM* pTable, const std::vector<CROM*>& bindableColumns, const std::vector<CROM*>& notypeColumns, const std::vector<CROM*>& fkColumns);
	void initCromMaps(CROM*root);
	int setPkLinks(CROM* table);
	void checkInnerJoin(std::vector<DbRecord*>& records);
	void writeLog(const std::string& msg);
	void writeLog(const char* msg);
	static std::string removeTagFromAdHocFilter(const std::string& adHocFilter);
	void modifyRootTableFilter(CROM* rootTable, const std::string& adHocFilter);
	void applyAdHocFilterToManualSqlStatement(CROM*rootTable, const std::string& adHocFilter, const std::string& manualSql);

protected:
	CViewLog*getViewLog(){return m_viewlog;}
	CErrorLog* getErrorLog(){return m_errlog;}
	const char* getDriverName(){return (const char*)m_sDriverName;}
	std::vector<CROM*>*getAttributes(CROM*pTable);
	std::vector<CROM*>*getChildTables(CROM*pTable);
	OdbcTable* getOdbcTable(CROM* pTable);
};

#endif             
