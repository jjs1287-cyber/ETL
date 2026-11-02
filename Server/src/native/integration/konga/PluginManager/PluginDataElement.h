/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_PLUGIN_DATAELEMENT_H
#define KONGA_PLUGIN_DATAELEMENT_H

#include <konga/DataElement/DataElement.h>

#include "PluginManagerApi.h"

#ifdef WIN32
	                                                                                    
	#pragma warning( disable: 4275 )
#endif

namespace konga {

	                                                                                           
	class PLUGIN_MANAGER_API PluginDataElement : public DataElement {
		
	public:
		PluginDataElement(const std::string& sName, DataElement::Type type,
			const std::string& sValue, bool isRequired, bool isWildCard)
			: DataElement(type, sValue.c_str()), m_bIsRequired(isRequired), m_bIsWildCard(isWildCard)
		{
			this->setName(sName.c_str());
		}

		PluginDataElement(const DataElement& de, bool bIsRequired)
			: DataElement(de), m_bIsRequired(bIsRequired) {}

		virtual ~PluginDataElement(void) {}

		bool IsRequired() const {
			return m_bIsRequired;
		}

		bool IsWildCard() const {
			return m_bIsWildCard;
		}

	private:
		bool m_bIsRequired;
		bool m_bIsWildCard;
	};
}

#endif                              

