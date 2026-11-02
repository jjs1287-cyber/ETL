/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <memory>		                 

#include <konga/Utils/FileUtils.h>		               
#include <konga/Utils/StringUtils.h>	                     
#include <konga/Utils/MapUtils.h>

#include <konga/Utils/CrException.h>

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/DOMElementListPtr.h>

#include "PipelinePluginManifest.h"
#include "PluginManifestParser.h"

using namespace konga;
using namespace konga::xml;

using std::string;

namespace {
	const string sETLFORGE_PLUGIN("ETLForgePlugin");
	const string sSPEC_VERSION_ATTR("SpecVersion");

	const string sPIPELINE_PLUGIN("PipelinePlugin");
}

boost::shared_ptr<PluginManifest> PluginManifestParser::ParseManifest(const std::string& sManifestFile) {
	if ( !FileExists(sManifestFile) )
		throw CrException("The specified manifest file \"" + sManifestFile + "\" does not exist.");

	                                               
	string sErr;
	const DOMDocumentPtr pManifestDoc( XmlUtils::LoadDoc(sManifestFile, sErr, false) );
	if ( pManifestDoc == 0 ) {
		throw CrException(sErr);
	}

	const DOMElementPtr pETLForgePlugin( pManifestDoc.getDocumentElement() );
	if ( pETLForgePlugin == 0 )
		throw CrException("Plugin Manifest is invalid, there is no document element.");
	
	                                     
	if ( !EqualsIgnoreCase(XmlUtils::GetNodeName(pETLForgePlugin), sETLFORGE_PLUGIN) )
		throw CrException("Plugin Manifest is invalid. The root node \"" + sETLFORGE_PLUGIN
		+ "\" can not be found.");
	const string& sSpecVersion = XmlUtils::GetAttributeValue(pETLForgePlugin, sSPEC_VERSION_ATTR);
	if ( sSpecVersion.empty() )
		throw CrException("Plugin Manifest is invalid. No Specification Version was specified.");

	Version specVersion(0);
	try {
		specVersion = Version(sSpecVersion);
	}
	catch( const CrException& ex ) {
		throw CrException("Plugin manifest is invalid. Invalid Spec Version: " + ex.GetErrorString());
	}

	if ( specVersion != Version(1, 0, 0) )
		throw CrException("Plugin manifest is invalid. Spec version \"" + specVersion.GetShortVersionString()
		+ "\" is not supported.");

	boost::shared_ptr<PluginManifest> pManifest;

	                            
	DOMElementPtr pPluginElement( pETLForgePlugin.selectSingleNode(sPIPELINE_PLUGIN) );
	if ( pPluginElement != 0 ) {
		pManifest = ParsePipelinePlugin(pPluginElement);
	}
	                                             
	else {
		DOMElementListPtr pSpecifiedNodes = pETLForgePlugin.getChildNodes();
		const unsigned long nSize = pSpecifiedNodes.getLength();
		string sErrSpecified;
		for ( unsigned long i = 0; i < nSize; ++i ) {
			const string& sNodeName = XmlUtils::GetNodeName(pSpecifiedNodes.item[i]);
			if ( !sNodeName.empty() ) {
				if ( !sErrSpecified.empty() )
					sErrSpecified += ", ";
				sErrSpecified += sNodeName;
			}
		}
		string sErr("Plugin manifest is invalid. No valid plugin type was found. The following are present in the manifest: " + sErrSpecified);
		throw CrException(sErr);
	}

	pManifest->m_sParsedFrom = sManifestFile;

	return pManifest;
}

boost::shared_ptr<PluginManifest> PluginManifestParser::ParsePipelinePlugin(const DOMElementPtr& pPluginElement) {
	const string& sSpecVersion = XmlUtils::GetAttributeValue(pPluginElement, sSPEC_VERSION_ATTR);
	if ( sSpecVersion.empty() )
		throw CrException("Failed to parse pipeline plugin manifest. No SpecVersion attribute is present.");

	try {
		Version specVersion = Version(sSpecVersion);
		if ( specVersion != Version(1, 0, 0) )
			throw CrException("Failed to parse pipeline plugin manifest. Spec version \""
			+ specVersion.GetShortVersionString() + "\" is not valid.");
	}
	catch( const CrException& ex ) {
		throw CrException("Failed to parse pipeline plugin manifest. Invalid spec version: " + ex.GetErrorString());
	}

	std::auto_ptr<PipelinePluginManifest> pManifest( new PipelinePluginManifest() );

	DOMElementListPtr pElements = pPluginElement.getChildNodes();
	const unsigned long nElements = pElements.getLength();
	for( unsigned long i = 0; i < nElements; ++i ) {
		const DOMElementPtr& pElement = pElements.item[i];
		const string& sElementName = XmlUtils::GetNodeName(pElement);
		const string& sElementValue = XmlUtils::GetNodeValue(pElement);
		if ( EqualsIgnoreCase(sElementName, "Name") ) {
			pManifest->m_sUniqueName = sElementValue;
		}
		else if ( EqualsIgnoreCase(sElementName, "DisplayName") ) {
			pManifest->m_sDisplayName = sElementValue;
		}
		else if ( EqualsIgnoreCase(sElementName, "Version") ) {
			pManifest->m_PipelinePluginVersion = Version(sElementValue);
		}
		else if ( EqualsIgnoreCase(sElementName, "Author") ) {
			pManifest->m_sAuthor = sElementValue;
		}
		else if ( EqualsIgnoreCase(sElementName, "Description") ) {
			pManifest->m_sDescription = sElementValue;
		}
		else if ( EqualsIgnoreCase(sElementName, "Command") ) {
			pManifest->m_sCommand = sElementValue;
		}
		else if ( EqualsIgnoreCase(sElementName, "Arguments") ) {
			pManifest->m_sArguments = sElementValue;
		}
		else if ( EqualsIgnoreCase(sElementName, "DataElements") ) {
			AddDataElements(pElement, *pManifest);
		}
		else if ( EqualsIgnoreCase(sElementName, "PipelinePosition") ) {
			AddPipelinePosition(pElement, *pManifest);
		}
	}

	return boost::shared_ptr<PluginManifest>(pManifest.release());
}

void PluginManifestParser::AddDataElements(const xml::DOMElementPtr& pDataElements,
										   PipelinePluginManifest& manifest)
{
	manifest.m_bRequestAllDataElements = false;
	string sAll( XmlUtils::GetAttributeValue(pDataElements, "All") );
	if ( sAll.empty() )
		sAll = "false";

	if ( konga::EqualsIgnoreCase(sAll, "true") ) {
		manifest.m_bRequestAllDataElements = true;
	}

	DOMElementListPtr deNodes( pDataElements.selectNodes("DataElement") );
	const unsigned long nNodes = deNodes.getLength();
	for( unsigned long i = 0; i < nNodes; ++i ) {
		AddDataElement(deNodes.item[i], manifest);
	}
}

namespace {
	bool IsWildCard(const string& sDataElementName) {
		return sDataElementName.find_first_of("*?") != string::npos;
	}
}

void PluginManifestParser::AddDataElement(const xml::DOMElementPtr& pXmlDataElement,
										  PipelinePluginManifest& manifest)
{
	const string& sDataElementName = XmlUtils::GetAttributeValue(pXmlDataElement, "Name");
	if ( sDataElementName.empty() )
		throw CrException("Failed to parse plugin manifest. A data element without a name was specified.");

	if ( manifest.m_DataElements.find(sDataElementName) != manifest.m_DataElements.end() ) {
		throw CrException("Failed to parse plugin manifest. Two data elements with the name \""
			+ sDataElementName + "\" were specified.");
	}

	const bool isWildCard = IsWildCard(sDataElementName);

	const string& sDEType = XmlUtils::GetAttributeValue(pXmlDataElement, "Type");
	DataElement::Type deType = DataElement::t_unknown;
	if ( !isWildCard ) {
		if ( sDEType.empty() )
			throw CrException("Failed to parse plugin manifest. A data element without type was specified.");

		deType = DataElement::get_type(sDEType.c_str(), 0);
		if ( deType == DataElement::t_unknown ) {
			throw CrException("Failed to parse plugin manifest. A data element with an unknown data element type \""
				+ sDEType + "\" was specified.");
		}
	}

	const string& sRequired = XmlUtils::GetAttributeValue(pXmlDataElement, "Required");
	const bool isRequired = EqualsIgnoreCase(sRequired, "true") || EqualsIgnoreCase(sRequired, "yes") || sRequired == "1";

	if ( isRequired && isWildCard ) {
		throw CrException("Failed to parse plugin manifest. The data element '" + sDataElementName + "' is both required and contains wild cards.");
	}

	string sDefaultValue;
	DOMElementPtr pDefaultValue = pXmlDataElement->selectSingleNode("DefaultValue");
	if ( pDefaultValue != 0 ) {
		sDefaultValue = XmlUtils::GetNodeValue(pDefaultValue);
	}

	boost::shared_ptr<PluginDataElement> de(
		new PluginDataElement(sDataElementName, deType, sDefaultValue, isRequired, isWildCard)
		);
	de->setDefaultString(sDefaultValue);
	manifest.m_DataElements.insert(
		PipelinePluginManifest::DEMapT::value_type(sDataElementName, de)
		);
}

void PluginManifestParser::AddPipelinePosition(const xml::DOMElementPtr& pElement, PipelinePluginManifest& manifest) {
	const string& sOptional = XmlUtils::GetAttributeValue(pElement, "Optional");
	bool bOptional = true;
	if ( !sOptional.empty() ) {
		bOptional = EqualsIgnoreCase(sOptional, "true");
	}

	const string& sRelative = XmlUtils::GetAttributeValue(pElement, "Relative");
	const string& sExecutionOrder = XmlUtils::GetAttributeValue(pElement, "ExecutionOrder");
	long lExecutionOrder = atoi(sExecutionOrder.c_str());

	const string& sEntityName = XmlUtils::GetNodeValue(pElement);

	manifest.m_PipelinePositions.push_back(
		PipelinePosition(sEntityName, sRelative, lExecutionOrder, bOptional)
		);
}
