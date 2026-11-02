/*
 * Copyright (c) 1998-2006 Jitterbit, Inc.
 *
 */

                                                                                         
                                                                                                   
                                                                                       
                                                                                           
                                                                                         
                                             

#ifdef WIN32
	#ifdef TRANSFORMATIONENGINE_EXPORTS
		#define TRANSFORMATIONENGINE_API __declspec(dllexport)
	#else
		#define TRANSFORMATIONENGINE_API __declspec(dllimport)
	#endif
#else
	#define TRANSFORMATIONENGINE_API
#endif
