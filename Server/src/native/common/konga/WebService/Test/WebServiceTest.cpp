/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <iostream>
#ifdef WIN32
#include <tchar.h>
#endif WIN32

#include <konga/WebService/SimpleWebServiceClientSoap11.h>

using namespace std;
using namespace konga;

int testCallWebService();

#ifdef WIN32
int _tmain(int argc, _TCHAR* argv[])
#else
int main (int argc, char *argv[])
#endif
{
	return testCallWebService();
}

int testCallWebService() {
	SimpleWebServiceClientSoap11 soap;
	const string stUrl("http://greg.froh.ca/fun/random_bushism/soap/index.php");
	const string stSoapAction("urn:RandomBushism#bushism#getRandomBushism");
	const string stRequestFile("data/_empty_input_req.xml");
	const string stResponseFile("data/response.xml");


	soap.setUrl(stUrl);
	soap.setSoapAction(stSoapAction);
	if ( soap.postSoapEnvelope(stRequestFile, stResponseFile) != 0 ) {
		cout << soap.getLastErrorString() << endl;
		return -1;
	}

	return 0;
}
