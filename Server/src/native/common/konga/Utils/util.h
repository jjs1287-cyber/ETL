/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#ifndef KONGA_UTIL_H
#define KONGA_UTIL_H

#include <time.h>		          
#include <string>

namespace konga {

	                                                                        
	std::string getEnvironmentVariableValue(const std::string& name);

	void setEnvironmentVariableValue(const std::string& name, const std::string& value);

	                                                                         
	                             
	std::string getAbsolutePath(const std::string& stRelativePath, bool bThrowIfNotExisting = false);

	                                                                                  
	std::string getRelativePath(const std::string& stAbsolutePath);

	                    
	std::string GetGUIDString();
	void GetGUIDString(std::string& stGuid);

	                                                                            
	bool IsGUIDString(const std::string& stGuid);

	bool shouldThrowOnError();

	class Clock {
	public:
		Clock(void);
		~Clock() {}

		                                                                          
		                                                                                                   
		double getSeconds() const;

	private:
		clock_t m_startClock;
	};

	bool isNumeric(const char*str);

	                                                                                                              
	                                                                                  
	double roundDouble(double d);

}                   

#endif                

