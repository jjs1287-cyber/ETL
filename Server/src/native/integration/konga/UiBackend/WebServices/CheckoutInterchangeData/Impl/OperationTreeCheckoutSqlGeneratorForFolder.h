/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_CHECKOUT_OPERATION_TREE_CHECKOUT_SQL_GENERATOR_FOR_FOLDER_H
#define KONGA_WS_CHECKOUT_OPERATION_TREE_CHECKOUT_SQL_GENERATOR_FOR_FOLDER_H

#include "SelectSqlGenerator.h"

namespace konga {
	class UserInfo;

namespace interchange_checkout {

	                                                           
	                                                     
	class OperationTreeCheckoutSqlGeneratorForFolder : public SelectSqlGenerator {
	public:
		OperationTreeCheckoutSqlGeneratorForFolder(const UserInfo& userInfo) : m_bIsItem(false), m_userInfo(userInfo) { }
		virtual ~OperationTreeCheckoutSqlGeneratorForFolder(void) { }

		void SetIsItem(bool bIsItem) { m_bIsItem = bIsItem; }

		virtual std::string GetSql();

	private:
		bool m_bIsItem;
		const UserInfo& m_userInfo;
	};

}
}

#endif                                                                        

