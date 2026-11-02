/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <vector>
#include <string>

#ifdef WIN32
	#include <windows.h>
#endif
#include <sqlext.h>

#include <boost/scoped_array.hpp>

#include <konga/Utils/CsvUtils.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/util.h>

#include <konga/TransformationUtils/Definitions.h>
#include <konga/DataElement/DataElement.h>

#include <konga/Server/WDILogFileUtils.h>
#include <konga/Server/WDIPathFinder.h>
#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>
#include <konga/TransformationLogging/ErrorConfiguration.h>

#include "ErrorCodes/resource.h"
#include "TreeMapper.h"
#include "DTDParser.h"
#include "TxtParser.h"
#include "CRXmlParser.h"

using namespace std;
using namespace konga;


unsigned long TreeMapper::DoStreamingTransform(FILE*lnk, const string& stInputFileName)
{
	PROFILING doPrf(this,"DoStreamingTransform");
	unsigned long hr = S_OK;

	getViewLog()->DebugString("TreeMapper::DoStreamingTransform - BEGIN\n");

	CROM*pTarget=m_targetDTDParser->FindFlatNode();
	assert(pTarget!=0 && !pTarget->IsLeaf());
	StreamingTransform streamingTransform(this, pTarget, stInputFileName);

	try {
		if(m_bNoSourceInMapping){
			if(streamingTransform.TransformRecord()==1){
				streamingTransform.writeRecord(lnk);
			}
		}
		else if(!m_XmlSource){
		                                                                                                            
		                                                                                   
		                                                                
			char strQualChar_src='"', numQualChar_src=0, delimiterChar_src=DB_DELIMITER_CHAR;
			if ( m_sourceType == TextTypeId ){
				CROM*pFlatNode=m_sourceDTDParser->FindFlatNode();
				strQualChar_src=pFlatNode->get_strQual();
				numQualChar_src=pFlatNode->get_numQual();
				delimiterChar_src=pFlatNode->get_delimiter();
			}
			if(getViewLog()->IsDebugLogEnabled()) {
				getViewLog()->DebugString("Calling ProcessFlatStream\nInFileName = '%s'\nOutFileName = '%s'\nSourceStrQual = '%c'\nSourceNumQual = '%c'\nSourceDelimiterStr = '%c'\n",
									stInputFileName.c_str(),
									(const char*)m_outputDataFileName, 
									strQualChar_src,
									numQualChar_src,
									delimiterChar_src);
			}
			const int nRetVal = streamingTransform.ProcessFlatStream(lnk, m_lTxtSrcRecords, m_lTxtTgtRecords);
			m_bErrorsInOutput = (streamingTransform.getTrans()->m_partial_err > 0);

			if ( nRetVal !=0 ) 
			{
				                               
				if (nRetVal == TRANSFORM_SOURCE_EMPTY)
				{
					m_bSourceIsEmpty = true;
					AppendErrorMessage("The source is empty.");

				   #ifdef WIN32
					hr = MAKE_HRESULT(SEVERITY_ERROR, FACILITY_ITF, ID_BULKTRANSFORM_SOURCE_EMPTY);
				   #else
					hr=E_FAIL|ID_BULKTRANSFORM_SOURCE_EMPTY;
				   #endif
				}
				else
				{
					hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_DOBULKTRANSFORM_GENERAL_FAILURE, (const char*)streamingTransform.m_lastErr);
					m_lastErr=getErrorLog()->GetLastErr();
				}
				return hr ;
			}
		}
		else {               
			CROM* target_root=m_targetDTDParser->FindRoot();
			CROM* transaction=target_root->GetFirstChild()->m_Generator;
			CROM* source_root=m_sourceDTDParser->FindRoot();
			source_root->resetChildInstanceNumbers();
			streamingTransform.StartSAXParser(stInputFileName.c_str(), transaction, source_root, GetValidateSourceXml(), lnk, m_sourceDTDParser, m_lTxtSrcRecords, m_lTxtTgtRecords);
		}
	}
	catch(const konga::CrException& e){
		std::string err_msg=e.GetErrorString()+"\nProcessFlatStream error\n";
		throw konga::CrException(err_msg);
	}

	if(getViewLog()->IsDebugLogEnabled()){
		if(getOutputDataFileName()!=""){
			getViewLog()->debugCopyFile("DoStreamingTransform:getOutputDataFileName()",getOutputDataFileName(), getDebugFileName()+".out");
		}
	}

	long lErr = 0;
	long long lSize = konga::GetFileSize(stInputFileName, lErr);
	if ( lErr == 0 ) m_lTxtSrcSize = lSize;
	lSize = konga::GetFileSize((const char*)m_outputDataFileName, lErr);
	if ( lErr == 0 ) m_lTxtTgtSize = lSize;

	getViewLog()->DebugString("TreeMapper::DoStreamingTransform - END\n");
	return S_OK;
}

CROM* TreeMapper::getSourceFlatFolder(){
	assert(IsFlatSource());
	CROM* pSourceFlatFolder=m_sourceDTDParser->m_root->GetFirstChild();
	assert(pSourceFlatFolder!=0 && !pSourceFlatFolder->IsLeaf());
	return pSourceFlatFolder;
}

