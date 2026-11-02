/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

                   
#include <fstream>	           
#include <string>
#include <iostream>

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/Random.h>

using namespace konga;
using namespace std;

namespace {
	char charPool[] = "qwertyuiopasdfghjklzxcvbnm1234567890";
}

int GeneratePIESXml() {
	const string stDir("C:\\Emanuel\\ETLForge\\Testing\\XML\\PIES\\");

	const string stHeader(stDir + "header.xml");
	const string stTrailer(stDir + "trailer.xml");
	const string stItem(stDir + "item.xml");

	const string stPiesXml(stDir + "pies.xml");

	try {
		kCopyFile(stHeader, stPiesXml);

		string stItemContents;
		read_file_to_string(stItem, stItemContents);

		ofstream fout(stPiesXml.c_str(), std::ios_base::app);

		for( int i = 0; i < 100; ++i ) {
			string stNewItem;
			ReplaceStringWithString(stItemContents, stNewItem, "%PARTNR%",
				getRandomString(20, 48, charPool));
			fout << stNewItem;
		}

		string stTrailerContents;
		read_file_to_string(stTrailer, stTrailerContents);

		fout << stTrailerContents;
		fout.close();
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}
	catch( const std::exception& ex ) {
		cout << ex.what() << endl;
		return 2;
	}

	return 0;
}
