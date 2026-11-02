/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DBRELATIONS_KEYINF_H
#define KONGA_DBRELATIONS_KEYINF_H

#include <string>

namespace konga {

	                                                               
	class KeyInfo {
	public:
		                                                                        
		KeyInfo(const std::string& stColumnName, int iEntityType)
			: m_stColumnName(stColumnName), m_iEntityTypeId(iEntityType), m_bIsPrimary(true), m_bIsDependent(false) {}

		                                                                                       
		                                                                                              
		KeyInfo(const std::string stColumnName, const std::string& stParentTableName, bool bIsDependent = false)
			: m_stColumnName(stColumnName), m_iEntityTypeId(-1),
				m_bIsPrimary(false), m_bIsDependent(bIsDependent), m_stParentTableName(stParentTableName) {}


		~KeyInfo(void) {};

		std::string GetColumnName() const {
			return m_stColumnName;
		}

		                                                                                      
		                        
		int GetEntityTypeId() const {
			return m_iEntityTypeId;
		}

		                                                                   
		bool IsPrimary() const {
			return m_bIsPrimary;
		}

		                                                                       
		                                                                                     
		                                                                          
		bool IsDependent() const {
			return m_bIsDependent;
		}

		                                                                                               
		std::string GetParentTableName() const {
			return m_stParentTableName;
		}

	private:
		std::string m_stColumnName;
		int m_iEntityTypeId;
		bool m_bIsPrimary;
		bool m_bIsDependent;
		std::string m_stParentTableName;
	};
}

#endif                              

