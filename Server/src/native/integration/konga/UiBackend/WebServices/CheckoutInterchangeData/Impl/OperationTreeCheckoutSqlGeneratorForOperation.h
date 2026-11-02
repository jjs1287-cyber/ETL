/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_CHECKOUT_OPERATION_TREE_CHECKOUT_SQL_GENERATOR_FOR_OPERATION_H
#define KONGA_WS_CHECKOUT_OPERATION_TREE_CHECKOUT_SQL_GENERATOR_FOR_OPERATION_H

#include "SelectSqlGenerator.h"

namespace konga {
	class UserInfo;

namespace interchange_checkout {

	class OperationTreeCheckoutSqlGeneratorForOperation : public SelectSqlGenerator {
	public:
		OperationTreeCheckoutSqlGeneratorForOperation(const UserInfo& userInfo) : m_userInfo(userInfo) {}
		~OperationTreeCheckoutSqlGeneratorForOperation(void) {}

		virtual std::string GetSql();

	private:
		const UserInfo& m_userInfo;
	};
}
}

#endif                                                                           

