/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <string>
#include <exception>
#include <iostream>

#include <konga/Utils/StringUtils.h>

#include <konga/Crypto/Crypto.h>

#include <konga/JavaBackend/JdbcEngine/JdbcEngine.hpp>

using std::string;
using namespace konga;

int main(int argc, char* argv[]) {
	if ( argc < 3 || !argv ) {
		std::cerr << "Usage: " << argv[0] << " username password" << std::endl;
		return -1;
	}

	const string userName(argv[1]);
	const string md5Pwd(md5(argv[2]));

	try {

		JdbcEngine jdbc("http://localhost:46912/axis/services/konga_jdbc_engine");

		xsd__string sessionId = jdbc.startNewSession(const_cast<xsd__string>(userName.c_str()), const_cast<xsd__string>(md5Pwd.c_str()));
		if ( sessionId == 0 )
			return 1;

		WsJdbcSessionId wsJdbcSessionId;
		wsJdbcSessionId.setetlforgeUser(const_cast<xsd__string>(userName.c_str()), true);
		wsJdbcSessionId.setmd5Pwd(const_cast<xsd__string>(md5Pwd.c_str()), true);
		wsJdbcSessionId.setsessionGuid(sessionId, true);

		WsDbLookupParams params;
		params.setsourceGuid("8deb55ff-9996-4534-afca-5828aa4ba6a1", true);
		params.setsql("select \"Namee\" from \"Albums\" where \"AlbumID\" = 1", true);

		WsDbLookupResult* res = jdbc.dbLookup(&wsJdbcSessionId, &params);
		if ( res && res->getvalue() ) {
			std::cout << res->getvalue() << std::endl;
		}
		delete res;

		jdbc.closeSession(&wsJdbcSessionId);
		delete sessionId;
	}
	catch( const std::exception& e ) {
		std::cerr << e.what() << std::endl;
		return 1;
	}
	return 0;
}

