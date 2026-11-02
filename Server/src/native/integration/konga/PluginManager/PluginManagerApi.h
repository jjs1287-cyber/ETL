/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

#ifndef KONGA_FILTER_MANAGER_DLL_API
#define KONGA_FILTER_MANAGER_DLL_API

#ifdef WIN32
	#ifdef FILTERMANAGER_EXPORTS
		#define PLUGIN_MANAGER_API __declspec(dllexport)
	#else
		#define PLUGIN_MANAGER_API __declspec(dllimport)
	#endif
#else
	#define PLUGIN_MANAGER_API
#endif


#endif                                

