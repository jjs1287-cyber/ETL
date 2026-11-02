/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_HTTP_LOCATION_H
#define KONGA_OPERATION_HTTP_LOCATION_H

#include <konga/FileTransfer/HttpVerbEnum.h>
#include <konga/FileTransfer/HttpOptions.h>

#include <konga/FileTransfer/HttpOptions.h>

#include "DataLocation.h"

namespace konga {
	class HttpBase;
	class HttpProxyConfiguration;

	                                                      
	class HttpLocation : public DataLocation {
	public:
		HttpLocation(void);
		virtual ~HttpLocation(void);

	protected:
		static void appendPartOfErrorResponse(const std::string& stResponseFileName, std::string& stErrorMsg);

		std::vector<std::string> getFileListImpl() const;

		virtual std::string getHttpUrl() const;

		std::string getLogin() const;

		HttpVerb getHttpVerb() const;

		                                                                                                         
		std::string getFileName() const;

		                                                                         
		HttpBase* getHttpForLocation() const;

		virtual long getTransferTimeOutSeconds() const = 0;

	private:
		HttpProxyConfiguration* getProxyConfigCache() const;

	private:
		mutable HttpProxyConfiguration* m_pProxyConfigCache;
	};
}
#endif                                   

