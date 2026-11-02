/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
                                                            
                                                   
                                       
                               

#include <stdio.h>	                    
#include <sstream>	               
#include <map>

#ifndef WIN32
	#include <libsmbclient.h>			                             
	#include <errno.h>	                 
	#include <boost/thread/mutex.hpp>	              
#else
	#include <comutil.h>						          
	#include <konga/CrNet/CrNet.h>				                                
	#include <konga/Utils/FileUtils.h>			                           
#endif         

#include <boost/noncopyable.hpp>

#include <konga/Utils/AutoFileCleanup.h>		                 
#include <konga/Utils/ErrorResolver.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/StringUtils.h>
#include <konga/Utils/DateUtils.h>
#include <konga/Utils/PortDefs.h>				                 

#include "Samba.h"

using namespace boost;
using namespace konga;
using std::string;
using std::vector;
using std::stringstream;


                     
namespace {

#ifndef WIN32
	                                                            
	                                                                                         
	                                                                                                      
	                                          
	class SmbCredentials {
	public:
		SmbCredentials(const string& stUser = "", const string& stPassword = "", const string& stWorkGroup = "")
		: m_stUser(stUser), m_stPwd(stPassword), m_stWG(stWorkGroup) {}
		~SmbCredentials(void) {}

		bool empty() {
			return (m_stUser.empty() && m_stPwd.empty() && m_stWG.empty());
		}

		string getUserName() const {
			return m_stUser;
		}

		string getPassword() const {
			return m_stPwd;
		}

		string getWorkgroup() const {
			return m_stWG;
		}

	private:
		string m_stUser;
		string m_stPwd;
		string m_stWG;
	};

	class SmbLocation {
	public:
		SmbLocation(const string& stServer, const string& stShare)
		: m_stServer(stServer), m_stShare(stShare) { }
		~SmbLocation(void) {}

		                                                                                 
		bool operator<(const SmbLocation& rhs) const {
			string stLhs(this->m_stServer + this->m_stShare);
			string stRhs(rhs.m_stServer + rhs.m_stShare);
			return (stLhs < stRhs);
		}

	private:
		string m_stServer;
		string m_stShare;
	};

	typedef std::map<SmbLocation, SmbCredentials> LocCredMap;

	mutex g_smbc_cred_mutex;	                                                            

	class SmbCredentialsLookup : noncopyable {
	public:
		SmbCredentialsLookup(void) {
		}
		~SmbCredentialsLookup(void) {
		}

		SmbCredentials getSmbCredentials(const char* server, const char* share) {
			mutex::scoped_lock global_lock(g_smbc_cred_mutex);
			LocCredMap::iterator it = m_LocCredMap.find(SmbLocation(server, share));
			if ( it != m_LocCredMap.end() ) {
				return it->second;
			}
			return SmbCredentials();
		}

		void addSmbCredentials(const Samba& smb) {
			addSmbCredentials(SmbLocation(smb.getServer(), smb.getShare()),
							  SmbCredentials(smb.getUserName(), smb.getPassword(), smb.getWorkGroup()));
		}

		void addSmbCredentials(const SmbLocation& loc, const SmbCredentials& creds) {
			mutex::scoped_lock global_lock(g_smbc_cred_mutex);
			m_LocCredMap[loc] = creds;
		}

		void removeCredentials(const SmbLocation& loc) {
			mutex::scoped_lock global_lock(g_smbc_cred_mutex);
			m_LocCredMap.erase(loc);
		}

		size_t size() const {
			mutex::scoped_lock global_lock(g_smbc_cred_mutex);
			return m_LocCredMap.size();
		}

	private:
		LocCredMap m_LocCredMap;
	};

	                                                                                                                       
	SmbCredentialsLookup g_CredentialLookup;

	void auth_fn(const char *server, const char *share,
			 char *workgroup, int wgmaxlen, char *username, int unmaxlen,
			 char *password, int pwmaxlen) {

		SmbCredentials creds = g_CredentialLookup.getSmbCredentials(server, share);
		string stWG( creds.getWorkgroup() );
		if ( static_cast<int>(stWG.length()) > wgmaxlen-1 ) {
			return;
		}
		string stUserName( creds.getUserName() );
		if ( static_cast<int>(stUserName.length()) > unmaxlen-1 ) {
			return;
		}
		string stPassword( creds.getPassword() );
		if ( static_cast<int>(stPassword.length()) > pwmaxlen-1 ) {
			return;
		}

		strcpy(workgroup, stWG.c_str());
		strcpy(username, stUserName.c_str());
		strcpy(password, stPassword.c_str());
	}


	                                                                                                                   
	                                                         
	mutex g_global_smbc_init_mutex;
	bool g_smbc_initialized = false;

	void global_smbc_init() {

		mutex::scoped_lock global_lock(g_global_smbc_init_mutex);
		if ( !g_smbc_initialized ) {
			if ( smbc_init(auth_fn,  0) < 0 ) {
				int error = GetLastError();
				throw CrException("Failed to initialize samba library. Reason: "
					+ ErrorResolver().Resolve(error));
			}
			g_smbc_initialized = true;
		}
	}

	class AutoCloseSmbFileHandle : noncopyable {
	public:
		AutoCloseSmbFileHandle(int fd) : m_fd(fd) {
		}
		~AutoCloseSmbFileHandle(void) {
			Close();
		}

		void Close() {
			if ( m_fd > 0 ) {
				smbc_close(m_fd);
				m_fd = 0;
			}
		}

	private:
		int m_fd;
	};

	class AutoCloseSmbDirHandle : noncopyable {
	public:
		AutoCloseSmbDirHandle(int fd) : m_fd(fd) {
		}
		~AutoCloseSmbDirHandle(void) {
			Close();
		}

		void Close() {
			if ( m_fd > 0 ) {
				smbc_closedir(m_fd);
				m_fd = 0;
			}
		}

	private:
		int m_fd;
	};

	inline int OpenSmbUrlForReading(const string& stUrl) {
		int fd = smbc_open(stUrl.c_str(), O_RDONLY, 0666);	                                                               
		if (fd < 0) {
			long lerr = GetLastError();
			stringstream sErr;
			sErr << "Failed to open \"" << stUrl << "\" for reading." << std::endl << ErrorResolver().Resolve(lerr);
			throw CrException(sErr.str());
		}
		return fd;
	}

	inline int OpenSmbUrlForWriting(const string& stUrl) {
		int fd = smbc_open(stUrl.c_str(), O_WRONLY | O_CREAT | O_TRUNC, 0666);	                                                               
		if (fd < 0) {
			long lerr = GetLastError();
			stringstream sErr;
			sErr << "Failed to open \"" << stUrl << "\" for writing." << std::endl << ErrorResolver().Resolve(lerr);
			throw CrException(sErr.str());
		}
		return fd;
	}

	                                                    
	inline string getSmbUrl(const Samba& smb, const string& stRemoteFile) {
		stringstream sUrl;
		sUrl << "smb://" << smb.getServer() << '/' << smb.getShare() << '/' << stRemoteFile;
		return sUrl.str();
	}
#else
	                                                                  
	inline string getLocator(const Samba& smb, const string& stRemoteFile) {
		string stRemoteFile_copy(stRemoteFile);
		ReplaceCharWithChar(stRemoteFile_copy, '/', '\\');
		if ( !stRemoteFile_copy.empty() && stRemoteFile_copy.at(0) == '\\' ) {
			stRemoteFile_copy.erase(0, 1);
		}
		return string("\\\\" + smb.getServer() + "\\" + smb.getShare() + "\\" + stRemoteFile_copy);
	}

#endif             

	const size_t BUF_SIZE = 4096;

	void RemoveReadOnlyFlag(const string stLocalFile) {
#ifdef WIN32
		DWORD attr = ::GetFileAttributes(stLocalFile.c_str());
		if ( attr == INVALID_FILE_ATTRIBUTES ) {
			const long lErr = GetLastError();
			throw CrException("Failed to get file attributes of local file \""
				+ stLocalFile + "\".\nReason: "
				+ ErrorResolver().Resolve(lErr));
		}

		if ( attr & FILE_ATTRIBUTE_READONLY ) {
			attr ^= FILE_ATTRIBUTE_READONLY;
			if ( !SetFileAttributes(stLocalFile.c_str(), attr) ) {
				const long lErr = GetLastError();
				throw CrException("Failed to remove read-only attribute of local file \""
					+ stLocalFile + "\".\nReason: "
					+ ErrorResolver().Resolve(lErr));
			}
		}
#endif
	}


}                         

#ifdef WIN32
namespace konga {
namespace SambaImpl {
	                                                      
	class AutoCreateSession : noncopyable {
	public:
		AutoCreateSession(const Samba& smb)
		: m_bSessionCreated(false), m_stHost(smb.getServer()) {
			const string stDomain(smb.getWorkGroup());
			const string stUser(smb.getUserName());
			const string stPassword(smb.getPassword());

			const long err = ::CreateSession(_bstr_t(m_stHost.c_str()), _bstr_t(stDomain.c_str()), _bstr_t(stUser.c_str()), _bstr_t(stPassword.c_str()));
			if ( err !=  NERR_Success ) {
				string stErr("Failed to connect to " + m_stHost + " with user ");
				if ( stDomain.empty() )
					stErr += stUser;
				else
					stErr += stDomain + "\\" + stUser;
				stErr += ". Reason:\n";
				stErr += ErrorResolver().Resolve(err);
				throw CrException(stErr, err);
			}
			m_bSessionCreated = true;
		}

		~AutoCreateSession(void) {
			if ( m_bSessionCreated ) {
				long err = ::DestroySession(_bstr_t(m_stHost.c_str()), 1);
				                           
			}
		}

	private:
		bool m_bSessionCreated;
		string m_stHost;
	};
}}
#endif

void Samba::Connect() const {
#ifdef WIN32
	if(m_session) return;
	m_session = new SambaImpl::AutoCreateSession(*this);
#endif
}

void Samba::Disconnect() const {
#ifdef WIN32
	if(m_session) delete m_session;
#endif
}

bool Samba::doTargetDirectoryCheck() const {
	return ( m_bCheckIfTargetDirectoryExists && !m_bTargetDirectoryKnownToExist );
}

int Samba::SmbDirectoryEntry::day() const {
	return CrDateTime(ctime).GetDay();
}

int Samba::SmbDirectoryEntry::month() const {
	return CrDateTime(ctime).GetMonth();
}

int Samba::SmbDirectoryEntry::time() const {
	CrDateTime dtime(ctime);
	stringstream sTime;
	sTime << dtime.GetHour() << dtime.GetMinute() << dtime.GetSecond();
	return atoi(sTime.str().c_str());
}

Samba::Samba(bool bCheckIfTargetDirectoryExists, bool bPreserveReadOnlyFlagOnWindows) :
	m_bCheckIfTargetDirectoryExists(bCheckIfTargetDirectoryExists), m_bPreserveReadOnlyFlagOnWindows(bPreserveReadOnlyFlagOnWindows), m_bTargetDirectoryKnownToExist(false) {
#ifndef WIN32
	global_smbc_init();
#else
	m_session = 0;
#endif
}

Samba::~Samba(void) {
	Disconnect();
}

void Samba::setServer(const string& stServer) {
	if( m_stServer != stServer ) {
		                                                                    
		Disconnect();
		m_stServer = stServer;
	}
	if ( m_stWorkGroup.empty() )
		m_stWorkGroup = stServer;
}

void Samba::setShare(const string& stShare) {
	m_stShare = stShare;
}

string Samba::setUNCPath(const string& stPath_in) {
	string stPath(TrimString(stPath_in));

	if ( stPath.length() < 5 ) {
		throw CrException("The path \"" + stPath_in + "\" is not a valid UNC path. It is too short.");
	}

	ReplaceCharWithChar(stPath, '\\', '/');

	if ( stPath.substr(0,2) != "//" ) {
		throw CrException("The path \"" + stPath_in + "\" is not a valid UNC path. It does not start with \"//\" or \"\\\\\"");
	}

	stPath = stPath.substr(2);
	string::size_type iPos = stPath.find_first_of('/');
	if ( iPos == string::npos ) {
		throw CrException("The path \"" + stPath_in + "\" is not a valid UNC path. It does not contain any server/share separator.");
	}
	else if ( iPos == stPath.size()-1 ) {
		throw CrException("The path \"" + stPath_in + "\" is not a valid UNC path. It does not contain any share name.");
	}
	string stServer(stPath.substr(0, iPos));

	stPath = stPath.substr(iPos+1);
	iPos = stPath.find_first_of('/');
	string stShare, stFile;
	if ( iPos != string::npos ) {
		stShare = stPath.substr(0, iPos);
		if ( iPos < stPath.size()-1) {
			stFile = stPath.substr(iPos+1);
		}
	}
	else {
		stShare = stPath;
	}
	setServer(stServer);
	setShare(stShare);
	return stFile;
}

void Samba::setWorkGroup(const string& stWorkGroup) {
	m_stWorkGroup = stWorkGroup;
}

void Samba::setUserName(const string& stUserName) {
	string::size_type ipos = stUserName.find_first_of("/\\");
	if ( ipos == string::npos ) {
		m_stUserName = stUserName;
	}
	else {
		m_stWorkGroup = stUserName.substr(0, ipos);
		if ( ipos < stUserName.size()-1 ) {
			m_stUserName = stUserName.substr(ipos+1);
		}
	}
}

void Samba::setPassword(const string& stPassword) {
	m_stPassword = stPassword;
}

string Samba::getServer() const {
	return m_stServer;
}

string Samba::getShare() const {
	return m_stShare;
}

string Samba::getWorkGroup() const {
	return m_stWorkGroup;
}

string Samba::getUserName() const {
	return m_stUserName;
}

string Samba::getPassword() const {
	return m_stPassword;
}

void Samba::GetUncDirectoryListing(const string& stUncPath, vector<Samba::SmbDirectoryEntry>& dirList, int nMaxFiles) {
	return GetShareDirectoryListing(setUNCPath(stUncPath), dirList, nMaxFiles);
}

void Samba::CopyFromUncToLocalFile(const string& stUncPath, const string& stLocalFile){
	CopyFromShareToLocalFile(setUNCPath(stUncPath), stLocalFile);
	if ( !m_bPreserveReadOnlyFlagOnWindows) RemoveReadOnlyFlag(stLocalFile);
}

void Samba::CopyFromLocalFileToUnc(const string& stLocalFile, const string& stUncPath){
	CopyFromLocalFileToShare(stLocalFile, setUNCPath(stUncPath));
}

void Samba::CopyFromUncToLocalString(const string& stUncPath, string& stLocalString) {
	CopyFromShareToLocalString(setUNCPath(stUncPath), stLocalString);
}

void Samba::CopyFromLocalStringToUnc(const string& stLocalString, const string& stUncPath) {
	CopyFromLocalStringToShare(stLocalString, setUNCPath(stUncPath));
}

void Samba::DeleteUnc(const string& stUncPath, bool bThrowOnNonExistingPath) {
	DeleteFileOnShare(setUNCPath(stUncPath), bThrowOnNonExistingPath);
}

void Samba::MoveUnc(const std::string& stOldUncPath, const std::string& stNewUncPath) {
	MoveFileOnShare(setUNCPath(stOldUncPath), setUNCPath(stNewUncPath));
}

                                                                                              
#ifndef WIN32
	                                                                                               
	                                                                  
	mutex g_smb_call_mutex;
#endif                      

void Samba::GetShareDirectoryListing(const string& stRemoteDirectory_in, vector<SmbDirectoryEntry>& vresult, int nMaxFiles_) const {
	string stRemoteDirectory(stRemoteDirectory_in);
	ReplaceCharWithChar(stRemoteDirectory, '\\', '/');
	if ( !stRemoteDirectory.empty() && stRemoteDirectory.at(stRemoteDirectory.size()-1) != '/' ) {
		stRemoteDirectory += '/';
	}

	size_t nMaxFiles = 0;
	if ( nMaxFiles_ > 0 ) {
		nMaxFiles = nMaxFiles_;
	}

#ifndef WIN32
	mutex::scoped_lock global_lock(g_smb_call_mutex);

	string stSmbUrl( getSmbUrl(*this, stRemoteDirectory) );
	g_CredentialLookup.addSmbCredentials(*this);

	int fdDir = smbc_opendir(stSmbUrl.c_str());
	if ( fdDir < 0 ) {
		long lerr = GetLastError();
		stringstream sErr;
		sErr << "Failed to get directory listing for remote shared directory \"" << stSmbUrl << "\"." << std::endl << ErrorResolver().Resolve(lerr);
		throw CrException(sErr.str());
	}
	AutoCloseSmbDirHandle adopt_fdDir(fdDir);

	struct smbc_dirent* dirent = 0;
	while ( (dirent=smbc_readdir(fdDir)) != 0 ) {
		if ( strcmp(dirent->name, ".") != 0 && strcmp(dirent->name, "..") != 0 ) {
			string stSmbEntryUrl( stSmbUrl + dirent->name);
			struct stat s;
			smbc_stat(stSmbEntryUrl.c_str(), &s);
			vresult.push_back( SmbDirectoryEntry(dirent->smbc_type, dirent->name, s) );
			if ( nMaxFiles > 0 && vresult.size() >= nMaxFiles ) break;
		}
	}

	                                                                          
	long lerr = GetLastError();
	if (lerr != 0 ) {
		stringstream sErr;
		sErr << "Failed to get directory listing for remote shared directory \"" << stSmbUrl << "\"." << std::endl << ErrorResolver().Resolve(lerr);
		throw CrException(sErr.str());
	}

#else
	ReplaceCharWithChar(stRemoteDirectory, '/', '\\');
	string stLocator( getLocator(*this, stRemoteDirectory) );
	if ( stLocator.at(stLocator.size()-1) != '\\' )
		stLocator += '\\';

	const string stLocatorFilter( stLocator + "*");

	Connect();
	WIN32_FIND_DATA FindFileData;
	HANDLE hFind = ::FindFirstFile(stLocatorFilter.c_str(), &FindFileData);
	if ( hFind == INVALID_HANDLE_VALUE ) {
		unsigned long lErr = GetLastError();
		throw CrException( ErrorResolver(lErr).Resolve(lErr) );
	}

	bool bDone = false;
	while( !bDone ) {
		string stFile( FindFileData.cFileName );
		if ( !(FindFileData.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY) ) {
			struct stat s;
			string stThisFile( stLocator + FindFileData.cFileName );
			if ( stat(stThisFile.c_str(), &s) == 0 ) {
				vresult.push_back( SmbDirectoryEntry(SMB_FILE, FindFileData.cFileName, s) );
			}
			if ( nMaxFiles > 0 && vresult.size() >= nMaxFiles ) break;
		}
		if ( !::FindNextFile(hFind, &FindFileData) ) {
			if (GetLastError() == ERROR_NO_MORE_FILES) {
				bDone = true;
			} 
			else {
				::FindClose(hFind);
				throw CrException("Failed to find next file while searching for files in \"" + stLocator + "\".\n" + ErrorResolver().Resolve(::GetLastError()));
			} 
		}
	}
	::FindClose(hFind);
#endif                      
}

void Samba::CopyFromShareToLocalFile(const string& stRemoteFile, const string& stLocalFile) const {
#ifndef WIN32
	mutex::scoped_lock global_lock(g_smb_call_mutex);

	string stUrl( getSmbUrl(*this, stRemoteFile) );
	g_CredentialLookup.addSmbCredentials(*this);

	int fdSmb = OpenSmbUrlForReading(stUrl);
	AutoCloseSmbFileHandle adopt_fdSmb(fdSmb);

	FILE* file = fopen(stLocalFile.c_str(), "wb");
	if ( file == 0 ) {
		long lerr = GetLastError();
		stringstream sErr;
		sErr << "Failed to open local file \"" << stLocalFile << "\" for writing. " << std::endl << ErrorResolver().Resolve(lerr);
		throw CrException(sErr.str());
	}
	AutoFileHandle adopt_file(file);

	char buf[BUF_SIZE];
	size_t nread = 0;
	while ( (nread=smbc_read(fdSmb, buf, BUF_SIZE)) > 0 )  {
		if ( fwrite(buf, 1, nread, file) < nread ) {
			long lerr = GetLastError();
			stringstream sErr;
			sErr << "Failed to write to previously opened file \"" << stLocalFile << "\"." << std::endl << ErrorResolver().Resolve(lerr);
			throw CrException(sErr.str());
		}
	}
	if ( nread < 0 ) {
		long lerr = GetLastError();
		stringstream sErr;
		sErr << "Failed to read from previously opened file share location \"" << stUrl << "\"." << std::endl << ErrorResolver().Resolve(lerr);
		throw CrException(sErr.str());
	}

#else
	Connect();
	if ( ::CopyFile(getLocator(*this, stRemoteFile).c_str(), stLocalFile.c_str(), false) == 0 ) {
		throw CrException("Failed to copy the file \"" + getLocator(*this, stRemoteFile) + "\" to \"" + stLocalFile + "\".\n" + ErrorResolver().Resolve(::GetLastError()));
	}
#endif         
}

void Samba::CopyFromShareToLocalString(const string& stRemoteFile, string& stLocalString) const {
#ifndef WIN32
	mutex::scoped_lock global_lock(g_smb_call_mutex);

	string stUrl( getSmbUrl(*this, stRemoteFile) );
	g_CredentialLookup.addSmbCredentials(*this);

	int fdSmb = OpenSmbUrlForReading(stUrl);
	AutoCloseSmbFileHandle adopt_fdSmb(fdSmb);

	char buf[BUF_SIZE+1];	                                       
	int nread = 0;
	try {
		while ( (nread=smbc_read(fdSmb, buf, BUF_SIZE)) > 0 ) {
			buf[nread] = 0;
			stLocalString.append(buf);
		}
	}
	catch( const std::exception& ex ) {
		stringstream sErr;
		sErr << "Failed to append to string. " << std::endl << ex.what();
		throw CrException(sErr.str());
	}
	if ( nread < 0 ) {
		long lerr = GetLastError();
		stringstream sErr;
		sErr << "Failed to read from previously opened file share location \"" << stUrl << "\"." << std::endl << ErrorResolver().Resolve(lerr);
		throw CrException(sErr.str());
	}
#else
	        
	throw CrException("Not implemented");
#endif         
}

void Samba::CopyFromLocalFileToShare(const string& stLocalFile, const string& stRemoteFile) const{
#ifndef WIN32
	mutex::scoped_lock global_lock(g_smb_call_mutex);

	string stUrl( getSmbUrl(*this, stRemoteFile) );
	g_CredentialLookup.addSmbCredentials(*this);

	int fdSmb = OpenSmbUrlForWriting(stUrl);
	AutoCloseSmbFileHandle adopt_fdSmb(fdSmb);

	FILE* file = fopen(stLocalFile.c_str(), "rb");
	if ( file == 0 ) {
		long lerr = GetLastError();
		stringstream sErr;
		sErr << "Failed to open local file \"" << stLocalFile << "\" for reading. " << std::endl << ErrorResolver().Resolve(lerr);
		throw CrException(sErr.str());
	}
	AutoFileHandle adopt_file(file);

	char buf[BUF_SIZE];
	int nread = 0;
	while ( (nread=fread(buf, 1, BUF_SIZE, file)) > 0 ) {
		if ( smbc_write(fdSmb, buf, nread) < nread ) {
			long lerr = GetLastError();
			stringstream sErr;
			sErr << "Failed to write to previously opened file share location \"" << stUrl << "\"." << std::endl << ErrorResolver().Resolve(lerr);
			throw CrException(sErr.str());
		}
	}
	if ( ferror(file) != 0 ) {
		long lerr = GetLastError();
		stringstream sErr;
		sErr << "Failed to read from previously opened local file \"" << stLocalFile << "\"." << std::endl << ErrorResolver().Resolve(lerr);
		throw CrException(sErr.str());
	}
	else if ( feof(file) == 0) {
		stringstream sErr;
		sErr << "Inconsistent EOF indicator for previously opened local file \"" << stLocalFile << "\". EOF was not reached.";
		throw CrException(sErr.str());
	}
#else
	Connect();
	const string& sLocator = getLocator(*this, stRemoteFile);
	if ( doTargetDirectoryCheck() ) {
		try {
			const string& dir = konga::GetBranchPath(sLocator);
			if(!konga::IsDirectory(dir)) {
				konga::kCreateDirectory(dir);
			}
		}
		catch(...) {
			                                                                                              
		}
	}

	                                                                                                
	m_bTargetDirectoryKnownToExist = true;

	if ( ::CopyFile(stLocalFile.c_str(), sLocator.c_str(), false) == 0 ) {
		throw CrException("Failed to copy the file \"" + stLocalFile + "\" to \"" + getLocator(*this, stRemoteFile) + "\".\n" + ErrorResolver().Resolve(::GetLastError()));
	}
#endif         
}

void Samba::CopyFromLocalStringToShare(const string& stLocalString, const string& stRemoteFile) const {
#ifndef WIN32
	mutex::scoped_lock global_lock(g_smb_call_mutex);

	string stUrl( getSmbUrl(*this, stRemoteFile) );
	g_CredentialLookup.addSmbCredentials(*this);

	int fdSmb = OpenSmbUrlForWriting(stUrl);
	AutoCloseSmbFileHandle adopt_fdSmb(fdSmb);

	char buf[BUF_SIZE+1];	                                    
	int nread = 0;
	string::size_type pos = 0;
	string::size_type size = stLocalString.size();
	while(true) {
		nread = (size-pos < BUF_SIZE) ? size-pos : BUF_SIZE;
		strcpy(buf, stLocalString.substr(pos, nread).c_str());
		if ( smbc_write(fdSmb, buf, nread) < nread ) {
			long lerr = GetLastError();
			stringstream sErr;
			sErr << "Failed to write to previously opened file share location \"" << stUrl << "\"." << std::endl << ErrorResolver().Resolve(lerr);
			throw CrException(sErr.str());
		}
		pos += nread;
		if ( pos >= size )
			break;
	}
#else
	        
	throw CrException("Copying from a local string to file share is not implemented on windows.");
#endif         
}

void Samba::DeleteFileOnShare(const string& stRemoteFile, bool bThrowOnNonExistingFile) const {
#ifndef WIN32
	mutex::scoped_lock global_lock(g_smb_call_mutex);

	string stUrl( getSmbUrl(*this, stRemoteFile) );
	g_CredentialLookup.addSmbCredentials(*this);

	if ( smbc_unlink(stUrl.c_str()) < 0 ) {
		long lerr = GetLastError();
		if ( !bThrowOnNonExistingFile && lerr == ENOENT) {
			return;                                   
		}
		stringstream sErr;
		sErr << "Failed to delete file on share \"" << stUrl << "\"." << std::endl << ErrorResolver().Resolve(lerr);
		throw CrException(sErr.str());
	}
#else
	Connect();
	const string stLocator(getLocator(*this, stRemoteFile));
	WIN32_FIND_DATA FindFileData;
	HANDLE hFind = ::FindFirstFile(stLocator.c_str(), &FindFileData);
	if ( hFind == INVALID_HANDLE_VALUE ) {
		if ( bThrowOnNonExistingFile ) {
			throw CrException(ErrorResolver().Resolve(::GetLastError()));
		}
		return;
	}
	else {
		::FindClose(hFind);
	}

	                          
	if ( ::DeleteFile(stLocator.c_str()) == 0 ) {
		throw CrException("Failed to delete the file \"" + stLocator + "\".\n" + ErrorResolver().Resolve(::GetLastError()));
	}
#endif
}

void Samba::MoveFileOnShare(const std::string& stOldName, const std::string& stNewName) const {
#ifndef WIN32
	mutex::scoped_lock global_lock(g_smb_call_mutex);

	string stOldUrl( getSmbUrl(*this, stOldName) );
	string stNewUrl( getSmbUrl(*this, stNewName) );
	g_CredentialLookup.addSmbCredentials(*this);

	if ( smbc_rename(stOldUrl.c_str(), stNewUrl.c_str()) < 0 ) {
		long lerr = GetLastError();
		stringstream sErr;
		sErr << "Failed to move \"" << stOldUrl << "\" to \"" << stNewUrl << "\"." << std::endl << ErrorResolver().Resolve(lerr);
		throw CrException(sErr.str());
	}
#else
	Connect();
	if ( ::MoveFileEx(getLocator(*this, stOldName).c_str(), getLocator(*this, stNewName).c_str(), MOVEFILE_REPLACE_EXISTING) == 0 ) {
		throw CrException("Failed to rename the file \"" + getLocator(*this, stOldName) + "\" to \"" + getLocator(*this, stNewName) + "\".\n" + ErrorResolver().Resolve(::GetLastError()));
	}
#endif         
}

                                  

void konga::CopyFromUncToUnc(const string& stSourceUNC, Samba& source, const string& stTargetUNC, Samba& target) {
	string stSource( source.setUNCPath(stSourceUNC) );
	string stTarget( target.setUNCPath(stTargetUNC) );

#ifndef WIN32
	mutex::scoped_lock global_lock(g_smb_call_mutex);

	g_CredentialLookup.addSmbCredentials(source);
	g_CredentialLookup.addSmbCredentials(target);

	string stSourceUrl( getSmbUrl(source, stSource) );
	string stTargetUrl( getSmbUrl(target, stTarget) );

	int fdSource = OpenSmbUrlForReading(stSourceUrl);
	AutoCloseSmbFileHandle adopt_fdSource(fdSource);

	int fdTarget = OpenSmbUrlForWriting(stTargetUrl);
	AutoCloseSmbFileHandle adopt_fdTarget(fdTarget);

	char buf[BUF_SIZE];
	int nread = 0;
	while ( (nread=smbc_read(fdSource, buf, BUF_SIZE)) > 0 )  {
		if ( smbc_write(fdTarget, buf, nread) < nread ) {
			long lerr = GetLastError();
			stringstream sErr;
			sErr << "Failed to write to previously opened file share location \"" << stTargetUrl << "\"." << std::endl << ErrorResolver().Resolve(lerr);
			throw CrException(sErr.str());
		}
	}
	if ( nread < 0 ) {
		long lerr = GetLastError();
		stringstream sErr;
		sErr << "Failed to read from previously opened file share location \"" << stSourceUrl << "\"." << std::endl << ErrorResolver().Resolve(lerr);
		throw CrException(sErr.str());
	}
#else
	SambaImpl::AutoCreateSession source_session(source);
	SambaImpl::AutoCreateSession target_session(target);

	if ( ::CopyFile(getLocator(source, stSource).c_str(), getLocator(target, stTarget).c_str(), false) == 0 ) {
		throw CrException("Failed to copy the file \"" + getLocator(source, stSource) + "\" to \"" + getLocator(target, stTarget) + "\".\n" + ErrorResolver().Resolve(::GetLastError()));
	}
#endif         
}
