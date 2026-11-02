/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_LDAP_DATA_STRUCTURE_H
#define KONGA_OPERATION_LDAP_DATA_STRUCTURE_H

#include <string>

#include "DataStructure.h"

namespace konga {

	class LdapDataStructure : public DataStructure {
	public:
		LdapDataStructure(void) : m_Nature(NATURE_NONE) {
		}
		virtual ~LdapDataStructure(void) {
		}

		DataStructureType getDataStructureType() const {
			return DATA_STRUCTURE_LDAP;
		}

		virtual DataStructureNature getNature() const {
			return m_Nature;
		}

		friend class LocationCreatorDb;

	private:
		DataStructureNature m_Nature;
	};
}
#endif                                         

