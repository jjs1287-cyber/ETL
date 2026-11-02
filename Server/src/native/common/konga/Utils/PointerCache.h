/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_UTILS_POINTER_CACHE_H
#define KONGA_UTILS_POINTER_CACHE_H

#include <map>
#include <string>

#include <boost/function.hpp>		              

namespace konga {

                                                                               
                                      
                                                                                    
template<typename KeyType, typename ValueType>
class PointerCache {
public:
	PointerCache() {}
	virtual ~PointerCache() {
		Clear();
	}

	ValueType& Get(const KeyType& key) {
		typename CacheT::iterator it = m_cache.find(key);
		if( it == m_cache.end() ) {
			ValueType* p = new ValueType;
			m_cache.insert(typename PointerCache::CacheT::value_type(key, p));
			return *p;
		}
		return *it->second;
	}

	ValueType& Get(const KeyType& key, boost::function0<ValueType*> createFcn) {
		typename CacheT::iterator it = m_cache.find(key);
		if( it == m_cache.end() ) {
			ValueType* p = createFcn();
			m_cache.insert(typename PointerCache::CacheT::value_type(key, p));
			return *p;
		}
		return *it->second;
	}

	void Remove(const KeyType& key) {
		typename CacheT::iterator it = m_cache.find(key);
		if( it == m_cache.end() )
			return;
		delete it->second;
		m_cache.erase(it);
	}

	void Remove(const ValueType& value) {
		const ValueType* pValue = &value;
		typename CacheT::iterator it = m_cache.begin();
		for( ; it != m_cache.end(); ++it ) {
			if ( pValue == it->second ) {
				this->Remove(it->first);
				break;
			}
		}
	}

	void Clear() {
		typename CacheT::iterator it = m_cache.begin();
		for(; it != m_cache.end(); ++it)
			delete it->second;
		m_cache.clear();
	}

private:
	typedef std::map<KeyType, ValueType*> CacheT;
	CacheT m_cache;
};

}                   

#endif                               
