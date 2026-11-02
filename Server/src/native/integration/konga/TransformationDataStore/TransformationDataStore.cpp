/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/util.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/DbTypeUtils.h>
#include <konga/Utils/DateUtils.h>
#include <konga/Utils/Settings.h>

#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/AutoDbCleanup.h>

#include "TransformationDataStore.h"

using namespace konga;
using std::string;
using std::vector;

namespace {
	string GetBaseDir(const string& guid) {
		string dir( konga::GetOptionAsString("Settings", "TransformationDataStoreDirectory", "") );
		if ( dir.empty() || !konga::IsDirectory(dir) ) {
			dir = konga::getTmpDir() + "TranDataStore";
		}
		AppendPathSeparator(dir);
		dir += guid;
		konga::kCreateDirectory(dir);
		AppendPathSeparator(dir);
		return dir;
	}

	void AddNewStore(DbConnection* pTranDb, const string& guid) {
		pTranDb->ExecuteSql("insert into transformationdatastore (guid) values ('" + guid + "')");
	}

	void AddNewStoreIfNeeded(DbConnection* pTranDb, const string& guid) {
		AutoRollbackTransaction transaction(pTranDb);
		konga::LockTableForUpdate("transformationdatastore", *pTranDb);

		SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(pTranDb) );
		rs->SetSqlStatement("select 1 from transformationdatastore where guid = '" + guid + "'");
		rs->Open();
		if ( !rs->Next() ) {
			AddNewStore(pTranDb, guid);
		}

		transaction.Commit();
	}

	string AddFile(const string& fileName, const string& dir) {
		string retval;
		if( !fileName.empty() && konga::NonEmptyFileExists(fileName) ) {
			const string& leaf = GetLeafPath(fileName);
			const string& storeFile = konga::getTmpFileName(leaf, dir);
			konga::kCopyFile(fileName, storeFile, true);
			retval = storeFile;
		}

		return retval;
	}

	void DeleteFileIfItExists(const string& fileName) {
		if ( fileName.empty() ) return;
		if ( konga::FileExists(fileName) ) {
			konga::kDeleteFile(fileName);
		}
	}

	void UpdateInstance(DbConnection* pTranDb, const string& guid, int transformationId_, int operationId_, int originalSourceNumber_, int chunkNumber_,
		const string& sourceFile, const string& linkFile, const string& targetFile)
	{
		const int transformationId = transformationId_ > 0 ? transformationId_ : 0;
		const int operationId = operationId_ > 0 ? operationId_ : 0;

		const int originalSourceNumber = originalSourceNumber_ > 0 ? originalSourceNumber_ : 0;
		const int chunkNumber = chunkNumber_ > 0 ? chunkNumber_ : 0;

		string whereClause(" where guid = '" + guid);
		whereClause += "' and transformationid = " + konga::toString(transformationId);
		whereClause += " and operationid = " + konga::toString(operationId);
		whereClause += " and originalsourcenumber = " + konga::toString(originalSourceNumber);
		whereClause += " and chunknumber = " + konga::toString(chunkNumber);
		
		SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(pTranDb) );
		rs->SetSqlStatement("select sourcefile, linkfile, targetfile from transformationdatastoreinstance" + whereClause);
		rs->Open();
		if ( rs->Next() ) {
			const string& oldSource = rs->GetFieldValue(0);
			const string& oldLink = rs->GetFieldValue(1);
			const string& oldTarget = rs->GetFieldValue(2);
			rs.CloseAndDelete();

			pTranDb->ExecuteSql("update transformationdatastoreinstance set sourcefile = '" + konga::SqlEscape(sourceFile)
				+ "', linkfile = '" + konga::SqlEscape(linkFile)
				+ "', targetfile = '" + konga::SqlEscape(targetFile) + "'" + whereClause);
			DeleteFileIfItExists(oldSource);
			DeleteFileIfItExists(oldLink);
			DeleteFileIfItExists(oldTarget);
		}
		else {
			rs.CloseAndDelete();

			pTranDb->ExecuteSql("insert into transformationdatastoreinstance (guid,transformationid,operationid,originalsourcenumber,chunknumber,sourcefile,linkfile,targetfile) values('"
				+ guid + "'," + konga::toString(transformationId) + "," + konga::toString(operationId) + "," + konga::toString(originalSourceNumber) + ","
				+ konga::toString(chunkNumber) + ",'" + konga::SqlEscape(sourceFile) + "','" + konga::SqlEscape(linkFile) + "','" + konga::SqlEscape(targetFile) + "')");
		}
	}
}                       

TransformationDataStore::TransformationDataStore(DbConnection* pDbConn) : m_dbConnectionHolder(pDbConn) {
	if ( pDbConn ) {
		m_dbConnectionHolder.SetInternalDbName("TranDb");
	}
}

TransformationDataStore::TransformationDataStore(void) {
	m_dbConnectionHolder.SetInternalDbName("TranDb");
}

DbConnection& TransformationDataStore::GetDbConnection() const {
	return m_dbConnectionHolder.GetDbConnection();
}

void TransformationDataStore::AddFiles(TransformationDataStoreInstance& instance) {
	DbConnection* pTranDb = &GetDbConnection();

	string guid(instance.GUID);
	if( guid.empty() ) {
		guid = konga::GetGUIDString();
		AddNewStore(pTranDb, guid);
	}
	else {
		AddNewStoreIfNeeded(pTranDb, guid);
	}

	const string& dir = GetBaseDir(guid);

	const string sourceFile(AddFile(instance.sourceFile, dir));
	const string linkFile(AddFile(instance.linkFile, dir));
	const string targetFile(AddFile(instance.targetFile, dir));

	UpdateInstance(pTranDb, guid, instance.transformationId, instance.operationId, instance.originalSourceNumber, instance.chunkNumber, sourceFile, linkFile, targetFile);
	instance.sourceFile = sourceFile;
	instance.linkFile = linkFile;
	instance.targetFile = targetFile;

	if(instance.GUID.empty() ) instance.GUID = guid;
}

namespace {
	void AddAndClause(string& sqlWhere, const string& clause) {
		if ( !sqlWhere.empty() )
			sqlWhere.append(" AND ").append(clause);
		else
			sqlWhere.assign(clause);
	}
}

vector<TransformationDataStoreInstance> TransformationDataStore::GetFileList(const TransformationDataStoreInstance& input) const {
	DbConnection* pTranDb = &GetDbConnection();

	string sqlWhere;
	if ( !input.GUID.empty() ) {
		AddAndClause(sqlWhere, "guid = '" + input.GUID + "'");
	}
	if ( input.operationId > 0 ) {
		AddAndClause(sqlWhere, "operationid = " + toString(input.operationId));
	}
	if ( input.transformationId > 0 ) {
		AddAndClause(sqlWhere, "transformationid = " + toString(input.transformationId));
	}
	if ( input.originalSourceNumber > 0 ) {
		AddAndClause(sqlWhere, "originalsourcenumber = " + toString(input.originalSourceNumber));
	}
	if ( input.chunkNumber > 0 ) {
		AddAndClause(sqlWhere, "chunknumber = " + toString(input.chunkNumber));
	}
	
	string sql("SELECT guid, operationid, transformationid, originalsourcenumber, chunknumber, sourcefile, linkfile, targetfile FROM transformationdatastoreinstance");
	if ( !sqlWhere.empty() ) {
		sql.append(" WHERE ").append(sqlWhere);
	}

	sql.append(" ORDER BY operationid, transformationid, originalsourcenumber, chunknumber");

	SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(pTranDb, sql) );
	rs->Open();

	vector<TransformationDataStoreInstance> retVal;
	if ( !rs->Next() ) return retVal;

	do {
		TransformationDataStoreInstance inst;
		inst.GUID = rs->GetFieldValue(0);
		inst.operationId = toLong(rs->GetFieldValue(1));
		inst.transformationId = toLong(rs->GetFieldValue(2));
		inst.originalSourceNumber = toLong(rs->GetFieldValue(3));
		inst.chunkNumber = toLong(rs->GetFieldValue(4));
		inst.sourceFile = rs->GetFieldValue(5);
		inst.linkFile = rs->GetFieldValue(6);
		inst.targetFile = rs->GetFieldValue(7);

		retVal.push_back(inst);
	} while (rs->Next() );

	return retVal;
}

namespace {

	                                                                                      
	void setBaseDir(string& baseDir, const string& fileName) {
		if ( !baseDir.empty() ) return;
		if ( fileName.empty() ) return;

		const string& branchPath = konga::GetBranchPath(fileName);
		if ( branchPath.empty() || !konga::IsDirectory(branchPath) ) return;

		baseDir = branchPath;
	}

	string getDeleteInstanceSQL(const string& inClause) {
		return string("DELETE FROM transformationdatastoreinstance WHERE id IN (" + inClause + ")");
	}

	string getDeleteStoreSQL(const string& guid) {
		return string("DELETE FROM transformationdatastore WHERE guid IN (" + guid + ")");
	}

}

void TransformationDataStore::DeleteOld(int ageDays) {
	if ( ageDays < 1 ) ageDays = 1;

	DbConnection& tranDb = GetDbConnection();

	SmartDbPtr<DynamicDbRecordSet> nowRs( DynamicDbRecordSet::MakeInstance(&tranDb, "select NOW()") );
	nowRs->Open();
	if ( !nowRs->Next() ) return;	                         

	const TIMESTAMP_STRUCT dbNow = nowRs->GetFieldValueAsTimeStampStruct(0);
	nowRs.CloseAndDelete();	                                        

	CrDateTime now(dbNow.year, dbNow.month, dbNow.day, dbNow.hour, dbNow.minute, dbNow.second, 0);
	string debug1 = now.toString();
	CrDateTime added(now - CrTimeDuration(ageDays*24, 0, 0, 0));
	string debug2 = added.toString();
	
	string sql("SELECT guid FROM transformationdatastore WHERE started < '" + added.toString("yyyy-mm-dd HH:MM:SS") + "'");

	SmartDbPtr<DynamicDbRecordSet> rsStore( DynamicDbRecordSet::MakeInstance(&tranDb, sql) );
	rsStore->Open();
	if ( !rsStore->Next() ) return;

	string baseDir;
	vector<string> guids_to_delete;
	vector<string> instances_to_delete;
	do {
		baseDir.clear();
		const string& guid = rsStore->GetFieldValue(0);
		guids_to_delete.push_back(guid);
		string sql = "SELECT id, sourcefile, linkfile, targetfile FROM transformationdatastoreinstance WHERE guid = '" + guid + "'";
		SmartDbPtr<DynamicDbRecordSet> rsInstance( DynamicDbRecordSet::MakeInstance(&tranDb, sql) );
		rsInstance->Open();
		if ( !rsInstance->Next() ) continue;

		do {
			const string& id = rsInstance->GetFieldValue(0);

			const string source( rsInstance->GetFieldValue(1) );
			setBaseDir(baseDir, source);
			DeleteFileIfItExists(source);

			const string link( rsInstance->GetFieldValue(2) );
			setBaseDir(baseDir, link);
			DeleteFileIfItExists(link);

			const string target( rsInstance->GetFieldValue(3) );
			setBaseDir(baseDir, target);
			DeleteFileIfItExists(target);

			instances_to_delete.push_back(id);
		} while ( rsInstance->Next() );

		if ( !baseDir.empty() ) {
			konga::kRemoveDirectory(baseDir);
		}

	} while ( rsStore->Next() );
	rsStore.CloseAndDelete();

	string inClause;
	for( vector<string>::const_iterator instIt = instances_to_delete.begin(); instIt != instances_to_delete.end(); ++instIt ) {
		const string& id = *instIt;
		if ( !inClause.empty() ) inClause.append(1, ',');
		inClause.append(id);
		if ( inClause.length() > 500 ) {
			tranDb.ExecuteSql(getDeleteInstanceSQL(inClause));
			inClause.clear();
		}
	}
	if ( !inClause.empty() ) {
		tranDb.ExecuteSql(getDeleteInstanceSQL(inClause));
		inClause.clear();
	}

	for( vector<string>::const_iterator storeIt = guids_to_delete.begin(); storeIt != guids_to_delete.end(); ++storeIt ) {
		const string& guid = *storeIt;
		if ( !inClause.empty() ) inClause.append(1, ',');
		inClause.append(1, '\'').append(guid).append(1, '\'');
		if ( inClause.length() > 500 ) {
			tranDb.ExecuteSql(getDeleteStoreSQL(inClause));
			inClause.clear();
		}
	}
	if ( !inClause.empty() ) {
		tranDb.ExecuteSql(getDeleteStoreSQL(inClause));
		inClause.clear();
	}

}

