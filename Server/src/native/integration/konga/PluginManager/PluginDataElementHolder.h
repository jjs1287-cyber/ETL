/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef KONGA_PLUGIN_DATA_ELEMENT_HOLDER_H
#define KONGA_PLUGIN_DATA_ELEMENT_HOLDER_H

#include <string>

#include <boost/shared_ptr.hpp>

#include "PluginDataElement.h"

namespace konga {

	class PLUGIN_MANAGER_API PluginDataElementHolder {
	public:
		PluginDataElementHolder(void) {}
		~PluginDataElementHolder(void) {}

		typedef std::map<std::string, boost::shared_ptr<PluginDataElement> > DEMapT;

		const DEMapT& GetDataElements() const;

		void AddDataElement(const std::string& deName, boost::shared_ptr<PluginDataElement> de);

	private:
		DEMapT m_DataElements;

	};
}

#endif                                      

