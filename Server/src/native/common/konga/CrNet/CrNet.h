/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifdef WIN32
#define WIN32_LEAN_AND_MEAN		                                                 

#include <windows.h>
#include <Wtypes.h>
#include <lm.h>

                                                                                         
                                                                                    
                                                                                       
                                                                                           
                                                                                 
                                             
#ifdef CRNET_EXPORTS
#define CRNET_API __declspec(dllexport)
#else
#define CRNET_API __declspec(dllimport)
#endif

  
                                                    
                                                      
  
extern "C" CRNET_API long __stdcall CreateSession(wchar_t* RemoteComputer, wchar_t* Domain, wchar_t* User, wchar_t* Password);

extern "C" CRNET_API long __stdcall DestroySession(wchar_t* RemoteComputer, int iUseForce);
#else

#endif
