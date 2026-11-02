/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_ITEM_PROPERTIES_H
#define KONGA_ITEM_PROPERTIES_H

#include <string>
#include <map>

#include <konga/Utils/KeyValues.h>

#include <konga/Db/DbUtils/DbConnectionHolder.h>

namespace konga {

	class DbConnection;

	class ItemProperties : public DbConnectionHolder {
	public:
		                                                                          
		static bool hasItemProperties(int iEntityTypeId);

	public:
		                                                                    
		                                                  
		ItemProperties(DbConnection* pTranDbConnection = 0);
		~ItemProperties(void) {}

		                                                          
		void GetItemProperties(int iEntityTypeId, long lEntityId, KeyValues& properties) const;

		void GetItemPropertiesForEntityType(int iEntityTypeId, std::map<long, KeyValues>& allProperties);

		                                           
		                                                            
		bool GetItemProperty(int iEntityTypeId, long lEntityId, const std::string& keyName, std::string& keyValue) const;

		void Delete(int iEntityTypeId, long lEntityId);

		                                                                                            
		                                                                             
		void Add(int iEntityTypeId, long lEntityId, const std::string& keyName, const std::string& keyValue);

		                                                                                     
		                                                                      
		bool Update(int iEntityTypeId, long lEntityId, const std::string& keyName, const std::string& keyValue);
	};
}

#endif                           

