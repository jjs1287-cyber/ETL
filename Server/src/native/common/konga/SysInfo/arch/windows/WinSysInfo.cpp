/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */

#include <windows.h>

#define SECURITY_WIN32
#include <security.h>                 
#include <lmcons.h>         

#include <pdh.h>	                          
#include <pdhmsg.h>

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/AutoPtrCleanup.h>

#include <konga/WinUtils/RegistryUtils.h>	                      

#include "../../SysInfo.h"

using namespace konga;
using std::string;

typedef void (WINAPI *GetNativeSystemInfoT)(LPSYSTEM_INFO);
typedef BOOL (WINAPI *GetProductInfoT)(DWORD, DWORD, DWORD, DWORD, PDWORD);

string konga::GetOsName() {
	string sRet;
	BOOL bOsVersionInfoEx;
	DWORD dwType;

	OSVERSIONINFOEX osvi;
	ZeroMemory(&osvi, sizeof(OSVERSIONINFOEX));
	osvi.dwOSVersionInfoSize = sizeof(OSVERSIONINFOEX);

	if( !(bOsVersionInfoEx = GetVersionEx ((OSVERSIONINFO *) &osvi)) )
		return "unknown OS";

	SYSTEM_INFO si;
	ZeroMemory(&si, sizeof(SYSTEM_INFO));
	                                        
	GetNativeSystemInfoT pGetNativeSystemInfo;
	pGetNativeSystemInfo = (GetNativeSystemInfoT) GetProcAddress(GetModuleHandle(TEXT("kernel32.dll")), "GetNativeSystemInfo");
	if(pGetNativeSystemInfo)
		pGetNativeSystemInfo(&si);
	else
		GetSystemInfo(&si);

	if ( VER_PLATFORM_WIN32_NT==osvi.dwPlatformId && osvi.dwMajorVersion > 4 ) {
		sRet = "Microsoft ";

		if ( osvi.dwMajorVersion == 6 && osvi.dwMinorVersion == 0 ) {
			if( osvi.wProductType == VER_NT_WORKSTATION )
				sRet += "Windows Vista ";
			else
				sRet += "Windows Server 2008 ";

			GetProductInfoT pGetProductInfo;
			pGetProductInfo = (GetProductInfoT) GetProcAddress(GetModuleHandle(TEXT("kernel32.dll")), "GetProductInfo");
			pGetProductInfo( 6, 0, 0, 0, &dwType);

#if _WIN32_WINNT >= 0x0600
			switch( dwType )
			{
			case PRODUCT_ULTIMATE:
				sRet += "Ultimate Edition";
				break;
			case PRODUCT_HOME_PREMIUM:
				sRet += "Home Premium Edition";
				break;
			case PRODUCT_HOME_BASIC:
				sRet += "Home Basic Edition";
				break;
			case PRODUCT_ENTERPRISE:
				sRet += "Enterprise Edition";
				break;
			case PRODUCT_BUSINESS:
				sRet += "Business Edition";
				break;
			case PRODUCT_STARTER:
				sRet += "Starter Edition";
				break;
			case PRODUCT_CLUSTER_SERVER:
				sRet += "Cluster Server Edition";
				break;
			case PRODUCT_DATACENTER_SERVER:
				sRet += "Datacenter Edition";
				break;
			case PRODUCT_DATACENTER_SERVER_CORE:
				sRet += "Datacenter Edition (core installation)";
				break;
			case PRODUCT_ENTERPRISE_SERVER:
				sRet += "Enterprise Edition";
				break;
			case PRODUCT_ENTERPRISE_SERVER_CORE:
				sRet += "Enterprise Edition (core installation)";
				break;
			case PRODUCT_ENTERPRISE_SERVER_IA64:
				sRet += "Enterprise Edition for Itanium-based Systems";
				break;
			case PRODUCT_SMALLBUSINESS_SERVER:
				sRet += "Small Business Server";
				break;
			case PRODUCT_SMALLBUSINESS_SERVER_PREMIUM:
				sRet += "Small Business Server Premium Edition";
				break;
			case PRODUCT_STANDARD_SERVER:
				sRet += "Standard Edition";
				break;
			case PRODUCT_STANDARD_SERVER_CORE:
				sRet += "Standard Edition (core installation)";
				break;
			case PRODUCT_WEB_SERVER:
				sRet += "Web Server Edition";
				break;
			}
#endif                          
		} else if ( osvi.dwMajorVersion == 5 && osvi.dwMinorVersion == 2 ) {
#if _WIN32_WINNT >= 0x0501
			if( GetSystemMetrics(SM_SERVERR2) )
				sRet +=  "Windows Server 2003 R2, ";
			else
#endif
			if ( osvi.wSuiteMask==VER_SUITE_STORAGE_SERVER )
				sRet +=  "Windows Storage Server 2003";
			else if( osvi.wProductType == VER_NT_WORKSTATION && si.wProcessorArchitecture==PROCESSOR_ARCHITECTURE_AMD64)
				sRet +=  "Windows XP Professional x64 Edition";
			else
				sRet += "Windows Server 2003, ";

			if ( osvi.wProductType != VER_NT_WORKSTATION ) {
				if ( si.wProcessorArchitecture==PROCESSOR_ARCHITECTURE_IA64 ) {
					if( osvi.wSuiteMask & VER_SUITE_DATACENTER )
						sRet +=  "Datacenter Edition for Itanium-based Systems";
					else if( osvi.wSuiteMask & VER_SUITE_ENTERPRISE )
						sRet +=  "Enterprise Edition for Itanium-based Systems";
				} else if ( si.wProcessorArchitecture==PROCESSOR_ARCHITECTURE_AMD64 ) {
					if( osvi.wSuiteMask & VER_SUITE_DATACENTER )
						sRet +=  "Datacenter x64 Edition";
					else if( osvi.wSuiteMask & VER_SUITE_ENTERPRISE )
						sRet +=  "Enterprise x64 Edition";
					else
						sRet +=  "Standard x64 Edition";
				} else {
					if ( osvi.wSuiteMask & VER_SUITE_COMPUTE_SERVER )
					   sRet +=  "Compute Cluster Edition";
					else if( osvi.wSuiteMask & VER_SUITE_DATACENTER )
					   sRet +=  "Datacenter Edition";
					else if( osvi.wSuiteMask & VER_SUITE_ENTERPRISE )
					   sRet +=  "Enterprise Edition";
					else if ( osvi.wSuiteMask & VER_SUITE_BLADE )
					   sRet +=  "Web Edition";
					else sRet +=  "Standard Edition";
				}
			}
		} else if ( osvi.dwMajorVersion == 5 && osvi.dwMinorVersion == 1 ) {
			sRet += "Windows XP ";
			if( osvi.wSuiteMask & VER_SUITE_PERSONAL )
				sRet += "Home Edition";
			else sRet += "Professional";
		} else if ( osvi.dwMajorVersion == 5 && osvi.dwMinorVersion == 0 ) {
			sRet += "Windows 2000 ";
			if ( osvi.wProductType == VER_NT_WORKSTATION )
				sRet +=  "Professional";
			else {
				if( osvi.wSuiteMask & VER_SUITE_DATACENTER )
					sRet +=  "Datacenter Server";
				else if( osvi.wSuiteMask & VER_SUITE_ENTERPRISE )
					sRet +=  "Advanced Server";
				else sRet +=  "Server";
			}
		} else {
			sRet = GetRegistryString(HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion", "ProductName", "unknown OS");
		}

		if ( si.wProcessorArchitecture==PROCESSOR_ARCHITECTURE_AMD64 )
			sRet +=  ", 64-bit";
		else if (si.wProcessorArchitecture==PROCESSOR_ARCHITECTURE_INTEL )
			sRet += ", 32-bit";
		return sRet;
	}
	return GetRegistryString(HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion", "ProductName", "unknown OS");
}

string konga::GetOsVersion() {
	OSVERSIONINFOEX osvi;
	ZeroMemory(&osvi, sizeof(OSVERSIONINFOEX));

	osvi.dwOSVersionInfoSize = sizeof(OSVERSIONINFOEX);

	const BOOL bOsVersionInfoEx = GetVersionEx((OSVERSIONINFO*)&osvi);
	if( !bOsVersionInfoEx ) {
		osvi.dwOSVersionInfoSize = sizeof (OSVERSIONINFO);
		if (! GetVersionEx ( (OSVERSIONINFO *) &osvi) ) 
			return string("unknown");
	}

	string version(toString(osvi.dwMajorVersion) + "." + toString(osvi.dwMinorVersion));

	                    
	if ( osvi.wServicePackMajor > 0 ) {
		version += " SP " + toString(osvi.wServicePackMajor) + "." + toString(osvi.wServicePackMinor);
		if ( strlen(osvi.szCSDVersion) > 0 ) {
			version += " (" + string(osvi.szCSDVersion) + ")";
		}
	}
	else {
		version += " (No Service Pack Detected)";
	}

	                      
	if ( osvi.dwBuildNumber > 0 ) {
		version += " Build " + toString(osvi.dwBuildNumber);
	}
	return version;
}

string konga::GetPlatform() {
	return "Windows";
}

namespace {

	double toGb(double d) {
		const unsigned long lGB = 1024 * 1024 * 1024;
		return d/lGB;
	}

	struct AutoPerformanceCounter {
		AutoPerformanceCounter(HCOUNTER h) : hCounter(h) {}
		~AutoPerformanceCounter() { PdhRemoveCounter(hCounter); }
	private:
		HCOUNTER hCounter;
	};

	double getPerformanceCounterValueAsDouble(HQUERY hQuery, const string& sCounterName) {
		HCOUNTER hCounter;
		PDH_STATUS pdhStatus = PdhAddCounter(hQuery, sCounterName.c_str(), 0, &hCounter);
		if( pdhStatus == ERROR_SUCCESS ) {
			AutoPerformanceCounter adopt_hCounter(hCounter);
			pdhStatus = PdhCollectQueryData(hQuery);
			if( pdhStatus == ERROR_SUCCESS ) {
				PDH_FMT_COUNTERVALUE fmtValue;
				pdhStatus = PdhGetFormattedCounterValue(hCounter, PDH_FMT_LONG, 0, &fmtValue);
				if( pdhStatus == ERROR_SUCCESS ) {
					return fmtValue.longValue;
				}
			}
		}
		return 0;
	}
}

void konga::GetMemoryInfo(double& fGbRamInUse, double& fGbRamAvailable, double& fGbRamTotal,
			double& fGbSwapInUse, double& fGbSwapAvailable, double& fGbSwapTotal,
			double& fGbDiskInUse, double& fGbDiskAvailable, double& fGbDiskTotal)
{
	fGbRamInUse = 0,  fGbRamAvailable = 0,  fGbRamTotal = 0, 
	fGbSwapInUse = 0, fGbSwapAvailable = 0, fGbSwapTotal = 0,
	fGbDiskInUse = 0,  fGbDiskAvailable = 0,  fGbDiskTotal = 0;	

	MEMORYSTATUSEX mem;
	mem.dwLength = sizeof mem;
	BOOL bRet = GlobalMemoryStatusEx(&mem);
	if( bRet ) {
		fGbRamAvailable = toGb(mem.ullAvailPhys);
		fGbRamTotal = toGb(mem.ullTotalPhys);
		fGbSwapAvailable = toGb(mem.ullAvailVirtual);
		fGbSwapTotal = toGb(mem.ullTotalVirtual);
	}

	HQUERY hQuery;
	if( PdhOpenQuery(0, 0, &hQuery) == ERROR_SUCCESS ) {
		fGbRamInUse = toGb(getPerformanceCounterValueAsDouble(hQuery, "\\Memory\\Committed Bytes"));
		fGbSwapInUse = toGb(getPerformanceCounterValueAsDouble(hQuery, "\\Memory\\Cache Bytes"));
		PdhCloseQuery(hQuery);
	}

	ULARGE_INTEGER ulFreeBytesAvailable, ulTotalNumberOfBytes, ulTotalNumberOfFreeBytes;
	bRet = GetDiskFreeSpaceEx(0,                                                                              
				&ulFreeBytesAvailable, &ulTotalNumberOfBytes, &ulTotalNumberOfFreeBytes);
	if( bRet ) {
		fGbDiskInUse = toGb(ulTotalNumberOfBytes.QuadPart - ulFreeBytesAvailable.QuadPart);
		fGbDiskAvailable = toGb(ulFreeBytesAvailable.QuadPart);
		fGbDiskTotal = toGb(ulTotalNumberOfBytes.QuadPart);
	}
}

namespace {
	string CpuArchitectureToString(WORD wArch) {
		switch(wArch) {
		case PROCESSOR_ARCHITECTURE_INTEL: return "x86";
		case PROCESSOR_ARCHITECTURE_MIPS: return "MIPS";
		case PROCESSOR_ARCHITECTURE_ALPHA: return "Alpha";
		case PROCESSOR_ARCHITECTURE_PPC: return "PowerPC";
		case PROCESSOR_ARCHITECTURE_SHX: return "SHX";
		case PROCESSOR_ARCHITECTURE_ARM: return "ARM";
		case PROCESSOR_ARCHITECTURE_IA64: return "IA64";
		case PROCESSOR_ARCHITECTURE_ALPHA64: return "Alpha64";
		case PROCESSOR_ARCHITECTURE_MSIL: return "MSIL";
		case PROCESSOR_ARCHITECTURE_AMD64: return "AMD64";
		case PROCESSOR_ARCHITECTURE_IA32_ON_WIN64: return "x86 on Win64";
		default: return "unknown (" + toString(wArch) + ")";
		}
	}
}

void konga::GetCpuInfo(string& sVendor, string& sArchitecture, string& sProduct, unsigned long& nCores) {
	sVendor.clear();
	sArchitecture.clear();
	sProduct.clear();
	nCores = 0;

	SYSTEM_INFO si;
	GetSystemInfo(&si);
	nCores = si.dwNumberOfProcessors;
	sArchitecture = CpuArchitectureToString(si.wProcessorArchitecture);
	sVendor = GetRegistryString(HKEY_LOCAL_MACHINE, "Hardware\\Description\\System\\CentralProcessor\\0", "VendorIdentifier", "");
	sProduct = TrimString( GetRegistryString(HKEY_LOCAL_MACHINE, "Hardware\\Description\\System\\CentralProcessor\\0", "ProcessorNameString", "") );
	string sIdentifier = GetRegistryString(HKEY_LOCAL_MACHINE, "Hardware\\Description\\System\\CentralProcessor\\0", "Identifier", "");
	if( !sIdentifier.empty() )
		sProduct += " - " + sIdentifier;
}

string konga::getMachineName()
{
	TCHAR compName[256];
	DWORD compNameLen = 256;
	if( ::GetComputerName(compName, &compNameLen) )
		return compName;
	else
		return "localhost";
}

string konga::getAccountName()
{
	DWORD dwVersion = ::GetVersion();
	if( (DWORD)(LOBYTE(LOWORD(dwVersion))) < 5 ) {
		unsigned long lSize = UNLEN;
		char buf[ UNLEN ];
		if( ::GetUserName(buf,&lSize) ) {
			return string(buf);
		}
	}
	else {
		typedef BOOLEAN (CALLBACK* pF)(EXTENDED_NAME_FORMAT,LPTSTR,DWORD*);
		HMODULE hSecLib;
		pF GetUserNameEx;
		string ret;
		if( (hSecLib= ::LoadLibrary("Secur32")) &&
			(GetUserNameEx=(pF)::GetProcAddress(hSecLib,"GetUserNameExA")) )
		{
			unsigned long lSize=0;
			GetUserNameEx(NameSamCompatible,0,&lSize);
			AutoArrayDelete<char> buf(new char[lSize]);
			if( GetUserNameEx(NameSamCompatible,buf.Get(),&lSize) ) {
				ret = buf.Get();
				if( GetUserNameEx(NameDisplay,buf.Get(),&lSize) ) {
					ret += " (";
					ret += buf.Get();
					ret += ')';
				} else {
					buf.Reset(new char[lSize]);
					if( GetUserNameEx(NameDisplay,buf.Get(),&lSize) ) {
						ret += " (";
						ret += buf.Get();
						ret += ')';
					}
				}
			}
		}
		::FreeLibrary(hSecLib);
		return ret;
	}
	return "";
}

