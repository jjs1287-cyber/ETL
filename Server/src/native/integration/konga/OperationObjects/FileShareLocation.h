/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_FILE_SHARE_LOCATION_H
#define KONGA_OPERATION_FILE_SHARE_LOCATION_H

#include <string>
#include <vector>

#include <konga/FileTransfer/Samba.h>

#include "DataLocation.h"

namespace konga {

	class FileShareLocation : public DataLocation {
	public:
		FileShareLocation(void) {
		}
		virtual ~FileShareLocation(void) {
		}

	protected:
		std::string getUncPath() const;

		std::string getUserName() const {
			return replace_de( m_CommonData.m_stLogin );
		}

		                                                       
		void initSmb(Samba& smb) const;

		typedef Samba::SmbDirectoryEntry direntry;
		typedef std::vector<direntry> vdirentry;

		void GetSmbDirectoryList(Samba& smb, vdirentry& dirlist, int nMaxFiles) const;
	};
}
#endif                                         

