/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#ifndef KONGA_JAVA_LAUNCH_DETAILS_H
#define KONGA_JAVA_LAUNCH_DETAILS_H

namespace konga {

struct JavaLaunchDetails {
	std::string sProcessPath, sArgs, sWorkingDir, sStdOutPath, sStdErrPath;
	unsigned long lPort;
};

}                   

#endif                               
