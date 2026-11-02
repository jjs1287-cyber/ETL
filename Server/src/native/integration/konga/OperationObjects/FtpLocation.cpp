/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>	             

#include <konga/FileTransfer/Ftp.h>

#include "OperationEngineException.h"
#include "FtpLocation.h"

using namespace std;
using namespace konga;

string FtpLocation::getHost() const {
	return replace_de( m_CommonData.m_stLocator );
}

long FtpLocation::getPort() const {
	return m_CommonData.m_lPort;
}

string FtpLocation::getPath() const {
	string stPath(m_CommonData.m_stPath);

	if ( stPath.empty() )
		return "/";

	if ( stPath.at(0) != '/' )
		stPath.insert(stPath.begin(), '/');

	if (stPath.length() > 1 && stPath.at(stPath.length()-1) != '/' ) {
		stPath += '/';
	}
	ReplaceCharWithChar(stPath, '\\', '/');
	return replace_de( stPath );
}

                                                                           
                        
string FtpLocation::initFtp(Ftp& ftp) const {
	ftp.setHost(getHost());
	const string& stPath = getPath();
	ftp.setPath(stPath);
	ftp.setPort(getPort());

	if ( useAsciiMode() )
		ftp.setTransferType(FTP_TRANSFER_TYPE_ASCII);

	ftp.setUsePassiveMode( usePassiveMode() );

	                                                    
	ftp.setSecurityOption( getFtpSecurityOption() );
	ftp.useImplicitFtps( useImplicitFtps() );
	ftp.setPreferredFtpsAuth( getFtpsAuthenticationOption() );
	ftp.SetTimeOutSec( getTransferTimeOutSeconds() );

	const string& stUserName = getUserName();
	if ( !stUserName.empty() ) {
		ftp.SetUserName(stUserName);
		ftp.SetPassword(m_extendedProperties.GetString("sftp_key_id", "").empty()?getDecryptedPassword():"");
	}
	if(ftp.isSftp()){try{ftp.configureSftp(m_extendedProperties.GetString("sftp_key_id", ""),m_extendedProperties.GetString("sftp_host_sha256", ""));}catch(const CrException& ex){throw OperationEngineException(ex.GetErrorString());}}
	return stPath;
}

void FtpLocation::getFileListImpl(Ftp& ftp, vdirentry& filelist, bool bListFileNamesOnly) const {
	if ( ftp.GetDirectoryList(filelist, bListFileNamesOnly) ) {
		throw OperationEngineException(ftp.GetLastError());
	}
}

string FtpLocation::getUserName() const {
	return replace_de( m_CommonData.m_stLogin );
}

bool FtpLocation::useAsciiMode() const {
	return !m_extendedProperties.GetBool("compression_enabled",false) && !m_extendedProperties.GetBool("pgp_enabled",false) && m_CommonData.m_lTransferType == 1;
}

bool FtpLocation::usePassiveMode() const {
	return m_CommonData.m_iPassiveMode == 1;
}

FtpSecurityOption FtpLocation::getFtpSecurityOption() const {
	if ( m_CommonData.m_lEncryptChoice < 0 || m_CommonData.m_lEncryptChoice > 4 )
		throw OperationEngineException("Internal Error: Encrypt Choice (FTP Security Option flag) must be between 0 and 4.");
	else {
		return static_cast<FtpSecurityOption>(m_CommonData.m_lEncryptChoice);
	}
}

bool FtpLocation::useImplicitFtps() const {
	const int i = m_extendedProperties.GetInt("ftps_explicit", 1);
	return i == 0;
}

FtpsAuthenticationOption FtpLocation::getFtpsAuthenticationOption() const {
	const int i = m_extendedProperties.GetInt("ftps_auth", 0);
	if ( i == 1 )
		return FTPS_AUTH_SSL;
	return FTPS_AUTH_TLS;
}

