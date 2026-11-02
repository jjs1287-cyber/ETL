/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <string>
#include <sstream>	               

#include <konga/Utils/CrException.h>
#include <konga/Utils/PortDefs.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/util.h>		                  

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/TranDb/WebServicesTab.h>
#include <konga/Db/TranDb/TransformationChainLinksTab.h>

#include <konga/DbRelations/TransformationTypeIdConstants.h>
#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/FunctionIdMapper.h>

#include <konga/ItemAccess/UserAccess.h>

#include <konga/UiBackend/UploadedFilesManager/UploadedFilesManager.h>
#include <konga/UiBackend/UploadedFilesManager/FileUploadUtils.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include "../InterchangeDataTable.hpp"

#include "DeployUtils.h"	                   

#include "SourceTargetXmlHandler.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::string;
using std::stringstream;

namespace {
	string toHostedXsd(const string& stXsd) {
		string::size_type ipos = stXsd.find_last_of('.');
		if ( ipos == string::npos )
			throw CrException("XSD file path " + stXsd  + " is illegal. It has no file extension.");

		string ret(stXsd);
		ret.insert(ipos, "_host");
		return ret;
	}
}

namespace {
	const char c0[] = "transformation_id";
	const char c1[] = "source_type_id";
	const char c2[] = "target_type_id";
	const char c3[] = "source_xml";
	const char c4[] = "target_xml";
}

namespace {
	string GetUserGuid(const UserInfo& user, const GuidToIdMapper& guidMapper) {
		const string& stGuid = guidMapper.GetGuid(user.GetId(), UserEntityId);
		if ( stGuid.empty() )
			throw CrException("Internal Error: Failed to find the GUID for user \""
				+ user.GetLogin() + "\".");
		return stGuid;
	}
}

std::string SourceTargetXmlHandler::GetXmlSchemaPath(const std::string& stClientPath) {
	UploadedFilesManager fileManager(m_dbConnection);

	const string& stUserGuid = GetUserGuid(m_userInfo, m_guidToIdMapper);
	string stPath, stName;
	GetServerPath(stClientPath, stUserGuid, stPath, stName);
	
	UploadedFile uploadedFile(stPath, stName);
	if ( !fileManager.ExistsFile(uploadedFile, uploadedFile, true) ) {
		throw CrException("The XML schema file \"" + stClientPath + "\" does not exist on the server." \
			" Please upload all the XML schemas needed for this project or deploy your whole project again.");
	}
	const string& stGuid = uploadedFile.GetGuid();

	long lXmlId = m_guidToIdMapper.GetId(stGuid, XmlEntityId);
	if ( lXmlId == GuidToIdMapper::invalid_id ) {
		throw CrException("Internal Error: Failed to find the XML id for the file that should exist:\n"
			+ uploadedFile.GetCanonicalPath() + uploadedFile.GetName());
	}

	if ( !m_userAccess.HasReadPermission(lXmlId, XmlEntityId) ) {
		throw CrException("The current user does not have read permission for the XML file \""
			+ uploadedFile.GetCanonicalPath() + uploadedFile.GetName() + "\".");
	}

	return getRelativePath(uploadedFile.GetFullAbsolutePath());
}

void SourceTargetXmlHandler::HandleTransformations(InterchangeDataTableStruct& tranTabStruct) {

	if ( tranTabStruct.m_vRows.empty() )
		return;

	int iColPosTransformationId = -1;
	int iColPosSource_Type_ID = -1, iColPosTarget_Type_ID = -1;
	int iColPosSourceXml = -1, iColPosTargetXml = -1;

	const vector<string>& arr = tranTabStruct.m_vColumnNames;
	vector<string>::const_iterator citCols = arr.begin();
	int iCol = 0;
	for(; citCols != arr.end(); ++citCols, ++iCol ) {
		if ( iColPosTransformationId == -1 && strcasecmp(citCols->c_str(), c0) == 0 )
			iColPosTransformationId = iCol;
		else if ( iColPosSource_Type_ID == -1 && strcasecmp(citCols->c_str(), c1) == 0 )
			iColPosSource_Type_ID = iCol;
		else if ( iColPosTarget_Type_ID == -1 && strcasecmp(citCols->c_str(), c2) == 0 )
			iColPosTarget_Type_ID = iCol;
		else if ( iColPosSourceXml == -1 && strcasecmp(citCols->c_str(), c3) == 0 )
			iColPosSourceXml = iCol;
		else if ( iColPosTargetXml == -1 && strcasecmp(citCols->c_str(), c4) == 0 )
			iColPosTargetXml = iCol;
	}

	if ( iColPosTransformationId < 0 )
		throw CrException("Internal Error: Failed to find column position for transformation_id.");
	if ( iColPosSource_Type_ID < 0 )
		throw CrException("Internal Error: Failed to find column position for source_type_id.");
	if ( iColPosTarget_Type_ID < 0 )
		throw CrException("Internal Error: Failed to find column position for target_type_id.");
	if ( iColPosSourceXml < 0 )
		throw CrException("Internal Error: Failed to find column position for source_xml.");
	if ( iColPosTargetXml < 0 )
		throw CrException("Internal Error: Failed to find column position for target_xml.");

	                                                                           
	vInterchangeDataRowStructT::const_iterator citRows = tranTabStruct.m_vRows.begin();
	for(; citRows != tranTabStruct.m_vRows.end(); ++citRows ) {
		const InterchangeDataRowStruct& row = *citRows;

		int iSourceTypeId = -1, iTargetTypeId = -1;
		const NullableString& tranId = row.m_vFields.at(iColPosTransformationId);
		if ( tranId == 0 || tranId.str().empty() )
			throw CrException("Bad Data: TransformationId is null or empty.");
		const int iTransformationId = atol(tranId.str().c_str());

		const NullableString& souceTypeId = row.m_vFields.at(iColPosSource_Type_ID);
		if ( souceTypeId != 0 && !souceTypeId.str().empty() ) {
			iSourceTypeId = atoi(souceTypeId.str().c_str());
		}

		const NullableString& targetTypeId = row.m_vFields.at(iColPosTarget_Type_ID);
		if ( targetTypeId != 0 && !targetTypeId.str().empty() ) {
			iTargetTypeId = atoi(targetTypeId.str().c_str());
		}

		string stSourceXml, stTargetXml;

		if ( iSourceTypeId == WebServiceInputTypeId ||
			 iSourceTypeId == WebServiceOutputTypeId ) {
			stringstream ssWhere;
			ssWhere << "WHERE nexttransformation_id = " << iTransformationId
				    << " AND operation_id IS NULL";
			SmartDbPtr<TranDb::TransformationChainLinksTab> pTransformationChainLinksTab( TranDb::TransformationChainLinksTab::MakeInstance(&m_dbConnection) );
			pTransformationChainLinksTab->Select(ssWhere.str());
			if ( !pTransformationChainLinksTab->Next() )
				throw CrException("No TransformationChainLinkTab entry with NextTransformationId = " + toString(iTransformationId)
				+ ". This is probably a bug.");

			int iWebServiceTranId = pTransformationChainLinksTab->GetTransformation_ID();
			int iWebServiceId = m_functionIdMapper.GetWebServiceIdForTransformation(iWebServiceTranId);
			
			SmartDbPtr<TranDb::WebServicesTab> pWebServicesTab( TranDb::WebServicesTab::MakeInstance(&m_dbConnection) );
			pWebServicesTab->Select("WHERE WebService_Id = " + toString(iWebServiceId));
			if ( !pWebServicesTab->Next() )
				throw CrException("There is no WebService defined with WebServiceId = " + toString(iWebServiceId)
				+ ". This is probably a bug.");

			if ( iSourceTypeId == WebServiceInputTypeId ) {
				                                                                         
				if ( pWebServicesTab->IsInputXSDFileNameNull() )
					throw CrException("InputXSDFileName is null for WebService " + toString(iWebServiceId)
					+ ". This is probably a bug.");
				stSourceXml = toHostedXsd(pWebServicesTab->GetInputXSDFileName());
			}
			else {
				                                                                         
				if ( pWebServicesTab->IsOutputXSDFileNameNull() )
					throw CrException("OutputXSDFileName is null for WebService " + toString(iWebServiceId)
					+ ". This is probably a bug.");
				stSourceXml = pWebServicesTab->GetOutputXSDFileName();
			}
		}
		else if ( (iSourceTypeId == XmlTypeId) || (iSourceTypeId == JsonTypeId) ) {
			if ( row.m_vFields.at(iColPosSourceXml) != 0 ) {
				const string& stClientPath = row.m_vFields.at(iColPosSourceXml).const_ref();
				if ( !stClientPath.empty() ) {
					stSourceXml = GetXmlSchemaPath(stClientPath);
				}
			}
		}

		if ( iTargetTypeId == WebServiceInputTypeId ||
			 iTargetTypeId == WebServiceOutputTypeId ) {
			stringstream ssWhere;
			ssWhere << "WHERE transformation_id = " << iTransformationId
				    << " AND operation_id IS NULL";
			SmartDbPtr<TranDb::TransformationChainLinksTab> pTransformationChainLinksTab( TranDb::TransformationChainLinksTab::MakeInstance(&m_dbConnection) );
			pTransformationChainLinksTab->Select(ssWhere.str());
			if ( !pTransformationChainLinksTab->Next() )
				throw CrException("Internal Error: No TransformationChainLinkTab entry with TransformationId = " + toString(iTransformationId)
				+ ". Try to deploy the operation using the \"Individual Items\" deploy option." );

			int iWebServiceTranId = pTransformationChainLinksTab->GetNextTransformation_ID();
			int iWebServiceId = m_functionIdMapper.GetWebServiceIdForTransformation(iWebServiceTranId);

			SmartDbPtr<TranDb::WebServicesTab> pWebServicesTab( TranDb::WebServicesTab::MakeInstance(&m_dbConnection) );
			pWebServicesTab->Select("WHERE WebService_Id = " + toString(iWebServiceId));
			if ( !pWebServicesTab->Next() )
				throw CrException("There is no WebService defined with WebServiceId = " + toString(iWebServiceId)
				+ ". This is probably a bug.");

			if ( iTargetTypeId == WebServiceInputTypeId ) {
				                                                                            
				if ( pWebServicesTab->IsInputXSDFileNameNull() )
					throw CrException("InputXSDFileName is NULL for WebService " + toString(iWebServiceId)
					+ ". This is probably a bug.");
				stTargetXml = pWebServicesTab->GetInputXSDFileName();
			}
			else {
				                                                                                
				if ( pWebServicesTab->IsOutputXSDFileNameNull() )
					throw CrException("OutputXSDFileName is NULL for WebService " + toString(iWebServiceId)
					+ ". This is probably a bug.");
				stTargetXml = toHostedXsd(pWebServicesTab->GetOutputXSDFileName());
			}
		}
		else if ( (iTargetTypeId == XmlTypeId) || (iTargetTypeId == JsonTypeId) ) {
			if ( row.m_vFields.at(iColPosTargetXml) != 0 ) {
				const string& stClientPath = row.m_vFields.at(iColPosTargetXml).const_ref();
				if ( !stClientPath.empty() ) {
					stTargetXml = GetXmlSchemaPath(stClientPath);
				}
			}
		}

		if ( !stSourceXml.empty() || !stTargetXml.empty() ) {

			stringstream ssUpdateSql;
			ssUpdateSql << "UPDATE " << getTableName(nameTransformationsTab_New) << " SET";
			if ( !stSourceXml.empty() )
				ssUpdateSql << " source_xml = '" << stSourceXml << "'";
			if ( !stTargetXml.empty() ) {
				if ( !stSourceXml.empty() )
					ssUpdateSql << ",";
				ssUpdateSql << " target_xml = '" << stTargetXml << "'";
			}
			ssUpdateSql << " WHERE transformation_id = " << iTransformationId;
			UIBACKEND_TRACE("SourceTargetXmlHandler::HandleTransformations " + ssUpdateSql.str());
			m_dbConnection.ExecuteSql(ssUpdateSql.str());
		}

	}
}

