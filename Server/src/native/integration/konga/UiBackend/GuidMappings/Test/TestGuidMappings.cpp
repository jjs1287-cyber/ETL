/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                                              
  

#include <iostream>
#include <string>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>
#include <konga/UiBackend/GuidMappings/GuidMappings.h>

#include <konga/Utils/util.h>

using namespace konga;
using namespace std;


void testAllEntityTypes();
void testEntityType(int iEntityType);
void testConvenienceMethods();

int main(int argc, char* argv[])
{
	testAllEntityTypes();
                            

	return 0;
}

void testConvenienceMethods() {
	DbGuidToIdMapper mapper;

	string stGuid;
	GetGUIDString(stGuid);
	int iEntityType = 1;
	long lItemId = -98989898;	                                                          
	mapper.SetGuidForItem(lItemId, iEntityType, stGuid);

	if ( konga::GetItemIdForGuid(stGuid, iEntityType) != lItemId )
		cout << "Oooops!" << endl;
	if ( konga::GetGuidForId(lItemId, iEntityType) != stGuid )
		cout << "Oooops!" << endl;

	mapper.DeleteItem(stGuid, iEntityType);
}

void testAllEntityTypes() {
	testEntityType(1);
	testEntityType(2);
	testEntityType(3);
	testEntityType(4);
	testEntityType(5);
	testEntityType(6);
	testEntityType(8);
	testEntityType(9);
	testEntityType(10);
	testEntityType(12);
	testEntityType(13);
	testEntityType(16);
	testEntityType(17);
	testEntityType(18);
	testEntityType(19);

	cout << "done..." << endl;
}

void testEntityType(int iEntityType) {
	DbGuidToIdMapper mapper;

	string stGuid;
	GetGUIDString(stGuid);

	long lItemId = -987654321;	                                                          
	mapper.SetGuidForItem(lItemId+1, iEntityType, stGuid);
	mapper.SetGuidForItem(lItemId, iEntityType, stGuid);	                                        

	if ( mapper.GetGuid(lItemId, iEntityType) != stGuid )
		cout << "1: Oooops!" << endl;

	if ( mapper.GetId(stGuid, iEntityType) != lItemId )
		cout << "2: Oooops!" << endl;

	mapper.DeleteItem(stGuid, iEntityType);
}

