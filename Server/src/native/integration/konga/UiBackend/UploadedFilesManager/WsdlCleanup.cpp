/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <boost/filesystem/exception.hpp>
#include <boost/filesystem/operations.hpp>
#include <boost/filesystem/directory.hpp>

#include <konga/Utils/FileUtils.h>
#include <konga/Utils/util.h>		               

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/Server/Server.h>

#include "WsdlCleanup.h"

using namespace konga;
using namespace boost::filesystem;
using std::string;

namespace {
	bool IsWsdlUsed(const string& leaf, DbConnection& dbConn) {
		const string likePath( "%" + leaf + "%");
		string sql("select wsdl_id from webservicestab where inputxsdfilename LIKE '"
			+ likePath + "' or outputxsdfilename LIKE '"
			+ likePath + "'");
		SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&dbConn, sql) );
		rs->Open();
		if ( rs->Next() )
			return true;
		rs.CloseAndDelete();

		sql = "select transformation_id from transformationstab_new where source_xml LIKE '"
			+ likePath + "' or target_xml LIKE '"
			+ likePath + "'";
		rs.Attach( DynamicDbRecordSet::MakeInstance(&dbConn, sql) );
		rs->Open();
		if ( rs->Next() )
			return true;

		return false;
	}
}

void konga::CleanWsdlFiles(DbConnection& dbConn, string& sWarnings) {

	                                   
	string sWsdlDir(konga::GetUploadedWsdlBaseDirectoryWithSlash());

	directory_iterator end;
	path dir(sWsdlDir);
	directory_iterator dit(dir);
	for( ; dit != end; ++dit ) {
		const string& stPath = dit->path().string();
		if ( IsDirectory(stPath) ) {
			const string& leaf = konga::GetLeafPath(stPath);
			if (konga::IsGUIDString(leaf) ) {
				if ( !IsWsdlUsed(leaf, dbConn) ) {
					try {
						kRemoveDirectory(stPath);
					}
					catch( const CrException& e) {
						sWarnings += "Warning. Failed to delete directory: ";
						sWarnings += e.GetErrorString();
						sWarnings += "\n";
					}
				}
			}
		}
	}
}
