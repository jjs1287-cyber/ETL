/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef UI_ENUMS_H
#define UI_ENUMS_H

typedef                        
enum __MIDL___MIDL_itf_CommerceRoute_0000_0001
    {	ktTaskInstance	= 1,
	ktProcessInstance	= 2
    } 	KillTask;

typedef                        
enum __MIDL___MIDL_itf_CommerceRoute_0000_0002
    {	esNO_ERROR	= 0,
	esMACHINE_SPECIFIC_MSG_ERROR	= 1,
	esWAITING	= 2,
	esNOT_READY	= 4,
	esRESOURCE_DISTRIBUTION_JOBS_ONLY	= 8
    } 	ErrorStatusEnum;

typedef                        
enum __MIDL___MIDL_itf_CommerceRoute_0000_0003
    {	rsUNDEFINED	= 0,
	rsSTOPPED	= 1,
	rsRUNNING	= 2,
	rsSTOPPING	= 4,
	rsERROR	= 8,
	rsSINGLE_THREAD	= 16,
	rsRESOURCE_DISTRIBUTION	= 32,
	rsRESOURCE_DISTRIBUTION_ERROR	= 64,
	rsDISABLED	= 128,
	rsUNREACHABLE = 256
    } 	RunningStatusEnum;

#endif              

