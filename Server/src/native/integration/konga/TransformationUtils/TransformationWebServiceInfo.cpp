/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/Utils/util.h>

#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/Db/TranDb/WebServiceView2.h>
#include <konga/Db/TranDb/WsdlTab.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/TransformationUtils/TranDBUtils.h>

#include "TranDBUtils.h"
#include "TransformationWebServiceInfo.h"

using namespace konga;
using namespace TranDb;
using std::string;

void konga::GetWebServiceInfo(int iFunctionID,
							  TransformationWebServiceInfo& wsInfo,
							  const OperationDataElementHolder& deHolder,
							  DbConnection& tranDbConn)
{
    SmartDbPtr<WebServiceView2> wsView( WebServiceView2::MakeInstance(&tranDbConn) );
	wsView->Select( "WHERE Function_ID = " + konga::toString(iFunctionID) );
	if( wsView->Next() ) {
		const string blank("");

		wsInfo.Url = wsView->IsWebService_UrlNull()?blank:wsView->GetWebService_Url();
		wsInfo.TargetNamespace = wsView->IsTargetNamespaceNull()?blank:wsView->GetTargetNamespace();
		wsInfo.OtherNamespaces = wsView->IsOtherNamespacesNull()?blank:wsView->GetOtherNamespaces();
		wsInfo.SoapAction = wsView->IsSoapActionNull()?blank:wsView->GetSoapAction();
		wsInfo.SoapStyle = wsView->IsSoapStyleNull()?blank:wsView->GetSoapStyle();
		wsInfo.SoapBodyUse = wsView->IsSoapBodyUseNull()?blank:wsView->GetSoapBodyUse();
		wsInfo.RequestXsd = wsView->IsInputXSDFileNameNull()?blank:konga::getAbsolutePath(wsView->GetInputXSDFileName());
		wsInfo.ResponseXsd = wsView->IsOutputXSDFileNameNull()?blank:konga::getAbsolutePath(wsView->GetOutputXSDFileName());
		wsInfo.HttpUserName = wsView->IsHttpUserNameNull()?blank:wsView->GetHttpUserName();
		wsInfo.HttpPassword = wsView->IsHttpPasswordNull()?blank:wsView->GetHttpPassword();
		wsInfo.DelayMS = wsView->IsMinCallDelay_msNull()?0:wsView->GetMinCallDelay_ms();
		wsInfo.MaxSizeBytes = wsView->IsMaxSize_bytesNull()?0:wsView->GetMaxSize_bytes();

		const long wsid = wsView->IsWebService_IDNull()?-1:wsView->GetWebService_ID();
		if ( wsid > 0) {
			wsInfo.HttpPostOptions = konga::GetRuntimeHttpOptionsForEntity(wsid, WebServiceEntityId, deHolder, &tranDbConn);
		}

		                                             
		if( wsView->GetWsdl_ID() >= 0 ) {
            SmartDbPtr<WsdlTab> wsdlTab( WsdlTab::MakeInstance(&tranDbConn) );
			string stWsdlFilter = "WHERE Wsdl_ID = " + konga::toString(wsView->GetWsdl_ID());
			wsdlTab->Select(stWsdlFilter);
			int iStatus = -1;
			if( wsdlTab->Next() ) {
				if(!wsdlTab->IsStatusNull())iStatus = wsdlTab->GetStatus();
			}
			wsInfo.SchemaStatus = WsdlConstants::GetWsdlSchemaStatusFromInt(iStatus);
		}
		else {
			throw CrException("There is no WSDL associated with the webservice.");
		}
	}
}
