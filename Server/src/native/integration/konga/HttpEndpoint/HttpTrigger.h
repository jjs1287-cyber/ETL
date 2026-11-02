/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_HTTP_TRIGGER_H
#define KONGA_HTTP_TRIGGER_H

#include <string>

namespace konga {

	class DbConnection;

	enum HttpTriggerType {
		HttpTriggerType_Unknown = 0,
		HttpTriggerType_HostedWebService = 1,
		HttpTriggerType_HttpEndpoint = 2
	};

	HttpTriggerType getHttpTriggerType(int itype);

	int toIntegerValue(const HttpTriggerType type);

	std::string getHttpTriggerTypeName(HttpTriggerType type);

	struct HttpTrigger {
		HttpTrigger(void) : Type(HttpTriggerType_Unknown), ItemId(0) {}
		~HttpTrigger(void) {}

		HttpTriggerType Type;
		long ItemId;
		std::string Url;
	};

	bool operator==(const HttpTrigger& lhs, const HttpTrigger& rhs);
	bool operator!=(const HttpTrigger& lhs, const HttpTrigger& rhs);

	HttpTrigger selectHttpTrigger(const std::string& url, DbConnection& dbConn);

	void addHttpTrigger(const HttpTrigger& trigger, DbConnection& dbConn);
	void removeHttpTrigger(const HttpTrigger& trigger, DbConnection& dbConn);
}

#endif                        

