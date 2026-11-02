/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include "GroupInfo.h"

using namespace konga;
using std::string;

GroupInfo::GroupInfo(long iGroupId, const string& stGroupName, const std::string& stDescription)
: m_iGroupId(iGroupId), m_stGroupName(stGroupName), m_stDescription(stDescription)
{
}

GroupInfo::~GroupInfo(void) {}

long GroupInfo::GetId() const {
	return m_iGroupId;
}

bool GroupInfo::IsAdministrator() const {
	return m_iGroupId == 0;
}

string GroupInfo::GetName() const {
	return m_stGroupName;
}
