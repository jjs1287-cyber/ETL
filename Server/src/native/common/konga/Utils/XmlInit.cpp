/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <xalanc/XalanTransformer/XalanTransformer.hpp>
#include <xalanc/XPath/XPathEvaluator.hpp>

#include <boost/thread/mutex.hpp>

#include <konga/Utils/Settings.h>
#include <konga/Utils/CrException.h>

#include <konga/Xml/XmlStrings.h>

#include "XmlInit.h"

using namespace konga;
using namespace konga::xml;
XERCES_CPP_NAMESPACE_USE

namespace {
	const XMLCh enc_latin1[] = {
		XERCES_CPP_NAMESPACE::chLatin_I,
		XERCES_CPP_NAMESPACE::chLatin_S,
		XERCES_CPP_NAMESPACE::chLatin_O,
		XERCES_CPP_NAMESPACE::chDash,
		XERCES_CPP_NAMESPACE::chDigit_8,
		XERCES_CPP_NAMESPACE::chDigit_8,
		XERCES_CPP_NAMESPACE::chDigit_5,
		XERCES_CPP_NAMESPACE::chDigit_9,
		XERCES_CPP_NAMESPACE::chDash,
		XERCES_CPP_NAMESPACE::chDigit_1,
		XERCES_CPP_NAMESPACE::chNull
	};

	const XMLCh enc_utf8[] = {
		XERCES_CPP_NAMESPACE::chLatin_U,
		XERCES_CPP_NAMESPACE::chLatin_T,
		XERCES_CPP_NAMESPACE::chLatin_F,
		XERCES_CPP_NAMESPACE::chDash,
		XERCES_CPP_NAMESPACE::chDigit_8,
		XERCES_CPP_NAMESPACE::chNull
	};

	const XMLCh enc_win1252[] = {
		XERCES_CPP_NAMESPACE::chLatin_W,
		XERCES_CPP_NAMESPACE::chLatin_i,
		XERCES_CPP_NAMESPACE::chLatin_n,
		XERCES_CPP_NAMESPACE::chLatin_d,
		XERCES_CPP_NAMESPACE::chLatin_o,
		XERCES_CPP_NAMESPACE::chLatin_w,
		XERCES_CPP_NAMESPACE::chLatin_s,
		XERCES_CPP_NAMESPACE::chDash,
		XERCES_CPP_NAMESPACE::chDigit_1,
		XERCES_CPP_NAMESPACE::chDigit_2,
		XERCES_CPP_NAMESPACE::chDigit_5,
		XERCES_CPP_NAMESPACE::chDigit_2,
		XERCES_CPP_NAMESPACE::chNull
	};

	const XMLCh* g_enc = 0;

	boost::mutex g_initxml_mutex;
	bool g_xerces_initialized = false;
	bool g_xalan_initialized = false;

	                                    
	void global_init_xerces() {
		if ( !g_xerces_initialized ) {
			XMLPlatformUtils::Initialize();
			g_enc = enc_utf8;
			g_xerces_initialized = true;
		}
	}

	                                    
	void global_init_xalan() {
		using XALAN_CPP_NAMESPACE::XPathEvaluator;
		using XALAN_CPP_NAMESPACE::XalanTransformer;
		if ( !g_xalan_initialized ) {
			XalanTransformer::initialize();
			XPathEvaluator::initialize();
			g_xalan_initialized = true;
		}
	}
}

void XmlInitializer::InitXml() {
	using XALAN_CPP_NAMESPACE::XSLException;
	try {
		boost::mutex::scoped_lock lock(g_initxml_mutex);
		global_init_xerces();
		global_init_xalan();
	}
	catch(const XMLException& ex)
	{
		throw CrException("Internal Error: Failed to initialize XML Platform (Xerces). "
			+ XMLChToString(ex.getMessage()));
	}
	catch(const XSLException& ) {
		throw CrException("Internal Error: Failed to initialize XPathEvaluator (Xalan).");
	}
}

void XmlInitializer::InitXerces() {
	try {
		boost::mutex::scoped_lock lock(g_initxml_mutex);
		global_init_xerces();
	}
	catch(const XMLException& ex)
	{
		throw CrException("Internal Error: Failed to initialize XML Platform (Xerces). "
			+ XMLChToString(ex.getMessage()));
	}
}

const XMLCh* konga::get_xml_enc() {
	return g_enc;
}

