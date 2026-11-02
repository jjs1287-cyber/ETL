/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef TRANSFORMATION_DATA_STORE_MAIN_H
#define TRANSFORMATION_DATA_STORE_MAIN_H

#include <string>
#include <vector>

#include <konga/Db/DbUtils/DbConnectionHolder.h>

#include "TransformationDataStoreInstance.h"

namespace konga {

	                                                              
	class TransformationDataStore {
	public:
		TransformationDataStore(DbConnection* pDbConn);
		TransformationDataStore(void);

		~TransformationDataStore(void) {}

		                                                                              
		                                                  
		             
		                 
		                                
		                         
		                                                     
		                         
		                                                                                                     
		  
		                                                                                                
		void AddFiles(TransformationDataStoreInstance& instance);

		                                                            
		std::vector<TransformationDataStoreInstance> GetFileList(const TransformationDataStoreInstance& filter) const;

		                                                       
		                                 
                                         

		void DeleteOld(int ageDays);

	private:
		DbConnection& GetDbConnection() const;
		mutable DbConnectionHolder m_dbConnectionHolder;
	};

}

#endif                                    

