/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <map>
#include <string>

#include <boost/filesystem/path.hpp>		                       

#include <konga/Utils/CrException.h>
#include <konga/Utils/FilesTraverser.h>					                                     
#include <konga/Utils/SimpleFileFilterFileMatcher.h>	                                    
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/Settings.h>			                           

#include <konga/Db/DbUtils/DbUtils.h>		                                  
#include <konga/Db/DbUtils/AutoDbCleanup.h>

#include <konga/Db/TranDb/PluginsTab.h>

#include "PipelinePluginManifest.h"
#include "PluginManifestParser.h"
#include "PluginManager.h"

using namespace konga;
using namespace std;
using namespace TranDb;

                                                               
                                    

namespace {

const string sPLUGIN_RELATIVE_ROOT_PATH("Plugins/");
const string sSYSTEM_PLUGIN_PATH("System");
const string sUSER_PLUGIN_PATH("User");

struct Plugin {
	Plugin(int id=-2, string sUniqueName="", string sPath="") :
		m_id(id), m_sUniqueName(sUniqueName), m_sPath(sPath) {}

	int m_id;
	string m_sUniqueName;
	string m_sPath;
};

typedef map<string, Plugin> Plugins_t;

                                                                                
class PluginFileAction : public FileAction {
public:
	PluginFileAction(DbConnection& tranDb) :
		m_bPluginsChanged(false), m_nBasePluginPathLength(PluginManager::GetPluginsBaseDir().length()), m_tranDb(tranDb)
	{
		try {
			SmartDbPtr<TranDb::PluginsTab> pPluginsTab( TranDb::PluginsTab::MakeInstance(&tranDb) );
			pPluginsTab->Select();
			while( pPluginsTab->Next() ) {
				m_PluginsInDb[ pPluginsTab->GetPluginName() ] = 
					Plugin(pPluginsTab->GetPluginId(), pPluginsTab->GetPluginName(), pPluginsTab->GetPluginPath());
			}
			pPluginsTab->Close();

		} catch(DbException& e) {
			throw CrException("Error while reading plugin information from database: " +
				e.GetPrettyErrorMessage(), e.GetErrorNumber());
		}
	}

	virtual void doAction(const std::string& stFilePath);

	bool HavePluginsChanged() const {
		return m_bPluginsChanged;
	}

	vector<int> GetPluginsToBeDeleted() {
		vector<int> viPluginsToDelete;
		Plugins_t::const_iterator citPluginInDb = m_PluginsInDb.begin();
		for(; citPluginInDb != m_PluginsInDb.end(); ++citPluginInDb) {
			if( m_PluginsOnDisk.find( citPluginInDb->second.m_sUniqueName ) == m_PluginsOnDisk.end() )
				viPluginsToDelete.push_back(citPluginInDb->second.m_id);
		}
		return viPluginsToDelete;
	}

	void DeletePluginsThatWereRemovedFromDisk() {
		const vector<int>& viPluginsToDelete = GetPluginsToBeDeleted();
		if( viPluginsToDelete.size() > 0 ) {
			string sIds;
			vector<int>::const_iterator citPluginToDelete = viPluginsToDelete.begin();
			for(; citPluginToDelete != viPluginsToDelete.end(); ++citPluginToDelete) {
				sIds += toString(*citPluginToDelete);
				if( (citPluginToDelete + 1) != viPluginsToDelete.end() )
					sIds += ", ";
			}
			string sDeleteSql = "DELETE FROM PluginsTab WHERE PluginId in (" + sIds + "); "
								"DELETE FROM PipelinePluginInstanceTab WHERE PluginId in (" + sIds + "); "
								"DELETE FROM PipelinePluginPositionTab WHERE PluginId in (" + sIds + "); ";

			m_tranDb.ExecuteSql(sDeleteSql);
		}
	}

	void AddOrUpdatePluginsThatWereAddedToDiskOrMovedOnDisk() {
		int iNextId = GetNextId("PluginsTab", "PluginId", m_tranDb);
		const string sBasePath(	PluginManager::GetPluginsBaseDir() );

		SmartDbPtr<TranDb::PluginsTab> pPluginsTab( TranDb::PluginsTab::MakeInstance(&m_tranDb) );
		Plugins_t::const_iterator citPluginOnDisk = m_PluginsOnDisk.begin();
		for(; citPluginOnDisk != m_PluginsOnDisk.end(); ++citPluginOnDisk) {
			Plugins_t::const_iterator citPluginInDb = m_PluginsInDb.find( citPluginOnDisk->second.m_sUniqueName );
			if( citPluginInDb == m_PluginsInDb.end() ) {
				                                                      
				pPluginsTab->AddNew();
				pPluginsTab->SetPluginId( iNextId++ );	                                  
				pPluginsTab->SetPluginName( citPluginOnDisk->first );
				pPluginsTab->SetPluginPath( citPluginOnDisk->second.m_sPath );
				pPluginsTab->Update();
				m_bPluginsChanged = true;
			}
			else {
				                                                                      
				const string& sDbPath = citPluginInDb->second.m_sPath;
				const string& sDiskPath = citPluginOnDisk->second.m_sPath;
				if ( sDbPath != sDiskPath ) {
					                                              
					pPluginsTab->Select("WHERE PluginId = " + toString(citPluginInDb->second.m_id));
					if ( pPluginsTab->Next() ) {
						pPluginsTab->Edit();
						pPluginsTab->SetPluginPath(citPluginOnDisk->second.m_sPath);
						pPluginsTab->Update();
						m_bPluginsChanged = true;
					}
				}
			}
		}
	}

	void DoUpdateToDb() {
		try {
			DeletePluginsThatWereRemovedFromDisk();
			AddOrUpdatePluginsThatWereAddedToDiskOrMovedOnDisk();
		} catch(DbException& e) {
			throw CrException("Error while writing pipeline plugin information to database: " +
				e.GetPrettyErrorMessage(), e.GetErrorNumber());
		}
	}

	string GetWarnings() const {
		return m_sWarnings;
	}

private:
	bool m_bPluginsChanged;
	unsigned int m_nBasePluginPathLength;
	DbConnection& m_tranDb;
	Plugins_t m_PluginsInDb;
	Plugins_t m_PluginsOnDisk;
	string m_sWarnings;
};

Plugin GetPluginInfoFromPluginFile(const string& sPathToPluginXml) {
	PluginManifestParser parser;
	boost::shared_ptr<PluginManifest> pManifest = parser.ParseManifest(sPathToPluginXml);
	PipelinePluginManifest* pPipelineManifest = dynamic_cast<PipelinePluginManifest*>(pManifest.get());
	if( !pPipelineManifest ) {
		throw CrException("A plugin that is not a pipeline plugin was found where only "
			"pipeline plugins are allowed. The plugin is '" + pManifest->GetDisplayName() +
			"' and its manifest is '" + sPathToPluginXml + "'.");
	}
	return Plugin(-1, pManifest->GetUniqueName(), "");
}

void PluginFileAction::doAction(const std::string& sFilePath) {
	try {
		Plugin plugin( GetPluginInfoFromPluginFile(sFilePath) );
		if( m_PluginsInDb.find( plugin.m_sUniqueName ) == m_PluginsInDb.end() )
			m_bPluginsChanged = true;
		plugin.m_sPath = sFilePath.substr(m_nBasePluginPathLength);
		m_PluginsOnDisk[ plugin.m_sUniqueName ] = plugin;
	}
	catch( const CrException& ex ) {
		m_sWarnings += ex.GetErrorString();
	}
}

}                       

std::string konga::PluginManager::GetPluginsBaseDir() {
	return std::string( GetServerHomeWithSlash() + sPLUGIN_RELATIVE_ROOT_PATH );
}

bool konga::PluginManager::UpdatePluginsInDatabase(string& sWarnings) {

	konga::SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );
	AutoRollbackTransaction transaction_pTranDb(pTranDb);

	                        
	PluginFileAction pluginAction(*pTranDb);
	SimpleFileFilterFileMatcher matcher("*/plugin.xml", false, false);
	FilesTraverser traverser(pluginAction, matcher);
	traverser.traverseDirectory(GetPluginsBaseDir(), true);

	pluginAction.DoUpdateToDb();

	transaction_pTranDb.Commit();

	sWarnings = pluginAction.GetWarnings();
	return pluginAction.HavePluginsChanged();
}

void konga::PluginManager::GetPipelinePlugins(vector<string>& pipelinePlugins) {

	const string& sPluginRootPath( GetPluginsBaseDir() );

	konga::SmartDbPtr<DbConnection> pTranDb( konga::GetBackEndDbConnection("TranDb") );

	try {
		SmartDbPtr<TranDb::PluginsTab> pPluginsTab( TranDb::PluginsTab::MakeInstance(pTranDb) );
		pPluginsTab->Select();
		while( pPluginsTab->Next() ) {
			pipelinePlugins.push_back( sPluginRootPath + pPluginsTab->GetPluginPath() );
		}
		pPluginsTab->Close();
	} catch(DbException& e) {
		throw CrException("Error while reading pipeline plugin information from database: " +
			e.GetPrettyErrorMessage(), e.GetErrorNumber());
	}
}

