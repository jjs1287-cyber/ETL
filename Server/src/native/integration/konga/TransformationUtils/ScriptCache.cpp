/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <map>
#include <string>

#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>	             

#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/TranDb/ScriptsTab.h>
#include <konga/DbRelations/EntityIdConstants.h>		                 

#include <konga/UiBackend/GuidMappings/DbGuidToIdMapper.h>	                     

#include "ScriptCache.h"

using namespace std;
using namespace konga;


ScriptCache::ScriptCache() : DbConnectionHolder() {
	this->SetInternalDbName("TranDb");
}

ScriptCache::ScriptCache(DbConnection* pDbConnection) : DbConnectionHolder(pDbConnection) {}

void ScriptCache::getScriptImpl(long& lScriptId, const string& sScriptGuid_in,
							string& sScriptName, string& sScript)
{
	assert(lScriptId > 0 || !sScriptGuid_in.empty());

	                                                      
	string sScriptGuid(sScriptGuid_in);
	if ( !sScriptGuid.empty() ) {
		DbGuidToIdMapper guidMapper(&GetDbConnection());
		lScriptId = guidMapper.GetId(sScriptGuid, ScriptEntityId);
		if ( lScriptId == GuidToIdMapper::invalid_id )
			throw CrException("The requested script (GUID=" + sScriptGuid + ") has not been deployed to this server.");
	}
	else if ( lScriptId < 1 ) {
		throw InternalErrorException("Neither script id nor script guid was passed to getScript. One of the identifiers are needed!");
	}
	else {
		DbGuidToIdMapper guidMapper(&GetDbConnection());
		sScriptGuid = guidMapper.GetGuid(lScriptId, ScriptEntityId);
		if ( sScriptGuid.empty() )
			throw InternalErrorException("There is no script with script id = " + toString(lScriptId)
			+ " defined on this server.");

		ScriptCacheT::const_iterator cit = m_ScriptCache.find(sScriptGuid);
		if( cit != m_ScriptCache.end() ) {
			sScriptName = cit->second.second;
			sScript = cit->second.first;
			return;
		}
	}

	                                    
	konga::SetClientEncoding(GetDbConnection());
	SmartDbPtr<TranDb::ScriptsTab> pScriptsTab( TranDb::ScriptsTab::MakeInstance(&GetDbConnection()) );
	pScriptsTab->Select("WHERE Script_Id = " + toString(lScriptId));
	if ( !pScriptsTab->Next() )
		throw InternalErrorException("Script with GUID = '" + sScriptGuid + "' was not found in the database." \
		" Database id = " + toString(lScriptId));

	sScript = pScriptsTab->IsScript_TextNull() ? "" : pScriptsTab->GetScript_Text();
	sScriptName = pScriptsTab->IsScript_NameNull() ? "" : pScriptsTab->GetScript_Name();

	                               
	m_ScriptCache.insert( ScriptCacheT::value_type(sScriptGuid, pair<string,string>(sScript, sScriptName)) );
}

void ScriptCache::getScript(long scriptId, string& sScriptName, string& sScript) {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	getScriptImpl(scriptId, "", sScriptName, sScript);
}

void ScriptCache::getScript(const string& sScriptGuid, string& sScriptName, string& sScript, long& lScriptId) {
	boost::recursive_mutex::scoped_lock lock(m_ThreadSyncLock);
	ScriptCacheT::const_iterator cit = m_ScriptCache.find(sScriptGuid);
	if( cit != m_ScriptCache.end() ) {
		sScriptName = cit->second.second;
		sScript = cit->second.first;
		return;
	}
	lScriptId = -1;
	getScriptImpl(lScriptId, sScriptGuid, sScriptName, sScript);
}
