/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef KONGA_FILE_UTILS_AUTO_FILE_CLEANUP_H
#define KONGA_FILE_UTILS_AUTO_FILE_CLEANUP_H

#include <cstdio>	                  

#include <vector>
#include <string>

#include <boost/noncopyable.hpp>

namespace konga {

	                                      
	                                                                          
	class AutoFileHandle : boost::noncopyable
	{
	public:
		AutoFileHandle(FILE*& h) : m_FILE(h) { }
		~AutoFileHandle(void) {
			Close();
		}

		void Close() {
			if ( m_FILE ) {
				fclose(m_FILE);
				m_FILE = 0;
			}
		}

	private:
		FILE*& m_FILE;
	};
	

	   
                                                                                        
                                                                                           
                                                                                              
                                                                                             
                        
                                                                                       
                                                                             
    
	class AutoDeleteFileSet : boost::noncopyable
	{
	public:
		AutoDeleteFileSet(bool bDeleteDir=false, bool bAutoDelete=true) : m_bDeleteDir(bDeleteDir), m_bAutoDelete(bAutoDelete)
		{ }
		virtual ~AutoDeleteFileSet(void);

		void AddFile(const std::string& stFile);

		                                                   
		void RemoveFile(const std::string& stFile);

		void SetAutoDelete(bool bAutoDelete) {
			m_bAutoDelete = bAutoDelete;
		}

	private:
		bool m_bDeleteDir;
		bool m_bAutoDelete;
		std::vector< std::string > m_vstFiles;
	};

	   
                                                  
                                                      
                                                                               
                           
    
	class AutoCleanupTempDir: boost::noncopyable
	{
	public:
		AutoCleanupTempDir(const std::string& stDirName, bool bAutoDelete=true)
			: m_bAutoDelete(bAutoDelete), m_stDirName(stDirName) {}
		virtual ~AutoCleanupTempDir(void);

		void SetAutoDelete(bool bAutoDelete) {
			m_bAutoDelete = bAutoDelete;
		}

	private:
		bool m_bAutoDelete;
		std::string m_stDirName;
	};

}

#endif                                        


