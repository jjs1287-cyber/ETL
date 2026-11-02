/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_LDAP_LOCATION_H
#define KONGA_OPERATION_LDAP_LOCATION_H

#include "DataLocation.h"
#include "ProcessedFiles.h"

namespace konga {

	class LdapLocation : public DataLocation {
	public:
		LdapLocation(void) { }
		virtual ~LdapLocation(void) { }

		std::string getServer() const {
			return replace_de( m_CommonData.m_stLocator );
		}

		std::string getUid() const {
			return replace_de( m_CommonData.m_stLogin );
		}

		unsigned long getLoginTimeOut() const {
			if ( m_CommonData.m_lTimeOut < 0 )
				return 300;
			else
				return m_CommonData.m_lTimeOut;
		}

		long getConnectionMode() const {
			return m_CommonData.m_lSecured;
		}

		                                                                   
		long getPort() const {
			return m_CommonData.m_lPort;
		}

		int getSecured() const {
			return m_CommonData.m_lSecured;
		}

		long getReferralMode() const {
			return m_CommonData.m_lReferralMode;
		}

		                                                                
		                                                       
		virtual void testConnection() const;
	};
}
#endif                                   

