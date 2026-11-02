/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DB_CRTRANDB_TRANSFORMATIONSTAB_V_H
#define KONGA_DB_CRTRANDB_TRANSFORMATIONSTAB_V_H

#ifdef WIN32
	#include <windows.h>
#endif
#include <string>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DbRecordSet.h>
#include <konga/Db/DbUtils/DbNullException.h>

namespace TranDb
{

class TRANSFORMATIONSTAB_VRow
{
public:
	TRANSFORMATIONSTAB_VRow() : 
			field_Transformation_ID(0),
			IsTransformation_IDNull(true),
			IsTransformation_IDPrimaryKey(false),
			IsTransformation_IDDirty(false),
			IsTransformation_NameNull(true),
			IsTransformation_NamePrimaryKey(false),
			IsTransformation_NameDirty(false),
			field_Source_Type_ID(0),
			IsSource_Type_IDNull(true),
			IsSource_Type_IDPrimaryKey(false),
			IsSource_Type_IDDirty(false),
			field_Target_Type_ID(0),
			IsTarget_Type_IDNull(true),
			IsTarget_Type_IDPrimaryKey(false),
			IsTarget_Type_IDDirty(false),
			IsSource_Text_DocNull(true),
			IsSource_Text_DocPrimaryKey(false),
			IsSource_Text_DocDirty(false),
			IsSource_EDINull(true),
			IsSource_EDIPrimaryKey(false),
			IsSource_EDIDirty(false),
			IsSource_EDI_VerNull(true),
			IsSource_EDI_VerPrimaryKey(false),
			IsSource_EDI_VerDirty(false),
			IsSource_SourceIDNull(true),
			IsSource_SourceIDPrimaryKey(false),
			IsSource_SourceIDDirty(false),
			IsTarget_Text_DocNull(true),
			IsTarget_Text_DocPrimaryKey(false),
			IsTarget_Text_DocDirty(false),
			IsTarget_EDINull(true),
			IsTarget_EDIPrimaryKey(false),
			IsTarget_EDIDirty(false),
			IsTarget_EDI_VerNull(true),
			IsTarget_EDI_VerPrimaryKey(false),
			IsTarget_EDI_VerDirty(false),
			IsTarget_TargetIDNull(true),
			IsTarget_TargetIDPrimaryKey(false),
			IsTarget_TargetIDDirty(false),
			IsSource_XMLNull(true),
			IsSource_XMLPrimaryKey(false),
			IsSource_XMLDirty(false),
			IsTarget_XMLNull(true),
			IsTarget_XMLPrimaryKey(false),
			IsTarget_XMLDirty(false),
			IsSource_DSNNull(true),
			IsSource_DSNPrimaryKey(false),
			IsSource_DSNDirty(false),
			IsTarget_DSNNull(true),
			IsTarget_DSNPrimaryKey(false),
			IsTarget_DSNDirty(false),
			IsSource_TablesNull(true),
			IsSource_TablesPrimaryKey(false),
			IsSource_TablesDirty(false),
			IsTarget_TablesNull(true),
			IsTarget_TablesPrimaryKey(false),
			IsTarget_TablesDirty(false),
			IsUpdateFieldsNull(true),
			IsUpdateFieldsPrimaryKey(false),
			IsUpdateFieldsDirty(false),
			IsSource_SQLNull(true),
			IsSource_SQLPrimaryKey(false),
			IsSource_SQLDirty(false),
			IsTarget_SQLNull(true),
			IsTarget_SQLPrimaryKey(false),
			IsTarget_SQLDirty(false),
			IsNatureOfSourceNull(true),
			IsNatureOfSourcePrimaryKey(false),
			IsNatureOfSourceDirty(false),
			IsNatureOfTargetNull(true),
			IsNatureOfTargetPrimaryKey(false),
			IsNatureOfTargetDirty(false),
			IsSource_WhereNull(true),
			IsSource_WherePrimaryKey(false),
			IsSource_WhereDirty(false),
			field_Insert_Mode(0),
			IsInsert_ModeNull(true),
			IsInsert_ModePrimaryKey(false),
			IsInsert_ModeDirty(false),
			IsTarget_DTD_URLNull(true),
			IsTarget_DTD_URLPrimaryKey(false),
			IsTarget_DTD_URLDirty(false),
			field_Source_EDI_Default(0),
			IsSource_EDI_DefaultNull(true),
			IsSource_EDI_DefaultPrimaryKey(false),
			IsSource_EDI_DefaultDirty(false),
			field_Target_EDI_Default(0),
			IsTarget_EDI_DefaultNull(true),
			IsTarget_EDI_DefaultPrimaryKey(false),
			IsTarget_EDI_DefaultDirty(false),
			IsTarget_UpdateKeysNull(true),
			IsTarget_UpdateKeysPrimaryKey(false),
			IsTarget_UpdateKeysDirty(false),
			IsSource_DiffKeysNull(true),
			IsSource_DiffKeysPrimaryKey(false),
			IsSource_DiffKeysDirty(false),
			field_IsValid(0),
			IsIsValidNull(true),
			IsIsValidPrimaryKey(false),
			IsIsValidDirty(false),
			IsSourceDTD_RootNull(true),
			IsSourceDTD_RootPrimaryKey(false),
			IsSourceDTD_RootDirty(false),
			IsTargetDTD_RootNull(true),
			IsTargetDTD_RootPrimaryKey(false),
			IsTargetDTD_RootDirty(false),
			field_Function_ID(0),
			IsFunction_IDNull(true),
			IsFunction_IDPrimaryKey(false),
			IsFunction_IDDirty(false),
			IsInputXSDFileNameNull(true),
			IsInputXSDFileNamePrimaryKey(false),
			IsInputXSDFileNameDirty(false),
			IsOutputXSDFileNameNull(true),
			IsOutputXSDFileNamePrimaryKey(false),
			IsOutputXSDFileNameDirty(false),
			IsFunctionType_NameNull(true),
			IsFunctionType_NamePrimaryKey(false),
			IsFunctionType_NameDirty(false),
			field_SourceJmsMessageId(0),
			IsSourceJmsMessageIdNull(true),
			IsSourceJmsMessageIdPrimaryKey(false),
			IsSourceJmsMessageIdDirty(false),
			field_TargetJmsMessageId(0),
			IsTargetJmsMessageIdNull(true),
			IsTargetJmsMessageIdPrimaryKey(false),
			IsTargetJmsMessageIdDirty(false)
	{
	}

	void Copy(const TRANSFORMATIONSTAB_VRow& r)
	{
		field_Transformation_ID = r.field_Transformation_ID;
		IsTransformation_IDNull = r.IsTransformation_IDNull;
		IsTransformation_IDDirty = r.IsTransformation_IDDirty;

		field_Transformation_Name = r.field_Transformation_Name;
		IsTransformation_NameNull = r.IsTransformation_NameNull;
		IsTransformation_NameDirty = r.IsTransformation_NameDirty;

		field_Source_Type_ID = r.field_Source_Type_ID;
		IsSource_Type_IDNull = r.IsSource_Type_IDNull;
		IsSource_Type_IDDirty = r.IsSource_Type_IDDirty;

		field_Target_Type_ID = r.field_Target_Type_ID;
		IsTarget_Type_IDNull = r.IsTarget_Type_IDNull;
		IsTarget_Type_IDDirty = r.IsTarget_Type_IDDirty;

		field_Source_Text_Doc = r.field_Source_Text_Doc;
		IsSource_Text_DocNull = r.IsSource_Text_DocNull;
		IsSource_Text_DocDirty = r.IsSource_Text_DocDirty;

		field_Source_EDI = r.field_Source_EDI;
		IsSource_EDINull = r.IsSource_EDINull;
		IsSource_EDIDirty = r.IsSource_EDIDirty;

		field_Source_EDI_Ver = r.field_Source_EDI_Ver;
		IsSource_EDI_VerNull = r.IsSource_EDI_VerNull;
		IsSource_EDI_VerDirty = r.IsSource_EDI_VerDirty;

		field_Source_SourceID = r.field_Source_SourceID;
		IsSource_SourceIDNull = r.IsSource_SourceIDNull;
		IsSource_SourceIDDirty = r.IsSource_SourceIDDirty;

		field_Target_Text_Doc = r.field_Target_Text_Doc;
		IsTarget_Text_DocNull = r.IsTarget_Text_DocNull;
		IsTarget_Text_DocDirty = r.IsTarget_Text_DocDirty;

		field_Target_EDI = r.field_Target_EDI;
		IsTarget_EDINull = r.IsTarget_EDINull;
		IsTarget_EDIDirty = r.IsTarget_EDIDirty;

		field_Target_EDI_Ver = r.field_Target_EDI_Ver;
		IsTarget_EDI_VerNull = r.IsTarget_EDI_VerNull;
		IsTarget_EDI_VerDirty = r.IsTarget_EDI_VerDirty;

		field_Target_TargetID = r.field_Target_TargetID;
		IsTarget_TargetIDNull = r.IsTarget_TargetIDNull;
		IsTarget_TargetIDDirty = r.IsTarget_TargetIDDirty;

		field_Source_XML = r.field_Source_XML;
		IsSource_XMLNull = r.IsSource_XMLNull;
		IsSource_XMLDirty = r.IsSource_XMLDirty;

		field_Target_XML = r.field_Target_XML;
		IsTarget_XMLNull = r.IsTarget_XMLNull;
		IsTarget_XMLDirty = r.IsTarget_XMLDirty;

		field_Source_DSN = r.field_Source_DSN;
		IsSource_DSNNull = r.IsSource_DSNNull;
		IsSource_DSNDirty = r.IsSource_DSNDirty;

		field_Target_DSN = r.field_Target_DSN;
		IsTarget_DSNNull = r.IsTarget_DSNNull;
		IsTarget_DSNDirty = r.IsTarget_DSNDirty;

		field_Source_Tables = r.field_Source_Tables;
		IsSource_TablesNull = r.IsSource_TablesNull;
		IsSource_TablesDirty = r.IsSource_TablesDirty;

		field_Target_Tables = r.field_Target_Tables;
		IsTarget_TablesNull = r.IsTarget_TablesNull;
		IsTarget_TablesDirty = r.IsTarget_TablesDirty;

		field_UpdateFields = r.field_UpdateFields;
		IsUpdateFieldsNull = r.IsUpdateFieldsNull;
		IsUpdateFieldsDirty = r.IsUpdateFieldsDirty;

		field_Source_SQL = r.field_Source_SQL;
		IsSource_SQLNull = r.IsSource_SQLNull;
		IsSource_SQLDirty = r.IsSource_SQLDirty;

		field_Target_SQL = r.field_Target_SQL;
		IsTarget_SQLNull = r.IsTarget_SQLNull;
		IsTarget_SQLDirty = r.IsTarget_SQLDirty;

		field_NatureOfSource = r.field_NatureOfSource;
		IsNatureOfSourceNull = r.IsNatureOfSourceNull;
		IsNatureOfSourceDirty = r.IsNatureOfSourceDirty;

		field_NatureOfTarget = r.field_NatureOfTarget;
		IsNatureOfTargetNull = r.IsNatureOfTargetNull;
		IsNatureOfTargetDirty = r.IsNatureOfTargetDirty;

		field_Source_Where = r.field_Source_Where;
		IsSource_WhereNull = r.IsSource_WhereNull;
		IsSource_WhereDirty = r.IsSource_WhereDirty;

		field_Insert_Mode = r.field_Insert_Mode;
		IsInsert_ModeNull = r.IsInsert_ModeNull;
		IsInsert_ModeDirty = r.IsInsert_ModeDirty;

		field_Target_DTD_URL = r.field_Target_DTD_URL;
		IsTarget_DTD_URLNull = r.IsTarget_DTD_URLNull;
		IsTarget_DTD_URLDirty = r.IsTarget_DTD_URLDirty;

		field_Source_EDI_Default = r.field_Source_EDI_Default;
		IsSource_EDI_DefaultNull = r.IsSource_EDI_DefaultNull;
		IsSource_EDI_DefaultDirty = r.IsSource_EDI_DefaultDirty;

		field_Target_EDI_Default = r.field_Target_EDI_Default;
		IsTarget_EDI_DefaultNull = r.IsTarget_EDI_DefaultNull;
		IsTarget_EDI_DefaultDirty = r.IsTarget_EDI_DefaultDirty;

		field_Target_UpdateKeys = r.field_Target_UpdateKeys;
		IsTarget_UpdateKeysNull = r.IsTarget_UpdateKeysNull;
		IsTarget_UpdateKeysDirty = r.IsTarget_UpdateKeysDirty;

		field_Source_DiffKeys = r.field_Source_DiffKeys;
		IsSource_DiffKeysNull = r.IsSource_DiffKeysNull;
		IsSource_DiffKeysDirty = r.IsSource_DiffKeysDirty;

		field_IsValid = r.field_IsValid;
		IsIsValidNull = r.IsIsValidNull;
		IsIsValidDirty = r.IsIsValidDirty;

		field_SourceDTD_Root = r.field_SourceDTD_Root;
		IsSourceDTD_RootNull = r.IsSourceDTD_RootNull;
		IsSourceDTD_RootDirty = r.IsSourceDTD_RootDirty;

		field_TargetDTD_Root = r.field_TargetDTD_Root;
		IsTargetDTD_RootNull = r.IsTargetDTD_RootNull;
		IsTargetDTD_RootDirty = r.IsTargetDTD_RootDirty;

		field_Function_ID = r.field_Function_ID;
		IsFunction_IDNull = r.IsFunction_IDNull;
		IsFunction_IDDirty = r.IsFunction_IDDirty;

		field_InputXSDFileName = r.field_InputXSDFileName;
		IsInputXSDFileNameNull = r.IsInputXSDFileNameNull;
		IsInputXSDFileNameDirty = r.IsInputXSDFileNameDirty;

		field_OutputXSDFileName = r.field_OutputXSDFileName;
		IsOutputXSDFileNameNull = r.IsOutputXSDFileNameNull;
		IsOutputXSDFileNameDirty = r.IsOutputXSDFileNameDirty;

		field_FunctionType_Name = r.field_FunctionType_Name;
		IsFunctionType_NameNull = r.IsFunctionType_NameNull;
		IsFunctionType_NameDirty = r.IsFunctionType_NameDirty;

		field_SourceJmsMessageId = r.field_SourceJmsMessageId;
		IsSourceJmsMessageIdNull = r.IsSourceJmsMessageIdNull;
		IsSourceJmsMessageIdDirty = r.IsSourceJmsMessageIdDirty;

		field_TargetJmsMessageId = r.field_TargetJmsMessageId;
		IsTargetJmsMessageIdNull = r.IsTargetJmsMessageIdNull;
		IsTargetJmsMessageIdDirty = r.IsTargetJmsMessageIdDirty;
	}

	void CopyPKs(const TRANSFORMATIONSTAB_VRow& r)
	{
		Copy(r);
	}

	void SetDirty(bool b)
	{
		IsTransformation_IDDirty = b;
		IsTransformation_NameDirty = b;
		IsSource_Type_IDDirty = b;
		IsTarget_Type_IDDirty = b;
		IsSource_Text_DocDirty = b;
		IsSource_EDIDirty = b;
		IsSource_EDI_VerDirty = b;
		IsSource_SourceIDDirty = b;
		IsTarget_Text_DocDirty = b;
		IsTarget_EDIDirty = b;
		IsTarget_EDI_VerDirty = b;
		IsTarget_TargetIDDirty = b;
		IsSource_XMLDirty = b;
		IsTarget_XMLDirty = b;
		IsSource_DSNDirty = b;
		IsTarget_DSNDirty = b;
		IsSource_TablesDirty = b;
		IsTarget_TablesDirty = b;
		IsUpdateFieldsDirty = b;
		IsSource_SQLDirty = b;
		IsTarget_SQLDirty = b;
		IsNatureOfSourceDirty = b;
		IsNatureOfTargetDirty = b;
		IsSource_WhereDirty = b;
		IsInsert_ModeDirty = b;
		IsTarget_DTD_URLDirty = b;
		IsSource_EDI_DefaultDirty = b;
		IsTarget_EDI_DefaultDirty = b;
		IsTarget_UpdateKeysDirty = b;
		IsSource_DiffKeysDirty = b;
		IsIsValidDirty = b;
		IsSourceDTD_RootDirty = b;
		IsTargetDTD_RootDirty = b;
		IsFunction_IDDirty = b;
		IsInputXSDFileNameDirty = b;
		IsOutputXSDFileNameDirty = b;
		IsFunctionType_NameDirty = b;
		IsSourceJmsMessageIdDirty = b;
		IsTargetJmsMessageIdDirty = b;
	}

	bool IsDirty() const
	{
		return IsTransformation_IDDirty ||
			IsTransformation_NameDirty ||
			IsSource_Type_IDDirty ||
			IsTarget_Type_IDDirty ||
			IsSource_Text_DocDirty ||
			IsSource_EDIDirty ||
			IsSource_EDI_VerDirty ||
			IsSource_SourceIDDirty ||
			IsTarget_Text_DocDirty ||
			IsTarget_EDIDirty ||
			IsTarget_EDI_VerDirty ||
			IsTarget_TargetIDDirty ||
			IsSource_XMLDirty ||
			IsTarget_XMLDirty ||
			IsSource_DSNDirty ||
			IsTarget_DSNDirty ||
			IsSource_TablesDirty ||
			IsTarget_TablesDirty ||
			IsUpdateFieldsDirty ||
			IsSource_SQLDirty ||
			IsTarget_SQLDirty ||
			IsNatureOfSourceDirty ||
			IsNatureOfTargetDirty ||
			IsSource_WhereDirty ||
			IsInsert_ModeDirty ||
			IsTarget_DTD_URLDirty ||
			IsSource_EDI_DefaultDirty ||
			IsTarget_EDI_DefaultDirty ||
			IsTarget_UpdateKeysDirty ||
			IsSource_DiffKeysDirty ||
			IsIsValidDirty ||
			IsSourceDTD_RootDirty ||
			IsTargetDTD_RootDirty ||
			IsFunction_IDDirty ||
			IsInputXSDFileNameDirty ||
			IsOutputXSDFileNameDirty ||
			IsFunctionType_NameDirty ||
			IsSourceJmsMessageIdDirty ||
			IsTargetJmsMessageIdDirty;
	}

	int field_Transformation_ID;
	bool IsTransformation_IDNull;
	bool IsTransformation_IDDirty;

	std::string field_Transformation_Name;
	bool IsTransformation_NameNull;
	bool IsTransformation_NameDirty;

	int field_Source_Type_ID;
	bool IsSource_Type_IDNull;
	bool IsSource_Type_IDDirty;

	int field_Target_Type_ID;
	bool IsTarget_Type_IDNull;
	bool IsTarget_Type_IDDirty;

	std::string field_Source_Text_Doc;
	bool IsSource_Text_DocNull;
	bool IsSource_Text_DocDirty;

	std::string field_Source_EDI;
	bool IsSource_EDINull;
	bool IsSource_EDIDirty;

	std::string field_Source_EDI_Ver;
	bool IsSource_EDI_VerNull;
	bool IsSource_EDI_VerDirty;

	std::string field_Source_SourceID;
	bool IsSource_SourceIDNull;
	bool IsSource_SourceIDDirty;

	std::string field_Target_Text_Doc;
	bool IsTarget_Text_DocNull;
	bool IsTarget_Text_DocDirty;

	std::string field_Target_EDI;
	bool IsTarget_EDINull;
	bool IsTarget_EDIDirty;

	std::string field_Target_EDI_Ver;
	bool IsTarget_EDI_VerNull;
	bool IsTarget_EDI_VerDirty;

	std::string field_Target_TargetID;
	bool IsTarget_TargetIDNull;
	bool IsTarget_TargetIDDirty;

	std::string field_Source_XML;
	bool IsSource_XMLNull;
	bool IsSource_XMLDirty;

	std::string field_Target_XML;
	bool IsTarget_XMLNull;
	bool IsTarget_XMLDirty;

	std::string field_Source_DSN;
	bool IsSource_DSNNull;
	bool IsSource_DSNDirty;

	std::string field_Target_DSN;
	bool IsTarget_DSNNull;
	bool IsTarget_DSNDirty;

	std::string field_Source_Tables;
	bool IsSource_TablesNull;
	bool IsSource_TablesDirty;

	std::string field_Target_Tables;
	bool IsTarget_TablesNull;
	bool IsTarget_TablesDirty;

	std::string field_UpdateFields;
	bool IsUpdateFieldsNull;
	bool IsUpdateFieldsDirty;

	std::string field_Source_SQL;
	bool IsSource_SQLNull;
	bool IsSource_SQLDirty;

	std::string field_Target_SQL;
	bool IsTarget_SQLNull;
	bool IsTarget_SQLDirty;

	std::string field_NatureOfSource;
	bool IsNatureOfSourceNull;
	bool IsNatureOfSourceDirty;

	std::string field_NatureOfTarget;
	bool IsNatureOfTargetNull;
	bool IsNatureOfTargetDirty;

	std::string field_Source_Where;
	bool IsSource_WhereNull;
	bool IsSource_WhereDirty;

	int field_Insert_Mode;
	bool IsInsert_ModeNull;
	bool IsInsert_ModeDirty;

	std::string field_Target_DTD_URL;
	bool IsTarget_DTD_URLNull;
	bool IsTarget_DTD_URLDirty;

	int field_Source_EDI_Default;
	bool IsSource_EDI_DefaultNull;
	bool IsSource_EDI_DefaultDirty;

	int field_Target_EDI_Default;
	bool IsTarget_EDI_DefaultNull;
	bool IsTarget_EDI_DefaultDirty;

	std::string field_Target_UpdateKeys;
	bool IsTarget_UpdateKeysNull;
	bool IsTarget_UpdateKeysDirty;

	std::string field_Source_DiffKeys;
	bool IsSource_DiffKeysNull;
	bool IsSource_DiffKeysDirty;

	int field_IsValid;
	bool IsIsValidNull;
	bool IsIsValidDirty;

	std::string field_SourceDTD_Root;
	bool IsSourceDTD_RootNull;
	bool IsSourceDTD_RootDirty;

	std::string field_TargetDTD_Root;
	bool IsTargetDTD_RootNull;
	bool IsTargetDTD_RootDirty;

	int field_Function_ID;
	bool IsFunction_IDNull;
	bool IsFunction_IDDirty;

	std::string field_InputXSDFileName;
	bool IsInputXSDFileNameNull;
	bool IsInputXSDFileNameDirty;

	std::string field_OutputXSDFileName;
	bool IsOutputXSDFileNameNull;
	bool IsOutputXSDFileNameDirty;

	std::string field_FunctionType_Name;
	bool IsFunctionType_NameNull;
	bool IsFunctionType_NameDirty;

	int field_SourceJmsMessageId;
	bool IsSourceJmsMessageIdNull;
	bool IsSourceJmsMessageIdDirty;

	int field_TargetJmsMessageId;
	bool IsTargetJmsMessageIdNull;
	bool IsTargetJmsMessageIdDirty;

private:
	bool IsTransformation_IDPrimaryKey;
	bool IsTransformation_NamePrimaryKey;
	bool IsSource_Type_IDPrimaryKey;
	bool IsTarget_Type_IDPrimaryKey;
	bool IsSource_Text_DocPrimaryKey;
	bool IsSource_EDIPrimaryKey;
	bool IsSource_EDI_VerPrimaryKey;
	bool IsSource_SourceIDPrimaryKey;
	bool IsTarget_Text_DocPrimaryKey;
	bool IsTarget_EDIPrimaryKey;
	bool IsTarget_EDI_VerPrimaryKey;
	bool IsTarget_TargetIDPrimaryKey;
	bool IsSource_XMLPrimaryKey;
	bool IsTarget_XMLPrimaryKey;
	bool IsSource_DSNPrimaryKey;
	bool IsTarget_DSNPrimaryKey;
	bool IsSource_TablesPrimaryKey;
	bool IsTarget_TablesPrimaryKey;
	bool IsUpdateFieldsPrimaryKey;
	bool IsSource_SQLPrimaryKey;
	bool IsTarget_SQLPrimaryKey;
	bool IsNatureOfSourcePrimaryKey;
	bool IsNatureOfTargetPrimaryKey;
	bool IsSource_WherePrimaryKey;
	bool IsInsert_ModePrimaryKey;
	bool IsTarget_DTD_URLPrimaryKey;
	bool IsSource_EDI_DefaultPrimaryKey;
	bool IsTarget_EDI_DefaultPrimaryKey;
	bool IsTarget_UpdateKeysPrimaryKey;
	bool IsSource_DiffKeysPrimaryKey;
	bool IsIsValidPrimaryKey;
	bool IsSourceDTD_RootPrimaryKey;
	bool IsTargetDTD_RootPrimaryKey;
	bool IsFunction_IDPrimaryKey;
	bool IsInputXSDFileNamePrimaryKey;
	bool IsOutputXSDFileNamePrimaryKey;
	bool IsFunctionType_NamePrimaryKey;
	bool IsSourceJmsMessageIdPrimaryKey;
	bool IsTargetJmsMessageIdPrimaryKey;
};


class TRANSFORMATIONSTAB_V : public konga::DbRecordSet {
public:
	TRANSFORMATIONSTAB_V() {}

	static TRANSFORMATIONSTAB_V* MakeInstance(konga::DbConnection* pConn);

	std::string GetTableName() const {
		return "TRANSFORMATIONSTAB_V";
	}

	void DoFieldExchange(const TRANSFORMATIONSTAB_VRow& row) {
		m_row.Copy(row);
	}

	void Edit();

	std::string GetUpdateFilter() {
		return m_stUpdateFilter;
	}

	int GetTransformation_ID() {
		if( m_row.IsTransformation_IDNull )
			throw konga::DbNullException("Transformation_ID");
		return m_row.field_Transformation_ID;
	}
	void SetTransformation_ID(int i) {
		m_row.field_Transformation_ID = i;
		m_row.IsTransformation_IDNull = false;
		m_row.IsTransformation_IDDirty = true;
	}
	bool IsTransformation_IDNull() const {
		return m_row.IsTransformation_IDNull;
	}
	void SetTransformation_IDToNull() {
		m_row.IsTransformation_IDNull = true;
		m_row.IsTransformation_IDDirty = true;
	}

	std::string GetTransformation_Name() {
		if( m_row.IsTransformation_NameNull )
			throw konga::DbNullException("Transformation_Name");
		return m_row.field_Transformation_Name;
	}
	void SetTransformation_Name(const std::string& st) {
		m_row.field_Transformation_Name = st;
		m_row.IsTransformation_NameNull = false;
		m_row.IsTransformation_NameDirty = true;
	}
	bool IsTransformation_NameNull() const {
		return m_row.IsTransformation_NameNull;
	}
	void SetTransformation_NameToNull() {
		m_row.field_Transformation_Name = "";
		m_row.IsTransformation_NameNull = true;
		m_row.IsTransformation_NameDirty = true;
	}

	int GetSource_Type_ID() {
		if( m_row.IsSource_Type_IDNull )
			throw konga::DbNullException("Source_Type_ID");
		return m_row.field_Source_Type_ID;
	}
	void SetSource_Type_ID(int i) {
		m_row.field_Source_Type_ID = i;
		m_row.IsSource_Type_IDNull = false;
		m_row.IsSource_Type_IDDirty = true;
	}
	bool IsSource_Type_IDNull() const {
		return m_row.IsSource_Type_IDNull;
	}
	void SetSource_Type_IDToNull() {
		m_row.IsSource_Type_IDNull = true;
		m_row.IsSource_Type_IDDirty = true;
	}

	int GetTarget_Type_ID() {
		if( m_row.IsTarget_Type_IDNull )
			throw konga::DbNullException("Target_Type_ID");
		return m_row.field_Target_Type_ID;
	}
	void SetTarget_Type_ID(int i) {
		m_row.field_Target_Type_ID = i;
		m_row.IsTarget_Type_IDNull = false;
		m_row.IsTarget_Type_IDDirty = true;
	}
	bool IsTarget_Type_IDNull() const {
		return m_row.IsTarget_Type_IDNull;
	}
	void SetTarget_Type_IDToNull() {
		m_row.IsTarget_Type_IDNull = true;
		m_row.IsTarget_Type_IDDirty = true;
	}

	std::string GetSource_Text_Doc() {
		if( m_row.IsSource_Text_DocNull )
			throw konga::DbNullException("Source_Text_Doc");
		return m_row.field_Source_Text_Doc;
	}
	void SetSource_Text_Doc(const std::string& st) {
		m_row.field_Source_Text_Doc = st;
		m_row.IsSource_Text_DocNull = false;
		m_row.IsSource_Text_DocDirty = true;
	}
	bool IsSource_Text_DocNull() const {
		return m_row.IsSource_Text_DocNull;
	}
	void SetSource_Text_DocToNull() {
		m_row.field_Source_Text_Doc = "";
		m_row.IsSource_Text_DocNull = true;
		m_row.IsSource_Text_DocDirty = true;
	}

	std::string GetSource_EDI() {
		if( m_row.IsSource_EDINull )
			throw konga::DbNullException("Source_EDI");
		return m_row.field_Source_EDI;
	}
	void SetSource_EDI(const std::string& st) {
		m_row.field_Source_EDI = st;
		m_row.IsSource_EDINull = false;
		m_row.IsSource_EDIDirty = true;
	}
	bool IsSource_EDINull() const {
		return m_row.IsSource_EDINull;
	}
	void SetSource_EDIToNull() {
		m_row.field_Source_EDI = "";
		m_row.IsSource_EDINull = true;
		m_row.IsSource_EDIDirty = true;
	}

	std::string GetSource_EDI_Ver() {
		if( m_row.IsSource_EDI_VerNull )
			throw konga::DbNullException("Source_EDI_Ver");
		return m_row.field_Source_EDI_Ver;
	}
	void SetSource_EDI_Ver(const std::string& st) {
		m_row.field_Source_EDI_Ver = st;
		m_row.IsSource_EDI_VerNull = false;
		m_row.IsSource_EDI_VerDirty = true;
	}
	bool IsSource_EDI_VerNull() const {
		return m_row.IsSource_EDI_VerNull;
	}
	void SetSource_EDI_VerToNull() {
		m_row.field_Source_EDI_Ver = "";
		m_row.IsSource_EDI_VerNull = true;
		m_row.IsSource_EDI_VerDirty = true;
	}

	std::string GetSource_SourceID() {
		if( m_row.IsSource_SourceIDNull )
			throw konga::DbNullException("Source_SourceID");
		return m_row.field_Source_SourceID;
	}
	void SetSource_SourceID(const std::string& st) {
		m_row.field_Source_SourceID = st;
		m_row.IsSource_SourceIDNull = false;
		m_row.IsSource_SourceIDDirty = true;
	}
	bool IsSource_SourceIDNull() const {
		return m_row.IsSource_SourceIDNull;
	}
	void SetSource_SourceIDToNull() {
		m_row.field_Source_SourceID = "";
		m_row.IsSource_SourceIDNull = true;
		m_row.IsSource_SourceIDDirty = true;
	}

	std::string GetTarget_Text_Doc() {
		if( m_row.IsTarget_Text_DocNull )
			throw konga::DbNullException("Target_Text_Doc");
		return m_row.field_Target_Text_Doc;
	}
	void SetTarget_Text_Doc(const std::string& st) {
		m_row.field_Target_Text_Doc = st;
		m_row.IsTarget_Text_DocNull = false;
		m_row.IsTarget_Text_DocDirty = true;
	}
	bool IsTarget_Text_DocNull() const {
		return m_row.IsTarget_Text_DocNull;
	}
	void SetTarget_Text_DocToNull() {
		m_row.field_Target_Text_Doc = "";
		m_row.IsTarget_Text_DocNull = true;
		m_row.IsTarget_Text_DocDirty = true;
	}

	std::string GetTarget_EDI() {
		if( m_row.IsTarget_EDINull )
			throw konga::DbNullException("Target_EDI");
		return m_row.field_Target_EDI;
	}
	void SetTarget_EDI(const std::string& st) {
		m_row.field_Target_EDI = st;
		m_row.IsTarget_EDINull = false;
		m_row.IsTarget_EDIDirty = true;
	}
	bool IsTarget_EDINull() const {
		return m_row.IsTarget_EDINull;
	}
	void SetTarget_EDIToNull() {
		m_row.field_Target_EDI = "";
		m_row.IsTarget_EDINull = true;
		m_row.IsTarget_EDIDirty = true;
	}

	std::string GetTarget_EDI_Ver() {
		if( m_row.IsTarget_EDI_VerNull )
			throw konga::DbNullException("Target_EDI_Ver");
		return m_row.field_Target_EDI_Ver;
	}
	void SetTarget_EDI_Ver(const std::string& st) {
		m_row.field_Target_EDI_Ver = st;
		m_row.IsTarget_EDI_VerNull = false;
		m_row.IsTarget_EDI_VerDirty = true;
	}
	bool IsTarget_EDI_VerNull() const {
		return m_row.IsTarget_EDI_VerNull;
	}
	void SetTarget_EDI_VerToNull() {
		m_row.field_Target_EDI_Ver = "";
		m_row.IsTarget_EDI_VerNull = true;
		m_row.IsTarget_EDI_VerDirty = true;
	}

	std::string GetTarget_TargetID() {
		if( m_row.IsTarget_TargetIDNull )
			throw konga::DbNullException("Target_TargetID");
		return m_row.field_Target_TargetID;
	}
	void SetTarget_TargetID(const std::string& st) {
		m_row.field_Target_TargetID = st;
		m_row.IsTarget_TargetIDNull = false;
		m_row.IsTarget_TargetIDDirty = true;
	}
	bool IsTarget_TargetIDNull() const {
		return m_row.IsTarget_TargetIDNull;
	}
	void SetTarget_TargetIDToNull() {
		m_row.field_Target_TargetID = "";
		m_row.IsTarget_TargetIDNull = true;
		m_row.IsTarget_TargetIDDirty = true;
	}

	std::string GetSource_XML() {
		if( m_row.IsSource_XMLNull )
			throw konga::DbNullException("Source_XML");
		return m_row.field_Source_XML;
	}
	void SetSource_XML(const std::string& st) {
		m_row.field_Source_XML = st;
		m_row.IsSource_XMLNull = false;
		m_row.IsSource_XMLDirty = true;
	}
	bool IsSource_XMLNull() const {
		return m_row.IsSource_XMLNull;
	}
	void SetSource_XMLToNull() {
		m_row.field_Source_XML = "";
		m_row.IsSource_XMLNull = true;
		m_row.IsSource_XMLDirty = true;
	}

	std::string GetTarget_XML() {
		if( m_row.IsTarget_XMLNull )
			throw konga::DbNullException("Target_XML");
		return m_row.field_Target_XML;
	}
	void SetTarget_XML(const std::string& st) {
		m_row.field_Target_XML = st;
		m_row.IsTarget_XMLNull = false;
		m_row.IsTarget_XMLDirty = true;
	}
	bool IsTarget_XMLNull() const {
		return m_row.IsTarget_XMLNull;
	}
	void SetTarget_XMLToNull() {
		m_row.field_Target_XML = "";
		m_row.IsTarget_XMLNull = true;
		m_row.IsTarget_XMLDirty = true;
	}

	std::string GetSource_DSN() {
		if( m_row.IsSource_DSNNull )
			throw konga::DbNullException("Source_DSN");
		return m_row.field_Source_DSN;
	}
	void SetSource_DSN(const std::string& st) {
		m_row.field_Source_DSN = st;
		m_row.IsSource_DSNNull = false;
		m_row.IsSource_DSNDirty = true;
	}
	bool IsSource_DSNNull() const {
		return m_row.IsSource_DSNNull;
	}
	void SetSource_DSNToNull() {
		m_row.field_Source_DSN = "";
		m_row.IsSource_DSNNull = true;
		m_row.IsSource_DSNDirty = true;
	}

	std::string GetTarget_DSN() {
		if( m_row.IsTarget_DSNNull )
			throw konga::DbNullException("Target_DSN");
		return m_row.field_Target_DSN;
	}
	void SetTarget_DSN(const std::string& st) {
		m_row.field_Target_DSN = st;
		m_row.IsTarget_DSNNull = false;
		m_row.IsTarget_DSNDirty = true;
	}
	bool IsTarget_DSNNull() const {
		return m_row.IsTarget_DSNNull;
	}
	void SetTarget_DSNToNull() {
		m_row.field_Target_DSN = "";
		m_row.IsTarget_DSNNull = true;
		m_row.IsTarget_DSNDirty = true;
	}

	std::string GetSource_Tables() {
		if( m_row.IsSource_TablesNull )
			throw konga::DbNullException("Source_Tables");
		return m_row.field_Source_Tables;
	}
	void SetSource_Tables(const std::string& st) {
		m_row.field_Source_Tables = st;
		m_row.IsSource_TablesNull = false;
		m_row.IsSource_TablesDirty = true;
	}
	bool IsSource_TablesNull() const {
		return m_row.IsSource_TablesNull;
	}
	void SetSource_TablesToNull() {
		m_row.field_Source_Tables = "";
		m_row.IsSource_TablesNull = true;
		m_row.IsSource_TablesDirty = true;
	}

	std::string GetTarget_Tables() {
		if( m_row.IsTarget_TablesNull )
			throw konga::DbNullException("Target_Tables");
		return m_row.field_Target_Tables;
	}
	void SetTarget_Tables(const std::string& st) {
		m_row.field_Target_Tables = st;
		m_row.IsTarget_TablesNull = false;
		m_row.IsTarget_TablesDirty = true;
	}
	bool IsTarget_TablesNull() const {
		return m_row.IsTarget_TablesNull;
	}
	void SetTarget_TablesToNull() {
		m_row.field_Target_Tables = "";
		m_row.IsTarget_TablesNull = true;
		m_row.IsTarget_TablesDirty = true;
	}

	std::string GetUpdateFields() {
		if( m_row.IsUpdateFieldsNull )
			throw konga::DbNullException("UpdateFields");
		return m_row.field_UpdateFields;
	}
	void SetUpdateFields(const std::string& st) {
		m_row.field_UpdateFields = st;
		m_row.IsUpdateFieldsNull = false;
		m_row.IsUpdateFieldsDirty = true;
	}
	bool IsUpdateFieldsNull() const {
		return m_row.IsUpdateFieldsNull;
	}
	void SetUpdateFieldsToNull() {
		m_row.field_UpdateFields = "";
		m_row.IsUpdateFieldsNull = true;
		m_row.IsUpdateFieldsDirty = true;
	}

	std::string GetSource_SQL() {
		if( m_row.IsSource_SQLNull )
			throw konga::DbNullException("Source_SQL");
		return m_row.field_Source_SQL;
	}
	void SetSource_SQL(const std::string& st) {
		m_row.field_Source_SQL = st;
		m_row.IsSource_SQLNull = false;
		m_row.IsSource_SQLDirty = true;
	}
	bool IsSource_SQLNull() const {
		return m_row.IsSource_SQLNull;
	}
	void SetSource_SQLToNull() {
		m_row.field_Source_SQL = "";
		m_row.IsSource_SQLNull = true;
		m_row.IsSource_SQLDirty = true;
	}

	std::string GetTarget_SQL() {
		if( m_row.IsTarget_SQLNull )
			throw konga::DbNullException("Target_SQL");
		return m_row.field_Target_SQL;
	}
	void SetTarget_SQL(const std::string& st) {
		m_row.field_Target_SQL = st;
		m_row.IsTarget_SQLNull = false;
		m_row.IsTarget_SQLDirty = true;
	}
	bool IsTarget_SQLNull() const {
		return m_row.IsTarget_SQLNull;
	}
	void SetTarget_SQLToNull() {
		m_row.field_Target_SQL = "";
		m_row.IsTarget_SQLNull = true;
		m_row.IsTarget_SQLDirty = true;
	}

	std::string GetNatureOfSource() {
		if( m_row.IsNatureOfSourceNull )
			throw konga::DbNullException("NatureOfSource");
		return m_row.field_NatureOfSource;
	}
	void SetNatureOfSource(const std::string& st) {
		m_row.field_NatureOfSource = st;
		m_row.IsNatureOfSourceNull = false;
		m_row.IsNatureOfSourceDirty = true;
	}
	bool IsNatureOfSourceNull() const {
		return m_row.IsNatureOfSourceNull;
	}
	void SetNatureOfSourceToNull() {
		m_row.field_NatureOfSource = "";
		m_row.IsNatureOfSourceNull = true;
		m_row.IsNatureOfSourceDirty = true;
	}

	std::string GetNatureOfTarget() {
		if( m_row.IsNatureOfTargetNull )
			throw konga::DbNullException("NatureOfTarget");
		return m_row.field_NatureOfTarget;
	}
	void SetNatureOfTarget(const std::string& st) {
		m_row.field_NatureOfTarget = st;
		m_row.IsNatureOfTargetNull = false;
		m_row.IsNatureOfTargetDirty = true;
	}
	bool IsNatureOfTargetNull() const {
		return m_row.IsNatureOfTargetNull;
	}
	void SetNatureOfTargetToNull() {
		m_row.field_NatureOfTarget = "";
		m_row.IsNatureOfTargetNull = true;
		m_row.IsNatureOfTargetDirty = true;
	}

	std::string GetSource_Where() {
		if( m_row.IsSource_WhereNull )
			throw konga::DbNullException("Source_Where");
		return m_row.field_Source_Where;
	}
	void SetSource_Where(const std::string& st) {
		m_row.field_Source_Where = st;
		m_row.IsSource_WhereNull = false;
		m_row.IsSource_WhereDirty = true;
	}
	bool IsSource_WhereNull() const {
		return m_row.IsSource_WhereNull;
	}
	void SetSource_WhereToNull() {
		m_row.field_Source_Where = "";
		m_row.IsSource_WhereNull = true;
		m_row.IsSource_WhereDirty = true;
	}

	int GetInsert_Mode() {
		if( m_row.IsInsert_ModeNull )
			throw konga::DbNullException("Insert_Mode");
		return m_row.field_Insert_Mode;
	}
	void SetInsert_Mode(int i) {
		m_row.field_Insert_Mode = i;
		m_row.IsInsert_ModeNull = false;
		m_row.IsInsert_ModeDirty = true;
	}
	bool IsInsert_ModeNull() const {
		return m_row.IsInsert_ModeNull;
	}
	void SetInsert_ModeToNull() {
		m_row.IsInsert_ModeNull = true;
		m_row.IsInsert_ModeDirty = true;
	}

	std::string GetTarget_DTD_URL() {
		if( m_row.IsTarget_DTD_URLNull )
			throw konga::DbNullException("Target_DTD_URL");
		return m_row.field_Target_DTD_URL;
	}
	void SetTarget_DTD_URL(const std::string& st) {
		m_row.field_Target_DTD_URL = st;
		m_row.IsTarget_DTD_URLNull = false;
		m_row.IsTarget_DTD_URLDirty = true;
	}
	bool IsTarget_DTD_URLNull() const {
		return m_row.IsTarget_DTD_URLNull;
	}
	void SetTarget_DTD_URLToNull() {
		m_row.field_Target_DTD_URL = "";
		m_row.IsTarget_DTD_URLNull = true;
		m_row.IsTarget_DTD_URLDirty = true;
	}

	int GetSource_EDI_Default() {
		if( m_row.IsSource_EDI_DefaultNull )
			throw konga::DbNullException("Source_EDI_Default");
		return m_row.field_Source_EDI_Default;
	}
	void SetSource_EDI_Default(int i) {
		m_row.field_Source_EDI_Default = i;
		m_row.IsSource_EDI_DefaultNull = false;
		m_row.IsSource_EDI_DefaultDirty = true;
	}
	bool IsSource_EDI_DefaultNull() const {
		return m_row.IsSource_EDI_DefaultNull;
	}
	void SetSource_EDI_DefaultToNull() {
		m_row.IsSource_EDI_DefaultNull = true;
		m_row.IsSource_EDI_DefaultDirty = true;
	}

	int GetTarget_EDI_Default() {
		if( m_row.IsTarget_EDI_DefaultNull )
			throw konga::DbNullException("Target_EDI_Default");
		return m_row.field_Target_EDI_Default;
	}
	void SetTarget_EDI_Default(int i) {
		m_row.field_Target_EDI_Default = i;
		m_row.IsTarget_EDI_DefaultNull = false;
		m_row.IsTarget_EDI_DefaultDirty = true;
	}
	bool IsTarget_EDI_DefaultNull() const {
		return m_row.IsTarget_EDI_DefaultNull;
	}
	void SetTarget_EDI_DefaultToNull() {
		m_row.IsTarget_EDI_DefaultNull = true;
		m_row.IsTarget_EDI_DefaultDirty = true;
	}

	std::string GetTarget_UpdateKeys() {
		if( m_row.IsTarget_UpdateKeysNull )
			throw konga::DbNullException("Target_UpdateKeys");
		return m_row.field_Target_UpdateKeys;
	}
	void SetTarget_UpdateKeys(const std::string& st) {
		m_row.field_Target_UpdateKeys = st;
		m_row.IsTarget_UpdateKeysNull = false;
		m_row.IsTarget_UpdateKeysDirty = true;
	}
	bool IsTarget_UpdateKeysNull() const {
		return m_row.IsTarget_UpdateKeysNull;
	}
	void SetTarget_UpdateKeysToNull() {
		m_row.field_Target_UpdateKeys = "";
		m_row.IsTarget_UpdateKeysNull = true;
		m_row.IsTarget_UpdateKeysDirty = true;
	}

	std::string GetSource_DiffKeys() {
		if( m_row.IsSource_DiffKeysNull )
			throw konga::DbNullException("Source_DiffKeys");
		return m_row.field_Source_DiffKeys;
	}
	void SetSource_DiffKeys(const std::string& st) {
		m_row.field_Source_DiffKeys = st;
		m_row.IsSource_DiffKeysNull = false;
		m_row.IsSource_DiffKeysDirty = true;
	}
	bool IsSource_DiffKeysNull() const {
		return m_row.IsSource_DiffKeysNull;
	}
	void SetSource_DiffKeysToNull() {
		m_row.field_Source_DiffKeys = "";
		m_row.IsSource_DiffKeysNull = true;
		m_row.IsSource_DiffKeysDirty = true;
	}

	int GetIsValid() {
		if( m_row.IsIsValidNull )
			throw konga::DbNullException("IsValid");
		return m_row.field_IsValid;
	}
	void SetIsValid(int i) {
		m_row.field_IsValid = i;
		m_row.IsIsValidNull = false;
		m_row.IsIsValidDirty = true;
	}
	bool IsIsValidNull() const {
		return m_row.IsIsValidNull;
	}
	void SetIsValidToNull() {
		m_row.IsIsValidNull = true;
		m_row.IsIsValidDirty = true;
	}

	std::string GetSourceDTD_Root() {
		if( m_row.IsSourceDTD_RootNull )
			throw konga::DbNullException("SourceDTD_Root");
		return m_row.field_SourceDTD_Root;
	}
	void SetSourceDTD_Root(const std::string& st) {
		m_row.field_SourceDTD_Root = st;
		m_row.IsSourceDTD_RootNull = false;
		m_row.IsSourceDTD_RootDirty = true;
	}
	bool IsSourceDTD_RootNull() const {
		return m_row.IsSourceDTD_RootNull;
	}
	void SetSourceDTD_RootToNull() {
		m_row.field_SourceDTD_Root = "";
		m_row.IsSourceDTD_RootNull = true;
		m_row.IsSourceDTD_RootDirty = true;
	}

	std::string GetTargetDTD_Root() {
		if( m_row.IsTargetDTD_RootNull )
			throw konga::DbNullException("TargetDTD_Root");
		return m_row.field_TargetDTD_Root;
	}
	void SetTargetDTD_Root(const std::string& st) {
		m_row.field_TargetDTD_Root = st;
		m_row.IsTargetDTD_RootNull = false;
		m_row.IsTargetDTD_RootDirty = true;
	}
	bool IsTargetDTD_RootNull() const {
		return m_row.IsTargetDTD_RootNull;
	}
	void SetTargetDTD_RootToNull() {
		m_row.field_TargetDTD_Root = "";
		m_row.IsTargetDTD_RootNull = true;
		m_row.IsTargetDTD_RootDirty = true;
	}

	int GetFunction_ID() {
		if( m_row.IsFunction_IDNull )
			throw konga::DbNullException("Function_ID");
		return m_row.field_Function_ID;
	}
	void SetFunction_ID(int i) {
		m_row.field_Function_ID = i;
		m_row.IsFunction_IDNull = false;
		m_row.IsFunction_IDDirty = true;
	}
	bool IsFunction_IDNull() const {
		return m_row.IsFunction_IDNull;
	}
	void SetFunction_IDToNull() {
		m_row.IsFunction_IDNull = true;
		m_row.IsFunction_IDDirty = true;
	}

	std::string GetInputXSDFileName() {
		if( m_row.IsInputXSDFileNameNull )
			throw konga::DbNullException("InputXSDFileName");
		return m_row.field_InputXSDFileName;
	}
	void SetInputXSDFileName(const std::string& st) {
		m_row.field_InputXSDFileName = st;
		m_row.IsInputXSDFileNameNull = false;
		m_row.IsInputXSDFileNameDirty = true;
	}
	bool IsInputXSDFileNameNull() const {
		return m_row.IsInputXSDFileNameNull;
	}
	void SetInputXSDFileNameToNull() {
		m_row.field_InputXSDFileName = "";
		m_row.IsInputXSDFileNameNull = true;
		m_row.IsInputXSDFileNameDirty = true;
	}

	std::string GetOutputXSDFileName() {
		if( m_row.IsOutputXSDFileNameNull )
			throw konga::DbNullException("OutputXSDFileName");
		return m_row.field_OutputXSDFileName;
	}
	void SetOutputXSDFileName(const std::string& st) {
		m_row.field_OutputXSDFileName = st;
		m_row.IsOutputXSDFileNameNull = false;
		m_row.IsOutputXSDFileNameDirty = true;
	}
	bool IsOutputXSDFileNameNull() const {
		return m_row.IsOutputXSDFileNameNull;
	}
	void SetOutputXSDFileNameToNull() {
		m_row.field_OutputXSDFileName = "";
		m_row.IsOutputXSDFileNameNull = true;
		m_row.IsOutputXSDFileNameDirty = true;
	}

	std::string GetFunctionType_Name() {
		if( m_row.IsFunctionType_NameNull )
			throw konga::DbNullException("FunctionType_Name");
		return m_row.field_FunctionType_Name;
	}
	void SetFunctionType_Name(const std::string& st) {
		m_row.field_FunctionType_Name = st;
		m_row.IsFunctionType_NameNull = false;
		m_row.IsFunctionType_NameDirty = true;
	}
	bool IsFunctionType_NameNull() const {
		return m_row.IsFunctionType_NameNull;
	}
	void SetFunctionType_NameToNull() {
		m_row.field_FunctionType_Name = "";
		m_row.IsFunctionType_NameNull = true;
		m_row.IsFunctionType_NameDirty = true;
	}

	int GetSourceJmsMessageId() {
		if( m_row.IsSourceJmsMessageIdNull )
			throw konga::DbNullException("SourceJmsMessageId");
		return m_row.field_SourceJmsMessageId;
	}
	void SetSourceJmsMessageId(int i) {
		m_row.field_SourceJmsMessageId = i;
		m_row.IsSourceJmsMessageIdNull = false;
		m_row.IsSourceJmsMessageIdDirty = true;
	}
	bool IsSourceJmsMessageIdNull() const {
		return m_row.IsSourceJmsMessageIdNull;
	}
	void SetSourceJmsMessageIdToNull() {
		m_row.IsSourceJmsMessageIdNull = true;
		m_row.IsSourceJmsMessageIdDirty = true;
	}

	int GetTargetJmsMessageId() {
		if( m_row.IsTargetJmsMessageIdNull )
			throw konga::DbNullException("TargetJmsMessageId");
		return m_row.field_TargetJmsMessageId;
	}
	void SetTargetJmsMessageId(int i) {
		m_row.field_TargetJmsMessageId = i;
		m_row.IsTargetJmsMessageIdNull = false;
		m_row.IsTargetJmsMessageIdDirty = true;
	}
	bool IsTargetJmsMessageIdNull() const {
		return m_row.IsTargetJmsMessageIdNull;
	}
	void SetTargetJmsMessageIdToNull() {
		m_row.IsTargetJmsMessageIdNull = true;
		m_row.IsTargetJmsMessageIdDirty = true;
	}

protected:
	TRANSFORMATIONSTAB_VRow m_row;

private:
	bool IsTransformation_IDDirty() const {
		return m_row.IsTransformation_IDDirty;
	};
	bool IsTransformation_NameDirty() const {
		return m_row.IsTransformation_NameDirty;
	};
	bool IsSource_Type_IDDirty() const {
		return m_row.IsSource_Type_IDDirty;
	};
	bool IsTarget_Type_IDDirty() const {
		return m_row.IsTarget_Type_IDDirty;
	};
	bool IsSource_Text_DocDirty() const {
		return m_row.IsSource_Text_DocDirty;
	};
	bool IsSource_EDIDirty() const {
		return m_row.IsSource_EDIDirty;
	};
	bool IsSource_EDI_VerDirty() const {
		return m_row.IsSource_EDI_VerDirty;
	};
	bool IsSource_SourceIDDirty() const {
		return m_row.IsSource_SourceIDDirty;
	};
	bool IsTarget_Text_DocDirty() const {
		return m_row.IsTarget_Text_DocDirty;
	};
	bool IsTarget_EDIDirty() const {
		return m_row.IsTarget_EDIDirty;
	};
	bool IsTarget_EDI_VerDirty() const {
		return m_row.IsTarget_EDI_VerDirty;
	};
	bool IsTarget_TargetIDDirty() const {
		return m_row.IsTarget_TargetIDDirty;
	};
	bool IsSource_XMLDirty() const {
		return m_row.IsSource_XMLDirty;
	};
	bool IsTarget_XMLDirty() const {
		return m_row.IsTarget_XMLDirty;
	};
	bool IsSource_DSNDirty() const {
		return m_row.IsSource_DSNDirty;
	};
	bool IsTarget_DSNDirty() const {
		return m_row.IsTarget_DSNDirty;
	};
	bool IsSource_TablesDirty() const {
		return m_row.IsSource_TablesDirty;
	};
	bool IsTarget_TablesDirty() const {
		return m_row.IsTarget_TablesDirty;
	};
	bool IsUpdateFieldsDirty() const {
		return m_row.IsUpdateFieldsDirty;
	};
	bool IsSource_SQLDirty() const {
		return m_row.IsSource_SQLDirty;
	};
	bool IsTarget_SQLDirty() const {
		return m_row.IsTarget_SQLDirty;
	};
	bool IsNatureOfSourceDirty() const {
		return m_row.IsNatureOfSourceDirty;
	};
	bool IsNatureOfTargetDirty() const {
		return m_row.IsNatureOfTargetDirty;
	};
	bool IsSource_WhereDirty() const {
		return m_row.IsSource_WhereDirty;
	};
	bool IsInsert_ModeDirty() const {
		return m_row.IsInsert_ModeDirty;
	};
	bool IsTarget_DTD_URLDirty() const {
		return m_row.IsTarget_DTD_URLDirty;
	};
	bool IsSource_EDI_DefaultDirty() const {
		return m_row.IsSource_EDI_DefaultDirty;
	};
	bool IsTarget_EDI_DefaultDirty() const {
		return m_row.IsTarget_EDI_DefaultDirty;
	};
	bool IsTarget_UpdateKeysDirty() const {
		return m_row.IsTarget_UpdateKeysDirty;
	};
	bool IsSource_DiffKeysDirty() const {
		return m_row.IsSource_DiffKeysDirty;
	};
	bool IsIsValidDirty() const {
		return m_row.IsIsValidDirty;
	};
	bool IsSourceDTD_RootDirty() const {
		return m_row.IsSourceDTD_RootDirty;
	};
	bool IsTargetDTD_RootDirty() const {
		return m_row.IsTargetDTD_RootDirty;
	};
	bool IsFunction_IDDirty() const {
		return m_row.IsFunction_IDDirty;
	};
	bool IsInputXSDFileNameDirty() const {
		return m_row.IsInputXSDFileNameDirty;
	};
	bool IsOutputXSDFileNameDirty() const {
		return m_row.IsOutputXSDFileNameDirty;
	};
	bool IsFunctionType_NameDirty() const {
		return m_row.IsFunctionType_NameDirty;
	};
	bool IsSourceJmsMessageIdDirty() const {
		return m_row.IsSourceJmsMessageIdDirty;
	};
	bool IsTargetJmsMessageIdDirty() const {
		return m_row.IsTargetJmsMessageIdDirty;
	};


	std::string m_stUpdateFilter;

};

}                         


#endif
