/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_JOBCONTROLIMPL_H
#define KONGA_JOBCONTROLIMPL_H

#include <string>

#include "../JobControl.hpp"

namespace konga {

                                                                      
                                                                       
                                   
class JobControlImpl {
public:
	JobControlImpl() {}

	                                                                         
	                                                               
	void RunOperation(const xsd__string& UserName, const xsd__string& Password, const xsd__string& OperationGuid);
	
	                                                                                      
	void RunOperationMD5Password(const xsd__string& UserName, const xsd__string& MD5Password, const xsd__string& OperationGuid);

	                             
	WsRunOperationResult* RunOperationWithSourceData(const xsd__string& UserName, const xsd__string MD5Password, const xsd__string& OperationGuid,
		const xsd__string& CompressedAndbase64EncodedSourceData);

	                                                      
	WsRunOperationResult* RunOperationInstanceMD5Password(const xsd__string& UserName, const xsd__string& MD5Password, const xsd__string& OperationGuid);

private:
	WsRunOperationResult* RunOperationImpl(const std::string& stUserName, const std::string& stMD5Password, const std::string& stOperationGuid,
		const xsd__string& CompressedAndbase64EncodedSourceData);
};

}

#endif                          
