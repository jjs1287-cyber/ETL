/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_LDAP_TARGET_LOCATION_H
#define KONGA_OPERATION_LDAP_TARGET_LOCATION_H

#include "TargetDataLocation.h"
#include "LdapLocation.h"

namespace konga {

	class LdapTargetLocation : public TargetDataLocation, public LdapLocation {
	public:
		LdapTargetLocation(void) { }
		~LdapTargetLocation(void) {}

		void doTransfer(const OperationDataElementHolder& deHolder, ProcessedFiles& files) const {
			                                                           
		}

	};
}
#endif                                          

