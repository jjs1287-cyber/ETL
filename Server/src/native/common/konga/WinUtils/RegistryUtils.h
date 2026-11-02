/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef REGISTRY_UTILS_H
#define REGISTRY_UTILS_H

#include <string>
#include <vector>

#include <windows.h>

namespace konga { 

int OpenRemoteRegistry(const std::string& stMachineName, const HKEY& Key, HKEY& hNetKey);
int DeleteRegistryValue(HKEY key, const std::string& subKey, const std::string& name, const std::string& stMachineName = "");
int DeleteRegistryKey(HKEY key, LPCSTR subKey, const std::string& stMachineName = "");
long SetRegistryInt(HKEY key, const std::string& subKey, const std::string& name, int value, const std::string& stMachineName = "");
long SetRegistryBinary(HKEY key, const std::string& subKey, const std::string& name, const BYTE* value, DWORD len, const std::string& stMachineName = "");
long SetRegistryString(HKEY key, const std::string& subKey, const std::string& name, const std::string& value, const std::string& stMachineName = "");
long SetRegistryMultiString(HKEY key, const std::string& subKey, const std::string& name, const std::string& value, int size);
long SetRegistryMultiString(HKEY key, const std::string& subKey, const std::string& name, const std::vector< std::string > vstValue);

int GetRegistryInt(HKEY key, const std::string& subKey, const std::string& name, int defaultValue, const std::string& stMachineName = "");
BYTE* GetRegistryBinary(HKEY key, const std::string& subKey, const std::string& name, int& iSizeOfData, const std::string& stMachineName = "");

                                                        
std::string GetRegistryString(HKEY key, const std::string& subKey, const std::string& name, const std::string& defaultValue, const std::string& stMachineName = "", bool bWOW64 = false);

std::string GetRegistryExpandString(HKEY key, const std::string& subKey, const std::string& name, const std::string& defaultValue, const std::string& stMachineName = "");
std::string GetRemoteRegistryString(const std::string& stMachineName, HKEY key, const std::string& subKey, const std::string& name, const std::string& defaultValue);
char* GetRegistryMultiString(HKEY key, const std::string& subKey, const std::string& name, const std::string& defaultValue=0);
std::vector< std::string > GetRegistryMultiStringAsVector(HKEY key, const std::string& subKey, const std::string& name);

                                                                                         
                                                                             
std::vector< std::string > EnumRegistryKeys(HKEY key, const std::string& subKey, bool bWOW64 = false);
std::vector< std::pair<std::string, std::string> > EnumRegistryValues(HKEY key, const std::string& subKey);

}                   

#endif                    

