/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_RENAME_FILE
#define KONGA_OPERATION_RENAME_FILE

#include <string>

namespace konga {

	class RenameFile {
	public:
		   
                                                                                                        
                                                                                                                                    
                                                                                                                    
                                                      
                                                      
                                                      
     
             
     
                                                                 
                                                                                                                   
     
		RenameFile(const std::string& stRenamePattern);
		~RenameFile(void);

		                                               
		bool empty() const;

		std::string getPattern() const;

		                                                 
		bool hasSubstitutions() const;

		                                            
		std::string getRenamedFileName(const std::string& stFileName) const;

	private:
		void init(const std::string& stRenamePattern);

	private:
		bool m_bHasFile, m_bHasExt, m_bHasDate, m_bHasTime, m_bHasUnique, m_bHasSubstitutions;
		std::string m_stPattern;
	};
}
#endif                               

