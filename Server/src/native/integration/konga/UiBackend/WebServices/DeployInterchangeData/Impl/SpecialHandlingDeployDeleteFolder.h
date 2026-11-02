/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_SPECIAL_HANDLING_DEPLOY_DELETE_FOLDER_H
#define KONGA_WS_DEPLOY_SPECIAL_HANDLING_DEPLOY_DELETE_FOLDER_H

#include "SpecialHandlingDeployDelete.h"

namespace konga {
	
	class DbConnection;

namespace interchange_deploy {

	                                       
	class SpecialHandlingDeployDeleteFolder : public SpecialHandlingDeployDelete {

	public:
		SpecialHandlingDeployDeleteFolder(DbConnection& dbConnection) :
		  m_dbConnection(dbConnection) {}
		virtual ~SpecialHandlingDeployDeleteFolder(void) {};

		virtual bool HandleRow(long folderId);

		virtual bool Delete() { return false; }

	private:
		DbConnection& m_dbConnection;
	};

}                   
}                                

#endif                                                           

