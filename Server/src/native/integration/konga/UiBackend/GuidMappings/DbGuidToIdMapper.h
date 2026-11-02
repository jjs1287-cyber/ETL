/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_UI_DB_GUIDTOIDMAPPER_H
#define KONGA_UI_DB_GUIDTOIDMAPPER_H

#include <string>

#include <konga/Db/DbUtils/DbConnectionHolder.h>

#include "GuidToIdMapper.h"

namespace konga {

	   
                                                                                        
                                                                                
    
	class DbGuidToIdMapper : public GuidToIdMapper, private DbConnectionHolder {

	public:
		                                                                                
		                                                                                
		                                                                        
		DbGuidToIdMapper(void);

		                                              
		                                                                                 
		DbGuidToIdMapper(DbConnection* pDbConnection);

		virtual ~DbGuidToIdMapper(void) {}

		virtual long GetId(const std::string& stGuid, const int iEntityType, const long lDefaultId = invalid_id) const;

		virtual std::string GetGuid(const long lItemId, const int iEntityType, const std::string& stDefaultGuid = "") const;

		virtual void SetGuidForItem(const long lItemId, const int iEntityType, const std::string& stGuid);

		virtual void DeleteItem(const long lItemId, const int iEntityType);

		virtual void DeleteItem(const std::string& stGuid, const int iEntityType);

	};
}                   

#endif                                

