/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_STACK_WALKER_H
#define KONGA_STACK_WALKER_H

#include <string>
#include <sstream>

#include "StackWalker.h"

namespace konga {

class KongaStackWalker : public StackWalker {
public:
	KongaStackWalker() : iFrame(0) {}
	
	virtual void OnCallstackEntry(CallstackEntryType eType, CallstackEntry &entry);
	std::string toString() { return m_str.str(); }

private:
	int iFrame;
	std::stringstream m_str;
};

}                   

#endif                        
