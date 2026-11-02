/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <boost/scoped_array.hpp>

#include <konga/Server/Server.h>
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/AutoPtrCleanup.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/util.h>
#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/StringUtils.h>	                  

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/TranDb/OperationsTab.h>

#include <konga/DbRelations/TransformationTypeIdConstants.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include <konga/UiBackend/GuidMappings/GuidMappings.h>
#include <konga/WebService/WsdlConstants.h>
#include <konga/OperationContext/OperationContext.h>

#include <konga/ItemProperties/ItemProperties.h>
#include <konga/ItemProperties/Definitions.h>

#include <konga/Server/WDIPathFinder.h>

#include <konga/TransformationUtils/TransformationWebServiceInfo.h>
#include <konga/TransformationUtils/Definitions.h>
#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>

#include "ErrorCodes/resource.h"
#include "TreeMapper.h"
#include "JtrReader.h"


using namespace std;
using namespace TranDb;

namespace {
	const std::string getAttributeValue(const char* line, const std::string& attributeName){
		konga::StringTokenizer tk(line, "\"= ");
		int found=0;
		while(tk.NextToken()){
			std::string token(tk.token());
			switch(found){
				case 0:
					if(token==attributeName)found=1;
					break;
				case 1:
					if(token=="=")found=2;
					break;
				case 2:
					if(token=="\""){
						std::string value;
						while(tk.NextToken()){
							token.assign(tk.token());
							if(token=="\"")break;
							value+=token;
						}
						return value;
					}
			}
		}
		return "";
	}
	bool outOfDate(const std::string& inputStructFileName){
		const int maxlen=1000;
		FILE* in=fopen(inputStructFileName.c_str(), "r");
		if(in==0)return true;
		konga::AutoFileHandle adopt_in(in);
		char* line=new char[maxlen+1];
		boost::scoped_array<char> adopt_line(line);
		if(fgets(line, maxlen, in)==0)return true;
		if(strncmp(line,"<?xml ", 6)==0)fgets(line, maxlen,in);
		if(line!=0 && strncmp(line, "<JTR ",5)==0){
			std::string type=getAttributeValue(line, JTR_TYPE);
			std::string version=getAttributeValue(line, JTR_VERSION);
			int iv=version.empty()? 0: atoi(version.c_str());
			return (iv < getCurrentVersion(type));
		}
		return true;
	}
}


void TreeMapper::WriteTransformationLogHeader(const string& stInputFileName_in) {
	const string stInputFileName( stInputFileName_in.empty() ? m_inputDataFileName : stInputFileName_in );

	CViewLog* const pTranViewLog = getViewLog();
	if( pTranViewLog == 0 )
		return;

	const konga::CrDateTime SysTime;
	if( pTranViewLog->IsDebugLogEnabled() )
	{
		pTranViewLog->DebugString("\n-------------------- %d/%d/%d %d:%d:%d -------------------------\n",
					SysTime.GetMonth(), SysTime.GetDay(), SysTime.GetYear(), SysTime.GetHour(), SysTime.GetMinute(), SysTime.GetSecond());
		pTranViewLog->DebugString("1> TreeMapper::MapByTransformationID - Begin\n");
#ifdef _DEBUG
		pTranViewLog->DebugString("DEBUG BUILD\n");
#endif
		pTranViewLog->DebugString("TransformationID = %d\n inputDataFileName = %s\n outputDataFileName = %s\nsLoadSourceFilter = %s\n",
			m_TransformationID, stInputFileName.c_str(), (const char*)m_outputDataFileName, (const char*)m_loadSourceFilter);
		if(m_bTestTransformation)
		{
			pTranViewLog->DebugString("\n* This is a test transformation %s *\n", isTestOperation() ? "called by a test operation" : "");
		}
		if( !stInputFileName.empty() )
		{
			FILE *pSrcFile = 0;

			pSrcFile = fopen(stInputFileName.c_str(), "r");
			if (pSrcFile!=0)
			{
				fseek(pSrcFile, 0L, SEEK_END);
				long lSrcSize = ftell(pSrcFile);
				fclose(pSrcFile);

				if (lSrcSize > 0)
				{
					KString WDIPath;
					if ( getViewLog()->IsDebugLogEnabled() && konga::WDIPathFinder::GetWDIDebugPath(WDIPath) ) {
						WDIPath += getTransformationName__("_Source");
						pTranViewLog->DebugString("Copying Source file %s as %s\n (file size: %d)", stInputFileName.c_str(), (const char*)WDIPath, lSrcSize);
						getViewLog()->debugCopyFile("WriteTransformationLogHeader: Input File", stInputFileName, (const char*)WDIPath, false);
						konga::touch_file((const char*)WDIPath);
					}
				}
				else
					pTranViewLog->DebugString("The file %s has size = 0\n", stInputFileName.c_str());
			}
			else
				pTranViewLog->DebugString("Unable to open the file %s\n", stInputFileName.c_str());
		}
		else
			pTranViewLog->DebugString("No source file to copy\n");
	}
}

                                                            
                                                               
void TreeMapper::HandleNoMappings() {
	if(m_bMappingPresent) return;
	if(m_sourceType==NoSourceTypeId)return;
	if(isLoadSourceData())return;
	if( m_targetType == WebServiceInputTypeId && !isTestTransformation()) {
		                                                                                  
		                                                                      
		                                      
	}
	else {
		throw konga::CrException(std::string("The transformation: \"") + getTransformationName() + "\" has no mappings!");
	}
}

void TreeMapper::InitializeStructureParsers(bool always_from_structure_file, bool bDoSource, bool bDoTarget)
{
	PROFILING doPrf(this,"InitializeStructureParsers");
	unsigned long hr = S_OK;
	int err=0;
	KString tmp;

	getViewLog()->DebugString("TreeMapper::InitializeStructureParsers() always_from_structure_file=%s, inputStructFileName=%s, outputStructureFileName=%s\n",
		(always_from_structure_file?"true":"false"),
		bDoSource?((const char*)m_inputStructFileName):"",
		bDoTarget?((const char*)m_outputStructFileName):"");
	if(bDoSource){
		if( m_XmlSource )
		{
			const string file_extension(konga::GetFileExtension((const char*)m_inputStructFileName));
			const bool isXsdFile = konga::EqualsIgnoreCase(file_extension, "xsd");
			const string& jtr_file_name=konga::GetSourceJtrFileName(m_TransformationID);
			if(always_from_structure_file || outOfDate(jtr_file_name)){
				const bool bValidateParser = ((m_sourceType == WebServiceInputTypeId || m_sourceType == WebServiceOutputTypeId) && m_TransformationWebServiceInfo) ?
					(m_TransformationWebServiceInfo->SchemaStatus == konga::WsdlConstants::Valid || m_TransformationWebServiceInfo->SchemaStatus == konga::WsdlConstants::Unknown) : true;
				m_sourceDTDParser->SetValidateSchemaOnCreate(bValidateParser);
				getViewLog()->DebugString("Source: bValidateParser=%s\n",bValidateParser?"true":"false");
				if(stricmp(file_extension.c_str(),"dtd")==0){
					err=m_sourceDTDParser->createJtrFromDtd((const char*)m_inputStructFileName, (const char*)m_sourceDTD_Root, jtr_file_name.c_str());
					if(err!=0){
						hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_TREEMAPPER_SOURCEDTD_CREATE_FAILED, (const char*)m_inputStructFileName, (const char*)m_sourceDTDParser->m_lastErr);
						KString sErr(getErrorLog()->GetLastErr());
						throw konga::CrException(std::string("createJtrFromDtd for source structure failed.\n")+(const char*)sErr);
					}
				}
				else if(isXsdFile){
					hr=m_sourceDTDParser->createJtrFromXSD((const char*)m_inputStructFileName, (const char*)m_sourceDTD_Root, jtr_file_name.c_str());
					if(hr!=S_OK){
						m_lastErr=m_sourceDTDParser->m_lastErr;
						throw konga::CrException(std::string("createJtrFromXSD for source failed.\n")+(const char*)m_lastErr);
					}
				}
				else {
					m_lastErr=KString("Unrecognized file extension for source structure file: ")+m_inputStructFileName;
					throw konga::CrException(std::string("createJtrFromXSD failed.\n")+(const char*)m_lastErr);
				}
			}
			else {
				getViewLog()->DebugString("CreateCromFromJtr for source - jtr_file_name:%s\n", jtr_file_name.c_str());
				m_sourceDTDParser->CreateCromFromJtr(jtr_file_name.c_str());
			}
		
			m_sourceXmlParser.SetDTDParserPtr(m_sourceDTDParser);
			m_sourceXmlParser.SetDocType(CRXml_bit_Source);
			m_sourceXmlParser.SetStructType(m_inputStructFileName);
			if( m_sourceType == WebServiceInputTypeId || m_sourceType == WebServiceOutputTypeId )
				m_sourceXmlParser.AddDocType(CRXml_bit_WebService);
			m_sourceXmlParser.setupPatternMatchingFlag();
		} 
		else 
		{
			if(m_sourceType!=NoSourceTypeId){
				m_inputStructFileName=konga::GetSourceJtrFileName(m_TransformationID);
			}
			if(!m_inputStructFileName.IsEmpty()){
				if(outOfDate((const char*)m_inputStructFileName)){
					getViewLog()->DebugString("input structure file is out of date, filename:\"%s\"\n", (const char*)m_inputStructFileName);
					if(m_sourceType==DbTypeId)
						m_sourceDTDParser->CreateJtrForDB(m_TransformationID, 1, (const char*)m_inputStructFileName);
					else if(m_sourceType==TextTypeId || m_sourceType == ComplexTextTypeId)
						m_sourceDTDParser->CreateJtrForTextDocument(m_source_text_doc_id, (const char*)m_inputStructFileName);
				}
				m_sourceDTDParser->CreateCromFromJtr((const char*)m_inputStructFileName);
				                                                           
				CROM*root=m_sourceDTDParser->FindRoot();
				if(IsFlatSource() && root && !root->IsFlat())root->SetFlatFlag();
			}
			if(m_sourceType==DbTypeId){
				getViewLog()->DebugString("ReplaceGlobalDataElementsInConditions.\n");
				                                                                        
			}
		}
		if ( m_sourceDTDParser != 0 && !IsNoSource()) m_sourceDTDParser->FindRoot()->checkLoopNode();
	}

	if(bDoTarget){
		if( m_XmlTarget )
		{
			const string file_extension(konga::GetFileExtension((const char*)m_outputStructFileName));
			const bool isXsdFile = konga::EqualsIgnoreCase(file_extension, "xsd");
			GetTargetDTDParser();	                                                  
			assert(m_targetDTDParser!=0);

			const string& jtr_file_name=konga::GetTargetJtrFileName(m_TransformationID);
			if(always_from_structure_file || outOfDate(jtr_file_name)){
				const bool bValidateParser = ((m_targetType == WebServiceInputTypeId || m_targetType == WebServiceOutputTypeId) && m_TransformationWebServiceInfo) ?
					(m_TransformationWebServiceInfo->SchemaStatus == konga::WsdlConstants::Valid || m_TransformationWebServiceInfo->SchemaStatus == konga::WsdlConstants::Unknown) : true;
				m_targetDTDParser->SetValidateSchemaOnCreate(bValidateParser);
				getViewLog()->DebugString("Target: bValidateParser=%s\n",bValidateParser?"true":"false");
				if(stricmp(file_extension.c_str(),"dtd")==0){
					err=m_targetDTDParser->createJtrFromDtd((const char*)m_outputStructFileName, (const char*)m_targetDTD_Root, jtr_file_name.c_str());
					if(err!=0){
						hr = getErrorLog()->MakeError(__FILE__, __LINE__, ID_TREEMAPPER_TARGETDTD_CREATE_FAILED, (const char*)m_outputStructFileName, (const char*)m_targetDTDParser->m_lastErr);
						throw konga::CrException(std::string("createJtrFromDtd for target structure failed.\n")+(const char*)getErrorLog()->GetLastErr());
					}
				}
				else if(isXsdFile){
					hr=m_targetDTDParser->createJtrFromXSD((const char*)m_outputStructFileName,(const char*)m_targetDTD_Root, jtr_file_name.c_str());
					if(hr!=S_OK){
						m_lastErr=m_targetDTDParser->m_lastErr;
						throw konga::CrException(std::string("createJtrFromXSD for target failed.\n")+(const char*)m_lastErr);
					}
				}
				else {
					m_lastErr=KString("Unrecognized file extension for target structure file: ")+m_outputStructFileName;
					throw konga::CrException(std::string("createJtrFromXSD failed.\n")+(const char*)m_lastErr);
				}
			}
			else {
				getViewLog()->DebugString("CreateCromFromJtr for target - jtr_file_name:%s\n", jtr_file_name.c_str());
				m_targetDTDParser->CreateCromFromJtr(jtr_file_name.c_str());
			}

			m_targetXmlParser.SetDTDParserPtr(m_targetDTDParser);
			m_targetXmlParser.AddDocType(CRXml_bit_Target);
			m_targetXmlParser.SetStructType(m_outputStructFileName);
			if( m_targetType == WebServiceInputTypeId || m_targetType == WebServiceOutputTypeId )
				m_targetXmlParser.AddDocType(CRXml_bit_WebService);
			if(!m_bNumForBool){
				m_targetDTDParser->FindRoot()->setBoolTypeFormat("%s");
			}
		} 
		else 
		{
			const string& jtr_file_name=konga::GetTargetJtrFileName(m_TransformationID);
			m_outputStructFileName=jtr_file_name;

			if(!m_outputStructFileName.IsEmpty()){
				if(outOfDate((const char*)m_outputStructFileName)){
					getViewLog()->DebugString("output structure file is out of date, filename:\"%s\"\n", (const char*)m_outputStructFileName);
					if(m_targetType == DbTypeId)
						m_targetDTDParser->CreateJtrForDB(m_TransformationID, 2, (const char*)m_outputStructFileName);
					else if(m_targetType == TextTypeId || m_targetType == ComplexTextTypeId)
						m_targetDTDParser->CreateJtrForTextDocument(m_target_text_doc_id, (const char*)m_outputStructFileName);
				}
				m_targetDTDParser->CreateCromFromJtr(m_outputStructFileName);
				                                                           
				CROM*root=m_targetDTDParser->FindRoot();
				if(IsFlatTarget() && root && !root->IsFlat())root->SetFlatFlag();
			}
		}
		if ( m_targetDTDParser != 0 ) m_targetDTDParser->FindRoot()->checkLoopNode();
	}
}

void TreeMapper::collectSourceDataElements(CROM*dtd){
	if(dtd==0){
		return;
	}
	const std::string path( (const char*)dtd->get_Path() );
	m_source_de_set.insert(path);
	for(CROM*child=dtd->GetFirstChild(); child!=0; child=child->GetNextSibling()){
		collectSourceDataElements(child);
	}
}

unsigned long TreeMapper::PrepareDbSource(bool &bReturn)
{
	PROFILING doPrf(this,"PrepareDbSource");
	unsigned long hr = S_OK;
	bReturn = false;

	bool bDeletedColumns = false;
	if(m_bModifyDBSourceSQL){
		                                                    
		if(m_NatureOfSource=="Flat"){
			MarkSourceUpdateColumns(m_sourceDTDParser);
			m_sourceDTDParser->MarkOrderByColumns(m_sSourceSQL);
		}
		                                                                                                                                
		getViewLog()->DebugString("\nMapByTransformationID source structure tree\n");
		m_sourceDTDParser->write(getViewLog()->GetDebugLog());
	}

	if(m_NatureOfSource=="Flat"){
		if(bDeletedColumns){
			                                                                   
			int iPos = m_sSourceSQL.Find('*');
			if(iPos != -1){
				string sBeginStr,sEndStr;
				getBeginEndStringForTable(this,m_sSourceSQL,sBeginStr,sEndStr);
				m_sSourceSQL.Delete(iPos, 1);
				m_sSourceSQL.Insert(iPos, m_sourceDTDParser->CreateDbSelectList(sBeginStr, sEndStr));
			}
		}
		if(m_loadSourceFilter.GetLength()>0){
			KString sql=m_sSourceSQL;
			sql.MakeLower();
			const int iWherePos = sql.Find(" where ");
			if(iWherePos>=0) {
				m_sSourceSQL=m_sSourceSQL.Left(iWherePos);
			}
			m_sSourceSQL+=" WHERE "+m_loadSourceFilter;
		}
	}
	return hr;
}

unsigned long TreeMapper::PrepareSpecialSourceTypes(bool& bReturn)
{
	bReturn = false;
	if( m_sourceType == WebServiceInputTypeId )
	{
		                                                                               
		                                                                                      
		                                                                                        
		                
		unsigned long hr = PrepareHostedWebServiceInput();
		if( FAILED(hr) )
		{
			bReturn = true;
			return hr;
		}
	}
	else if( m_sourceType == DbTypeId )
	{
		unsigned long hr = PrepareDbSource(bReturn);
		if( FAILED(hr) )
		{
			return hr;
		}
	}
	return S_OK;
}

unsigned long TreeMapper::CheckBulkTransform(bool& bIsBulkTransform, bool& bReturn)
{
	PROFILING doPrf(this,"CheckBulkTransform");
	unsigned long hr=S_OK;
	bReturn = false;
	KString tmp;
	bIsBulkTransform = IsFlatSource() && IsFlatTarget();
	if( bIsBulkTransform )
	{
		                              
	}
	else
	{
		                                        
	}
	return hr;
}

void TreeMapper::SetTransformationChainItemInfo(const TransformationChainItem& item, long previousTransformationId)
{
	m_TransformationID = item.TransformationId;
	m_NextTransformation_ID = item.NextTransformationId;
	m_PreviousTransformation_ID = previousTransformationId;
	m_Function_ID = item.FunctionId;

	if ( m_TransformationWebServiceInfo ) {
		delete m_TransformationWebServiceInfo;
		m_TransformationWebServiceInfo = 0;
	}

	if ( !item.WebServiceInfo.Url.empty() || !item.WebServiceInfo.RequestXsd.empty() || !item.WebServiceInfo.ResponseXsd.empty() ) {
		m_TransformationWebServiceInfo = new konga::TransformationWebServiceInfo(item.WebServiceInfo);
		m_webServiceStructure = new WebServiceStructure(this,
						m_TransformationWebServiceInfo->RequestXsd,
						m_TransformationWebServiceInfo->RequestJtr,
						m_TransformationWebServiceInfo->ResponseXsd,
						m_TransformationWebServiceInfo->ResponseJtr);	
	}
}

void TreeMapper::ReplaceGlobalDataElementsInConditions(CROM*pNode)
{
	KString sqlFilter(pNode->get_sqlFilter());
	if(!sqlFilter.IsEmpty()){
		sqlFilter = replaceGlobalDataElementsInString((const char*)sqlFilter);
		pNode->set_sqlFilter(sqlFilter);
	}
	KString sqlOrderBy(pNode->get_sqlOrderBy());
	if(!sqlOrderBy.IsEmpty()){
		sqlOrderBy = replaceGlobalDataElementsInString((const char*)sqlOrderBy);
		pNode->set_sqlOrderBy(sqlOrderBy);
	}
	for(CROM*pChild=pNode->GetFirstChild(); pChild; pChild=pChild->GetNextSibling()){
		if(!pChild->IsLeaf()){
			ReplaceGlobalDataElementsInConditions(pChild);
		}
	}
}
