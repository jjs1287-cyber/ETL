/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_REMOTE_EXCEPTION_H
#define KONGA_REMOTE_EXCEPTION_H

#include <konga/Utils/CrException.h>

namespace konga {
namespace networking {

class RemoteException : public CrException {
public:
	RemoteException(long errNum=CR_E_FAIL, const std::string& errStr="") : CrException(errNum, errStr) {}
	RemoteException(const std::string& errStr, long errNum = CR_E_FAIL) : CrException(errNum, errStr) {} 
};

}                   
}                        

#endif                            
