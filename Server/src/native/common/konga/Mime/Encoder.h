/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_ENCODER_H
#define KONGA_ENCODER_H

#include <string>

namespace konga {
	                                                                                        
	class Encoder{
	public:
		Encoder(const std::string& EncodeMe);
		Encoder(const std::wstring& EncodeMe);
		Encoder(char* buf, long len);
		Encoder(wchar_t* buf, long len);
		~Encoder();
		int CBase64();
		int EncodeUTF7();
		std::string GetEncodedString(){ return m_Encoded; }
	
	private:
		std::string m_AsciiMessage, m_Encoded;
		std::wstring m_UnicodeMessage;
		bool m_IsUnicode;
		char* m_AsciiCharMessage;
		wchar_t* m_UnicodeCharMessage;
		long m_MessageLen;
	};
}
#endif                   

