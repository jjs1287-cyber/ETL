/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

                                   
#include <xercesc/util/XMLUri.hpp>

                                                        
#include <konga/Xml/XmlStrings.h>

#include "CrException.h"
#include "XmlInit.h"
#include "PortDefs.h"
#include "Url.h"

XERCES_CPP_NAMESPACE_USE

using namespace konga;
using namespace konga::xml;
using std::string;

Url::Url(void) : m_lPort(-1), m_bHasQueryString(false), m_bHasFragment(false) { }

Url::Url(const std::string& stUrl) : m_lPort(-1), m_bHasQueryString(false), m_bHasFragment(false)
{
	Parse(stUrl);
}

Url::~Url(void) { }

void Url::Parse(const string& stUrl) {
	Clear();
	konga::XmlInitializer::InitXerces();
	try {
		const XMLUri url(X(stUrl.c_str()));

		m_stProtocol = XMLChToString(url.getScheme());
		m_stHost = XMLChToString(url.getHost());
		m_lPort = url.getPort();
		m_stPath = XMLChToString(url.getPath());

		const XMLCh* ch = url.getQueryString();
		m_bHasQueryString = ( ch != 0 );
		m_stQueryString = XMLChToString(ch);

		ch = url.getFragment();
		m_bHasFragment = (ch != 0 );
		m_stFragment = XMLChToString(ch);
		string stRegBasedAuthority = XMLChToString( url.getRegBasedAuthority() );
		
		m_stParsedUrl = XMLChToString(url.getUriText());
	}
	catch ( const XMLException& ex ) {
		Clear();
		throw CrException(
			"Failed to parse the URL " + stUrl + ". " + XMLChToString(ex.getMessage())
			);
	}
}

string Url::getParsedUrl() const {
	return m_stParsedUrl;
}

string Url::getProtocol() const {
	return m_stProtocol;
}

string Url::getHost() const {
	return m_stHost;
}

string Url::getPath() const {
	return m_stPath;
}

long Url::getPort() const {
	return m_lPort;
}

string Url::getQueryString() const {
	return m_stQueryString;
}

bool Url::hasQueryString() const {
	return m_bHasQueryString;
}

string Url::getFragment() const {
	return m_stFragment;
}

bool Url::hasFragment() const {
	return m_bHasFragment;
}

void Url::Clear() {
	m_stProtocol.clear();
	m_stHost.clear();
	m_stPath.clear();
	m_stQueryString.clear();
	m_stFragment.clear();
	m_stParsedUrl.clear();
	m_bHasQueryString = false;
	m_bHasFragment = false;
	m_lPort = -1;
}

bool konga::IsUrl(const std::string& stUrl) {
	konga::Url url;
	try {
		url.Parse(stUrl);
	}
	catch ( const CrException& ) {
		return false;
	}
	return true;
}
