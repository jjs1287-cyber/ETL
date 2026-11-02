/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifdef WIN32
	#include <conio.h>	          
#endif         

#include <iostream>
#include <list>
#include <map>
#include <string>
#include <vector>

#include <dtl/DTL.h>

#include <konga/Utils/CrException.h>
#include <konga/Utils/DBConnectionInfo.h>

#include <konga/Utils/PortDefs.h>	                            
#include <GetPot>		                                                                
#include <konga/Utils/Settings.h>	                                                

#include <konga/Db/SchemaInfo/SchemaInfo.h>
#include <konga/Db/SchemaInfo/DatabaseObject.h>

using namespace std;
using namespace dtl;
using namespace konga;

void GetChar() {
	#ifdef WIN32
		cout << "\nPress any key to continue." << endl;
		getch();
	#endif         
}

struct TableInfo {
	string name;
};

class TableInfoBCA {
public:
	void operator()(BoundIOs &cols, TableInfo &tab) {
		cols["TABLE_NAME"] >> tab.name;
	}
};

class TableInfoSelValidator {
public:
	bool operator()(BoundIOs &cols, TableInfo &tab) {
		return true;
	}
};

struct localCleanUp {
	~localCleanUp() {
		cleanup();
	}
	void cleanup() {
		if(dtl::DBConnection::GetDefaultConnection().IsConnected()) {
			dtl::DBConnection::GetDefaultConnection().Release();
			dtl::DBConnection::GetDefaultEnvironment().Release();
		}
	}
};

                               

void GenericTests() {
	                                     
	DBConnectionInfo dbConInfo("TranDb");
#if USING_BIND
	DBConnection pdb(dbConInfo.dsn());
	pdb.Connect();
	                                                              
	DBView<TableInfo>::Args 
		args(DBView<TableInfo>::Args().tables("_SQLTables").
										conn(pdb).
										bca(TableInfoBCA()).
										SelValidate(TableInfoSelValidator()));
	DBView<TableInfo> view(args);
	
	TableInfo tab;
	DBView<TableInfo>::sql_iterator it = view.begin(), end_it = view.end();

	for( ;it != view.end(); ++it) {
		cout << it.GetDataObj().name;
	}
#elif defined(USING_DYNAMIC_DBVIEW)
	DBConnection pdb(dbConInfo.dsn());
	pdb.Connect();

	DynamicDBView<>::Args args(DynamicDBView<>::Args().tables("_SQLTables").conn(pdb));
	DynamicDBView<> view(args);
	                                   
	DynamicDBView<>::sql_iterator it = view.begin(), end_it = view.end();
	vector <tstring> cols = it.GetBoundIOs().GetColumnNames();
	copy(cols.begin(), cols.end(), ostream_iterator<tstring>(cout," "));
	cout << endl;

	                        
                                   
                                                   
     
	copy(it, end_it, ostream_iterator<variant_row>(cout,"\n"));


	return 0;
                  
#endif

	SchemaInfo schemaInfo(dbConInfo.connectionString());

	vector<string> vDriverNames;
	schemaInfo.GetDrivers(vDriverNames);
	cout << "Drivers:" << endl;
	for(vector<string>::const_iterator citDriver = vDriverNames.begin(); citDriver != vDriverNames.end(); ++citDriver)
		cout << "\t" << *citDriver << endl; 

	list<DatabaseObject*> dbObjs;
	map<string, bool> mObjTypes;
	mObjTypes["TABLE"] = true;
	                            
	schemaInfo.GetObjects(dbObjs, mObjTypes, "");

	for(list<DatabaseObject*>::const_iterator citObject = dbObjs.begin(); citObject != dbObjs.end(); ++citObject) {
		vector<DatabaseColumn*>& vCols = (*citObject)->GetColumns();
		cout << endl << "*** Object = " << (*citObject)->GetName() << " ***" << endl;
		for(vector<DatabaseColumn*>::const_iterator citCol = vCols.begin(); citCol != vCols.end(); ++citCol) {
			cout << "\t" << (*citCol)->GetName() 
						 << ((*citCol)->IsPrimaryKey() ? ", P" : "")
						 << ", Type: " << (*citCol)->GetType()
						 << ", Default: " << (*citCol)->GetDefaultValue()
						 << endl;

		}
	}

	cout << endl;
}

int TestSQLColumnsUsage(char* argv[], const string& sReason = "") {
	if( !sReason.empty() ) cerr << "Inccorect usage: " << sReason << endl;
	cerr << "To list columns from a table in a db:" << endl;
	cerr << argv[0] << ": -driver <driver> -s <server> -db <db> -t <table> -u <user>" << endl;
	return 1;
}

string AskForPassword() {
	cout << "Password: ";
	string sPwd;
	cin >> sPwd;
	return sPwd;
}

int TestSQLColumns(int argc, char* argv[]) {
	if( argc < 8 )
		return TestSQLColumnsUsage(argv);

	GetPot opt(argc, argv);

	string sDriver = opt.follow("", "-driver");
	if( sDriver.empty() ) return TestSQLColumnsUsage(argv, "Must specify a driver. E.g. \"PostgreSQL\", \"MySQL\".");

	string sDb = opt.follow("", "-db");
	if( sDb.empty() ) return TestSQLColumnsUsage(argv, "Must specify a database. E.g. \"PDb\", \"Test\".");

	string sTable = opt.follow("", "-t");
	if( sTable.empty() ) return TestSQLColumnsUsage(argv, "Must specify a table. E.g. \"Customers\", \"OrderHeader\".");

	string sUser = opt.follow("", "-u");
	if( sUser.empty() ) return TestSQLColumnsUsage(argv, "Must specify a user name. E.g. \"postgres\", \"user1\".");

	string sServer = opt.follow("", "-s");
	if( sServer.empty() ) return TestSQLColumnsUsage(argv, "Must specify a server. E.g. \"localhost\", \"linuxdoc\".");

	string sPwd = AskForPassword();

	DBConnectionInfo dbConInfo(sUser, sPwd, sServer, sDb, sDriver);

	SchemaInfo schema(dbConInfo.connectionString());

	list<DatabaseObject*> liObjs;	
	map<string, bool> mObjTypes;
	mObjTypes["TABLE"] = true;
	mObjTypes["VIEW"] = true;
	schema.GetObjects(liObjs, mObjTypes, sTable);

	if( liObjs.size() == 0 ) {
		cout << "No tables returned." << endl;
		return 0;
	}

	vector<DatabaseColumn*> vCols = (*liObjs.begin())->GetColumns();

	if( vCols.size() == 0 ) {
		cout << "No columns returned." << endl;
		return 0;
	}

	cout << "Columns in table " << sTable << " in database " << sDb << " on " << sServer << ":"<< endl;
	vector<DatabaseColumn*>::iterator itCol = vCols.begin();
	for(;itCol != vCols.end(); ++itCol) {
		cout << "Name:\t\t" << (*itCol)->GetName() << ", Default: " << (*itCol)->GetDefaultValue() << "\n";
	}

	GetChar();

	return 0;
}

int main(int argc, char* argv[])
{
	localCleanUp cleanup_before_exit;

	try {

		                  
		TestSQLColumns(argc, argv);

	} catch(DBException& e) {
		cerr << "An error occurred (DBException): " << e.what() << endl;
		GetChar();
	} catch(CrException& e) {
		cerr << "An error occurred (CrException): " << e.GetErrorString() << endl;
		GetChar();
	} catch(...) {
		cerr << "An unknown error occurred." << endl;
		GetChar();
	}

	return 0;
}

