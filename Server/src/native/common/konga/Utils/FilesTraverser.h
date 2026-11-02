/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef KONGA_UTILS_FILE_TRAVERSER_H
#define KONGA_UTILS_FILE_TRAVERSER_H

#include <string>

#include "FileAction.h"
#include "Matcher.h"

namespace konga {

	                                                                               
	class FilesTraverser {
	public:
		FilesTraverser(FileAction& action, const Matcher& matcher)
			: m_action(action), m_matcher(matcher) {}
		virtual ~FilesTraverser(void) {}

		                                                              
		                               
		void traverseDirectory(const std::string& sDirectory, bool bRecurseSubDirs);

	private:
		FileAction& m_action;
		const Matcher& m_matcher;
	};
}

#endif                                
