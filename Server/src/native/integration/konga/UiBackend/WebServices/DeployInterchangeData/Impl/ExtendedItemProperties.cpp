/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <cassert>

#include "ExtendedItemProperties.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::map;
using std::string;

ExtendedItemPropertiesCollection::~ExtendedItemPropertiesCollection(void) {
	map<string, ExtendedItemProperties*>::iterator it = m_PropertiesMap.begin();
	for(; it != m_PropertiesMap.end(); ++it ) {
		delete it->second;
		it->second = 0;
	}
	m_PropertiesMap.clear();
}

bool ExtendedItemPropertiesCollection::empty() const {
	return m_PropertiesMap.empty();
}

ExtendedItemProperties* ExtendedItemPropertiesCollection::GetExtendedItemPropertiesForGuid(const std::string& guid) const {
	map<string, ExtendedItemProperties*>::const_iterator cit = m_PropertiesMap.find(guid);
	if ( cit == m_PropertiesMap.end() )
		return 0;
	else
		return cit->second;
}

void ExtendedItemPropertiesCollection::AddExtendedItemProperties(const std::string& guid, ExtendedItemProperties* properties) {
	assert(this->GetExtendedItemPropertiesForGuid(guid) == 0);
	m_PropertiesMap.insert( map<string, ExtendedItemProperties*>::value_type(guid, properties) );
}
