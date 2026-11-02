/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_WS_SPECIAL_HANDLING_DEPLOY_TRANSFORMATION_H
#define KONGA_WS_SPECIAL_HANDLING_DEPLOY_TRANSFORMATION_H

#include <string>

#include "DeployUtils.h"	           

#include "SpecialHandlingDeploy.h"

namespace konga {

	class DbConnection;
	class GuidToIdMapper;

namespace interchange_deploy {

	  
	                                                          
	                                          
	  
	                                                                                                     
	  
	class SpecialHandlingTransformation : public SpecialHandlingDeploy {
	public:

		SpecialHandlingTransformation(const vColPosT& vDocumentColPosToHandle,
									  const vColPosT& vSourceTargetIdColPos,
                                      GuidToIdMapper& guidToIdMapper) :
		m_vDocumentColPosToHandle(vDocumentColPosToHandle),
		m_vSourceTargetIdColPos(vSourceTargetIdColPos),
		m_guidToIdMapper(guidToIdMapper)
		{}

		virtual ~SpecialHandlingTransformation() {}

		virtual bool HandleRow(InterchangeDataRowStruct& row);

	private:
		const vColPosT& m_vDocumentColPosToHandle;
		const vColPosT& m_vSourceTargetIdColPos;
		GuidToIdMapper& m_guidToIdMapper;
	};

                                                                             
                                                                             

}                   
}                                

#endif                                                     

