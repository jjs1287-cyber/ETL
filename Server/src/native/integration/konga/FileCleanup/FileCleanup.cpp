/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#include <boost/filesystem/operations.hpp>
#include <boost/filesystem/directory.hpp>
#include <boost/filesystem/exception.hpp>

#include <konga/Utils/CrException.h>
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/PortDefs.h>
#include <konga/Utils/StringTokenizer.h>
#include <konga/Utils/util.h>
#include <konga/Utils/Settings.h>

#include <konga/Zip/ZipUtils.h>

#include "CleanupUtils.h"
#include "FileCleanup.h"
#include "resource.h"

#include <atomic>
extern std::atomic<bool> g_cancel;

using namespace boost::filesystem;
using namespace konga;

using std::string;
using std::vector;

MatchPattern FileCleanup::GetPatternAtIndex(const vector<MatchPattern>::size_type& index) const {
	MatchPattern Pattern;
	if (index < m_patternList.size() && index >= 0) 
		Pattern = m_patternList[index];
	return Pattern;
}

MatchPattern FileCleanup::GetNotPatternAtIndex(const vector<MatchPattern>::size_type& index) const {
	MatchPattern Pattern;
	if (index < m_notPatternList.size() && index >= 0) 
		Pattern = m_notPatternList[index];
	return Pattern;
}

string FileCleanup::GetIgnoreSubDirAtIndex(const StringArray::size_type& index) const {
	string retStr;
	if (index < m_ignoreSubDirList.size() && index >= 0) 
		return m_ignoreSubDirList[index];
	return string();
}

void FileCleanup::SetDirectory(const string& strDir) {
	                                                                          
	string special("$(KONGA_TEMP_DIR)");
	string::size_type ipos=strDir.find(special);
	if(ipos==0){
		const string::size_type n=strDir.length();
		string sTmpDir(konga::getTmpDir());
		if(n<strlen(special.c_str()))sTmpDir+=strDir.substr(n);
		m_dirName=sTmpDir;
		return;
	}

	special="$(KONGA_LOG_DIR)";
	ipos=strDir.find(special);
	if(ipos==0){
		const string::size_type n=strDir.length();
		string sErr;
		string sLogDir(konga::GetLogDirectoryWithSlash(sErr, false));
		if ( sLogDir.empty() )
			throw CrException(sErr);

		if(n<strlen(special.c_str()))sLogDir+=strDir.substr(n);
		m_dirName=sLogDir;
		return;
	}

	m_dirName = strDir;
}

SizeUnit FileCleanup::GetSizeUnit(const string& sizeUnit) {
	if (stricmp(sizeUnit.c_str(), "BYTE") == 0)
		return SizeUnit_BYTE;
	if (stricmp(sizeUnit.c_str(), "KB") == 0)
		return SizeUnit_KB;
	if (stricmp(sizeUnit.c_str(), "MB") == 0)
		return SizeUnit_MB;
	if (stricmp(sizeUnit.c_str(), "GB") == 0)
		return SizeUnit_GB;
	
	throw CrException("The size unit '" + sizeUnit + "' is invalid.");
}

Comparator FileCleanup::GetComparator(const string& sComparator) {
	if ( stricmp(sComparator.c_str(), "EQ") == 0 )
		return Comparator_EQ;
	if ( stricmp(sComparator.c_str(), "LT") == 0 )
		return Comparator_LT;
	if ( stricmp(sComparator.c_str(), "GT") == 0 )
		return Comparator_GT;
	if ( stricmp(sComparator.c_str(), "LE") == 0 )
		return Comparator_LE;
	if ( stricmp(sComparator.c_str(), "GE") == 0 )
		return Comparator_GE;
	if ( stricmp(sComparator.c_str(), "NE") == 0 )
		return Comparator_NE;
	
	throw CrException("The comparator '" + sComparator + "' is invalid.");
}

long FileCleanup::GetSizeInBytes(long lSize, SizeUnit unit) {
	const long s = 1024;
	switch(unit) {
		case SizeUnit_BYTE:
			return lSize;
		case SizeUnit_KB:
			return lSize * s;
		case SizeUnit_MB:
			return lSize * s * s;
		case SizeUnit_GB:
			return lSize * s * s * s;
	};
	return lSize;
}

void FileCleanup::ConvertToAbsolutePath(){
	if(!konga::IsDirectory(m_dirName))m_dirName=konga::getAbsolutePath(m_dirName);
}

bool FileCleanup::ValidateRule(std::vector<std::string>& warnings)
{
	if ( m_valid >= 0 )
		return m_valid > 0;

	m_valid = 1;
	if (m_fileAge.nDays < 0) {
		warnings.push_back("File age '" + konga::toString(m_fileAge.nDays) + "' is invalid.");
		m_valid = 0;
	}

	if (m_fileSize.sizeBytes< 0) {
		warnings.push_back("File size '" + konga::toString(m_fileSize.sizeBytes) + "' is invalid.");
		m_valid = 0;
	}
		
	if (m_patternList.empty()) {
		warnings.push_back("No pattern(s) specified in the file cleanup rule. Nothing to clean.");
		m_valid = 0;
	}

	if (m_dirName.empty()) {
		warnings.push_back("No input directory name/path specified. Nothing to clean.");
		m_valid = 0;
	}

	if ( m_fileCompression.enabled && m_fileCompression.maxCompressed <= 0 ) {
		warnings.push_back("Number of compressed files to keep must be an integer larger than 0. Current value is "
			+ toString(m_fileCompression.maxCompressed));
		m_valid = 0;
	}

	return m_valid > 0;
}

bool FileCleanup::Execute(vector<string>& warnings)
{
	if ( !ValidateRule(warnings) )
		return true;	                                                     

                                                 
                                                   
	return SearchDirectoryAndClean(m_dirName, warnings, m_searchSubDir);
}

bool FileCleanup::MatchIgnoreDirList(const std::string& dir_name){
	for(StringArray::const_iterator it=m_ignoreSubDirList.begin();
		it!=m_ignoreSubDirList.end();it++){
		if(dir_name==*it)return true;
	}
	return false;
}

bool FileCleanup::match_pattern(const std::string&file_name, bool isSubdir){
	bool match=false;
	for(std::vector<MatchPattern>::iterator it=m_patternList.begin();
		it!=m_patternList.end(); it++){
		if (isSubdir && !it->bApplyToSubdir)
			continue;
		if (PathMatchSpec(file_name, it->stPattern)) {
			match = true;
			break;
		}
	}
	return match;
}

bool FileCleanup::match_not_pattern(const std::string&file_name, bool isSubdir){
	bool match=false;
	for(std::vector<MatchPattern>::iterator it=m_notPatternList.begin();
		it!=m_notPatternList.end(); it++){
		if (isSubdir && !it->bApplyToSubdir)
			continue;
		if (PathMatchSpec(file_name, it->stPattern)) {
			match = true;
			break;
		}
	}
	return match;
}

bool FileCleanup::SearchDirectoryAndClean(const std::string& strDirName,
										  vector<string>& warnings,
											   bool recurseSubdir          ,
											   bool isSubdir            )
{
	if (!konga::IsDirectory(strDirName.c_str())) {
		return true;
	}

	const path dir_path(strDirName);

	try {
		directory_iterator end_it, it(dir_path);
		if(it==end_it){
			return true;
		}

		string sTmpDir(konga::getTmpDir());                                                         
		konga::AppendPathSeparator(sTmpDir);
		path tmp_dir_path(sTmpDir);

		for(; it!=end_it; it++){
			if(g_cancel){
				m_lastErrorNum = FC_ERR_REQUEST_CANCELLED;
				return false;
			}
			                                                  
			const string leaf(it->path().filename().string());
			if(leaf=="."||leaf=="..")continue;
			if( is_directory(*it) ){
				const string sSubdirName(leaf), sSubdirPath(it->path().string());
				if( !recurseSubdir || MatchIgnoreDirList(sSubdirName) ) continue;

				const time_t dirWriteTime=last_write_time(*it);	                                                                  
				if( !SearchDirectoryAndClean(sSubdirPath, warnings, recurseSubdir, true) )
					return false;

				if(equivalent(tmp_dir_path, *it)) {
					warnings.push_back("Could not delete directory: " + sSubdirPath +
						". This directory is set as TMP directory under current environment.");
					continue;
				}

				try {
					if ( IsDirectoryEmpty(sSubdirPath) ) {
						                                                                    
						if ( MatchesFileAge(dirWriteTime) ) {
							konga::kRemoveDirectory(sSubdirPath);
						}
					}
				} catch( const CrException& e ){
					warnings.push_back("Could not delete directory: " + sSubdirPath +
						"\nExtended error: " + e.GetErrorString());
				}
				continue;
			} else {
				const string sFileName(leaf), sFilePath(it->path().string());
				const bool bMatchesPattern = match_pattern(sFileName, isSubdir) && !match_not_pattern(sFileName, isSubdir);

				if ( bMatchesPattern ) {
					const long fileSize=file_size(*it);
					const bool bMatchesSize=MatchesFileSize(fileSize);
					const time_t fileWriteTime=last_write_time(*it);
					const bool bMatchesAge=MatchesFileAge(fileWriteTime);
					if (bMatchesAge && bMatchesSize) {
						try {
							if ( m_fileCompression.enabled )
								CompressFile(sFilePath);
							else
								konga::kDeleteFile(sFilePath);
						} catch(CrException& e){
							warnings.push_back("Could not delete the file: " + sFilePath +
								"\nerror: " + e.GetErrorString());
						}
					}
				}
			}
		}
	} catch(const CrException& ex) {
		warnings.push_back(ex.GetErrorString());
	} catch(const boost::filesystem::filesystem_error& ex) {
		warnings.push_back(ex.what() + string(". "));
	} catch(const std::exception& ex) {
		warnings.push_back(ex.what());
	}

	return true;
}

namespace {
	const string COMPRESSED_PREFIX(".gz");
}

void FileCleanup::CompressFile(const std::string& sFilePath) {
	if ( sFilePath.size() > 5 && sFilePath.substr(sFilePath.size()-3) == COMPRESSED_PREFIX ) {
		                                                                  
		return;
	}

	                                                                      
	if ( m_fileCompression.maxCompressed < 1 ) {
		                  
		m_fileCompression.maxCompressed = 1;
	}
	
	if ( m_fileCompression.maxCompressed == 1 ) {
		                                                            
		konga::ZipUtils::GzipFile(sFilePath);
		konga::kDeleteFile(sFilePath);
		return;
	}
	else {
		int tag = 1;
		time_t oldestFile = 0;
		for( int i = 1; i <= m_fileCompression.maxCompressed; ++i ) {
			const string tmp( sFilePath + "-" + toString(i) + COMPRESSED_PREFIX);
			if ( FileExists(tmp) ) {
				time_t fileAge = boost::filesystem::last_write_time(tmp);
				if ( oldestFile == 0 || fileAge <= oldestFile ) {
					oldestFile = fileAge;
					tag = i;
				}
			}
			else {
				tag = i;
				break;
			}
		}

		const string sFilePathBeforeCompressing(sFilePath + "-" + toString(tag));
		konga::kMoveFile(sFilePath, sFilePathBeforeCompressing, true);
		konga::ZipUtils::GzipFile(sFilePathBeforeCompressing);
		konga::kDeleteFile(sFilePathBeforeCompressing);
	}
}

bool FileCleanup::MatchesFileAge(time_t fileWriteTime)
{
	time_t fileAgeTime = fileWriteTime+(m_fileAge.nDays * 24 * 3600);
	time_t currentTime = CleanupUtils::GetCurrentTime();

	                                              
                                                            

	switch(m_fileAge.comparator) {
		case Comparator_EQ:
			return fileAgeTime == currentTime;
		case Comparator_GT:
			return fileAgeTime < currentTime;
		case Comparator_LT:
			return fileAgeTime > currentTime;
		case Comparator_GE:
			return fileAgeTime <= currentTime;
		case Comparator_LE:
			return fileAgeTime >= currentTime;
		case Comparator_NE:
			return fileAgeTime != currentTime;
	};
	return false;
}

bool FileCleanup::MatchesFileSize(size_t fileSize)
{
	switch(m_fileSize.comparator) {
		case Comparator_EQ:
			return fileSize == m_fileSize.sizeBytes;
		case Comparator_GT:
			return fileSize > m_fileSize.sizeBytes;
		case Comparator_LT:
			return fileSize < m_fileSize.sizeBytes;
		case Comparator_GE:
			return fileSize >= m_fileSize.sizeBytes;
		case Comparator_LE:
			return fileSize <= m_fileSize.sizeBytes;
		case Comparator_NE:
			return fileSize != m_fileSize.sizeBytes;
	};
	return false;
}

bool FileCleanup::PathMatchSpec(const std::string& stPath, const std::string& stSpec)
{
	                                             
	const char* path=stPath.c_str();
	konga::StringTokenizer tk(stSpec.c_str(),"*?");
	char c=0;
	int i=0, n=strlen(path);
	while(tk.NextToken()){
		const char*token=tk.token();
		switch(token[0]){
		case '*':
			c='*';
			break;
		case '?':
			i++;
			if(i>n)return false;
			break;
		default:
			if(c=='*'){
				                                
				const char*s=strstr(&path[i],token);
				if(s==0)return false;
				i=s-path+strlen(token);
				c=0;
			} else {
				                                
				int l=strlen(token);
				if(strncmp(&path[i],token,l)!=0)return false;
				i+=l;
			}
		}
	}
	return i==n||c=='*';
}

bool FileCleanup::specialEqual(const std::string& path1, const std::string& path2)
{
	const char*p1=path1.c_str(),*p2=path2.c_str();
	for(int i=0,n=strlen(p1);i<n;i++){
		char c1=p1[i], c2=p2[i];
		if((c1=='/'||c1=='\\')&&(c2=='/'||c2=='\\'))continue;
		if(c1!=c2)return false;
	}
	return true;
}

