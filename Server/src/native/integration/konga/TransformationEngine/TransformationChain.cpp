/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/FileUtils.h>
#include <set>

#include <konga/Db/TranDb/TransformationChainLinksTab.h>
#include <konga/Db/TranDb/TransformationFunctionTab.h>

#include <konga/Server/Server.h>

#include <konga/OperationContext/OperationDataElementHolder.h>

#include "TransformationChain.h"

using namespace konga;
using namespace TranDb;
using std::string;

int TransformationChain::Create(int iFirstTransformationId, const OperationDataElementHolder& deHolder, DbConnection& tranDbConn)
{
	try
	{
		                                                                                          
		                                        
		if( iFirstTransformationId == 0 )
		{
            SmartDbPtr<TransformationChainLinksTab> linksTab( TransformationChainLinksTab::MakeInstance(&tranDbConn) );
			std::string stChainLinkFilter = "WHERE (Transformation_ID=0) AND (Operation_ID=";
			stChainLinkFilter += konga::toString(m_iOperationId);
			stChainLinkFilter += ")";
			linksTab->Select(stChainLinkFilter);
			if( linksTab->Next() )
			{
				if(!linksTab->IsNextTransformation_IDNull())iFirstTransformationId = linksTab->GetNextTransformation_ID();
			}
			                                                                          
			if( iFirstTransformationId == 0 )
			{
				m_stError = "No transformations are defined for the operation.";
				return 2;
			}
		}

		int iTransformationId = iFirstTransformationId;
		std::set<int> visited;
		while( iTransformationId != 0 )
		{
			if (!visited.insert(iTransformationId).second) {
				m_stError = "The operation contains a cyclic transformation chain.";
				return -1;
			}
			                                                     
			                                              
			int iNextTransformationId = 0;
            SmartDbPtr<TransformationChainLinksTab> linksTab( TransformationChainLinksTab::MakeInstance(&tranDbConn) );
			std::string stLinkTabFilter = "WHERE (Transformation_ID=" + konga::toString(iTransformationId);
			stLinkTabFilter += ") AND (Operation_ID=" + konga::toString(m_iOperationId);
			stLinkTabFilter += ")";
			linksTab->Select(stLinkTabFilter);
			if( linksTab->Next() )
			{
				if(!linksTab->IsNextTransformation_IDNull())iNextTransformationId = linksTab->GetNextTransformation_ID();
			}
			                                                        
			int iFunctionId = 0;
            SmartDbPtr<TransformationFunctionTab> funcTab( TransformationFunctionTab::MakeInstance(&tranDbConn) );
			std::string stFuncFilter = "WHERE Transformation_ID = " + konga::toString(iTransformationId);
			funcTab->Select(stFuncFilter);
			if( funcTab->Next() )
			{
				if(!funcTab->IsFunction_IDNull())iFunctionId = funcTab->GetFunction_ID();
			}

			TransformationChainItem item;
			item.TransformationId = iTransformationId;
			item.NextTransformationId = iNextTransformationId;
			item.FunctionId = iFunctionId;

			                                                              
			                                                           
			if( iFunctionId > 0 ) {
				konga::GetWebServiceInfo(iFunctionId, item.WebServiceInfo, deHolder, tranDbConn);
			}

			                                            
			AddChainItem(item);

			                                                       
			iTransformationId = iNextTransformationId;
		}
		SetWebServiceJtrFileLocations();
	}
	catch( const CrException& e )
	{
		m_stError = e.GetErrorString();
		return -1;
	}

	return 0;
}

void TransformationChain::AddChainItem(TransformationChainItem item)
{
	if( Length() > 0 )
	{
		TransformationChainItem& prev = m_vChainItems[Length()-1];
		if( item.FunctionId == 0 )
		{
			                                                                                    
			                        
			if( prev.FunctionId != 0 )
				item.WebServiceInfo.SchemaStatus = prev.WebServiceInfo.SchemaStatus;
		}
		else
		{
			                                                                               
			                 
			if( prev.FunctionId == 0 )
				prev.WebServiceInfo.SchemaStatus = item.WebServiceInfo.SchemaStatus;
		}
	}
	m_vChainItems.push_back(item);
}

const TransformationChainItem* TransformationChain::GetLastTransformationChainItem() const {
	if ( m_vChainItems.empty() ) return 0;
	return &m_vChainItems.back();
}

void TransformationChain::SetWebServiceJtrFileLocations() {
	                              
	const int length = Length();
	if (length <= 1) {
		return;
	}

	for (int n = 0; n < length; ++n) {
		TransformationChainItem& item(m_vChainItems[n]);
		if (item.FunctionId == 0)continue;
		                              
		int transformationId=-99;
		if(n>0){
			TransformationChainItem& last_item(m_vChainItems[n-1]);
			transformationId=last_item.TransformationId;
		}
		if(transformationId<0)transformationId=item.TransformationId;
		string requestJtr(GetTargetJtrFileName(transformationId));
		item.WebServiceInfo.RequestJtr = requestJtr;

		                               
		transformationId=item.NextTransformationId;
		if(transformationId<0)transformationId=item.TransformationId;
		string responseJtr(GetSourceJtrFileName(transformationId));
		item.WebServiceInfo.ResponseJtr = responseJtr;
		break;
	}
}
