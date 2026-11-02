/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef GLOBALS_H
#define GLOBALS_H

#include <string>

#ifdef WIN32
	#include <windows.h>
#endif         

#include "Engine/EngineCore.h"			                   
#include "Engine/Messenger/msgrCore.h"	                  

extern EngineCore	*g_engine;
                           

extern std::string g_status_str;	                          
extern std::string g_stEventSource;

extern int g_max_threads;

#endif             

