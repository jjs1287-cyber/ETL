/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_WSBASE_AUTHENTICATOR_H
#define KONGA_WS_WSBASE_AUTHENTICATOR_H

#include <konga/ItemAccess/UserInfo.h>

namespace konga {

	                                                                    
	class WsImplBaseAuthenticator {
	public:
		WsImplBaseAuthenticator(const char* user, const char* password);
		virtual ~WsImplBaseAuthenticator(void) {}

	protected:
		UserInfo m_UserInfo;
	};
}
#endif                                   

