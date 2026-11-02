/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_HTTP_SOURCE_H
#define KONGA_OPERATION_HTTP_SOURCE_H

#include "FileTypeSourceLocation.h"
#include "HttpLocation.h"

namespace konga {

	class HttpSource : public FileTypeSourceLocation, public HttpLocation {
	public:
		HttpSource(void);
		virtual ~HttpSource(void);
		
		virtual void doPostTransformation(const OperationEngineContext& context, ProcessedFiles& files) const {
			                                             
		}

		virtual void getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const;

		virtual std::vector<std::string> getFileList() const;

	protected:
		virtual std::string getHttpUrl() const;

		long getTransferTimeOutSeconds() const;

		std::string getFileFilter() const {
			return getFileName();
		}
		void doTransferImpl(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const;

	};
}
#endif                                 

