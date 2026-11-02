/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include "JmsUtils.h"

using namespace konga;
using std::string;

const string OUTGOING_JMS_PROPERTY_PREFIX("jms.property.out.");

const string JmsHeader::OUTGOING_JMS_HEADER_PREFIX("jms.header.out.");

const JmsHeader JmsHeader::JMSCorrelationID("JMSCorrelationID", DataElement::t_int);
const JmsHeader JmsHeader::JMSDeliveryMode("JMSDeliveryMode", DataElement::t_int);
                                                              
const JmsHeader JmsHeader::JMSExpiration("JMSExpiration", DataElement::t_long);
const JmsHeader JmsHeader::JMSMessageID("JMSMessageID", DataElement::t_string);
const JmsHeader JmsHeader::JMSPriority("JMSPriority", DataElement::t_int);
const JmsHeader JmsHeader::JMSRedelivered("JMSRedelivered", DataElement::t_bool);
                                                      
const JmsHeader JmsHeader::JMSTimestamp("JMSTimestamp", DataElement::t_long);
const JmsHeader JmsHeader::JMSType("JMSType", DataElement::t_string);

JmsHeader::JmsHeader(const string& name, const DataElement::Type& type)
: m_type(type), m_name(name) {}

JmsHeader::~JmsHeader(void) {}

string JmsHeader::getName() const {
	return m_name;
}

DataElement::Type JmsHeader::getType() const {
	return m_type;
}

string JmsHeader::getIncomingDataElementName() const {
	return string("jms.header.in." + m_name);
}

string JmsHeader::getOutgoingDataElementName() const {
	return string("jms.header.out." + m_name);
}

