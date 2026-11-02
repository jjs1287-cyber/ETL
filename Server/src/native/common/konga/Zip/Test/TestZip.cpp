/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                                     
  
#include <iostream>
#include <string>

#include <konga/Utils/CrException.h>

#include <konga/Zip/ZipUtils.h>

using namespace konga;
using namespace std;

int testGzipFile();

int main(int argc, char* argv[])
{
	return testGzipFile();
}

int testGzipFile() {
	string sFileName("C:\\tmp\\setup.exe");

	try {
		konga::ZipUtils::GzipFile(sFileName);
	}
	catch( const CrException& ex ) {
		cerr << ex.GetErrorString() << endl;
	}

	return 0;
}
