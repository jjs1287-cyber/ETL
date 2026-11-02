/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/CrException.h>

#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbUtils.h>		            
#include <konga/Db/TranDb/PluginsTab.h>

#include "PluginManager.h"
#include "PluginManifestParser.h"
#include "PluginManifestDbStore.h"

using namespace konga;
using std::string;
using std::map;

PluginManifestDbStore::PluginManifestDbStore(void) {
	this->SetInternalDbName("TranDb");
}

namespace {
	string GetManifestFileImpl(TranDb::PluginsTab& pluginsTab) {
		string sManifestFile( PluginManager::GetPluginsBaseDir() );
		AppendPathSeparator(sManifestFile);
		sManifestFile += pluginsTab.GetPluginPath();
		return sManifestFile;
	}
}

boost::shared_ptr<PluginManifest> PluginManifestDbStore::AddManifestToCache(TranDb::PluginsTab& pluginsTab) {

	                                       
	const string sManifestFile( GetManifestFileImpl(pluginsTab) );
	PluginManifestParser parser;
	boost::shared_ptr<PluginManifest> manifest = parser.ParseManifest(sManifestFile);

	                                 
	const long lPluginId = pluginsTab.GetPluginId();
	m_ManifestCache.insert( ManifestCacheMapT::value_type(lPluginId, manifest) );
	m_GuidMapCache.insert( map<string, long>::value_type(pluginsTab.GetPluginName() , lPluginId) );
	return manifest;
}

boost::shared_ptr<PluginManifest> PluginManifestDbStore::GetManifest(long lPluginId) {
	ManifestCacheMapT::const_iterator cit = m_ManifestCache.find(lPluginId);
	if ( cit != m_ManifestCache.end() )
		return cit->second;

	                                 
	SmartDbPtr<TranDb::PluginsTab> pPluginsTab( TranDb::PluginsTab::MakeInstance(&this->GetDbConnection()) );
	pPluginsTab->Select("WHERE pluginid = " + toString(lPluginId));
	if ( !pPluginsTab->Next() )
		throw CrException("There is no plugin installed on this server with PluginId = " + toString(lPluginId));

	return AddManifestToCache(*pPluginsTab);
}

boost::shared_ptr<PluginManifest> PluginManifestDbStore::GetManifest(const std::string& stUniqueId) {
	map<string, long>::iterator it = m_GuidMapCache.find(stUniqueId);
	if ( it == m_GuidMapCache.end() ) {
		                                    
		SmartDbPtr<TranDb::PluginsTab> pPluginsTab( TranDb::PluginsTab::MakeInstance(&this->GetDbConnection()) );
		pPluginsTab->Select("WHERE pluginname = '" + SqlEscape(stUniqueId) + "'");
		if ( !pPluginsTab->Next() )
			throw CrException("There is no plugin installed on this server with the unique name '" + stUniqueId + "'");

		return AddManifestToCache(*pPluginsTab);
	}
	return GetManifest(it->second);
}

