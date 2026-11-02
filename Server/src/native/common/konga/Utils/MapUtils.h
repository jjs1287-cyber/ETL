/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_UTILS_MAP_UTILS_H
#define KONGA_UTILS_MAP_UTILS_H

#include <map>
#include <string>

#include "PortDefs.h"	          

namespace konga {

	                                                              
	                                                                                       
	template<typename MapType, typename KeyType, typename ValueType>
	typename MapType::iterator	                                        
	AddOrUpdate(MapType& m, const KeyType& k, const ValueType& v) {

		typename MapType::iterator it_lb = m.lower_bound(k);
		if ( it_lb != m.end() && !(m.key_comp()(k,it_lb->first)) ) {
			it_lb->second = v;
			return it_lb;
		}
		else {
			typedef typename MapType::value_type MVT;
			return m.insert(it_lb, MVT(k, v));
		}
	}

	class icasestr_less {
	public:
		inline bool operator()(const std::string& s1, const std::string& s2) const {
			return stricmp(s1.c_str(), s2.c_str()) < 0;
		}
	};

	typedef std::map<std::string, std::string, icasestr_less> IcaseStringMapT;

}                   

#endif                           
