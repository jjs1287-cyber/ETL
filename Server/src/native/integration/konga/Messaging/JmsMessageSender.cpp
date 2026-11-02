/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include <string>

#include <konga/Utils/StringUtils.h>	           
#include <konga/Utils/FileUtils.h>	                      
#include <konga/Utils/CrException.h>
#include <konga/Utils/DateUtils.h>
#include <konga/Utils/Settings.h>					                 
#include <konga/Utils/util.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>

#include <konga/DataElement/DataElement.h>

#include <konga/Server/Server.h>	                              

#include <konga/Networking/BasicCommands.h>
#include <konga/JmsEngine/ComponentInfo.h>

#include "JmsMessage.h"
#include "MessageSender.h"

using namespace konga;
using std::string;

namespace {
	const string OUT_TAB_NAME = "OutgoingJmsMsgTab";
	const string OUT_TAB_ID_NAME = "JmsMsgInstanceId";
	const string OUTGOING_TAB_FIELDS = "(jmsmsginstanceid, sendingprogress, jmspropertiesid, jmspayloadpath)";

	const string PROP_TAB_NAME = "JmsPropertiesTab";
	const string PROP_TAB_ID_NAME = "JmsProperiesId";
	const string PROP_FIELDS = "(jmsproperiesid, jmspropertyid, jmspropertyname, jmspropertytype, jmspropertyvalue)";

	const string SEND_TAB_NAME = "JmsSendingPropertiesTab";
	const string SEND_TAB_FIELDS = "(jmsmsginstanceid, sendingpropertykey, sendingpropertyvalue)";
}

namespace {
	                                                  
	                                                               
	string GetAndCreateJmsPayloadPath() {
		string retVal(GetDataDirectoryWithSlash() + "outgoing");
		AppendPathSeparator(retVal);
		retVal += "JMS";
		AppendPathSeparator(retVal);
		const CrDateTime& now = (CrDateTime::GetCurrentTime)();
		retVal += toString(now.GetYear());
		AppendPathSeparator(retVal);
		retVal += toString(now.GetMonth());
		AppendPathSeparator(retVal);
		retVal += toString(now.GetDay());
		AppendPathSeparator(retVal);

		kCreateDirectory(retVal);

		retVal += GetGUIDString();
		retVal += ".payload";

		return retVal;
	}
}

void MessageSender::AddMessageToQueue(const JmsMessage& jmsMessage, std::string& sWarning) {
	sWarning.clear();
	try {
		jmsMessage.Validate();
	}
	catch( const CrException& ex ) {
		throw CrException("Failed to validate JMS message:\n" + ex.GetErrorString());
	}

	                                     
	string payloadPath;
	try {
		                                                                       
		                                                               
		const string& localPath = jmsMessage.GetLocalPayloadPath();
		if ( FileExists(localPath) ) {
			payloadPath = GetAndCreateJmsPayloadPath();
			kCopyFile(localPath, payloadPath, true);
		}
	}
	catch( const CrException& ex ) {
		throw CrException("Failed to copy local payload to the outgoing directory. Reason:\n"
			+ ex.GetErrorString());
	}

	DbConnection* pMsgDb = &this->GetDbConnection();

	                     
	AutoRollbackTransaction transaction(pMsgDb);

	                             
	const JmsMessage::DEVector& jmsProperties = jmsMessage.GetJmsProperties();
	long lJmsProperiesId = -1;
	if ( !jmsProperties.empty() ) {
		lJmsProperiesId = konga::GetNextId(PROP_TAB_NAME, PROP_TAB_ID_NAME, *pMsgDb);

		int lJmsPropertyId = 1;
		JmsMessage::DEVector::const_iterator cit = jmsProperties.begin();
		for(; cit != jmsProperties.end(); ++cit ) {
			const DataElement& de = *(*cit);
			int err = 0;
			const string value((const char*)de.GetString(err));
			if ( err ) continue;

			string sql = "INSERT INTO " + PROP_TAB_NAME + " " + PROP_FIELDS + " VALUES ("
				+ toString(lJmsProperiesId) + ", " + toString(lJmsPropertyId) + ", '"
				+ SqlEscape((string(de.getName()))) + "', '" + SqlEscape((string(de.getTypeStr()))) + "', '"
				+ SqlEscape(value) + "')";
			pMsgDb->ExecuteSql(sql);
			++lJmsPropertyId;
		}
	}

	                                   
	const long lJmsMsgInstanceId = GetNextId(OUT_TAB_NAME, OUT_TAB_ID_NAME, *pMsgDb);
	string sql = "INSERT INTO " + OUT_TAB_NAME + " " + OUTGOING_TAB_FIELDS + " VALUES("
		+ toString(lJmsMsgInstanceId) + ", 0, " + toString(lJmsProperiesId) + ", '"
		+ SqlEscape(payloadPath) + "')";
	pMsgDb->ExecuteSql(sql);

	                                 
	const JmsMessage::DEVector& jmsSendingProperties = jmsMessage.GetSendingProperties();
	if ( !jmsSendingProperties.empty() ) {
		JmsMessage::DEVector::const_iterator cit = jmsSendingProperties.begin();
		for(; cit != jmsSendingProperties.end(); ++cit ) {
			const DataElement& de = *(*cit);
			int err = 0;
			const string value((const char*)de.GetString(err));
			if ( err ) continue;

			sql = "INSERT INTO " + SEND_TAB_NAME + " " + SEND_TAB_FIELDS + " VALUES("
				+ toString(lJmsMsgInstanceId) + ", '" + SqlEscape(string(de.getName())) + "', '"
				+ SqlEscape(value) + "')";
			pMsgDb->ExecuteSql(sql);
		}
	}

	                      
	transaction.Commit();

	                                                 
	try {
		string stUserName, stPassword;
		GetInternalAdminCredentials(stUserName, stPassword);
		const long lPort = konga::GetOptionAsInt(stJMS_SERVICE_COMPONENT_NAME,
			"CommandPort", lJMS_SERVICE_DEFAULT_PORT);
		if ( lPort < 0 ) {
			throw CrException("Server Configuration Error: JMSEngine/CommandPort is negative");
		}

		networking::GenericCmd("CheckOutgoing", stUserName, stPassword, static_cast<unsigned long>(lPort), stJMS_SERVICE_COMPONENT_NAME);
	}
	catch( const CrException& ex ) {
		sWarning = "Failed to send signal to the ETLForge JMS Engine." \
			" This means that the JMS message may not be sent until the JMS Engine has been started. "
			+ ex.GetErrorString();
	}
}

