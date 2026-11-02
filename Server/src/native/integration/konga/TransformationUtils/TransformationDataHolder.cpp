/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <cassert>

#include <konga/Utils/CrException.h>

#include <konga/PluginManager/PluginManifestDbStore.h>

#include <konga/OperationContext/DefaultOperationContext.h>
#include <konga/OperationContext/DefaultOperationDataElementHolder.h>

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>

#include "DbLookupCache.h"
#include "CachedDbLookup.h"
#include "FileTypeTargetCache.h"
#include "LdapExecutor.h"
#include "ScriptCache.h"
#include "TransformationDataHolder.h"

using namespace konga;

TransformationDataHolder::TransformationDataHolder(const UserInfo& currentUser,
	OperationContext* pOperationContext, NotCloneableType ocType,
	OperationDataElementHolder* pOperationDataElementHolder, CloneableType odehType,
	CViewLog* pViewLog, CErrorLog* pErrorLog, CloneableType logType,
	DbLookupCache* pDbLookupCache, CloneableType dblcType,
	CachedDbLookup* pCachedDbLookup, NotCloneableType cdlType,
	FileTypeTargetCache* pFileTypeTargetCache, NotCloneableType fttcType,
	ScriptCache* pScriptCache, NotCloneableType scType) :
m_pOperationContext(0), m_OperationContextType(ocType),
m_pOperationDataElementHolder(0), m_pParentOperationDataElementHolder(0), m_OperationDataElementHolderType(odehType),
m_pViewLog(0), m_pErrorLog(0), m_LogType(logType),
m_pDbLookupCache(0), m_DbLookupCacheType(dblcType),
m_pCachedDbLookup(0), m_CachedDbLookupType(cdlType),
m_pFileTypeTargetCache(0), m_FileTypeTargetCacheType(fttcType),
m_pScriptCache(0), m_ScriptCacheType(scType),
m_pPluginManifestStore(0)
{
	switch (ocType) {
		case NCT_CREATE:
			m_pOperationContext = new DefaultOperationContext(currentUser, 0);                                                          
			break;
		case NCT_REFERENCE:
			m_pOperationContext = pOperationContext;
			break;
	};
	assert(m_pOperationContext);

	switch(odehType) {
		case CT_CREATE:
			m_pOperationDataElementHolder = new DefaultOperationDataElementHolder(currentUser);
			break;
		case CT_REFERENCE:
			m_pOperationDataElementHolder = pOperationDataElementHolder;
			break;
		case CT_CLONE:
			if ( pOperationDataElementHolder ) {
				m_pOperationDataElementHolder = pOperationDataElementHolder->clone();
				m_pParentOperationDataElementHolder = pOperationDataElementHolder;
			}
			break;
	};
	assert(m_pOperationDataElementHolder);

	switch(logType) {
		case CT_CREATE:
			m_pViewLog = new CViewLog();
			m_pErrorLog = new CErrorLog();
			break;
		case CT_REFERENCE:
			m_pViewLog = pViewLog;
			m_pErrorLog = pErrorLog;
			break;
		case CT_CLONE:
			throw NotImplementedYet("CViewLog and CErrorLog are not cloneable");
		default:
			throw InternalErrorException("Unknown logType TransformationDataHolder::TransformationDataHolder() ctor");
	};
	assert(m_pViewLog && m_pErrorLog);
	if (m_LogType == CT_CREATE) m_pViewLog->setOperationCleanupContext(m_pOperationContext);

	switch(dblcType) {
		case CT_CREATE:
			m_pDbLookupCache = new DbLookupCache(*m_pOperationDataElementHolder);
			break;
		case CT_REFERENCE:
			m_pDbLookupCache = pDbLookupCache;
			break;
		case CT_CLONE:
			throw NotImplementedYet("DbLookupCache is not cloneable ");
	};
	assert(m_pDbLookupCache);

	switch(cdlType) {
		case NCT_CREATE:
			m_pCachedDbLookup = new CachedDbLookup();
			break;
		case NCT_REFERENCE:
			m_pCachedDbLookup = pCachedDbLookup;
			break;
	};
	assert(m_pCachedDbLookup);

	switch(fttcType) {
		case NCT_CREATE:
			m_pFileTypeTargetCache = new FileTypeTargetCache();
			break;
		case NCT_REFERENCE:
			m_pFileTypeTargetCache = pFileTypeTargetCache;
			break;
	};
	assert(m_pFileTypeTargetCache);

	switch(scType) {
		case NCT_CREATE:
			m_pScriptCache = new ScriptCache();
			break;
		case NCT_REFERENCE:
			m_pScriptCache = pScriptCache;
			break;
	};
	assert(m_pScriptCache);

	m_pLdapExecutor = new LdapExecutor();
	assert(m_pLdapExecutor);
}

TransformationDataHolder::~TransformationDataHolder(void) {
	clean();
}

void TransformationDataHolder::clean() {
	                                           
	  

	delete m_pPluginManifestStore;
	m_pPluginManifestStore = 0;

	delete m_pLdapExecutor;
	m_pLdapExecutor = 0;

	if ( m_ScriptCacheType == NCT_CREATE && m_pScriptCache != 0 ) {
		delete m_pScriptCache;
	}
	m_pScriptCache = 0;

	if ( m_FileTypeTargetCacheType == NCT_CREATE && m_pFileTypeTargetCache != 0 ) {
		delete m_pFileTypeTargetCache;
	}
	m_pFileTypeTargetCache = 0;

	if ( m_CachedDbLookupType == NCT_CREATE ) {
		delete m_pCachedDbLookup;
	}
	m_pCachedDbLookup = 0;

	switch(m_DbLookupCacheType) {
		case CT_CREATE:
			delete m_pDbLookupCache;
			break;
		case CT_CLONE:
			                                   
			assert(false);
			break;
		default:
			break;
	};
	m_pDbLookupCache = 0;

	if ( m_LogType == CT_CREATE ) {
		delete m_pViewLog;
		delete m_pErrorLog;
	}
	m_pViewLog = 0;
	m_pErrorLog = 0;

	switch(m_OperationDataElementHolderType) {
		case CT_CREATE:
			delete m_pOperationDataElementHolder;
			break;
		case CT_CLONE:
			m_pParentOperationDataElementHolder->merge(*m_pOperationDataElementHolder);
			delete m_pOperationDataElementHolder;
		default:
			break;
	};
	m_pOperationDataElementHolder = 0;

	if ( m_OperationContextType == NCT_CREATE ) {
		delete m_pOperationContext;
	}
	m_pOperationContext = 0;
}

OperationContext* TransformationDataHolder::getOperationContext() const {
	return m_pOperationContext;
}

OperationDataElementHolder* TransformationDataHolder::getOperationDataElementHolder() const {
	return m_pOperationDataElementHolder;
}

CViewLog* TransformationDataHolder::getViewLog() const {
	return m_pViewLog;
}

CErrorLog* TransformationDataHolder::getErrorLog() const {
	return m_pErrorLog;
}

DbLookupCache* TransformationDataHolder::getDbLookupCache() const {
	return m_pDbLookupCache;
}

CachedDbLookup* TransformationDataHolder::getCachedDbLookup() const {
	return m_pCachedDbLookup;
}

FileTypeTargetCache* TransformationDataHolder::getFileTypeTargetCache() const {
	return m_pFileTypeTargetCache;
}

LdapExecutor* TransformationDataHolder::getLdapExecutor() const {
	return m_pLdapExecutor;
}

ScriptCache* TransformationDataHolder::getScriptCache() const {
	return m_pScriptCache;
}

PluginManifestStore* TransformationDataHolder::getPluginManifestStore() {
	if ( m_pPluginManifestStore == 0 ) {
		m_pPluginManifestStore = new PluginManifestDbStore();
	}
	return m_pPluginManifestStore;
}
