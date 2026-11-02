/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

                                              
                           

#include <unicode/ucnv.h>
#include <unicode/ustring.h>	                            

#include <boost/scoped_array.hpp>

#include <konga/Utils/CrException.h>

#include "Utf8Conv.h"

using namespace konga;
using std::string;
using std::wstring;
using std::vector;
using std::map;

SupportedEncoding::SupportedEncoding(const std::string canonical_name_, const std::vector<std::string> aliases_)
: canonical_name(canonical_name_), aliases(aliases_) {}

std::vector<SupportedEncoding> konga::GetSupportedEncodings() {
	std::vector<SupportedEncoding> retval;
	const int nNames = ucnv_countAvailable();
	for( int i = 0; i < nNames; ++i ) {
		const char* conv = ucnv_getAvailableName(i);
		if ( conv != 0 && *conv != 0 ) {
			const string canonical_name(conv);
			vector<string> vAliases;
			UErrorCode err = U_ZERO_ERROR;
			const int nAliases = ucnv_countAliases(conv, &err);
			if( nAliases > 0 && !U_FAILURE(err) ) {
				char** aliases = new char*[nAliases];
				ucnv_getAliases(conv, (const char**)aliases, &err);
				if( !U_FAILURE(err) ) {
					for( int j = 0; j < nAliases; ++j ) {
						const char* alias = aliases[j];
						if( alias != 0 && strcmp(alias, conv) != 0 ) {
							vAliases.push_back( string(alias) );
						}
					}
				}
				delete[] aliases;
			}
			retval.push_back( SupportedEncoding(canonical_name, vAliases) );
		}
		else {
			printf("No converter available for %d", i);
		}
	}

	return retval;
}

namespace {
	const string CANONICAL_UTF_8_ENC_NAME("UTF-8");
#ifdef WIN32
	const string CANONICAL_SYSTEM_ENC_NAME("windows-1252");
#else
	const string CANONICAL_SYSTEM_ENC_NAME("ISO-8859-1");
#endif
}

namespace {
	UConverter* getConverterImpl(const string& encoding) {
		                           
		UErrorCode err = U_ZERO_ERROR;
		UConverter* conv = ucnv_open(encoding.c_str(), &err);
		if ( U_FAILURE(err) ) {
			throw CrException("Failed to find converter for character encoding '" + encoding + string("'. ICU Error Code: ") + u_errorName(err));
		}
		if ( conv == 0 ) {
			                          
			throw InternalErrorException("Failed to find converter for character encoding '" + encoding + "'. ucnv_open returned 0 but no error code was reported.");
		}
		return conv;
	}
}

Utf8Conv* Utf8Conv::createEncoderFor(const std::string& encoding_in) {
	string encoding(encoding_in);
	if ( encoding.empty() ) encoding = CANONICAL_SYSTEM_ENC_NAME;

	UConverter* conv = getConverterImpl(encoding);
	const UConverterType type = ucnv_getType(conv);
	if ( type == UCNV_UTF8 ) {
		ucnv_close(conv);
		return 0;
	}
	return new Utf8Conv(conv);
}

Utf8Conv::Utf8Conv() :
m_uchar_buf(0), m_uchar_buf_capacity(0), m_char_buf(0), m_char_buf_capacity(0), m_default_cnv(0), m_utf8_cnv(0) {
	init();
}

Utf8Conv::Utf8Conv(UConverter* c) :
m_uchar_buf(0), m_uchar_buf_capacity(0), m_char_buf(0), m_char_buf_capacity(0), m_default_cnv(c), m_utf8_cnv(0) {
	init();
}


Utf8Conv::~Utf8Conv() {
	clear();
}

void Utf8Conv::init() {
	m_utf8_cnv = getConverterImpl(CANONICAL_UTF_8_ENC_NAME);
}

void Utf8Conv::clear() {
	delete[] m_uchar_buf;
	m_uchar_buf = 0;
	m_uchar_buf_capacity = 0;

	delete[] m_char_buf;
	m_char_buf = 0;
	m_char_buf_capacity = 0;

	if ( m_default_cnv ) {
		ucnv_close(m_default_cnv);
		m_default_cnv = 0;
	}
	if ( m_utf8_cnv ) {
		ucnv_close(m_utf8_cnv);
		m_default_cnv = 0;
	}

	for( ConvMapT::iterator it = m_converters.begin(); it != m_converters.end(); ++it ) {
		ucnv_close(it->second);
	}
	m_converters.clear();
}

UConverter* Utf8Conv::getConverter(const string& encoding) {
	ConvMapT::const_iterator iconv = m_converters.find(encoding);
	if ( iconv != m_converters.end() ) return iconv->second;

	UConverter* conv = getConverterImpl(encoding);

	m_converters.insert(ConvMapT::value_type(encoding, conv));
	return conv;
}

void Utf8Conv::allocateUCharBuf(size_t size) {
	if ( size <= m_uchar_buf_capacity ) return;

	                    
	if ( m_uchar_buf != 0 ) {
		delete[] m_uchar_buf;
		m_uchar_buf = 0;
	}

	m_uchar_buf = new UChar[size];
	m_uchar_buf_capacity = size;
}

void Utf8Conv::allocateCharBuf(size_t size) {
	if ( size <= m_char_buf_capacity ) return;

	                    
	if ( m_char_buf != 0 ) {
		delete[] m_char_buf;
		m_char_buf = 0;
	}

	m_char_buf = new char[size];
	m_char_buf_capacity = size;
}

                                                                                                      
                                              
string Utf8Conv::impl(UConverter* cnv_from, UConverter* cnv_to, const string& s, short bufHint) {
	if ( s.empty() ) return string("");

	allocateUCharBuf(bufHint*s.length());
	UErrorCode err = U_ZERO_ERROR;
	const int ucharLen = ucnv_toUChars(cnv_from, m_uchar_buf, m_uchar_buf_capacity, s.data(), static_cast<int32_t>(s.size()), &err);
	if ( U_FAILURE(err) ) {
		throw CrException(string("Encoding conversion failed. ICU Error Code: ") + u_errorName(err));
	}

	allocateCharBuf(UCNV_GET_MAX_BYTES_FOR_STRING(ucharLen, ucnv_getMaxCharSize(cnv_to)));
	const int byteLen = ucnv_fromUChars(cnv_to, m_char_buf, m_char_buf_capacity, m_uchar_buf, ucharLen, &err);
	if ( U_FAILURE(err) ) {
		throw CrException(string("Encoding conversion failed. ICU Error Code: ") + u_errorName(err));
	}
	return string(m_char_buf, byteLen);
}

  
                                                                                             
                                    

                                      

                                      

                               
                                                                                                 
                        
                                                                                                                   
  

                        
                                                                                    
                                                                                    
                        
                                                                                                                       
  
                           
 
  

string Utf8Conv::fromW(const wstring& wstr) {
	if ( wstr.empty() ) return string("");

	                           
	int destLength = 0;
	UErrorCode err = U_ZERO_ERROR;
	u_strFromWCS(0, 0, &destLength, wstr.c_str(), wstr.length(), &err);
	err = U_ZERO_ERROR;	                                                      
	if ( destLength == 0 ) return string("");

	const int destCapacity = destLength + 1;
	boost::scoped_array<UChar> ucharBuf( new UChar[destCapacity] );
	u_strFromWCS(ucharBuf.get(), destCapacity, &destLength, wstr.c_str(), wstr.length(), &err);

	                         
	UConverter* cnv = m_utf8_cnv;

	allocateCharBuf(UCNV_GET_MAX_BYTES_FOR_STRING(destLength, ucnv_getMaxCharSize(cnv)));
	ucnv_fromUChars(cnv, m_char_buf, m_char_buf_capacity, ucharBuf.get(), destLength, &err);
	ucharBuf.reset();

	if ( U_FAILURE(err) ) {
		throw CrException(string("Converting UTF-16 to UTF-8 failed. ICU Error Code: ") + u_errorName(err));
	}
	return string(m_char_buf);
}

wstring Utf8Conv::toW(const string& utf8_string) {
	if ( utf8_string.empty() ) return wstring(L"");

	                          
	allocateUCharBuf(utf8_string.length());
	UConverter* cnv = m_utf8_cnv;

	UErrorCode err = U_ZERO_ERROR;
	const int ucharLen = ucnv_toUChars(cnv, m_uchar_buf, m_uchar_buf_capacity, utf8_string.c_str(), utf8_string.length(), &err);
	if ( U_FAILURE(err) ) {
		throw CrException(string("Converting from UTF-8 to UTF-16 failed. ICU Error Code: ") + u_errorName(err));
	}

	                           
	boost::scoped_array<wchar_t> dest( new wchar_t[ucharLen + 1] );
	int destLength = 0;
	u_strToWCS(dest.get(), ucharLen + 1, &destLength, m_uchar_buf, ucharLen, &err);
	if ( U_FAILURE(err) ) {
		throw CrException(string("Converting from UTF-8 to UTF-16 failed. ICU Error Code: ") + u_errorName(err));
	}
	return wstring(dest.get());
}

std::string Utf8Conv::from(const std::string& other_string) {
	if ( m_default_cnv == 0 ) return other_string;
	return impl(m_default_cnv, m_utf8_cnv, other_string);
}

string Utf8Conv::from(const string& encoding, const string& other_string) {
	return impl(getConverter(encoding), m_utf8_cnv, other_string);
}

string Utf8Conv::to(const string& utf8_string) {
	if ( m_default_cnv == 0 ) return utf8_string;
	return impl(m_utf8_cnv, m_default_cnv, utf8_string, 1);
}

string Utf8Conv::to(const string& encoding, const string& utf8_string) {
	return impl(m_utf8_cnv, getConverter(encoding), utf8_string, 1);
}

string Utf8Conv::toNative_(const string& utf8_string) {
	return impl(m_utf8_cnv, getConverter(CANONICAL_SYSTEM_ENC_NAME), utf8_string, 1);
}

string Utf8Conv::fromNative_(const std::string& native) {
	return impl(getConverter(CANONICAL_SYSTEM_ENC_NAME), m_utf8_cnv, native, 1);
}
