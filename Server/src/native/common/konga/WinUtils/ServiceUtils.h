/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef WINUTILS_SERVICE_UTILS_H
#define WINUTILS_SERVICE_UTILS_H

#include <windows.h>
#include <string>

void kStartService(const std::string& sServiceName);
void kStopService(const std::string& sServiceName);
void kDeleteService(const std::string& sServiceName);

bool IsServiceInstalled(const std::string& sServiceName);
bool IsServiceRunning(const std::string& sServiceName);
SERVICE_STATUS GetServiceStatus(const std::string& sServiceName);

void kCreateService(const std::string& sSvcName, const std::string& sSvcDisplayName,
				   const std::string& sSvcFileName, const std::string& sSvcDescription,
				   const std::string& sDependOnSvc = "", DWORD dwStartType = SERVICE_AUTO_START);
void DependService(const std::string& sMotherSvc, const std::string& sChildSvc);
void SetServiceRecovery(const std::string& sServiceName, ULONG lDelay, SC_ACTION_TYPE ActionType);
void SetServiceRunAs(const std::string& sServiceName, const std::string& sUserName, const std::string& sPassword);
void SetServiceStartType(const std::string& sServiceName, DWORD dwStartType);

#endif                            