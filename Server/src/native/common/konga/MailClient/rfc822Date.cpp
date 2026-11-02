/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifdef WIN32
	#include <stdlib.h>	        
	#include <windows.h>		
	#include <sstream>	               
	#include <iomanip>	                
#endif

#include <time.h>		         

#include "rfc822Date.h"

using namespace std;

        
                                                                                          
                                                  
                                                                

                                                                       
string konga::rfc822Date() {
	                                               
	time_t tnow = 0;
	time(&tnow);
	struct tm tmloc;
	memset(&tmloc, 0, sizeof(struct tm));

#ifdef WIN32
	                                          
	struct tm *ptmloc = localtime(&tnow);
	tmloc = *ptmloc;

#else
	localtime_r(&tnow, &tmloc);
#endif

	char buf[100];

	                                                                         
	                                        
#ifdef WIN32
	strftime(buf, 100, "%a, %d %b %Y %H:%M:%S", &tmloc);
	stringstream ssBuf;
	ssBuf << buf;

	                                             
	TIME_ZONE_INFORMATION tzInfo;
	memset(&tzInfo, 0, sizeof(tzInfo));
	::GetTimeZoneInformation(&tzInfo);

	long bias = tzInfo.Bias + tzInfo.DaylightBias;
	if ( bias != 0 ) {
		const string sSign( bias < 0 ? "" : "-" );	                         
		bias = abs(bias);
		const long h = bias/60;
		const long m = bias - h*60;

		ssBuf << " " << sSign;
		ssBuf << setfill('0') << setw(2) << h;
		ssBuf << setfill('0') << setw(2) << m;
	}
	return ssBuf.str();
#else
	strftime(buf, 100, "%a, %d %b %Y %H:%M:%S %z", &tmloc);
	return string(buf);
#endif
}

