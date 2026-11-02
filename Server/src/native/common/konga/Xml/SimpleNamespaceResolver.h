/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XML_SIMPLE_NAMESPACE_RESOLVER_H
#define KONGA_XML_SIMPLE_NAMESPACE_RESOLVER_H

#include <map>
#include <string>

#include <boost/shared_ptr.hpp>

#include <xalanc/DOMSupport/XalanDocumentPrefixResolver.hpp>
#include <xalanc/XalanDOM/XalanDOMString.hpp>
#include <xalanc/XercesParserLiaison/XercesWrapperHelper.hpp>

namespace konga {

	                                                              
	                                                           
                                             
	class SimpleNamespaceResolver : public XALAN_CPP_NAMESPACE::PrefixResolver {

		typedef std::map< std::string, boost::shared_ptr<XALAN_CPP_NAMESPACE::XalanDOMString> > prefixMapT;

	public:
		SimpleNamespaceResolver(const std::map<std::string, std::string>& nsMap) {
			for( std::map<std::string, std::string>::const_iterator it =
				nsMap.begin(); it != nsMap.end(); ++it ) {
				m_Map[it->first] = boost::shared_ptr<XALAN_CPP_NAMESPACE::XalanDOMString> (
					new XALAN_CPP_NAMESPACE::XalanDOMString(it->second.c_str())
					);
			}
		}
		~SimpleNamespaceResolver(void) { }

		virtual const XALAN_CPP_NAMESPACE::XalanDOMString* getNamespaceForPrefix(const XALAN_CPP_NAMESPACE::XalanDOMString& prefix) const {
			const std::string& stPrefix =
				xml::XMLChToString( XALAN_CPP_NAMESPACE::XercesWrapperHelper::XalanDOMStringToXercesDOMString(prefix) );
			prefixMapT::const_iterator it = m_Map.find(stPrefix);
			return ( it != m_Map.end() ) ? it->second.get() : 0;
		}

		virtual const XALAN_CPP_NAMESPACE::XalanDOMString& getURI() const {
			return m_Uri;
		}

	private:
		XALAN_CPP_NAMESPACE::XalanDOMString m_Uri;
		prefixMapT m_Map;
	};

}

#endif                                         

