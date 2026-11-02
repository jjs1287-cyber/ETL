/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <vector>

#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/StringUtils.h>

#include <konga/Crypto/Crypto.h>	         

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/XsdConstants.h>
#include <konga/Xml/XmlConstants.h>

#include <konga/FileTransfer/FileRetriever.h>
#include <konga/Soap/SoapEncoding.h>

#include "WsdlUtils.h"
#include "XsdUtils.h"
#include "RemoteSchemaResolver.h"

using namespace std;
using namespace konga;
using namespace konga::xml;
using konga::CrException;

namespace
{
	bool IsKnownSchemaImportLocation(const string& s)
	{
		return konga::Equals(s, WsdlUtils::WSDL_NAMESPACE) ||
			konga::Equals(s, WsdlUtils::WSDL_SOAP_NAMESPACE_11) ||
			konga::Equals(s, WsdlUtils::WSDL_SOAP_NAMESPACE_12) ||
			konga::Equals(s, XsdConstants::SCHEMA_DEFINITION_NAMESPACE_1999) ||
			konga::Equals(s, XsdConstants::SCHEMA_DEFINITION_NAMESPACE_2001) ||
			konga::Equals(s, XsdConstants::SCHEMA_INSTANCE_NAMESPACE_1999) ||
			konga::Equals(s, XsdConstants::SCHEMA_INSTANCE_NAMESPACE_2001) ||
			konga::Equals(s, SoapEncoding::VERSION_11_NAMESPACE);
	}
}

RemoteSchemaResolver::RemoteSchemaResolver(const std::string& stBaseDir) : m_stBaseDirectory(AppendPathSeparator(stBaseDir))
{
}

string RemoteSchemaResolver::Resolve(const std::string& stNamespace, const std::string& stLocation)
{
	if (IsKnownSchemaImportLocation(stNamespace)) {
		return stLocation;
	}
	if (FileRetriever::IsSupportedUrl(stLocation)) {
		string stLocalFileName(md5(stLocation) + ".xsd");
		string stLocalFilePath(m_stBaseDirectory + stLocalFileName);
		if (!FileExists(stLocalFilePath)) {
			FileRetriever fr;
			if ( fr.GetAsFile(stLocation, stLocalFilePath) != Protocol::SUCCESS ) {
				throw CrException("Failed to download the external xsd: " + stLocation
					+ ".\nReason: " + fr.LastError());
			}
			ResolveRemoteImportsInDownloadedSchema(stLocalFilePath);
		}
		return stLocalFileName;
	}
	else {
		return stLocation;
	}
}

void RemoteSchemaResolver::ResolveRemoteImportsInDownloadedSchema(const std::string& stSchemaFile)
{
	string stError;
	DOMDocument2Ptr schemaDoc = XmlUtils::LoadDoc(stSchemaFile, stError, false);
	if (schemaDoc == 0) {
		throw CrException(stError);
	}
	DOMElementPtr schemaNode = XsdUtils::FindSchemaElement(schemaDoc->getFirstChild());
	if (schemaNode == 0) {
		throw CrException("Failed to locate the schema node in the schema " + stSchemaFile);
	}
	bool save = ProcessImportNodes(schemaNode);
	if (save) {
		SaveSchema(schemaDoc, stSchemaFile);
	}
}

bool RemoteSchemaResolver::ProcessImportNodes(const DOMElementPtr& schemaNode)
{
	bool save = false;
	vector<DOMElementPtr> importNodes;
	XsdUtils::GetImportNodes(schemaNode, importNodes);
	if (!importNodes.empty())
	{
		for (vector<DOMElementPtr>::iterator it = importNodes.begin(); it != importNodes.end(); ++it) {
			bool changed = ProcessImportNode(*it);
			save = save || changed;
		}
	}
	return save;
}

bool RemoteSchemaResolver::ProcessImportNode(DOMElementPtr& importNode)
{
	string stImportedNamespace(XmlUtils::GetAttributeValue(importNode, XmlConstants::NAMESPACE));
	string stSchemaLocation(XmlUtils::GetAttributeValue(importNode, XsdConstants::SCHEMALOCATION_ATTRIBUTE));
	string stLocalFileName(Resolve(stImportedNamespace, stSchemaLocation));
	if (!Equals(stSchemaLocation, stLocalFileName))
	{
		importNode->setAttribute(XsdConstants::SCHEMALOCATION_ATTRIBUTE.c_str(), stLocalFileName);
		return true;
	}
	return false;
}

void RemoteSchemaResolver::SaveSchema(const DOMDocumentPtr& schemaDoc, const string& stSchemaFile)
{
	unsigned long hr = schemaDoc->save(stSchemaFile);
	if( FAILED(hr) )
	{
		string stHeader("Failed to save the downloaded schema file '");
		stHeader += stSchemaFile;
		stHeader += "' to disk. ";
		throw CrException(XmlUtils::GetLastError(schemaDoc, stHeader));
	}
}
