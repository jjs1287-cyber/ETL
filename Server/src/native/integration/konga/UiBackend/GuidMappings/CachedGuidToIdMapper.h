/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_UI_CACHED_GUID_TO_ID_MAPPER_H
#define KONGA_UI_CACHED_GUID_TO_ID_MAPPER_H

#include <map>
#include <string>

#include <boost/noncopyable.hpp>

#include "GuidToIdMapper.h"

namespace konga {

	class CachedGuidToIdMapper : public GuidToIdMapper, boost::noncopyable {
	public:
		                                                                          
		CachedGuidToIdMapper(GuidToIdMapper& nonCachedGuidToIdMapper);
		virtual ~CachedGuidToIdMapper(void);

		virtual long GetId(const std::string& stGuid, const int iEntityType, const long lDefaultId) const;

		virtual std::string GetGuid(const long lItemId, const int iEntityType, const std::string& stDefaultGuid = "") const;

		virtual void SetGuidForItem(const long lItemId, const int iEntityType, const std::string& stGuid);

		virtual void DeleteItem(const long lItemId, const int iEntityType);

		virtual void DeleteItem(const std::string& stGuid, const int iEntityType);

	private:
		GuidToIdMapper& m_nonCachedGuidToIdMapper;

		typedef std::map<long, std::string> IdGuidMapT;
		typedef std::map<std::string, long> GuidIdMapT;

		class CacheMaps {
		public:
			IdGuidMapT IdGuidMap;
			GuidIdMapT GuidIdMap;
		};

		CacheMaps& GetCacheMaps(int iEntityType) const;

		void AddToCache(CacheMaps& cacheMaps, long lItemId, const std::string& stGuid) const;

		void RemoveFromCache(CacheMaps& cacheMaps, long lItemId) const;
		void RemoveFromCache(CacheMaps& cacheMaps, const std::string& stGuid) const;

		typedef std::map<int, CacheMaps*> EntityTypeMapT;

		mutable EntityTypeMapT m_entityTypeIdMap;
	};
}


#endif                                       
