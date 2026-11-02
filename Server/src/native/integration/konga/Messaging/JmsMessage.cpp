/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include <konga/DataElement/DataElement.h>

#include "JmsMessage.h"

using namespace konga;

std::string JmsMessage::DESTINATION_TYPE = "Jms.Destination.Type";
std::string JmsMessage::PROVIDER_ID = "Jms.ProviderId";
std::string JmsMessage::CONNECTION_FACTORY_JNDI_NAME = "Jms.ConnectionFactoryJndiName";
std::string JmsMessage::INITIAL_CONTEXT_FACTORY_NAME = "Jms.InitialContextFactoryName";
std::string JmsMessage::URL_PACKAGE_PREFIXES = "Jms.UrlPackagePrefixes";
std::string JmsMessage::CONNECTION_CREDENTIALS = "Jms.Connection.Credentials";
std::string JmsMessage::JNDI_URL = "Jms.JndiUrl";
std::string JmsMessage::DESTINATION_PATH = "Jms.DestinationPath";

std::string JmsMessage::CONNECTION_USER_NAME = "Jms.Connection.UserName";
std::string JmsMessage::CONNECTION_PASSWORD = "Jms.Connection.Password";


JmsMessage::JmsMessage(const std::string& sLocalPayloadPath)
: m_sLocalPayloadPath(sLocalPayloadPath)
{}

JmsMessage::~JmsMessage(void) {
	for( JmsMessage::DEVector::iterator it = m_vSendingProperties.begin();
		it != m_vSendingProperties.end(); ++it )
	{
		delete *it;
	}
	m_vSendingProperties.clear();

	for( JmsMessage::DEVector::iterator it = m_vJmsProperties.begin();
		it != m_vJmsProperties.end(); ++it )
	{
		delete *it;
	}
	m_vJmsProperties.clear();
}

void JmsMessage::AddSendingProperty(const DataElement& de) {
	m_vSendingProperties.push_back( new DataElement(de) );
}

void JmsMessage::AddJmsProperty(const DataElement& de) {
	m_vJmsProperties.push_back( new DataElement(de) );
}

const JmsMessage::DEVector& JmsMessage::GetSendingProperties() const {
	return m_vSendingProperties;
}

const JmsMessage::DEVector& JmsMessage::GetJmsProperties() const {
	return m_vJmsProperties;
}

std::string JmsMessage::GetLocalPayloadPath() const {
	return m_sLocalPayloadPath;
}

void JmsMessage::Validate() const {
	                                         
}

