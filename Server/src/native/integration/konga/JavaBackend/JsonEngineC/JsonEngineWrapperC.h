/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_JSON_ENGINE_WRAPPER_C_H
#define KONGA_JSON_ENGINE_WRAPPER_C_H

#include <string>

#include <konga/JavaBackend/JsonEngine/JsonEngineWrapper.h>
#include <konga/JavaBackend/JsonEngineC/axis2_stub_JsonEngineService.h>

#include <konga/JavaBackend/JavaBackendConfiguration/Axis2CImpl.h>
#include <konga/JavaBackend/JavaBackendConfiguration/ServiceNames.h>


namespace konga {

	                                    
	class JsonEngineWrapperC : public JsonEngineWrapper, private Axis2CImplBase {
	public:
		JsonEngineWrapperC(const std::string& userName, const std::string& md5pwd);
		virtual ~JsonEngineWrapperC(void);

		                               
		virtual axis2_stub_t* create_axis2_stub_t();

		                                  
		virtual void startNewSession();
		virtual void closeSession();

		virtual void jsonToXml(const std::string& jsonFilePath, const std::string& xmlFilePath, long transformationId);
		virtual void xmlToJson(const std::string& xmlFilePath, const std::string& jsonFilePath, long transformationId);

	private:
		void handle_json_error(adb_WsJsonError_t* wsError, const std::string& header) const;

		adb_WsJsonSessionId_t* create_adb_WsJsonSessionId_t() const;
		adb_WsConversionParams_t* create_adb_WsConversionParams_t(const std::string& jsonFilePath, const std::string& xmlFilePath, long transformationId) const;

	};

}

#endif                                 
