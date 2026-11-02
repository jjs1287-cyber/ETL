/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_DATABASE_DATA_STRUCTURE_H
#define KONGA_OPERATION_DATABASE_DATA_STRUCTURE_H

#include <string>

#include "DataStructure.h"

namespace konga {

	class DatabaseDataStructure : public DataStructure {
	public:
		DatabaseDataStructure(void) : m_Nature(NATURE_NONE) {
		}
		virtual ~DatabaseDataStructure(void) {
		}

		virtual DataStructureNature getNature() const {
			return m_Nature;
		}

		friend class LocationCreatorDb;

	private:
		DataStructureNature m_Nature;
		std::string m_stScm;
		std::string m_stTables;
		std::string m_stSQL;
	};
}
#endif                                             

