/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
                                                 
  
                                                                      

#ifndef CR_BINDPARAM_H
#define CR_BINDPARAM_H

#ifdef DS_USE_SP

#include <konga/Db/DbUtils/DynamicDbRecordSet.h>

#include "SpArgs.h"

class CBindParam : public DynamicDbRecordSet  
{
public:
	CBindParam(int numFields, int numArgs, spArgs* argList, const SmartDbPtr<DbConnection>& pDatabase);
	virtual ~CBindParam();

	  
                                                     

                                                                    
                                                             
   

private:
	int m_numFields;
	int m_numParams;
	spArgs* m_argList;

};

#endif             

#endif                  

