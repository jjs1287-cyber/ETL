/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef LDAPDATACONVERTER_H
#define LDAPDATACONVERTER_H

class CROM;

class LdapDataConverter
{
public:
	static void writeCromElement(FILE*out, CROM*pTableNode);
	static void readRecord(FILE*in, CROM*pTableNode);
};
#endif                       
