/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_HTTP_TARGET_H
#define KONGA_OPERATION_HTTP_TARGET_H

#include <boost/noncopyable.hpp>

#include "FileTypeTargetLocation.h"
#include "HttpLocation.h"

namespace konga {

	class HttpTarget : boost::noncopyable, public FileTypeTargetLocation, public HttpLocation {
	public:
		HttpTarget(void);
		virtual ~HttpTarget(void);

		virtual void doTransfer(const OperationDataElementHolder& deHolder, ProcessedFiles& files) const;

		virtual void testFileTargetConnection(bool bCopyTestFile) const;

		                              
		virtual std::string getHttpUrl() const;

		                                        
		virtual std::string getFileName() const;

		                                          
		void setResponseTarget(TargetDataLocation* pTargetLoc) {
			m_pResponseTarget = pTargetLoc;
		}

	protected:
		long getTransferTimeOutSeconds() const;

	private:
		TargetDataLocation* m_pResponseTarget;
	};
}
#endif                                 

