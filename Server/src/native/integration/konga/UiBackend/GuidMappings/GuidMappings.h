/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_GUID_MAPPINGS_H
#define KONGA_GUID_MAPPINGS_H

#include <string>

namespace konga {

	class GuidToIdMapper;

	   
                                                                                                       
                                                                                                   
                                                                 
    

	                                        
	                                                   
	std::string GetGuidForId(const int lItemId, const int iEntityType);

	                                                                
	                                                      
	long GetItemIdForGuid(const std::string& stGuid, const int iEntityType, bool bThrowIfNotExisting = true, long lDefaultValue = -1);

	                                      
	                                                 
	                                                                  
	GuidToIdMapper* GetDefaultGuidToIdMapper();
}

#endif                         

