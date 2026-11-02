/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <fstream>
#include <iostream>
#include <string>

#include <stdio.h>

#include <cassert>

#include <konga/Utils/CrException.h>
#include <konga/Utils/util.h>

#include <konga/Crypto/Crypto.h>

using namespace konga;
using namespace std;

void test1();
void testEncryptPwd();

void testBase64();

void testBigBase64();

void testEncryptString();

int testMD5();

int testMD5_2();

                          

int main (int argc, char *argv[])
{
	try {
                     
                 
                    
                        
              
		testMD5_2();
                       
	} catch(CrException& e) {
		cerr << e.GetErrorString();
		return 1;
	}
	return 0;
}

namespace {
	string ll_to_hex(long long ll) {
		unsigned char* p = (unsigned char*)(&ll);
		char buf[10];
		string test;
		for( int i = 0; i < 8; ++i ) {
			sprintf(buf, "%02x", p[i]);
			test += buf;
		}
		return test;
	}
}

int testMD5_2() {
	const string text("Sex laxar i en laxask");
	const string md5_correct("831712b4f8fc922f381fd36c3c834b1f");

	if(konga::md5(text) != md5_correct) throw CrException("MD5 is broken!");

	long long high, low;
	konga::md5(text, low, high);

	const string test(ll_to_hex(high) + ll_to_hex(low));
	if( test != md5_correct ) throw CrException("MD5 to 64-bit unsigned integer is broken!");



	  
                        
                                     
                                         
              
             
                                
                                 
              
  
   

  
                          
                              
              
  
  
                            
                                
                
    

	return 0;
}

int testMD5() {
	for( int i = 0; i < 100; ++i ) {
		const string sMD5(md5(GetGUIDString()));
		cout << sMD5.length() <<  ": " << sMD5 << endl;
		assert(IsMD5(sMD5));
		if ( !IsMD5(sMD5) )
			cout << sMD5 << " is not an MD5??" << endl;
	}

	assert(IsMD5(md5("This is just a string")));

	if ( !IsMD5("335c421b2a3a24b8a53e592be4e07a62") )
		cout << "error" << endl;
	if ( !IsMD5("a4985d20c9aeaacc11da10368cb6569b") )
		cout << "error" << endl;
	if ( IsMD5("498g5d20c9aeaacc11da10368cb6569b") )
		cout << "error" << endl;
	if ( IsMD5("335c421b2a3a2b8a53e592be4e07a62") )
		cout << "error" << endl;

	return 0;
}

void testEncryptString() {
	string stPlainText = "plain string";
	string stEncrypted = EncryptString(stPlainText);
	cout << stEncrypted << endl;
}

void testBigBase64() {

	                                                     
	string stFile("C:\\tmp\\base64.txt");

                                                         
	FILE* f = fopen(stFile.c_str(), "r");
	if ( f == 0 ) {
		cerr << "Failed to open file." << endl;
		return;
	}

	string stContents;
	char buf[1025];
	size_t chRead = 0;
	while ( (chRead=fread(buf, 1, 1024, f)) > 0 ) {
		buf[chRead] = 0;
		stContents += buf;
	}
	fclose(f);

	int N = stContents.size();
	unsigned char* pchToDecode = new unsigned char[N];
	for( int i = 0; i < N; ++i ) {
		pchToDecode[i] = stContents.at(i);
	}
	string stEncoded( EncodeBase64(pchToDecode, N) );

	int len = 0;
	unsigned char* pchDecoded = DecodeBase64(stEncoded, len);

	if ( len != N ) {
		cerr << "Inconsistent length of decoded result" << endl;
		return;
	}

	if ( memcmp(pchDecoded, pchToDecode, N) != 0 ) {
		cerr << "Original data and decoded data are different!!" << endl;
	}
}


void testBase64() {

	string st("Some stuff to encode");

	int N = st.size();
	unsigned char* pch = new unsigned char[st.size()];
	for( int i = 0; i < N; ++i ) {
		pch[i] = st.at(i);
	}
	string stEncoded = EncodeBase64(pch, N);
	int len = 0;
	unsigned char* pchDecoded = DecodeBase64(stEncoded, len);
	if ( len != N )
		cout << "Inconsistent length!!" << endl;
	if ( memcmp(pch, pchDecoded, N ) != 0 )
		cout << "Decoded is different from the original!!" << endl;
}

void testEncryptPwd() {
	string stPwd("x");
	cout << EncryptString(stPwd) << endl;
}

void test1() {
	string stText("sometext");
	string stEncoded( EncryptString(stText) );
	cout << "Encoded text: " << stEncoded << endl;

	string stDecoded( DecryptString(stEncoded) );

	if ( stText != stDecoded ) {
		cout << "Some problem here... " << stText << " != " << stDecoded << endl;
	}
}

char* load_file(istream& is, streamsize& len)
{
	is.seekg (0, ios::end);
	len = is.tellg();
	is.seekg (0, ios::beg);
	char* pBuf = new char[len];
	is.read(pBuf, len);
	return pBuf;
}

  
                         
                             

                                                                           
                                                                                                                                        

                                             
                                                   
          
                                                                          
                
                                                             
                                           
            

                                                                                                                    
 
  