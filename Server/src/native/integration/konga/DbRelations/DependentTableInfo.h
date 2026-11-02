/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DBRELATIONS_DEPENDENTTABLESINFO_H
#define KONGA_DBRELATIONS_DEPENDENTTABLESINFO_H

#include <string>

namespace konga {

	class DependentTableInfo {
	public:
		DependentTableInfo(const std::string& stTableName, const std::string& stForeignKeyName)
			: m_stTableName(stTableName), m_stForeignKeyName(stForeignKeyName) {}
			
		~DependentTableInfo(void) {}

		std::string GetTableName() const {
			return m_stTableName;
		}

		std::string GetForeignKeyName() const {
			return m_stForeignKeyName;
		}

	private:
		std::string m_stTableName;
		std::string m_stForeignKeyName;
	};
}

#endif                                           

