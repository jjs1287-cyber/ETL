/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_DB_MAINTENANCE_H
#define ETLFORGE_DB_MAINTENANCE_H

namespace konga {
	
	class DbConnection;

	void CleanOperationDataElementTab(DbConnection& pIDbConnection, DbConnection& pMsgDbConnection);
}

#endif                              

