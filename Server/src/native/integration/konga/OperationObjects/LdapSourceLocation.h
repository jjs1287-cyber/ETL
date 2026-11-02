/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_LDAP_SOURCE_LOCATION_H
#define KONGA_OPERATION_LDAP_SOURCE_LOCATION_H

#include "SourceDataLocation.h"
#include "LdapLocation.h"

namespace konga {

	class LdapSourceLocation : public SourceDataLocation, public LdapLocation {
	public:
		LdapSourceLocation(void) { }
		~LdapSourceLocation(void) {}

		void doTransfer(const OperationEngineContext& context, ProcessedFiles& files, OperationProgress& progress) const {
			files.add(ProcessedFile());
		}

		void doPostTransformation(const OperationEngineContext& context, ProcessedFiles& files) const {
			                
		}

		void getFirstSourceFile(const OperationEngineContext& context, ProcessedFiles& files) const {
			files.add(ProcessedFile());
		}
	};
}
#endif                                          

