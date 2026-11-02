/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef OPERATION_CONTEXT_ACCESS_PROTECTED_DATA_ELEMENTS_HANDLER_H
#define OPERATION_CONTEXT_ACCESS_PROTECTED_DATA_ELEMENTS_HANDLER_H

#include "DataElementHandler.h"

namespace konga {

	class UserInfo;
	class DbConnection;
	class OperationDataElementHolder;

	class AccessProtectedDataElementsHandler : public DataElementHandler {
	public:
		static void RegisterAll(OperationDataElementHolder& deHolder, AccessProtectedDataElementsHandler& handler);
		static void UnRegisterAll(OperationDataElementHolder& deHolder);

	public:
		AccessProtectedDataElementsHandler(const UserInfo& userInfo) : m_UserInfo(userInfo) {}
		~AccessProtectedDataElementsHandler(void) {}

		virtual DataElement* getDataElement(const std::string& deName) const;

	private:
		const UserInfo& m_UserInfo;
	};
}

#endif                                                              

