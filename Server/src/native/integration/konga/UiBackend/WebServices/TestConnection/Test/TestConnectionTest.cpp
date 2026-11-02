#include <iostream>
#include <string>
#include <vector>

#include <konga/UiBackend/WebServices/Utils/WsUtils.h>


#include <konga/UiBackend/WebServices/TestConnection/Impl/TestConnectionImpl.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>


using namespace konga;
using namespace std;

string stUser;
string stPassword;

string stLocationUser;
string stLocationPassword;

void testFileShareConnection();

void testDbConnection();

int testFtpSource();

int main(int argc, char* argv[])
{
	if ( argc < 5 ) {
		cout << "Usage: " << argv[0] << " user password location_user location_password" << endl;
		return 1;
	}
	stUser = argv[1];
	stPassword = argv[2];
	stLocationUser = argv[3];
	stLocationPassword = argv[4];

	try {
		return testFtpSource();
	}
	catch( const CrException& e ) {
		cout << e.GetErrorString() << endl;
		return 1;
	}

                      
	return 0;
}

int testFtpSource() {
	TestConnectionImpl impl(stUser.c_str(), stPassword.c_str(), true, "496c2391-0ad2-4887-80cc-62233208c147");

	vector<string> vKeys;
	vector<string> vValues;

	vKeys.push_back("source_type_id");
	vValues.push_back("1");

	vKeys.push_back("locator");
	vValues.push_back("linuxdoc");

	vKeys.push_back("path");
	vValues.push_back("source/emanuel/SimpleText");

	vKeys.push_back("file_name");
	vValues.push_back("*.*");

	vKeys.push_back("login");
	vValues.push_back(stLocationUser);

	vKeys.push_back("password");
	vValues.push_back(stLocationPassword);

	vKeys.push_back("port");
	vValues.push_back("21");

	ConnectionInfo* pConnectionInfo = new ConnectionInfo();
	pConnectionInfo->Keys = new ArrayOf_xsd_string();
	pConnectionInfo->KeyValues = new ArrayOf_xsd_string();

	allocate_xsd__string_Array(vKeys, *(pConnectionInfo->Keys->item));
	allocate_xsd__string_Array(vValues, *(pConnectionInfo->KeyValues->item));

	FileList* pFileList = impl.TestFileTypeConnection(pConnectionInfo);
	int n = -1;
	const xsd__string* arrFiles = pFileList->Files->item->get(n);
	for( int i = 0; i < n; ++i ) {
		cout << arrFiles[i] << endl;
	}
	delete pFileList;

	delete pConnectionInfo;
	return 0;
}

  
                                

                      
                        

                                                        
                                                  
                                                       
                                 
                                     
                                                                                           
 


                         
      
                                                                                                                
                                                                                                                     
                                                                      
                                        
  
                                 
                                      
  
 

  
