/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <sstream>
#include <map>
#include <memory>	           

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/Settings.h>

#include <konga/Xml/XMLOstreamWriter.h>

#include "PluginSerializer.h"

using namespace konga;
using namespace std;


std::string PluginSerializer::SerializePipelinePluginInput(const PipelinePluginInput& input) const {

	using namespace konga::xml;

	stringstream ss;
	WriteXmlPI(ss, konga::defaultEncodingXml());

	{           
	int level = 0;
	AttrT attr;
	string nodeName("ETLForgePlugin");
	attr["SpecVersion"] = input.GetPluginSpecVersion().GetShortVersionString();
	WriteStartElement(ss, level, nodeName, attr);
	AutoWriteEndElement e1(ss, level, nodeName);

	{           
	level = 1;
	nodeName = "PipelinePluginInput";
	attr.clear();
	attr["SpecVersion"] = input.GetPluginSpecVersion().GetShortVersionString();
	WriteStartElement(ss, level, nodeName, attr);
	AutoWriteEndElement e2(ss, level, nodeName);

	{           
	level = 2;
	attr.clear();
	attr["Name"] = input.GetOperatingSystemName();
	attr["Version"] = input.GetOperatingSystemVersion();
	WriteEmptyElement(ss, level, "OperatingSystem", attr);

	attr.clear();
	attr["Version"] = input.GetETLForgeServerVersion().GetShortVersionString();
	attr["Home"] = input.GetETLForgeServerHome();
	WriteEmptyElement(ss, level, "ETLForgeServer", attr);

	attr.clear();
	attr["Name"] = input.GetOperationName();
	attr["Guid"] = input.GetOperationGuid();
	attr["Id"] = toString(input.GetOperationId());
	WriteEmptyElement(ss, level, "Operation", attr);

	attr.clear();
	WriteValueElement(ss, level, "PluginHome", input.GetPluginHome(), attr);

	attr.clear();
	const PipelinePosition& pipelinePosition = input.GetPipelinePosition();
	attr["Relative"] = pipelinePosition.GetRelativePositionName();
	WriteValueElement(ss, level, "PipelinePosition", pipelinePosition.GetEntityTypeName(), attr);

	const PipelinePluginInput::DEMapT& dataElements = input.GetDataElements();
	{
	std::auto_ptr<AutoWriteEndElement> addDataElementsEndNode;
	if ( !dataElements.empty() ) {
		nodeName = "DataElements";
		WriteStartElement(ss, level, nodeName);
		addDataElementsEndNode.reset( new AutoWriteEndElement(ss, level, nodeName) );
	}

	{           
	level = 3;
	nodeName = "DataElement";
	for( PipelinePluginInput::DEMapT::const_iterator citDE = dataElements.begin();
		citDE != dataElements.end(); ++citDE )
	{
		const PluginDataElement& de = *citDE->second.get();
		attr.clear();
		attr["Name"] = citDE->first;
		attr["Type"] = de.getTypeStr();
		int err = 0;
		const string sValue( (const char*)de.GetString(err) );
		WriteValueElement(ss, level, nodeName, sValue, attr);
	}
	}           
	}
	level = 2;

	vector<string> inputFiles = input.GetInputFiles();
	std::auto_ptr<AutoWriteEndElement> addFilesEndElement;
	if ( !inputFiles.empty() ) {
		nodeName = "Files";
		WriteStartElement(ss, level, nodeName);
		addFilesEndElement.reset( new AutoWriteEndElement(ss, level, nodeName) );
	}

	level = 3;
	for( vector<string>::const_iterator citFiles = inputFiles.begin();
		citFiles != inputFiles.end(); ++ citFiles )
	{
		attr.clear();
		attr["Name"] = *citFiles;
		WriteEmptyElement(ss, level, "File", attr);
	}

	level = 2;

	}           

	}           

	}           

	return ss.str();
}
