/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef SQLFORWRITE_BUILDER_H
#define SQLFORWRITE_BUILDER_H

#include <vector>

class CROM;
class TreeMapper;

namespace konga {
	class Utf8Conv;
}

class SqlForWrite_Builder
{
public:
	SqlForWrite_Builder(TreeMapper*p, konga::Utf8Conv* conv, bool isOracle, bool isTarget=true);
	~SqlForWrite_Builder(){}

	const std::string& getBeginQuote() const {return sBeginQuote;}
	const std::string& getEndQuote() const {return sEndQuote;}

	void buildSqlForTarget(CROM* pTableNode, CROM* pIdentityField, char override_mode);
	void buildSourceUpdateSql(const CROM* pTableNode);

	const std::vector<CROM*>& getInsertCroms() const {return insert_croms;}
	const KString& getInsertSql() const {return sqlInsert;}

	const std::vector<CROM*>& getUpdateCroms() const {return update_croms;}
	const KString& getUpdateSql() const {return sqlUpdate;}
	const KString& getCountSql() const {return sqlGetCount;}

	const KString& getDeleteSql() const {return sqlDelete;}

private:
	static std::string getSqlValue(const char*value, const CROM* const column, konga::Utf8Conv* conv, bool &bParameterized, std::string& errmsg);
	void parseSourceUpdateFields(const CROM* pTableNode, const char*update_fields);

private:
	TreeMapper*			pTreeMapper;
	konga::Utf8Conv*	m_conv;
	int					mode;
	std::string			sBeginQuote;
	std::string			sEndQuote;
	bool				bIsTarget;
	bool				bDriverIsOracle;
	const char*			update_fields;
	std::vector<CROM*>	insert_croms;
	std::vector<CROM*>	update_croms;
	std::vector<CROM*>  where_croms;
	KString				sqlTableName;
	KString				sqlInsert;
	KString				sqlUpdate;
	                                                   
	KString				sqlDelete;
	KString				sqlGetCount;
	KString				whereSql;
};


#endif                         

