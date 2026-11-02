/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_WS_FILE_MANAGEMENT_IMPL_H
#define ETLFORGE_WS_FILE_MANAGEMENT_IMPL_H

#include <konga/UiBackend/WebServices/Utils/WsImplBaseAuthenticator.h>

class CFileDownloadResult;
class CDownloadedFile;

namespace konga {

	class FileManagementImpl : public WsImplBaseAuthenticator {
	public:
		FileManagementImpl(const char* user, const char* password) :
		  WsImplBaseAuthenticator(user, password) {}

		CFileDownloadResult* downloadFile(const CDownloadedFile* const pRequestedFile);
	};

}                   

#endif                                      

