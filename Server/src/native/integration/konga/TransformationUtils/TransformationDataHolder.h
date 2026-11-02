/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef TRANSFORMATION_UTILS_TRANSFORMATION_DATA_HOLDER_H
#define TRANSFORMATION_UTILS_TRANSFORMATION_DATA_HOLDER_H

#include <boost/noncopyable.hpp>

class CViewLog;
class CErrorLog;

namespace konga {

	class UserInfo;
	class OperationDataElementHolder;
	class OperationContext;
	class FileTypeTargetCache;
	class DbLookupCache;
	class CachedDbLookup;
	class LdapExecutor;
	class ScriptCache;
	class PluginManifestStore;

	enum CloneableType {
		CT_CREATE = 0,
		CT_REFERENCE = 1,
		CT_CLONE = 2
	};

	enum NotCloneableType {
		NCT_CREATE = 3,
		NCT_REFERENCE = 4,
	};

	                                         
	                                 

	class TransformationDataHolder : boost::noncopyable {
	public:
		TransformationDataHolder(const UserInfo& currentUser,
			OperationContext* pOperationContext, NotCloneableType ocType,
			OperationDataElementHolder* pOperationDataElementHolder, CloneableType odehType,
			CViewLog* pViewLog, CErrorLog* pErrorLog, CloneableType logType,
			DbLookupCache* pDbLookupCache, CloneableType dblcType,
			CachedDbLookup* pCachedDbLookup, NotCloneableType cdlType,
			FileTypeTargetCache* pFileTypeTargetCache, NotCloneableType fttcType,
			ScriptCache* pScriptCache, NotCloneableType scType);

		~TransformationDataHolder(void);

		OperationContext* getOperationContext() const;

		OperationDataElementHolder* getOperationDataElementHolder() const;

		CViewLog* getViewLog() const;
		CErrorLog* getErrorLog() const;

		DbLookupCache* getDbLookupCache() const;

		CachedDbLookup* getCachedDbLookup() const;

		FileTypeTargetCache* getFileTypeTargetCache() const;

		LdapExecutor* getLdapExecutor() const;

		ScriptCache* getScriptCache() const;

		PluginManifestStore* getPluginManifestStore();

	private:
		void clean();

	private:
		OperationContext* m_pOperationContext;
		const NotCloneableType m_OperationContextType;

		OperationDataElementHolder* m_pOperationDataElementHolder;
		OperationDataElementHolder* m_pParentOperationDataElementHolder;
		const CloneableType m_OperationDataElementHolderType;

		CViewLog* m_pViewLog;
		CErrorLog* m_pErrorLog;
		const CloneableType m_LogType;

		DbLookupCache* m_pDbLookupCache;
		const CloneableType m_DbLookupCacheType;

		CachedDbLookup* m_pCachedDbLookup;
		const NotCloneableType m_CachedDbLookupType;

		FileTypeTargetCache* m_pFileTypeTargetCache;
		const NotCloneableType m_FileTypeTargetCacheType;

		ScriptCache* m_pScriptCache;
		const NotCloneableType m_ScriptCacheType;

		LdapExecutor* m_pLdapExecutor;
		PluginManifestStore* m_pPluginManifestStore;
	};
}

#endif                                                     

