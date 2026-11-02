/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_SAMBA_H
#define KONGA_SAMBA_H

#include <sys/stat.h>
#include <string>
#include <vector>

namespace konga {

#ifdef WIN32
	namespace SambaImpl {
		class AutoCreateSession;
	}
#endif

	   
                                                                                                
                                                                                                
                                                                                         
                                                                      
    
                                                                      
                                                                                                                  
    
	class Samba {
	public:
		                                                                                                                   
		                                                                                                          
		                                                                                                                    
		                                          
		Samba(bool bCheckIfTargetDirectoryExists, bool bPreserveReadOnlyFlagOnWindows);
		~Samba(void);

		enum SmbDirentryType {
			SMB_WORKGROUP=1,
			SMB_SERVER=2, 
			SMB_FILE_SHARE=3,
			SMB_PRINTER_SHARE=4,
			SMB_COMMS_SHARE=5,
			SMB_IPC_SHARE=6,
			SMB_DIR=7,
			SMB_FILE=8,
			SMB_LINK=9
		};

		class SmbDirectoryEntry {
		public:
			SmbDirectoryEntry(unsigned int itype, const std::string& stName, struct stat s)
			: type(static_cast<SmbDirentryType>(itype)), size(s.st_size), name(stName), ctime(s.st_ctime) {
			}
			~SmbDirectoryEntry(void) {
			}

			int day() const;
			int month() const;
			int time() const;

			bool isFile() const {
				return ( type == SMB_FILE );
			}

			SmbDirentryType type;
			long size;
			std::string name;

		private:
			time_t ctime;
		};

		                                           
		void setServer(const std::string& stServer);

		                                          
		void setShare(const std::string& stShare);

		                                                                  
                                                                               
                                                                                                                 
     
                                                        
     
		std::string setUNCPath(const std::string& stPath);

		                                                 

		                           
		void setWorkGroup(const std::string& stWorkGroup);
		                                                                                         
		void setUserName(const std::string& stUserName);
		void setPassword(const std::string& stPassword);

		                       

		std::string getServer() const;
		std::string getShare() const;

		std::string getWorkGroup() const;
		std::string getUserName() const;
		std::string getPassword() const;

		                                                        

		                                                                         
                                                                                                          
                                                                                                  
                        
     
		void GetUncDirectoryListing(const std::string& stUncPath, std::vector<SmbDirectoryEntry>& dirList, int nMaxFiles);

		                                                                                   
		void GetShareDirectoryListing(const std::string& stRemoteDirectory, std::vector<SmbDirectoryEntry>& dirList, int nMaxFiles) const;

		                                                                                               
		                               
		void CopyFromUncToLocalFile(const std::string& stUncPath, const std::string& stLocalFile);

		                                                                                               
		                               
		void CopyFromLocalFileToUnc(const std::string& stLocalFile, const std::string& stUncPath);

		                                                                                               
		                               
		void CopyFromUncToLocalString(const std::string& stUncPath, std::string& stLocalString);

		                                                                                               
		                               
		void CopyFromLocalStringToUnc(const std::string& stLocalString, const std::string& stUncPath);

		                                                                                               
		                               
		void DeleteUnc(const std::string& stUncPath, bool bThrowOnNonExistingPath = false);

		                                                                                                  
		                                                                                           
		                                                                        
		                               
		void MoveUnc(const std::string& stOldUncPath, const std::string& stNewUncPath);

		                                                                                   
		                               
		void CopyFromShareToLocalFile(const std::string& stRemoteFile, const std::string& stLocalFile) const;

		                                                                                                 
		                               
		void CopyFromShareToLocalString(const std::string& stRemoteFile, std::string& stLocalString) const;

		                                                                       
		                               
		void CopyFromLocalFileToShare(const std::string& stLocalFile, const std::string& stRemoteFile) const;

		                                                                                              
		                               
		void CopyFromLocalStringToShare(const std::string& stLocalString, const std::string& stRemoteFile) const;

		                                                              
		                               
		void DeleteFileOnShare(const std::string& stRemoteFile, bool bThrowOnNonExistingFile = false) const;

		                                                                       
		                                                                       
		                               
		void MoveFileOnShare(const std::string& stOldName, const std::string& stNewName) const;

	private:
		void Connect() const;
		void Disconnect() const;

		bool doTargetDirectoryCheck() const;

	private:
		const bool m_bCheckIfTargetDirectoryExists;
		const bool m_bPreserveReadOnlyFlagOnWindows;

		mutable bool m_bTargetDirectoryKnownToExist;
		std::string m_stServer;
		std::string m_stShare;

		std::string m_stUserName;
		std::string m_stPassword;
		std::string m_stWorkGroup;

#ifdef WIN32
		mutable SambaImpl::AutoCreateSession* m_session;
#endif
	};

	   
                                                              
                                                                                  
                                                                                                  
                                    
    
         
    
                                                                                                 
                                                                                                  
    
                                
    
	void CopyFromUncToUnc(const std::string& stSourceUNC, Samba& source, const std::string& stTargetUNC, Samba& target);
}
#endif

