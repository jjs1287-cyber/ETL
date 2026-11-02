/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <string.h>		         

#include <map>
#include <vector>

#include <konga/Utils/FileUtils.h>	              
#include <konga/Utils/MapUtils.h>	                       

#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/ItemAccess/UserAccess.h>
#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/Server/Server.h>
#include <konga/Zip/ZipUtils.h>

#include <konga/TransformationEngine/TransformationEngine.h>

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "DeployInterchangeDataImpl.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::map;
using std::vector;

namespace {
	const char GENERATE_JTR_TAG[] = "<generate>";

	bool NeedToGenerate(const char* pch) {
		return ( strcmp(GENERATE_JTR_TAG, pch) == 0 );
	}

	void CreateJtrFile(const UserInfo& userInfo, ItemWarningHolder& warningHolder,
		long lTransformationId, bool isSource, const string& guid)
	{
		const string& sSourceFileName = isSource? GetSourceJtrFileName(lTransformationId): "";
		const string& sTargetFileName = isSource? "": GetTargetJtrFileName(lTransformationId);

		TransformationEngine transform(userInfo);
		UIBACKEND_TRACE("About to call TransformationEngine::CreateJtrFromMappings(<connection string>, "
			+ toString(lTransformationId) + ", '" + sSourceFileName + "', '" + sTargetFileName + "')");

		try {
			const unsigned long hr =
				transform.CreateJtrFromMappings(lTransformationId, sSourceFileName, sTargetFileName);
			if( hr != 0 ) {
				warningHolder.AddWarning(TransformationEntityId, guid, transform.getLastError());
			}
		}
		catch( const CrException& ex ) {
			warningHolder.AddWarning(TransformationEntityId, guid, ex.GetErrorString());
		}
	}

	typedef map<string,string> generateJtrMapT;

	void AddToGenerateMap(generateJtrMapT& m, long lTransformationId, bool isSource, const string& guid, const string& sFileName) {
		                                                                  
		kDeleteFile(sFileName);
		AddOrUpdate<generateJtrMapT, string, string>(m, konga::toString(lTransformationId)+(isSource?"S":"T"), guid);
	}

	void HandleJtr(const char* const pchJtr, const string& sFileName, generateJtrMapT& generateJtrForTheseTransformations,
		const long lTransformationId, bool isSource, const string& guid) {
		if ( NeedToGenerate(pchJtr) ) {
			AddToGenerateMap(generateJtrForTheseTransformations, lTransformationId, isSource, guid, sFileName);
		}
		else {
			ZipUtils::DecodeUnzipAndWriteToFile(sFileName, pchJtr, false);
		}
	}
}

CDeployResults* DeployInterchangeDataImpl::UploadJtrFiles(CJtrFiles* const pJtrFiles) {
	UIBACKEND_TRACE("DeployInterchangeDataImpl::UploadJtrFiles called.");
	                     
	if ( pJtrFiles == 0 )
		return 0;

	if ( pJtrFiles->getJtrFiles() == 0 )
		return 0;
	if ( pJtrFiles->getJtrFiles()->getitem() == 0 )
		return 0;

	int nJtrFiles = 0;
	CJtrFile** const arrJtrFile = pJtrFiles->getJtrFiles()->getitem()->get(nJtrFiles);
	if ( nJtrFiles < 1 || arrJtrFile == 0 )
		return 0;

	UIBACKEND_TRACE("Number of Jtr files for uploading: " + toString(nJtrFiles));

	                                      
	m_pTranDb.Attach( konga::GetBackEndDbConnection("TranDb") );
	m_pUserAccess = UserAccess::GetUserAccessWithGroup(m_UserInfo.GetId(), m_pTranDb);
	m_pGuidToIdMapper = new DbGuidToIdMapper(m_pTranDb);

	                                                                                                                               
	generateJtrMapT generateJtrForTheseTransformations;

	                                   
	for ( int iJtr = 0; iJtr < nJtrFiles; ++iJtr ) {
		if ( arrJtrFile[iJtr] == 0 )
			continue;

		CJtrFile& jtrFile = *arrJtrFile[iJtr];
		const string& guid( toString(jtrFile.getTransformationGuid()) );
		if ( guid.empty() )
			continue;

		const long lTransformationId = m_pGuidToIdMapper->GetId(guid, TransformationEntityId);
		if ( lTransformationId == GuidToIdMapper::invalid_id )
			continue;

		if ( !m_pUserAccess->HasWritePermission(lTransformationId, TransformationEntityId) ) {
			AddWarning(TransformationEntityId, guid, "The current user does not have write permission for this transformation.");
			continue;
		}

		              
		if ( jtrFile.getSourceJtrZippedBase64() != 0 ) {
			const char* const pchJtr = jtrFile.getSourceJtrZippedBase64();
			const string& sFileName = GetSourceJtrFileName(lTransformationId);
			HandleJtr(pchJtr, sFileName, generateJtrForTheseTransformations, lTransformationId, true, guid);
		}
		
		              
		if ( jtrFile.getTargetJtrZippedBase64() != 0 ) {
			const char* const pchJtr = jtrFile.getTargetJtrZippedBase64();
			const string& sFileName = GetTargetJtrFileName(lTransformationId);
			HandleJtr(pchJtr, sFileName, generateJtrForTheseTransformations, lTransformationId, false, guid);
		}
	}                                  

	                              
	for( generateJtrMapT::const_iterator cit = generateJtrForTheseTransformations.begin(); cit != generateJtrForTheseTransformations.end(); ++cit ) {
		const string temp = cit->first;
		int len = temp.size();
		assert(len>1);
		bool isSource = temp.at(len-1)=='S';
		long lTransformationId = atol(temp.substr(0,len-1).c_str());
		const string& guid = cit->second;
		CreateJtrFile(m_UserInfo, *this, lTransformationId, isSource, guid);
	}

	                               
	const vector<ItemWarning>& warnings = GetWarningsRef();
	if ( !warnings.empty() ) {
		UIBACKEND_TRACE("There were " + toString(warnings.size())
			+ " warnings in DeployInterchangeDataImpl::UploadJtrFiles");
		vector<ItemWarning>::const_iterator cit = warnings.begin();
		for(; cit != warnings.end(); ++cit) {
			UIBACKEND_TRACE("Warning for tran " + cit->GetGuid() + ": "
				+ cit->GetWarning());
		}
	}

	const int iStatus = warnings.empty() ? 0 : 1;
	UIBACKEND_TRACE("About to return from DeployInterchangeDataImpl::UploadJtrFiles");
	return AllocateDeployResults(iStatus, *this);
}

