/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef ETLFORGE_WS_PLUGIN_MANAGEMENT_IMPL_H
#define ETLFORGE_WS_PLUGIN_MANAGEMENT_IMPL_H

#include <konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.h>

class FilterAsXml_Array;

namespace konga {

	class PluginManagementImpl : public WsImplBaseAuthenticator {
	public:
		PluginManagementImpl(const char* user, const char* password) :
		  WsImplBaseAuthenticator(user, password) {}

		FilterAsXml_Array* GetFilters();
	};

}                   

#endif                                        

