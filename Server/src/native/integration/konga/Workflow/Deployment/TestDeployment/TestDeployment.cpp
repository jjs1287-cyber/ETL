/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <iostream>
#include <string>

#include <konga/Authentication/Authentication.h>
#include <konga/Utils/CrException.h>
#include <konga/Workflow/XmlReader/WfXmlReader.h>
#include <konga/Workflow/DbWriter/WfDbWriter.h>
#include <konga/Workflow/Process.h>
#include <konga/Workflow/RuleTree.h>
#include <konga/Xml/CrXmlException.h>

using namespace std;
using namespace konga;
using namespace konga::xml;

int testDeployment();
int testStackParse();
int testExpressionParse();

int main(int argc, char* argv[])
{
	return testDeployment();
                           
                                
}

int testDeployment() {
	try {
		                                                                      

                                                                   
		if( !Authentication::IsValidUser("emanuel", "****") ) {
			cout << "Authentication failed." << endl;
		}

		#ifdef WIN32
			auto_ptr<Process> pProcess(WfXmlReader::ParseXml( "../../Data/completeProcess.xml" ));
		#else
			auto_ptr<Process> pProcess(WfXmlReader::ParseXml( "../../../Data/completeProcess.xml" ));
		#endif
		pProcess->SetOwner("emanuel");

		pProcess->PrintRoutes();

		const string stConnectionString("DSN=CRPDB_P;UID=edgeways;PWD=****;");
		WfDbWriter dbWriter;
		dbWriter.WriteProcess(*pProcess, stConnectionString, WfDbWriter::dtDELETE_PREVIOUS);

		cout << "Process deployed with ProcessId = " << pProcess->GetId() << " and ProcessVersion = "
			 << pProcess->GetVersion() << endl;

	} catch(CrException& e) {
		cerr << "CrException caught in testDeployment(): " << e.GetErrorString() << endl;
		return 1;
	} 
	return 0;
}

int testExpressionParse() {
                                               
                                                 
                                    
                            
                          
	string stExpr("companyFilter=\"CompanyName = 'ETLForge'\"");
	RuleTree rtree;
	cout << "input expression: " << stExpr << endl;
	rtree.parseExpression(stExpr);
	cout << rtree.toString() << endl;
	cout << rtree.stackFormat() << endl;
	cout << rtree.expressionFormat() << endl;

	return 0;
}

int testStackParse() {
	try {
                               
                                     
                               
		string stStack("a;b1;b2;~;&3;c;~;d;&;|;");
                            
		cout << "Stack: " << stStack << endl;
		RuleTree rtree;
		rtree.parseStack(stStack);
		cout << "RuleTree:" << endl;
		cout << rtree.toString() << endl;

		cout << "Stack: " << endl;
		cout << rtree.stackFormat() << endl;
		cout << "Expression: " << endl;
		cout << rtree.expressionFormat() << endl;

		                     
		rtree.parseExpression(rtree.expressionFormat());
		rtree.parseStack(rtree.stackFormat());
		cout << "Stack: " << endl;
		cout << rtree.stackFormat() << endl;
		cout << "Expression: " << endl;
		cout << rtree.expressionFormat() << endl;
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return -1;
	}
	return 0;
}
