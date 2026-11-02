/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                        

#include <konga/Utils/util.h>

#include "Encoder.h"

using namespace konga;

Encoder::Encoder(const std::string& EncodeMe)
	: m_AsciiMessage(EncodeMe), m_IsUnicode(false), m_AsciiCharMessage(0), m_UnicodeCharMessage(0), m_MessageLen(0) { }

Encoder::Encoder(const std::wstring& EncodeMe)
	: m_UnicodeMessage(EncodeMe), m_IsUnicode(true), m_AsciiCharMessage(0), m_UnicodeCharMessage(0), m_MessageLen(0) { }

Encoder::Encoder(char* buf, long len)
	: m_IsUnicode(true), m_AsciiCharMessage(buf), m_UnicodeCharMessage(0), m_MessageLen(len) { }

Encoder::Encoder(wchar_t* buf, long len)
	: m_IsUnicode(true), m_AsciiCharMessage(0), m_UnicodeCharMessage(buf), m_MessageLen(len) { }

Encoder::~Encoder() { }

int Encoder::CBase64()
{
	if(m_IsUnicode) return 1;
                          
	const char base64code[]="ABCDEFGHIJKLMNOPQRSTUVWXYZ"
		                    "abcdefghijklmnopqrstuvwxyz"
							"0123456789+/";
	#define ENCODE_BYTE(b) base64code[((unsigned char)b)&077];
	const int byte_per_line = 54;
	                                     
	char chr[5];
	chr[4] = 0;
	int k ,ibyte, k2;
	if(m_AsciiCharMessage == 0){
		k = m_AsciiMessage.length();
		m_AsciiCharMessage = new char[k + 1];
		strcpy(m_AsciiCharMessage, m_AsciiMessage.c_str());
	}
	else
		k = m_MessageLen;
	std::string EncodedString;
	ibyte=0;
	k2=k-2;
	while(ibyte<k2){
		chr[0]=ENCODE_BYTE(m_AsciiCharMessage[ibyte]>>2);
		chr[1]=ENCODE_BYTE(((m_AsciiCharMessage[ibyte]&3)<<4)|((m_AsciiCharMessage[ibyte+1]&240)>>4));
		chr[2]=ENCODE_BYTE(((m_AsciiCharMessage[ibyte+1]&15)<<2)|((m_AsciiCharMessage[ibyte+2]&192)>>6));
		chr[3]=ENCODE_BYTE(m_AsciiCharMessage[ibyte+2]);
		EncodedString += chr;
		ibyte+=3;
		if((ibyte%byte_per_line)==0){
			EncodedString += "\n";
		}
	}
	if(ibyte<k){
		                    
		chr[0]=ENCODE_BYTE(m_AsciiCharMessage[ibyte]>>2);
		if((ibyte + 1) == k){
			chr[1]=ENCODE_BYTE((m_AsciiCharMessage[ibyte]&3)<<4);
			chr[2]='=';
		}
		else{
			chr[1]=ENCODE_BYTE(((m_AsciiCharMessage[ibyte]&3)<<4)|((m_AsciiCharMessage[ibyte+1]&240)>>4));
			chr[2]=ENCODE_BYTE(((m_AsciiCharMessage[ibyte+1]&15)<<2));
		}
		chr[3]='=';
		EncodedString += chr;
	}
	EncodedString += "\n";
	m_Encoded = EncodedString;
	if(m_MessageLen == 0)
		delete[] m_AsciiCharMessage;
	return 0;
}

int Encoder::EncodeUTF7()
{
	if(!m_IsUnicode) return 1;
	const int byte_per_line = 54;

	const char base64code[]="ABCDEFGHIJKLMNOPQRSTUVWXYZ"
		                    "abcdefghijklmnopqrstuvwxyz"
							"0123456789+/";
	#define ENCODE_BYTE(b) base64code[((unsigned char)b)&077];

	int buffer_length;
	if(m_UnicodeCharMessage == 0){
		buffer_length = m_UnicodeMessage.length();
		m_UnicodeCharMessage = new wchar_t[buffer_length + 1];
		wcscpy(m_UnicodeCharMessage, m_UnicodeMessage.c_str());
	}
	else
		buffer_length = m_MessageLen;

	wchar_t *tmpbuf1 = new wchar_t[2 * buffer_length + 10];
	char *tmpbuf = new char[4 * buffer_length + 20];
	char *cbuf = new char[3*buffer_length];
	int count = 0;
	int tmpcount = 0;
	int NoOfLines = 1;
	std::string EncodedString;
	                    
	memset(cbuf, 0, 3 * buffer_length);
	int i = 0;
	count = 0;
	while(i < buffer_length){
		if(((m_UnicodeCharMessage[i] >= 'A') && (m_UnicodeCharMessage[i] <= 'Z')) 
			|| ((m_UnicodeCharMessage[i] >= 'a') && (m_UnicodeCharMessage[i] <= 'z')) 
			|| ((m_UnicodeCharMessage[i] >= '0') && (m_UnicodeCharMessage[i] <= '9')) 
			|| ((m_UnicodeCharMessage[i] >= 39) && (m_UnicodeCharMessage[i] <= 41))
			|| ((m_UnicodeCharMessage[i] >= 44) && (m_UnicodeCharMessage[i] <= 47)) 
			|| (m_UnicodeCharMessage[i] == ':') || (m_UnicodeCharMessage[i] == '?')
			|| (m_UnicodeCharMessage[i] == '\r') || (m_UnicodeCharMessage[i] == '\n') 
			|| (m_UnicodeCharMessage[i] == '\t') || (m_UnicodeCharMessage[i] == ' '))
		{
			cbuf[count++] = static_cast<char>(m_UnicodeCharMessage[i++]);
			if((NoOfLines * byte_per_line) <= count){
				cbuf[count++] = '\r';
				cbuf[count++] = '\n';
				NoOfLines++;
			}
		}
		else{
			cbuf[count++] = '+';
			while((tmpcount < 2 * buffer_length) && (i < buffer_length) && 
				!(((m_UnicodeCharMessage[i] >= 'A') && (m_UnicodeCharMessage[i] <= 'Z')) 
			|| ((m_UnicodeCharMessage[i] >= 'a') && (m_UnicodeCharMessage[i] <= 'z')) 
			|| ((m_UnicodeCharMessage[i] >= '0') && (m_UnicodeCharMessage[i] <= '9')) 
			|| ((m_UnicodeCharMessage[i] >= 39) && (m_UnicodeCharMessage[i] <= 41))
			|| ((m_UnicodeCharMessage[i] >= 44) && (m_UnicodeCharMessage[i] <= 47)) 
			|| (m_UnicodeCharMessage[i] == ':') || (m_UnicodeCharMessage[i] == '?')
			|| (m_UnicodeCharMessage[i] == '\r') || (m_UnicodeCharMessage[i] == '\n') 
			|| (m_UnicodeCharMessage[i] == '\t') || (m_UnicodeCharMessage[i] == ' ')))
			{
				tmpbuf1[tmpcount++] = m_UnicodeCharMessage[i++];
			}
			memcpy(tmpbuf, tmpbuf1, sizeof(wchar_t) * tmpcount);
			char chr[5];
			chr[4] = 0;
			int ibyte,k2;
			ibyte = 0;
			k2 = tmpcount*2 - 2;
#ifdef __INTEL__
			for(int a = 0; a < tmpcount*2; a+=2){
				int tmp = tmpbuf[a];
				tmpbuf[a] = tmpbuf[a+1];
				tmpbuf[a+1] = tmp;
			}
#endif
			while(ibyte < k2){
				chr[0]=ENCODE_BYTE(tmpbuf[ibyte]>>2);
				chr[1]=ENCODE_BYTE(((tmpbuf[ibyte]&3)<<4)|((tmpbuf[ibyte+1]&240)>>4));
				chr[2]=ENCODE_BYTE(((tmpbuf[ibyte+1]&15)<<2)|((tmpbuf[ibyte+2]&192)>>6));
				chr[3]=ENCODE_BYTE(tmpbuf[ibyte+2]);
				strcat(cbuf, chr);
				count += 4;
				ibyte += 3;
				if((NoOfLines * byte_per_line) <= count){
					if(ibyte != tmpcount * 2){
						cbuf[count++] = '\r';
						cbuf[count++] = '\n';
						cbuf[count++] = '+';
						NoOfLines++;
					}
				}
			}
			if(ibyte < tmpcount*2){
                                                      
				if(tmpcount*2 - ibyte == 1){
					cbuf[count++] = ENCODE_BYTE(tmpbuf[ibyte] >> 2);
					cbuf[count++] = ENCODE_BYTE(tmpbuf[ibyte] << 4);
				}
				else{
					cbuf[count++] = ENCODE_BYTE(tmpbuf[ibyte] >> 2);
					cbuf[count++] = ENCODE_BYTE(((tmpbuf[ibyte]&3) << 4) | ((tmpbuf[ibyte+1]&240) >> 4));
					cbuf[count++] = ENCODE_BYTE((tmpbuf[ibyte+1]&15) << 2);
				}
			}
			tmpcount = 0;
			cbuf[count++] = '-';
		}
	}
	EncodedString += cbuf;
	EncodedString += "\n";
	m_Encoded = EncodedString;
	delete[] cbuf;
	delete[] tmpbuf;
	delete[] tmpbuf1;
	if(m_MessageLen == 0)
		delete[] m_UnicodeCharMessage;
	return 0;
}

