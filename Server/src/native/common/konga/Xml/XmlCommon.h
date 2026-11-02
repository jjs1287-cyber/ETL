/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XML_COMMON_H
#define KONGA_XML_COMMON_H

#include <string>
#include <boost/noncopyable.hpp>
#include <xercesc/dom/DOMLSSerializer.hpp>
#include <xercesc/dom/DOMLSParser.hpp>
#include <xercesc/util/XMLString.hpp>
#include <xercesc/dom/DOMLSOutput.hpp>

                                                                                 
#include "XmlStrings.h"

namespace konga {
namespace xml {
    class AutoReleaseDOMOutput : boost::noncopyable {
    public:
        explicit AutoReleaseDOMOutput(XERCES_CPP_NAMESPACE::DOMLSOutput* output) : output_(output) {}
        ~AutoReleaseDOMOutput() { if(output_) output_->release(); }
    private:
        XERCES_CPP_NAMESPACE::DOMLSOutput* output_;
    };


	class AutoReleaseDOMWriter : boost::noncopyable {
	public:
		AutoReleaseDOMWriter(XERCES_CPP_NAMESPACE::DOMLSSerializer* pWriter) : m_pWriter(pWriter) { }
		~AutoReleaseDOMWriter(void) {
			if ( m_pWriter != 0 ) {
				m_pWriter->release();
			}
		}
	private:
		XERCES_CPP_NAMESPACE::DOMLSSerializer* m_pWriter;
	};

	class AutoReleaseDOMBuilder : boost::noncopyable {
	public:
		AutoReleaseDOMBuilder(XERCES_CPP_NAMESPACE::DOMLSParser* pBuilder = 0) : m_pBuilder(pBuilder) {
		}
		~AutoReleaseDOMBuilder(void) {
			reset();
		}

		XERCES_CPP_NAMESPACE::DOMLSParser* release() {
			XERCES_CPP_NAMESPACE::DOMLSParser* p = m_pBuilder;
			m_pBuilder = 0;
			return p;
		}

		void reset(XERCES_CPP_NAMESPACE::DOMLSParser* pBuilder = 0) {
			if ( m_pBuilder != 0 ) {
				m_pBuilder->release();
			}
			m_pBuilder = pBuilder;
		}

	private:
		XERCES_CPP_NAMESPACE::DOMLSParser* m_pBuilder;
	};



}                 
}                   

#endif                      

