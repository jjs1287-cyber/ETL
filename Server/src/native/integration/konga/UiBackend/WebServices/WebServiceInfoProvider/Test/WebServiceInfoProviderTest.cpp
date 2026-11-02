#include <string>
#include <iostream>

#include <konga/UiBackend/WebServices/WebServiceInfoProvider/Impl/WebServiceInfoProviderImpl.h>
#include <konga/UiBackend/WebServices/WebServiceInfoProvider/WebServiceOperationData.hpp>

#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/TranDb/WsdlTab.h>

#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/CrException.h>

#include "../WebServiceJtr.hpp"
#include "../CWsdlFiles.hpp"
#include "../WebServiceOperationData.hpp"
#include "../WebServiceJtr.hpp"

using namespace konga;
using namespace std;

string stUser;
string stPasswd;

void testGetJtrs();

void testUploadWsdl();

void testGetWsdlFiles();

void testDownloadWsdl();

void testDeleteWsdl();

int g_argc = -1;
char** g_argv = 0;


int main(int argc, char* argv[])
{
	if ( argc < 3 ) {
		cout << "Usage: " << argv[0] << " user password [options]" << endl;
		return 1;
	}

	g_argc = argc;
	g_argv = argv;

	stUser = argv[1];
	stPasswd = argv[2];

	try {
                     
                       
		testGetJtrs();
                       
                     

		return 0;
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}
}

string GetWsdlGuid() {
	if ( g_argc < 4 ) {
		cout << "Usage: " << g_argv[0] << " user password WsdlGuid" << endl;
		exit(1);
	}
	return string(g_argv[3]);
}

void testDeleteWsdl() {
	const string& stGuid = GetWsdlGuid();

	WebServiceInfoProviderImpl impl(stUser.c_str(), stPasswd.c_str());
	impl.DeleteWsdl(stGuid.c_str(), false);
}

void testDownloadWsdl() {
	const string& stGuid = GetWsdlGuid();

	WebServiceInfoProviderImpl impl(stUser.c_str(), stPasswd.c_str());
	xsd__string str = impl.DownloadWsdl(stGuid.c_str());
	cout << str << endl;
	delete str;
}

void testGetWsdlFiles() {
	WebServiceInfoProviderImpl impl(stUser.c_str(), stPasswd.c_str());
	CWsdlFiles* retval = impl.GetWsdlFiles();
	delete retval;
}

void testUploadWsdl() {
	WebServiceInfoProviderImpl impl(stUser.c_str(), stPasswd.c_str());
	const string& stGuid = GetWsdlGuid();
	impl.UploadWsdl("", "http://greg.froh.ca/fun/random_bushism/soap/?wsdl", stGuid.c_str(), "");
	impl.UploadWsdl("", "http://greg.froh.ca/fun/random_bushism/soap/?wsdl", stGuid.c_str(), "");
}

void testGetJtrs() {
	const string& stGuid = GetWsdlGuid();

	WebServiceInfoProviderImpl impl(stUser.c_str(), stPasswd.c_str());
	WebServiceOperationData* pInput = new WebServiceOperationData();

	                     
                                               
                                       
                                                                  
                                            
                                          

	                
	pInput->setBindingName("CurrencyConvertorSoap", true);
	pInput->setBodyUse("literal", true);
	pInput->setGuid(const_cast<xsd__string>(stGuid.c_str()), true);
	pInput->setOperationName("ConversionRate", true);
	pInput->setSoapStyle("document", true);

	WebServiceJtr* retval = impl.GetJtrs(pInput);
	cout << retval->getInputJtr() << endl << endl;
	cout << retval->getOutputJtr() << endl << endl;

	delete retval;
	delete pInput;
}


