/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ADMIN_IMPL_H
#define ADMIN_IMPL_H

#include <konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.h>

#include <axis/AxisUserAPI.hpp>                             
#include "../Admin.hpp"			               

namespace konga {

class AdminImpl : public WsImplBaseAuthenticator {
public:
	AdminImpl(const char* user, const char* password);

	                                                 
	  
	void startEngine();
	void stopEngine();
	axiscpp::xsd__boolean isEngineRunning();
	ServerStatus* getEngineStatus(int iDetailLevel);

	void startScheduler();
	void stopScheduler();
	axiscpp::xsd__boolean isSchedulerRunning();
	ServerStatus* getSchedulerStatus(int iDetailLevel);

	void startJmsEngine();
	void stopJmsEngine();
	axiscpp::xsd__boolean isJmsEngineRunning();
	ServerStatus* getJmsEngineStatus(int iDetailLevel);

protected:
	axiscpp::xsd__boolean isServerRunning(unsigned long lCommandPort, const char*sServerName);
	ServerStatus* getServerStatus(int iDetailLevel, unsigned long lCommandPort, const char*sServerName);

private:
	std::string m_user, m_password;
};

}                   

#endif                
