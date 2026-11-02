/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_AUTHENTICATE_IMPL_H
#define KONGA_WS_AUTHENTICATE_IMPL_H

#include <konga/Db/DbUtils/DbConnectionHolder.h>

#include "../AuthenticateResult.hpp"	                           

namespace konga {

	class AuthenticateImpl : private DbConnectionHolder {
	public:
		AuthenticateImpl();
		~AuthenticateImpl() {}

		AuthenticateResult* login(const char* const user, const char* const password);

		AuthenticateResult* loginWithVersion(const char* const user, const char* const password,
			const char* const version);

		AuthenticateResult* loginWithVersionAndKeyValues(const char* const user, const char* const password,
			const char* const version, WsKeyValuePairs* keyValues);
	};

}                   

#endif                                

