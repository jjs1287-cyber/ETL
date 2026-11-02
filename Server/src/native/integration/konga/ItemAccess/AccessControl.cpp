/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <sstream>	               

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/DbRelations/DatabaseTableNameMappings.h>

#include "AccessControl.h"

using namespace konga;
using std::string;
using std::stringstream;

void AccessControl::DeleteAccessControlForItem(int iEntityTypeId, long lItemId, DbConnection& dbConnection) {

	stringstream ssSql1;
	ssSql1 << "DELETE FROM " << GetUserPermissionTableName(iEntityTypeId)
		<< " WHERE item_id = " << lItemId;
	dbConnection.ExecuteSql(ssSql1.str());

	stringstream ssSql2;
	ssSql2 << "DELETE FROM " << GetGroupPermissionTableName(iEntityTypeId)
		  << " WHERE item_id = " << lItemId;
	dbConnection.ExecuteSql(ssSql2.str());
}

void AccessControl::AddItem(long iItemId, int iEntityTypeId, AllowBits allowBits, DenyBits denyBits) {
	if ( IsAdminUser() )
		return;

	VerifyAllowBits(allowBits);
	VerifyDenyBits(denyBits);

	if ( allowBits == ACCESS_LEVEL_DENIED && denyBits == ACCESS_LEVEL_DENIED )
		DeleteItem(iItemId, iEntityTypeId);
	else {
		stringstream ssSql;
		ssSql << "INSERT INTO " << GetTableName(iEntityTypeId) << " VALUES ("
			  << iItemId << ","
			  << GetId() << ","
			  << allowBits << ","
			  << denyBits << ")";
		GetDbConnection().ExecuteSql(ssSql.str());
	}
}

void AccessControl::ChangeItem(long iItemId, int iEntityTypeId, AllowBits allowBits, DenyBits denyBits) {
	if ( IsAdminUser() )
		return;

	VerifyAllowBits(allowBits);
	VerifyDenyBits(denyBits);
	if ( allowBits == ACCESS_LEVEL_DENIED && denyBits == ACCESS_LEVEL_DENIED ) {
		                                                                                 
		DeleteItem(iItemId, iEntityTypeId);
		return;
	}

	                                                                       
	                                                                              
	AllowBits currentAllowBits;
	DenyBits currentDenyBits;
	AccessControl::GetPermissions(iItemId, iEntityTypeId, currentAllowBits, currentDenyBits);
	if ( currentAllowBits == ACCESS_LEVEL_DENIED && currentDenyBits == ACCESS_LEVEL_DENIED )
		AddItem(iItemId, iEntityTypeId, allowBits, denyBits);
	else if ( currentAllowBits != allowBits || currentDenyBits != denyBits ) {
		stringstream ssSql;
		ssSql << "UPDATE " << GetTableName(iEntityTypeId) << " SET AllowBits = " << allowBits
			  << ", DenyBits = " << denyBits
			  << " WHERE " << GetIdName() << " = " << GetId()
			  << " AND Item_ID = " << iItemId;
		GetDbConnection().ExecuteSql(ssSql.str());
	}
}

void AccessControl::DeleteItem(long iItemId, int iEntityTypeId) {
	if ( IsAdminUser() )
		return;

	stringstream ssSql;
	ssSql << "DELETE FROM " << GetTableName(iEntityTypeId)
		  << " WHERE " << GetIdName() << " = " << GetId()
		  << " AND Item_ID = " << iItemId;
	GetDbConnection().ExecuteSql(ssSql.str());
}

void AccessControl::DeleteItem(int iEntityTypeId) {
	if ( IsAdminUser() )
		return;

	stringstream ssSql;
	ssSql << "DELETE FROM " << GetTableName(iEntityTypeId)
		  << " WHERE " << GetIdName() << " = " << GetId();
	GetDbConnection().ExecuteSql(ssSql.str());
}

void AccessControl::DeleteItem() {
	DeleteItem( GetEntityIdByName(nameDocuments));
	DeleteItem( GetEntityIdByName(nameFolders));
	DeleteItem( GetEntityIdByName(nameInterchangeProjects));
	DeleteItem( GetEntityIdByName(nameJmsMessage));
	DeleteItem( GetEntityIdByName(nameMessages));
	DeleteItem( GetEntityIdByName(nameOperations));
	DeleteItem( GetEntityIdByName(nameHttpEndpoints) );
	DeleteItem( GetEntityIdByName(nameSchedules));
	DeleteItem( GetEntityIdByName(nameScripts));
	DeleteItem( GetEntityIdByName(nameSources));
	DeleteItem( GetEntityIdByName(nameTargets));
	DeleteItem( GetEntityIdByName(nameTransformationsTab_New));
	DeleteItem( GetEntityIdByName(nameWebServices));
	DeleteItem( GetEntityIdByName(nameWsdl));
	DeleteItem( GetEntityIdByName(nameXMLDtd));
	DeleteItem( GetEntityIdByName(nameInterchangeProjects));
}

void AccessControl::GetPermissions(long iItemId, int iEntityTypeId, AllowBits& allowBits, DenyBits& denyBits) const {
	allowBits = 0;
	denyBits = 0;

	stringstream ssSql;
	ssSql << "SELECT AllowBits, DenyBits FROM " << GetTableName(iEntityTypeId)
		<< " WHERE " << GetIdName() << " = " << GetId() << " AND"
		<< " Item_id = " << iItemId;

	SmartDbPtr<DynamicDbRecordSet> rsSelect(DynamicDbRecordSet::MakeInstance(&GetDbConnection(), ssSql.str()));
	rsSelect->Open();
	if ( rsSelect->Next() ) {
		int iAllowBits = atoi(rsSelect->GetFieldValue(0).c_str());
		int iDenyBits = atoi(rsSelect->GetFieldValue(1).c_str());
		allowBits = static_cast<AllowBits>(iAllowBits);
		denyBits = static_cast<DenyBits>(iDenyBits);
		return;
	}
	allowBits = ACCESS_LEVEL_DENIED;
}

bool AccessControl::HasPermission(long iItemId, int iEntityTypeId, AccessBit accessBit) const {
	if ( IsAdministrator() )
		return true;

	AllowBits allowBits;
	DenyBits denyBits;
	GetPermissions(iItemId, iEntityTypeId, allowBits, denyBits);

	if ( (allowBits & ACCESS_BIT_OWNER) != 0 ) {
		                                                           
		                                                                    
		allowBits = ACCESS_LEVEL_ADMINISTRATOR;

		                                                                        
		denyBits &= ~ACCESS_BIT_READ;
		denyBits &= ~ACCESS_BIT_CHANGE_PERMISSIONS;
	}

	return ( (allowBits & accessBit) != 0  && (denyBits & accessBit) == 0 );
}
