/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef UTILS_THREAD_UTILS_H
#define UTILS_THREAD_UTILS_H

#include <boost/function.hpp>

namespace konga {

	typedef void(*ThreadStartFunctionType)(void*);

	struct LegacyThreadFunction {
	                                                                                       
	                                                                                       
	                                                                                               
		LegacyThreadFunction(ThreadStartFunctionType pf, void* p) : pFunction(pf), pSomething(p) {}

		void operator()() {
			pFunction(pSomething);
		}

		ThreadStartFunctionType pFunction;
		void* pSomething;
	};

	                                                                              
	                                                                
	                                                                                       
	                                                    

	void RunInNewThread(const boost::function0<void>& f);

}                    

#endif                        

