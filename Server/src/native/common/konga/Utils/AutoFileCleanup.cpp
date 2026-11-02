/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <algorithm>
#include <cassert>
#include <exception>

#ifdef _DEBUG
#include <iostream>
#endif

#include "CrException.h"
#include "FileUtils.h"
#include "AutoFileCleanup.h"

using namespace konga;
using std::string;
using std::vector;

AutoDeleteFileSet::~AutoDeleteFileSet(void)
{
	if ( !m_bAutoDelete )
		return;

	vector<string>::const_iterator citFile = m_vstFiles.begin();
	for(; citFile != m_vstFiles.end(); ++citFile )
	{
		try
		{
			                        
			DeleteFileWithRetries(*citFile, 10, 50);
			if( m_bDeleteDir )
				kRemoveDirectory( GetBranchPath(*citFile) );

		                                                                    
		                                                       
		}
		catch(CrException& e)
		{
			const string s(e.GetErrorString());	                                        
			#ifdef _DEBUG
				std::cerr << e.GetErrorString() << std::endl;
				assert(!e.GetErrorString().c_str());
			#endif
		} 
		catch( const std::exception& say)
		{
			const string s(say.what());                                          
			#ifdef _DEBUG
				std::cerr << say.what() << std::endl;
				assert(!say.what());
			#endif
		}
		catch(...)
		{
			#ifdef _DEBUG
			assert(!"Unhandled Exception in ~AutoDeleteFileSet()");
			#endif
		}
	}
}

void AutoDeleteFileSet::AddFile(const std::string& stFile) {
	                         
	if ( std::find(m_vstFiles.begin(), m_vstFiles.end(), stFile) == m_vstFiles.end() )
		m_vstFiles.push_back(stFile);
}

void AutoDeleteFileSet::RemoveFile(const std::string& stFile) {
	vector<string>::iterator removeMe = std::find(m_vstFiles.begin(), m_vstFiles.end(), stFile);
	if ( removeMe != m_vstFiles.end() )
		m_vstFiles.erase(removeMe);
}

AutoCleanupTempDir::~AutoCleanupTempDir(void)
{
	if ( !m_bAutoDelete )
		return;

	try {
		if ( IsDirectory(m_stDirName) )
			kRemoveDirectory(m_stDirName);
	}
	catch( const CrException& e) {
		                                                                    
		                                                       
		const string s(e.GetErrorString());                                        
		#ifdef _DEBUG
			std::cerr << e.GetErrorString() << std::endl;
			assert(!e.GetErrorString().c_str());
		#endif
	}
	catch( const std::exception& say) {
		const string s(say.what());                                          
		#ifdef _DEBUG
			std::cerr << say.what() << std::endl;
			assert(!say.what());
		#endif
	}
	catch(...)
	{
		#ifdef _DEBUG
		assert(!"Unhandled Exception in ~AutoCleanupTempDir()");
		#endif
	}
}

