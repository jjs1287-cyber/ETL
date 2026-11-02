/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_STRING_ENCODING_CONVERSION_UTF8_CONV_H
#define KONGA_STRING_ENCODING_CONVERSION_UTF8_CONV_H

#include <string>
#include <vector>
#include <map>

#include <unicode/utf.h>	                

struct UConverter;

namespace konga {

	                                     
	class SupportedEncoding {
	public:
		SupportedEncoding(const std::string canonical_name, const std::vector<std::string> aliases);

		std::string canonical_name;
		std::vector<std::string> aliases;
	};

	std::vector<SupportedEncoding> GetSupportedEncodings();

	class Utf8Conv {

	public:

		                                                                                               
		                                                
		                                                                
		                                                                                 
		static Utf8Conv* createEncoderFor(const std::string& encoding);

	private:
		Utf8Conv(UConverter* c);

	public:
		Utf8Conv();
		~Utf8Conv();

		                                                             
		                                                                         
		                                  
		std::string from(const std::string& other_string);

		                                                               
		                                  
		std::string from(const std::string& encoding, const std::string& other_string);

		                                     
		std::string fromW(const std::wstring& other_string);

		                                                                         
		                                                                         
		std::string to(const std::string& utf8_string);

		                                                          
		std::string to(const std::string& encoding, const std::string& utf8_string);

		                                     
		std::wstring toW(const std::string& utf8_string);

		                                                                                                                                          
		                                                                                                    
		                                                                  
		std::string toNative_(const std::string& utf8_string);

		std::string fromNative_(const std::string& native);

	private:
		void init();
		void clear();

		UConverter* getConverter(const std::string& encoding);

		void allocateUCharBuf(size_t size);
		void allocateCharBuf(size_t size);

		std::string impl(UConverter* cnv_from, UConverter* cnv_to, const std::string& s, short bufHint = 2);

	private:
		UChar* m_uchar_buf;
		size_t m_uchar_buf_capacity;

		char* m_char_buf;
		size_t m_char_buf_capacity;

		UConverter *m_default_cnv, *m_utf8_cnv;

		typedef std::map<std::string, UConverter*> ConvMapT;
		ConvMapT m_converters;
	};
}

#endif                                                

