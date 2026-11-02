/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_INTERCHANGE_PROJECT_INTERCHANGE_PROJECT_H
#define KONGA_INTERCHANGE_PROJECT_INTERCHANGE_PROJECT_H

#include <string>
#include <vector>
#include <map>

#include <konga/Db/DbUtils/DbConnectionHolder.h>

namespace konga {

	class DbConnection;

	class InterchangeProject : private DbConnectionHolder {
	public:
		InterchangeProject(long lId, const std::string& stName = "", const std::string& stDescription = "")
			: DbConnectionHolder(), m_lId(lId), m_stName(stName), m_stDescription(stDescription)
		{
			SetInternalDbName("TranDb");
		}

		InterchangeProject(long lId, const std::string& stName, DbConnection* pDbConnection)
			: DbConnectionHolder(pDbConnection), m_lId(lId), m_stName(stName)
		{
		}

		~InterchangeProject(void) {}

		long GetProjectId() const {
			return m_lId;
		}

		void SetProjectId(long lId) {
			m_lId = lId;
		}

		std::string GetName() const {
			return m_stName;
		}

		void SetName(const std::string& stName) {
			m_stName = stName;
		}

		std::string GetDescription() const {
			return m_stDescription;
		}

		void SetDescription(const std::string& stDescription) {
			m_stDescription = stDescription;
		}

		                                           
		void AddItem(int iEntityTypeId, long lItemId);

		                                                 
		                                                                  
		bool DeleteItem(int iEntityTypeId, long lItemId);

		                                                    
		bool ItemBelongsToProject(int iEntityTypeId, long lItemId) const;

		   
                                             
                                                                           
     
		bool Exists();

		   
                                                                           
     
		int GetNumberOfItems(int iEntityTypeId) const;

		   
                                                                                    
     
		void FillEntityMap(std::map<int, std::vector<long> >& m) const;

		   
                                                                                        
     
		bool IsEmpty() const;

		   
                                                         
                                                              
     
		bool DeleteProjectIfEmpty();

		                                                                           
		std::string GetItemTableName(int iEntityTypeId) const;

		int GetNumberOfProjectsThatItemBelongsTo(int iEntityTypeId, long lItemId) const;

		int GetNumberOfOtherProjectsThatItemBelongsTo(int iEntityTypeId, long lItemId, long lProjectId) const;

		std::vector<long> GetProjectsThatItemBelongsTo(int iEntityTypeId, long lItemId) const;

	protected:
		   
                                                                     
     
		std::vector<long> GetItemsOfType(int iEntityTypeId) const;

		                                                                                                         
		                                                                        
		bool Check(int iEntityTypeId) const;

		const std::string& GetTableName() const;

		const std::string& GetIdName() const;

		                                                                                       
		const std::vector<int>& GetValidEntityTypes() const;

		                                                                               
		bool IsValidEntityType(int iEntityType) const;

	private:
		long m_lId;
		std::string m_stName;
		std::string m_stDescription;

		mutable std::string m_stTableName, m_stIdName;
		mutable std::vector<int> m_vValidEntityTypes;
	};
}                   

#endif                                                   


