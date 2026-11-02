/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <memory>	                
#include <string>
#include <exception>

#include <boost/thread/thread.hpp>

#include "CrException.h"
#include "ThreadUtils.h"

using namespace konga;
using std::string;

void konga::RunInNewThread(const boost::function0<void>& f) {
	try {
		boost::thread* p = new boost::thread(f);
		if ( p == 0 )
			throw CrException("There is not enough memory left to create a new thread.");

		std::auto_ptr<boost::thread> pThread(p);
		pThread->join();
	}
	catch( const boost::thread_resource_error& e ) {
		throw CrException(string("Not enough resources to create a new thread: ") + e.what());
	}
	catch( const std::exception& e ) {
		throw CrException(string("Failed to create a new thread: ") + e.what());
	}
}

