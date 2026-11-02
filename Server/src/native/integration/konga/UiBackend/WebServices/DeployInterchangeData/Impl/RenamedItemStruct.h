/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_DEPLOY_RENAMED_ITEM_STRUCT_H
#define KONGA_WS_DEPLOY_RENAMED_ITEM_STRUCT_H

#include <string>


namespace konga {
namespace interchange_deploy {

	struct RenamedItemStruct {
		RenamedItemStruct(void) : m_iEntityTypeId(-1) {}
		RenamedItemStruct(int iEntityTypeId, const std::string& stGuid,
			const std::string& stOldName, const std::string& stNewName) :
		m_iEntityTypeId(iEntityTypeId), m_stGuid(stGuid),
			m_stOldName(stOldName), m_stNewName(stNewName)
		{}

		int m_iEntityTypeId;
		std::string m_stGuid;
		std::string m_stOldName;
		std::string m_stNewName;
	};
}
}

#endif                                         

