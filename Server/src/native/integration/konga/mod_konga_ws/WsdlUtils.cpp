/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/FileUtils.h>	                      
#include <konga/Utils/CrException.h>
#include <konga/Utils/util.h>		                  

#ifdef WIN32
#include <windows.h>
#endif

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/Db/TranDb/WebServicesTab.h>
#include <konga/Db/TranDb/WsdlTab.h>
#include <konga/Db/TranDb/OperationsTab.h>

#include <konga/OperationEngine/OperationUtils.h>

#include "WsdlUtils.h"

using namespace konga;
using std::string;


namespace {

	long GetWsdlId(DbConnection* pTranDb, const long lWebServiceId) {
		SmartDbPtr<TranDb::WebServicesTab> pWebServicesTab( TranDb::WebServicesTab::MakeInstance(pTranDb) );
		pWebServicesTab->Select("WHERE WebService_Id = " + toString(lWebServiceId));
		if ( !pWebServicesTab->Next() )
			throw InternalErrorException("Failed to find the Web Service  in the WebServiceTab.");

		if ( pWebServicesTab->IsWsdl_IDNull() )
			throw InternalErrorException("Wsdl_Id is NULL for the web service.");

		return pWebServicesTab->GetWsdl_ID();
	}

	void GetWsdlContents(DbConnection* pTranDb, const long lWsdlId, string& sWsdlContents) {
		SmartDbPtr<TranDb::WsdlTab> pWsdlTab( TranDb::WsdlTab::MakeInstance(pTranDb) );
		pWsdlTab->Select("WHERE Wsdl_Id = " + toString(lWsdlId));
		if ( !pWsdlTab->Next() )
			throw CrException("Failed to find the WSDL for the web service.");

		if ( pWsdlTab->IsWsdlPathNull() )
			throw CrException("Wsdl_Path is NULL for the web service.");

		string sWsdlPath( pWsdlTab->GetWsdlPath() );
		if ( sWsdlPath.empty() )
			throw InternalErrorException("Wsdl_Path is empty for web service .");

		sWsdlPath = konga::getAbsolutePath(sWsdlPath, true);
		read_file_to_string(sWsdlPath, sWsdlContents);
	}
}

void konga::GetWsdlForHostedWebService(const long lHostedWebServiceOperationId, std::string& sWsdlContents) {

	SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );

	                                              
	  
	string sOperationName;
	const long lWebServiceId = konga::GetWebServiceId(pTranDb, lHostedWebServiceOperationId, sOperationName);

	                                                
	  
	const long lWsdlId = GetWsdlId(pTranDb, lWebServiceId);
	GetWsdlContents(pTranDb, lWsdlId, sWsdlContents);
}


void konga::GetHtmlForHostedWebService(const long lHostedWebServiceOperationId,
									   const string& sOperationGuid,
									   string& sHtmlInfo)
{
	SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
	SmartDbPtr<TranDb::OperationsTab> pOperationsTab(TranDb::OperationsTab::MakeInstance(pTranDb));

	pOperationsTab->Select("WHERE Operation_ID = " + toString(lHostedWebServiceOperationId));
	if ( !pOperationsTab->Next() )
		throw CrException("Failed to find operation with Operation_ID = " + toString(lHostedWebServiceOperationId));

	const string sOperationName( pOperationsTab->IsOperation_IDNull() ? "" : pOperationsTab->GetOperation_Name() );

	string sWSDLUrl("wsdl?id=" + sOperationGuid);

	string tmp("<html>\n<head>\n<title>");
	tmp += sOperationName;
	tmp += "</title>\n</head>\n<body>\n";
	tmp += "<h2>The Web Service \"" + sOperationName + "\" is operational</h2>\n";
	tmp += "<a href=\"" + sWSDLUrl + "\">WSDL</a>\n</body>\n</html>\n";

	sHtmlInfo = tmp;
}
