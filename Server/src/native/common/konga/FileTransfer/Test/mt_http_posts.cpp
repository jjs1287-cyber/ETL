/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <memory>	                
#include <vector>
#include <iostream>
#include <string>

#include <boost/thread/thread.hpp>
#include <boost/thread/mutex.hpp>

#include <konga/Utils/CrException.h>
#include <konga/Utils/FileUtils.h>
#include <konga/Utils/util.h>
#include <konga/Utils/ProcessUtils.h>

#include <konga/Utils/util.h>

#include <konga/FileTransfer/Http.h>

using namespace konga;
using std::vector;
using std::string;
using boost::thread;


namespace {

	boost::mutex g_write_mx;

                                                                          
	const string origFile("C:/tmp/soap_request.xml");	                 
	class RunThis {
	public:
		RunThis(int i) : m_iThreadId(i) {}
		~RunThis() {}

		void operator()() {
			Clock cl;
			std::cout << "Thread " << m_iThreadId << " started" << std::endl;
			const string tmpFile("C:/tmp/" + konga::GetGUIDString());
			kCopyFile(origFile, tmpFile);

			           
                                                                                                    
                                                                                                     
                                                                                                    
                                                                                                    

			                  
                                                                                                    
			         
                                                                                                    
			const string url("http://localhost:8888/etlforge_ws/?id=c566551e-0884-4ea3-b429-0f61175257c5");

			Http http(0);
			HttpOptions options;
			options.setContentType("text/xml; charset=\"utf-8\"");
			http.SetHttpOptions(options);
			http.SetUrl(url);
			http.SetHttpVerb(konga::HttpPost);
			string sResponse;
			std::cout << "Thread " << m_iThreadId << " about to post" << std::endl;
			if ( http.PutAsFileGetRespAsString(tmpFile, sResponse) != Protocol::SUCCESS ) {
				std::cerr << http.GetLastError() << std::endl;
			}
			
			kDeleteFile(tmpFile);

			boost::mutex::scoped_lock lock(g_write_mx);

			std::cout << "Thread " << m_iThreadId << " finished: " << cl.getSeconds() << std::endl;

		}

	private:
		int m_iThreadId;
	};
}

namespace {
	int sendBurst(const int Nthreads) {
		try {
			vector<thread*> vThreads;
			for( int i = 0; i < Nthreads; ++i ) {
				RunThis f(i);
				thread* p = new boost::thread(f);
				if ( p == 0 )
					throw CrException("There is not enough memory left to create a new thread.");
				vThreads.push_back(p);
			}

			for( vector<thread*>::iterator it = vThreads.begin(); it != vThreads.end(); ++it ) {
				thread* p = *it;
				p->join();
				delete p;
			}
		}
		catch( const CrException& e ) {
			std::cout << e.GetErrorString() << std::endl;
			return 1;
		}
		catch( const boost::thread_resource_error& e ) {
			std::cout << "Not enough resources to create a new thread: " << e.what() << std::endl;
			return 1;
		}
		catch( const std::exception& e ) {
			std::cout << "Failed to create a new thread: " << e.what() << std::endl;
			return 1;
		}
		return 0;
	}
}

int testMTHTTP() {
	const int Nbursts = 1;
	for( int i =0; i < Nbursts; ++i ) {
		Clock cl;
		const int retVal = sendBurst(8);
		if ( retVal != 0 ) {
			return retVal;
		}

		std::cout << "Total clock time: " << cl.getSeconds() << std::endl;
		konga::MilliSleep(500);
	}

	return 0;
}


