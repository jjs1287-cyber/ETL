/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#include "Message.h"

using namespace konga;
using std::string;

Message::Message(void) : m_priority(MSG_PRIORITY_NORMAL) {}
Message::~Message(void) {}

void Message::SetFrom(const std::string& sFrom) {
	m_sFrom = sFrom;
}

std::string Message::GetFrom() const {
	return m_sFrom;
}

void Message::SetTo(const std::string& sTo) {
	m_sTo = sTo;
}

std::string Message::GetTo() const {
	return m_sTo;
}

void Message::SetSubject(const std::string& sSubject) {
	m_sSubject = sSubject;
}

std::string Message::GetSubject() const {
	return m_sSubject ;
}

void Message::SetPriority(MSG_PRIORITY p) { m_priority = p; }

MSG_PRIORITY Message::GetPriority() const {
	return m_priority;
}
