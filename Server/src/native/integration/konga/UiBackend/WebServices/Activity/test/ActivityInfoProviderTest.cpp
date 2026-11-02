
#include <konga/Utils/CrException.h>

#include <iostream>
#include <string>

#include "../Impl/ActivityInfoProviderImpl.h"

using namespace konga;
using namespace std;

static string toString(TIMESTAMP_STRUCT ts){
	char buf[30];
	                                      
	                                                        
	                                                                          
	sprintf(buf,"%4.4d-%2.2d-%2.2d %2.2d:%2.2d:%2.2d",ts.year,ts.month,ts.day,ts.hour,ts.minute,ts.second);
	return string(buf);
}


void test_getOperationLog(const std::string& id, int id_type,int iLogStatus, int maxRecords, int sortType)
{
	try {
		ActivityInfoProviderImpl impl("Win_User","****");
		                                                          
		string stStartDate="", stEndDate="",stShortMsg="",stFieldName="",stLongMsg="";
		xsd__string_Array* sa=impl.getOperationLog(id,id_type,iLogStatus,
			stStartDate,stEndDate,stShortMsg,stFieldName,stLongMsg,
			maxRecords, sortType);

		int iSize = 0;
		const xsd__string* arr = sa->get(iSize);
		for(int i=0;i<iSize;i++){
			std::cout << arr[i] << std::endl;
		}
	}
	catch(CrException& e){
		std::cout << e.GetErrorString() << std::endl;
	}
}

void test2()
{
	TIMESTAMP_STRUCT ts;
	ts.year=2005;
	ts.month=1;
	ts.day=1;
	ts.hour=0;
	ts.minute=0;
	ts.second=0;
	string str=toString(ts);
	std::cout << str << std::endl;
}


int main(int argc, char* argv[])
{
	test_getOperationLog("bcb52e67-e109-44b6-87a5-95dc2f6fd968", 6, 1, 20, 1);
	return 0;
}
