/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <time.h>	                

#include <memory>	                

#include <konga/Utils/StringUtils.h>	           
#include <konga/Utils/CrException.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>

#include <konga/Db/MsgDb/DataElementTab.h>
#include <konga/Db/MsgDb/DataElementTextTab.h>
#include <konga/Db/MsgDb/SpInsertIncomingMsgTab.h>

#include "ProcessMessage.h"
#include "MessageSender.h"

using namespace konga;
using std::string;
using std::vector;

MessageSender::MessageSender(DbConnection* pMsgDbConnection) :
DbConnectionHolder(pMsgDbConnection), m_bShouldValidate(true) {
	this->SetInternalDbName("MsgDb");
}

MessageSender::~MessageSender() {}

namespace {
	const std::string::size_type MAX_SUBJ_LENGTH = 255;
}

int MessageSender::AddMessageToQueue(const ProcessMessage& processMessage, bool bCommitTransaction) {
	try {
		Validate(processMessage);
	}
	catch( const CrException& ex ) {
		throw CrException("Failed to validate process message:\n" + ex.GetErrorString());
	}

	DbConnection* pMsgDb = &this->GetDbConnection();
	konga::SetClientEncoding(*pMsgDb);

	                                  
	std::auto_ptr<AutoRollbackTransaction> pTransaction(0);
	if ( bCommitTransaction ) {
		pTransaction.reset( new AutoRollbackTransaction(pMsgDb) );
	}

	SmartDbPtr<MsgDb::SpInsertIncomingMsgTab> pSpInsertIncomingMsgTab( MsgDb::SpInsertIncomingMsgTab::MakeInstance(pMsgDb) );

	const string& sFrom = processMessage.GetFrom();
	if( sFrom.empty() ) pSpInsertIncomingMsgTab->SetMsgFromToNull();
	else pSpInsertIncomingMsgTab->SetMsgFrom(sFrom);

	const string& sTo = processMessage.GetTo();
	if( sTo.empty() ) pSpInsertIncomingMsgTab->SetMsgToToNull();
	else pSpInsertIncomingMsgTab->SetMsgTo(sTo);

	const string& sOwner = processMessage.GetOwner();
	if( sOwner.empty() ) pSpInsertIncomingMsgTab->SetOwnerToNull();
	else pSpInsertIncomingMsgTab->SetOwner(sOwner);

	const string& sSubject = processMessage.GetSubject();
	if( sSubject.empty() )
		pSpInsertIncomingMsgTab->SetMsgSubjectToNull();
	else {
		if( sSubject.size() < MAX_SUBJ_LENGTH )
			pSpInsertIncomingMsgTab->SetMsgSubject(sSubject);
		else {
			pSpInsertIncomingMsgTab->SetMsgSubject(sSubject.substr(0, MAX_SUBJ_LENGTH));
		}
	}

	const string& sProcessVariables = processMessage.GetProcessVariables();
	if( sProcessVariables.empty() )
		pSpInsertIncomingMsgTab->SetProcessVariables("{" + processMessage.GetProcessName() + ";0;" + processMessage.GetTaskName() + ";0;}");
	else
		pSpInsertIncomingMsgTab->SetProcessVariables(sProcessVariables);

	time_t t;
	pSpInsertIncomingMsgTab->SetReceivedAt( static_cast<int>(time(&t)) );

	const ProcessMessage::DEVector& vDataElements = processMessage.GetDataElements();
	pSpInsertIncomingMsgTab->SetIsExecuting( vDataElements.empty() ? 0 : -1);
	pSpInsertIncomingMsgTab->SetMsgPriority(static_cast<int>(processMessage.GetPriority()));
	pSpInsertIncomingMsgTab->SetNumThreads(-1);

	try
	{
		pSpInsertIncomingMsgTab->Execute();
	}
	catch( const konga::DbException& e ) {
		string stErr("Failed to add message with subject '" + processMessage.GetSubject() + "' to the queue.\n");
		stErr += "Database error in Calling SpInsertIncomingMsgTab. Error:\n" +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "].";
		throw CrException(stErr, e.GetErrorNumber());
	}

	const int iMsgId = pSpInsertIncomingMsgTab->GetMsgId();
	pSpInsertIncomingMsgTab->Close();

	if( vDataElements.empty() ) {
		if ( bCommitTransaction ) pTransaction->Commit();
		return iMsgId;
	}

	SmartDbPtr<MsgDb::DataElementTab> pDataElementTab( MsgDb::DataElementTab::MakeInstance(pMsgDb) );
	SmartDbPtr<MsgDb::DataElementTextTab> pDataElementTextTab( MsgDb::DataElementTextTab::MakeInstance(pMsgDb) );

	try
	{
		ProcessMessage::DEVector::const_iterator deIt = vDataElements.begin();
		for(; deIt != vDataElements.end(); ++deIt) {
			if( deIt->type == DET_TEXT ) {
				pDataElementTextTab->AddNew();
				pDataElementTextTab->SetMsgId(iMsgId);
				pDataElementTextTab->SetDataElementName(deIt->stName);
				pDataElementTextTab->SetStringValue(deIt->stValue);
				pDataElementTextTab->Update();
				continue;
			}
			pDataElementTab->AddNew();
			pDataElementTab->SetMsgId(iMsgId);
			pDataElementTab->SetDataType( deIt->DataTypeAsString() );
			pDataElementTab->SetDataElementName(deIt->stName);
			switch(deIt->type) {
				case DET_BOOL:
				case DET_INT:
				case DET_LONG:
					pDataElementTab->SetIntValue( atoi(deIt->stValue.c_str()) );
					break;
				case DET_FLOAT:
				case DET_DOUBLE:
					pDataElementTab->SetDoubleValue( atof(deIt->stValue.c_str()) );
					break;
				case DET_STRING:
					pDataElementTab->SetStringValue( deIt->stValue );
					break;
				default:
					break;
			}
			pDataElementTab->Update();
		}

		                                                                
		string stSql = "UPDATE IncomingMsgTab SET IsExecuting = 0 WHERE MsgId = " + toString(iMsgId);
		pMsgDb->ExecuteSql(stSql);
	}
	catch(konga::DbException& e)
	{
		string stErr = "Database error: AddMessageToQueue: adding data elements -- Error: " +
			e.GetErrorString() + " [" + e.GetStateNativeOrigin() + "].";
		throw CrException(stErr, e.GetErrorNumber());
	}
	if ( bCommitTransaction ) pTransaction->Commit();

	return iMsgId;
}

void MessageSender::EnableValidation(bool bEnable) { m_bShouldValidate = bEnable; }

void MessageSender::Validate(const ProcessMessage& message) {
	if ( !m_bShouldValidate )
		return;

	message.Validate();

	                                                                                                     
	                                                                                      
}
