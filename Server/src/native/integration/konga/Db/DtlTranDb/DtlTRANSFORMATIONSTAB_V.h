/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_TRANSFORMATIONSTAB_V_H
#define KONGA_DTL_CRTRANDB_TRANSFORMATIONSTAB_V_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/TRANSFORMATIONSTAB_V.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::TRANSFORMATIONSTAB_VRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::TRANSFORMATIONSTAB_VRow &r)
	{
		r.IsTransformation_IDNull = cols["Transformation_ID"].IsNull();
		r.IsTransformation_NameNull = cols["Transformation_Name"].IsNull();
		r.IsSource_Type_IDNull = cols["Source_Type_ID"].IsNull();
		r.IsTarget_Type_IDNull = cols["Target_Type_ID"].IsNull();
		r.IsSource_Text_DocNull = cols["Source_Text_Doc"].IsNull();
		r.IsSource_EDINull = cols["Source_EDI"].IsNull();
		r.IsSource_EDI_VerNull = cols["Source_EDI_Ver"].IsNull();
		r.IsSource_SourceIDNull = cols["Source_SourceID"].IsNull();
		r.IsTarget_Text_DocNull = cols["Target_Text_Doc"].IsNull();
		r.IsTarget_EDINull = cols["Target_EDI"].IsNull();
		r.IsTarget_EDI_VerNull = cols["Target_EDI_Ver"].IsNull();
		r.IsTarget_TargetIDNull = cols["Target_TargetID"].IsNull();
		r.IsSource_XMLNull = cols["Source_XML"].IsNull();
		r.IsTarget_XMLNull = cols["Target_XML"].IsNull();
		r.IsSource_DSNNull = cols["Source_DSN"].IsNull();
		r.IsTarget_DSNNull = cols["Target_DSN"].IsNull();
		r.IsSource_TablesNull = cols["Source_Tables"].IsNull();
		r.IsTarget_TablesNull = cols["Target_Tables"].IsNull();
		r.IsUpdateFieldsNull = cols["UpdateFields"].IsNull();
		r.IsSource_SQLNull = cols["Source_SQL"].IsNull();
		r.IsTarget_SQLNull = cols["Target_SQL"].IsNull();
		r.IsNatureOfSourceNull = cols["NatureOfSource"].IsNull();
		r.IsNatureOfTargetNull = cols["NatureOfTarget"].IsNull();
		r.IsSource_WhereNull = cols["Source_Where"].IsNull();
		r.IsInsert_ModeNull = cols["Insert_Mode"].IsNull();
		r.IsTarget_DTD_URLNull = cols["Target_DTD_URL"].IsNull();
		r.IsSource_EDI_DefaultNull = cols["Source_EDI_Default"].IsNull();
		r.IsTarget_EDI_DefaultNull = cols["Target_EDI_Default"].IsNull();
		r.IsTarget_UpdateKeysNull = cols["Target_UpdateKeys"].IsNull();
		r.IsSource_DiffKeysNull = cols["Source_DiffKeys"].IsNull();
		r.IsIsValidNull = cols["IsValid"].IsNull();
		r.IsSourceDTD_RootNull = cols["SourceDTD_Root"].IsNull();
		r.IsTargetDTD_RootNull = cols["TargetDTD_Root"].IsNull();
		r.IsFunction_IDNull = cols["Function_ID"].IsNull();
		r.IsInputXSDFileNameNull = cols["InputXSDFileName"].IsNull();
		r.IsOutputXSDFileNameNull = cols["OutputXSDFileName"].IsNull();
		r.IsFunctionType_NameNull = cols["FunctionType_Name"].IsNull();
		r.IsSourceJmsMessageIdNull = cols["SourceJmsMessageId"].IsNull();
		r.IsTargetJmsMessageIdNull = cols["TargetJmsMessageId"].IsNull();

		return true;
	}
};

template<> class DefaultInsValidate<TranDb::TRANSFORMATIONSTAB_VRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::TRANSFORMATIONSTAB_VRow &r)
	{
		if( r.IsTransformation_IDNull )
			cols["Transformation_ID"].SetNull();
		else
			cols["Transformation_ID"].ClearNull();
		if( r.IsTransformation_NameNull )
			cols["Transformation_Name"].SetNull();
		else
			cols["Transformation_Name"].ClearNull();
		if( r.IsSource_Type_IDNull )
			cols["Source_Type_ID"].SetNull();
		else
			cols["Source_Type_ID"].ClearNull();
		if( r.IsTarget_Type_IDNull )
			cols["Target_Type_ID"].SetNull();
		else
			cols["Target_Type_ID"].ClearNull();
		if( r.IsSource_Text_DocNull )
			cols["Source_Text_Doc"].SetNull();
		else
			cols["Source_Text_Doc"].ClearNull();
		if( r.IsSource_EDINull )
			cols["Source_EDI"].SetNull();
		else
			cols["Source_EDI"].ClearNull();
		if( r.IsSource_EDI_VerNull )
			cols["Source_EDI_Ver"].SetNull();
		else
			cols["Source_EDI_Ver"].ClearNull();
		if( r.IsSource_SourceIDNull )
			cols["Source_SourceID"].SetNull();
		else
			cols["Source_SourceID"].ClearNull();
		if( r.IsTarget_Text_DocNull )
			cols["Target_Text_Doc"].SetNull();
		else
			cols["Target_Text_Doc"].ClearNull();
		if( r.IsTarget_EDINull )
			cols["Target_EDI"].SetNull();
		else
			cols["Target_EDI"].ClearNull();
		if( r.IsTarget_EDI_VerNull )
			cols["Target_EDI_Ver"].SetNull();
		else
			cols["Target_EDI_Ver"].ClearNull();
		if( r.IsTarget_TargetIDNull )
			cols["Target_TargetID"].SetNull();
		else
			cols["Target_TargetID"].ClearNull();
		if( r.IsSource_XMLNull )
			cols["Source_XML"].SetNull();
		else
			cols["Source_XML"].ClearNull();
		if( r.IsTarget_XMLNull )
			cols["Target_XML"].SetNull();
		else
			cols["Target_XML"].ClearNull();
		if( r.IsSource_DSNNull )
			cols["Source_DSN"].SetNull();
		else
			cols["Source_DSN"].ClearNull();
		if( r.IsTarget_DSNNull )
			cols["Target_DSN"].SetNull();
		else
			cols["Target_DSN"].ClearNull();
		if( r.IsSource_TablesNull )
			cols["Source_Tables"].SetNull();
		else
			cols["Source_Tables"].ClearNull();
		if( r.IsTarget_TablesNull )
			cols["Target_Tables"].SetNull();
		else
			cols["Target_Tables"].ClearNull();
		if( r.IsUpdateFieldsNull )
			cols["UpdateFields"].SetNull();
		else
			cols["UpdateFields"].ClearNull();
		if( r.IsSource_SQLNull )
			cols["Source_SQL"].SetNull();
		else
			cols["Source_SQL"].ClearNull();
		if( r.IsTarget_SQLNull )
			cols["Target_SQL"].SetNull();
		else
			cols["Target_SQL"].ClearNull();
		if( r.IsNatureOfSourceNull )
			cols["NatureOfSource"].SetNull();
		else
			cols["NatureOfSource"].ClearNull();
		if( r.IsNatureOfTargetNull )
			cols["NatureOfTarget"].SetNull();
		else
			cols["NatureOfTarget"].ClearNull();
		if( r.IsSource_WhereNull )
			cols["Source_Where"].SetNull();
		else
			cols["Source_Where"].ClearNull();
		if( r.IsInsert_ModeNull )
			cols["Insert_Mode"].SetNull();
		else
			cols["Insert_Mode"].ClearNull();
		if( r.IsTarget_DTD_URLNull )
			cols["Target_DTD_URL"].SetNull();
		else
			cols["Target_DTD_URL"].ClearNull();
		if( r.IsSource_EDI_DefaultNull )
			cols["Source_EDI_Default"].SetNull();
		else
			cols["Source_EDI_Default"].ClearNull();
		if( r.IsTarget_EDI_DefaultNull )
			cols["Target_EDI_Default"].SetNull();
		else
			cols["Target_EDI_Default"].ClearNull();
		if( r.IsTarget_UpdateKeysNull )
			cols["Target_UpdateKeys"].SetNull();
		else
			cols["Target_UpdateKeys"].ClearNull();
		if( r.IsSource_DiffKeysNull )
			cols["Source_DiffKeys"].SetNull();
		else
			cols["Source_DiffKeys"].ClearNull();
		if( r.IsIsValidNull )
			cols["IsValid"].SetNull();
		else
			cols["IsValid"].ClearNull();
		if( r.IsSourceDTD_RootNull )
			cols["SourceDTD_Root"].SetNull();
		else
			cols["SourceDTD_Root"].ClearNull();
		if( r.IsTargetDTD_RootNull )
			cols["TargetDTD_Root"].SetNull();
		else
			cols["TargetDTD_Root"].ClearNull();
		if( r.IsInputXSDFileNameNull )
			cols["InputXSDFileName"].SetNull();
		else
			cols["InputXSDFileName"].ClearNull();
		if( r.IsOutputXSDFileNameNull )
			cols["OutputXSDFileName"].SetNull();
		else
			cols["OutputXSDFileName"].ClearNull();
		if( r.IsSourceJmsMessageIdNull )
			cols["SourceJmsMessageId"].SetNull();
		else
			cols["SourceJmsMessageId"].ClearNull();
		if( r.IsTargetJmsMessageIdNull )
			cols["TargetJmsMessageId"].SetNull();
		else
			cols["TargetJmsMessageId"].ClearNull();

		return true;
	}
};

template<> class DefaultBCA<TranDb::TRANSFORMATIONSTAB_VRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::TRANSFORMATIONSTAB_VRow &rowbuf)
	{
		cols["Transformation_ID"] == rowbuf.field_Transformation_ID;
		cols["Transformation_Name"] == rowbuf.field_Transformation_Name;
		cols["Source_Type_ID"] == rowbuf.field_Source_Type_ID;
		cols["Target_Type_ID"] == rowbuf.field_Target_Type_ID;
		cols["Source_Text_Doc"] == rowbuf.field_Source_Text_Doc;
		cols["Source_EDI"] == rowbuf.field_Source_EDI;
		cols["Source_EDI_Ver"] == rowbuf.field_Source_EDI_Ver;
		cols["Source_SourceID"] == rowbuf.field_Source_SourceID;
		cols["Target_Text_Doc"] == rowbuf.field_Target_Text_Doc;
		cols["Target_EDI"] == rowbuf.field_Target_EDI;
		cols["Target_EDI_Ver"] == rowbuf.field_Target_EDI_Ver;
		cols["Target_TargetID"] == rowbuf.field_Target_TargetID;
		cols["Source_XML"] == rowbuf.field_Source_XML;
		cols["Target_XML"] == rowbuf.field_Target_XML;
		cols["Source_DSN"] == rowbuf.field_Source_DSN;
		cols["Target_DSN"] == rowbuf.field_Target_DSN;
		cols["Source_Tables"] == rowbuf.field_Source_Tables;
		cols["Target_Tables"] == rowbuf.field_Target_Tables;
		cols["UpdateFields"] == rowbuf.field_UpdateFields;
		cols["Source_SQL"] == rowbuf.field_Source_SQL;
		cols["Target_SQL"] == rowbuf.field_Target_SQL;
		cols["NatureOfSource"] == rowbuf.field_NatureOfSource;
		cols["NatureOfTarget"] == rowbuf.field_NatureOfTarget;
		cols["Source_Where"] == rowbuf.field_Source_Where;
		cols["Insert_Mode"] == rowbuf.field_Insert_Mode;
		cols["Target_DTD_URL"] == rowbuf.field_Target_DTD_URL;
		cols["Source_EDI_Default"] == rowbuf.field_Source_EDI_Default;
		cols["Target_EDI_Default"] == rowbuf.field_Target_EDI_Default;
		cols["Target_UpdateKeys"] == rowbuf.field_Target_UpdateKeys;
		cols["Source_DiffKeys"] == rowbuf.field_Source_DiffKeys;
		cols["IsValid"] == rowbuf.field_IsValid;
		cols["SourceDTD_Root"] == rowbuf.field_SourceDTD_Root;
		cols["TargetDTD_Root"] == rowbuf.field_TargetDTD_Root;
		cols["Function_ID"] == rowbuf.field_Function_ID;
		cols["InputXSDFileName"] == rowbuf.field_InputXSDFileName;
		cols["OutputXSDFileName"] == rowbuf.field_OutputXSDFileName;
		cols["FunctionType_Name"] == rowbuf.field_FunctionType_Name;
		cols["SourceJmsMessageId"] == rowbuf.field_SourceJmsMessageId;
		cols["TargetJmsMessageId"] == rowbuf.field_TargetJmsMessageId;
	}
};


}                     



namespace dtl_trandb {


class DtlTRANSFORMATIONSTAB_VInsertBinder : public CustomBinder<TranDb::TRANSFORMATIONSTAB_VRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::TRANSFORMATIONSTAB_VRow& rowbuf = *m_pRow;
		cols["Transformation_ID"] << rowbuf.field_Transformation_ID;
		cols["Transformation_Name"] << rowbuf.field_Transformation_Name;
		cols["Source_Type_ID"] << rowbuf.field_Source_Type_ID;
		cols["Target_Type_ID"] << rowbuf.field_Target_Type_ID;
		cols["Source_Text_Doc"] << rowbuf.field_Source_Text_Doc;
		cols["Source_EDI"] << rowbuf.field_Source_EDI;
		cols["Source_EDI_Ver"] << rowbuf.field_Source_EDI_Ver;
		cols["Source_SourceID"] << rowbuf.field_Source_SourceID;
		cols["Target_Text_Doc"] << rowbuf.field_Target_Text_Doc;
		cols["Target_EDI"] << rowbuf.field_Target_EDI;
		cols["Target_EDI_Ver"] << rowbuf.field_Target_EDI_Ver;
		cols["Target_TargetID"] << rowbuf.field_Target_TargetID;
		cols["Source_XML"] << rowbuf.field_Source_XML;
		cols["Target_XML"] << rowbuf.field_Target_XML;
		cols["Source_DSN"] << rowbuf.field_Source_DSN;
		cols["Target_DSN"] << rowbuf.field_Target_DSN;
		cols["Source_Tables"] << rowbuf.field_Source_Tables;
		cols["Target_Tables"] << rowbuf.field_Target_Tables;
		cols["UpdateFields"] << rowbuf.field_UpdateFields;
		cols["Source_SQL"] << rowbuf.field_Source_SQL;
		cols["Target_SQL"] << rowbuf.field_Target_SQL;
		cols["NatureOfSource"] << rowbuf.field_NatureOfSource;
		cols["NatureOfTarget"] << rowbuf.field_NatureOfTarget;
		cols["Source_Where"] << rowbuf.field_Source_Where;
		cols["Insert_Mode"] << rowbuf.field_Insert_Mode;
		cols["Target_DTD_URL"] << rowbuf.field_Target_DTD_URL;
		cols["Source_EDI_Default"] << rowbuf.field_Source_EDI_Default;
		cols["Target_EDI_Default"] << rowbuf.field_Target_EDI_Default;
		cols["Target_UpdateKeys"] << rowbuf.field_Target_UpdateKeys;
		cols["Source_DiffKeys"] << rowbuf.field_Source_DiffKeys;
		cols["IsValid"] << rowbuf.field_IsValid;
		cols["SourceDTD_Root"] << rowbuf.field_SourceDTD_Root;
		cols["TargetDTD_Root"] << rowbuf.field_TargetDTD_Root;
		cols["InputXSDFileName"] << rowbuf.field_InputXSDFileName;
		cols["OutputXSDFileName"] << rowbuf.field_OutputXSDFileName;
		cols["FunctionType_Name"] << rowbuf.field_FunctionType_Name;
		cols["SourceJmsMessageId"] << rowbuf.field_SourceJmsMessageId;
		cols["TargetJmsMessageId"] << rowbuf.field_TargetJmsMessageId;

	}
};

class DtlTRANSFORMATIONSTAB_VDeleteBinder : public CustomBinder<TranDb::TRANSFORMATIONSTAB_VRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::TRANSFORMATIONSTAB_VRow& rowbuf = *m_pRow;
		cols["Transformation_ID"] == rowbuf.field_Transformation_ID;
		cols["Transformation_Name"] == rowbuf.field_Transformation_Name;
		cols["Source_Type_ID"] == rowbuf.field_Source_Type_ID;
		cols["Target_Type_ID"] == rowbuf.field_Target_Type_ID;
		cols["Source_Text_Doc"] == rowbuf.field_Source_Text_Doc;
		cols["Source_EDI"] == rowbuf.field_Source_EDI;
		cols["Source_EDI_Ver"] == rowbuf.field_Source_EDI_Ver;
		cols["Source_SourceID"] == rowbuf.field_Source_SourceID;
		cols["Target_Text_Doc"] == rowbuf.field_Target_Text_Doc;
		cols["Target_EDI"] == rowbuf.field_Target_EDI;
		cols["Target_EDI_Ver"] == rowbuf.field_Target_EDI_Ver;
		cols["Target_TargetID"] == rowbuf.field_Target_TargetID;
		cols["Source_XML"] == rowbuf.field_Source_XML;
		cols["Target_XML"] == rowbuf.field_Target_XML;
		cols["Source_DSN"] == rowbuf.field_Source_DSN;
		cols["Target_DSN"] == rowbuf.field_Target_DSN;
		cols["Source_Tables"] == rowbuf.field_Source_Tables;
		cols["Target_Tables"] == rowbuf.field_Target_Tables;
		cols["UpdateFields"] == rowbuf.field_UpdateFields;
		cols["Source_SQL"] == rowbuf.field_Source_SQL;
		cols["Target_SQL"] == rowbuf.field_Target_SQL;
		cols["NatureOfSource"] == rowbuf.field_NatureOfSource;
		cols["NatureOfTarget"] == rowbuf.field_NatureOfTarget;
		cols["Source_Where"] == rowbuf.field_Source_Where;
		cols["Insert_Mode"] == rowbuf.field_Insert_Mode;
		cols["Target_DTD_URL"] == rowbuf.field_Target_DTD_URL;
		cols["Source_EDI_Default"] == rowbuf.field_Source_EDI_Default;
		cols["Target_EDI_Default"] == rowbuf.field_Target_EDI_Default;
		cols["Target_UpdateKeys"] == rowbuf.field_Target_UpdateKeys;
		cols["Source_DiffKeys"] == rowbuf.field_Source_DiffKeys;
		cols["IsValid"] == rowbuf.field_IsValid;
		cols["SourceDTD_Root"] == rowbuf.field_SourceDTD_Root;
		cols["TargetDTD_Root"] == rowbuf.field_TargetDTD_Root;
		cols["Function_ID"] == rowbuf.field_Function_ID;
		cols["InputXSDFileName"] == rowbuf.field_InputXSDFileName;
		cols["OutputXSDFileName"] == rowbuf.field_OutputXSDFileName;
		cols["FunctionType_Name"] == rowbuf.field_FunctionType_Name;
		cols["SourceJmsMessageId"] == rowbuf.field_SourceJmsMessageId;
		cols["TargetJmsMessageId"] == rowbuf.field_TargetJmsMessageId;
	}
};

class DtlTRANSFORMATIONSTAB_V : public TranDb::TRANSFORMATIONSTAB_V {

public:
	DtlTRANSFORMATIONSTAB_V(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlTRANSFORMATIONSTAB_V() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::TRANSFORMATIONSTAB_V::Edit();
		m_dtlRecSet.Edit();
	}
	void AddNew() {
		m_dtlRecSet.AddNew(&m_insertBinder);
	}
	int Delete() {
		return m_dtlRecSet.Delete();
	}
	bool IsEOF() {
		return m_dtlRecSet.IsEOF();
	}
	bool Next() {
		return m_dtlRecSet.Next();
	}
	void Close() {
		m_dtlRecSet.Close();
	}
	void SetWhereClause(const std::string& stWhereClause) {
		m_dtlRecSet.SetWhereClause(stWhereClause);
	}
	std::string GetWhereClause() const {
		return m_dtlRecSet.GetWhereClause();
	}

	std::string GetLastError() const {
		return m_dtlRecSet.GetLastError();
	}
	long GetLastErrorNumber() const {
		return m_dtlRecSet.GetLastErrorNumber();
	}

private:
	DtlTRANSFORMATIONSTAB_VInsertBinder m_insertBinder;
	DtlTRANSFORMATIONSTAB_VDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::TRANSFORMATIONSTAB_V, TranDb::TRANSFORMATIONSTAB_VRow> m_dtlRecSet;

};


}                            


#endif

