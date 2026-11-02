/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
                                                                                               
                                                     

#include <boost/scoped_array.hpp>

#include <konga/Utils/CsvUtils.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/util.h>

#include <konga/Server/Server.h>
#include <konga/Server/WDIPathFinder.h>

#include <konga/Db/DbUtils/DbException.h>
#include <konga/Db/DbUtils/DbUtils.h>

#include <konga/DataElement/DataElement.h>

#include <konga/TransformationLogging/ErrorLog.h>
#include <konga/TransformationLogging/ViewLog.h>

#include "ErrorCodes/resource.h"
#include "ProfilingSections.h"
#include "TreeMapper.h"
#include "TextDocument.h"
#include "DataElementUtils.h"
#include "TransformationEngine.h"
#include "misc.h"
#include "CROMData.h"

using namespace konga;
using std::string;

KString OverideDbFilter(const KString& originalSql, const KString& overRideParams) {
	if (overRideParams.GetLength() == 0) {
		return originalSql;
	}
	KString sql = originalSql;
	sql.MakeLower();
	int wherePos = sql.Find(" where ");
	if (wherePos >= 0) {
		return originalSql.Left(wherePos) + " WHERE " + overRideParams;
	}
	else {
		return originalSql + " WHERE " + overRideParams;
	}
}

unsigned long TreeMapper::TestTransformation(long lTransformationID, const string& stInputFileName, const string& stOutputFileName, const string& sLinkFileName)
{
	m_bTestTransformation = true;
	int	nTransformationStatus = 0;
	unsigned long hr = S_OK;
	{
		SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
		hr = InitTransformation(*pTranDb, lTransformationID, nTransformationStatus, true);
		if(FAILED(hr))return hr;
	}

	                                   
	if ( m_sourceType!=NoSourceTypeId && !konga::FileExists(stInputFileName) ) {
		throw CrException("No source file is available for testing. The loaded source may have expired, please load the source again.");
	}

	TransformationStatistics stats;
	return doTransformation(stInputFileName, stOutputFileName, sLinkFileName, nTransformationStatus, stats);
}

unsigned long TreeMapper::LoadSource(const std::string& sInputFileName) {
	unsigned long hr = S_OK;
	if ( (m_sourceType == XmlTypeId) || 
					  (m_sourceType == RosettanetTypeId) || 
					  (m_sourceType == WebServiceInputTypeId) ||
					  (m_sourceType == WebServiceOutputTypeId)){
		bool bValidateSourceXml = GetValidateSourceXml();
		if(m_sourceXmlParser.Read(m_TransformationID, true, sInputFileName.c_str(), (const char*)m_inputStructFileName, (const char*)m_sourceDTD_Root, bValidateSourceXml)!=S_OK)
		{
			const string sErr = (const char*)m_sourceXmlParser.m_LastErr;
			AppendErrorMessage(sErr);
			getViewLog()->DebugString("%s", sErr.c_str());
			throw konga::CrException(sErr);
		}
	}
	else if ( m_sourceType==DbTypeId ) {
		                                                                                    
		string sWhereClause;
		konga::read_file_to_string(sInputFileName, sWhereClause, true);
		m_loadSourceFilter = getOperationDataElementHolder()->replaceOperationDataElementInString(sWhereClause);

		hr = dumpDatabaseSourceToFile(sInputFileName, m_loadSourceFilter);
		if(FAILED(hr))return hr;
	}
	else if(m_sourceType == LdapTypeId) {
		                                      
		hr=dumpLdapSourceToFile(sInputFileName);
		if(FAILED(hr))return hr;
	}

	return hr;
}
