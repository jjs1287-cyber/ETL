/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DBRELATIONS_DbRelations_H
#define KONGA_DBRELATIONS_DbRelations_H

#include <string>
#include <vector>

#include <boost/noncopyable.hpp>

namespace konga {

	namespace dbrelations_impl {
		class Impl;
	}

	class TableKeyInfo;
	class DependentTableInfo;

	class DbRelations : private boost::noncopyable {
	private:
		DbRelations(dbrelations_impl::Impl* impl);

	public:
		~DbRelations(void);

		const TableKeyInfo& GetTableKeyInfo(const std::string& stTableName) const;

		                                                                  
		                                                                        
		                                                                                        
		                              
		const std::vector<DependentTableInfo>& GetDependentTables(const std::string& stTableName) const;

		                                                                                          
		                                                                                             
		std::string GetNameColumnName(const std::string& stTableName) const;

		                                      
		                                                                               
		                                                                
		static DbRelations* GetTranDbRelations();

	private:
		dbrelations_impl::Impl* m_pImpl;
	};
}

#endif                                   

