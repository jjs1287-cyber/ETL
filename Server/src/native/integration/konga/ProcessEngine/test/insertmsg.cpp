/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <time.h>
#include <stdlib.h>          

#include <iostream>
#include <string>

#include <konga/Messaging/MessageSender.h>

#include <konga/Utils/StringUtils.h>

using namespace std;
using namespace konga;

int main (int argc, char *argv[])
{
	string stProcessvariables, stMachine;
	int nRowsToInsert = 1;
	if( argc > 1 ) {
		nRowsToInsert = atoi(argv[1]);
		if( nRowsToInsert < 1 )
			nRowsToInsert = 1;
		if( nRowsToInsert > 49 ) {
			cout << "Are you sure you want to insert " << nRowsToInsert << " rows into the IncomingMsgTab? (y/n) ";
			std::string sResponse;
			cin >> sResponse;
			if( sResponse != "y" && sResponse != "Y" ) return 0;
		}
		if( argc > 2 ) {
			stProcessvariables = argv[2];
			cout << "Using process variables: " << stProcessvariables << endl;
		}
		if( argc > 3 ) {
			stMachine = argv[3];
			cout << "For engine running on machine: " << stMachine << endl;
		}
	}

	cout << "Inserting " << nRowsToInsert << (nRowsToInsert==1?" row":" rows") << " into IncomingMsgTab...";
	cout.flush();

	MessageSender msgSender;
	if( stProcessvariables.empty() ) {
		msgSender.SetProcessName("test");
		msgSender.SetTaskName("test");
		msgSender.AddDataElement("test", "test", DET_STRING);
	} else
		msgSender.SetProcessVariables(stProcessvariables);

	msgSender.SetUserName("test");                              
	msgSender.SetPassword("test");

	if( !stMachine.empty() )
		msgSender.SetOwner(stMachine);


	try {
		for(int i=0; i<nRowsToInsert; ++i)
			msgSender.SendMessage();
	} catch(CrException& e) {
		cout << endl;
		cerr << e.GetErrorString() << endl;
		return 2;
	}

	cout << "Done." << endl;

	return(0);
}

