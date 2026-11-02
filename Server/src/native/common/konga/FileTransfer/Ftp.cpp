/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <boost/scoped_array.hpp>

#include <konga/Utils/Url.h>
#include <konga/Utils/DateUtils.h>
#include <konga/Utils/CrException.h>
#include <konga/Utils/Settings.h>
#include <konga/Db/DbUtils/DbUtils.h>
#include <konga/Utils/StringUtils.h>	                   

#include "ftpparse.h"
#include "Ftp.h"
#ifdef GetCurrentTime
#undef GetCurrentTime
#endif

using namespace konga;
using std::vector;
using std::string;

typedef vector<string> vstrings;

Ftp::Ftp(void) : m_bUsePassiveMode(true), m_bUseImplicitFtps(false),
m_TransferType(FTP_TRANSFER_TYPE_BINARY), m_SecurityOption(FTP_SECURITY_NONE), m_PreferredFtpsAuth(FTPS_AUTH_TLS) { }

Ftp::~Ftp(void) { }

long Ftp::CheckHeadersForErrors() {
	return Protocol::SUCCESS;
}

void Ftp::setTransferType(FtpTransferType type) {
	m_TransferType = type;
}

FtpTransferType Ftp::getTransferType() const {
	return m_TransferType;
}

void Ftp::setUsePassiveMode(bool bUsePassiveMode) {
	m_bUsePassiveMode = bUsePassiveMode;
}

bool Ftp::usePassiveMode() const {
	return m_bUsePassiveMode;
}

void Ftp::setSecurityOption(FtpSecurityOption level) {
	m_SecurityOption = level;
}

FtpSecurityOption Ftp::getSecurityOption() const {
	return m_SecurityOption;
}

void Ftp::setPreferredFtpsAuth(FtpsAuthenticationOption auth) {
	m_PreferredFtpsAuth = auth;
}

FtpsAuthenticationOption Ftp::getPreferredFtpsAuth() const {
	return m_PreferredFtpsAuth;
}

void Ftp::useImplicitFtps(bool useImplicitFtps) {
	m_bUseImplicitFtps = useImplicitFtps;
}

bool Ftp::useImplicitFtps() const {
	switch( getSecurityOption() ) {
		case FTP_SECURITY_NONE:
		case FTP_SECURITY_SFTP:
			return false;
		default:
			return m_bUseImplicitFtps;
	}
}

bool Ftp::isSftp() const {
	return (getSecurityOption() == FTP_SECURITY_SFTP);
}

long Ftp::ParseURL() {
	std::string sUrl;
	if ( !UsingUrl()) {
		if ( isSftp() ) {
			sUrl = "sftp://";
		}
		else if ( useImplicitFtps() ) {
			                                           
			sUrl = "ftps://";
		}
		else {
			sUrl = "ftp://";
		}
		sUrl += getHost();

		const long lPort = getPort();
		if ( lPort > 0 )
			sUrl += ":" + toString(lPort);
		sUrl += getPath();

		                                                                   
		konga::UrlEscape(sUrl, false, false);
	}
	else {
		sUrl = GetUrl();
	}

	try {
		Url url;
		url.Parse(sUrl);

		                      
		const string& stProtocol = url.getProtocol();
		if ( !EqualsIgnoreCase(stProtocol, "ftp") &&
			 !EqualsIgnoreCase(stProtocol, "sftp") &&
			 !EqualsIgnoreCase(stProtocol, "ftps") )
		{
			AddError("Failed to parse the url \"" + sUrl + "\". This is not an ftp URL.");
			return Protocol::FAILURE;
		}

		if ( useImplicitFtps() ) {
			                                                        
			if ( !EqualsIgnoreCase(stProtocol, "ftps") ) {
				sUrl = url.getParsedUrl();
				const string::size_type ipos = sUrl.find("://");
				if ( ipos != string::npos ) {
					sUrl.replace(0, ipos, "ftps");
				}
				url.Parse(sUrl);
			}
		}

		                                                                                      
		if ( UsingUrl() ) {
			setHost(url.getHost());
			setPort(url.getPort());
			setPath(url.getPath());
		}

		m_stParsedUrl = url.getParsedUrl();
#ifdef _DEBUG
                                                             
#endif
	}
	catch ( const CrException& ex ) {
		AddError(ex.GetErrorString());
		return Protocol::FAILURE;
	}
#ifndef _DEBUG
	catch( ... ) {
		AddError("Unexpected exception while parsing URL " + sUrl + ". Please report this bug.");
		return Protocol::FAILURE;
	}
#endif
	return Protocol::SUCCESS;
}

void Ftp::configureSftp(const string& keyId,const string& fingerprint) {
 try {
  m_sshFingerprint=etlforge::ssh::validateHostFingerprint(fingerprint);
  if(keyId.empty())m_sshKey=nullptr;else {konga::SmartDbPtr<konga::DbConnection> db(konga::GetBackEndDbConnection("TranDb"));m_sshKey=etlforge::ssh::lease(db->GetConnectionHandle(),konga::GetServerHomeWithSlash()+"private/ssh-keys",konga::GetOptionAsString("Security","KeyVaultMasterKey",""),keyId);}
 } catch(const std::exception& ex) {throw CrException(ex.what());}
}

void Ftp::setSSHKeyFileOptions() {
	string st = konga::GetOptionAsString("SSH", "PublicKeyFile");
	if ( !st.empty() )
		m_pTransferLib->setSshPublicKeyFileName(st);

	st = konga::GetOptionAsString("SSH", "PrivateKeyFile");
	if ( !st.empty() ) {
		const string stPassphrase( konga::GetOptionAsString("SSH", "PrivateKeyPassphrase") );
		m_pTransferLib->setSshPrivateKeyFileName(st, stPassphrase);
	}
}

bool Ftp::isSslEnabled() const {
	                                                 
	if ( useImplicitFtps() )
		return true;

	switch( getSecurityOption() ) {
		case FTP_SECURITY_TRY_SSL:
		case FTP_SECURITY_SSL_CONTROL:
		case FTP_SECURITY_SSL_ALL:
			return true;
		default:
			return false;
	}
}

long Ftp::doPrePut() {
	const long retval = InetBase::doPrePut();
	if ( retval != Protocol::SUCCESS )
		return retval;

	m_pTransferLib->setFtpRenameTo(m_stRenameFromFileName, m_stRenameToFileName);
	return Protocol::SUCCESS;
}

long Ftp::doPreTransfer() {
	long lErr = InetBase::doPreTransfer();
	if ( lErr != Protocol::SUCCESS )
		return lErr;

	                     
	if ( m_TransferType == FTP_TRANSFER_TYPE_ASCII )
		m_pTransferLib->setFtpAscii();
	else
		m_pTransferLib->setFtpBinary();

	                                   
	m_pTransferLib->setFtpPassiveMode(usePassiveMode());

	if ( isSslEnabled() ) {
		if ( useImplicitFtps() ) {
			                          
		}
		else {
			                                
			switch( getSecurityOption() ) {
				case FTP_SECURITY_TRY_SSL:
					m_pTransferLib->setFtpSslTry();
					break;
				case FTP_SECURITY_SSL_CONTROL:
					m_pTransferLib->setFtpSslControl();
					break;
				case FTP_SECURITY_SSL_ALL:
					m_pTransferLib->setFtpSslAll();
					break;
				default:
					break;
			};

			if ( m_PreferredFtpsAuth == FTPS_AUTH_TLS ) {
				m_pTransferLib->preferAUTH_TLS(true);
			}
			else {
				m_pTransferLib->preferAUTH_TLS(false);
			}
		}
	}

	if ( isSftp() ) {
		                                              
		                                                                     
		if(m_sshFingerprint.empty())throw CrException("SFTP requires a verified SHA256 host fingerprint in the source or target settings.");
        m_pTransferLib->configureSftp(m_sshKey?m_sshKey->privateFile:"",m_sshKey?m_sshKey->publicFile:"",m_sshKey?m_sshKey->passphrase:"",m_sshFingerprint);
	}

	return Protocol::SUCCESS;
}

namespace {
	void fillDateTime(const CrDateTime& dt, Ftp::FtpDirectoryListEntry& entry, bool bIncludeSeconds) {
		entry.year = dt.GetYear();
		entry.month = dt.GetMonth();
		entry.day = dt.GetDay();
		if ( bIncludeSeconds ) {
			entry.seconds = 3600*dt.GetHour() + 60*dt.GetMinute() + dt.GetSecond();
		}
		else {
			entry.seconds = 3600*dt.GetHour() + 60*dt.GetMinute();
		}
	}

	void addSimpleEntry(vector<Ftp::FtpDirectoryListEntry>& filelist, const string& stEntry, const CrDateTime& now) {
		Ftp::FtpDirectoryListEntry entry;
		entry.name = stEntry;
		fillDateTime(now, entry, true);
		filelist.push_back(entry);
	}
}

                 
void Ftp::addEntry(vector<Ftp::FtpDirectoryListEntry>& filelist, const string& stEntry) {
#ifdef _DEBUG
                                               
#endif
	if ( stEntry.empty() )
		return;

	                                                                                        
	                                                                                                              
	boost::scoped_array<char> pEntry( new char[stEntry.size()] );
	memcpy(pEntry.get(), stEntry.c_str(), stEntry.size());
	struct ftpparse fp;
	const int retVal = kftpparse(&fp, pEntry.get(), stEntry.size());
#ifdef _DEBUG
                                                                       
                                            
#endif
	if ( retVal== 1 && fp.name != 0 && fp.namelen > 0 ) {
		if ( fp.flagtryretr == 0 )
			return;	                                                                                          
		boost::scoped_array<char> pName( new char[fp.namelen+1] );
		pName[0] = 0;
		memcpy(pName.get(), fp.name, fp.namelen);
		pName[fp.namelen] = 0;
		                                                            
		                                
		if ( strcmp(pName.get(), ".") == 0 || strcmp(pName.get(), "..") == 0 )
			return;

		Ftp::FtpDirectoryListEntry entry;
		entry.name = pName.get();
		if ( fp.sizetype != FTPPARSE_SIZE_UNKNOWN )
			entry.size = fp.size;
		else
			entry.size = 0;

		if ( fp.mtimetype != FTPPARSE_MTIME_UNKNOWN ) {
			CrDateTime dt(fp.mtime);
			if ( fp.mtimetype == FTPPARSE_MTIME_LOCAL ) {
				fillDateTime(dt, entry, true);
			}
			else if ( fp.mtimetype == FTPPARSE_MTIME_REMOTEMINUTE ) {
				fillDateTime(dt, entry, false);
			}
			else if ( fp.mtimetype == FTPPARSE_MTIME_REMOTEDAY ) {
				fillDateTime(dt, entry, false);
				entry.seconds = 0;
			}
		}

		if ( fp.idtype == FTPPARSE_ID_FULL ) {
			boost::scoped_array<char> pId( new char[fp.idlen+1] );
			memcpy(pId.get(), fp.id, fp.idlen);
			pId[fp.idlen] = 0;
			entry.id = pId.get();
		}

		filelist.push_back(entry);
	}
	else {
		                      
#ifdef _DEBUG
		std::cout<<"Failed to parse FTP directory entry"<<std::endl<<stEntry<<std::endl;
#endif
	}
}

long Ftp::GetDirectoryList(vector<FtpDirectoryListEntry>& filelist, bool bListFileNamesOnly) {
	long lErr = doPreTransfer();
	if ( lErr != Protocol::SUCCESS )
		return lErr;

	try {
		filelist.clear();
		const string& stResult = m_pTransferLib->getFtpDirectoryListing(bListFileNamesOnly);
#ifdef _DEBUG
                                                                                
                           
                                                             
#endif

		string::size_type iPos = 0;
		string::size_type iBegin = 0;
#ifdef _DEBUG
                                                                                              
                                                                                                                
#endif
		const char feed_char[]="\r\n";
		const CrDateTime& now = CrDateTime::GetCurrentTime();
		while ( (iBegin!=string::npos) &&
			    (iPos=stResult.find_first_of(feed_char, iBegin))!=string::npos )
		{
			const string& stEntry = stResult.substr(iBegin, iPos-iBegin);
			if ( !stEntry.empty() ) {
				if ( bListFileNamesOnly ) {
					addSimpleEntry(filelist, stEntry, now);
				}
				else {
					addEntry(filelist, stEntry);
				}
			}
			iBegin = stResult.find_first_not_of(feed_char, iPos);
		}
#ifdef _DEBUG
                                                              
#endif

		return Protocol::SUCCESS;
	}
	catch ( const CrException& ex ) {
		AddError(ex.GetErrorString());
		string str;
		AppendRequestAndResponseHeadersToString(str);
		AddError(str);
		m_lLastError = ex.GetErrorNumber();
		return Protocol::FAILURE;
	}
}

void Ftp::setFtpUploadRenameFileNames(const string& stOldFileName, const string& stNewFileName) {
	m_stRenameFromFileName = stOldFileName;
	m_stRenameToFileName = stNewFileName;
}

long Ftp::RenameFile(const std::string& stOldName, const std::string& stNewName) {
	long lErr = doPreTransfer();
	if ( lErr != Protocol::SUCCESS )
		return lErr;

	try {
		m_pTransferLib->ftpRename(stOldName, stNewName);
		return doPostTransfer();
	}
	catch( const CrException& ex ) {
		AddError("Failed to rename file. " + ex.GetErrorString());
		string str;
		AppendRequestAndResponseHeadersToString(str);
		AddError(str);

		m_lLastError = -1;
		return Protocol::FAILURE;
	}
}

