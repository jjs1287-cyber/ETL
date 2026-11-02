/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_CONNECTION_TEST_CONNECTION_H
#define KONGA_WS_CONNECTION_TEST_CONNECTION_H

#include <konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.h>

#include "../ConnectionInfo.hpp"
#include "../FileList.hpp"

namespace konga {
	
	class TestConnectionImpl : WsImplBaseAuthenticator {
	public:
		TestConnectionImpl(const char* user, const char* password, bool bIsSource, const char* id);
		~TestConnectionImpl(void) {}

		FileList* TestFileTypeConnection(ConnectionInfo* pConnectionInfo);

		xsd__string TestDatabaseConnection(ConnectionInfo* pConnectionInfo);

		xsd__string TestLdapConnection(ConnectionInfo* pConnectionInfo);

	private:
		bool m_bIsSource;
		long m_lId;
		std::string m_stId;
	};

}                   

#endif                                         
