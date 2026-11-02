/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CrException.h>

#include <konga/Xml/XmlUtils.h>
#include <konga/Xml/DOMElementListPtr.h>

#include "PipelinePluginParser.h"

using namespace konga;
using namespace konga::xml;
using std::string;

  
                                                                                                                          
                                                                                               
                                                             

                             
 
  
namespace {

	void GetLogForNode(const DOMElementPtr& node, string& sWarning, string& sSeverity) {
		DOMElementPtr logNode = node.selectSingleNode("Log");
		if ( logNode == 0 )
			return;

		sWarning = XmlUtils::GetNodeValue(logNode);
		if ( sWarning.empty() )
			return;

		sSeverity = XmlUtils::GetAttributeValue(logNode, "Severity");
	}

	void ParseFile(PipelinePluginOutput& output, const DOMElementPtr& fileNode) {
		const string& sName = XmlUtils::GetAttributeValue(fileNode, "Name");

		PipelinePluginOutput::OutputFile outputFile;
		outputFile.Name = sName;
		
		string sWarning, sSeverity;
		GetLogForNode(fileNode, sWarning, sSeverity);
		outputFile.Warning.Warning = sWarning;
		outputFile.Warning.Severity = sSeverity;

		DOMElementListPtr originNodes = fileNode.selectNodes("Origin");
		const unsigned long nOrigin = originNodes.getLength();
		for( unsigned long o = 0; o < nOrigin; ++o ) {
			DOMElementPtr origin( originNodes.item[o] );
			DOMElementListPtr fileNodes = origin.selectNodes("File");
			const unsigned long nFiles = fileNodes.getLength();
			for( unsigned long f = 0; f < nFiles; ++f ) {
				DOMElementPtr file( fileNodes.item[f] );
				const string& sOriginName = XmlUtils::GetAttributeValue(file, "Name");
				outputFile.OriginFiles.push_back(sOriginName);
			}
		}
		output.AddOutputFile(outputFile);
	}

	void ParseFiles(PipelinePluginOutput& output, const DOMElementPtr& filesNode) {
		output.SetHasFilesSection();
		DOMElementListPtr files = filesNode.selectNodes("File");
		const unsigned long nFiles = files.getLength();
		for( unsigned long i = 0; i < nFiles; ++i ) {
			ParseFile(output, files.item[i]);
		}
	}

	void AddDataElement(PipelinePluginOutput& output, const DOMElementPtr& deNode) {
		const string& deName = XmlUtils::GetAttributeValue(deNode, "Name");
		if ( deName.empty() )
			return;

		const string& deType = XmlUtils::GetAttributeValue(deNode, "Type");
		DataElement::Type type = DataElement::get_type(deType.c_str(), 0);
		if ( type == DataElement::t_unknown )
			return;	                                                   

		const string& deValue = XmlUtils::GetNodeValue(deNode);

		boost::shared_ptr<PluginDataElement> de( new PluginDataElement(deName, type, deValue, false, false) );
		output.AddDataElement(deName, de);
	}

	void ParseDataElements(PipelinePluginOutput& output, const DOMElementPtr& dataElementsNode) {
		DOMElementListPtr dataElements( dataElementsNode.selectNodes("DataElement") );
		const unsigned long nDataElements = dataElements.getLength();
		for( unsigned long i = 0; i < nDataElements; ++i ) {
			AddDataElement(output, dataElements.item[i]);
		}
	}

	                                                  
	boost::shared_ptr<PipelinePluginOutput> ParsePipelinePluginOutputSpecVersionOneDotZero(const DOMElementPtr& pPipelinePluginOutput) {
		DOMElementListPtr nodes = pPipelinePluginOutput->getChildNodes();

		boost::shared_ptr<PipelinePluginOutput> output( new PipelinePluginOutput() );
		const unsigned long n = nodes.getLength();
		for( unsigned long i = 0; i < n; ++i ) {
			DOMElementPtr element = nodes.item[i];
			const string& sNodeName = XmlUtils::GetNodeName(element);
			if ( EqualsIgnoreCase(sNodeName, "DataElements") )
				ParseDataElements(*output, element);
			else if ( EqualsIgnoreCase(sNodeName, "Files") ) {
				ParseFiles(*output, element);
			}
		}

		string sWarning, sSeverity;
		GetLogForNode(pPipelinePluginOutput, sWarning, sSeverity);
		output->SetWarning(sWarning, sSeverity);

		return output;
	}

	                                            
	boost::shared_ptr<PipelinePluginOutput> ParseETLForgePluginSpecVersionOneDotZero(const DOMElementPtr& pETLForgePlugin) {
		DOMElementPtr pPipelinePluginOutput( pETLForgePlugin.selectSingleNode("PipelinePluginOutput") );
		if ( pPipelinePluginOutput == 0 )
			throw CrException("Invalid output XML for spec version 1.2. No \"PipelinePluginOutput\" node was found.");

		const Version specVersion( XmlUtils::GetAttributeValue(pPipelinePluginOutput, "SpecVersion") );
		if ( specVersion == Version(1,0,0) ) {
			return ParsePipelinePluginOutputSpecVersionOneDotZero(pPipelinePluginOutput);
		}
		                             
		else {
			throw CrException("Invalid output. Pipeline plugin output spec version \"" + specVersion.GetShortVersionString()
				+ "\" is not supported.");
		}
	}
}

boost::shared_ptr<PipelinePluginOutput> PipelinePluginParser::ParsePipelinePluginOutput(const std::string& sOutputFileName) {

	                                                
	string sErr;
	const DOMDocumentPtr pInputDoc( XmlUtils::LoadDoc(sOutputFileName, sErr, false) );
	if ( pInputDoc == 0 ) {
		throw CrException(sErr);
	}

	DOMElementPtr pETLForgePlugin( pInputDoc.selectSingleNode("ETLForgePlugin") );
	if ( pETLForgePlugin == 0 )
		throw CrException("Invalid output. Failed to find the node \"ETLForgePlugin\"");

	const Version specVersion(XmlUtils::GetAttributeValue(pETLForgePlugin, "SpecVersion"));
	if ( specVersion == Version(1,0,0) ) {
		return ParseETLForgePluginSpecVersionOneDotZero(pETLForgePlugin);
	}
	                                
	else {
		throw CrException("The etlforge plugin spec version \"" + specVersion.GetShortVersionString()
			+ "\" is not supported.");
	}
}

