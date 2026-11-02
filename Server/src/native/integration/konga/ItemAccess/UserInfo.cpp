/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include <sstream>		               

#include <konga/Db/DbUtils/DbConnection.h>
#include <konga/Db/DbUtils/DynamicDbRecordSet.h>
#include <konga/Db/DbUtils/SmartDbPtr.h>

#include <konga/Utils/StringUtils.h>

#include "UserInfo.h"

using namespace konga;

using std::string;
using std::vector;
using std::stringstream;

namespace {
	const long ADMIN_UID = 0;
}


UserInfo konga::GetUserInfo(long iUserId, DbConnection& dbConnection, bool bIncludeGroupInfo) {
	stringstream ssSql;
	ssSql << "SELECT first_name, last_name, login, email FROM UsersTab"
		  << " WHERE user_id = " << iUserId;

	SmartDbPtr<DynamicDbRecordSet> pUserInfo( DynamicDbRecordSet::MakeInstance(&dbConnection, ssSql.str()) );
	pUserInfo->Open();
	if ( !pUserInfo->Next() )
		throw CrException("There is no user defined with User_ID = " + toString(iUserId));

	const string stFirstName = pUserInfo->IsFieldNull(0) ? "" : pUserInfo->GetFieldValue(0);
	const string stLastName = pUserInfo->IsFieldNull(1) ? "" : pUserInfo->GetFieldValue(1);
	const string stLogin = pUserInfo->IsFieldNull(2) ? "" : pUserInfo->GetFieldValue(2);
	const string stEmail = pUserInfo->IsFieldNull(3) ? "" : pUserInfo->GetFieldValue(3);
	vector<GroupInfo> vGroups;
	if ( bIncludeGroupInfo ) {
		stringstream ssSql;
		ssSql << "SELECT g.group_id, g.group_name FROM GroupsTab g"
			<< " INNER JOIN User_GroupTab ug on ug.group_id = g.group_id and ug.user_id = " << iUserId;

		SmartDbPtr<DynamicDbRecordSet> pGroupInfo( DynamicDbRecordSet::MakeInstance(&dbConnection, ssSql.str()) );
		pGroupInfo->Open();
		while( pGroupInfo->Next() ) {
			long iGroupId = atol(pGroupInfo->GetFieldValue(0).c_str());
			const string stName = pGroupInfo->IsFieldNull(1) ? "" : pGroupInfo->GetFieldValue(1);
			vGroups.push_back( GroupInfo(iGroupId, stName) );
		}
	}

	return UserInfo(iUserId, vGroups, stLogin, stFirstName, stLastName, stEmail);
}

UserInfo konga::GetAdminUserInfo(DbConnection& dbConnection, bool bIncludeGroupInfo) {
	return GetUserInfo(ADMIN_UID, dbConnection, bIncludeGroupInfo);
}

GroupInfo konga::GetGroupInfo(long lGroupId, DbConnection& dbConnection) {
	stringstream ssSql;
	ssSql << "SELECT group_name, description FROM GroupsTab"
		  << " WHERE group_id = " << lGroupId;

	SmartDbPtr<DynamicDbRecordSet> pGroupInfo( DynamicDbRecordSet::MakeInstance(&dbConnection, ssSql.str()) );
	pGroupInfo->Open();
	if ( !pGroupInfo->Next() )
		throw CrException("There is no group defined with User_ID = " + toString(lGroupId));

	const string stName = pGroupInfo->IsFieldNull(0) ? "" : pGroupInfo->GetFieldValue(0);
	const string stDesc = pGroupInfo->IsFieldNull(1) ? "" : pGroupInfo->GetFieldValue(1);

	return GroupInfo(lGroupId, stName, stDesc);
}

UserInfo::UserInfo(long iUserId, const string& stLogin, const string& stFirstName,
			const string& stLastName, const string& stEmail)
: m_iUserId(iUserId), m_stLogin(stLogin), m_stFirstName(stFirstName),
m_stLastName(stLastName), m_stEmail(stEmail) { }

UserInfo::UserInfo(long iUserId, const std::vector<GroupInfo>& vGroups, const std::string& stLogin,
			const std::string& stFirstName , const std::string& stLastName,
			const std::string& stEmail)
: m_iUserId(iUserId), m_stLogin(stLogin), m_stFirstName(stFirstName), m_stLastName(stLastName),
m_stEmail(stEmail), m_vGroups(vGroups){ }


UserInfo::~UserInfo(void) {}

long UserInfo::GetId() const {
	return m_iUserId;
}

bool UserInfo::IsAdministrator() const {
	if ( IsAdminUser() )
		return true;

	vector<GroupInfo>::const_iterator cit = m_vGroups.begin();
	for(; cit != m_vGroups.end(); ++cit ) {
		if ( cit->IsAdministrator() )
			return true;
	}
	return false;
}

bool UserInfo::IsAdminUser() const {
	return ( m_iUserId == ADMIN_UID );
}

string UserInfo::GetLogin() const {
	return m_stLogin;
}

string UserInfo::GetFirstName() const {
	return m_stFirstName;
}

string UserInfo::GetLastName() const {
	return m_stLastName;
}

string UserInfo::GetEmail() const {
	return m_stEmail;
}

int UserInfo::GetNumberOfGroups() const {
	return m_vGroups.size();
}

vector<GroupInfo> UserInfo::GetGroups() const {
	return m_vGroups;
}

vector<long> UserInfo::GetGroupIds() const {
	vector<long> vGroupIds(m_vGroups.size());
	for(int i = 0, N = m_vGroups.size(); i < N; ++i ) {
		vGroupIds[i] = m_vGroups[i].GetId();
	}
	return vGroupIds;
}
