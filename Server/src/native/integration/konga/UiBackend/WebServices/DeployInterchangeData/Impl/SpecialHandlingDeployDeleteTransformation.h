/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_SPECIAL_HANDLING_DEPLOY_DELETE_TRANSFORMATION_H
#define KONGA_WS_DEPLOY_SPECIAL_HANDLING_DEPLOY_DELETE_TRANSFORMATION_H

#include <vector>

#include "SpecialHandlingDeployDelete.h"

namespace konga {

	class DbConnection;
	class AutoDeleteFileSet;
	class DbRelations;

namespace interchange_deploy {

	   
                                                     
    
    
	class SpecialHandlingDeployDeleteTransformation : public SpecialHandlingDeployDeleteDependents {
	public:
		SpecialHandlingDeployDeleteTransformation(DbConnection& dbConnection,
			const DbRelations& tranDbRelations, AutoDeleteFileSet& autoDeleteFileSet);
		virtual ~SpecialHandlingDeployDeleteTransformation(void);

		virtual bool HandleRow(long transformationId);

		                                                   
		virtual bool Delete();

	private:
		DbConnection& m_dbConnection;
		AutoDeleteFileSet& m_AutoDeleteFileSet;
		typedef std::vector<long> vLongT;
		std::vector<long> m_vIdToDelete;
	};

}                   
}                                

#endif                                                                   

