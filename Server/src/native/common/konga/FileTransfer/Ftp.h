/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_FILE_TRANSFER_FTP_H
#define KONGA_FILE_TRANSFER_FTP_H

#include "InetBase.h"
#include <memory>
#include "../../../ssh/SshKeys.h"

namespace konga {

	                                     
	enum FtpTransferType {
		FTP_TRANSFER_TYPE_BINARY = 1,		                                                         
		FTP_TRANSFER_TYPE_ASCII = 2			                                                                               
	};

	                                    
	enum FtpSecurityOption {
		FTP_SECURITY_NONE = 0,			                                           
		FTP_SECURITY_TRY_SSL = 1,		                                                               
		FTP_SECURITY_SSL_CONTROL = 2,	                                                                                    
		FTP_SECURITY_SSL_ALL = 3,		                                                                           
		FTP_SECURITY_SFTP = 4,			                         
	};

	                                           
	enum FtpsAuthenticationOption {
		FTPS_AUTH_TLS = 0,	                                               
		FTPS_AUTH_SSL = 1	                                                
	};

	class Ftp : public InetBase {
	public:
		Ftp(void);
		virtual ~Ftp(void);

		struct FtpDirectoryListEntry {
			FtpDirectoryListEntry(void) : size(0), year(0), month(0), day(0), seconds(0) {}
			~FtpDirectoryListEntry(void) { }
			long size;
			int year;
			int month;
			int day;
			int seconds;	                          
			std::string name;	                    
			std::string id;		                                            
		};

		                                                                                    
		                                                                       
		                                                                             
		long GetDirectoryList(std::vector<FtpDirectoryListEntry>& vFileList, bool bListFileNamesOnly);

		                                                                                   
		                                  
		void setFtpUploadRenameFileNames(const std::string& stOldFileName, const std::string& stNewFileName);

		                                                                                    
		                                                                       
		long RenameFile(const std::string& stOldName, const std::string& stNewName);

		void configureSftp(const std::string& keyId, const std::string& fingerprint);

		void setTransferType(FtpTransferType type);
		FtpTransferType getTransferType() const;

		void setUsePassiveMode(bool bUsePassiveMode);

		               
		void setSecurityOption(FtpSecurityOption level);
		FtpSecurityOption getSecurityOption() const;

		void setPreferredFtpsAuth(FtpsAuthenticationOption auth);
		FtpsAuthenticationOption getPreferredFtpsAuth() const;

		                                                 
		void useImplicitFtps(bool useImplicitFtps);

		                                                                                 
		bool isSftp() const;

	public:
		static void addEntry(std::vector<Ftp::FtpDirectoryListEntry>& filelist, const std::string& stEntry);

	protected:
		virtual long CheckHeadersForErrors();
		virtual long ParseURL();
		virtual long doPreTransfer();
		virtual long doPrePut();

		bool usePassiveMode() const;
		bool useImplicitFtps() const;
		void setSSHKeyFileOptions();

		                         
		virtual bool isSslEnabled() const;

	private:
		std::shared_ptr<etlforge::ssh::KeyLease> m_sshKey;
		std::string m_sshFingerprint;
		bool m_bUsePassiveMode;
		bool m_bUseImplicitFtps;

		FtpTransferType m_TransferType;
		FtpSecurityOption m_SecurityOption;
		FtpsAuthenticationOption m_PreferredFtpsAuth;

		std::string m_stRenameFromFileName, m_stRenameToFileName;
	};
}
#endif                             

