/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */

#include "KongaStackWalker.h"

using namespace konga;
using namespace std;

void KongaStackWalker::OnCallstackEntry(CallstackEntryType eType, CallstackEntry &entry) {
	if( eType == StackWalker::firstEntry )
		m_str << "Frame\tModule\tFunction\tLine number\tSource file\tModule file name" << std::endl;
	m_str << iFrame++ << ":\t" << entry.moduleName << '\t' << entry.undFullName << 
		"\t (line " << entry.lineNumber << ") " << entry.lineFileName << '\t' << entry.loadedImageName << std::endl;
}
