/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <cstdio>	                  

#include <algorithm>       
#include <string>
#include <memory>	                

#include <boost/thread/mutex.hpp>

#include "FileUtils.h"	                        
#include "util.h"		                                
#include "CrException.h"
#include "ErrorResolver.h"

#include "PortDefs.h"	                            
#include <GetPot>		                                                                

#include "Settings.h"

using std::string;

namespace {

	                                                                                                               
	                                         
	std::auto_ptr<GetPot>& GetFileOptions() {
		static std::auto_ptr<GetPot> FileOptions(0);
		return FileOptions;
	}

	                                                          
	                                  
	const GetPot* GetValidFileOptions() {
		GetPot* p = GetFileOptions().get();
		if (p != 0 ) return p;

		const string& stKongaConfigFile = konga::GetConfFileLocation();
		p = new GetPot(stKongaConfigFile.c_str());
		p->disable_request_recording();
		GetFileOptions().reset( p );
		return p;
	}

	std::auto_ptr<GetPot>& GetCommandLineOptions() {
		static std::auto_ptr<GetPot> CmdLineOptions(0);
		return CmdLineOptions;
	}

	boost::mutex& GetOptionMutex() {
		static boost::mutex mu;
		return mu;
	}

	template<typename T>
	T GetOption(const string& stSection, const string& stName, const T DefaultVal) {
		boost::mutex::scoped_lock lock(GetOptionMutex());

		const string& stOption = stSection.empty() ? stName : (stSection + '/' + stName);

		                                    
		GetPot* const pCmdlnOptions = GetCommandLineOptions().get();
		if( pCmdlnOptions ) {
			T opt = (*pCmdlnOptions)(stOption.c_str(), DefaultVal);
			if ( opt != DefaultVal )
				return opt;
			string stOptionCmdLine(stOption);
			konga::ReplaceCharWithChar(stOptionCmdLine, '/', '-');
			stOptionCmdLine = "--" + stOptionCmdLine;
			if ( pCmdlnOptions->search(stOptionCmdLine.c_str()) ) {
				return pCmdlnOptions->next(DefaultVal);
			}
		}

		const GetPot* const pFileOptions = GetValidFileOptions();
		if( pFileOptions != 0 ){
			return (*pFileOptions)(stOption.c_str(), DefaultVal);
		}

		return DefaultVal;
	}

}

namespace {
	const string SERVER_HOME_ENV = "ETLFORGE_HOME";
}

namespace konga {

string GetServerHome() {
	const string kongaHome(konga::getEnvironmentVariableValue(SERVER_HOME_ENV));
	if ( kongaHome.empty() ) {
		#ifdef WIN32
			char buf[1024];
			buf[0]=0;
			DWORD lRet = GetEnvironmentVariable(SERVER_HOME_ENV.c_str(), buf, sizeof(buf));
			if( buf[0] )
				return buf;

			                                              
			char path[MAX_PATH];
			path[0] = 0;
			::GetModuleFileName(0, path, MAX_PATH);
			const string sFileName(path);
			size_t lPos1 = sFileName.find_last_of("/");
			size_t lPos2 = sFileName.find_last_of("\\");
			size_t lPos = std::min(lPos1, lPos2);
			if( lPos != string::npos && lPos > 0) {
				const string& sPath = sFileName.substr(0, lPos - 4);                    
				return sPath;
			}
		#endif         
		throw CrException("The environment variable " + SERVER_HOME_ENV + " must be set.");
	}
	return kongaHome;
}

string GetServerHomeWithSlash() {
	string sKongaHome( GetServerHome() );
	AppendPathSeparator(sKongaHome);
	return sKongaHome;
}

string GetConfFileLocation() {
	const string& stKongaConfigFile = (GetServerHomeWithSlash() + "etlforge.conf");
	if ( !FileExists(stKongaConfigFile) )
		throw CrException("The default configuration file \"" + stKongaConfigFile + "\" does not exist." \
		" Is the environment variable " + SERVER_HOME_ENV + " set to the correct directory?");

	return stKongaConfigFile;
}

string GetLogDirectoryWithSlash(string& sLogError, bool bCreateDirectory) {
	string sLogDir;

	                                                           
	try {
		sLogDir = GetOptionAsString("Settings", "LogDir", "");
		if ( !sLogDir.empty() && !IsDirectory(sLogDir) && bCreateDirectory ) {
			kCreateDirectory(sLogDir);
		}

		                                           
		if ( bCreateDirectory && !sLogDir.empty() ) {
			string tmp(sLogDir);
			AppendPathSeparator(tmp);
			tmp += GetGUIDString();
			FILE* f = fopen(tmp.c_str(), "wb");
			if ( f == 0 ) {
				long lErr = GetLastError();
				sLogError = "The user defined temporary directory '"
					+ sLogDir + " is not writable for the current process.\n"
					+ ErrorResolver().Resolve(lErr);
				sLogDir.clear();
			}
			else {
				fclose(f); f = 0;
				kDeleteFile(tmp);
			}
		}
	}
	catch( const CrException& e ) {
		sLogError += "\n\nFailed to use the user defined log directory '"
			+ sLogDir + "'. Reason: " + e.GetErrorString()
			+ "\nWill use the default directory.";
		sLogDir.clear();
	}

	try {
		if ( sLogDir.empty() ) {
			sLogDir = GetServerHomeWithSlash() + "log";
		}
	} catch(CrException& e) {
		sLogError += "\n\nCould not find the Konga home directory. Reason: "
			+ e.GetErrorString() + "\nWill log to current directory.";
		sLogDir.clear();
	}

	if ( !sLogDir.empty() )
		AppendPathSeparator(sLogDir);
	return sLogDir;
}

void SetOptionsFromFile(const std::string& stFileName) {
	boost::mutex::scoped_lock lock(GetOptionMutex());
	GetFileOptions().reset( new GetPot(stFileName.c_str()) );
}

void ResetOptions() {
	boost::mutex::scoped_lock lock(GetOptionMutex());
	GetFileOptions().reset(0);
}

void SetCommandLineOptions(int argc, char* argv[]) {
	boost::mutex::scoped_lock lock(GetOptionMutex());
	GetCommandLineOptions().reset(new GetPot(argc, argv));
	GetPot* const pCmdlnOptions = GetCommandLineOptions().get();

	                                                                             
	string stOptionalConfFile;
	if( pCmdlnOptions->search("-f") || pCmdlnOptions->search("--configuration-file") ) {
		stOptionalConfFile = pCmdlnOptions->next("");
	}
	if ( stOptionalConfFile.empty() ) {
		stOptionalConfFile = (*pCmdlnOptions)("configuration-file","");
	}
	if ( !stOptionalConfFile.empty() ) {
		if ( !FileExists(stOptionalConfFile) ) {
			throw CrException("The specified configuration file '" + stOptionalConfFile + "' does not exist.");
		}
		GetFileOptions().reset( new GetPot(stOptionalConfFile.c_str())  );
	}
}

bool IsOptionDefined(const std::string& stSection, const std::string& stName) {
	boost::mutex::scoped_lock lock(GetOptionMutex());
	const GetPot* const pFileOptions = GetValidFileOptions();
	if ( pFileOptions == 0 )
		return false;

	const string& stOption = (stSection + '/' + stName);
	const char empty[] = "";
	const char* pVal = (*pFileOptions)(stOption.c_str(), empty);
	return (pVal != empty);
}

int GetOptionAsInt(const string& stSection, const string& stName, int iDefaultVal           ) {
	return GetOption<int>(stSection, stName, iDefaultVal);
}

string GetOptionAsString(const string& stSection, const string& stName, const string& stDefaultVal            ) {
	return GetOption<const char*>(stSection, stName, stDefaultVal.c_str());
}

double GetOptionAsDouble(const std::string& stSection, const std::string& stName, double DefaultValue) {
	const string stStringRep = GetOptionAsString(stSection, stName, toString(DefaultValue));
	char* pChar = 0;
	double d = strtod(stStringRep.c_str(), &pChar);
	if ( d == 0 && stStringRep.c_str() == pChar ) {
		                                                                                           
		return DefaultValue;
	}
	return d;
}

bool GetOptionAsBoolean(const std::string& stSection, const std::string& stName, bool bDefaultValue) {
	const string& stStringRep = GetOptionAsString(stSection, stName, "");
	if ( stStringRep.empty() )
		return bDefaultValue;

	return ( konga::EqualsIgnoreCase(stStringRep, "true") || stStringRep == "1" );
}

void SetOptionAsInt(const string& stSection, const string& stName, int iVal) {
}

void SetOptionAsString(const string& stSection, const string& stName, const string& stVal) {
}

void LoadSectionNameValuePairs(const std::string& sectionName0, std::map<std::string, std::string>&nameValues){
	nameValues.clear();

	boost::mutex::scoped_lock lock(GetOptionMutex());
	const GetPot* const pFileOptions = GetValidFileOptions();
	if ( pFileOptions == 0 ) return;

	const STRING_VECTOR& vs=pFileOptions->get_variable_names();
	const string sectionName(sectionName0+"/");
	const string::size_type n=sectionName.size();
	for(STRING_VECTOR::const_iterator it=vs.begin(); it!=vs.end(); ++it){
		const string& name=*it;
		const string value( (*pFileOptions)(name.c_str(), "") );
		if(strncmp(name.c_str(), sectionName.c_str(), n)==0)nameValues[name]=value;
	}
}

                             
string defaultEncodingXml() {
	return string( "UTF-8" );
}

}                   
