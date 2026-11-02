/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#include <string>

#include <konga/Utils/FileUtils.h>		                                      
#include <konga/Utils/Settings.h>		                           
#include <konga/Utils/StringUtils.h>	                        
#include <konga/Utils/DateUtils.h>		             

#include "FileUploadUtils.h"
#include "UploadedFilesManager.h"		                        
#include "UploadedFile.h"

using namespace konga;
using namespace std;

time_t UploadedFile::GetModifiedDateAsLong() const {
	time_t t = konga::get_time_t(m_modifiedDate);
	if ( t < 0 ) t = 0;
	return t;
}

void UploadedFile::SetModifiedNow() {
	const time_t t = (CrDateTime::GetCurrentTime)().GetTime();
	this->SetModifiedDate(t);
}

                                                                         
                                                                                         
std::string UploadedFile::GetCanonicalPath() const { 
	string sPath(m_sPath);
	AppendPathSeparator(sPath);
	ReplaceCharWithChar(sPath, '\\', '/');
	return sPath;
}

std::string UploadedFile::GetFullAbsolutePath() const { 
	return	GetServerHomeWithSlash() + 
			GetUploadedFilesDir() + 
			GetCanonicalPath() + 
			GetName();
}

bool UploadedFile::FileExistsOnDisk() const {
	return FileExists(GetFullAbsolutePath());
}

ZippedUploadedFile& ZippedUploadedFile::operator=(const UploadedFile& rhs) {
	if( this == &rhs ) return *this;
	SetPath(rhs.GetPath());
	SetName(rhs.GetName());
	SetGuid(rhs.GetGuid());
	SetFileType(rhs.GetFileType());
	SetMd5(rhs.GetMd5());
	SetModifiedDate(rhs.GetModifiedDate());
	m_pchZippedBase64Contents = 0;
	return *this;
}
