/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_SPECIAL_HANDLING_DEPLOY_CALLRESTRICTIONS_H
#define KONGA_WS_SPECIAL_HANDLING_DEPLOY_CALLRESTRICTIONS_H

#include "../InterchangeDataRow.hpp"

#include "DeployUtils.h"

#include "SpecialHandlingDeploy.h"

namespace konga {

	class GuidToIdMapper;
	class FunctionIdMapper;

namespace interchange_deploy {

                                                                      
class CallRestrictions : public SpecialHandlingDeploy {
public:
	                                                    
	CallRestrictions(const FunctionIdMapper& functionIdMapper,
		GuidToIdMapper& guidToIdMapper, DbConnection& dbConnection)
		: m_functionIdMapper(functionIdMapper), m_guidToIdMapper(guidToIdMapper),
		m_dbConnection(dbConnection),
		m_functionIdColumnIndex(0)
	{
		init();
	}

	virtual ~CallRestrictions() {};

	bool HandleRow(InterchangeDataRowStruct& row);

protected:
	virtual long GetWebServiceId(InterchangeDataRowStruct& row) const;
	int GetFunctionIdColumnIndex() const { return m_functionIdColumnIndex; }
	void init();

private:
	const FunctionIdMapper& m_functionIdMapper;
	GuidToIdMapper& m_guidToIdMapper;
	DbConnection& m_dbConnection;
	int m_functionIdColumnIndex;
};

typedef CallRestrictions CallRestrictionsInsert;
typedef CallRestrictions CallRestrictionsUpdate;

}                   
}                                

#endif                                                       

