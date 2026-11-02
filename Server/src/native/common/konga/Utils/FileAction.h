/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef KONGA_UTILS_FILE_ACTION_H
#define KONGA_UTILS_FILE_ACTION_H

#include <string>

namespace konga {

	                                     
	class FileAction {
	public:
		FileAction(void) {}
		virtual ~FileAction(void) {}

		                           
		virtual void doAction(const std::string& stFilePath) = 0;
	};
}

#endif                             

