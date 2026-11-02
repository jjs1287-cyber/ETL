/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>

#include "HttpEndpoint.h"

using namespace konga;
using std::string;
using std::vector;

HttpEndpoint::~HttpEndpoint(void) {
	for( vector<HttpEndpointOperation*>::iterator it = m_operations.begin(); it != m_operations.end(); ++it ) {
		delete *it;
		*it = 0;
	}
	m_operations.clear();
}

void HttpEndpoint::addOperation(HttpEndpointOperation* p) {
	m_operations.push_back(p);
}

string HttpEndpoint::getName() const {
	return m_sName;
}

HttpEndpointResponseMode HttpEndpoint::getHttpEndpointResponseMode() const {
	return m_response_mode;
}

string HttpEndpoint::getRequestVariable() const {
	return m_sRequestVariable;
}

string HttpEndpoint::getResponseVariable() const {
	return m_sResponseVariable;
}

string HttpEndpoint::getResponseContentType() const {
	return m_sResponseContentType;
}

const vector<HttpEndpointOperation*>& HttpEndpoint::getOperations() const {
	return m_operations;
}

vector<HttpEndpointOperation*> HttpEndpoint::getSynchronousOperations(const string& verb) const {
	vector<HttpEndpointOperation*> retVal;
	for( vector<HttpEndpointOperation*>::const_iterator cit = m_operations.begin(); cit != m_operations.end(); ++cit ) {
		const HttpEndpointOperation* const p = *cit;

		                                                          
		const string& thisVerb = p->getHttpVerb();
		if ( !verb.empty() && !thisVerb.empty() && !EqualsIgnoreCase(verb, thisVerb) )
			continue;

		if ( p->isSynchronous() ) {
			retVal.push_back(*cit);
		}
	}
	return retVal;
}

vector<HttpEndpointOperation*> HttpEndpoint::getAsynchronousOperations(const string& verb) const {
	vector<HttpEndpointOperation*> retVal;
	for( vector<HttpEndpointOperation*>::const_iterator cit = m_operations.begin(); cit != m_operations.end(); ++cit ) {
		const HttpEndpointOperation* const p = *cit;

		                                                          
		const string& thisVerb = p->getHttpVerb();
		if ( !verb.empty() && !thisVerb.empty() && !EqualsIgnoreCase(verb, thisVerb) )
			continue;

		if ( !p->isSynchronous() ) {
			retVal.push_back(*cit);
		}
	}
	return retVal;
}

vector<HttpEndpointOperation*> HttpEndpoint::getOperations(const string& verb) const {
	vector<HttpEndpointOperation*> retVal;
	for( vector<HttpEndpointOperation*>::const_iterator cit = m_operations.begin(); cit != m_operations.end(); ++cit ) {
		const HttpEndpointOperation* const p = *cit;

		                                                          
		const string& thisVerb = p->getHttpVerb();
		if ( !verb.empty() && !thisVerb.empty() && !EqualsIgnoreCase(verb, thisVerb) )
			continue;

		retVal.push_back(*cit);
	}
	return retVal;
}
