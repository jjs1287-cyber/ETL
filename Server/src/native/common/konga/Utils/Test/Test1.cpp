/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <iostream>

#include <konga/Utils/RCStringTable.h>
#include <konga/Utils/KString.h>
#include <konga/Utils/Random.h>

#include <konga/Utils/PortDefs.h>	                                                           
#include <GetPot>

#include <konga/Utils/CrException.h>


using namespace std;
using namespace konga;


int testRandom() {
	cout << "getRandMax() returns " << getRandMax() << endl;
	double sum = 0;
	double mean = 0;
	const double max = getRandMax();
	double d = 0;
	for( d = 1; d < 50; d+=1.0 ) {
		sum += getRandomNumber();
		mean = sum/d;
		double diff = mean - max/2.0;
		cout << diff << endl;
	}

	const char input[] = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890!@#$%^&*()_+-={}[]:;<>,.?/\\|~";
	for ( int i = 0; i < 1000; ++i ) {
		cout << getRandomString(16,36,input) << endl;
	}

	return 0;
}

int testRCStringTable(){
    using namespace konga;
    RCStringTable rcs;
    std::map<int, std::string> mapFormat;
    if(rcs.Parse("/home/frank/dev/Integration/src/konga/TransformationEngine/ErrorCodes/ErrorCodes.rc",mapFormat)==false){
        printf("%s\n",rcs.getErrorString().c_str());
    } else {
        for(std::map<int, std::string>::iterator it=mapFormat.begin(); it!=mapFormat.end();it++){
            printf("%.10d\"%s\"\n", it->first, it->second.c_str());
        }
        
    }
	return 0;
}

int testKString() {
	KString test("012345678987");
	int res = test.Find('6');
	cout << "Found at " << res << endl;

	res = test.Find("21");
	if ( res == -1 ) {
		cout << "Not found" << endl;
	}
	else
		cout << "Bug..." << endl;

	res = test.FindOneOf("21");
	if (res != -1) {
		cout << "found at " << res << endl;
	}
	else
		cout << "Bug..." << endl;

	res = test.ReverseFind('8');
	if (res != -1) {
		cout << "found at " << res << endl;
	}
	else
		cout << "Bug..." << endl;

	char* buf = test.GetBuffer(20);
	cout << buf << endl;

	buf = test.GetBuffer(30);

	const char pool[] = "jkfsdnkdsffdsksdfjipwlnknkl89489489743897";
	string str = konga::getRandomString(11000, 12000, pool);
	KString kstr;
	kstr.Format("Some random string: %s", str.c_str());

	kstr.Replace('f', 'g');

	return 0;
}

int testGetPot() {
#ifdef WIN32
	string stFileName("/tmp/konga.conf");
#else
	string stFileName("/usr/local/konga/konga.conf");
#endif
	GetPot ifile(stFileName.c_str());
	string st = ifile("Settings/XMLSchemas/WSDL", "not found...");
	cout << st << endl;

	return 0;
}
