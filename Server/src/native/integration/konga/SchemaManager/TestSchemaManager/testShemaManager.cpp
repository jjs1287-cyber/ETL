/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/SchemaManager/SchemaManager.h>

using namespace konga;
using namespace TranDb;

int main()
{
	DBConnectionInfo dbInfo("TranDb");
	std::string dsn=dbInfo.connectionString();
	dsn="UID=sa;PWD=****;SERVER=windev;DRIVER={SQL Server};DATABASE=CRTRANDB;";
	try {
		konga::SchemaManager manager(dsn);
		std::vector<std::string> v=manager.getSourceDtds(19);
		int i=0;
		for(std::vector<std::string>::iterator it=v.begin(); it!=v.end(); it++)
		{
			printf("%d, %s\n",i++,(*it).c_str());
		}
	} catch(CrException& e){
		printf("%s\n",e.GetErrorString().c_str());
	}
}
