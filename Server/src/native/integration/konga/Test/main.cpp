/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include <iostream>
#include <fstream>
#include <sstream>
#include <string>

#include <konga/Utils/MemUtils.h>

#include <konga/WinUtils/KongaStackWalker.h>
#include <konga/WinUtils/WinErrorUtils.h>

using namespace konga;
using namespace std;

LONG WINAPI ExceptFilter(PEXCEPTION_POINTERS pExp, DWORD dwExpCode)
{
	KongaStackWalker sw;
	cout << prettyPrintExceptionInfo(pExp);
	sw.ShowCallstack(GetCurrentThread(), pExp->ContextRecord);
	cout << sw.toString();
	return EXCEPTION_EXECUTE_HANDLER;
}

int f0();
int f1();
int f2();
int f3();

int f1() {
	*(int*)0=0;        
	  
                     
                                      
                     
                                                       
   
	return 1;
}


int f0() { 
	const int buflen = 20000;
	char buf[buflen];
	memset(buf,0,buflen);
	if(buf[0]>1)return f2();
	return f3();
}
int f2() { return f0(); }
int f3() { 
	if( !GetCallsLeft() ) return 0;
	return f2();
}

#include <konga/Xml/XmlSchemaValidator.h>
int validateXml(){
	std::string xmlFN="Z:/tmp/etlforge/OpId_289_7ff27684-0768-4e8d-9505-9a75d5b67a0a/1_CompanyForChuckTest.xml_t526.target";                                                                                     
	std::string xsdFN="Z:/tmp/etlforge/OpId_289_7ff27684-0768-4e8d-9505-9a75d5b67a0a/CompanyForChuckTest.xsd";                                                                                                                                    
	std::string ns="www.etlforge.invalid/fengtest1";                                   

	konga::xml::XmlSchemaValidator validator;
	std::string err;
	int ret=validator.ValidateXmlInstance(xmlFN, xsdFN, ns, err);
	printf("%s\n",err.c_str());
	return ret;
}

void printHead(){
	std::string filename("Z:\\tmp\\etlforge\\OpId_289_e0752b79-a59b-4778-9f03-ea29f833d6bd\\1_CompanyForChuckTest.xml_t526.target");
	FILE*in=fopen(filename.c_str(),"rb");
	size_t size=3000;
	while(size--)putchar(fgetc(in));
}

void testFseek(){
	FILE*out=fopen("C:\\Users\\Frank Chu\\Documents\\test1.txt","wb+");
	fprintf(out,"line0\n");
	fprintf(out,"line1\n");
	fprintf(out,"line2\n");
	fprintf(out,"line3\n");
	fprintf(out,"line4\n");
	size_t pos=ftell(out);
	fprintf(out,"line5\n");
	fprintf(out,"line6\n");
	fprintf(out,"line7\n");
	fprintf(out,"line8\n");
	fprintf(out,"line9\n");
	fprintf(out,"line10\n");
	fseek(out,pos,SEEK_SET);
	fputs("add new line\n",out);
	fclose(out);
}

int main() {
	                                                       

	                         
	KongaExceptionHandler eh(cout);

	                         
	                                                     
	                                     

	                         
	                                                  

	       
	                        
	testFseek();

	return 0;
}

int main1() {
	__try
	{
		f3();
	}
	__except (ExceptFilter(GetExceptionInformation(), GetExceptionCode()))
	{
	}
	return 0;
}
