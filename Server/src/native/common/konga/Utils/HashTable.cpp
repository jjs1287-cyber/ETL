/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include "HashTable.h"
#include "PortDefs.h"

namespace konga {
unsigned int hashFunction_(const char* s){
	int f=0;
	const unsigned int mask=0xFFFF;
	for(int i=0;s[i]!=0;i++){
		f=(f*53+s[i])&mask;
	}
	return f;
}

bool equalFunction_(const char* s1, const char* s2){
	return strcasecmp(s1, s2)==0;
}



}                   
