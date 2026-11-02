/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_CHECKOUT_OPERATION_TREE_CHECKOUT_SQL_GENERATOR_FOR_SCHEDULE_H
#define KONGA_WS_CHECKOUT_OPERATION_TREE_CHECKOUT_SQL_GENERATOR_FOR_SCHEDULE_H

#include "SelectSqlGenerator.h"

namespace konga {
	class UserInfo;

namespace interchange_checkout {

	class OperationTreeCheckoutSqlGeneratorForSchedule : public SelectSqlGenerator {
	public:
		OperationTreeCheckoutSqlGeneratorForSchedule(const UserInfo& userInfo) : m_userInfo(userInfo) {}
		~OperationTreeCheckoutSqlGeneratorForSchedule(void) {}

		virtual std::string GetSql();

	private:
		const UserInfo& m_userInfo;
	};
}
}

#endif                                                                          

