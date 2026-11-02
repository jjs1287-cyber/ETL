/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_EXTENDED_ITEM_PROPERTIES_H
#define KONGA_WS_DEPLOY_EXTENDED_ITEM_PROPERTIES_H

#include <string>
#include <map>

#include <boost/noncopyable.hpp>

#include <konga/Utils/KeyValues.h>

namespace konga {
namespace interchange_deploy {

	class ExtendedItemProperties : private boost::noncopyable {
	public:
		ExtendedItemProperties(int iEntityTypeId, const std::string& guid) : m_iEntityTypeId(iEntityTypeId), m_lItemId(-1), m_sGuid(guid) {}
		~ExtendedItemProperties(void) {}

		void SetItemId(long lItemId) { m_lItemId = lItemId; }
		int GetEntityTypeId() const { return m_iEntityTypeId; }

	public:
		konga::KeyValues m_properties;

	private:
		const int m_iEntityTypeId;
		long m_lItemId;
		const std::string m_sGuid;
	};

	class ExtendedItemPropertiesCollection {
	public:
		ExtendedItemPropertiesCollection(void) {}
		~ExtendedItemPropertiesCollection(void);

		bool empty() const;

		                                                                                       
		ExtendedItemProperties* GetExtendedItemPropertiesForGuid(const std::string& guid) const;

		                          
		void AddExtendedItemProperties(const std::string& guid, ExtendedItemProperties* properties);

		std::map<std::string, ExtendedItemProperties*> m_PropertiesMap;
	};
}
}

#endif                                              

