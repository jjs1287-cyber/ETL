/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#ifndef MOD_KONGA_WS_WSDL_UTILS_H
#define MOD_KONGA_WS_WSDL_UTILS_H

#include <string>

namespace konga {

	void GetWsdlForHostedWebService(const long lHostedWebServiceOperationId, std::string& sWsdlContents);

	void GetHtmlForHostedWebService(const long lHostedWebServiceOperationId, const std::string& sOperationGuid, std::string& sHtmlInfo);
}

#endif                             

