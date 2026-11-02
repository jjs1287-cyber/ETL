/*
 * Copyright (c) 1998-2008 Jitterbit, Inc.
 *
 */
                                                                         


#include "TreeMapper.h"

#include <konga/TransformationLogging/ViewLog.h>
#include <konga/TransformationLogging/ErrorLog.h>


                                                                                                
                                                                               
unsigned long	TreeMapper::HandleTransformationFunction(int iFunctionID, 
												 const std::string& stInputDataFilename,
												 const std::string& stOutputDataFilename)
{
	PROFILING doPrf(this,"HandleTransformationFunction");
	                                                                                              
	                                               
	return CallWebService(iFunctionID,
						  stInputDataFilename,
						  stOutputDataFilename);
}
