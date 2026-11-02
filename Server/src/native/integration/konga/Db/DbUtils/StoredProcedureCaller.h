/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_STORED_PROCEDURE_CALLER_H
#define KONGA_STORED_PROCEDURE_CALLER_H

#include <string>
#include <vector>

#include "OdbcConnectionHolder.h"
#include "DbProcedureColumn.h"
#include "DbConnection.h"

namespace konga {


	class StoredProcedureCaller : public OdbcConnectionHolder {
	public:

		enum SPParamType {
			PARAM_TYPE_NULL=0,
			PARAM_TYPE_STRING=1,
			PARAM_TYPE_INTEGER=2,
			PARAM_TYPE_REAL=3,
			PARAM_TYPE_DATETIME=4,
		};

		struct SPParam {
			SPParam() : type(PARAM_TYPE_NULL), value() {}
			SPParam(SPParamType t, const std::string& v) : type(t), value(v) {}
			SPParamType type;
			std::string value;
		};

		typedef std::vector<SPParam> SPParamsT;

	public:
		StoredProcedureCaller(const std::string& connectionString, TransactionIsolationLevel txnIsolationLevel)
			: OdbcConnectionHolder(connectionString), m_txnIsolationLevel(txnIsolationLevel) {};
		virtual ~StoredProcedureCaller(void) {};

		                                                                
		                                                            
		                                                              
		                                                    
		                             
		virtual void CallStoredProcedure(const std::string& spName,
			SPParamsT& parameters, SPParam& returnVal);

	private:
		void PrivateConnect(const std::string& stConnectionString);

		void GetProcessColumns(const std::string& sConnectionString,
			const std::string& spName,
			std::vector<DbProcedureColumn>& vColumns,
			DbProcedureColumn& retVal);

	private:
		TransactionIsolationLevel m_txnIsolationLevel;
	};

}                   

#endif                                   

