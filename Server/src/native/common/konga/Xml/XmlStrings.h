/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_XML_XML_STRINGS_H
#define KONGA_XML_XML_STRINGS_H

#include <string>
#include <cassert>

#include <konga/Utils/XmlInit.h>

#include <boost/noncopyable.hpp>

#include <xercesc/util/XMLString.hpp>
#include <xercesc/util/TransService.hpp>
#include <xercesc/util/PlatformUtils.hpp>
#include <xercesc/util/XMLUniDefs.hpp>

               
#define USE_TRANSCODEFROM_TO
        

                                                         
                                                                               
                            

namespace konga {
namespace xml {

#ifdef USE_TRANSCODEFROM_TO

	namespace {

		char* xml_transcode_to_impl(const XMLCh* const unicode) {
			assert(unicode);

			XERCES_CPP_NAMESPACE_USE;
			const XMLSize_t srcCount = XMLString::stringLen(unicode);

			                                                                                       
			XMLSize_t blockSize = 4*1024;
			if ( srcCount > blockSize ) {
			   const int nK = srcCount/1024 + 1;
			   blockSize = nK*1024;
			}
			
			XMLTransService::Codes failReason;
			XMLTranscoder* t = XMLPlatformUtils::fgTransService->makeNewTranscoderFor(
			   get_xml_enc(), failReason, blockSize);
			assert(t);
			const XMLSize_t maxBytes = srcCount*3;		                                                                 
			XMLByte* toFill = new XMLByte[maxBytes + 1];	                                 
			XMLSize_t charsEaten = 0;
			const XMLSize_t charsPut = t->transcodeTo(unicode, srcCount, toFill, maxBytes, charsEaten, XMLTranscoder::UnRep_RepChar);
#ifdef _DEBUG
			assert(charsEaten==srcCount);
#endif
			toFill[charsPut] = 0;
			delete t;
			return (char*)toFill;
		}
		
		XMLCh* xml_transcode_from_impl(const char* const pch, const XMLSize_t srcCount) {
			assert(pch);

			                                                                                       
			XMLSize_t blockSize = 4*1024;
			if ( srcCount > blockSize ) {
			   const int nK = srcCount/1024 + 1;
			   blockSize = nK*1024;
			}
			
			XERCES_CPP_NAMESPACE_USE;
			XMLTransService::Codes failReason;
			XMLTranscoder* t = XMLPlatformUtils::fgTransService->makeNewTranscoderFor(
			   get_xml_enc(), failReason, blockSize);
			assert(t);
			
			const XMLSize_t maxChars = srcCount;
			XMLCh* toFill = new XMLCh[maxChars + 1];
			XMLSize_t bytesEaten = 0;
			unsigned char* charSizes = new unsigned char [maxChars];
			const XMLSize_t charsPut = t->transcodeFrom(
				(const XMLByte* const)pch,
				srcCount,
				toFill,
				maxChars,
				bytesEaten,
				charSizes);
			toFill[charsPut] = 0;
			
			delete[] charSizes;
			delete t;
			return toFill;
		}

		void xml_transcode_impl_release(XMLCh** pXMLCh) {
			if (pXMLCh != 0 && *pXMLCh != 0 ) {
				delete[] *pXMLCh;
				*pXMLCh = 0;
			}
		}

		void xml_transcode_impl_release_pch(char** pch) {
			if (pch != 0 && *pch != 0 ) {
				delete[] *pch;
				*pch = 0;
			}
		}

	}
#endif

	                                                                        
	class XMLChConverter : boost::noncopyable {
	public:
		XMLChConverter(const XMLCh* pXmlCh) :
#ifdef USE_TRANSCODEFROM_TO
		  m_pch( pXmlCh!=0 ? xml_transcode_to_impl(pXmlCh) : 0 ) {}
#else
		  m_pch( pXmlCh!=0 ? XERCES_CPP_NAMESPACE::XMLString::transcode(pXmlCh) : 0 ) {}
#endif
		~XMLChConverter(void) {
			if ( m_pch ) {
#ifdef USE_TRANSCODEFROM_TO
				xml_transcode_impl_release_pch(&m_pch);
#else
				XERCES_CPP_NAMESPACE::XMLString::release(&m_pch);
#endif
			}
		}

		std::string toString() const {
			return std::string( m_pch!=0 ? m_pch : "" );
		}

	private:
		char* m_pch;
	};

       
                                                                     
    
	class StringConverter : boost::noncopyable {

    public :
		StringConverter(const std::string& st) :
#ifdef USE_TRANSCODEFROM_TO
		   m_pXMLCh( xml_transcode_from_impl(st.c_str(), st.size()) ) { }
#else
		   m_pXMLCh( XERCES_CPP_NAMESPACE::XMLString::transcode(st.c_str()) ) { }
#endif

		StringConverter(const char* const pch) :
#ifdef USE_TRANSCODEFROM_TO
		   m_pXMLCh( pch!=0 ? xml_transcode_from_impl(pch, strlen(pch)) : 0 ) {}
#else
		   m_pXMLCh( pch!=0 ? XERCES_CPP_NAMESPACE::XMLString::transcode(pch) : 0 ) { }
#endif
        ~StringConverter(void)
		{
			                                         
			if ( m_pXMLCh ) {
#ifdef USE_TRANSCODEFROM_TO
				xml_transcode_impl_release(&m_pXMLCh);
#else
				XERCES_CPP_NAMESPACE::XMLString::release(&m_pXMLCh);
#endif
			}
        }
    
        const XMLCh* getXMLCh() const
        {
            return m_pXMLCh;
        }
    
    private :
        XMLCh* m_pXMLCh;
    };


	namespace {

		   
                                       
     
		inline std::string XMLChToString(const XMLCh* pXmlCh) {
			return XMLChConverter(pXmlCh).toString();
		}
	}

    #define X(str) StringConverter(str).getXMLCh()

}                 
}                   


#endif                           


