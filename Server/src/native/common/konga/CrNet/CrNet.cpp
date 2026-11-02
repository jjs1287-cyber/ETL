/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                               
  

#include "CrNet.h"
#include <comdef.h>

BOOL APIENTRY DllMain( HANDLE hModule, 
                       DWORD  ul_reason_for_call, 
                       LPVOID lpReserved
					 )
{
    switch (ul_reason_for_call)
	{
		case DLL_PROCESS_ATTACH:
		case DLL_THREAD_ATTACH:
		case DLL_THREAD_DETACH:
		case DLL_PROCESS_DETACH:
			break;
    }
    return TRUE;
}


extern "C" CRNET_API long __stdcall CreateSession(wchar_t* RemoteComputer, wchar_t* Domain, wchar_t* User, wchar_t* Password)
{
	if( !RemoteComputer || !Domain || !User || !Password )
	{
		::SetLastError(ERROR_INVALID_PARAMETER);
		return ERROR_INVALID_PARAMETER;
	}

	USE_INFO_2 ui2;
	::ZeroMemory(&ui2, sizeof ui2);

	wchar_t szPassword[256];
	wcscpy(szPassword,Password);

	wchar_t szResource[1024];
	wsprintfW(szResource,L"\\\\%s\\IPC$",RemoteComputer);

	ui2.ui2_remote = szResource;
	ui2.ui2_domainname = Domain;
	ui2.ui2_username = User;
	ui2.ui2_password = szPassword;
	ui2.ui2_asg_type = USE_WILDCARD;

	::SetLastError(0);
	DWORD nParmErr;
	NET_API_STATUS lStat = ::NetUseAdd(0,2, (BYTE*)&ui2, &nParmErr);
	::SetLastError(lStat);

	return lStat;
}

extern "C" CRNET_API long __stdcall DestroySession(wchar_t* RemoteComputer, int iUseForce)
{
	if( !RemoteComputer )
	{
		::SetLastError(ERROR_INVALID_PARAMETER);
		return ERROR_INVALID_PARAMETER;
	}

	wchar_t szResource[1024];
	wsprintfW(szResource,L"\\\\%s\\IPC$",RemoteComputer);

	::SetLastError(0);
	NET_API_STATUS lStat = ::NetUseDel(0,szResource,iUseForce);
	::SetLastError(lStat);

	return lStat;
}
