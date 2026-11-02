/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <shlwapi.h>	              

#include <string>
#include <utility>		         
#include <vector>

#include <konga/Utils/AutoPtrCleanup.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/ThreadUtils.h>

#include "RegistryUtils.h"

using namespace konga;
using std::string;

namespace konga {

int OpenRemoteRegistry(const std::string& stMachineName, const HKEY& Key, HKEY& hNetKey)
{
                                                                     
	if ( stMachineName.empty() )
		hNetKey = Key;
	else
	{
		std::string stMachine;
		if( stMachineName.substr(0,2) == "\\\\" )
			stMachine = stMachineName;
		else
			stMachine = "\\\\" + stMachineName;
		if ( ::RegConnectRegistry(stMachine.c_str(), Key, &hNetKey) != ERROR_SUCCESS )
			return -1;
	}
	return 0;
}

int DeleteRegistryValue(HKEY key, const std::string& sSubKey, const std::string& sName, const std::string& stMachineName        )
{
                                                                                                     
	long err = -1;
	HKEY hNetKey;
	if ( OpenRemoteRegistry(stMachineName, key, hNetKey) )
		return err;
	::SetLastError(0);

	HKEY hKey;
	bool bSuccess = false;
	if ( (err=::RegOpenKeyEx(hNetKey, sSubKey.c_str(), 0, KEY_SET_VALUE , &hKey)) == ERROR_SUCCESS )
	{
		if ( (err=::RegDeleteValue(hKey, sName.c_str())) == ERROR_SUCCESS )
			bSuccess = true;
		::RegCloseKey(hKey);
	}
	if ( !stMachineName.empty() )
		::RegCloseKey(hNetKey);
	
	if (bSuccess)
		::SetLastError(0);
	return err;
}

#ifdef WIN32
int DeleteRegistryKey(HKEY key, const std::string& sSubKey, const std::string& stMachineName        )
{
	long err = -1;
	HKEY hNetKey;
	if ( OpenRemoteRegistry(stMachineName, key, hNetKey) )
		return err;
	::SetLastError(0);

	  
	                                                                               
	  
	bool bSuccess = (err=::SHDeleteKey(stMachineName.empty()?key:hNetKey, sSubKey.c_str())) == ERROR_SUCCESS;

	if ( !stMachineName.empty() )
		::RegCloseKey(hNetKey);

	if (bSuccess)
		::SetLastError(0);
	return err;
}
#endif

int GetRegistryInt(HKEY key, const std::string& sSubKey, const std::string& sName, int defaultValue, const std::string& stMachineName        )
{
	int iRes = defaultValue;
	HKEY hNetKey;
	if ( OpenRemoteRegistry(stMachineName, key, hNetKey) )
		return iRes;
	::SetLastError(0);

	HKEY hKey;
	bool bSuccess = false;
	if(::RegOpenKeyEx(hNetKey, sSubKey.c_str(), 0, KEY_QUERY_VALUE, &hKey) == ERROR_SUCCESS )
	{
		DWORD regType = REG_DWORD, len = sizeof(DWORD), val;
		if( ::RegQueryValueEx(hKey, sName.c_str(), 0, &regType, (BYTE*)&val, &len) == ERROR_SUCCESS )
		{
			bSuccess = true;
			iRes = val;
		}
		::RegCloseKey(hKey);
	}
	if ( !stMachineName.empty() )
		::RegCloseKey(hNetKey);
	
	if (bSuccess)
		::SetLastError(0);
	return iRes;
}

long SetRegistryInt(HKEY key, const std::string& sSubKey, const std::string& sName, int value, const std::string& stMachineName        )
{
	long err=-1;
	HKEY hNetKey;
	if ( OpenRemoteRegistry(stMachineName, key, hNetKey) )
		return err;

	HKEY hKey;
	bool bSuccess = false;
	if( (err=::RegCreateKeyEx(hNetKey, sSubKey.c_str(), 0, "", REG_OPTION_NON_VOLATILE, KEY_SET_VALUE, 0, &hKey, 0)) == ERROR_SUCCESS )
	{
		DWORD regType = REG_DWORD, len = sizeof(DWORD);
		if ( (err = ::RegSetValueEx(hKey, sName.c_str(), 0, regType, (BYTE*)&value, len)) == ERROR_SUCCESS )
			bSuccess = true;
		::RegCloseKey( hKey );
	}

	if ( !stMachineName.empty() )
		::RegCloseKey(hNetKey);

	if ( bSuccess )
		::SetLastError(0);
	return err;
}

BYTE* GetRegistryBinary(HKEY key, const std::string& sSubKey, const std::string& sName, int& iSizeOfData, const std::string& stMachineName        )
{
                                                                                                                                         
                                                                                             
	BYTE* pRes = 0;
	iSizeOfData = 0;
	HKEY hNetKey;
	if ( OpenRemoteRegistry(stMachineName, key, hNetKey) )
		return pRes;
	::SetLastError(0);

	HKEY hKey;
	bool bSuccess = false;
	if(::RegOpenKeyEx(hNetKey, sSubKey.c_str(), 0, KEY_QUERY_VALUE, &hKey) == ERROR_SUCCESS )
	{
		DWORD regType = REG_BINARY, len = 0;
		if( ::RegQueryValueEx(hKey, sName.c_str(), 0, &regType, pRes, &len) == ERROR_SUCCESS )                                     
		{
			BYTE* pBinary = new BYTE[len];
			if( ::RegQueryValueEx(hKey, sName.c_str(), 0, &regType, pBinary, &len) == ERROR_SUCCESS )
			{
				bSuccess = true;
				pRes = pBinary;
				iSizeOfData = len;
			}
			else 
				delete[] pBinary;
		}
		::RegCloseKey(hKey);
	}

	if ( !stMachineName.empty() )
		::RegCloseKey(hNetKey);
	
	if ( bSuccess )
		::SetLastError(0);
	return pRes;
}

long SetRegistryBinary(HKEY key, const std::string& sSubKey, const std::string& sName, const BYTE* value, DWORD len, const std::string& stMachineName        )
{
	long err=-1;
	HKEY hNetKey;
	if ( OpenRemoteRegistry(stMachineName, key, hNetKey) )
		return err;
	::SetLastError(0);

	HKEY hKey;
	bool bSuccess = false;
	if( (err=::RegCreateKeyEx(hNetKey, sSubKey.c_str(), 0, "", REG_OPTION_NON_VOLATILE, KEY_SET_VALUE, 0, &hKey, 0)) == ERROR_SUCCESS )
	{
		DWORD regType = REG_BINARY;
		if ( (err=::RegSetValueEx(hKey, sName.c_str(), 0, regType, value, len)) == ERROR_SUCCESS )
			bSuccess = true;
		::RegCloseKey( hKey );
	}

	if ( !stMachineName.empty() )
		::RegCloseKey( hNetKey );

	if ( bSuccess )
		::SetLastError(0);
	return err;
}

namespace {
	string GetRegistryStringImpl(HKEY hNetKey, const REGSAM& samDesired, const std::string& sSubKey, const std::string& sName, bool& bValueExists) {
		bValueExists = false;
		HKEY hKey;
		if( ::RegOpenKeyEx(hNetKey, sSubKey.c_str(), 0, samDesired, &hKey) != ERROR_SUCCESS ) {
			return string("");
		}

		string stRes;
		TCHAR buf[2048];
		DWORD regType = REG_SZ, len = 2048;
		if( ::RegQueryValueEx(hKey, sName.c_str(), 0, &regType, (BYTE*)&buf, &len) == ERROR_SUCCESS ) {
			bValueExists = true;
			stRes = buf;
			::SetLastError(0);
		}
		else {
			bValueExists = false;
		}
		::RegCloseKey( hKey );
		return stRes;
	}
}

std::string GetRegistryString(HKEY key, const std::string& sSubKey, const std::string& sName, const std::string& defaultValue, const std::string& stMachineName        , bool bWOW64)
{
	HKEY hNetKey;
	if ( OpenRemoteRegistry(stMachineName, key, hNetKey) )
		return string("");
	::SetLastError(0);

	bool bValueExists = false;
	REGSAM samDesired = bWOW64 ? KEY_QUERY_VALUE | KEY_WOW64_64KEY : KEY_QUERY_VALUE;
	string stRes = GetRegistryStringImpl(hNetKey, samDesired, sSubKey, sName, bValueExists);
	if ( bValueExists ) return stRes;

	if ( bWOW64 ) {
		REGSAM samDesired = KEY_QUERY_VALUE;
		stRes = GetRegistryStringImpl(hNetKey, samDesired, sSubKey, sName, bValueExists);
		if ( bValueExists ) return stRes;
	}

	return defaultValue;
}

std::string GetRegistryExpandString(HKEY key, const std::string& sSubKey, const std::string& sName, const std::string& defaultValue, const std::string& stMachineName         )
{
	std::string stRes = GetRegistryString(key, sSubKey.c_str(), sName.c_str(), defaultValue, stMachineName);
	size_t sizeFirst = stRes.find('%');
	if ( sizeFirst != std::string::npos )
	{
		size_t sizeSecond = stRes.find('%', sizeFirst + 1);
		TCHAR buf[2048];
		if ( ::ExpandEnvironmentStrings(stRes.substr(sizeFirst, sizeSecond - sizeFirst + 1).c_str(), buf, 2048) )
		{
			stRes.replace(sizeFirst, sizeSecond + 1, buf);
		}
	}
	return stRes;
}

long SetRegistryString(HKEY key, const std::string& sSubKey, const std::string& sName, const std::string& sValue, const std::string& stMachineName        )
{
	long err=-1;
	HKEY hNetKey;
	if ( OpenRemoteRegistry(stMachineName, key, hNetKey) )
		return err;
	::SetLastError(0);

	HKEY hKey;
	bool bSuccess = false;
	if( (err=::RegCreateKeyEx(hNetKey, sSubKey.c_str(), 0, "", REG_OPTION_NON_VOLATILE, KEY_SET_VALUE, 0, &hKey, 0)) == ERROR_SUCCESS )
	{
		                                                                             
		                                  
		DWORD regType = REG_SZ;
		size_t len = (sValue.length()+1)*sizeof(TCHAR);
		if ( (err=::RegSetValueEx(hKey, sName.c_str(), 0, regType, (BYTE*)sValue.c_str(), (DWORD)len)) == ERROR_SUCCESS )
			bSuccess = true;
		::RegCloseKey( hKey );
	}

	if ( !stMachineName.empty() )
		::RegCloseKey(hNetKey);

	if ( bSuccess )
		::SetLastError(0);
	return err;
}

std::vector< std::string > GetRegistryMultiStringAsVector(HKEY key, const std::string& sSubKey, const std::string& sName)
{
	std::vector< std::string > vstValue;
	char* value = GetRegistryMultiString(key, sSubKey.c_str(), sName.c_str());
	AutoArrayDelete<char> adopt_value(value);

	size_t len = value ? strlen(value) : 0;
	while( true && len )
	{
		vstValue.push_back(value);
		size_t len = strlen(value);
		if( value[len+1] )
			value += len + 1;
		else
			break;
	}
	
	return vstValue;
}

  
                                                                              
  
char* GetRegistryMultiString(HKEY key, const std::string& sSubKey, const std::string& sName, const std::string& defaultValue      )
{
	char* res = !defaultValue.empty() ? strnewdup(defaultValue.c_str()) : 0;
	HKEY hKey;
	if(::RegOpenKeyEx(key, sSubKey.c_str(), 0, KEY_QUERY_VALUE, &hKey) == ERROR_SUCCESS )
	{
		TCHAR buf[2048];
		DWORD regType = REG_MULTI_SZ, len = 2048;
		if( ::RegQueryValueEx(hKey, sName.c_str(), 0, &regType, (BYTE*)&buf, &len) == ERROR_SUCCESS )
			res = len>2?strnewdup(buf):0;
		::RegCloseKey( hKey );
	}
	return res;
}

long SetRegistryMultiString(HKEY key, const std::string& sSubKey, const std::string& sName, const std::vector< std::string > vstValue)
{
	size_t len = 0;
	int iNumStrings = 0;
	std::vector< std::string >::const_iterator stIt = vstValue.begin();
	for(;stIt != vstValue.end(); ++stIt)
	{
		len += stIt->length();
		++iNumStrings;
	}

	char* value = new char[ len + iNumStrings + 2 ];                                                         
	AutoArrayDelete<char> adopt_value(value);

	size_t iPos=0;
	for(stIt = vstValue.begin(); stIt != vstValue.end(); ++stIt)
	{
		memcpy(&value[iPos], stIt->c_str(), stIt->length());
		iPos += stIt->length();
		value[iPos++] = 0;
	}
	value[iPos++] = 0;                                                    
	if( iPos == 1 )
		value[iPos++] = 0;

	return SetRegistryMultiString(key, sSubKey.c_str(), sName.c_str(), value, (int)iPos);
}

long SetRegistryMultiString(HKEY key, const std::string& sSubKey, const std::string& sName, const std::string& sValue, int size)
{
	long err=0;
	HKEY hKey;
	if((err=::RegOpenKeyEx(key, sSubKey.c_str(), 0, KEY_SET_VALUE, &hKey)) == ERROR_SUCCESS )
	{												   
		DWORD regType = REG_MULTI_SZ, len = size;
		err = ::RegSetValueEx(hKey, sName.c_str(), 0, regType, (BYTE*)sValue.c_str(), len);
		::RegCloseKey( hKey );
	}
	else if((err=::RegCreateKey(key, sSubKey.c_str(), &hKey)) == ERROR_SUCCESS )
	{
		if((err=::RegOpenKeyEx(key, sSubKey.c_str(), 0, KEY_SET_VALUE, &hKey)) == ERROR_SUCCESS )
		{
			DWORD regType = REG_MULTI_SZ, len = size;
			err = ::RegSetValueEx(hKey, sName.c_str(), 0, regType, (BYTE*)sValue.c_str(), len);
			::RegCloseKey( hKey );
		}
	}
	return err;
}

namespace {
	void EnumRegistryKeysImpl(HKEY rootKey, const std::string& sSubKey, std::vector< std::string >& vstKeyNames, REGSAM samDesired) {
		HKEY hKey;
		long err=::RegOpenKeyEx(rootKey, sSubKey.c_str(), 0, samDesired, &hKey);
		if( err != ERROR_SUCCESS ) {
			throw CrException("Failed to open registry key " + sSubKey + "'. Reason: " + ErrorResolver().Resolve(err), err);
		}
		char sName[MAX_PATH];
		unsigned long lNameSize = MAX_PATH;
		for(int lIdx=0; (err=::RegEnumKeyEx(hKey, lIdx, sName, &lNameSize, 0, 0, 0, 0)) == ERROR_SUCCESS; ++lIdx)
		{
			vstKeyNames.push_back(sName);
			lNameSize = MAX_PATH;
		}
		::RegCloseKey( hKey );
		if( err && err != ERROR_NO_MORE_ITEMS && err != ERROR_FILE_NOT_FOUND )
			throw CrException("Failed to read registry keys from 'HKEY_LOCAL_MACHINE\\" +
				sSubKey + "'. Reason: " + ErrorResolver().Resolve(err), err);
	}

	class EnumRegistryKeysImpl2 {
	public:
		EnumRegistryKeysImpl2(HKEY rootKey, const std::string& sSubKey, std::vector< std::string >& vstKeyNames, REGSAM samDesired)
			: m_rootKey(rootKey), m_sSubKey(sSubKey), m_vstKeyNames(vstKeyNames), m_samDesired(samDesired) {}

		void operator()() {
			try {
				EnumRegistryKeysImpl(m_rootKey, m_sSubKey, m_vstKeyNames, m_samDesired);
			}
			catch ( const CrException& e ) {
				m_error = e.GetErrorString();
			}
		}

		HKEY m_rootKey;
		const std::string& m_sSubKey;
		std::vector< std::string >& m_vstKeyNames;
		REGSAM m_samDesired;

		string m_error;
	};
}


std::vector< std::string > EnumRegistryKeys(HKEY key, const std::string& sSubKey, bool bWOW64              ) {
	std::vector< std::string > vstKeyNames;
	if ( !bWOW64 ) {
		EnumRegistryKeysImpl(key, sSubKey, vstKeyNames, KEY_READ);
	}
	else {
		                                                                                  
		                                                               
		EnumRegistryKeysImpl2 impl(key, sSubKey, vstKeyNames, KEY_WOW64_64KEY | KEY_READ);
		konga::RunInNewThread(impl);
                                                                                
		if ( !vstKeyNames.empty() ) return vstKeyNames;

		impl.m_samDesired = KEY_WOW64_32KEY | KEY_READ;
		konga::RunInNewThread(impl);
                                                                                
	}

	return vstKeyNames;
}

std::vector< std::pair<std::string, std::string> > EnumRegistryValues(HKEY key, const std::string& sSubKey)
{
	std::vector< std::pair<std::string, std::string> > vstKeyNamesAndValues;
	long err=0;
	HKEY hKey;
	if((err=::RegOpenKeyEx(key, sSubKey.c_str(), 0, KEY_READ, &hKey)) == ERROR_SUCCESS )
	{
		char sName[4096], sValue[4096];
		unsigned long lNameSize = 4096, lValueSize = 4096;
		unsigned long lType;
		for(int lIdx=0; (err=::RegEnumValue(hKey, lIdx, sName, &lNameSize, 0, &lType, (BYTE*)sValue, &lValueSize)) == ERROR_SUCCESS; ++lIdx)
		{
			switch(lType) {
			case REG_SZ:
				vstKeyNamesAndValues.push_back( std::pair<std::string, std::string>(sName,sValue) );
				break;
			case REG_DWORD:
				{
					char buf[64];
					vstKeyNamesAndValues.push_back( std::pair<std::string, std::string>(sName, konga::itoa(*(DWORD*)sValue,buf,10)) );
					break;
				}
			default:
				;
			}
			lNameSize = 4096, lValueSize = 4096;
		}
		::RegCloseKey( hKey );
		if( err && err != ERROR_NO_MORE_ITEMS && err != ERROR_FILE_NOT_FOUND )
			throw err;
	}
	else
		throw err;
	return vstKeyNamesAndValues;
}

}                   

