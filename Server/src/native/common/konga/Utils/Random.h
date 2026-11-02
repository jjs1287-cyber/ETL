/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef ETLFORGE_UTILS_RANDOM_H
#define ETLFORGE_UTILS_RANDOM_H

#include <string>

namespace konga {

	                                                                   
	int getRandMax();

	                                                                                   
	                                                                  
	int getRandomNumber();

	                                                                                
	                                                
	int getRandomNumber(int min, int max);

	                                                                              
	                                                                            
	                                       
	std::string getRandomString(int minLength, int maxLength, const char* input);
}

#endif                           

