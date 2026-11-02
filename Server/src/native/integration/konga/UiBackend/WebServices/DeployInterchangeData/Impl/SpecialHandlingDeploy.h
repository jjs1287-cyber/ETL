/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_SPECIAL_HANDLING_DEPLOY_H
#define KONGA_WS_SPECIAL_HANDLING_DEPLOY_H

#include <string>

#include <boost/noncopyable.hpp>

#include "InterchangeDataStructs.h"

namespace konga {
namespace interchange_deploy {

	                                                                          
	class SpecialHandlingDeploy : private boost::noncopyable {
	public:
		SpecialHandlingDeploy() {}

		virtual ~SpecialHandlingDeploy(void) {}

		                                   
		                                                   
		virtual bool HandleRow(InterchangeDataRowStruct& row) = 0;

		                                                                     
		virtual bool HandleRowAfterInsert(long newId, InterchangeDataRowStruct& row) {
			return false;
		}

		                                                                    
		virtual bool HandleRowAfterUpdate(long id, InterchangeDataRowStruct& row) {
			return false;
		}

	};
}                   
}                                

#endif                                      

