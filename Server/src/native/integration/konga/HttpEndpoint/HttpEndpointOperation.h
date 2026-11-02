/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_HTTP_ENDPOINT_OPERATION_H
#define KONGA_HTTP_ENDPOINT_OPERATION_H

namespace konga {

	class HttpEndpointOperation {
	public:
		HttpEndpointOperation(long operationId, bool isSync, bool finalTargetIsResponse, const std::string& verb)
			: m_bSynchronous(isSync), m_bFinalTargetIsResponse(finalTargetIsResponse), m_bUseRequestAsSource(true),
			m_lOperationId(operationId), m_sHttpVerb(verb) {}
		~HttpEndpointOperation(void) {}

		long getOperationId() const {
			return m_lOperationId;
		}

		bool isSynchronous() const {
			return m_bSynchronous;
		}

		bool useRequestAsSource() const {
			return m_bUseRequestAsSource;
		}

		bool finalTargetIsResponse() const {
			return m_bFinalTargetIsResponse;
		}

		std::string getHttpVerb() const {
			return m_sHttpVerb;
		}

	private:
		bool m_bSynchronous;
		bool m_bFinalTargetIsResponse;
		bool m_bUseRequestAsSource;
		long m_lOperationId;
		std::string m_sHttpVerb;
	};

}

#endif                                   

