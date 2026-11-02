/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DBUTILS_DBCONNECTION_HOLDER
#define KONGA_DBUTILS_DBCONNECTION_HOLDER

#include <string>

#include <boost/noncopyable.hpp>

namespace konga {

	class DbConnection;

	   
                                                                              
                                                   
                        
    
	class DbConnectionHolder : boost::noncopyable {
	public:
		explicit DbConnectionHolder(DbConnection* pDbConnection)
			: m_pDbConnection(pDbConnection), m_bOwnsConnection(false)
		{}
		explicit DbConnectionHolder(const std::string& stConnectionString = "")
			: m_pDbConnection(0), m_bOwnsConnection(false), m_stConnectionString(stConnectionString)
		{}

		virtual ~DbConnectionHolder(void);

		                                                                                                  
		                                                          
		void SetInternalDbName(const std::string& stDbName) {
			m_stBackEndDbName = stDbName;
		}

		                                                                          
		virtual DbConnection& GetDbConnection() const;

	protected:
		                                                                          
		virtual void ExecuteSql(const std::string& stSql) const;

		virtual void CloseDbConnectionIfOwner();

	private:
		mutable DbConnection* m_pDbConnection;
		mutable bool m_bOwnsConnection;
		mutable std::string m_stConnectionString;
		std::string m_stBackEndDbName;
	};
}                   
#endif                                     

