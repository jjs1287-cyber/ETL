/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#include <konga/Utils/CsvUtils.h>

#include "crom.h"
#include "LdapDataConverter.h"

void LdapDataConverter::writeCromElement(FILE*out, CROM*pTableNode){
	const char*identifier = pTableNode->get_Name();
	for(int i=0, c; (c=identifier[i])!=0; ++i)fputc(c, out);
	for(CROM*pChild=pTableNode->GetFirstChild(); pChild && pChild->IsLeaf();
		pChild=pChild->GetNextSibling()){
		fputc(',', out);
		if(!pChild->IsNull()){
			KString value(pChild->GetValue());
			if(pChild->IsNull())value="";
			konga::qualifyCsvStringK(value, ',');
			const char*v=(const char*)value;
			for(int i=0, c; (c=v[i])!=0; ++i)fputc(c, out);
		}
	}
	fputc('\n', out);
}

void LdapDataConverter::readRecord(FILE*in, CROM*pTableNode){
}
