/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifdef WIN32
	                                                                                         
	                                                                                           
	                                                                                       
	                                                                                           
	                                                                                         
	                                             
	#ifdef MOD_KONGA_WF_EXPORTS
		#define MOD_KONGA_WF_API __declspec(dllexport)
	#else
		#define MOD_KONGA_WF_API __declspec(dllimport)
	#endif
#else       
	#ifdef MOD_KONGA_WF_EXPORTS
		#define MOD_KONGA_WF_API
	#else
		#define MOD_KONGA_WF_API
	#endif
#endif

class Cmod_konga_wf {
public:
	Cmod_konga_wf(void) {}
};

                                             
                                             
