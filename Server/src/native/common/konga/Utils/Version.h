/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_UTILS_VERSION_H
#define KONGA_UTILS_VERSION_H

#include <string>

namespace konga {

	                                                                          
	                                                           
	class Version {
	public:
		                
		                     
		                     
		                       
		                                
		Version(int major, int minor, int patch, int build = 0, const std::string& stVersionComment = "");

		                                                          
		                                                    
		                                                                    
		                                                                                        
		                                                                         
		explicit Version(unsigned long VersionNumber);

		                                                      
		                                            
		                                                                       
		                                                       
		                                                              
		explicit Version(const std::string& stVersion);

		~Version(void) {}

		int GetMajor() const;

		int GetMinor() const;

		int GetPatchLevel() const;

		int GetBuildNumber() const;

		                                                                       
		                                          
		std::string GetShortVersionString() const;

		                                                                                    
		std::string GetLongVersionString() const;

		                                                                                                             
		std::string GetVersionString(int nAtoms) const;

		std::string GetVersionComment() const;

		                                                           
		unsigned long GetVersionNumber() const;

	private:

		                                                       
		                                                
		void fromVersionAtoms(int major, int minor, int patch, int build);

		                          
		                                                  
		void fromVersionString(const std::string& stVersion);

		                                                                                 
		unsigned long m_ulVersionNumber;
		int m_iBuild;
		std::string m_stVersionComment;
	};

	                                   
	  
	bool operator==(const Version& lhs, const Version& rhs);
	bool operator!=(const Version& lhs, const Version& rhs);

	bool operator<(const Version& lhs, const Version& rhs);
	bool operator<=(const Version& lhs, const Version& rhs);

	bool operator>(const Version& lhs, const Version& rhs);
	bool operator>=(const Version& lhs, const Version& rhs);
}
#endif                         
