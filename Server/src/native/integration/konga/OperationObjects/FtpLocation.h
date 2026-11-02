/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_FTP_LOCATION_H
#define KONGA_OPERATION_FTP_LOCATION_H

#include <string>
#include <vector>

#include <konga/FileTransfer/Ftp.h>

#include "DataLocation.h"

namespace konga {

	                                                    
	class FtpLocation : public DataLocation {
	public:
		FtpLocation(void) {}
		virtual ~FtpLocation(void) {}

	protected:
		std::string getHost() const;

		long getPort() const;

		std::string getPath() const;

		std::string getUserName() const;
		
		bool useAsciiMode() const;

		bool usePassiveMode() const;

		FtpSecurityOption getFtpSecurityOption() const;

		bool useImplicitFtps() const;

		FtpsAuthenticationOption getFtpsAuthenticationOption() const;

		virtual long getTransferTimeOutSeconds() const = 0;

		                                 
		std::string initFtp(Ftp& ftp) const;

		typedef Ftp::FtpDirectoryListEntry direntry;
		typedef std::vector<direntry> vdirentry;
		void getFileListImpl(Ftp& ftp, vdirentry& filelist, bool bListFileNamesOnly) const;
	};
}
#endif                                  

