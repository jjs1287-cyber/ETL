/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <konga/UiBackend/GuidMappings/GuidToIdMapper.h>

#include "DeployUtils.h"

#include "SpecialHandlingDeployMessage.h"

using namespace konga;
using namespace konga::interchange_deploy;
using std::vector;
using std::string;

namespace {
	unsigned int GetColPos(const string& sColName, const xsd__string_Array& columnNames) {
		vector<string> vColName(1);
		vColName[0] = sColName;
		vColPosT colPos = GetFieldIndexes(columnNames, vColName);
		return colPos.at(0);
	}
}

SpecialHandlingDeployMessage::SpecialHandlingDeployMessage(const xsd__string_Array& columnNames, GuidToIdMapper& guidToIdMapper) :
Crypto(GetColPos("account_password", columnNames)),
m_iAccountColPos(GetColPos("account", columnNames)), m_iAccountPwdColPos(GetColPos("account_password", columnNames)),
m_guidToIdMapper(guidToIdMapper)
{
}

bool SpecialHandlingDeployMessage::HandleRowAfterInsert(long msgId, InterchangeDataRowStruct& row) {
	Crypto::HandleRowAfterInsert(msgId, row);

	if ( row.m_vFields.size() - 1 < m_iAccountPwdColPos )
		throw CrException("Internal Error: Not enough fields received for MessagesTab.");

	NullableString& account = row.m_vFields.at(m_iAccountColPos);
	if ( account != 0 ) {
		const string& sAccount = account.str();
		if ( !sAccount.empty() ) {
			NullableString& pwd = row.m_vFields.at(m_iAccountPwdColPos);
			if ( pwd == 0 ) {
				const string& guid = m_guidToIdMapper.GetGuid(msgId, EmailMessageEntityId);
				if ( !guid.empty() ) {
					AddWarning(EmailMessageEntityId, guid, "No password was specified for the mail account \""
						+ sAccount + "\". If this account requires a password you need to enter a non-empty password and redeploy.");
				}
			}
		}
	}
	return true;
}

bool SpecialHandlingDeployMessage::HandleRow(InterchangeDataRowStruct& row) {
	                                      
	return Crypto::HandleRow(row);
}
