/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef SMTP_PRIORITY_H
#define SMTP_PRIORITY_H

namespace konga {
	enum smtpPriority {
		prHighest=1,
		prHigh=2,
		prNormal=3,
		prLow=4,
		prLowest=5
	};

	namespace {
	                                                                             
		smtpPriority GetPriorityFromLevel(const std::string& stLevel) {
			if ( stLevel == "Lowest" )
				return prLowest;
			else if ( stLevel == "Low" )
				return prLow;
			else if ( stLevel == "Normal" )
				return prNormal;
			else if ( stLevel == "High" )
				return prHigh;
			else if ( stLevel == "Highest" )
				return prHighest;
			else
				return prNormal;
		}
	}

}             

#endif                   

