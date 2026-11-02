/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include "FileContents.h"
#include "CrException.h"
#include "AutoFileCleanup.h"
#include "ErrorResolver.h"
#include "EbcdicToAscii.h"

#ifndef WIN32
#include "PortDefs.h"	                                         
#endif

void konga::EbcdicToAscii(const std::string& ebcdicFileName, const std::string& asciiFileName) {
	                                                   
	                                             
	                                             

	                                    
	const unsigned char table[]={
		0x00,	                   
		0x01,	                   
		0x02,	                   
		0x03,	                   
		0xff,	                
		0x09,	                   
		0xff,	                
		0x7f,	                   
		0xff,	                
		0xff,	                
		0xff,	                
		0x0b,	                   
		0x0c,	                   
		0x0d,	                   
		0x0e,	                   
		0x0f,	                   
		0x10,	                   
		0x11,	                   
		0x12,	                   
		0x13,	                   
		0xff,	                
		0x85,	                                               
		0x08,	                                               
		0xff,	                
		0x18,	                   
		0x19,	                   
		0xff,	                
		0xff,	                
		0x1c,	                   
		0x1d,	                   
		0x1e,	                   
		0x1f,	                   
		0xff,	                
		0xff,	                
		0xff,	                                            
		0xff,	                
		0xff,	                
		0x0a,	                   
		0x17,	                   
		0x1b,	                   
		0xff,	               
		0xff,	               
		0xff,	               
		0xff,	               
		0xff,	               
		0x05,	                   
		0x06,	                   
		0x07,	                   
		0xff,	               
		0xff,	               
		0x16,	                   
		0xff,	               
		0xff,	               
		0xff,	               
		0xff,	               
		0x04,	                   
		0xff,	               
		0xff,	               
		0xff,	               
		0xff,	               
		0x14,	                   
		0x15,	                   
		0xff,	               
		0x1a,	                   
		0x20,	                   
		0xa0,	                         
		0xe2,	                   
		0xe4,	                   
		0xe0,	                   
		0xe1,	                   
		0xe3,	                   
		0xe5,	                   
		0xe7,	                   
		0xf0,	                   
		0xa2,	                   
		0x2e,	                   
		0x3c,	                   
		0x28,	                   
		0x2b,	                   
		0x7c,	                   
		0x26,	                   
		0xe9,	                   
		0xea,	                   
		0xeb,	                   
		0xe8,	                   
		0xed,	                   
		0xee,	                   
		0xef,	                   
		0xec,	                   
		0xdf,	                   
		0x21,	                   
		0x24,	                   
		0x2a,	                   
		0x29,	                   
		0x3b,	                   
		0x5e,	                   
		0x2d,	                   
		0x2f,	                   
		0xc2,	                   
		0xc4,	                   
		0xc0,	                    
		0xc1,	                    
		0xc3,	                    
		0xc5,	                    
		0xc7,	                    
		0xd1,	                    
		0xa6,	                    
		0x2c,	                    
		0x25,	                    
		0x5f,	                    
		0x3e,	                    
		0x3f,	                    
		0xf8,	                    
		0xc9,	                    
		0xca,	                    
		0xcb,	                    
		0xc8,	                    
		0xcd,	                    
		0xce,	                    
		0xcf,	                    
		0xcc,	                    
		0x60,	                              
		0x3a,	                    
		0x23,	                    
		0x40,	                    
		0x27,	                    
		0x3d,	                    
		0x22,	                    
		0xd8,	                    
		0x61,	                    
		0x62,	                    
		0x63,	                    
		0x64,	                    
		0x65,	                    
		0x66,	                    
		0x67,	                    
		0x68,	                    
		0x69,	                    
		0xab,	                    
		0xbb,	                    
		0xf0,	                            
		0x0d,	                    
		0xde,	                    
		0xb1,	                    
		0xb0,	                    
		0x6a,	                    
		0x6b,	                    
		0x6c,	                    
		0x6d,	                    
		0x6e,	                    
		0x6f,	                    
		0x70,	                    
		0x71,	                    
		0x72,	                    
		0xaa,	                    
		0xba,	                    
		0xe6,	                    
		0xb8,	                    
		0xc6,	                    
		0xa4,	                    
		0xb5,	                    
		0x7e,	                    
		0x73,	                    
		0x74,	                    
		0x75,	                    
		0x76,	                    
		0x77,	                    
		0x78,	                    
		0x79,	                    
		0x7a,	                    
		0xa1,	                    
		0xbf,	                    
		0xd0,	                    
		0x5b,	                    
		0xfe,	                    
		0xae,	                    
		0xac,	                    
		0xa3,	                    
		0xa5,	                    
		0x95,	                    
		0xa9,	                    
		0xa7,	                    
		0xb6,	                    
		0xbc,	                    
		0xbd,	                    
		0xbe,	                    
		0xdd,	                    
		0xa8,	                    
		0xaf,	                    
		0x5d,	                    
		0x92,	                    
		0xd7,	                    
		0x7b,	                    
		0x41,	                    
		0x42,	                    
		0x43,	                    
		0x44,	                    
		0x45,	                    
		0x46,	                    
		0x47,	                    
		0x48,	                    
		0x49,	                    
		0xad,	                              
		0xf4,	                    
		0xf6,	                    
		0xf2,	                    
		0xf3,	                    
		0xf5,	                    
		0x7d,	                    
		0x4a,	                    
		0x4b,	                    
		0x4c,	                    
		0x4d,	                    
		0x4e,	                    
		0x4f,	                    
		0x50,	                    
		0x51,	                    
		0x52,	                    
		0xb9,	                    
		0xfb,	                    
		0xfc,	                    
		0xf9,	                    
		0xfa,	                    
		0xff,	                    
		0x5c,	                    
		0xf7,	                    
		0x53,	                    
		0x54,	                    
		0x55,	                    
		0x56,	                    
		0x57,	                    
		0x58,	                    
		0x59,	                    
		0x5a,	                    
		0xb2,	                    
		0xd4,	                    
		0xd6,	                    
		0xd2,	                    
		0xd3,	                    
		0xd5,	                    
		0x30,	                    
		0x31,	                    
		0x32,	                    
		0x33,	                    
		0x34,	                    
		0x35,	                    
		0x36,	                    
		0x37,	                    
		0x38,	                    
		0x39,	                    
		0xb3,	                    
		0xdb,	                    
		0xdc,	                    
		0xd9,	                    
		0xda,	                    
		0xff	                 
	};

#ifdef _DEBUG
	long long population[256];
	int rt[256];
	for(int i=0; i<256; ++i){
		population[i]=0;
		rt[i]=-1;
	}

	                                      
	for(int ebcdic=0; ebcdic<256; ++ebcdic){
		unsigned char ascii=table[ebcdic];
		if(ascii==0xff)continue;
		if(rt[ascii]>=0){
			printf("more than 1 numbers in ebcdic table for ascii=%d, with ebcdic=%d, %d\n", ascii, rt[ascii], ebcdic);
		}
		else {
			rt[ascii]=ebcdic;
		}
	}
#endif

	konga::FileContents fc(ebcdicFileName);

	FILE* out=fopen(asciiFileName.c_str(), "w+b");
	if(out==0) {
		const long lErr=GetLastError();
		const std::string sErr("Failed to open output file '" + asciiFileName + "'.\nReason: " + ErrorResolver(lErr).Resolve(lErr));
		throw konga::CrException(sErr);
	}
	konga::AutoFileHandle adopt_out(out);

	for(int i=0, n=fc.size(); i<n; ++i){
		unsigned char c=fc.at(i);
		unsigned char k=table[c];
		fputc(k, out);
#ifdef _DEBUG
		population[c]++;
#endif
	}

#ifdef _DEBUG
	                  
	printf("EBCDIC        ASCII          population\n");
	char s[2]={0,0};
	for(int i=0; i<256; ++i){
		if(table[i]!=0xff){
			unsigned char c=table[i];
			s[0]=c;
			const char* ss=(c=='\r')?"\\r":(c=='\n'?"\\n":s);
			printf("%3d [0x%02x]    [0x%02x] '%s'     %lld\n", i, i, c, ss, population[i]);
		}
		else
			printf("%3d [0x%02x]    -1\n", i, i);
	}
#endif

}

