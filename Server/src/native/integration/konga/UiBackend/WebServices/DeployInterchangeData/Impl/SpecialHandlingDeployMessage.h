/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_SPECIAL_HANDLING_DEPLOY_MESSAGE_H
#define KONGA_WS_SPECIAL_HANDLING_DEPLOY_MESSAGE_H

#include <konga/DbRelations/EntityIdConstants.h>

#include "SpecialHandlingDeployCrypto.h"
#include "ItemWarningHolder.h"

namespace konga {
	class GuidToIdMapper;

namespace interchange_deploy {

	class SpecialHandlingDeployMessage : public Crypto, public ItemWarningHolder {
	public:
		SpecialHandlingDeployMessage(const xsd__string_Array& columnNames, GuidToIdMapper& guidToIdMapper);
		~SpecialHandlingDeployMessage(void) {}

		virtual bool HandleRow(InterchangeDataRowStruct& row);

		virtual bool HandleRowAfterInsert(long newId, InterchangeDataRowStruct& row);

	private:
		const unsigned int m_iAccountColPos, m_iAccountPwdColPos;
		GuidToIdMapper& m_guidToIdMapper;
	};
}
}

#endif                                              

