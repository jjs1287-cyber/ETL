/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_UPLOADED_FILE_H
#define KONGA_UPLOADED_FILE_H

#include <time.h>		         

#ifdef WIN32
	#include <windows.h>
#endif
#include <sqltypes.h>	                   

#include <string>

#include <konga/Utils/DbTypeUtils.h>	                   

#include "UploadedFileTypes.h"

namespace konga {

class UploadedFile {
public:
	UploadedFile(const std::string& sPath, const std::string sName, const std::string& sGuid,
		UploadedFileType iFileType, const std::string sMd5, const TIMESTAMP_STRUCT& modifiedDate) :
		m_sPath(sPath), m_sName(sName), m_sGuid(sGuid), m_sMd5(sMd5), m_iFileType(iFileType),
		m_modifiedDate(modifiedDate) {}

	                                                    
	UploadedFile(const std::string& sPath, const std::string sName, const std::string& sGuid,
		UploadedFileType iFileType, const std::string sMd5, time_t modifiedDate) :
		m_sPath(sPath), m_sName(sName), m_sGuid(sGuid), m_sMd5(sMd5), m_iFileType(iFileType) {
			setTimeStruct(m_modifiedDate, modifiedDate);
		}

	                                                                     
	UploadedFile(const std::string& sPath = "", const std::string sName = "", const std::string& sGuid = "",
		UploadedFileType iFileType = INVALID_FILE_TYPE, const std::string sMd5 = "") : m_sPath(sPath), m_sName(sName), m_sGuid(sGuid),
		m_sMd5(sMd5), m_iFileType(iFileType)
	{
		memset(&m_modifiedDate, 0, sizeof m_modifiedDate);
	}

	virtual ~UploadedFile() {}

	std::string GetPath() const { return m_sPath; }
	std::string GetName() const { return m_sName; }
	std::string GetGuid() const { return m_sGuid; }
	std::string GetMd5() const { return m_sMd5; }
	UploadedFileType GetFileType() const { return m_iFileType; }
	TIMESTAMP_STRUCT GetModifiedDate() const { return m_modifiedDate; }
	time_t GetModifiedDateAsLong() const;

	void SetPath(const std::string& sPath) { m_sPath = sPath; }
	void SetName(const std::string& sName) { m_sName = sName; }
	void SetGuid(const std::string& sGuid) { m_sGuid = sGuid; }
	void SetMd5(const std::string& sMd5) { m_sMd5 = sMd5; }
	void SetFileType(UploadedFileType iFileType) { m_iFileType = iFileType; }
	void SetModifiedDate(const TIMESTAMP_STRUCT& modifiedDate) { m_modifiedDate = modifiedDate; }
	void SetModifiedDate(time_t t) {
		konga::setTimeStruct(m_modifiedDate, t);
	}

	void SetModifiedNow();

	                                                                      
	                                           
	                                                                                         
	std::string GetCanonicalPath() const;

	                                                                 
	std::string GetFullAbsolutePath() const;

	bool FileExistsOnDisk() const;

private:
	std::string m_sPath;
	std::string m_sName;
	std::string m_sGuid;
	std::string m_sMd5;

	UploadedFileType m_iFileType;

	TIMESTAMP_STRUCT m_modifiedDate;
};

class ZippedUploadedFile : public UploadedFile {
public:
	ZippedUploadedFile(const std::string& sPath, const std::string sName, const std::string& sGuid,
		UploadedFileType iFileType, const std::string sMd5, 
		const TIMESTAMP_STRUCT& modifiedDate, const char* pchZippedBase64Contents = 0) : 
		UploadedFile(sPath, sName, sGuid, iFileType, sMd5, modifiedDate), m_pchZippedBase64Contents(pchZippedBase64Contents) {}

	ZippedUploadedFile(const std::string& sPath, const std::string sName, const std::string& sGuid,
		UploadedFileType iFileType, const std::string sMd5, time_t modifiedDate, const char* pchZippedBase64Contents = 0) : 
		UploadedFile(sPath, sName, sGuid, iFileType, sMd5, modifiedDate), m_pchZippedBase64Contents(pchZippedBase64Contents) {}

	ZippedUploadedFile(const std::string& sPath = "", const std::string sName = "", const std::string& sGuid = "",
		UploadedFileType iFileType = INVALID_FILE_TYPE, const std::string sMd5 = "", const char* pchZippedBase64Contents = 0) :
		UploadedFile(sPath, sName, sGuid, iFileType, sMd5), m_pchZippedBase64Contents(pchZippedBase64Contents) {}

	                                                                                   
	ZippedUploadedFile(const UploadedFile& uploadedFile) :
		UploadedFile(uploadedFile), m_pchZippedBase64Contents(0) {}
		
	virtual ~ZippedUploadedFile() {}

	const char* GetZippedBase64Contents() const { return m_pchZippedBase64Contents; }

	                                                                                                  
	void SetZippedBase64Contents(const char* pchZippedBase64Contents) {
		m_pchZippedBase64Contents = pchZippedBase64Contents;
	}

	ZippedUploadedFile& operator=(const UploadedFile& rhs);

private:
	const char* m_pchZippedBase64Contents;

};


}                   

#endif                         
