/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#ifndef TRANSFORMATION_ENGINE_FILE_TYPE_TARGET_CACHE_H
#define TRANSFORMATION_ENGINE_FILE_TYPE_TARGET_CACHE_H

#include <stdio.h>	        

#include <boost/noncopyable.hpp>

#include <boost/thread/mutex.hpp>

#include <konga/ItemAccess/UserInfo.h>

#include <string>
#include <map>

namespace konga {

	class FileTypeTargetLocation;
	class OperationDataElementHolder;
	class DataElement;

	class TargetKey {
	public:
		TargetKey(long lTargetId, const std::string& sFileName);
		~TargetKey(void) {};

		long getTargetId() const {
			return m_lTargetId;
		}

		std::string getOverrideTargetName() const {
			return m_sOverrideTargetName;
		}

	private:
		const long m_lTargetId;
		const std::string m_sOverrideTargetName;
	};

	class TargetBuffer : boost::noncopyable {
	public:
		TargetBuffer(const UserInfo& currentUser, FileTypeTargetLocation& targetLocation, const std::string& sFileName);
		~TargetBuffer(void);

		                                                         
		void WriteToBuffer(const DataElement& contents);

		                                                
		void FlushBuffer();

		long getTargetId() const;

	private:
		FILE* m_pBuffer;
		FileTypeTargetLocation& m_target;
		std::string m_sFileName;
		std::string m_sLocalTargetFile;
		boost::mutex m_AccessMutex;	                                        
		const UserInfo m_CurrentUser;
	};

	                                                                                          
	                     
	class FileTypeTargetCache : boost::noncopyable {
	public:
		FileTypeTargetCache(void) {}
		~FileTypeTargetCache(void);

		                                                                           
		                                                      
		                                                                                      
		                                           
		void WriteToBuffer(const UserInfo& currentUser,
			const std::string& stTargetId,
			const std::string& sOverrideTargetName,
			OperationDataElementHolder* pDEHolder,
			const DataElement& contents);

		                                                           
		                                                     
		void FlushBuffer(const UserInfo& currentUser, const std::string& stTargetId,
			const std::string& sOverrideTargetName);

		                                                                            
		                                                                             
		                   
		void FlushAll(const UserInfo& currentUser, const std::string& stTargetId = "");

	private:
		FileTypeTargetLocation* getTargetLocation(const UserInfo& currentUser, const std::string& stTargetId, OperationDataElementHolder* pDEHolder, bool bCreateNonExisting = true);
		TargetBuffer* getTargetBuffer(const UserInfo& currentUser, FileTypeTargetLocation& target, const std::string& sOverrideTargetName, bool bCreateNonExisting = true);

	private:
		                                             
		typedef std::map<TargetKey, TargetBuffer*> TargetBufferT;
		TargetBufferT m_TargetBuffers;

		                                   
		typedef std::map<std::string, FileTypeTargetLocation*> CachedTargetsT;
		CachedTargetsT m_CachedTargets;
		boost::mutex m_CacheMutex;	                                                            
	};
	
	                                                                   
	bool operator<(const TargetKey& lhs, const TargetKey& rhs);
}

#endif                                                  

