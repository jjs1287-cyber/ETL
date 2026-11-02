/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <sstream>		               
#include <algorithm>	            

#include <konga/Utils/StringUtils.h>

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/DbRelations/DatabaseTableNameMappings.h>
#include <konga/DbRelations/EntityIdConstants.h>

#include "InterchangeProject.h"

using namespace konga;
using std::string;
using std::vector;
using std::map;
using std::stringstream;


const string& InterchangeProject::GetTableName() const {
	if ( m_stTableName.empty() ) {
		m_stTableName = getTableName(nameInterchangeProjects);
	}
	return m_stTableName;
}

string InterchangeProject::GetItemTableName(int iEntityTypeId) const {
	if ( IsValidEntityType(iEntityTypeId) )
		return GetInterchangeProjectEntityTab(iEntityTypeId);                                              
	else
		return "";
}

const string& InterchangeProject::GetIdName() const {
	if ( m_stIdName.empty() ) {
		m_stIdName = getIdName(nameInterchangeProjects);
	}
	return m_stIdName;
}

const vector<int>& InterchangeProject::GetValidEntityTypes() const {
	if ( m_vValidEntityTypes.empty() ) {
		m_vValidEntityTypes.push_back(FolderEntityId);
		m_vValidEntityTypes.push_back(SourceEntityId);
		m_vValidEntityTypes.push_back(TargetEntityId);
		m_vValidEntityTypes.push_back(TransformationEntityId);
		m_vValidEntityTypes.push_back(ScheduleEntityId);
		m_vValidEntityTypes.push_back(ProjectVariableEntityId); m_vValidEntityTypes.push_back(XsltTransformEntityId); m_vValidEntityTypes.push_back(ScriptEntityId);
		m_vValidEntityTypes.push_back(OperationEntityId);
		m_vValidEntityTypes.push_back(WebServiceEntityId);
		m_vValidEntityTypes.push_back(EmailMessageEntityId);
		m_vValidEntityTypes.push_back(DocumentEntityId);
		m_vValidEntityTypes.push_back(JmsMessageEntityId);
		m_vValidEntityTypes.push_back(HttpEndpointEntityId);
		m_vValidEntityTypes.push_back(SalesforceConnectorEntityId); m_vValidEntityTypes.push_back(SalesforceQueryEntityId);
		m_vValidEntityTypes.push_back(SalesforceUpsertEntityId);
		m_vValidEntityTypes.push_back(SalesforceCreateEntityId);
		m_vValidEntityTypes.push_back(SalesforceUpdateEntityId);
		m_vValidEntityTypes.push_back(SalesforceBulkLoadEntityId);
		m_vValidEntityTypes.push_back(SalesforceDeleteEntityId);
	}
	return m_vValidEntityTypes;
}

void InterchangeProject::AddItem(int iEntityTypeId, long lItemId) {
	if ( !Check(iEntityTypeId) )
		return;

	                                                    
	  
	stringstream ssSelectSql;
	ssSelectSql << "SELECT " << GetIdName()
				<< " FROM " << GetItemTableName(iEntityTypeId)
				<< " WHERE item_id = " << lItemId
				<< " AND interchangeproject_id = " << GetProjectId();

	SmartDbPtr<DynamicDbRecordSet> pSelectItem( DynamicDbRecordSet::MakeInstance(&GetDbConnection(), ssSelectSql.str()) );
	pSelectItem->Open();
	if ( pSelectItem->Next() ) return;
	pSelectItem->Close();

	                                               
	  
	stringstream ssInsertSql;
	ssInsertSql << "INSERT INTO " << GetItemTableName(iEntityTypeId)
				<< " VALUES (" << GetProjectId() << "," << lItemId << ")";

	ExecuteSql(ssInsertSql.str());	
}

bool InterchangeProject::DeleteItem(int iEntityTypeId, long lItemId) {
	if ( !Check(iEntityTypeId) )
		return true;

	{
	stringstream ssSql;
	ssSql << "DELETE FROM " << GetItemTableName(iEntityTypeId)
		  << " WHERE interchangeproject_id = " << GetProjectId()
		  << " AND item_id = " << lItemId;

	ExecuteSql(ssSql.str());
	}

	return this->GetNumberOfProjectsThatItemBelongsTo(iEntityTypeId, lItemId) == 0;
}

bool InterchangeProject::ItemBelongsToProject(int iEntityTypeId, long lItemId) const {
	if ( !Check(iEntityTypeId) )
		return false;

	stringstream ssSelectSql;
	ssSelectSql << "SELECT Item_ID FROM " << GetItemTableName(iEntityTypeId)
		<< " WHERE interchangeproject_id = " << GetProjectId()
		<< " AND item_id = " << lItemId;
	SmartDbPtr<DynamicDbRecordSet> pSelect( DynamicDbRecordSet::MakeInstance(&GetDbConnection(), ssSelectSql.str()) );
	pSelect->Open();
	return pSelect->Next();
}

bool InterchangeProject::Exists() {
	stringstream ssSelectSql;
	ssSelectSql << "SELECT InterchangeProject_Name, Description FROM " << GetTableName()
		<< " WHERE " << GetIdName()
		<< " = " << GetProjectId();

	SmartDbPtr<DynamicDbRecordSet> pSelect( DynamicDbRecordSet::MakeInstance(&GetDbConnection(), ssSelectSql.str()) );
	pSelect->Open();
	if ( pSelect->Next() ) {
		m_stName = pSelect->IsFieldNull(0) ? "" : pSelect->GetFieldValue(0);
		m_stDescription = pSelect->IsFieldNull(1) ? "" : pSelect->GetFieldValue(1);
		return true;
	}
	else
		return false;
}

int InterchangeProject::GetNumberOfItems(int iEntityTypeId) const {
	if ( !Check(iEntityTypeId) )
		return 0;

	stringstream ssSql;
	ssSql << "SELECT COUNT(*) FROM " << GetItemTableName(iEntityTypeId)
		  << " WHERE " << GetIdName() << " = " << GetProjectId();

	SmartDbPtr<DynamicDbRecordSet> pSelect( DynamicDbRecordSet::MakeInstance(&GetDbConnection(), ssSql.str()) );
	pSelect->Open();
	if ( pSelect->Next() ) {
		return atoi( pSelect->GetFieldValue(0).c_str() );
	}

	return 0;
}

vector<long> InterchangeProject::GetItemsOfType(int iEntityTypeId) const {
	stringstream ssSql;
	ssSql << "SELECT item_id FROM " << GetItemTableName(iEntityTypeId)
		  << " WHERE " << GetIdName() << " = " << GetProjectId();

	vector<long> v;
	SmartDbPtr<DynamicDbRecordSet> pSelect( DynamicDbRecordSet::MakeInstance(&GetDbConnection(), ssSql.str()) );
	pSelect->Open();
	while ( pSelect->Next() ) {
		v.push_back( atoi(pSelect->GetFieldValue(0).c_str()) );
	}

	return v;
}

void InterchangeProject::FillEntityMap(map<int, vector<long> >& m) const {
	const vector<int>& vEntityTypes = GetValidEntityTypes();
	vector<int>::const_iterator cit = vEntityTypes.begin();
	for(; cit != vEntityTypes.end(); ++cit ) {
		const vector<long>& v = GetItemsOfType(*cit);
		m.insert( map<int, vector<long> >::value_type(*cit,v) );
	}
}

bool InterchangeProject::IsEmpty() const {
	const vector<int>& vEntityTypes = GetValidEntityTypes();
	vector<int>::const_iterator cit = vEntityTypes.begin();
	for(; cit != vEntityTypes.end(); ++cit ) {
		if ( GetNumberOfItems(*cit) > 0 )
			return false;
	}
	return true;
}

bool InterchangeProject::DeleteProjectIfEmpty() {
	if ( IsEmpty() ) {
		{
			stringstream ssSql;
			ssSql << "DELETE FROM " << GetTableName()
				<< " WHERE " << GetIdName() << " = " << GetProjectId();
			ExecuteSql(ssSql.str());
		}

		stringstream ssSql;
		ssSql << "DELETE FROM " << getTableName(nameFolderItem)
			  << " WHERE entitytype_id = " << GetEntityIdByName(nameInterchangeProjects)
			  << " AND item_id = " << GetProjectId();
		ExecuteSql(ssSql.str());
		return true;
	}
	return false;
}

bool InterchangeProject::Check(int iEntityTypeId) const {
	if ( iEntityTypeId == InterchangeProjectEntityId )
		return false;

	if ( !IsValidEntityType(iEntityTypeId) )
		throw CrException("Internal Error: Items of type "
		+ GetEntityNameByEntityId(iEntityTypeId) + " cannot be part of an Interchange Project.");

	if ( GetProjectId() < 1 )
		throw CrException("Internal Error: InterchangeProject Id has not been initialized.");
	return true;
}

bool InterchangeProject::IsValidEntityType(int iEntityType) const {

	const vector<int>& v = GetValidEntityTypes();
	return std::find(v.begin(), v.end(), iEntityType) != v.end();
}

namespace {
	long GetProjectsThatItemBelongsToImpl(const string& sql, DbConnection& dbConn) {
		SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&dbConn, sql) );
		rs->Open();
		if ( !rs->Next() )
			return 0;

		return atoi( rs->GetFieldValue(0).c_str() );
	}
}

int InterchangeProject::GetNumberOfProjectsThatItemBelongsTo(int iEntityTypeId, long lItemId) const {
	string sql("SELECT COUNT(*) FROM " + GetItemTableName(iEntityTypeId)
		+ " WHERE item_id = " + toString(lItemId));
	return GetProjectsThatItemBelongsToImpl(sql, GetDbConnection());
}

int InterchangeProject::GetNumberOfOtherProjectsThatItemBelongsTo(int iEntityTypeId, long lItemId, long lProjectId) const {
	string sql("SELECT COUNT(*) FROM " + GetItemTableName(iEntityTypeId)
		+ " WHERE item_id = " + toString(lItemId)
		+ " AND interchangeproject_id != " + toString(lProjectId) );
	return GetProjectsThatItemBelongsToImpl(sql, GetDbConnection());
}

vector<long> InterchangeProject::GetProjectsThatItemBelongsTo(int iEntityTypeId, long lItemId) const {
	string sql("SELECT interchangeproject_id FROM " + GetItemTableName(iEntityTypeId)
		+ " WHERE item_id = " + toString(lItemId));

	vector<long> v;
	SmartDbPtr<DynamicDbRecordSet> rs( DynamicDbRecordSet::MakeInstance(&GetDbConnection(), sql) );
	rs->Open();
	if ( !rs->Next() )
		return v;

	do {
		const long l = toLong(rs->GetFieldValue(0));
		v.push_back(l);

	} while ( rs->Next() );

	return v;
}
