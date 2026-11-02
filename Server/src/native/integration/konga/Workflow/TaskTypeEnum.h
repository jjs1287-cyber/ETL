/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef TASK_TYPE_ENUM
#define TASK_TYPE_ENUM

namespace konga {

enum TaskTypeTag { ttREGULAR=0, ttINITIAL=1, ttTERMINATING=2, ttNOTIFICATION=4,
					 ttMACHINE_SPECIFIC=8, ttCR_UPDATE=16, ttDEBUG=32, ttESCALATION }; 

enum ProcessTypeTag { ptREGULAR=0, ptSYSTEM=1, ptAUTO_RERUN=2, ptDEBUG=32 }; 

}                   

#endif                  

