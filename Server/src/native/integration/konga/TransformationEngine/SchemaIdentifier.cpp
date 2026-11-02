/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

                                                                  

#include <konga/Utils/PortDefs.h>

#include "XsdUtils.h"

bool XsdUtils::SchemaIdentifier::EqualTo(const SchemaIdentifier& rhs) const
{
	return strcmp(m_stTargetNamespace.c_str(), rhs.m_stTargetNamespace.c_str()) == 0 &&
		stricmp(m_stSchemaLocation.c_str(), rhs.m_stSchemaLocation.c_str()) == 0;
}

bool XsdUtils::SchemaIdentifier::operator== (const SchemaIdentifier& rhs) const
{
	return this->EqualTo(rhs);
}

bool XsdUtils::SchemaIdentifier::operator< (const SchemaIdentifier& rhs) const
{
	std::string stLhs = m_stTargetNamespace + "|" + m_stSchemaLocation;
	std::string stRhs = rhs.m_stTargetNamespace + "|" + rhs.m_stSchemaLocation;
	return stricmp(stLhs.c_str(), stRhs.c_str()) < 0;
}

