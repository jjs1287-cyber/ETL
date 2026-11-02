/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <sstream>	               
#include <cassert>

#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>			             
#include <konga/Utils/AutoFileCleanup.h>	                    

#include <konga/Db/DbUtils/DbConnection.h>

#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/DbRelations.h>

#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>		                  

#include <konga/Server/Server.h>

#include "SpecialHandlingDeployDeleteTransformation.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::stringstream;

SpecialHandlingDeployDeleteTransformation::SpecialHandlingDeployDeleteTransformation(
	DbConnection& dbConnection, const DbRelations& tranDbRelations, AutoDeleteFileSet& autoDeleteFileSet)
: SpecialHandlingDeployDeleteDependents(dbConnection,
										tranDbRelations.GetDependentTables(getTableName(nameTransformationsTab_New))),
m_dbConnection(dbConnection), m_AutoDeleteFileSet(autoDeleteFileSet)
{
}


SpecialHandlingDeployDeleteTransformation::~SpecialHandlingDeployDeleteTransformation(void) {
	assert(m_vIdToDelete.empty());
}

bool SpecialHandlingDeployDeleteTransformation::HandleRow(long transformationId) {
	SpecialHandlingDeployDeleteDependents::HandleRow(transformationId);
	m_vIdToDelete.push_back(transformationId);
	return true;
}

namespace {
	void deleteJtrFilesForTransformation(long lTransformationId) {
		try {
			kDeleteFile( GetSourceJtrFileName(lTransformationId) );
			kDeleteFile( GetTargetJtrFileName(lTransformationId) );
		}
		catch( ... ) {
			                                                            
		}
	}
}

bool SpecialHandlingDeployDeleteTransformation::Delete() {
	SpecialHandlingDeployDeleteDependents::Delete();
	if ( !m_vIdToDelete.empty() ) {
		stringstream ssSqlIn;
		ssSqlIn << "(";
		vLongT::const_iterator cit = m_vIdToDelete.begin();
		for(; cit != m_vIdToDelete.end(); ++cit ) {
			const long& tranId = *cit;
			if ( cit != m_vIdToDelete.begin() )
				ssSqlIn << ",";
			ssSqlIn << tranId;

			deleteJtrFilesForTransformation(tranId);
		}
		ssSqlIn << ")";
		
		stringstream ssSql;
		ssSql << "DELETE FROM " << getTableName(nameTransformationChainLinks)
			  << " WHERE " << getIdName(nameTransformationsTab_New) << " IN "
			  << ssSqlIn.str()
			  << " OR nexttransformation_id IN "
			  << ssSqlIn.str();

		UIBACKEND_TRACE("SpecialHandlingDeployDeleteTransformation::Delete - " + ssSql.str());
		m_dbConnection.ExecuteSql(ssSql.str());

		m_vIdToDelete.clear();
		return true;
	}
	return false;
}

