/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef TRANSFORMATION_ENGINE_CROM_H
#define TRANSFORMATION_ENGINE_CROM_H

#include <vector>
#include <string>
#include <map>

#include <boost/noncopyable.hpp>

#include <xercesc/sax/AttributeList.hpp>                                

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/MapUtils.h>
#include <konga/Utils/HashTable.h>

#include <konga/Utils/DateUtils.h>
#include <konga/Utils/KString.h>

#include <konga/DataElement/DataElement.h>

#include "CromConstants.h"
#include "CROM_DB.h"
#include "CROM_Attr.h"

class CROMData;
class TokenArray;
class TokenArrayListItem;

typedef std::vector<TokenArray*> TALIST;

class TreeMapper;
class CTransform;
class CROM_Text;
class CROM_Xml;
class CROM_Ldap;
class CROM_Attr;

namespace konga {
	class Utf8Conv;
}

class CROM
{
	friend class JtrReader;
	                                                        
	                                                     

public:
          
	TreeMapper*getTreeMapper(){return m_pTreeMapper;}
	enum CROM_ext_type {t_CROM_Unknown=0, t_CROM_Text, t_CROM_DB, t_CROM_Ldap, t_CROM_Xml};                                                        
	enum DB_Mode {DB_Insert, DB_Update, DB_Unmap};

	KString m_Name;
	unsigned int m_Type;
	unsigned short m_Type2;
	CROM_ext_type m_CROM_ext_type;
	KString m_display_name;
	KString m_Comment;

private:
	TreeMapper*m_pTreeMapper;
	KString m_Path;
	union MapInfo {CTransform* trans; TokenArrayListItem* item; } m_mapInfo;
	int m_instanceNumber;
	size_t pos_out, end_pos_out, pos_lnk, pos_update_source;
public:
	void setTokenArrayListItem(TokenArrayListItem*);
	CTransform* getTrans();
	const char*getSourceExpr() const;
	TALIST* getTAList() const;

public:
	CROM*m_Generator;
	std::vector<const char*>*m_deList;

protected:
	CROM *m_Parent, *m_FirstChild, *m_NextSibling, *m_PrevSibling;
            
	union CROM_ext {void *p; CROM_Text *text; CROM_DB *db; CROM_Ldap *ldap; CROM_Xml *xml; CROM_Attr *attr;} m_ext;

public:
          
	              
	CROM(TreeMapper*p):
		m_Type(CROM_t_Unknown), m_Type2(0),
		m_CROM_ext_type(t_CROM_Unknown),
		m_pTreeMapper(p),
		m_Generator(0), m_deList(0), m_instanceNumber(0),
		m_Parent(0), m_FirstChild(0), m_NextSibling(0), m_PrevSibling(0)
	{
		m_mapInfo.trans=0;
	}

	CROM(TreeMapper*p,CROM *parent, CROM *prevSibling, const char *name, const char *value,
		int type, CROM_ext_type ext_type);

	                                                            
	CROM(CROM* Parent, CROM* to_be_cloned, const std::map<std::string,CROM*>&elementTypes, std::vector<std::string>&exploded);
	explicit CROM(CROM*obj, const char*recursive_name=0);

	CROM(TreeMapper*p, CROM*parent, XERCES_CPP_NAMESPACE::AttributeList& attributes, std::map<int,std::string>& names, CROM_ext_type type);             

	CROM(CROM *parent, CROM *prevSibling, CROM*to_be_cloned, int i_mapping);
	~CROM();

	CROM *getChildFolder_recursive(const std::string&name);
	CROM *GetParent() const {return m_Parent;}
	void SetParent(CROM*obj){m_Parent=obj;}
	CROM *GetFirstChild() const {return m_FirstChild;}
	CROM *GetFirstSibling();
	void SetFirstChild(CROM*obj){m_FirstChild=obj;}
	CROM *GetLastChild();
	CROM *GetLastAttributeChild();
	CROM *GetValueNode();
	CROM *GetRoot();
	CROM *GetNextSibling() const {return m_NextSibling;}
	void SetNextSibling(CROM*obj){m_NextSibling=obj;}
	CROM *GetPreviousSibling(){return m_PrevSibling;}
	void SetPreviousSibling(CROM*obj){m_PrevSibling=obj;}
	bool HasChildren(){return m_FirstChild!=0;}
	std::string getUniversalName();

	void checkForChildrenWithSameName();

	bool IsArray() const { return (m_Type & CROM_t_Array) != 0; }
	bool IsAtLeastOne() const {return (m_Type&CROM_t_AtLeastOne)!=0;}
	bool IsCollection() const {return (m_Type&CROM_t_Collection)!=0;}
	bool ChildrenCanBeSkipped() const;
	bool IsElement() const {return (m_Type&CROM_t_Element)!=0;}
	bool IsFlat() const {return (m_Type&CROM_t_Flat)!=0;}
	bool IsLeaf() const {return (m_Type&CROM_t_AttValuePrecondition)!=0;}
	bool IsOptional() const {return m_Parent&&(m_Parent->m_Type&CROM_t_Optional)!=0;}
	bool IsOptionalNode() const {return (m_Type&CROM_t_Optional)!=0;}
	bool IsPrecondition() const {return (m_Type&CROM_t_Precondition)!=0;}
	bool IsSourceUpdateColumn() const;
	void SetArray(KString sArrayType);
	KString& GetArrayType() const;

	      
	bool IsXMLType();
	bool hasUnqualifiedElement();
	bool CouldBeRoot();
	bool IsGroupNode() const {
		if(IsLeaf())return false;
		if((m_Type&(CROM_t_Element|CROM_t_TypeDefinition))!=0)return false;                                                       
		return true;
	}
	bool IsChoiceOrAnonymousNode(){return m_Type==0 || (m_Type&(CROM_t_OneOf|CROM_t_AnonymousGroup))!=0;}
	bool IsDerivedType();
	bool IsSubstitutionGroup();
	bool IsOneOfNode() const {return (m_Type&CROM_t_OneOf)!=0;}
	bool IsXml() const {return m_CROM_ext_type==t_CROM_Xml;}
	bool IsSubstitutionGroup() const {return m_Parent!=0&&m_Parent->IsSubstitutionGroupHead();}
	bool IsSubstitutionGroupHead() const {return (m_Type&CROM_t_SubstitutionGroupHead) == CROM_t_SubstitutionGroupHead;}
	void SetMinUse(int minUse);
	int GetMinUse();
	void SetMaxUse(int maxUse);
	int GetMaxUse();
	bool IsFixedValue() const { 
		return (m_Type&CROM_t_FixedValue)!=0;
	}
	bool IsNillable();
	bool CouldBeRoot() const;
	void SetNillable(bool b);
	void SetNamespace(const std::string& sNamespace);
	std::string GetNamespace() const;

	bool IsNamespaceQualified() const;
	void SetNamespaceQualified(bool value);

	void setSchemaType(const std::string& sTypeNamespace, const std::string& sType);
	std::string	GetSchemaType() const;
	std::string	GetSchemaTypeNamespace() const;
	bool IsTypeDefinition() const {return (m_Type&CROM_t_TypeDefinition)!=0;}
	bool IsDerivedTypeHolder() const {return (m_Type&CROM_t_DerivedTypeHolder)!=0 && (m_Type&CROM_t_SubstitutionGroupHead)==0;}
	void setDerivedTypeHolder(){m_Type|=CROM_t_DerivedTypeHolder;}
	std::map<std::string,bool>& get_TypeExtensions();
	std::map<std::string,bool>& get_SubstitutionGroup();
	CROM* expandHoldersTypeNode(const char*element_type_name, std::map<std::string,CROM*>&element_type_map, std::map<std::string,CROM*>&element_map);
	void addTypeExtensions(std::map<std::string, bool>& new_set);
	void addSubstitutionGroup(std::map<std::string, bool>& new_set);
	void AddSubstitutionGroupElement(const std::string& stElementName);
	void GetSubstitutionGroupElements(std::vector<std::string>& elementNames) const;
	void SetAbstract();
	bool IsAbstract();
	bool IsAllGroup() { return (m_Type & CROM_t_AllGroup) != 0; }
	void SetAllGroup(bool bIsAllGroup);

	           
	bool isDB() const {return m_CROM_ext_type == t_CROM_DB;}
	bool IsPrimaryKey() const {return m_CROM_ext_type==t_CROM_DB && !IsElement() && (m_Type&CROM_t_PrimaryKey)!=0;}
	bool IsUpdateKey() const {return m_CROM_ext_type==t_CROM_DB && !IsElement() && (m_Type&CROM_t_UpdateKey)!=0;}
	bool IsForeignKey() const {return m_CROM_ext_type==t_CROM_DB && !IsElement() && (m_Type&CROM_t_ForeignKey)!=0;}
	bool IsOracleSeqNo() const {return (m_CROM_ext_type==t_CROM_DB && !IsElement() && m_Type&CROM_t_OracleSeqNo)!=0;}
	bool IsOuterJoin() const {return (m_Type&CROM_t_OuterJoin)!=0;}
	bool IsInnerJoin() const {return IsAtLeastOne() || IsOneToOne();}
	bool IsOneToOne() const {return IsElement() && (m_Type&CROM_t_OneToOne)!=0;}
	bool IsUDF() const {return (m_Type&CROM_t_UseDbFcn)!=0;}
	bool IsLdap() const {return m_CROM_ext_type==t_CROM_Ldap;}
	int get_SqlDataType() const;
	void set_SqlDataType(int DbTypeId);
	int get_DbColSize() const;
	void set_DbColSize(int DbColSize);
	int get_DbDecimalDigits() const;
	void set_DbDecimalDigits(int DecimalDigits);
	CROM* get_PkLink();
	void set_PkLink(CROM* pk);
	void setUseDbFunctionFlag(){m_Type|=CROM_t_UseDbFcn;}
	void setOracleSeqNoFlag(){m_Type|=CROM_t_OracleSeqNo;}
	void set_LinkToPrimaryKey(const char* LinkToPrimaryKey);
	const char* get_LinkToPrimaryKey();
	void set_dbSchema(const char*schema);
	void set_sqlTableName(const char*tableName);
	void set_sqlFilter(const char* filter);
	void clearFilter_recursive();
	const char* get_sqlFilter() const;
	void set_sqlOrderBy(const char* sSortBy);
	const char* get_sqlOrderBy() const;
	void set_manualSQL(const std::string& manualSQL);
	const std::string& get_manualSQL() const;
	bool isManualSqlStatement() const;

	bool isMapped() const;
	bool isSqlIdentity() const;
	bool isSqlNextVal() const;
	bool isSqlCurrVal() const;
	bool isUserDefinedSequence() const;
	KString getNextValSql() const;
	KString getCurrValSql() const;

	const char* get_sqlTableName() const;
	const char* get_dbSchema() const;
	CROM* DelUnMappedColumns(bool& bDeletedColumn, bool bSourceIsFlat);
	void AddSQLFilter(const char* sWhere);
	bool HasSQLOrderBy() const;
	KString GetSQLOrderBy() const;
	bool ChildColumnInOrderBy(const char* szColumnName);
	bool HasXMLTypeColumn() const;
	CROM* FindObjectInChild(int type);                            

	       
	bool IsLdapAttributeRemove() const {return m_CROM_ext_type==t_CROM_Ldap && (m_Type&CROM_t_LdapAttributeRemove)!=0;}
	bool IsLdapAttributeReplaceAll() const {return m_CROM_ext_type==t_CROM_Ldap && (m_Type&CROM_t_LdapAttributeReplaceAll)!=0;}
	bool IsValueLeaf() const {return (m_Type&CROM_t_Value)!=0;}

	       
	bool IsIdentifier() const;
	const char* getIdentifierString() const;
	short GetFieldLength() const;
	void SetFieldLength(short iLen);
	short GetFieldPosition() const;
	void SetFieldPosition(short iPos);
	void set_strQual(char c);
	void set_delimiter(char c);
	char get_strQual() const;
	char get_numQual() const;
	char get_delimiter() const;
	void set_fieldPos(short pos);
	void set_fieldLength(short length);
	short get_fieldPos() const;
	short get_fieldLength() const;

	                                   
	int CountChildren() const;
	int CountSibling() const;
	const KString& GetName() const {return m_Name;}
	const KString& GetDisplayName() const {
		if(m_display_name.IsEmpty())return (m_Name);
		else return m_display_name;
	}
	void SetDisplayName(const char*display){
		if(m_Name==display)m_display_name="";
		else m_display_name=display;
	}
	bool IsStringType() const;
	const KString& GetValue() const;
	const char* GetValueType() const;
	void SetValue(const char*str);
	int GetType() const {return m_Type;}
	CROM *FindFirstObject(const char* name,int type=CROM_t_Element,CROM*exclusion=0);
	CROM *FindObjectInChild(const char*name,int type=CROM_t_Element) const;
	CROM *FindChildFolder(const char* name);
	CROM *FindChildFolder(const char* name, bool isLoop);
	CROM *FindChildLeaf(const char* name) const;
	CROM* FindPrecondition() const;
	void DeleteNode();
	int RemoveExtraNode(int&n_loop,bool&modified);
	int Exploding(CROM*obj,int order,int&err);
	int ExplodingAttribute(CROM*obj,int order,int&err);
	void write(FILE*out);
	KString&GetTreeText(bool showNodeType=true);
	KString&GetTreeText(const KString&value,bool showNodeType=true);
	static KString convertIllegalCharacters(const char*displayName);
	KString GetNodePath(int&err);
	konga::DataElement::Type get_DataElementType();

	bool IsNull() const {return (m_Type & CROM_t_Null)!=0;}
	bool IsAttribute() const {return (m_Type & CROM_t_Attribute)!=0;}
	char GetNodeChar();
	void AddConditions(bool bCheckForRecursive = true);
	bool ParentIsLoopNode();
	static bool isNum(const char*str);
	void SetValueType(KString ValType);
	void markMapFlag();
	bool IsFieldMapped() const {return (m_Type&CROM_t_Mapped)!=0;}
	void setLoopNode(bool flag=true);
	void setAddedFlag() {m_Type|=CROM_t_Added;}
	void resetAddedFlag();
	bool isLoopNode() const {return (m_Type&CROM_t_Loop)!=0;}
	bool isAddedCopy() const {return (m_Type&CROM_t_Added)!=0;}
	bool hasDefault() const {return (m_Type&CROM_t_HasDefault)!=0;}
	void setHasDefaultFlag();
	void setHasDefault(){m_Type|=CROM_t_HasDefault;}
	void setDefault(const char*sDefault);
	void definePath(bool bShoulNotAddNodeChar=false);
	void checkLoopNode();
	void SetFlatFlag(bool flag=true);
	void SetArrayFlag(){m_Type|=CROM_t_Array;}
	CROM*getPrecondition();
	CROM*GetLoopNode();
	int updateGenerator(CROM*source);
	int	GetNodeLevel();
	bool hasValue() const;
	bool HasSubElement();
	void SetNormalizationFixGenerator(){m_Type2|=CROM_t_NormalizationFixGenerator;}
	bool IsNormalizationFixGenerator(){return (m_Type2&CROM_t_NormalizationFixGenerator)!=0;}

	void AddType(int iType) {m_Type |= iType;}
	KString& GetDefaultValue() const;
	KString& GetFormat() const;
	void SetFormat(const char* format);


	                                
	CROM*	FindChild(int nodeType, const std::string& stNodeName);
	                              
	CROM*	FindChild(int nodeType, const std::string& stNodeName, const std::string& stNamespace);
	CROM*	FindDescendant(int nodeType, const std::string& stNodeName, const std::string& stNamespace);

	int		CountChildren(int childType);

	void	SetHidden() { m_Type |= CROM_t_Hidden; }
	bool	IsHidden() { return (m_Type & CROM_t_Hidden) != 0; }
	
	void MarkAsAnyElement(const std::string& stNamespace);
	bool	IsAnyElement() { return (m_Type & CROM_t_AnyElement) != 0; }
	std::string GetAnyElementNamespace();

	static void CloneCROMChildren(const CROM* source, CROM* target, const char* recursive_name=0);
	static void AddToParent(CROM* parent, CROM* child);

	void SetComplexTypeDefinition(CROM* pDef);
	CROM* GetComplexTypeDefinition() const;
	void AddExtension(const std::string& qualifiedTypeName);
	bool hasExtension() const;
	void GetAllDerivedTypes(std::vector<std::string>& derivedTypeNames) const;

	void RemoveChild(CROM* child);

private:
	KString getPropertyStr(int type);

public:
	bool isRecursiveFolder();
	void copyRecursiveParent(const std::map<std::string,CROM*>&elementTypes, const std::map<std::string,CROM*>&elements);
	void SetNormalOrder();
	CROM* getNextAncestor(CROM*ancestor);
	CROM_ext_type get_CROM_ext_type(){return m_CROM_ext_type;}
	const char* get_Name() const {return (const char*)m_Name;}
	const char* get_Value() const;
	const char* get_DefaultValue() const;
	const char* get_Format() const;
	const char* get_Domain() const;

	const KString& get_Path();

	static CROM* get_ElementType(const std::string &schemaTypeName, const std::map<std::string,CROM*>&elementTypes);
	std::string get_SchemaTypeExpandedName();
	std::string get_exploding_id();

	void set_Name(const char*name){
		if(m_display_name.IsEmpty() && name!=0 &&name[0]!=0){
			m_display_name=m_Name;
		}
		m_Name=name;
	}
	void set_Domain(const char*domain);
	void set_ValueType(const char*value_type);
	void set_Format(const char*Format);
	void set_Value(const char*value);

	CROM_Xml* create_CROM_Xml(XERCES_CPP_NAMESPACE::AttributeList& attributes, std::map<int,std::string>& names);
	CROM_Attr* create_CROM_Attr(XERCES_CPP_NAMESPACE::AttributeList& attributes, std::map<int,std::string>& names);

	void addChild(CROM*child);
	void exploding(CROM*schemaType, const std::map<std::string,CROM*>&schemaTypes, std::vector<std::string>&exploded);
	bool IsExpanded();
	void expandCROM(std::map<std::string,CROM*>&schemaTypes, std::map<std::string,CROM*>&elements);
	CROM*insertMultipleMapping(int i_mapping);
	CROM*insertElement(CROM*to_be_copied, CROM*prev,int i_mapping);
	CROM*insertLdapAttributeCmd();
	CROM*createValueCrom(CROM* schema_type);
	CROM*findMultipleMapping(int i_mapping);
	void SetNullFlag(bool b=true);
	CROM*findFirstLoopNode(int&i);
	bool foundSegmentIdentifier(const std::string&id);
	void setupPatternMatchingFlag();
	bool hasChoiceChildren();
	void setPatternMatchingFlag(bool b);
	bool IsComplexPatternMatching(){return (m_Type2&CROM_t_PatternMatching_2)!=0;}
	bool IsAllNSQualified();
	bool hasMapped();
	bool IsKindOfString();
	void SetDBSchema(const char* db_schema);
	static std::string toString(CROM_ext_type crom_type);
	bool GetComplexTextDelimiterQualifiers(unsigned char &cDelimiter,  unsigned char &cStrQual, unsigned char &cNumQual, int &iIdColNum, int &iStartPos, int &iLen, bool &bIsDatabase);
	const char* GetForeignKeyValue();
	CROM* findSegment(const std::string& id);
	CROM* findSegment(const std::string& id, CROMData*&pData);
	void setBoolTypeFormat(const char*format);
	void addUpdateKeyIfNoneDefined();

                           
private:
	std::string m_prefix;
public:
	const std::string& getPrefix();
	void setPrefix(const std::string& prefix);

private:
	const CROM* getParent_recursive(const std::string&name) const ;
	std::string getMultipleMappingName(int i_mapping);
	int getMultipleMappingNumber();
	CROM*findLastMultipleMapping(int i_mapping);
	void expandRecursiveNode();
	CROM* findTargetLoopNode(CROM*source, std::vector<CROM*>&list);
	void setDefaultNamspace();

public:
	void savedTargetFilePositions(FILE*out, FILE*lnk, FILE* update_source_file);
	void writeBeginRecord(FILE* out, FILE*lnk, FILE* update_source_file, bool& hasValue, bool bXmlIndent=true, konga::Utf8Conv* conv = 0);
	void writeEndRecord(FILE* out, bool bXmlIndent=false, bool hasSubelement=false,bool hasValue=false);
	void eraseRecord(FILE* out, FILE* lnk, FILE* update_source);
	void SetSourceUpdateColumn(bool flag=true){
		if(true)m_Type |= CROM_t_SourceUpdateColumn;
		else m_Type &= ~CROM_t_SourceUpdateColumn;
	}
	std::string getInstanceListing() const;
	int GetInstanceNumber() const {return m_instanceNumber;}
	void setInstanceNumber(int i);
	void resetChildInstanceNumbers();
	bool isParentOf(CROM*node);
	CROM* findTargetLoopNode(CROM*source);
	CROM* getXmlNilAttribute();
	bool useXsiPrefix();
	void writeAnyElement(FILE*out);
	void writeXmlBody(FILE*out, bool& hasValue, bool bXmlIndent);
	void appendAttributeXsiType();
	const std::map<std::string,std::string>& getXmlAttributeMap();
	std::string getPrefix(const std::string& namespaceURI, const std::string& prefix0);
	void addXmlAttribute(const std::string& name, const std::string& value);
	void defineNamespacePrefix(CROM*root, const std::string&defaultNamespace);
	std::string getXmlElementName();
	void add_xsi_type_attributes();

	void setDbMode(DB_Mode mode);
	void setUnmapped(bool flag=true);
	CROM_DB::MODE getDbMode();
	bool isUnmapped();
	bool isCsv() const;

public:
	enum BatchStatus{t_BatchNode, t_BatchChildNode, t_BatchParent, t_NotBatchNode, t_UndefinedBatchNode};
	BatchStatus getBatchStatus();
	void setBatchStatus(unsigned short status);
	bool isBatchNode();
	bool isBatchParent();
	KString getSourceInstanceTag();
};

char SetQualDelimiterChar(const char*s);
std::string CharToString(unsigned char c);

#endif                                
