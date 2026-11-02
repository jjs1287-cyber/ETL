/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_FILE_UTILS_H
#define KONGA_FILE_UTILS_H

#include <string>
#include <vector>

#ifdef kDeleteFile
	#undef kDeleteFile
#endif
#ifdef kMoveFile
	#undef kMoveFile
#endif
#ifdef kCopyFile
	#undef kCopyFile
#endif
#ifdef kCreateDirectory
	#undef kCreateDirectory
#endif
#ifdef kRemoveDirectory
	#undef kRemoveDirectory
#endif


namespace konga
{
	                                   
	                                                                                 
	bool FileExists(const std::string& stPath);
	                                                                          
	                                                                                 
	bool IsDirectoryEmpty(const std::string& stDirPath);

	                                                    
	                                                                                 
	bool NonEmptyFileExists(const std::string& stPath);
	bool IsDirectory(const std::string& stPath);
	long long GetFileSize(const std::string& stPath, long& lErr);

	std::string getModuleFileName();

	                                                                                
	                                                                                          
	                                 
	std::string getTmpDir();

	                                                                               
	                                                             
	std::string getSystemTmpDir();

	                                                          
	                                                                                      
	                                         
	  
	std::string getTmpFileName(const std::string& prefix="", const std::string& stTempDir="");

	                                          
	                                         
	                                   
	long CountLines(const std::string& fileName);

	                                                 
	                                                           
	void RemoveFirstAndLastLines(const std::string& fileName, long removeFirst, long removeLast);

	                                              
	                                                          
	                                                   
	                                                                                 
	void touch_file(const std::string& stFileName);

	                                               
	                                                        
	                                                                                 
	void truncate_file(const std::string& stFileName);

	                                                                                                     
	                                                   
	                                                                                                
	                                                                      
	                                                                       
                                                             

	                                                                                                     
	                                                                                                  
	                                              
	char* read_file(const std::string& stFilename, long& bytesRead, std::string& stErr, long maxBytesRead=0);

	                                                                  
	                                                                                                                     
	                               
	void read_file_to_string(const std::string& stFileName, std::string& stFileContents, bool bBinaryMode = true);

	                                                      
	                               
	                                                                                                                     
	void write_string_to_file(const std::string& stFileName, const std::string& stFileContents, bool bBinaryMode = true);

	                                
	void write_to_binary_file(const std::string& stFileName, const unsigned char* const pchContents, const unsigned long length);

	void ChangeWorkingDir(const std::string& stPath);

	                                                            
	                               
	void kCreateDirectory(const std::string& stPath);

	                                   
	                               
	void kRemoveDirectory(const std::string& stPath);

	                               
	                                                                                  
	void kDeleteFile(const std::string& stFileName);

	                                           
	void DeleteFileWithRetries(const std::string& stFileName, const int iMax, const int sleepMs);
	void RemoveDirectoryWithRetries(const std::string& stPath, const int iMax, const int sleepMs);

	                                              
	                                                                              
	                                                                               
	                                                                                                  
	                                                  
	                                                                                                   
	                               
	bool kMoveFile(const std::string& stFileName, const std::string& stToPath, bool bOverWrite = false);

	                                                                                                 
	                                                                                                     
	                                                                                                         
	                                            
	                               
    bool kCopyFile(const std::string& stFileName, const std::string& stToPath, bool bOverWrite = false);
	                                                          
	                                                                                                
	bool kAppendFile(const std::string& stFileName, const std::string& AppendingFileName, bool bOverWrite  = false ) ;

    void _tsplitpath(const char*path, std::string&drive, std::string&dir, std::string&fname, std::string&ext);

	                                                                               
	                                                                                             
	std::string GetBranchPath(const std::string& stFilePath);

	                                          
	                                                 
	std::string GetLeafPath(const std::string& stFilePath);

	std::string GetFileExtension(const std::string& stFilePath);

	                                                            
	                                                        
	std::string RemoveFileExtension(const std::string& stFilePath);

	                                                                                   
	std::string GetFileNameWithTagInserted(const std::string& stFileName, const std::string& stTag);

	                                                                                         
	                                               
	std::string GetSequenceFileName(const std::string& stFileName, const std::string& stTag, int iSequence);

	extern const char PathSeparator;

	void AppendPathSeparator(std::string& stPath);
	std::string AppendPathSeparator(const std::string& stPath);

	void RemovePathSeparator(std::string& stPath);

	                                                                                                     
	                                                                                                        
	           
	bool EquivalentPaths(const std::string& stPath1, const std::string& stPath2);

	void ftruncate(const std::string& fileName, size_t size);

}         

#endif                      

