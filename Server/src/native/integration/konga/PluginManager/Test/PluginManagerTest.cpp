/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#include <iostream>

#include <konga/Utils/CrException.h>
#include <konga/Utils/DBConnectionInfo.h>

#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbConnection.h>

#include <konga/PluginManager/PluginManager.h>
#include <konga/PluginManager/PluginManifestParser.h>
#include <konga/PluginManager/PluginSerializer.h>
#include <konga/PluginManager/PipelinePluginSelector.h>
#include <konga/PluginManager/PipelinePluginManifest.h>
#include <konga/PluginManager/PipelinePluginParser.h>

#include <konga/PipelinePluginEntityManager/PipelinePluginEntityManager.h>


using namespace konga;
using namespace std;

int testUpdatePluginsInDatabase();

int testManifestParser(int argc, char* argv[]);

int testInputSerializer();

int testPipelinePluginSelector();

int testPipelinePluginOutputParser(int argc, char* argv[]);

int testPipelinePluginEntityManager();

int main(int argc, char* argv[])
{

                                        
	return testManifestParser(argc, argv);
                                
                                       
                                                     
                                            
}

int testPipelinePluginEntityManager() {

	try {
		DBConnectionInfo dbInfo("TranDb");
		SmartDbPtr<DbConnection> pConn( DbConnection::MakeInstance(dbInfo.connectionString()) );
		pConn->SetAutoCommit(false);

		PipelinePluginEntityManager mgr(*pConn);

		long pluginId = mgr.GetPluginId("http://www.etlforge.invalid/plugins/filters/system/compression");
		pluginId = mgr.GetPluginId("http://www.etlforge.invalid/plugins/pipeline/user/dummy");
		pluginId = mgr.GetPluginId("http://www.etlforge.invalid/plugins/pipeline/user/dummy");
		pluginId = mgr.GetPluginId("http://www.etlforge.invalid/plugins/pipeline/user/dummy2");
		pluginId = mgr.GetPluginId("http://www.etlforge.invalid/plugins/pipeline/user/dummy");

		mgr.RemovePlugins(6, 4);
		mgr.AddPluginInstance(6, 4, 2, 0, 2);
		mgr.AddPluginInstance(6, 4, 2, 1, 2);
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}

	return 0;
}

int testPipelinePluginOutputParser(int argc, char* argv[]) {
	if ( argc < 2 ) {
		cout << "specify output.xml as the first argument." << endl;
		return 1;
	}

	const string sOutputXml(argv[1]);

	try {
		PipelinePluginParser parser;
		boost::shared_ptr<PipelinePluginOutput> output = parser.ParsePipelinePluginOutput(sOutputXml);

		cout << output->GetPluginSpecVersion().GetShortVersionString() << endl;
		cout << output->GetPipelinePluginOutputVersion().GetShortVersionString() << endl;
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 2;
	}

	return 0;
}

int testPipelinePluginSelector() {

	PipelinePluginSelector selector;

	std::vector< boost::shared_ptr<PluginManifest> > instances = selector.GetPipelinePluginInstances(1, 0, 2, 1, 0);
	instances = selector.GetPipelinePluginInstances(1, 0, 2, 1, 0);
	return 0;

}

int testInputSerializer() {

	PipelinePluginInput input(Version(1,2,0,9), "Windows", "XP SP3", "C:\\pf\\jis\\",
		"Some operation", "asdfasdfasdfas", 234, "C:\\pf\\jis\\plugins\\somepath");

	input.SetPipelinePosition( PipelinePosition("Source", "Post", 50, true) );

	boost::shared_ptr<PluginDataElement> de(new PluginDataElement("de1", DataElement::t_int, "9", false));
	input.AddDataElement(de->getName(), de);

	de.reset( new PluginDataElement("Tom & Jerry", DataElement::t_string, "this > that", true) );
	input.AddDataElement(de->getName(), de);

	input.AddInputFile("file1");
	input.AddInputFile("file2");
	input.AddInputFile("file1");
	
	PluginSerializer serializer;
	cout << serializer.SerializePipelinePluginInput(input) << endl;
	return 0;
}

int testUpdatePluginsInDatabase() {
	try {
		string sWarnings;
		PluginManager::UpdatePluginsInDatabase(sWarnings);
		cout << sWarnings << endl;
		return sWarnings.size();
	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 1;
	}
	return 0;
}

int testManifestParser(int argc, char* argv[]) {
	if ( argc < 2 ) {
		cout << "Specify manifest file as the first argument." << endl;
		return 1;
	}

	const string sManifestFile(argv[1]);

	try {
		PluginManifestParser parser;
		boost::shared_ptr<PluginManifest> manifest = parser.ParseManifest(sManifestFile);
		cout << manifest->GetUniqueName() << endl;
		cout << "Plugin Spec Version = " << manifest->GetPluginSpecVersion().GetShortVersionString() << endl;

		PipelinePluginManifest* pluginManifest = dynamic_cast<PipelinePluginManifest*>(manifest.get());
		if ( pluginManifest != 0 ) {
			cout << "Pipeline Plugin Spec Version = " << pluginManifest->GetPipelinePluginSpecVersion().GetShortVersionString() << endl;
			cout << "Plugin Version = " << pluginManifest->GetPipelinePluginVersion().GetLongVersionString() << endl;
			cout << "Command = " << pluginManifest->GetCommand() << endl;
			cout << "Request all DE? " << (pluginManifest->GetRequestsAllDataElements()? "Yes" : "No") << endl;
		}

	}
	catch( const CrException& ex ) {
		cout << ex.GetErrorString() << endl;
		return 2;
	}
	return 0;
}

