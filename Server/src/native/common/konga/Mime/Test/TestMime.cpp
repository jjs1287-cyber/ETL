/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#include <iostream>

#include <konga/Mime/Attachment.h>
#include <konga/Mime/CrMime.h>
#include <konga/Mime/Encoder.h>
#include <konga/Mime/mhtml.h>

using namespace std;
using namespace konga;

int main (int argc, char *argv[])
{
	CrMime m;


	Encoder e("String to encode");
	e.CBase64();
	cout << e.GetEncodedString() << endl;

	Attachment a("test.txt");

	Mhtml mhtm;

	return 0;
}

