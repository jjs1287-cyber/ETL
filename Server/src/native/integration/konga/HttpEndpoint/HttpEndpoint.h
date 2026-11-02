/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_HTTP_ENDPOINT_H
#define KONGA_HTTP_ENDPOINT_H

#include <string>
#include <vector>

#include "HttpEndpointResponseMode.h"
#include "HttpEndpointOperation.h"

namespace konga {

	class HttpEndpoint {
	public:
		HttpEndpoint(const std::string& name, HttpEndpointResponseMode mode,
			const std::string& requestVariable, const std::string& responseVariable) :
		m_response_mode(mode), m_sName(name),
			m_sRequestVariable(requestVariable), m_sResponseVariable(responseVariable)
		{}
		~HttpEndpoint(void);

		                                      
		void addOperation(HttpEndpointOperation* p);

		std::string getName() const;

		HttpEndpointResponseMode getHttpEndpointResponseMode() const;

		std::string getRequestVariable() const;

		std::string getResponseVariable() const;

		std::string getResponseContentType() const;

		const std::vector<HttpEndpointOperation*>& getOperations() const;

		                                                                                           
		std::vector<HttpEndpointOperation*> getSynchronousOperations(const std::string& verb) const;

		                                                           
		std::vector<HttpEndpointOperation*> getAsynchronousOperations(const std::string& verb) const;

		                                              
		std::vector<HttpEndpointOperation*> getOperations(const std::string& verb) const;

	private:
		HttpEndpointResponseMode m_response_mode;
		std::string m_sName;
		std::string m_sRequestVariable, m_sResponseVariable;
		std::string m_sResponseContentType;

		std::vector<HttpEndpointOperation*> m_operations;
	};
}

#endif                         

