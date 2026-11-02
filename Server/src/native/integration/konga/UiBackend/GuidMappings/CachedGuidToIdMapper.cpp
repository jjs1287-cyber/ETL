
#include <konga/Utils/MapUtils.h>

#include "CachedGuidToIdMapper.h"

using namespace konga;
using std::map;
using std::string;

namespace {
	inline void AddOrUpdate(std::map<long, std::string>& m, long l, const string& st) {
		konga::AddOrUpdate<std::map<long, std::string>, long, std::string>(m, l, st);
	}

}

CachedGuidToIdMapper::CachedGuidToIdMapper(GuidToIdMapper& nonCachedGuidToIdMapper) :
m_nonCachedGuidToIdMapper(nonCachedGuidToIdMapper) { }

CachedGuidToIdMapper::~CachedGuidToIdMapper(void) {
	EntityTypeMapT::iterator it = m_entityTypeIdMap.begin();
	for( ; it != m_entityTypeIdMap.end(); ++ it ) {
		delete it->second;
	}
	m_entityTypeIdMap.clear();
}


long CachedGuidToIdMapper::GetId(const std::string& stGuid, const int iEntityType, const long lDefaultId ) const {
	CacheMaps& cacheMaps = GetCacheMaps(iEntityType);
	GuidIdMapT& m = cacheMaps.GuidIdMap;
	GuidIdMapT::iterator guidIt = m.find(stGuid);
	if ( guidIt != m.end() ) {
		return guidIt->second;
	}
	else {
		const long lItemId = m_nonCachedGuidToIdMapper.GetId(stGuid, iEntityType, lDefaultId);
		if ( lItemId != lDefaultId ) {
			AddToCache(cacheMaps, lItemId, stGuid);
		}
		return lItemId;
	}
}

std::string CachedGuidToIdMapper::GetGuid(const long lItemId, const int iEntityType, const std::string& stDefaultGuid) const {
	CacheMaps& cacheMaps = GetCacheMaps(iEntityType);
	IdGuidMapT& m = cacheMaps.IdGuidMap;
	IdGuidMapT::iterator idIt = m.find(lItemId);
	if ( idIt != m.end() ) {
		return idIt->second;
	}
	else {
		const string& stGuid = m_nonCachedGuidToIdMapper.GetGuid(lItemId, iEntityType, stDefaultGuid);
		if ( stGuid !=  stDefaultGuid ) {
			AddToCache(cacheMaps, lItemId, stGuid);
		}
		return stGuid;
	}
}

void CachedGuidToIdMapper::SetGuidForItem(const long lItemId, const int iEntityType, const std::string& stGuid) {
	m_nonCachedGuidToIdMapper.SetGuidForItem(lItemId, iEntityType, stGuid);
	CacheMaps& cacheMaps = GetCacheMaps(iEntityType);
	AddToCache(cacheMaps, lItemId, stGuid);
}

void CachedGuidToIdMapper::DeleteItem(const long lItemId, const int iEntityType) {
	m_nonCachedGuidToIdMapper.DeleteItem(lItemId, iEntityType);
	CacheMaps& cacheMaps = GetCacheMaps(iEntityType);
	RemoveFromCache(cacheMaps, lItemId);
}

void CachedGuidToIdMapper::DeleteItem(const std::string& stGuid, const int iEntityType) {
	m_nonCachedGuidToIdMapper.DeleteItem(stGuid, iEntityType);
	CacheMaps& cacheMaps = GetCacheMaps(iEntityType);
	RemoveFromCache(cacheMaps, stGuid);
}

CachedGuidToIdMapper::CacheMaps& CachedGuidToIdMapper::GetCacheMaps(int iEntityType) const {
	EntityTypeMapT::iterator it = m_entityTypeIdMap.find(iEntityType);
	if ( it != m_entityTypeIdMap.end() ) {
		return *(it->second);
	}
	else {
		CacheMaps* pCacheMaps = new CacheMaps();
		m_entityTypeIdMap.insert( EntityTypeMapT::value_type(iEntityType, pCacheMaps) );
		return *pCacheMaps;
	}
}

void CachedGuidToIdMapper::AddToCache(CacheMaps& cacheMaps, long lItemId, const std::string& stGuid) const {
	konga::AddOrUpdate<IdGuidMapT, long, string>(cacheMaps.IdGuidMap, lItemId, stGuid);
	konga::AddOrUpdate<GuidIdMapT, string, long>(cacheMaps.GuidIdMap, stGuid, lItemId);
}

void CachedGuidToIdMapper::RemoveFromCache(CacheMaps& cacheMaps, long lItemId) const {
	IdGuidMapT::iterator idIt = cacheMaps.IdGuidMap.find(lItemId);
	if ( idIt != cacheMaps.IdGuidMap.end() ) {
		GuidIdMapT::iterator guidIt = cacheMaps.GuidIdMap.find(idIt->second);
		if ( guidIt != cacheMaps.GuidIdMap.end() ) {
			cacheMaps.GuidIdMap.erase(guidIt);
		}
		cacheMaps.IdGuidMap.erase(idIt);
	}
}

void CachedGuidToIdMapper::RemoveFromCache(CacheMaps& cacheMaps, const std::string& stGuid) const {
	GuidIdMapT::iterator guidIt = cacheMaps.GuidIdMap.find(stGuid);
	if ( guidIt != cacheMaps.GuidIdMap.end() ) {
		IdGuidMapT::iterator idIt = cacheMaps.IdGuidMap.find(guidIt->second);
		if ( idIt != cacheMaps.IdGuidMap.end() ) {
			cacheMaps.IdGuidMap.erase(idIt);
		}
		cacheMaps.GuidIdMap.erase(guidIt);
	}
}

