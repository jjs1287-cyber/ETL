/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>
#include <konga/Utils/MapUtils.h>

#include "PluginDataElementHolder.h"

using namespace konga;
using std::string;

void PluginDataElementHolder::AddDataElement(const std::string& deName,
										 boost::shared_ptr<PluginDataElement> de)
{
	if ( deName.empty() )
		throw CrException("Data Elements must have a name.");
	AddOrUpdate<DEMapT, string, boost::shared_ptr<PluginDataElement> >(
		m_DataElements,
		deName, de);
}

const PluginDataElementHolder::DEMapT& PluginDataElementHolder::GetDataElements() const {
	return m_DataElements;
}
