/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <vector>
#include <string>
#include <iostream>		       

#include <konga/Utils/util.h>
#include <konga/Utils/CrException.h>

#include <konga/Crypto/Crypto.h>

#include <konga/UiBackend/WebServices/Utils/WsUtils.h>

#include <konga/UiBackend/WebServices/CheckoutInterchangeData/InterchangeDataHolder.hpp>
#include <konga/UiBackend/WebServices/CheckoutInterchangeData/CInterchangeProjects.hpp>

#include <konga/UiBackend/WebServices/CheckoutInterchangeData/Impl/CheckoutInterchangeDataImpl.h>

using namespace konga;
using namespace konga::interchange_checkout;

void testEncrypt();
void testDecrypt();

int testDownload(int argc, char* argv[]);

int main(int argc, char* argv[])
{
	                            
	return testDownload(argc, argv);

                           
                 
                 
                                                               
}

int testDownload(int argc, char* argv[]) {
	                                             
	if ( argc < 5 ) {
		cout << "Usage: " << argv[0] << " user password projectId what_to_do" << endl;
		return 1;
	}
	const char* user = argv[1];
	const char* password = argv[2];
	const char* id = argv[3];
	const string what_to_do(argv[4]);

	try {
		if ( what_to_do == "1" ) {
			                                       
			CheckoutInterchangeDataImpl impl(user, md5(password).c_str());
			DownloadDataHolder* retVal = impl.DownloadIntegrationProject(id);
			delete retVal;
		}
	}
	catch( const CrException& ex ) {
		cerr << ex.GetErrorString() << endl;
		return -1;
	}

	return 0;
}

void testDecrypt() {
	const string stEncrypted("rp3FSKpquvpKygj46NCWSQ==");
	const string stDecrypted( DecryptString(stEncrypted) );
	cout << stDecrypted << endl;
}

void testEncrypt() {
	const int N = 1;
	for( int i=0; i < N; ++i ) {
		string stOriginal;
		stOriginal = "rp3FSKpquvpKygj46NCWSQ==";
                              
		const string stEncrypted( EncryptString(stOriginal) );
		const string stDecrypted( DecryptString(stEncrypted) );
		if ( stDecrypted != stOriginal ) {
			cout << "Original: " << stOriginal << endl;
			cout << "Encrypted: " << stEncrypted << endl;
			cout << "Decrypted: " << stDecrypted << endl << endl;
		}
	}
}

