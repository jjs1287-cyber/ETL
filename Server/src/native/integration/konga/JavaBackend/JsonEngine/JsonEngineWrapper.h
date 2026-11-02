/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_JSON_ENGINE_WRAPPER_H
#define KONGA_JSON_ENGINE_WRAPPER_H

#include <string>

namespace konga {

	class DbConnection;
	class UserInfo;


	class JsonEngineWrapper {
	public:
		static JsonEngineWrapper* createJsonEngineWrapper(DbConnection& tranDbConn, const UserInfo& userInfo);

	public:
		JsonEngineWrapper(void) {};
		virtual ~JsonEngineWrapper(void) {};

		virtual void startNewSession() = 0;
		virtual void closeSession() = 0;

		virtual void jsonToXml(const std::string& jsonFilePath, const std::string& xmlFilePath, long transformationId) = 0;
		virtual void xmlToJson(const std::string& xmlFilePath, const std::string& jsonFilePath, long transformationId) = 0;

	};

}

#endif                               
