/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_ZIP_ZIP_UTILS_H
#define KONGA_ZIP_ZIP_UTILS_H

#include <string>

namespace konga {
	namespace ZipUtils {

		    
                                                                                                   
                                                  
                                                               
     
		unsigned char* Inflate(const unsigned char* pchDeflated, unsigned long lenDeflated,
			unsigned long& lenInflated);

		    
                                                                                                 
                                                  
                                                               
                                                                                        
                                                          
     
		unsigned char* Deflate(const unsigned char* pchInflated, unsigned long lenInflated,
			unsigned long& lenDeflated);

		                                              
		std::string ZipAndBase64Encode(const unsigned char* pchInflated, unsigned long lenInflated);

		                                                
		std::string ZipAndBase64Encode(const std::string& stData);

		                                                                                                     
		                                                           
		std::string DecodeUnzipAndWriteToFile(const std::string& stFileName, const char* pchZippedBase64EncodedString, bool bCalculateHash = true);

		                                                                                           
		                                                           
		                                                                                              
		std::string ZipAndBase64EncodeFile(const std::string& stFileName, char*& pcZippedBase64EncodedString, bool bCalculateHash = true);

		                                                                           
		void GzipFile(const std::string& stFileName);
	}
}

#endif                         

