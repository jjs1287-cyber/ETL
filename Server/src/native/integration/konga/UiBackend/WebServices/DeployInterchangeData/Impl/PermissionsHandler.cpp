/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <string>

#include "konga/ItemAccess/UserAccess.h"
#include "konga/ItemAccess/GroupAccess.h"

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/DatabaseTableNameMappings.h>

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>
#include <konga/UiBackend/WebServices/Utils/UiBackendLogging.h>

#include "../PermissionsHolder.hpp"
#include "PermissionsHandler.h"


using namespace konga;
using namespace konga::interchange_deploy;
using std::string;

namespace {
	                                                                                                               
	void AddAccessControl(AccessControl& accessControl,
		UserAccess& currentUserAccess,
		const CUserPermissions& userPermissions,
		GuidToIdMapper& guidToIdMapper,
		ItemWarningHolder& itemWarningHolder)
	{
		if ( userPermissions.Permissions == 0 || userPermissions.Permissions->item == 0 )
			return;

		int nPermissions = -1;
		CPermission** arrPermission = userPermissions.Permissions->item->get(nPermissions);
		for( int j = 0; j < nPermissions; ++j ) {
			if ( !arrPermission[j] )
				continue;
			const CPermission& permission = *arrPermission[j];
			if ( permission.ItemId != 0 ) {
				const string stItemId(permission.ItemId);
				const long iEntityType = permission.EntityTypeId;
				const long lItemId = guidToIdMapper.GetId(stItemId, iEntityType);
				if ( lItemId == GuidToIdMapper::invalid_id ) {
					itemWarningHolder.AddWarning(iEntityType, stItemId, "This " + GetEntityNameByEntityIdSingular(iEntityType)
						+ " has not been deployed to the server. Try to deploy the entire project again.");
					continue;
				}
				if ( currentUserAccess.HasChangePermissionPermission(lItemId, iEntityType) ) {
					accessControl.ChangeItem(lItemId, iEntityType, permission.AllowBit, permission.DenyBits);
				}
				else {
					itemWarningHolder.AddWarning(iEntityType, stItemId,  "Access Denied: The current user is not allowed to change user permissions on this item.");
				}
			}
		}
	}
}

void PermissionsHandler::HandlePermissions(PermissionsHolder& permissionsHolder) {

	if ( permissionsHolder.UserPermissions &&
		 permissionsHolder.UserPermissions->item ) {
		int nUserPermissions = -1;
		CUserPermissions** arrUserPermissions = permissionsHolder.getUserPermissions()->item->get(nUserPermissions);

		for( int i = 0; i < nUserPermissions; ++i ) {
			if ( !arrUserPermissions[i] )
				continue;

			const CUserPermissions& userPermissions = *arrUserPermissions[i];

			const string stUserOrGroupId( userPermissions.UserId );
			const long lUserId = m_guidToIdMapper.GetId(stUserOrGroupId, UserEntityId);
			if ( lUserId != GuidToIdMapper::invalid_id ) {
				UserInfo userInfo = GetUserInfo(lUserId, m_dbConnection, false);	                                                                             
				UserAccess userAccess(userInfo, m_dbConnection);
				AddAccessControl(userAccess, m_currentUserAccess, userPermissions, m_guidToIdMapper, *this);
				continue;
			}

			                                                 
			const long lGroupId = m_guidToIdMapper.GetId(stUserOrGroupId, GroupEntityId);
			if ( lGroupId != GuidToIdMapper::invalid_id ) {
				GroupInfo groupInfo = GetGroupInfo(lGroupId, m_dbConnection);
				GroupAccess groupAccess(groupInfo, m_dbConnection);
				AddAccessControl(groupAccess, m_currentUserAccess, userPermissions, m_guidToIdMapper, *this);
				continue;
			}
			
			                                                                                                   
			UIBACKEND_TRACE("There is no user or group defined on this server with GUID = " + stUserOrGroupId);
		}
	}
}
