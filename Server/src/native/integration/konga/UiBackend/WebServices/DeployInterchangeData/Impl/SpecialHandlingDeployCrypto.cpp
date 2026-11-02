/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <string>

#include <konga/Crypto/Crypto.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>

#include <konga/UiBackend/WebServices/Utils/WsUtils.h>

#include "../InterchangeDataRow.hpp"
#include "SpecialHandlingDeployCrypto.h"

using namespace std;
using namespace konga;
using namespace konga::interchange_deploy;

bool Crypto::HandleRow(InterchangeDataRowStruct& row) {
	bool bDidEncryption = false;
	ColPosT::const_iterator citColPos = m_vColPosToEncrypt.begin();

	for(; citColPos != m_vColPosToEncrypt.end(); ++citColPos) {
		if ( row.m_vFields.size() < static_cast<vFieldsT::size_type>(*citColPos+1))
			throw CrException("Internal Error: Cannot modify column position " + toString(*citColPos) +
				" because there are only " + toString(row.m_vFields.size()) + " columns in total. Please report his bug.");

		const NullableString& pwd = row.m_vFields.at(*citColPos);
		if ( pwd  == 0 )
			continue;                     

		                     
		row.m_vFields[*citColPos] = EncryptString(pwd.str());
		bDidEncryption = true;
	}
	return bDidEncryption;
}
