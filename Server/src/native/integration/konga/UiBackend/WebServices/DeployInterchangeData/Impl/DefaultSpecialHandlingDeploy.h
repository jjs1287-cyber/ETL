/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_DEPLOY_DEFAULTSPECIALHANDLINGDEPLOY_H
#define KONGA_DEPLOY_DEFAULTSPECIALHANDLINGDEPLOY_H

#include "SpecialHandlingDeploy.h"

namespace konga {
namespace interchange_deploy {

	class DefaultSpecialHandlingDeployInsert : public SpecialHandlingDeploy {
	public:
		DefaultSpecialHandlingDeployInsert(void) {}
		~DefaultSpecialHandlingDeployInsert(void) {}

		bool HandleRow(InterchangeDataRowStruct& row) {
			return false;
		}

	};

	class DefaultSpecialHandlingDeployUpdate : public SpecialHandlingDeploy {
	public:
		DefaultSpecialHandlingDeployUpdate(void) {}
		~DefaultSpecialHandlingDeployUpdate(void) {}

		bool HandleRow(InterchangeDataRowStruct& row) {
			return false;
		}

	};

}                   
}                                

#endif                                               

