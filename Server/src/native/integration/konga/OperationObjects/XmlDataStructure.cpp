/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <sstream>

#include <boost/filesystem/path.hpp>
#include <boost/filesystem/operations.hpp>
#include <boost/filesystem/exception.hpp>

#include <konga/Utils/util.h>	                   

#include <konga/SchemaManager/SchemaManager.h>
#include <konga/Utils/DBConnectionInfo.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/AutoFileCleanup.h>
#include <konga/Utils/ErrorResolver.h>

#include "XmlDataStructure.h"
#include "OperationEngineException.h"

using namespace std;
using namespace boost;
using namespace konga;

                    
namespace {
	const string stWHERE("Copying schema file to temporary directory");
}

XmlDataStructure::~XmlDataStructure(void) {
	ClearFileSet();
}

void XmlDataStructure::ClearFileSet() const {
	if ( m_pAutoDeleteDtds != 0 ) {
		delete m_pAutoDeleteDtds;
		m_pAutoDeleteDtds = 0;
	}
}

string XmlDataStructure::getSchemaFileName() const {
	return m_stSchema;
}

void XmlDataStructure::CopySourceSchemaFilesToTmpDir(const std::string& stTmpDir, DbConnection* pDbConn) const {
	try {
		std::auto_ptr<SchemaManager> mgr ( pDbConn ? new SchemaManager(pDbConn) : new SchemaManager() );
		mgr->getSourceDtds(m_lTransformationId);
		CopySchemaFilesToTmpDir(stTmpDir, *mgr);
	}
	catch( const CrException& ex ) {
		throw OperationEngineException(ex.GetErrorString());
	}
}

void XmlDataStructure::CopyTargetSchemaFilesToTmpDir(const std::string& stTmpDir, DbConnection* pDbConn) const {
	try {
		std::auto_ptr<SchemaManager> mgr ( pDbConn ? new SchemaManager(pDbConn) : new SchemaManager() );
		mgr->getTargetDtds(m_lTransformationId);
		CopySchemaFilesToTmpDir(stTmpDir, *mgr);
	}
	catch( const CrException& ex ) {
		throw OperationEngineException(ex.GetErrorString());
	}
}

void XmlDataStructure::CopySchemaFilesToTmpDir(const std::string& stTmpDir, SchemaManager& schemaMgr) const {
	ClearFileSet();
	m_pAutoDeleteDtds = new AutoDeleteFileSet();
	schemaMgr.copyToDir(*m_pAutoDeleteDtds, stTmpDir);

	                                
	const string& stSchema = getAbsolutePath( getSchemaFileName() );
	string stTarget( stTmpDir );
	AppendPathSeparator(stTarget);
	stTarget += GetLeafPath(stSchema);
	kCopyFile(stSchema, stTarget);
}

void XmlDataStructure::CopySchemaFilesToTmpDirOld(const string& stTmpDir, const string& stSourceOrTarget) const {
	string stSchema(getSchemaFileName());
	if ( stSchema.empty() )
		return;	                               

	filesystem::path schema(stSchema);
	if ( !filesystem::exists(schema) ) {
		schema = filesystem::path( getAbsolutePath(stSchema, false) );
		if ( !filesystem::exists(schema) ) {
			stringstream sErr;
			sErr << "Failed to copy " << stSourceOrTarget << " schema file to temporary directory." << endl;
			sErr << "The file \"" << schema.string() << "\" does not exist. Also tried to find " << stSchema << " but it did not exist." << endl;
			throw OperationEngineException(sErr.str(), stWHERE);
		}
	}

	                                                            
	filesystem::path copied(filesystem::path(stTmpDir) / schema.filename());
	if ( filesystem::exists(copied) )
		return;                                                                                                                       

	try {
		filesystem::copy_file(schema, copied);
	}
	catch ( const filesystem::filesystem_error& ex ) {
		stringstream sErr;
		sErr << "Failed to copy the " << stSourceOrTarget << " schema file \"" + schema.string() << '\"' << endl;
		sErr << "to the temporary directory \"" << copied.string() << "\"." << endl;
		sErr << ex.what() << ".";
		throw OperationEngineException(sErr.str(), stWHERE);
	}
}

