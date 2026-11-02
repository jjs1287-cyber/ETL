/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <cassert>

#ifdef WIN32
#include "windows.h"
#endif

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/util.h>
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/Settings.h>		                     

#include <konga/Xml/DOMDocumentPtr.h>
#include <konga/Soap/SoapEnvelope.h>

#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/FileTransfer/HttpUtils.h>	                                        

#include <konga/Messaging/OperationMessageUtils.h>

#include <konga/DataElement/DataElement.h>

#include <konga/PluginManager/PipelinePluginSelector.h>
#include <konga/PluginManager/PipelinePosition.h>
#include <konga/PluginManager/PipelinePluginManifest.h>

#include <konga/PipelinePluginExecutor/PipelinePluginExecutor.h>

#include <konga/OperationContext/OperationDataElementHolder.h>

#include <konga/TransformationEngine/TransformationEngine.h>		                         

#include <konga/OperationObjects/Operation.h>
#include <konga/OperationObjects/Transformation.h>
#include <konga/OperationObjects/OperationEngineException.h>
#include <konga/OperationObjects/SourceFromHostedWebServicecall.h>
#include <konga/OperationObjects/ProcessedFiles.h>
#include <konga/OperationObjects/OperationEngineContext.h>

#include "OperationScriptRunner.h"
#include "OperationEngine.h"
#include "OperationUtils.h"
#include "DebugLog.h"

using namespace konga;
using std::string;
using std::vector;

void OperationEngine::HandleHostedWebServiceCallImpl(const HttpRequest& request_orig,
													 HttpResponse& response,
													 string& sWarning)
{
	const bool skip = konga::GetOptionAsBoolean("OperationEngine", "SkipHostedWebServiceCheck", false);
	try {
		this->Check(skip);
	}
	catch( const CrException& ex ) {
		string stErr(ex.GetErrorString());
		m_Logger.LogError(stErr);
		SoapFault_11 soapFault(FaultCodeValue_11::Server, "License check Error", "", stErr);
		xml::DOMDocumentPtr pEnvelope = SoapEnvelope::CreateSoapEnvelopeWithSoapFault(
			SoapConstants::Version_11, soapFault, stErr);

		response.Body = pEnvelope->getXml();
		sWarning = stErr;
		return;
	}

	HttpRequest request(request_orig);
	OP_DEBUG_LOG("About to run Request plugins on web service request.");
	RunOnRequestPluginsForHostedWebServiceCall(request.Body);

	const bool bSiblingOperationImplementsWS = m_pOperation->getTransformation() == 0;

	                                         
	OP_DEBUG_LOG("About to start sibling operations.");
	StartSiblingOperations(getOperationId(), request.Body, bSiblingOperationImplementsWS, response.Body);

	bool returnedSoapFault = false;
	if ( bSiblingOperationImplementsWS ) {
		                                     
	}
	else {
		OP_DEBUG_LOG("About to run scripts before the transformation.");
		m_pScriptRunner->RunScriptsBefore(ActivityType_Transformation);

		OP_DEBUG_LOG("About to call TransformationEngine::HandleWebServiceCall");
		TransformationEngine transform(m_CurrentUser, m_pContext, m_pDeHolder);
		transform.HandleWebServiceCall(request.Body, response.Body, returnedSoapFault);

		if ( returnedSoapFault ) {
			this->getOperationContext().setLocalTmpDirForOperationDeleteFlag(false);
		}
		sWarning = transform.getErrorsAndWarnings();
	}

	OP_DEBUG_LOG("About to run final scripts.");
	m_pScriptRunner->RunFinalScripts();

	OP_DEBUG_LOG("About to run onResponse plugins for hosted web service call.");
	RunOnResponsePluginsForHostedWebServiceCall(response.Body);

	                                                                      
	const string sResponseFileName(getOperationContext().getLocalTmpDirForOperation() + "WebServiceResponse.xml");
	write_string_to_file(sResponseFileName, response.Body);

	ProcessedFile responseFile;
	responseFile.setFileChangeTimeNow();
	responseFile.setInHistory(false);
	responseFile.setLocalTargetLocation(sResponseFileName);
	responseFile.setSourceSize(request.Body.size());
	responseFile.setTargetSize(response.Body.size());
	responseFile.setTransferedNow();
	responseFile.setTransformationTime(0);	                                     

	ProcessedFiles files;
	files.add(responseFile);

	if( returnedSoapFault ) {
		StartNextOperation(false, files, sWarning);
		return;
	}
	StartNextOperation(true, files, "");
}

void OperationEngine::RunOnRequestPluginsForHostedWebServiceCall(std::string& sRequest) {
	RunPluginsForHostedWebServiceCall(sRequest, PIPELINE_POSITION_ON_REQUEST);
}

void OperationEngine::RunOnResponsePluginsForHostedWebServiceCall(std::string& sResponse) {
	RunPluginsForHostedWebServiceCall(sResponse, PIPELINE_POSITION_ON_RESPONSE);
}

void OperationEngine::RunPluginsForHostedWebServiceCall(string& sData, int iRelativePosition) {
	assert(m_pOperation);

	string sOperationName;
	const long lWebServiceId = konga::GetWebServiceId(&GetDbConnection(), m_pOperation->getId(), sOperationName);

	const long lTransformationId = -1;	                                           
	PluginManifestListT pluginsToRun = 
		m_pPipelinePluginSelector->GetPipelinePluginInstances(getOperationContext().getOperationId(), lTransformationId,
		WebServiceEntityId, lWebServiceId, iRelativePosition);
	if ( pluginsToRun.empty() )
		return;

	SortPipelinePlugins(pluginsToRun, WebServiceEntityId);

	PipelinePluginExecutor executor;
	for( PluginManifestListT::const_iterator cit = pluginsToRun.begin();
		cit != pluginsToRun.end(); ++cit )
	{
		PipelinePluginManifest* pManifest
			= dynamic_cast<PipelinePluginManifest*>(cit->get());
		if ( pManifest == 0 )
			continue;

		if ( !pManifest->ExecuteAtPipelinePosition(WebServiceEntityId, iRelativePosition) )
			continue;
		
		PipelinePosition pipelinePosition(WebServiceEntityId,
			iRelativePosition, 0, false);

		string tmpFileName(konga::getTmpFileName("wsRequestIn"));
		write_string_to_file(tmpFileName, sData);
		AutoDeleteFileSet autoDeleteFiles;
		autoDeleteFiles.AddFile(tmpFileName);

		vector<string> inputFiles;
		inputFiles.push_back(tmpFileName);
		bool bHasNewFiles = false;
		vector<OutputFile> outputFiles;
		executor.ExecutePipelinePlugin(*pManifest, pipelinePosition,
			getOperationContext(), getOperationDataElementHolder(), inputFiles,
			bHasNewFiles, outputFiles);
		if ( bHasNewFiles ) {
			if ( outputFiles.size() == 0 ) {
				throw CrException("The plugin '" + pManifest->GetDisplayName()
				+ "' produced no output. This is not allowed for a plugin called for a hosted web service.");
			}
			else if ( outputFiles.size() > 1 ) {
				throw CrException("The plugin '" + pManifest->GetDisplayName()
				+ "' produced " + toString(outputFiles.size()) + " output files. Only one output file is allowed for a plugin called for a hosted web service.");
			}

			sData.clear();
			read_file_to_string(outputFiles.at(0).Name, sData);
		}
	}
}

namespace {
	string CreateTmpDirectoryForWebServiceRequestFiles() {
		                                                 
		string dir( getTmpDir() + "ws_req" );
		kCreateDirectory(dir);
		AppendPathSeparator(dir);
		dir += GetGUIDString();
		kCreateDirectory(dir);
		AppendPathSeparator(dir);
		return dir;
	}
}

namespace {
	struct SiblingOperationInfo {
		SiblingOperationInfo(long operationId, const string& sTmpFileName)
			: m_lOperationId(operationId), m_sTmpFileName(sTmpFileName) {}

		long m_lOperationId;
		string m_sTmpFileName;
	};
}

                                                                                 
void OperationEngine::StartSiblingOperations(long hostedWebServiceOperationId,
											 const string& sRequest,
											 const bool bSiblingImplementsWS,
											 string& sResponse) {

	const string sql("SELECT SiblingOperation_ID FROM CommonSourceOperationsTab " \
		"WHERE Operation_ID = " + toString(hostedWebServiceOperationId)
		+ " ORDER BY ID");

	SmartDbPtr<DynamicDbRecordSet> pCommonOptsTab( DynamicDbRecordSet::MakeInstance(&GetDbConnection(), sql) );
	pCommonOptsTab->Open();

	                                                                                   
	                                       
	string dir;
	vector<SiblingOperationInfo> vSiblingOperations;
	while( pCommonOptsTab->Next() ) {
		if ( dir.empty() ) {
			dir = CreateTmpDirectoryForWebServiceRequestFiles();
		}

		const string& sOpId = pCommonOptsTab->GetFieldValue(0);
		const long operationId = toLong(sOpId);

		const string sTmpFileName(dir + "request_" + toString(vSiblingOperations.size()+1) + "-Op_" + sOpId);
		write_string_to_file(sTmpFileName, sRequest);
		vSiblingOperations.push_back(SiblingOperationInfo(operationId, sTmpFileName));
	}
	pCommonOptsTab.CloseAndDelete();

	bool isFirstSibling = true;
	for( vector<SiblingOperationInfo>::const_iterator cit = vSiblingOperations.begin(); cit != vSiblingOperations.end(); ++cit ) {
		const long operationId = cit->m_lOperationId;
		const string& sTmpFileName = cit->m_sTmpFileName;

		                                                            
		DataElement* de = getOperationDataElementHolder().getOperationDataElement(SourceFromHostedWebServiceCall::GLOBAL_DE_NAME_FOR_REQUEST_PATH);
		de->setType(DataElement::t_string);
		de->setValue(sTmpFileName.c_str());

		if ( bSiblingImplementsWS && isFirstSibling ) {
			                                                                
			                                            
			OperationEngine opEngine(m_CurrentUser, 0, &this->GetDbConnection());
			const string responseFile(dir + "response_" + toString(operationId));
			opEngine.saveTargetToThisFile(responseFile);
			OP_DEBUG_LOG("About to run operation " + toString(operationId) + " as a sibling operation that implements the hosted web service.");
			opEngine.RunOperation(operationId, -1, -1, m_pDeHolder, true);
			if ( FileExists(responseFile) ) {
				read_file_to_string(responseFile, sResponse, true);
			}
		}
		else {			
			SetOperationDataElements();
			const string stSubj("Triggered by hosted web service call operation \""
				+ m_pOperation->getName() + "\"");

			OP_DEBUG_LOG(stSubj);
			konga::AddOperationToQueue(0, &GetDbConnection(), operationId, m_lSessionId, UNDEFINED_OPERATION_INSTANCE_ID, "OperationEngine", stSubj, m_CurrentUser.GetId());
			m_lSessionId = -1;
		}
		isFirstSibling = false;
	}
}
