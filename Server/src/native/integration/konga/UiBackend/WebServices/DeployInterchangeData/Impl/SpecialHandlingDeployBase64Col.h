/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_SPECIAL_HANDLING_DEPLOY_TRANSFORMATION_DEFINITIONS_H
#define KONGA_WS_SPECIAL_HANDLING_DEPLOY_TRANSFORMATION_DEFINITIONS_H

#include <string>

#include "DeployUtils.h"	           

#include "SpecialHandlingDeploy.h"

namespace konga {

namespace interchange_deploy {

	class SpecialHandlingDeployBase64Col : public SpecialHandlingDeploy {
	public:

		SpecialHandlingDeployBase64Col(int colpos);

		virtual ~SpecialHandlingDeployBase64Col() {}

		virtual bool HandleRow(InterchangeDataRowStruct& row);

	private:
		const int m_iColPos;
	};

}                   
}                                

#endif                                                                 

