/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_DATABASE_LOCATION_H
#define KONGA_OPERATION_DATABASE_LOCATION_H

#include <konga/Utils/StatusReceiver.h>

#include <konga/Db/DbUtils/DbConnection.h>		                            

#include "DataLocation.h"
#include "ProcessedFiles.h"

namespace konga {

	class DatabaseLocation : public DataLocation {
	protected:
		DatabaseLocation(void) { }

	public:
		static TransactionIsolationLevel fromClientIdentifier(int dbTxnLevel);

	public:
		virtual ~DatabaseLocation(void) { }

		std::string getDriver() const {
			return replace_de( m_CommonData.m_stDriver );
		}

		std::string getServer() const {
			return replace_de( m_CommonData.m_stLocator );
		}

		long getPort() const {
			return m_CommonData.m_lPort;
		}

		std::string getUid() const {
			return replace_de( m_CommonData.m_stLogin );
		}

		std::string getDSN() const {
			return replace_de( m_CommonData.m_stDSN );
		}

		unsigned long getLoginTimeOut() const {
			if ( m_CommonData.m_lTimeOut < 0 )
				return 300;
			else
				return m_CommonData.m_lTimeOut;
		}

		std::string getDatabaseName() const {
			return replace_de( m_CommonData.m_stDBName );
		}

		std::string getAdditionalConnectStringParameters() const {
			return replace_de( m_CommonData.m_stOptionalConnectString );
		}

		std::string getConnectString() const;

		TransactionIsolationLevel getTransactionIsolationLevel() const;

		                                        
		                                                       
		virtual void testConnection(StatusReceiver& sr) const;
	};
}
#endif                                       

