/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WIN_ERROR_UTILS_H
#define KONGA_WIN_ERROR_UTILS_H

#include <ostream>
#include <string>

#define USE_EXCEPTIONHANDLER 1;

#if defined(WIN32) && defined(USE_EXCEPTIONHANDLER)

#include <sstream>

#include <boost/thread/tss.hpp>				                       

#include <windows.h>

namespace konga {
	LONG WINAPI StderrExceptionHandler(PEXCEPTION_POINTERS pExp);
	std::string prettyPrintExceptionInfo(EXCEPTION_POINTERS* pExp);
}                   

#endif         

namespace konga {
	void installExceptionHandler();

#if defined(WIN32) && defined(USE_EXCEPTIONHANDLER)
	class KongaExceptionHandler {
	public:
		KongaExceptionHandler(std::ostream& output, const std::string& sCustomHeader = "", int handlerAction = EXCEPTION_CONTINUE_SEARCH);
		KongaExceptionHandler(const std::string& sOutputFileName, const std::string& sCustomHeader = "",
			int openMode = std::ios::app, int handlerAction = EXCEPTION_CONTINUE_SEARCH);
	private:

		struct OutputOptions {
			OutputOptions(std::ostream* poutput_, const std::string& sOutputFileName_, const std::string& sCustomHeader_, int openMode_, int handlerAction_) :
				poutput(poutput_), sOutputFileName(sOutputFileName_), sCustomHeader(sCustomHeader_), openMode(openMode_), handlerAction(handlerAction_) {}

			std::ostream* poutput;
			std::string sCustomHeader, sOutputFileName;
			int openMode, handlerAction;
		};
		static LONG WINAPI ExceptionHandler(PEXCEPTION_POINTERS pExp);
		static 	boost::thread_specific_ptr<OutputOptions> tls_pOutputOpt;
	};
#else
	#if !defined(EXCEPTION_EXECUTE_HANDLER)
	#define EXCEPTION_EXECUTE_HANDLER       1
	#define EXCEPTION_CONTINUE_SEARCH       0
	#define EXCEPTION_CONTINUE_EXECUTION    -1
	#endif

	class KongaExceptionHandler {
	public:
		KongaExceptionHandler(std::ostream& output, const std::string& sCustomHeader = "",
			int openMode = std::ios::app, int handlerAction = EXCEPTION_CONTINUE_EXECUTION) {}
		KongaExceptionHandler(const std::string& sOutputFileName, const std::string& sCustomHeader = "",
			int openMode = std::ios::app, int handlerAction = EXCEPTION_CONTINUE_SEARCH) {}
	};
#endif
}                   

#endif                           
