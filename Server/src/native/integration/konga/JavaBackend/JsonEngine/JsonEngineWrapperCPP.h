/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_JSON_ENGINE_WRAPPER_CPP_H
#define KONGA_JSON_ENGINE_WRAPPER_CPP_H

#include <konga/JavaBackend/JsonEngine/JsonEngineWrapper.h>

#include "JsonEngine.hpp"

namespace konga {

	class DbConnection;
	class UserInfo;

	                                
	class JsonEngineWrapperCPP : public JsonEngineWrapper {
	public:
		JsonEngineWrapperCPP(const std::string& userName, const std::string& md5pwd);
		virtual ~JsonEngineWrapperCPP(void);

		virtual void startNewSession();
		virtual void closeSession();

		virtual void jsonToXml(const std::string& jsonFilePath, const std::string& xmlFilePath, long transformationId);
		virtual void xmlToJson(const std::string& xmlFilePath, const std::string& jsonFilePath, long transformationId);

	private:
		WsJsonSessionId* createWsJsonSessionId();

	private:
		JsonEngine* m_wsImpl;

		std::string m_stUserName, m_stMD5Password;
		std::string m_stSessionId;
	};

}

#endif                                   
