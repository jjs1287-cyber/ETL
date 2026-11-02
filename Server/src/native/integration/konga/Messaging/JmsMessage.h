/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_MESSAGING_JMS_MESSAGE_H
#define ETLFORGE_MESSAGING_JMS_MESSAGE_H

#include <vector>
#include <string>

#include "Message.h"

namespace konga {

	class DataElement;

	class JmsMessage : public Message {
	public:
		                                                                      
		static std::string  DESTINATION_TYPE;
		static std::string  PROVIDER_ID;
		static std::string  CONNECTION_FACTORY_JNDI_NAME;
		static std::string  INITIAL_CONTEXT_FACTORY_NAME;
		static std::string  URL_PACKAGE_PREFIXES;
		static std::string  CONNECTION_CREDENTIALS;
		static std::string  JNDI_URL;
		static std::string  DESTINATION_PATH;
	    
		static std::string  CONNECTION_USER_NAME;
		static std::string  CONNECTION_PASSWORD;

	public:
		JmsMessage(const std::string& sLocalPayloadPath);
		virtual ~JmsMessage(void);

		void AddSendingProperty(const DataElement& de);
		void AddJmsProperty(const DataElement& de);

		typedef std::vector<DataElement*> DEVector;

		const DEVector& GetSendingProperties() const;
		const DEVector& GetJmsProperties() const;

		std::string GetLocalPayloadPath() const;

		void Validate() const;

	private:
		std::string m_sLocalPayloadPath;
		DEVector m_vSendingProperties;
		DEVector m_vJmsProperties;
	};
}

#endif                                    

