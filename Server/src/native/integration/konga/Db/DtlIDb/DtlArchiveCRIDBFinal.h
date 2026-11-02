/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DTL_CRIDB_ARCHIVECRIDBFINAL_H
#define KONGA_DTL_CRIDB_ARCHIVECRIDBFINAL_H

#include <string>

#include <konga/Db/DtlDbUtils/DtlDbRecordSet.h>

#include <konga/Db/IDb/ArchiveCRIDBFinal.h>

namespace dtl {

template<> class DefaultSelValidate<IDb::ArchiveCRIDBFinalRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::ArchiveCRIDBFinalRow &r)
	{
		r.IsStringValueNull = cols["StringValue"].IsNull();
		r.IsDoubleValueNull = cols["DoubleValue"].IsNull();
		r.IsIntValueNull = cols["IntValue"].IsNull();
		r.IsDataElementNameNull = cols["DataElementName"].IsNull();
		r.IsDataTypeNull = cols["DataType"].IsNull();
		r.IsTaskUserNull = cols["TaskUser"].IsNull();
		r.IsDataElementValueListNull = cols["DataElementValueList"].IsNull();
		r.IsFromDocDataElementValueListNull = cols["FromDocDataElementValueList"].IsNull();
		r.IsRouteUserNull = cols["RouteUser"].IsNull();
		r.IsMsgPriorityNull = cols["MsgPriority"].IsNull();
		r.IsInitialTaskNull = cols["InitialTask"].IsNull();
		r.IsUpdateFlagNull = cols["UpdateFlag"].IsNull();
		r.IsProcessDataElementValueListNull = cols["ProcessDataElementValueList"].IsNull();
		r.IsInsertTimeNull = cols["InsertTime"].IsNull();
		r.IsModTimeNull = cols["ModTime"].IsNull();
		r.IsRouteInstanceIdNull = cols["RouteInstanceId"].IsNull();
		r.IsSentTimeNull = cols["SentTime"].IsNull();
		r.IsStatusTimeNull = cols["StatusTime"].IsNull();
		r.IsStatusNull = cols["Status"].IsNull();
		r.IsEndTimeNull = cols["EndTime"].IsNull();
		r.IsStartTimeNull = cols["StartTime"].IsNull();
		r.IsProcessInstanceIdNull = cols["ProcessInstanceId"].IsNull();
		r.IsMsgFromNull = cols["MsgFrom"].IsNull();
		r.IsMsgToNull = cols["MsgTo"].IsNull();
		r.IsMsgSubjectNull = cols["MsgSubject"].IsNull();
		r.IsMessageFileNameNull = cols["MessageFileName"].IsNull();
		r.IsFormatNull = cols["Format"].IsNull();
		r.IsSentAtNull = cols["SentAt"].IsNull();
		r.IsURLNull = cols["URL"].IsNull();
		r.IsMsgCCNull = cols["MsgCC"].IsNull();
		r.IsMsgBCCNull = cols["MsgBCC"].IsNull();
		r.IsMhtmlUrlNull = cols["MhtmlUrl"].IsNull();
		r.IsMsgReplyToNull = cols["MsgReplyTo"].IsNull();
		r.IsOwnerNull = cols["Owner"].IsNull();
		r.IsMimeContentTypeNull = cols["MimeContentType"].IsNull();
		r.IsFileNameNull = cols["FileName"].IsNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultInsValidate<IDb::ArchiveCRIDBFinalRow>
{
public:
	bool operator()(BoundIOs &cols, IDb::ArchiveCRIDBFinalRow &r)
	{
		if( r.IsStringValueNull )
			cols["StringValue"].SetNull();
		else
			cols["StringValue"].ClearNull();
		if( r.IsDoubleValueNull )
			cols["DoubleValue"].SetNull();
		else
			cols["DoubleValue"].ClearNull();
		if( r.IsIntValueNull )
			cols["IntValue"].SetNull();
		else
			cols["IntValue"].ClearNull();
		if( r.IsDataElementNameNull )
			cols["DataElementName"].SetNull();
		else
			cols["DataElementName"].ClearNull();
		if( r.IsDataTypeNull )
			cols["DataType"].SetNull();
		else
			cols["DataType"].ClearNull();
		if( r.IsTaskUserNull )
			cols["TaskUser"].SetNull();
		else
			cols["TaskUser"].ClearNull();
		if( r.IsDataElementValueListNull )
			cols["DataElementValueList"].SetNull();
		else
			cols["DataElementValueList"].ClearNull();
		if( r.IsFromDocDataElementValueListNull )
			cols["FromDocDataElementValueList"].SetNull();
		else
			cols["FromDocDataElementValueList"].ClearNull();
		if( r.IsRouteUserNull )
			cols["RouteUser"].SetNull();
		else
			cols["RouteUser"].ClearNull();
		if( r.IsMsgPriorityNull )
			cols["MsgPriority"].SetNull();
		else
			cols["MsgPriority"].ClearNull();
		if( r.IsInitialTaskNull )
			cols["InitialTask"].SetNull();
		else
			cols["InitialTask"].ClearNull();
		if( r.IsUpdateFlagNull )
			cols["UpdateFlag"].SetNull();
		else
			cols["UpdateFlag"].ClearNull();
		if( r.IsProcessDataElementValueListNull )
			cols["ProcessDataElementValueList"].SetNull();
		else
			cols["ProcessDataElementValueList"].ClearNull();
		if( r.IsInsertTimeNull )
			cols["InsertTime"].SetNull();
		else
			cols["InsertTime"].ClearNull();
		if( r.IsModTimeNull )
			cols["ModTime"].SetNull();
		else
			cols["ModTime"].ClearNull();
		if( r.IsRouteInstanceIdNull )
			cols["RouteInstanceId"].SetNull();
		else
			cols["RouteInstanceId"].ClearNull();
		if( r.IsSentTimeNull )
			cols["SentTime"].SetNull();
		else
			cols["SentTime"].ClearNull();
		if( r.IsStatusTimeNull )
			cols["StatusTime"].SetNull();
		else
			cols["StatusTime"].ClearNull();
		if( r.IsStatusNull )
			cols["Status"].SetNull();
		else
			cols["Status"].ClearNull();
		if( r.IsEndTimeNull )
			cols["EndTime"].SetNull();
		else
			cols["EndTime"].ClearNull();
		if( r.IsStartTimeNull )
			cols["StartTime"].SetNull();
		else
			cols["StartTime"].ClearNull();
		if( r.IsProcessInstanceIdNull )
			cols["ProcessInstanceId"].SetNull();
		else
			cols["ProcessInstanceId"].ClearNull();
		if( r.IsMsgFromNull )
			cols["MsgFrom"].SetNull();
		else
			cols["MsgFrom"].ClearNull();
		if( r.IsMsgToNull )
			cols["MsgTo"].SetNull();
		else
			cols["MsgTo"].ClearNull();
		if( r.IsMsgSubjectNull )
			cols["MsgSubject"].SetNull();
		else
			cols["MsgSubject"].ClearNull();
		if( r.IsMessageFileNameNull )
			cols["MessageFileName"].SetNull();
		else
			cols["MessageFileName"].ClearNull();
		if( r.IsFormatNull )
			cols["Format"].SetNull();
		else
			cols["Format"].ClearNull();
		if( r.IsSentAtNull )
			cols["SentAt"].SetNull();
		else
			cols["SentAt"].ClearNull();
		if( r.IsURLNull )
			cols["URL"].SetNull();
		else
			cols["URL"].ClearNull();
		if( r.IsMsgCCNull )
			cols["MsgCC"].SetNull();
		else
			cols["MsgCC"].ClearNull();
		if( r.IsMsgBCCNull )
			cols["MsgBCC"].SetNull();
		else
			cols["MsgBCC"].ClearNull();
		if( r.IsMhtmlUrlNull )
			cols["MhtmlUrl"].SetNull();
		else
			cols["MhtmlUrl"].ClearNull();
		if( r.IsMsgReplyToNull )
			cols["MsgReplyTo"].SetNull();
		else
			cols["MsgReplyTo"].ClearNull();
		if( r.IsOwnerNull )
			cols["Owner"].SetNull();
		else
			cols["Owner"].ClearNull();
		if( r.IsMimeContentTypeNull )
			cols["MimeContentType"].SetNull();
		else
			cols["MimeContentType"].ClearNull();
		if( r.IsFileNameNull )
			cols["FileName"].SetNull();
		else
			cols["FileName"].ClearNull();
		  
                                        
                       
    
		return true;
	}
};

template<> class DefaultBCA<IDb::ArchiveCRIDBFinalRow>
{
public:
	void operator()(BoundIOs &cols, IDb::ArchiveCRIDBFinalRow &rowbuf)
	{
		cols["StringValue"] == rowbuf.field_StringValue;
		cols["DoubleValue"] == rowbuf.field_DoubleValue;
		cols["IntValue"] == rowbuf.field_IntValue;
		cols["DataElementName"] == rowbuf.field_DataElementName;
		cols["DataType"] == rowbuf.field_DataType;
		cols["TaskUser"] == rowbuf.field_TaskUser;
		cols["DataElementValueList"] == rowbuf.field_DataElementValueList;
		cols["FromDocDataElementValueList"] == rowbuf.field_FromDocDataElementValueList;
		cols["RouteUser"] == rowbuf.field_RouteUser;
		cols["MsgPriority"] == rowbuf.field_MsgPriority;
		cols["InitialTask"] == rowbuf.field_InitialTask;
		cols["UpdateFlag"] == rowbuf.field_UpdateFlag;
		cols["ProcessDataElementValueList"] == rowbuf.field_ProcessDataElementValueList;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		cols["RouteInstanceId"] == rowbuf.field_RouteInstanceId;
		cols["SentTime"] == rowbuf.field_SentTime;
		cols["StatusTime"] == rowbuf.field_StatusTime;
		cols["Status"] == rowbuf.field_Status;
		cols["EndTime"] == rowbuf.field_EndTime;
		cols["StartTime"] == rowbuf.field_StartTime;
		cols["ProcessInstanceId"] == rowbuf.field_ProcessInstanceId;
		cols["MsgFrom"] == rowbuf.field_MsgFrom;
		cols["MsgTo"] == rowbuf.field_MsgTo;
		cols["MsgSubject"] == rowbuf.field_MsgSubject;
		cols["MessageFileName"] == rowbuf.field_MessageFileName;
		cols["Format"] == rowbuf.field_Format;
		cols["SentAt"] == rowbuf.field_SentAt;
		cols["URL"] == rowbuf.field_URL;
		cols["MsgCC"] == rowbuf.field_MsgCC;
		cols["MsgBCC"] == rowbuf.field_MsgBCC;
		cols["MhtmlUrl"] == rowbuf.field_MhtmlUrl;
		cols["MsgReplyTo"] == rowbuf.field_MsgReplyTo;
		cols["Owner"] == rowbuf.field_Owner;
		cols["MimeContentType"] == rowbuf.field_MimeContentType;
		cols["FileName"] == rowbuf.field_FileName;
		  
                                        
                       
    
	}
};


}                     


class DeleteBinder : public CustomBinder<IDb::ArchiveCRIDBFinalRow>
{
public:
	void Bind()
	{
		dtl::BoundIOs& cols = *m_pBoundIOs;
		IDb::ArchiveCRIDBFinalRow& rowbuf = *m_pRow;
		cols["StringValue"] == rowbuf.field_StringValue;
		cols["DoubleValue"] == rowbuf.field_DoubleValue;
		cols["IntValue"] == rowbuf.field_IntValue;
		cols["DataElementName"] == rowbuf.field_DataElementName;
		cols["DataType"] == rowbuf.field_DataType;
		cols["TaskUser"] == rowbuf.field_TaskUser;
		cols["DataElementValueList"] == rowbuf.field_DataElementValueList;
		cols["FromDocDataElementValueList"] == rowbuf.field_FromDocDataElementValueList;
		cols["RouteUser"] == rowbuf.field_RouteUser;
		cols["MsgPriority"] == rowbuf.field_MsgPriority;
		cols["InitialTask"] == rowbuf.field_InitialTask;
		cols["UpdateFlag"] == rowbuf.field_UpdateFlag;
		cols["ProcessDataElementValueList"] == rowbuf.field_ProcessDataElementValueList;
		cols["InsertTime"] == rowbuf.field_InsertTime;
		cols["ModTime"] == rowbuf.field_ModTime;
		cols["RouteInstanceId"] == rowbuf.field_RouteInstanceId;
		cols["SentTime"] == rowbuf.field_SentTime;
		cols["StatusTime"] == rowbuf.field_StatusTime;
		cols["Status"] == rowbuf.field_Status;
		cols["EndTime"] == rowbuf.field_EndTime;
		cols["StartTime"] == rowbuf.field_StartTime;
		cols["ProcessInstanceId"] == rowbuf.field_ProcessInstanceId;
		cols["MsgFrom"] == rowbuf.field_MsgFrom;
		cols["MsgTo"] == rowbuf.field_MsgTo;
		cols["MsgSubject"] == rowbuf.field_MsgSubject;
		cols["MessageFileName"] == rowbuf.field_MessageFileName;
		cols["Format"] == rowbuf.field_Format;
		cols["SentAt"] == rowbuf.field_SentAt;
		cols["URL"] == rowbuf.field_URL;
		cols["MsgCC"] == rowbuf.field_MsgCC;
		cols["MsgBCC"] == rowbuf.field_MsgBCC;
		cols["MhtmlUrl"] == rowbuf.field_MhtmlUrl;
		cols["MsgReplyTo"] == rowbuf.field_MsgReplyTo;
		cols["Owner"] == rowbuf.field_Owner;
		cols["MimeContentType"] == rowbuf.field_MimeContentType;
		cols["FileName"] == rowbuf.field_FileName;
	}
};

class DtlArchiveCRIDBFinal : public IDb::ArchiveCRIDBFinal {

public:
	DtlArchiveCRIDBFinal(DBConnection* pConn) :
		m_dtlRecSet(pConn, GetTableName(), this, m_deleteBinder)
	{}
	virtual ~DtlArchiveCRIDBFinal() {}

	void Select(const std::string& stWhereClause = "") {
		 m_dtlRecSet.Select(stWhereClause);
	}
	int Update() {
		return m_dtlRecSet.Update(m_row);
	}
	void Edit() {
		IDb::ArchiveCRIDBFinal::Edit();
		m_dtlRecSet.Edit();
	}
	void AddNew() {
		m_dtlRecSet.AddNew();
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
	DeleteBinder m_deleteBinder;
	DtlDbRecordSet<IDb::ArchiveCRIDBFinal, IDb::ArchiveCRIDBFinalRow> m_dtlRecSet;

};

#endif

