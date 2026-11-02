/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRTRANDB_KEY_VALUES_H
#define KONGA_DTL_CRTRANDB_KEY_VALUES_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/TranDb/Key_Values.h>

namespace dtl {

template<> class DefaultSelValidate<TranDb::Key_ValuesRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::Key_ValuesRow &r)
	{
		r.IsKey_ValuesIDNull = cols["Key_ValuesID"].IsNull();
		r.IsKey_NameNull = cols["Key_Name"].IsNull();
		r.IsKey_ValueNull = cols["Key_Value"].IsNull();
		r.IsKey_TypeNull = cols["Key_Type"].IsNull();
		r.IsGroup_IdNull = cols["Group_Id"].IsNull();
		r.IsKey_Value_DescriptionNull = cols["Key_Value_Description"].IsNull();
		r.IsKey_Display_NameNull = cols["Key_Display_Name"].IsNull();
		r.IsSort_SequenceNull = cols["Sort_Sequence"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		r.IsLong_Key_ValueNull = cols["Long_Key_Value"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<TranDb::Key_ValuesRow>
{
public:
	bool operator()(BoundIOs &cols, TranDb::Key_ValuesRow &r)
	{
		if( r.IsKey_NameNull )
			cols["Key_Name"].SetNull();
		else
			cols["Key_Name"].ClearNull();
		if( r.IsKey_ValueNull )
			cols["Key_Value"].SetNull();
		else
			cols["Key_Value"].ClearNull();
		if( r.IsKey_TypeNull )
			cols["Key_Type"].SetNull();
		else
			cols["Key_Type"].ClearNull();
		if( r.IsGroup_IdNull )
			cols["Group_Id"].SetNull();
		else
			cols["Group_Id"].ClearNull();
		if( r.IsKey_Value_DescriptionNull )
			cols["Key_Value_Description"].SetNull();
		else
			cols["Key_Value_Description"].ClearNull();
		if( r.IsKey_Display_NameNull )
			cols["Key_Display_Name"].SetNull();
		else
			cols["Key_Display_Name"].ClearNull();
		if( r.IsSort_SequenceNull )
			cols["Sort_Sequence"].SetNull();
		else
			cols["Sort_Sequence"].ClearNull();
		if( r.IsInsertTimeNull )
			cols["InsertTime"].SetNull();
		else
			cols["InsertTime"].ClearNull();
		if( r.IsModTimeNull )
			cols["ModTime"].SetNull();
		else
			cols["ModTime"].ClearNull();
		if( r.IsLong_Key_ValueNull )
			cols["Long_Key_Value"].SetNull();
		else
			cols["Long_Key_Value"].ClearNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultBCA<TranDb::Key_ValuesRow>
{
public:
	void operator()(BoundIOs &cols, TranDb::Key_ValuesRow &rowbuf)
	{
		cols["Key_ValuesID"] == rowbuf.field_Key_ValuesID;
		cols["Key_Name"] == rowbuf.field_Key_Name;
		cols["Key_Value"] == rowbuf.field_Key_Value;
		cols["Key_Type"] == rowbuf.field_Key_Type;
		cols["Group_Id"] == rowbuf.field_Group_Id;
		cols["Key_Value_Description"] == rowbuf.field_Key_Value_Description;
		cols["Key_Display_Name"] == rowbuf.field_Key_Display_Name;
		cols["Sort_Sequence"] == rowbuf.field_Sort_Sequence;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		cols["Long_Key_Value"] == rowbuf.field_Long_Key_Value;
		  
                                        
                       
    
	}
};


}                     



namespace dtl_trandb {


class DtlKey_ValuesInsertBinder : public CustomBinder<TranDb::Key_ValuesRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::Key_ValuesRow& rowbuf = *m_pRow;
		cols["Key_Name"] << rowbuf.field_Key_Name;
		cols["Key_Value"] << rowbuf.field_Key_Value;
		cols["Key_Type"] << rowbuf.field_Key_Type;
		cols["Group_Id"] << rowbuf.field_Group_Id;
		cols["Key_Value_Description"] << rowbuf.field_Key_Value_Description;
		cols["Key_Display_Name"] << rowbuf.field_Key_Display_Name;
		cols["Sort_Sequence"] << rowbuf.field_Sort_Sequence;
		cols["InsertTime"] << rowbuf.field_InsertTime;
		cols["ModTime"] << rowbuf.field_ModTime;
		cols["Long_Key_Value"] << rowbuf.field_Long_Key_Value;
	}
};

class DtlKey_ValuesDeleteBinder : public CustomBinder<TranDb::Key_ValuesRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		TranDb::Key_ValuesRow& rowbuf = *m_pRow;
		cols["Key_ValuesID"] == rowbuf.field_Key_ValuesID;
	}
};

class DtlKey_Values : public TranDb::Key_Values {

public:
	DtlKey_Values(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlKey_Values() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		TranDb::Key_Values::Edit();
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
	DtlKey_ValuesInsertBinder m_insertBinder;
	DtlKey_ValuesDeleteBinder m_deleteBinder;
	DtlDbRecordSet<TranDb::Key_Values, TranDb::Key_ValuesRow> m_dtlRecSet;

};


}                            


#endif

