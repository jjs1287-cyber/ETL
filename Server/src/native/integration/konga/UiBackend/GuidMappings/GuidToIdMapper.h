/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_UI_GUID_MAPPINGS_H
#define KONGA_UI_GUID_MAPPINGS_H

#include <string>

namespace konga {

	   
                                               
                                                                        
                                
    
	class GuidToIdMapper {
	public:
		static long invalid_id;
	public:
		GuidToIdMapper(void) {}
		virtual ~GuidToIdMapper(void) {}

		virtual long GetId(const std::string& stGuid, const int iEntityType, const long lDefaultId = invalid_id) const = 0;

		virtual std::string GetGuid(const long lItemId, const int iEntityType, const std::string& stDefaultGuid = "") const = 0;

		virtual void SetGuidForItem(const long lItemId, const int iEntityType, const std::string& stGuid) = 0;

		virtual void DeleteItem(const long lItemId, const int iEntityType) = 0;

		virtual void DeleteItem(const std::string& stGuid, const int iEntityType) = 0;
	};
}                   

#endif                            

