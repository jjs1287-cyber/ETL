/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <string.h>

#include <memory>	                
#include <vector>
#include <string>
#include <map>

#include <konga/Utils/PortDefs.h>		                   

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include <konga/DbRelations/EntityIdConstants.h>	                          

#include <konga/Messaging/MessageSender.h>
#include <konga/Messaging/JmsMessage.h>

#include <konga/DataElement/DataElement.h>

#include <konga/OperationContext/OperationDataElementHolder.h>

#include <konga/JmsUtils/JmsUtils.h>

#include "OperationEngineException.h"
#include "OperationEngineContext.h"
#include "ProcessedFiles.h"
#include "JmsTarget.h"

using namespace konga;
using std::vector;
using std::string;
using std::map;
using std::auto_ptr;

JmsTarget::JmsTarget(int jmsMessageId) : m_iJmsMessageId(jmsMessageId) {}
JmsTarget::~JmsTarget(void) {}

namespace {
	typedef std::map<std::string, std::string> ssMapT;

	                                             
	                                                     
	const string JMS_TIMETOLIVE_HEADER("JMSTimeToLive");

	int validateDeliveryMode(const string& deliveryMode, int defaultValue) {
		const int i = atoi(deliveryMode.c_str());
		if ( i==1 || i==2 )
			return i;
		else
			return defaultValue;
	}

	int validatePriority(const string& priority, int defaultValue) {
		const int i = atoi(priority.c_str());
		if ( i >= 0 && i <= 9 )
			return i;
		else
			return defaultValue;
	}

	long long validateTimeToLive(const string& timeToLive, long long defaultValue) {
		const long long ll = atoll(timeToLive.c_str());
		if ( ll >= 0 )
			return ll;
		else
			return defaultValue;
	}

	void AddJmsProperties(JmsMessage& jmsMessage, const OperationDataElementHolder& deHolder,
		DbConnection& dbConnection, int jmsMessageId)
	{
		                                                             
		                                              
		             
		                                                                                  
		  
		                                                                                                 
		int deliveryMode = -1;
		int priority = -1;
		long long timeToLive = -1;

		                                              
		                                                                         
		if ( jmsMessageId > -1 ) {
			string sql = "SELECT dataelementname, dataelementdefaultvalue FROM dataelementstab " \
				"WHERE ownerentitytypeid = " + toString(JmsMessageEntityId) +
				" AND ownerentityid = " + toString(jmsMessageId) +
				" AND (DataElementName='JMSDeliveryMode' OR DataElementName='JMSTimeToLive' OR DataElementName='JMSPriority')";
			SmartDbPtr<DynamicDbRecordSet> rs(DynamicDbRecordSet::MakeInstance(&dbConnection, sql));
			rs->Open();
			while( rs->Next() ) {
				const string& name = rs->GetFieldValue(0);
				const string& value = rs->GetFieldValue(1);
				if ( EqualsIgnoreCase(name, JmsHeader::JMSDeliveryMode.getName()) ) {
					deliveryMode = validateDeliveryMode(value, deliveryMode);
				}
				else if ( EqualsIgnoreCase(name, JmsHeader::JMSPriority.getName()) ) {
					priority = validatePriority(value, priority);
				}
				else if ( EqualsIgnoreCase(name, JMS_TIMETOLIVE_HEADER) ) {
					timeToLive = validateTimeToLive(value, timeToLive);
				}
			}
		}

		const map<string, DataElement*> deMap = deHolder.getOperationDataElements();
		                                                                                    
		for( map<string, DataElement*>::const_iterator cit = deMap.begin();
			cit != deMap.end(); ++cit )
		{
			                                                              
			const string& sName = cit->first;
			if ( sName.compare(0, JmsHeader::OUTGOING_JMS_HEADER_PREFIX.size(), JmsHeader::OUTGOING_JMS_HEADER_PREFIX)  == 0 ) {
				const string& headerName = sName.substr(JmsHeader::OUTGOING_JMS_HEADER_PREFIX.size());
				const DataElement& de = *cit->second;
				int err = 0;
				if ( EqualsIgnoreCase(headerName, JmsHeader::JMSDeliveryMode.getName()) ) {
					deliveryMode = validateDeliveryMode((const char*)de.GetString(err), deliveryMode);
				}
				else if ( EqualsIgnoreCase(headerName, JmsHeader::JMSPriority.getName()) ) {
					priority = validatePriority((const char*)de.GetString(err), priority);
				}
				else if ( EqualsIgnoreCase(headerName, JMS_TIMETOLIVE_HEADER) ) {
					timeToLive = validateTimeToLive((const char*)de.GetString(err), timeToLive);
				}
			}

			                                          
			if ( sName.compare(0, OUTGOING_JMS_PROPERTY_PREFIX.size(), OUTGOING_JMS_PROPERTY_PREFIX) == 0 ) {
				std::auto_ptr<DataElement> de( new DataElement(*cit->second) );
				de->setName(sName.c_str());
				jmsMessage.AddJmsProperty(*de);
			}
		}

		                                                       
		if ( deliveryMode != -1 ) {
			DataElement de(DataElement::t_int, deliveryMode);
			de.setName(JmsHeader::JMSDeliveryMode.getOutgoingDataElementName().c_str());
			jmsMessage.AddJmsProperty(de);
		}
		if ( priority != -1 ) {
			DataElement de(DataElement::t_int, priority);
			de.setName( JmsHeader::JMSPriority.getOutgoingDataElementName().c_str() );
			jmsMessage.AddJmsProperty(de);
		}
		if ( timeToLive != -1 ) {
			DataElement de(DataElement::t_long, timeToLive);
			de.setName( (JmsHeader::OUTGOING_JMS_HEADER_PREFIX + JMS_TIMETOLIVE_HEADER).c_str() );
			jmsMessage.AddJmsProperty(de);
		}
	}
}

namespace {
	void AddSendingProperties(JmsMessage& jmsMessage, long targetId,
		DbConnection& dbConnection, const OperationDataElementHolder& deHolder) {
		                                                                                 
		                                                            

		string sql = "SELECT keyname, value FROM jmslocationpropertiestab WHERE " \
			"jmslocationentitytypeid = " + toString(TargetEntityId)
			+ " AND jmslocationid = " + toString(targetId);

		SmartDbPtr<DynamicDbRecordSet> pCursor( DynamicDbRecordSet::MakeInstance(&dbConnection, sql) );
		pCursor->Open();
		int iProps = 0;
		while( pCursor->Next() ) {
			const string& name = pCursor->GetFieldValue(0);
			string value = pCursor->GetFieldValue(1);
			value = deHolder.replaceOperationDataElementInString(value);
			DataElement de(DataElement::t_string, value.c_str());
			de.setName(name.c_str());
			jmsMessage.AddSendingProperty(de);
			++iProps;
		}
		if ( iProps == 0 )
			throw OperationEngineException("No JMS Sending properties has been specified for this JMS Target!");
	}
}

void JmsTarget::doTransfer(const OperationDataElementHolder& deHolder, ProcessedFiles& files) const {

	vector<ProcessedFile>& processedFiles = files.getProcessedFilesByRef();

	MessageSender sender;
	for( vector<ProcessedFile>::iterator it = processedFiles.begin();
		it != processedFiles.end(); ++it )
	{
		ProcessedFile& file = *it;
		if ( doNotTransfer(file) )
			continue;

		const string& sLocalFilePath = file.getLocalTargetLocation();

		JmsMessage jmsMessage(sLocalFilePath);

		SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );

		AddSendingProperties(jmsMessage, this->getId(), *pTranDb, deHolder);
		AddJmsProperties(jmsMessage, deHolder, *pTranDb, m_iJmsMessageId);

		string sWarning;
		sender.AddMessageToQueue(jmsMessage, sWarning);
		if ( !sWarning.empty() ) {
			file.addWarning(sWarning);
		}
	}
}

void JmsTarget::testFileTargetConnection(bool bCopyTestFile) const {
	JmsLocation::testJmsLocation();
}
