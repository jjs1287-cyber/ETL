/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_JMS_UTILS_H
#define ETLFORGE_JMS_UTILS_H

#include <string>

#include <konga/DataElement/DataElement.h>

namespace konga {

	const std::string OUTGOING_JMS_PROPERTY_PREFIX;

	class JmsHeader {
	private:
		JmsHeader(const std::string& name, const DataElement::Type& type);
		~JmsHeader(void);

	public:
		std::string getName() const;

		DataElement::Type getType() const;

		std::string getIncomingDataElementName() const;
		std::string getOutgoingDataElementName() const;

		static const std::string OUTGOING_JMS_HEADER_PREFIX;

		static const JmsHeader JMSCorrelationID;
		static const JmsHeader JMSDeliveryMode;
                                          
		static const JmsHeader JMSExpiration;
		static const JmsHeader JMSMessageID;
		static const JmsHeader JMSPriority;
		static const JmsHeader JMSRedelivered;
                                      
		static const JmsHeader JMSTimestamp;
		static const JmsHeader JMSType;

	private:
		DataElement::Type m_type;
		std::string m_name;
	};
}

#endif                        

