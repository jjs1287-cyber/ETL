/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/util.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/Settings.h>
#include <konga/Utils/DateUtils.h>
#include <konga/Utils/KString.h>

#include "WDIPathFinder.h"
#include "WDILogFileUtils.h"

using std::string;

namespace konga {

bool WDILogFileUtils::GetWDITempFileName(string& stDatFile, const string& tag)
{
    try {    
    	string stWDIPath;
    	WDIPathFinder::GetWDIPath(stWDIPath);
    	const string stTempFilePath(stWDIPath + "DataInterchange/Temp");
		stDatFile = konga::getTmpFileName( tag.empty() ? "Temp" : tag, stTempFilePath);
        return true;
    }
    catch(const CrException&) {
        return false;
    }
}

bool WDILogFileUtils::GetWDITempFileName(KString& sDatFile, const string& tag) {
	string tmp;
	const bool retVal = WDILogFileUtils::GetWDITempFileName(tmp, tag);
	sDatFile = tmp.c_str();
	return retVal;
}


                                  
                                                                    
                       
bool WDILogFileUtils::GenerateLogFile()
{
	return konga::GetOptionAsBoolean("TransformationEngine", "WriteLog", false);
}

string WDILogFileUtils::GenerateErrLogFileName(int id, const string& guid)
{
	string stLogDir(getWDILogDir());

	return string(stLogDir + "/" + konga::toString(id) + "_" + guid + ".txt");
}

bool WDILogFileUtils::CreateWDITempDirectory(int iOperationId,        string& stTempDirectory,        bool& bRemoveTempDir)
{
	KString WDIPath;
	if( WDIPathFinder::GetWDIPath(WDIPath) == false )
	{
		return false;
	}

	bRemoveTempDir = !WDILogFileUtils::GenerateLogFile();

	string stTempDir0 = string((const char*)WDIPath) + "DataInterchange/Temp/";
	string stPrefix = "op_" + konga::toString(iOperationId) + "_";
	string stGuid;
	konga::GetGUIDString(stGuid);
	stTempDirectory = stTempDir0 + stPrefix + stGuid;
	if( !konga::IsDirectory(stTempDirectory) )
	{
        try {
            konga::kCreateDirectory(stTempDirectory);
        } catch( const CrException&){
                                                                             
            stTempDirectory = WDIPath + "DataInterchange/Temp/";
            bRemoveTempDir = false;
        }
	}
	return true;
}

FILE* WDILogFileUtils::GetDebugFileHandle(const string& stDebugFilePrefix) {
	if( WDILogFileUtils::GenerateLogFile() ) {
		string WDIPath;
		if ( WDIPathFinder::GetWDIDebugPath(WDIPath) ) {
			const string sTempFilePath( WDIPath + stDebugFilePrefix + "_" + GetGUIDString() + ".log" );
			return fopen(sTempFilePath.c_str(),"w+");
		}
	}
	return 0;
}

string WDILogFileUtils::getWDILogDir(){
	KString WDIPath;

	if (WDIPathFinder::GetWDIPath(WDIPath) == false)
		WDIPath =  konga::getTmpFileName().c_str();
	else
		WDIPath += "DataInterchange/Temp";

    string stLogDir= string((const char*)WDIPath)+"/LOG";

	                          
	if(!konga::FileExists(stLogDir))
	{
		try {
			konga::kCreateDirectory(stLogDir);
		}
		catch ( const CrException& ) {
			                                                  
            stLogDir = (const char*)WDIPath;
		}
     }
	return stLogDir;
}

}                   
