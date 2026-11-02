/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef KONGA_OPERATION_DATA_ELEMENTS_MANAGER_H
#define KONGA_OPERATION_DATA_ELEMENTS_MANAGER_H

#include <string>
#include <map>

#include <konga/Db/DbUtils/DbConnectionHolder.h>

namespace konga {

	class OperationDataElementHolder;
	class DbConnection;
	class DataElement;

	class OperationDataElementsManager : public DbConnectionHolder {
	public:

		                                                                  
		                               
		OperationDataElementsManager(DbConnection* pIdbConn = 0);
		~OperationDataElementsManager(void) { }

		                                                                           
		                                                                              
		                                       
		long AddDataElements(const OperationDataElementHolder& deHolder);

		                                                                        
		                                        
		void GetDataElements(OperationDataElementHolder& deHolder, long lSessionId);

		                                                   
		void DeleteSession(long lSessionId);

	private:
		                                              
		                                                                            
		                                                                          
		long AddDataElement(const std::string& stName, const std::string& stValue, const std::string& stArrayIndex, long lSessionId);

		long AddDataElement(const std::string& stName, const DataElement& de, const std::string& stArrayIndex, long lSessionId);

		void AddDataElementToDbImpl(const std::string& stName, const std::string& stValue, const std::string& stArrayIndex, long lSessionId);
	};
}

#endif                                           

