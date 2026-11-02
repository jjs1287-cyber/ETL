/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#include "WinErrorUtils.h"

#if defined(WIN32) && defined(USE_EXCEPTIONHANDLER)
#include <cassert>
#include <fstream>
#include <iostream>
#include <ostream>

#include <windows.h>
#include <time.h>
#include <Dbghelp.h>

#include <konga/SysInfo/SysInfo.h>	                           
#include <konga/Utils/FileUtils.h>	                   
#include <konga/Utils/StringUtils.h>             

#include "KongaStackWalker.h"

#pragma comment( lib, "dbghelp.lib" )

using namespace konga;
using namespace std;

              
LONG WINAPI konga::StderrExceptionHandler(PEXCEPTION_POINTERS pExp) {
	KongaStackWalker sw;
	cerr << prettyPrintExceptionInfo(pExp);
	sw.ShowCallstack(GetCurrentThread(), pExp->ContextRecord);
	cerr << sw.toString();
	return EXCEPTION_CONTINUE_SEARCH;
}

namespace {
	void printHexAndDec(stringstream& str, DWORD64 dw, const string& title) {
		str << title << ": 0x" << std::hex << dw << " (" << std::dec << dw << ")" << std::endl;
	}
}

namespace {
string simpleStackTrace(DWORD ebp) {
#if 0                                                     
	long*p=(long*)ebp;
	stringstream str;
	do {
		if( !p )
			break;
		str.setf(ios::hex);
		str << p << " : " << *p << "\tdiff: " << ((long*)*p-p) << endl;
		if( IsBadReadPtr((long*)*p, 4) )
			break;
		p = (long*)*p;
	} while(p);
	return str.str();
#endif     
	return "";
}
}

std::string konga::prettyPrintExceptionInfo(EXCEPTION_POINTERS* pExp) {
	PCONTEXT pc = pExp->ContextRecord;
	time_t t;
	time(&t);
	std::stringstream str;
	str << "===========================================================================================================" << std::endl;
	str << "An error occurred. The following information may be of help to the support team in investigating the issue." << std::endl;
	str << GetPrettySysInfoString() << endl;
	str << GetPrettyOsString() << endl;
	str << "Time: " << ctime(&t) << endl;
	str << "Exception: 0x" << std::hex << pExp->ExceptionRecord->ExceptionCode << ", Flags: 0x" << pExp->ExceptionRecord->ExceptionFlags << std::endl;
	str << "Registers:" << std::endl;
	                                                   
	printHexAndDec(str, pc->Rax, "RAX");
	printHexAndDec(str, pc->Rbx, "RBX");
	printHexAndDec(str, pc->Rcx, "RCX");
	printHexAndDec(str, pc->Rdx, "RDX");
	printHexAndDec(str, pc->Rsi, "RSI");
	printHexAndDec(str, pc->Rdi, "RDI");
	printHexAndDec(str, pc->Rsp, "RSP");
	printHexAndDec(str, pc->Rbp, "RBP");
	printHexAndDec(str, pc->Rip, "RIP");
	printHexAndDec(str, pc->R8, "R8");
	printHexAndDec(str, pc->R9, "R9");
	printHexAndDec(str, pc->R10, "R10");
	printHexAndDec(str, pc->R11, "R11");
	printHexAndDec(str, pc->R12, "R12");
	printHexAndDec(str, pc->R13, "R13");
	printHexAndDec(str, pc->R14, "R14");
	printHexAndDec(str, pc->R15, "R15");
	                                                                 
	return str.str();
}
#endif         

void konga::installExceptionHandler() { 
	#if defined(WIN32) && defined(USE_EXCEPTIONHANDLER)
	SetUnhandledExceptionFilter(StderrExceptionHandler);
	#endif
}

#if defined(WIN32) && defined(USE_EXCEPTIONHANDLER)
boost::thread_specific_ptr<KongaExceptionHandler::OutputOptions> KongaExceptionHandler::tls_pOutputOpt;

LONG WINAPI konga::KongaExceptionHandler::ExceptionHandler(PEXCEPTION_POINTERS pExp) {
	assert(tls_pOutputOpt.get() != 0);

	if( tls_pOutputOpt.get() == 0)
		tls_pOutputOpt.reset(new OutputOptions(&cerr, "", "", ios::app, EXCEPTION_CONTINUE_EXECUTION));

	OutputOptions* popt = tls_pOutputOpt.get();
	ostream* o;
	bool bOwnage = false;
	if( popt->poutput ) {                            
		o = popt->poutput;
	} else if( !popt->sOutputFileName.empty() ) {                              
		bOwnage = true;
		o = new fstream(popt->sOutputFileName.c_str(), popt->openMode);
		if( o->fail() ) {
			bOwnage = false;
			delete o;
			o = &cerr;
		}
	} else {                                                                  
		o = &cerr;
	}

	if( !popt->sCustomHeader.empty() )
		*o << popt->sCustomHeader << endl;
	*o << prettyPrintExceptionInfo(pExp) << endl;

	KongaStackWalker sw;
	sw.ShowCallstack(GetCurrentThread(), pExp->ContextRecord);
	*o << sw.toString() << endl;

	string sMiniDumpFileName = getTmpFileName("ETLForge-pid-"+toString(GetCurrentProcessId())+"-") + ".mdmp";
	HANDLE hFile = CreateFile(sMiniDumpFileName.c_str(), GENERIC_READ | GENERIC_WRITE, 0, 0, CREATE_ALWAYS, FILE_ATTRIBUTE_NORMAL, 0); 
	if( hFile &&  hFile != INVALID_HANDLE_VALUE ) {
		MINIDUMP_TYPE mdt = MiniDumpNormal;
		MINIDUMP_EXCEPTION_INFORMATION mexp;
		memset(&mexp, 0, sizeof mexp);
		mexp.ExceptionPointers = pExp;
		mexp.ThreadId = GetCurrentThreadId();
		if( MiniDumpWriteDump(GetCurrentProcess(), GetCurrentProcessId(), hFile, mdt, &mexp, 0, 0) )
			*o << "Wrote minidump to: " << sMiniDumpFileName << endl;
		else {
			long lErr = GetLastError();
			*o << "Failed to write minidump to '" << sMiniDumpFileName <<
				"'. Error number: " << lErr << endl;
		}
	} else {
		long lErr = GetLastError();
		*o << "Failed to open minidump file '" << sMiniDumpFileName <<
			"' for writing. Error number = " << lErr << endl;
	}
	*o << endl;

	o->flush();

	if( bOwnage )
		delete o;

	return popt->handlerAction;
}

KongaExceptionHandler::KongaExceptionHandler(std::ostream& output, const std::string& sCustomHeader, int handlerAction) {
	if( tls_pOutputOpt.get() == 0)
		tls_pOutputOpt.reset(new OutputOptions(&output, "", sCustomHeader, ios::app, handlerAction));
	SetUnhandledExceptionFilter(ExceptionHandler);
}

KongaExceptionHandler::KongaExceptionHandler(const std::string& sOutputFileName, const std::string& sCustomHeader, int openMode, int handlerAction) {
	if( tls_pOutputOpt.get() == 0)
	  tls_pOutputOpt.reset(new OutputOptions(0, sOutputFileName, sCustomHeader, openMode, handlerAction));
	SetUnhandledExceptionFilter(ExceptionHandler);
}

#endif         
