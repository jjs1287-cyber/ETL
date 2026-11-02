/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_UTILS_FILE_LINE_READ_WRITER_H
#define KONGA_UTILS_FILE_LINE_READ_WRITER_H

namespace konga {

	   
                                                                            
                                                        
   
	class FileLineReadWriter {
	public:
		explicit FileLineReadWriter(const std::string& stTmpDir = "");
		virtual ~FileLineReadWriter(void);

		   
                                                                        
                                                                                 
                                                                                      
                                                                         
                                                                                    
                                                                                    
                                 
     
		virtual int Read(const char* filename);

	protected:
		class AbortRead {
		public:
			AbortRead(int iReason = -1) : m_iReason(iReason) {}
			virtual ~AbortRead(void) {}
			virtual int GetReason() const { return m_iReason; }

		private:
			int m_iReason;
		};

		   
                                                                           
                                             
                                                                              
                                                                     
                                                                                     
                                                                        
     
		virtual const char* ChangeLine(const char* line) = 0;

	private:
		std::string m_stTmpDir;
	};
}
#endif                                       
