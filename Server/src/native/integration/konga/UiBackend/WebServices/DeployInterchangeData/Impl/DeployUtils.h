/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_UTILS_H
#define KONGA_WS_DEPLOY_UTILS_H

#include <string>
#include <vector>
#include <deque>

#include <axis/AxisUserAPI.hpp>
#include <axis/AxisUserAPIArrays.hpp>	                    

AXIS_CPP_NAMESPACE_USE

namespace konga {
	
	class DbConnection;

namespace interchange_deploy {

	class NextIdFinder {
	public:
		NextIdFinder(DbConnection& dbConnection);
		~NextIdFinder(void) {
		}

		                                                       
		                                                          
		                                        
		                               
		long GetNextId(const std::string& stTableName, const std::string& stIdName);

	private:
		DbConnection& m_dbConnection;
	};

	const std::string MODTIME_COLNAME("modtime");

	class TypeInfoLoader {
	public:
		TypeInfoLoader(const std::string& stConnectionString) : m_stConnectionString(stConnectionString) {}
		~TypeInfoLoader(void) {}
		
		                                                               
		void LoadTypeInformation(std::deque<bool>& IsColString,
								 const std::vector<std::string>& vsColNames,
								 const std::string& stTableName,
								 bool& bHasModTimeCol) const;
	private:
		const std::string m_stConnectionString;
	};

	                                                                               
	                                                                                    
	                           
	typedef std::vector<int> vColPosT;
	vColPosT GetFieldIndexes(const xsd__string_Array& columnNames,
							 const std::vector<std::string>& vColumnNamesToGet);

	bool IsJmsSource(const long sourceId, DbConnection& dbConnection);

}                   
}                                

#endif                           
