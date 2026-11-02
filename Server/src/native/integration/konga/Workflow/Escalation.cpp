/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <string>

#include <konga/Utils/CrException.h>

#include "Escalation.h"

using std::string;

namespace konga {

                                                                                                          
int Escalation::GetTimeFromString(const string& stTime) {
	long lTime = atol(stTime.c_str());
	char cLastChar = *(stTime.end()-1);
	unsigned long lMultiplier = 1;
	switch(cLastChar) {
		case 'W':	        
			lMultiplier = 60*24*7;
			break;
		case 'D':	       
			lMultiplier = 60*24;
			break;
		case 'H':	        
			lMultiplier = 60;
			break;
		case 'M':	                          
			lMultiplier = 1;
			break;
		default:
			throw CrException("The time specification '" + stTime + 
				"' begins with a number, but it has an unknown or no unit. Valid units are: W,D,H, and M. ");
	}
	return lTime * lMultiplier;
}

                                                                                             
            
                                       
                       
void Escalation::SetMinutesToEscalation(const string& stMinutesToEscalation) {
	m_stMinutesToEscalation = "";
	m_nMinutesToEscalation = 0;

	char cFirstChar = *stMinutesToEscalation.begin();
	if( isdigit(cFirstChar) ) {
		                                     
		m_nMinutesToEscalation = GetTimeFromString(stMinutesToEscalation); 
	}
	else
		m_stMinutesToEscalation = stMinutesToEscalation;
}

string Escalation::GetMinutesToEscalationAsString() const { 
	if( m_nMinutesToEscalation > 0 ) return konga::toString(m_nMinutesToEscalation);
	else return m_stMinutesToEscalation;
}

}                   
