/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <iostream>
#include <vector>

#include <boost/thread.hpp>

#include <konga/Utils/CrException.h>

#include <konga/DataElement/DataElement.h>

#include <konga/OperationDataElements/OperationDataElementsManager.h>
#include <konga/OperationContext/DefaultOperationDataElementHolder.h>

using namespace konga;
using std::vector;

namespace {
	int createSession() {
		try {

			const UserInfo userInfo;
			long lSessionId = -1;
			DataElement* de0 = 0;
			{
			DefaultOperationDataElementHolder deHolderIn(userInfo);

			DataElement* de0 = deHolderIn.getOperationDataElement("de1");
			de0->setType(DataElement::t_string);
			de0->setValue("first data element");

			de0 = deHolderIn.getOperationDataElement("gåsapågen");
			de0->setType(DataElement::t_string);
			de0->setValue("Sex gäss på en öde ö");

			de0 = deHolderIn.getOperationDataElement("array de");
			de0->setType(DataElement::t_collection);

			de0->addToCollection( new DataElement(DataElement::t_string, "string data element") );
			de0->addToCollection( new DataElement(DataElement::t_bool, true) );

			OperationDataElementsManager mgr;
			lSessionId = mgr.AddDataElements(deHolderIn);
			if ( lSessionId < 1 ) {
				return 1;
			}
			std::cout << "Created Session " << lSessionId << std::endl;
			}

			DefaultOperationDataElementHolder deHolderOut(userInfo);

			{
			OperationDataElementsManager mgr;
			mgr.GetDataElements(deHolderOut, lSessionId);
			de0 = deHolderOut.getOperationDataElement("gåsapågen");
			int err;
			const KString& val = de0->GetString(err);
			std::cout << (const char*)val << std::endl;

			de0 = deHolderOut.getOperationDataElement("DE1");

                                   
			}
		}
		catch( const CrException& ex ) {
			std::cerr << ex.GetErrorString() << std::endl;
			return -1;
		}

		return 0;
	}

	const int defaultNThreads = 40;

}                       


int mt_sessions(int iThreads);

int main(int argc, char* argv[])
{
                   
	mt_sessions( argc > 1 ? atoi(argv[1]) : defaultNThreads );
	return 0;
}

class RunInThread {
public:
	RunInThread(int iThreadId) : m_iThreadId(iThreadId) {}
	~RunInThread() {}

	void operator()() {
		createSession();
	}

private:
	int m_iThreadId;
};

int mt_sessions(int Nthreads) {

	vector<boost::thread*> vThreads;
	for( int i = 0; i < Nthreads; ++i ) {
		vThreads.push_back( new boost::thread(RunInThread(i)) );
	}

	for( vector<boost::thread*>::iterator it = vThreads.begin(); it != vThreads.end(); ++it ) {
		boost::thread* p = *it;
		p->join();
		delete p;
		*it = 0;
	}
	vThreads.clear();

	return 0;
}

