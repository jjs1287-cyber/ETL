/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DBRELATIONS_FUNCTION_ID_MAPPER_H
#define KONGA_DBRELATIONS_FUNCTION_ID_MAPPER_H

#include <map>

namespace konga {

	class DbConnection;

	class FunctionIdMapper {
	public:
		FunctionIdMapper(DbConnection& dbConnection) : m_dbConnection(dbConnection) { }
		~FunctionIdMapper(void) {}

		void AddWebServiceToCache(long webServiceId, long functionId, long transformationId);
		long GetFunctionIdForWebService(long webServiceId) const;
		long GetTransformationIdForWebService(long webServiceId, bool throwOnError = true) const;
		long GetWebServiceIdForTransformation(long transformationId) const;

	private:
		                                                                        
		                                            
		long GetFunctionIdFromDatabase(int functionType, long foreignKey) const;
		long GetTransformationIdFromDatabase(int iWebServiceId) const;
		long GetWebServiceIdFromDatabase(int iTransformationId) const;

	private:
		DbConnection& m_dbConnection;
		typedef std::map<long, long> ididMap;
		mutable ididMap m_webServiceFunctionIdMapCache;
		mutable ididMap m_webServiceTransformationIdMapCache;
		mutable ididMap m_transformationWebServiceIdMapCache;
	};

}                   

#endif                                          

