                                                                                 
  

#include <iostream>		       

#include <konga/UiBackend/WebServices/XmlInfoProvider/Impl/XmlInfoProviderImpl.h>
#include <konga/UiBackend/WebServices/XmlInfoProvider/CXmlStructureFile.hpp>
                                                                                    
#include <konga/UiBackend/WebServices/XmlInfoProvider/CXmlStructureFileUploadResult.hpp>
#include <konga/UiBackend/WebServices/Utils/WsUtils.h>

using namespace konga;
using namespace std;

void test2();

void testUploadStructureFile();


extern void testCdtdParser();	                                    

string stUser;
string stPassword;

int main(int argc, char* argv[])
{
	if ( argc < 3 ) {
		cout << "Usage: " << argv[0] << " user password [options]" << endl;
		return 1;
	}
	stUser = argv[1];
	stPassword = argv[2];

	try {
                     

                              
	} catch( const CrException& ex ) { cout << ex.GetErrorString() << endl; return -1; }

	return 0;
}

