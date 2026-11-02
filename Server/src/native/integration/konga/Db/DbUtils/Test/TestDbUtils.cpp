/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <string>
#include <iostream>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/util.h>
#include <konga/Utils/Random.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/TableColumnsOdbc.h>
#include <konga/Db/DbUtils/ConnectionString.h>

#include <konga/Db/DbUtils/StoredProcedureCaller.h>

using namespace konga;
using namespace std;

int testSerializable(const string& pwd);

int testLocking(const string& pwd);

int testTransaction(const string& pwd);

int testSqlEscape();

int testStoredProcedure(const string& connectionString);

int insertParentChildTestData(const string& pwd);

int insertTestData(const string& pwd);
int insertTestData2(const string& pwd);

int testConnectionStringParser();

int insertEchoTab(const string& pwd);

int insertRentGrowData(const string& pwd);

int insertDiffTestData1(const string& pwd);

int testLocking2(const string& pwd);

int testDynamicDbRecordSet(const string& pwd);

int createHugeDataSet(const string& pwd);

int createSmoDataSet(const string& pwd);

int testGetNextIdMT();

int main(int argc, char* argv[])
{
	return testGetNextIdMT();
                                    

                                     

                                          
                                

                                       
                                   
                                 

                                      

                                       

                                             

                                  


                                    
                               
                                   

                          
                                       
}

int testDynamicDbRecordSet(const string& pwd) {
	const string connectionString("Driver=PostgreSQL Unicode;Server=linuxdoc;Port=5432;Database=Test;Uid=user1;Pwd=" + pwd);
	try {
		SmartDbPtr<DbConnection> pConn( DbConnection::MakeInstance(connectionString) );
		string sql("select createarcreditmemo(123, '123', '456', '2008-12-12', 890, 'some notes')");

		SmartDbPtr<DynamicDbRecordSet> rs(DynamicDbRecordSet::MakeInstance(pConn, sql));
		rs->Open();
		if ( rs->Next() ) {
			const int fieldCount = rs->GetFieldCount();
			for( int i = 0; i < fieldCount; ++i ) {
				cout << rs->GetFieldValue(i);
			}
			cout << endl;
		}
	}
	catch( const DbException& ex ) {
		cout << ex.GetPrettyErrorMessage() << endl;
		cout << ex.GetSqlErrorMessage() << endl;
		return 1;
	}
	return 0;
}

int testLocking2(const string& pwd) {
	try {
		const string connectionString("Driver=PostgreSQL Unicode;Server=127.0.0.1;Port=5433;Database=IDb;Uid=etlforge;Pwd=" + pwd);
		SmartDbPtr<DbConnection> pDbConn( DbConnection::MakeInstance(connectionString) );
		AutoRollbackTransaction transaction(pDbConn);

		                                                
		pDbConn->ExecuteSql("SET CLIENT_ENCODING TO 'LATIN1'");

		long lSessionId = GetNextId("OperationDataElementTab", "SessionId", *pDbConn);

		std::cout << lSessionId << std::endl;

		pDbConn->ExecuteSql("INSERT INTO operationdataelementtab VALUES (" + toString(lSessionId) + ",'name', '0', 'val')");


		transaction.Commit();
	}
	catch( const CrException& ex ) {
		std::cout << ex.GetErrorString() << std::endl;
		return 1;
	}

	return 0;
}

namespace {
	const char randomletters[] = "QWERTYUIOPLKJHGFDSAZXCVBNM ";

	string random_letters(int min, int max) {
		return konga::SqlEscape( konga::getRandomString(min,max,randomletters) );
	}
}

int createSmoDataSet(const string& pwd) {
	const string connectionString("Driver=PostgreSQL Unicode;Server=linuxdoc;Port=5432;Database=Test;Uid=user1;Pwd=" + pwd);
	try {
		SmartDbPtr<DbConnection> pConn( DbConnection::MakeInstance(connectionString) );

		pConn->ExecuteSql("truncate table smo_cust");
		pConn->ExecuteSql("truncate table smo_alt_id");
		pConn->ExecuteSql("truncate table smo_flat_target");

		const int maxAltId = 100;

		for( int i = 1; i <= 100; ++i ) {
			pConn->ExecuteSql("insert into smo_alt_id values (" + toString(i) + ",'" + random_letters(35,45) + "')");
		}

		int aid1=1, aid2=4;
		for( int i = 1; i <= 1000000; ++i, aid1+=2, aid2+=3 ) {
			if ( aid1 > 100 ) aid1 = 1;
			if ( aid2 > 100 ) aid2 = 1;
			pConn->ExecuteSql("insert into smo_cust values (" + toString(i) + "," + toString(aid1) + "," + toString(aid2)
				+ ",'" + random_letters(40,45) + "', '2008-10-22', '" + random_letters(450,500) + "')");
		}
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}

	return 0;
}

int createHugeDataSet(const string& pwd) {
	const string connectionString("Driver=PostgreSQL Unicode;Server=winqa;Port=5433;Database=Test;Uid=user1;Pwd=" + pwd);
	std::cout << connectionString << std::endl;
	try {
		SmartDbPtr<DbConnection> pConn( DbConnection::MakeInstance(connectionString) );

		pConn->ExecuteSql("TRUNCATE TABLE po");
		pConn->ExecuteSql("TRUNCATE TABLE po_item");

		          
		const int N = 70*500;
		std::cout << "Generating " << N << " POs" << std::endl;

		for( int po_id = 1; po_id <= N; ++po_id ) {
			             
			pConn->ExecuteSql(
				"INSERT INTO po (po_id, po_name, po_address, po_description) VALUES (" + toString(po_id) + ",'" +
				random_letters(10, 50) + "','" +
				random_letters(15, 256) + "','" +
				random_letters(50, 20000) + "')"
				);

			                    
			const int nItems = getRandomNumber(1, 50);
			for( int item_id = 1; item_id <= nItems; ++item_id ) {
				pConn->ExecuteSql(
					"INSERT INTO po_item (po_id, po_item_id, item_guid, item_name, item_description) VALUES (" +
					toString(po_id) + "," + toString(item_id) + ",'" +
					GetGUIDString() + "','" +
					random_letters(15, 256) + "','" +
					random_letters(50, 10000) + "')"
					);
			}
		}
	}
	catch( const DbException& ex ) {
		cout << ex.GetPrettyErrorMessage() << endl;
		cout << ex.GetSqlErrorMessage() << endl;
		return 1;
	}
	return 0;

}

int insertDiffTestData1(const string& pwd) {
	const string connectionString("Driver=PostgreSQL Unicode;Server=windoc;Port=5432;Database=Diff;Uid=user1;Pwd=" + pwd);
	try {
		SmartDbPtr<DbConnection> pConn( DbConnection::MakeInstance(connectionString) );

		pConn->ExecuteSql("TRUNCATE TABLE \"Test2\"");

		const int N = 1000;

		vector<string> node_codes;
		for( int i = 1; i <= N; ++i ) {
			string sql("INSERT INTO \"Test2\" VALUES("
				+ toString(i) + ", '"
				+ "Name 1', '"
				+ "Description " + toString(i) + "', "
				+ toString(2.0*i/3) + ", '"
				+ random_letters(10, 10) + "', "
				+ "'2008-09-15', "
				+ "Now())");
			pConn->ExecuteSql(sql);
			string sql2("INSERT INTO \"Test2\" VALUES("
				+ toString(i) + ", '"
				+ "Name 2', '"
				+ "Description " + toString(i) + "', "
				+ toString(2.0*i/3) + ", '"
				+ random_letters(10, 10) + "', "
				+ "'2008-09-15', "
				+ "Now())");
			pConn->ExecuteSql(sql2);
		}
	}
	catch( const DbException& ex ) {
		cout << ex.GetPrettyErrorMessage() << endl;
		cout << ex.GetSqlErrorMessage() << endl;
		return 1;
	}
	return 0;
}

namespace {
	const char cars[] = "`1234567890-=~!@#$%^&*()_+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?";
	string random_cars(int min, int max) {
		return konga::SqlEscape( konga::getRandomString(min,max,cars) );
	}

	string get_node_code() {
		static vector<string> node_codes;
		while(true) {
			const string node_code(random_letters(5,5));
			if ( std::find(node_codes.begin(), node_codes.end(), node_code) == node_codes.end() ) {
				node_codes.push_back(node_code);
				return node_code;
			}
			cout << "duplicate: " << node_code << endl;
		}
	}
}

int insertTestData2(const string& pwd) {
	const string connectionString("SERVER=linuxdoc;DRIVER=MySQL ODBC 3.51 Driver;DATABASE=test;UID=user1;PWD=" + pwd);
	try {
		SmartDbPtr<DbConnection> pConn( DbConnection::MakeInstance(connectionString) );
		pConn->ExecuteSql("truncate `users`");
		pConn->ExecuteSql("truncate `node`");
		for( int uid = 0; uid < 1000; ++uid ) {
			string sql("insert into `users` values (" + toString(uid) + ", '" + konga::SqlEscape(random_cars(4,20)) + "', 1)");
			pConn->ExecuteSql(sql);
			
			sql = "insert into `node` (type, uid) values ('type', " + toString(uid) + ")";
			pConn->ExecuteSql(sql);
		}
	}
	catch( const DbException& ex ) {
		cout << ex.GetPrettyErrorMessage() << endl;
		cout << ex.GetSqlErrorMessage() << endl;
		return 1;
	}

	return 0;
}

int insertRentGrowData(const string& pwd) {
	const string connectionString("Driver=PostgreSQL ANSI;Server=linuxdoc;Port=5432;Database=RentGrow;Uid=user1;Pwd=" + pwd);
	try {
		SmartDbPtr<DbConnection> pConn( DbConnection::MakeInstance(connectionString) );

		pConn->ExecuteSql("TRUNCATE TABLE node_tbl");
		pConn->ExecuteSql("TRUNCATE TABLE auxiliary_node");

		const int N = 10;

		vector<string> node_codes;
		for( int i = 0; i < N; ++i ) {
			const string node_code(get_node_code());
			pConn->ExecuteSql("insert into node_tbl values (" \
				"now(), now(), 'CCCCCC', '" + node_code + "', '" + random_cars(4,40)
				+ "', '"+random_cars(0,24)+"', 'CA', '94501-00000', '510282491000000000', '510282491000000000', 2, '" + random_cars(0,32)
				+ "', now(), now(), now(), 123, 345, '12345678', null, '"+random_cars(4,40)+"', '"+random_cars(4,40)+"'," \
				" '"+random_cars(4,40)+"', false)");

			pConn->ExecuteSql("insert into auxiliary_node values ('" + node_code
				+ "', now(), '" + random_cars(0,40) + "', 'notes', 'emanuel@etlforge.invalid', 12, 'remarkable'," \
				"123, 432, true, false, false)");
		}
	}
	catch( const DbException& ex ) {
		cout << ex.GetPrettyErrorMessage() << endl;
		cout << ex.GetSqlErrorMessage() << endl;
		return 1;
	}

	return 0;
}

int insertEchoTab(const string& pwd) {
	const string connectionString("Driver=PostgreSQL Unicode;Server=linuxdoc;Port=5432;Database=Test;Uid=user1;Pwd=" + pwd);

	try {
		SmartDbPtr<DbConnection> pConn( DbConnection::MakeInstance(connectionString) );

		pConn->ExecuteSql("TRUNCATE TABLE \"EchoTable\"");
		for( int i = 1; i <= 100; ++i ) {
			const string text("String number " + toString(i) + " " + GetGUIDString());
			const string date("2008-01-01");

			const string sql("INSERT INTO \"EchoTable\" VALUES (" + toString(i) + ", '" + text + "', '" + date + "')");
			pConn->ExecuteSql(sql);
		}
	}
	catch( const DbException& ex ) {
		cout << ex.GetPrettyErrorMessage() << endl;
		cout << ex.GetSqlErrorMessage() << endl;
		return 1;
	}

	return 0;
}


namespace {
	void FillUUPIC_Add(vector<string>& args) {
		const string date("2008-11-30 19:59:32-08:00");
		args.push_back("444222333");
		args.push_back("Emanuel");
		args.push_back("Hans");
		args.push_back("Norrbin");
		args.push_back("QG");
		args.push_back("1");
		args.push_back("UUPICType");
		args.push_back("Source");
		args.push_back(date);
		args.push_back("Contractor");
		args.push_back("123123123");
		args.push_back("CenterCode");
		args.push_back("1972-10-29");
		args.push_back("LicenseNum");
		args.push_back("CA");
		args.push_back(date);
		args.push_back("Swedish");
		args.push_back("ANum");
		args.push_back("");		         
	}

	void FillTransaction_Add(vector<string>& args) {
		args.push_back("Source");
		args.push_back("987654321");
		args.push_back("1972-10-29");
		args.push_back("Info");
		args.push_back("Type");
		args.push_back("");		         
	}

	void FillEchoSP(StoredProcedureCaller::SPParamsT& args) {

		                   
		args.push_back(StoredProcedureCaller::SPParam(
			StoredProcedureCaller::PARAM_TYPE_STRING, "Some string for you")
			);
		                      
		args.push_back(StoredProcedureCaller::SPParam());

                                                  
                                                    
       

		args.push_back(StoredProcedureCaller::SPParam(
			StoredProcedureCaller::PARAM_TYPE_DATETIME, "1972-10-29 19:43:31")
			);

		args.push_back(StoredProcedureCaller::SPParam(
			StoredProcedureCaller::PARAM_TYPE_STRING, "qwertyuio")
			);
		args.push_back(StoredProcedureCaller::SPParam(
			StoredProcedureCaller::PARAM_TYPE_INTEGER, "1234567")
			);

		args.push_back(StoredProcedureCaller::SPParam(
			StoredProcedureCaller::PARAM_TYPE_INTEGER, "12345678901234567")
			);

		args.push_back(StoredProcedureCaller::SPParam(
			StoredProcedureCaller::PARAM_TYPE_REAL, "1234.567")
			);

		                    
		args.push_back(StoredProcedureCaller::SPParam());
		args.push_back(StoredProcedureCaller::SPParam());
		args.push_back(StoredProcedureCaller::SPParam());
		args.push_back(StoredProcedureCaller::SPParam());
		args.push_back(StoredProcedureCaller::SPParam());
		args.push_back(StoredProcedureCaller::SPParam());
		args.push_back(StoredProcedureCaller::SPParam());

		                    
		args.push_back(StoredProcedureCaller::SPParam());
	}
}

int insertTestData(const string& pwd) {
		string connectionString(
			"Driver=PostgreSQL ANSI;Server=linuxdoc;Port=5432;Database=Emanuel;Uid=user1;Pwd="
			+ pwd);

		try {
			SmartDbPtr<DbConnection> pConn( DbConnection::MakeInstance(connectionString) );

			for( int i =0; i < 22000; ++i ) {
				pConn->ExecuteSql("INSERT INTO \"GuidUsersTab\" VALUES ('"
					+ GetGUIDString() + "', 'Name-" + toString(i) + "')");
			}
		}
		catch( const CrException& ex ) {
			cerr << ex.GetErrorString() << endl;
			return 1;
		}

		return 0;
}


int insertParentChildTestData(const string& pwd) {
	try {
		string connectionString(
			"Driver=PostgreSQL ANSI;Server=linuxdoc;Port=5432;Database=Emanuel;Uid=user1;Pwd="
			+ pwd);

		SmartDbPtr<DbConnection> pConn( DbConnection::MakeInstance(connectionString) );

		                    
		pConn->ExecuteSql("truncate \"Parent\"; truncate \"Child1\"; truncate \"Child2\"");

		const int nofParents = 2000;
		int child1Id = 1;
		int child2Id = 1;
		int child3Id = 1;
		for ( int parentId = 1; parentId <= nofParents; ++parentId ) {
			                 
			string sql("INSERT INTO \"Parent\" VALUES ("
				+ toString(parentId) + ", 'Some String', 'Some Other String')");
			pConn->ExecuteSql(sql);

			                                  
			for ( int i = 0; i < 3; ++i ) {
				sql = "INSERT INTO \"Child1\" VALUES ("
					+ toString(child1Id) + ", " + toString(parentId)
					+ ", 'Some Child1 String')";
				pConn->ExecuteSql(sql);

				                                             
				for ( int j = 0; j < 2; ++j ) {
					sql = "INSERT INTO \"Child2\" VALUES ("
						+ toString(child2Id) + ", " + toString(child1Id)
						+ ", 'Some Child2 String')";
					pConn->ExecuteSql(sql);

					++child2Id;
				}

				++child1Id;
			}

			                                   
			sql = "INSERT INTO \"Child3\" VALUES ("
				+ toString(child3Id) + ", " + toString(parentId)
				+ ", 'Some Child3 String with some more data, just making smalltalk bla bla bla')";
			pConn->ExecuteSql(sql);
			++child3Id;
		}
	}
	catch( const CrException& e ) {
		cerr << e.GetErrorString() << endl;
		return 1;
	}
	return 0;
}

int testStoredProcedure(const string& pwd) {
	string connectionString(
		"Driver=PostgreSQL ANSI; Server=localhost; Port=5433; Database=Test; Uid=postgres; Pwd="
		+ pwd);
	
	connectionString = 
		"Driver=SQL Server; Server=windev; Database=Test; Uid=sa; Pwd="
		+ pwd;

	string spName = "SPTest1";
	spName = "SPTestNoArg";
	spName = "SelectPanelCodeFromDrugs";
	spName = "UUPIC_SearchSSN";
	spName = "UUPIC_GetID";
	spName = "UUPIC_Add";
	spName = "Transaction_Add";
	spName = "DateTestProc";
	spName = "EchoSP";

	StoredProcedureCaller caller(connectionString, UNDEFINED);
	StoredProcedureCaller::SPParamsT spArgs;
                       
	{
                               
                                     
	}

	   
                                
                        
                                 
                                       
    

	{
                                
	}

	{
                                        
	}

                       
                             

	{
                                          
                       
	}

	FillEchoSP(spArgs);

	try {
		StoredProcedureCaller::SPParam retVal;
		caller.CallStoredProcedure(spName, spArgs, retVal);
		cout << "Procedure Return Value: " << retVal.value << " (" << retVal.type << ")" << endl;
		cout << "Parameter values:" << endl;
		for( StoredProcedureCaller::SPParamsT::const_iterator cit = spArgs.begin();
			cit != spArgs.end(); ++ cit )
		{
			cout << cit->value << " (" << cit->type << ")" << endl;
		}
	}
	catch( const CrException& e ) {
		cerr << e.GetErrorString() << endl;
		return 1;
	}

	return 0;
}

int testSqlEscape() {

	cout << SqlEscape("01234'678\\") << endl;
	cout << SqlEscape("'12345678'") << endl;
	cout << SqlEscape("\\12345678\\") << endl;
	cout << SqlEscape("'12345678\\") << endl;
	cout << SqlEscape("''23456789\\") << endl;
	cout << SqlEscape("0123\\\\6789") << endl;
	cout << SqlEscape("01234567''") << endl;
	cout << SqlEscape("0123456789") << endl;

	cout << SqlLikeEscape("123456789") << endl;
	cout << SqlLikeEscape("01%34_67\\90'2") << endl;
	return 0;
}

int testTransaction(const string& pwd) {
	const string connectStr(
		"Server=linuxdoc;Database=Emanuel;Driver=PostgreSQL;Uid=user1;Pwd=" + pwd
		);
	try {
		                                
		SmartDbPtr<DbConnection> pConn1( DbConnection::MakeInstance(connectStr) );
		AutoRollbackTransaction adopt_pConn1(pConn1);

		const string stTable("\"TableWithPK\""), stKey("pk");
		                                                           
		long lId1 = GetNextId(stTable, stKey, *pConn1);

		string stSql("insert into " + stTable + " values ("
			+ toString(lId1) + ", 1.23, 'Some String')");
		pConn1->ExecuteSql(stSql);

		                           
		SmartDbPtr<DbConnection> pConn2( DbConnection::MakeInstance(connectStr) );
		stSql = "select pk from " + stTable + " where pk = " + toString(lId1);
		SmartDbPtr<DynamicDbRecordSet> pRs( DynamicDbRecordSet::MakeInstance(pConn2, stSql) );
		pRs->Open();
		if ( pRs->Next() ) {
			cerr << "Problem, should not be seen here!!" << endl;
		}
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}
	return 0;
}


int testLocking(const string& pwd) {
	const string connectStr(
		"Server=linuxdoc;Database=Emanuel;Driver=PostgreSQL;Uid=user1;Pwd=" + pwd
		);
	try {
		                                
		SmartDbPtr<DbConnection> pConn1( DbConnection::MakeInstance(connectStr) );
		AutoRollbackTransaction adopt_pConn1(pConn1);

		const string stTable("\"TableWithPK\""), stKey("pk");

		                                     
		pConn1->ExecuteSql("LOCK " + stTable + " IN SHARE UPDATE EXCLUSIVE MODE");

		                                                           
		long lId1 = GetNextId(stTable, stKey, *pConn1);

		string stSql("insert into " + stTable + " values ("
			+ toString(lId1) + ", 1.23, 'Some String')");
		pConn1->ExecuteSql(stSql);

	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}
	return 0;
}

int testSerializable(const string& pwd) {

	const string connectStr(
		"Server=linuxdoc;Database=Emanuel;Driver=PostgreSQL;Uid=user1;Pwd=" + pwd
		);

	try {
		                                
		SmartDbPtr<DbConnection> pConn1( DbConnection::MakeInstance(connectStr) );
		AutoRollbackTransaction adopt_pConn1(pConn1);

		const string stTable("\"TableWithPK\""), stKey("pk");
		                                                           
		long lId1 = GetNextId(stTable, stKey, *pConn1);

		                                 
		string stSql("insert into " + stTable + " values ("
			+ toString(lId1) + ", 1.23, 'Some String')");
		pConn1->ExecuteSql(stSql);

		                                                             
		SmartDbPtr<DbConnection> pConn2( DbConnection::MakeInstance(connectStr) );
		AutoRollbackTransaction adopt_pConn2(pConn2);

		SmartDbPtr<DynamicDbRecordSet> pRs( DynamicDbRecordSet::MakeInstance(pConn2) );
		pRs->SetSqlStatement("select pk, \"DoubleCol\" from " + stTable
			+ " where pk = " + toString(lId1));
		pRs->Open();
		if ( pRs->Next() ) {
			string stVal = pRs->GetFieldValue(stKey);
			cout << "Error pConn2 should not be able to access this value!" << endl;
			cout << "The value is " << stVal << endl;
		}

		                                                        
		long lId2 = GetNextId(stTable, stKey, *pConn2);

		if ( lId2 == lId1 ) {
			cout << "Problem, we get the same id..." << endl;
		}

		                               
		adopt_pConn1.Commit();

		                                                                           
		stSql = "insert into " + stTable + " values ("
			+ toString(lId2) + ", 1.23, 'Some String')";

		pConn2->ExecuteSql(stSql);

		                                                            
	}
	catch ( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 2;
	}

	return 0;
	return 1;
}

int testConnectionStringParser() {

	string sConnStr(
		"DRIVER={PostgreSQL Unicode};SERVER=localhost; DATABASE=mydb;PORT=5433 ; Uid=user2  ; Pwd=abc; "
		);

	OdbcConnectionString cs(sConnStr);

	cout << sConnStr << endl;
	cout << cs.get() << endl;
	cout << cs.getDatabase() << endl;
	cout << cs.getDriver() << endl;
	cout << cs.getServer() << endl;
	cout << cs.getUid() << endl;

	cs.setDatabase("newDb");
	cs.setDriver("NewDriver");
	cs.setServer("NewServer");
	cs.setUid("NewUid");
	cout << cs.get() << endl;
	cout << cs.getDatabase() << endl;
	cout << cs.getDriver() << endl;
	cout << cs.getServer() << endl;
	cout << cs.getUid() << endl;

	return 0;
}

