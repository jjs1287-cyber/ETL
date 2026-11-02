/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef __DTDPARSER_H_
#define __DTDPARSER_H_

#include "resource.h"                      

#include <vector>
#include <string>
#include <map>

#include <konga/Db/DbUtils/DbConnection.h>	                                                           

#include "misc.h"	                                               
#include "SchemaCollection.h"
#include "XSDParser.h"

class DTDParser;
class CROM;
class CROMData;
class CViewLog;
class CErrorLog;

struct TranRelationInfo
{
	std::string parent,PK,child,FK;
	int join,one_one;
	TranRelationInfo(const std::string& stPar,
				  	 const std::string& stPK,
				  	 const std::string& stChild,
				  	 const std::string& stFK,
				  	 int iJoin, 
				  	 int iOneToOne) : parent(stPar),
				  	 				  PK(stPK),
				  	 				  child(stChild),
				  	 				  FK(stFK),
				  	 				  join(iJoin),
				  	 				  one_one(iOneToOne)
	{}
};

struct TableField
{
	std::string name,dataType,defaultValue;
	int sqlDataType,dbColSize,dbDecDigits;

	bool nullable,hasDefault;
	TableField(const std::string& stName,
			   const std::string& stDataType,
			   int sqlDataType,
			   int dbColSize,
			   int dbDecDigits,
			   const std::string& stDefault,
			   bool bNullable,
			   bool bHasDefault);
};

typedef std::vector<TableField> FieldVector;

class TableFieldVectorMap : public std::map<std::string,FieldVector*> {
public:
	~TableFieldVectorMap(){
	for(TableFieldVectorMap::iterator it=begin();it!=end();it++)
		delete it->second;
	}
};

typedef std::map<std::string,std::string> TableAliasMap;
typedef std::vector<TranRelationInfo> TranRelationVector;

class DTDParser 
{
	friend class CROM;
private:
	bool	m_bIsTarget;
	bool	m_xml;
	bool	m_bRequireValidSchema;
	bool	m_bValidateSchemaOnCreate;
	int		m_flat;                                                                           
	int		n_NoNameLoop;
	int		n_root,i_root;
	TreeMapper	*m_pTreeMapper;
	std::vector<std::string> m_vMissingDtdFiles;
	std::vector<std::string> m_vExistingDtdFiles;

	                                                          
	SchemaCollection m_SchemaCollection;

public:
	XSDParser m_SchemaParser;
	CROM*m_root;
	std::map<std::string,CROM*> m_ElementType_map;
	std::map<std::string,CROM*> m_Element_map;
	std::map<int,std::string> m_Names;
	std::map<std::string,std::string>m_jmsProperties;
	STRMAP_T strMap;
	std::vector<CROM*>m_obj_list;
	std::vector<CROM*>m_att_list;
	KString m_lastErr;
	KString m_extra_dtd_files;
	KString m_dtd_root;
	KString m_comment;

	CROM* m_conflict_target_loop_node;

	std::map<std::string, std::string> attrNS_prefix_map;

public:
	
	DTDParser(TreeMapper*p) :
	    m_bIsTarget(false),
		m_xml(false),
		m_bRequireValidSchema(true),
		m_bValidateSchemaOnCreate(true),
		m_flat(0),
		n_NoNameLoop(0),
		n_root(0),
		i_root(0),
		m_pTreeMapper(p),
		m_SchemaParser(p),
		m_root(0),
		m_conflict_target_loop_node(0)
	{
	};
	
	void cleanup();
	~DTDParser();
	TreeMapper*getTreeMapper(){return m_pTreeMapper;}
	void setTreeMapper(TreeMapper*p){
		m_pTreeMapper=p;
		m_SchemaParser.setTreeMapper(p);
	}
	void SetTarget(bool bIsTarget){m_bIsTarget=bIsTarget;}
	void SetRequireValidSchema(bool b) { m_bRequireValidSchema = b; }
	int Create(const char*structure_file_name,const char*root_str,const char*jtr_file_name,const char*type_str);

	int ReadDtdDef(const char*buf, const char*pathname,bool contains_root=false,const bool ignore_missing_imports=false);
	void write(FILE*LOG);
	CROM*FindRoot();
	int fill_children(CROM*node, int order);

	bool IsFlatFile();

	bool IsPrimaryKey(const KString strTableName, const KString strFieldName) ;
	
	int RestoreXPath(const char*path0,const char*path,
		std::vector<std::string>&str_vector,std::vector<CROM*>&node_vector);

	bool IsSingleTable(KString &TableName);
	void definePath(bool bIsFlat=false);

	XsdConstants::SchemaVersion	GetSchemaVersion() { return m_SchemaParser.GetSchemaVersion(); }
	unsigned long DoSchemaValidation(const std::string& stXmlFilePath, const std::string& stSchemaFilePath, std::string& stErr);

	std::string GetNamespacePrefix(const std::string& stNamespace)
	{ 
		return m_SchemaParser.GetNamespacePrefix(stNamespace); 
	}
	void SetValidateSchemaOnCreate(bool b) { m_bValidateSchemaOnCreate = b; }
	std::string getFirstMultiMappingInsertPath(const char*path);
	CROM* findSource_expandTreeIfNotExist(const char*path);
	CROM* findTarget_expandTreeIfNotExist(const char*path);
	CROM* insertMultipleMapping(const char* path);
	CROM* findNode(const char*path, bool isTarget);
	CROM* findNode(const std::string& path, bool isTarget);

private:
	void ProcessCondition(const char*str, int n, const char*pathname, const bool contains_root, const bool ignore_missing_imports,int&err);
	void ProcessEntity(const char*str,int n,const char*pathname,const bool ignore_missing_imports,int&err);
	CROM*ParseELEMENT(const char*str,int&err);
	CROM*ParseATTLIST(const char*str,int&err);
	int ProcessImportDTD(const char* buf, const char* pathname, KString& location, const bool ignore_missing_imports, bool& bHasMissingImports);
	CROM*FindLinkParent(const char*table_name,std::vector<int>*index_for_linked_objs,CROM*obj);
	CROM*FindLinkChild(const char*table_name,std::vector<int>*index_for_linked_objs,int&index);
	static int AddLink(CROM*parent,CROM*obj,const char*parentId,const char*linkId);
	CROM*FindTable(const char*table_name);
	void CreateAttrNS2PrefixMap();

public:
	static int DefinePrimaryKey(CROM*tableNode,const char*keyStr);
	static int DefineUpdateKey(CROM*tableNode,const char*keyStr);
	static int DefineForeignKey(CROM*tableNode,const char*pkeyStr,const char*fkeyStr);


public :	

	CROM*FindFlatNode();
	bool IsXml(){return m_xml;}
	CROM*findSourceLeaf(const char* str0);
	CROM*findSourceLoopNode(const char* str0,int&err);
	CROM*findTargetNode(const char* str0);
	CROM*findTargetNode_(const char* str0, KString& tmp);
	static bool hasInstanceTag(const KString&path){return path.Find('#')>=0;}
	static KString stripInstanceTag(const KString&path);
	int findGenerators(CROM*target);

	               
	CROM* FindColumnInFlatDB(const char* szColName);
	void MarkOrderByColumns(const char* szSqlStmt);
	void DelUnMappedColumns(bool& bDeletedColumn, bool bSourceIsFlat = false);
	KString CreateDbSelectList(const std::string&sBeginName,const std::string&sEndName);

	void DeleteRoot();

	std::vector<std::string> GetMissingDtdFiles() const {
		return m_vMissingDtdFiles;
	}

	std::vector<std::string> GetExistingDtdFiles() const {
		return m_vExistingDtdFiles;
	}

	int createJtrFromDtd(const char *dtd_file_name, const char *root_str, const char *jtr_file_name);
	unsigned long createJtrFromXSD(const char *xsd_file_name, const char *root_str, const char *jtr_file_name);
	                                                           
	int parseDtd(const char *dtd_file_name, const char *root_str, bool root_selected);
	unsigned long parseXSD(const char *xsd_file_name, const char *root_str, bool root_selected);
	unsigned long loadXSD(const std::string& stXsdFilePath, bool validate);
	void CreateCromFromJtr(const char*jtr_file_name);
	unsigned long writeJtr(const char*jtr_file_name, const char*click, const char*file_extention);
	                                 
	bool GetComplexTextDelimiterQualifiers(unsigned char &cDelimiter,  unsigned char &cStrQual, unsigned char &cNumQual, int &iIdColNum, int &iStartPos, int &iLen, bool &bIsDatabase);
	unsigned long CreateJtrForDB(long transId, int type, const std::string& stJtrFileName);
	unsigned long CreateJtrForTextDocument(int docId, const std::string& stJtrFileName);
	void addDbFilterToTableNodes(konga::DbConnection* pTranDbConn, TableAliasMap& mapTableAlias, const std::string&sBeginQuote,  const std::string&sEndQuote);
private:
	void get_connectionStr(konga::DbConnection* pTranDbConn, int transId, char cSorT, std::string& sConnectionStr, long& lTimeOut, konga::TransactionIsolationLevel& txnIsolationLevel, std::string& sBeginQuote, std::string& sEndQuote);
	int define_mapTableAlias(konga::DbConnection* pTransDbConn, int transId, int type, TableAliasMap& mapTableAlias, const std::string& sBeginQuote, const std::string& sEndQuote);
	void define_DbRelation_FieldVectors(konga::DbConnection* pTranDbConn, int transId, char sSorT, TranRelationVector& vRelations, TableFieldVectorMap& mapTableFieldVector, const std::string& sBeginQuote, const std::string& sEndQuote);
	CViewLog* getViewLog();
	CErrorLog* getErrorLog();
};


#endif                 
