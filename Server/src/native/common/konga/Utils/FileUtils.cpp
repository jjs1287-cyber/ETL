/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <errno.h>
#include <sys/stat.h>

#ifndef WIN32
#include <sys/types.h>
#include <dirent.h>		                
#include <unistd.h>		        
#endif

#include <stdio.h>

#include <filesystem>
#include <fstream>	           
#include <iostream>

#include <boost/filesystem/operations.hpp>
#include <boost/filesystem/directory.hpp>
#include <boost/filesystem/path.hpp>
#include <boost/filesystem/exception.hpp>


#include <boost/scoped_array.hpp>

#ifdef WIN32
                                           
#include <io.h>
#endif         

#include "util.h"				                  
#include "AutoPtrCleanup.h"		                  
#include "ErrorResolver.h"
#include "CrException.h"
#include "FileUtils.h"
#include "Settings.h"			                      
#include "AutoFileCleanup.h"	                 
#include "StringTokenizer.h"
#include "StringUtils.h"		                                       
#include "FileLineReadWriter.h"	                                
#include "PortDefs.h"			                                          
#include "ProcessUtils.h"		             

using namespace std;
using namespace boost::filesystem;

namespace konga {
	                                                                
	                         
	const char PathSeparator = '/';
}

                     
namespace {

	const string stPATH_SEPARATORS("/\\");	                                                    

	                                                                    
	void FixPathSeparators(string& stPath) {
		if ( konga::PathSeparator == '/' )
			konga::ReplaceCharWithChar(stPath, '\\', konga::PathSeparator);
		else {
			konga::ReplaceCharWithChar(stPath, '/', konga::PathSeparator);
		}
	}

}

namespace konga {

#ifdef WIN32
                                                          

vector<string> getLogicalVolumeDriveStrings()
{
	vector<string> vstRet;
	DWORD dwBufLen = ::GetLogicalDriveStrings(0,0);
	AutoArrayDelete<char> pc( new char[dwBufLen] );
	if( ::GetLogicalDriveStrings(dwBufLen, pc.Get()) )
	{
		char* pcc = pc.Get();                              
		pcc += strlen(pcc)+1;              
		while(*pcc)
		{
			vstRet.push_back(pcc);
			pcc += strlen(pcc)+1;
		}
	}
	return vstRet;
}

vector<DWORD> getLogicalVolumeSerialNumbers()
{
	char lpVolumeNameBuffer[1000];		                     
	DWORD dwVolumeSerialNumber;			                       
	DWORD dwMaximumComponentLength;		                           
	DWORD dwFileSystemFlags;			                      
	char lpFileSystemNameBuffer[1000];	                          

	vector<DWORD> vdwRet;
	vector<string> vstDrives = getLogicalVolumeDriveStrings();
	vector<string>::iterator itDrive = vstDrives.begin();
	for(;itDrive != vstDrives.end(); ++itDrive)
	{
		if( ::GetVolumeInformation(itDrive->c_str(), lpVolumeNameBuffer, 1000,
			&dwVolumeSerialNumber, &dwMaximumComponentLength, &dwFileSystemFlags,
			lpFileSystemNameBuffer, 1000) )
			vdwRet.push_back(dwVolumeSerialNumber);
	}
	return vdwRet;
}

DWORD getLogicalVolumeSerialNumberForFile(const string& stFileName)
{
	char lpVolumeNameBuffer[1000];		                     
	DWORD dwVolumeSerialNumber;			                       
	DWORD dwMaximumComponentLength;		                           
	DWORD dwFileSystemFlags;			                      
	char lpFileSystemNameBuffer[1000];	                          

	size_t pos1 = stFileName.find('\\',2);
	size_t pos2 = stFileName.find('\\',pos1+1);
	if( pos2 == string::npos )
		pos2 = pos1;
	if( ::GetVolumeInformation(stFileName.substr(0,pos2+1).c_str(), lpVolumeNameBuffer, 1000,
		&dwVolumeSerialNumber, &dwMaximumComponentLength, &dwFileSystemFlags, lpFileSystemNameBuffer, 1000) )
		return dwVolumeSerialNumber;
	long lErr = ::GetLastError();
	return 0;
}

#if defined(_MFC_VER)                     
KString GetModuleDirectory(HINSTANCE hinst)
{
	char path[MAX_PATH];
	::GetModuleFileName( hinst, path, MAX_PATH);
	KString cpath = path;
	int pos1=cpath.ReverseFind('\\'),pos2=cpath.ReverseFind('/');
	int pos=pos1>pos2?pos1:pos2;
	KString fn;
	if( pos!=-1 ) return cpath.Left(pos+1);
	return "";
}
#endif

#endif         


                                                                     

bool FileExists(const string& stPath_in) {
	if ( stPath_in.empty() )
		return false;

	                                                                                           
	string stPath(stPath_in);
	string::iterator last = stPath.end()-1;
	if ( *(last) == '/' || *(last) == '\\' )
		stPath.erase(last);

	if ( stPath.empty() ) {
		                                                                                    
		                                                  
		return true;
	}

    std::error_code error;
    bool exists=std::filesystem::exists(std::filesystem::u8path(stPath),error);
    if(error)throw CrException("Failed to determine whether the UTF-8 file path exists: "+error.message());
    return exists;
}

bool NonEmptyFileExists(const string& stPath) {
	if ( FileExists(stPath ) ){
		long lErr;
		return ( GetFileSize(stPath.c_str(), lErr) > 0 );
	}
	return false;
}

long long GetFileSize(const string& stPath, long& lErr) {
    std::error_code error;
    auto size=std::filesystem::file_size(std::filesystem::u8path(stPath),error);
    lErr=error.value();return error?-1:static_cast<long long>(size);
}

bool IsDirectory(const string& stPath) {
	string stTempPath;
	if( EndsWithPathSeparator(stPath) )
		stTempPath = stPath.substr(0, stPath.length()-1);
	else
		stTempPath = stPath;
    std::error_code error;
    return std::filesystem::is_directory(std::filesystem::u8path(stTempPath),error);
}

char* read_file(const string& stFilename, long& bytesRead, string& stErr, long maxBytesRead         )
{
	FILE*in=fopen(stFilename.c_str(),"rb");
	if(in){
		AutoFileHandle adopt_in(in);
		fseek(in,0,SEEK_END);
		long file_length=ftell(in);
		if(maxBytesRead > 0 && file_length > maxBytesRead) file_length = maxBytesRead;

		AutoArrayDelete<char> buff( new char[file_length+3] );
		if(file_length)
		{
			fseek(in,0,SEEK_SET);
			bytesRead=(long)fread(buff.Get(),sizeof(char),file_length,in);
		}
		else
			bytesRead=0;
		buff[file_length]='\r';
		buff[file_length+1]='\n';
		buff[file_length+2]=0;

		return buff.Release();
	} else {
		bytesRead = 0;
		long lErr = ::GetLastError();
		stErr = "Failed to open \"" + stFilename + "\" for reading. ";
		stErr += ErrorResolver().Resolve(lErr);
		return 0;
	}
}

void read_file_to_string(const string& stFileName, string& stFileContents, bool bBinaryMode) {
	if ( !FileExists(stFileName) )
		throw CrException("The file \"" + stFileName + "\" does not exist.");

	ifstream file;
	file.exceptions ( ifstream::badbit );
	try {
		const ifstream::openmode mode( bBinaryMode ? ifstream::in|ifstream::binary : ifstream::in );
		file.open(stFileName.c_str(), mode);
		stringstream s;
		char c = 0;
		while(1) {
			file.get(c);
			if(!file.good())break;
			s << c;
		}
		file.close();
		stFileContents = s.str();
	}
	catch( const ifstream::failure& e ) {
		throw CrException("Failed to read from file \"" + stFileName + "\". " + e.what());
	}
}

void write_string_to_file(const std::string& stFileName, const std::string& stFileContents, bool bBinaryMode) {
	ofstream f;
	f.exceptions(ifstream::eofbit | ifstream::failbit | ifstream::badbit);
	try {
		ios_base::openmode mode = ofstream::out | ofstream::trunc;
		if ( bBinaryMode ) mode |= ofstream::binary;
		f.open(stFileName.c_str(), mode);
		f << stFileContents;
		f.close();
	}
	catch ( const ifstream::failure& e) {
		throw CrException("Failed to write to file \"" + stFileName + "\". " + e.what());
	}
}

void write_to_binary_file(const string& stFileName, const unsigned char* const pchContents, const unsigned long length) {
	FILE* f = fopen(stFileName.c_str(), "wb");
	if ( f == 0 ) {
		long lErr = GetLastError();
		string stErr("Failed to open file '" + stFileName + "' for writing. Reason:\n"
			+ ErrorResolver(lErr).Resolve(lErr));
		throw CrException(stErr);
	}
	AutoFileHandle adopt_f(f);

	const size_t chunk_size = sizeof(unsigned char);
	for( unsigned long i = 0; i < length; ++i ) {
		if( fwrite(pchContents+i, chunk_size, 1, f) != 1 ) {
			long lErr = GetLastError();
			string stErr("Failed to write to file '" + stFileName + "'. Reason:\n"
				+ ErrorResolver(lErr).Resolve(lErr));
			throw CrException(stErr);
		}
	}
}

namespace {
	                                                                 
	                                                                 
	string getLongPathName(const string& stPath, unsigned long bufLen = MAX_PATH) {
		                                         
#ifdef WIN32
		boost::scoped_array<char> bufLongName(new char[bufLen]);
		DWORD bufSizeLongName = ::GetLongPathName(stPath.c_str(), bufLongName.get(), bufLen);
		if ( bufSizeLongName == 0 ) {
			string stErr = "Operating System Error: Failed to convert short file path \"" + stPath
				+ "\" to a long path name. Reason: ";
			long lErr = ::GetLastError();
			stErr += ErrorResolver().Resolve(lErr);
			throw CrException(stErr, lErr);
		}
		else if ( bufSizeLongName > bufLen ) {
			return getLongPathName(stPath, bufSizeLongName + 1);
		}
		return string(bufLongName.get());
#else
	return stPath;
#endif         
	}
}

#ifdef CR_HAS_GLOBAL_TMP_DIR
extern string g_temp_dir;
#endif                         

string getTmpFileName(const string& prefix, const string& stTempDir       )
{

	                                     
	  
	string stTmpFileName;
	if ( stTempDir.empty() || !IsDirectory(stTempDir) )
		stTmpFileName = getTmpDir();
	else
		stTmpFileName = stTempDir;
	AppendPathSeparator(stTmpFileName);
	stTmpFileName += prefix;
	while ( true ) {
		const string tmp( stTmpFileName + GetGUIDString() );
		if ( !FileExists(tmp) ) {
			stTmpFileName = tmp;
			break;
		}
	}
	FixPathSeparators(stTmpFileName);
	return stTmpFileName;

                
#ifdef CR_HAS_GLOBAL_TMP_DIR
	if( stTempDir.empty() )
		stTempDir = g_temp_dir;
#endif                         
}

long CountLines(const std::string& fileName) {
	if ( !FileExists(fileName) )
		return -1;

	                                                          
	FILE* f = fopen(fileName.c_str(), "rb");
	if ( f == 0 ) {
		long lErr = GetLastError();
		throw CrException("Failed to open file '" + fileName
			+ "' for reading. Reason: "
			+ ErrorResolver(lErr).Resolve(lErr));
	}
	AutoFileHandle adopt_f(f);
	rewind(f);

	const int MAX_LINE_LENGTH = 10000;
	char line[MAX_LINE_LENGTH];

	long n = 0;
	while( !feof( f ) ) {
		if( !fgets(line, MAX_LINE_LENGTH, f) )
			break;
		++n;
	}
	const int iErr = ferror(f);
	if ( iErr ) {
		throw CrException("I/O error occurred while reading the file '" + string(fileName)
			+ "'. Reason: " + ErrorResolver().Resolve(iErr));
	}

	return n;
}

namespace {

	                                                        
	                                               
	class LineRemover : public FileLineReadWriter {
	public:
		LineRemover(long firstLines, long lastLines, long nLines)
			: m_firstLines(firstLines>0?firstLines:0),
			m_lastLines(lastLines>0?lastLines:0),
			m_nLines(nLines), m_currentLine(0)
		{
			if ( nLines < 0 )
				throw CrException("Illegal argument: nLines must be 0 or more.");
		}
		~LineRemover(void) {}

		const char* ChangeLine(const char* line) {
			if ( line == 0 )
				return 0;

			++m_currentLine;
			if ( m_currentLine <= m_firstLines )
				return 0;
			else if ( (m_nLines-m_currentLine) < m_lastLines )
				return 0;
			else
				return line;
		}

	private:
		long m_firstLines, m_lastLines, m_nLines;
		long m_currentLine;
	};
}

void RemoveFirstAndLastLines(const std::string& fileName, long removeFirst, long removeLast) {
	if ( removeFirst <= 0 && removeLast <= 0 )
		return;

	const long nLineCount = CountLines(fileName);
	if ( nLineCount <= 0 )
		return;

	LineRemover lr(removeFirst, removeLast, nLineCount);
	lr.Read(fileName.c_str());
}

                                                                       
void RemoveFirstAndLastLines_New(const std::string& fileName, long removeFirst, long removeLast) {
	if ( removeFirst <= 0 && removeLast <= 0 )
		return;

	ifstream ifs(fileName.c_str(), ifstream::in | ifstream::binary);
	const string outFile(fileName + "-" + konga::GetGUIDString());
	ofstream ofs(outFile.c_str(), ofstream::out | ofstream::binary | ofstream::trunc);

	char buf[1025];

	size_t i, len = 0;

	while(!ifs.eof()) {
		ifs.read(buf, 1024);
		len = ifs.gcount();
		if ( len == 0 ) break;
		for(i=0; i < len; ++i) {

		}
	}
}


void touch_file(const std::string& stFileName) {
	FILE* f = fopen(stFileName.c_str(), "ab");
	if ( f == 0 ) {
		long lErr = GetLastError();
		throw CrException("Failed to touch the file '" + stFileName
			+ "'. Reason: " + ErrorResolver(lErr).Resolve(lErr));
	}
	fclose(f);
}

void truncate_file(const std::string& stFileName) {
	FILE* f = fopen(stFileName.c_str(), "wb");
	if ( f == 0 ) {
		long lErr = GetLastError();
		throw CrException("Failed to truncate the file '" + stFileName
			+ "'. Reason: " + ErrorResolver(lErr).Resolve(lErr));
	}
	fclose(f);
}

#ifdef WIN32
namespace {
	string getWindowsSystemTmpDir() {
		                                                          
		DWORD dw = ::GetTempPath(0, "");
		if ( dw == 0 ) {
			long lErr = GetLastError();
			throw CrException("Failed to get the path of the OS temporary directory (GetTempPath Win API). Reason: "
				+ ErrorResolver().Resolve(lErr));
		}

		boost::scoped_array<char> pTmpPath( new char[dw] );
		dw = ::GetTempPath(dw, pTmpPath.get());
		if ( dw == 0 ) {
			long lErr = GetLastError();
			throw CrException("Failed to get the path of the OS temporary directory (GetTempPath Win API with nBufferLength = "
				+ toString(dw) + "). Reason: " + ErrorResolver().Resolve(lErr));
		}
		assert(strlen(pTmpPath.get()) == dw);
		return string(pTmpPath.get());
	}
}
#endif

string getSystemTmpDir() {
#ifdef WIN32
	string dir( getWindowsSystemTmpDir() );
	if ( dir.empty() ) dir = PathSeparator;
#else
	string dir( konga::getEnvironmentVariableValue("TMP") );
	if ( dir.empty() ) {
		dir = konga::getEnvironmentVariableValue("TEMP");
	}
	if ( dir.empty() ) dir = "/tmp/"; 
#endif

	dir = getLongPathName(dir);
	FixPathSeparators(dir);
	AppendPathSeparator(dir);
	return dir;
}

string getTmpDir()
{
	string dir = GetOptionAsString("Settings", "TempDir");
	if( dir.empty() )
	{
		dir = getSystemTmpDir();
		dir += "etlforge";
		kCreateDirectory(dir);
	}

	dir = getLongPathName(dir);
	FixPathSeparators(dir);
	konga::AppendPathSeparator(dir);

	                                             
	if ( !IsDirectory(dir) )
		throw CrException("Configuration Error: The specified temporary directory \""
		+ dir + " does not exist or is not accessible to the current process.");
	return dir;
}

string getModuleFileName()
{
	#ifdef WIN32
	char pcBuf[MAX_PATH+1];
	memset(pcBuf,0,MAX_PATH+1);
	if( ::GetModuleFileName(::GetModuleHandle(0), pcBuf, MAX_PATH+1) )
		return pcBuf;
	return "Unknown";
	#else
	return "Unknown";
	#endif
}

bool kMoveFile(const string& stFileName, const string& stToPath, bool bOverWrite              ) {
	try {
		if ( !FileExists(stFileName) )
			throw CrException("Failed to move the file '" + stFileName + "' to '" + stToPath + "'. The source file does not exist.");

		boost::filesystem::path fromPath(stFileName);
		boost::filesystem::path toPath(stToPath);
		if ( boost::filesystem::equivalent(fromPath, toPath) )
			return true;	                                        

		if ( boost::filesystem::exists(toPath) && !boost::filesystem::is_directory(toPath) ) {
			if (bOverWrite)
				boost::filesystem::remove(toPath);
			else
				return false;
		}

		try {
			                                                                                                   
			                                                                         
			boost::filesystem::rename(fromPath, toPath);
		}
		catch ( const boost::filesystem::filesystem_error& ) {
			                                                                                                          
			boost::filesystem::copy_file(fromPath, toPath);
			boost::filesystem::remove(fromPath);
		}
		return true;
	} catch(const boost::filesystem::filesystem_error& e) {
		throw CrException("Failed to move the file '" + stFileName + "' to '" + stToPath + "'. Reason: " + e.what() + ".");
	}
}

bool kAppendFile(const string& stFileName, const string& AppendingFileName, bool bOverWrite              ) {
	if (FileExists(AppendingFileName) && stFileName!=AppendingFileName)
	{
		if (!bOverWrite && FileExists(AppendingFileName)) return false;
		  
		FILE* out=fopen(stFileName.c_str(), "ab");
		if(out==0){
			throw konga::CrException("can't open output file for combine chunking files. file_name:"+stFileName);
		}
		AutoFileHandle adopt_out(out);
		FILE* in=fopen(AppendingFileName.c_str(), "rb");
		if(in==0){
				throw konga::CrException("can't open input file for the chunking files. file_name:"+AppendingFileName);
		}
		AutoFileHandle adopt_in(in);
		const int buffersize=4096;
		char buffer[buffersize];
		while(!feof(in))
		{
			int readbytes=fread(buffer,1,buffersize,in);
			if (readbytes>0) fwrite(buffer,1,readbytes,out);
#ifdef _DEBUG
			if(buffersize!=readbytes) assert(feof(in));
#endif
		}
		return true;
	}
	return false;
}

bool kCopyFile(const string& stFileName, const string& stToPath, bool bOverWrite              ) {
    try {
        auto source=std::filesystem::u8path(stFileName),target=std::filesystem::u8path(stToPath);
        if(std::filesystem::exists(target)&&!bOverWrite)return false;
        return std::filesystem::copy_file(source,target,bOverWrite?std::filesystem::copy_options::overwrite_existing:std::filesystem::copy_options::none);
    }catch(const std::filesystem::filesystem_error& error){throw CrException("Failed to copy UTF-8 file path: "+std::string(error.what()));}
}

void kDeleteFile(const string& stFileName) {
	try {
		if ( !FileExists(stFileName) )
			return;
		boost::filesystem::remove( boost::filesystem::path(stFileName) );
	} catch(const boost::filesystem::filesystem_error& e) {
		throw CrException("Failed to remove the file '" + stFileName + "'. Reason: " + e.what() + ".");
	}
}

                                       
void DeleteFileWithRetries(const std::string& stFileName, const int iMax, const int sleepMs) {
	int i = 0;
	while ( i < iMax ) {
		try {
			kDeleteFile(stFileName);
			break;
		}
		catch( const CrException& ex ) {
			#ifdef _DEBUG
			std::cout << ex.GetErrorString() << std::endl << "Trying again... " << i << std::endl;
			#endif
			if ( i == iMax -1 ) {
				throw;
			}
			else {
				konga::MilliSleep(sleepMs);
				++i;
			}
		}
	}
}

void RemoveDirectoryWithRetries(const std::string& stPath, const int iMax, const int sleepMs) {
	int i = 0;
	while ( i < iMax ) {
		try {
			kRemoveDirectory(stPath);
			break;
		}
		catch( const CrException& ex ) {
			#ifdef _DEBUG
			std::cout << ex.GetErrorString() << std::endl << "Trying again... " << i << std::endl;
			#endif
			if ( i == iMax -1 ) {
				throw;
			}
			else {
				konga::MilliSleep(sleepMs);
				++i;
			}
		}
	}
}

void _tsplitpath(const char*path0, string&drive, string&dir, string&fname, string&ext)
{
    string path;
    drive="";
    dir="";
    fname="";
    ext="";
    {
        StringTokenizer tk(path0,":/\\");
        int ipart=0;
        while(tk.NextToken()){
            const char* token=tk.token();
            switch (token[0]) {
            case ':':
                drive=path;
                ipart=1;
                path="";
                break;
            case '/':
            case '\\':
                ipart=1;
                path+='/';
                break;
            default:
                path+=token;
            }
        }
    }
    size_t n=path.length(),i1=path.rfind('/'),i2=0,i3;
    if(i1<n)
    {
        dir=path.substr(0,i1+1);
        i2=i1+1;
    }
    i3=path.rfind('.');
    if(i3<i2)
    {
        fname=path;
    }
    else
    {
        if(i3>i2)fname=path.substr(i2,i3-i2);
        if(i3<(n-1))ext=path.substr(i3);
    }
}

void AppendPathSeparator(string& stPath) {
	if( stPath.length() > 0 && *(stPath.end()-1) != '/' && *(stPath.end()-1) != '\\' )
		stPath += PathSeparator;
}

string AppendPathSeparator(const string& stPath) {
	string sPathTmp(stPath);
	AppendPathSeparator(sPathTmp);
	return sPathTmp;
}

void RemovePathSeparator(std::string& stPath) {
	if ( stPath.length() > 0 &&
	   ( *(stPath.end()-1) == '/' || *(stPath.end()-1) == '\\') )
		stPath.erase(stPath.end()-1);
}

#ifndef WIN32
namespace {

	class AutoDirHandle : boost::noncopyable
	{
	public:
		AutoDirHandle(DIR*& h) : m_DIR(h) { }
		~AutoDirHandle(void) {
			Close();
		}

		void Close() {
			if ( m_DIR ) {
				closedir(m_DIR);
				m_DIR = 0;
			}
		}

	private:
		DIR*& m_DIR;
	};

	void RemoveDirectoryUnix(const string& stPath_in) {
		if ( !IsDirectory(stPath_in) )
			return;

		string dir_name(stPath_in);
		RemovePathSeparator(dir_name);

		DIR* pDir = opendir(dir_name.c_str());
		if ( pDir == 0 ) {
			throw CrException(ErrorResolver().Resolve(GetLastError()));
		}
		AutoDirHandle adopt_pDir(pDir);

		struct dirent64* entry = 0;
		while ( (entry=readdir64(pDir)) != 0 ) {
			if ( entry->d_name != 0 ) {
				const string d_name(entry->d_name);
                                       
				if ( d_name=="." || d_name == ".." ) continue;

				const string entry_path(dir_name + "/" + d_name);
				if ( IsDirectory(entry_path) ) {
					RemoveDirectoryUnix(entry_path);
				}
				else {
                                                         
					unlink(entry_path.c_str());
				}
			}
		}

		adopt_pDir.Close();

		                                               
                                              
		rmdir(dir_name.c_str());
	}
}
#endif

void ChangeWorkingDir(const std::string& stPath) {
#ifdef WIN32
	SetCurrentDirectory(stPath.c_str());
#else
	chdir(stPath.c_str());
#endif
	long lErr = GetLastError();
	if( lErr ) {
		throw CrException(lErr, "Failed to change dirctory to '" + stPath +
			"'. Reason: " + ErrorResolver().Resolve(lErr));
	}
}

void kRemoveDirectory(const string& stPath) {
#ifdef WIN32
	try {
		if ( !IsDirectory(stPath) )
			return;

		boost::filesystem::path p(stPath);
		if( p.empty() ) return;

		boost::filesystem::remove_all(p);
	} catch(const boost::filesystem::filesystem_error& e) {
		throw CrException("Failed to remove the directory '" + stPath + "'. Reason: " + e.what() + ".");
	}
#else
	                                                                     
	                       
	RemoveDirectoryUnix(stPath);
#endif
}

void kCreateDirectory(const string& stPath) {
	try {
		boost::filesystem::path p(stPath);
		if( p.empty() ) return;

		boost::filesystem::create_directories(p);
	} catch(const boost::filesystem::filesystem_error& e) {
		throw CrException("Failed to create the directory '" + stPath + "'. Reason: " + e.what() + ".");
	}
}

std::string GetBranchPath(const std::string& stFilePath) {
	                                                 
                                         
                                                    
                          

	                                  
	string::size_type ipos = stFilePath.find_last_of(stPATH_SEPARATORS);
	if ( ipos == string::npos )
		return string("");
	else if ( ipos == stFilePath.size()-1 )
		return stFilePath;
	else {
		string stRetVal( stFilePath.substr(0, ipos+1) );
		FixPathSeparators(stRetVal);
		return stRetVal;
	}
}

std::string GetLeafPath(const std::string& stFilePath) {
	string::size_type ipos = stFilePath.find_last_of(stPATH_SEPARATORS);
	if ( ipos == string::npos )
		return stFilePath;
	else if ( ipos == stFilePath.size()-1 )
		return string("");
	else
		return stFilePath.substr(ipos+1);
}

std::string GetFileExtension(const std::string& stFilePath) {
	string::size_type ipos = stFilePath.find_last_of(".");
	if ( ipos == string::npos || ipos == stFilePath.size()-1 )
		return string("");
	else
		return stFilePath.substr(ipos+1);
}

std::string RemoveFileExtension(const std::string& stFilePath) {
	const string tokens(stPATH_SEPARATORS + ".");
	const string::size_type ipos = stFilePath.find_last_of(tokens);

	if ( ipos == string::npos || stFilePath.at(ipos) != '.' ) {
		                                                   
		                                           
		return stFilePath;
	}
	return stFilePath.substr(0, ipos);
}

string GetFileNameWithTagInserted(const string& stFileName, const string& stTag) {
	const string::size_type pos=stFileName.find_last_of("./\\");
	if(pos!=string::npos&&stFileName.at(pos)=='.'){
		return stFileName.substr(0,pos)+stTag+stFileName.substr(pos);
	}
	else {
		return stFileName+stTag;
	}
}

string GetSequenceFileName(const string& stFileName, const string& stTag, int i){
	return GetFileNameWithTagInserted(stFileName, stTag + konga::toString(i));
}

bool IsDirectoryEmpty(const std::string&stDirPath)
{
	path dir_path(stDirPath);

	try {
		directory_iterator end_it, it(dir_path);
		return it==end_it;
	} catch( const boost::filesystem::filesystem_error& ) {
		return false;
	}
}

bool EquivalentPaths(const std::string& stPath1, const std::string& stPath2) {
	boost::filesystem::path path1(stPath1);
	boost::filesystem::path path2(stPath2);
	return boost::filesystem::equivalent(path1, path2);
}

void ftruncate(const std::string& fileName, size_t size){
	std::string tmp=getTmpFileName();
	FILE*in=fopen(fileName.c_str(), "rb");
	if(in==0){
		throw CrException("ftrunkcate error, can't open input file: \""+fileName+"\"");
	}

	fseek(in,0,SEEK_END);
	size_t end_pos=ftell(in);

	if(end_pos<=size){
		fclose(in);
		return;
	}

	const int len=1000;
	char buf[len];

	FILE*out=fopen(tmp.c_str(), "wb");
	if(out==0){
		throw CrException("ftrunkcate error, can't create output stream for temporally file: \""+tmp+"\"");
	}
	fseek(in,0, SEEK_SET);
	for(size_t i1=0, i2=len; i1<size; i1=i2, i2+=len){
		int len1=(i2<size)? len: (size-i1);
		fread(buf,1,len1,in);
		fwrite(buf,1,len1,out);
	}
	fclose(in);
	fclose(out);

	kMoveFile(tmp, fileName, true);
}

}         

