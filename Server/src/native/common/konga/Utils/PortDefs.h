/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */
#ifndef PORTABLE_DEFINITIONS_H
#define PORTABLE_DEFINITIONS_H

                                        
  
#ifdef WIN32
#include <konga/Utils/PlatformSpecific/Win32/PortDefs.h>
#else
#include <konga/Utils/PlatformSpecific/Unix/PortDefs.h>
#endif

#endif                          

