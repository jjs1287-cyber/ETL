/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include <cassert>
#include <cmath>

#include <iostream>

#include <konga/Utils/CrException.h>
#include <konga/CharacterEncodingConversion/Utf8Conv.h>

#include "Utf8Tests.h"

using namespace konga;
using namespace std;

void printHtmlDocForEncodings(const std::string fileName) {
	vector<SupportedEncoding> encodings = GetSupportedEncodings();

	FILE* f = fopen(fileName.c_str(), "w+");
	if ( f == 0 ) return;

	fprintf(f, "<html>\n<head>\n<title>Supported Character Encodings</title>\n</head>\n<body bgcolor=\"#ffffff\">\n\n");
	fprintf(f, "<h1>Supported Character Encodings</h1>\n\n");

	fprintf(f, "<table cellpadding=\"1\" cellspacing=\"2\">\n");

	fprintf(f, "<tr><th align=\"left\">Character Encoding</th><th align=\"left\">Alias</th></tr>\n");

	string tmp;
	for( vector<SupportedEncoding>::const_iterator cit = encodings.begin(); cit != encodings.end(); ++cit ) {
		const SupportedEncoding& encoding = *cit;
		fprintf(f, "<tr>\n");

		tmp = "<td>" + encoding.canonical_name + "</td>\n";
		fprintf(f, tmp.c_str());
		fprintf(f, tmp.c_str());
		fprintf(f, "</tr>\n");

		const vector<string>& aliases = encoding.aliases;
		for( vector<string>::const_iterator citAlias = aliases.begin(); citAlias != aliases.end(); ++citAlias ) {
			fprintf(f, "<tr>\n");
			fprintf(f, "<td></td>\n");
			tmp = "<td>" + *citAlias + "</td>\n";
			fprintf(f, tmp.c_str());
			fprintf(f, "</tr>\n");
		}
	}

	fprintf(f, "</table>\n\n");
	fprintf(f, "</body>\n");
	fprintf(f, "</html>");

	fclose(f);
}


namespace {
	const string iso8859("ISO-8859-1");
	const string utf8("UTF-8");
	const string windows("windows-1252");
	const string usascii("us-ascii");

	string unicode_to_utf8(int from, int to, Utf8Conv& cnv) {
		double d, dd;
		const double zero = 0, sixteen = 16;

		wstring ws;
		for( int i = from; i <= to; ++i ) {
			ws.append(1, (wchar_t)i);
			d = i;
			d = d/sixteen;
			if(modf(d, &dd)==zero) {
				ws.append(L"\n");
			}
		}
		return cnv.fromW(ws);
	}

	void append_set(string& xml, const string& set, const string& utf8_enc) {
		xml.append("\n<set name=\"" + set + "\"><![CDATA[");
		xml.append(utf8_enc);
		xml.append("]]>");
		xml.append("</set>");
	}
}

int utf8_conv_test() {
	printf("sizeof(wchar_t)=%i\n", sizeof(wchar_t));
	Utf8Conv cnv;

	assert(cnv.fromW(L"").size() == 0);
	string sw("åäö");
	string utf8 = cnv.from(iso8859, sw);
	string sw_(cnv.to(iso8859, utf8));
	assert(sw_==sw);

	string sp("Fernández");
	utf8 = cnv.from(iso8859, sp);
	string sp_(cnv.to(iso8859, utf8));
	assert(sp_==sp);

	string nl("Tomeš");
	utf8 = cnv.from(windows, nl);
	string nl_(cnv.to(windows, utf8));
	assert(sp_==sp);
	nl_ = cnv.toNative_(utf8);

	wstring wludmila(1, 0x0141);
	wludmila.append(L"udmila");
	string ludmila_utf8 = cnv.fromW(wludmila);

	const string lextA_utf8 = unicode_to_utf8(0x0100, 0x017F, cnv);

	                 
	string tmp = cnv.toNative_(lextA_utf8);

	           
	string invalid = cnv.fromW(cnv.toW("åäö"));

	string aao = cnv.fromNative_("åäö");

	const string arabic_utf8 = unicode_to_utf8(0x0600, 0x06FF, cnv);
	cnv.toW(arabic_utf8);

	const string cjk_radicals_utf8 = unicode_to_utf8(0x4E00, 0x9FFF, cnv);

	const string bmp_utf8 = unicode_to_utf8(0x0020, 0xFFFD, cnv);

	string xml("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	xml.append("\n<unicode>");

	append_set(xml, "LatinExtendedA", lextA_utf8);
	append_set(xml, "Arabic", arabic_utf8);
	append_set(xml, "CJK_Radicals", cjk_radicals_utf8);
	append_set(xml, "BMP", bmp_utf8);

	xml.append("\n</unicode>");

	FILE* f = fopen("unicode.xml", "wb+");
	if ( f == 0 ) return 1;
	fwrite(xml.c_str(), xml.size(), 1, f);

	return 0;
}


int utf8_factory_test() {
	try {
		Utf8Conv* cnv_local = Utf8Conv::createEncoderFor("");
		if ( cnv_local == 0 ) return 0;
		string local("åäö");
		string utf8 = cnv_local->from(local);
		assert(cnv_local->to(utf8)==local);

	}
	catch( const CrException& ex ) {
		cerr << ex.GetErrorString() << endl;
		return 1;
	}

	return 0;
}

