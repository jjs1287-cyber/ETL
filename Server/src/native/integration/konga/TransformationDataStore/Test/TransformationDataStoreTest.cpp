/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <iostream>

#include <konga/Utils/CrException.h>

#include <konga/TransformationDataStore/TransformationDataStore.h>

using namespace konga;
using namespace std;

int Register1();
int Retrieve1();

int main(int argc, char* argv[])
{
	return Retrieve1();
                      
}

int Retrieve1() {

	string guid("ba624805-22f6-43d8-a1a3-ecd405f1392d");

	TransformationDataStore store;
	TransformationDataStoreInstance filter;
	filter.GUID = guid;
	vector<TransformationDataStoreInstance> lst = store.GetFileList(filter);

	return 0;
}

int Register1() {

	TransformationDataStoreInstance instance;
	instance.transformationId = 187;
	instance.operationId = 106;
	instance.originalSourceNumber = 1;
	instance.chunkNumber = 0;
	instance.sourceFile = "C:\\tmp\\empty.txt";

	TransformationDataStore store;
	try {
		store.AddFiles(instance);
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}

	return 0;
}
