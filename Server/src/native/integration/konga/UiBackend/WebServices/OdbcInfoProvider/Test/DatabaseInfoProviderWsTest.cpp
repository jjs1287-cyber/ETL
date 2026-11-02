/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <iostream>

#include <konga/UiBackend/WebServices/OdbcInfoProvider/Impl/OdbcInfoProviderImpl.h>

using namespace std;
using namespace konga;


int main (int argc, char *argv[])
{
	if ( argc < 3 )
		return 1;

	OdbcInfoProviderImpl impl;
	WsDatabaseDriverInfo* pRes = impl.getDrivers(argv[1], argv[2]);
	ArrayOf_xsd_string* pArrayOfNames = pRes->getnames();
	xsd__string_Array* pArrNames = pArrayOfNames->getitem();

	int size = 0;
	const xsd__string* pNames = pArrNames->get(size);

	cout << "Found " << size << " drivers" << endl;

	for( int i = 0; i < size; ++i ) {
		cout << pNames[i] << endl;
	}

	delete pRes;

	return 0;
}

