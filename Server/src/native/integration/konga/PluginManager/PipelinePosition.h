/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#ifndef KONGA_PIPELINE_POSITION_H
#define KONGA_PIPELINE_POSITION_H

#include <string>

#include "PluginManagerApi.h"

namespace konga {

	enum PipelinePositionId {
		PIPELINE_POSITION_PRE = 0,
		PIPELINE_POSITION_ON_REQUEST = 0,
		PIPELINE_POSITION_POST = 1,
		PIPELINE_POSITION_ON_RESPONSE = 1,
		PIPELINE_POSTITION_INSIDE = 2
	};

	                                                  
	class PLUGIN_MANAGER_API PipelinePosition {
	public:

		                                                       
		                               
		  
		static int GetRelativePositionId(const std::string& sRelativePosition, int iEntityTypeId);
		static std::string GetRelativePosition(int iRelativePositionId, int iEntityTypeId);

		                                                                          
		  
		static int GetEntityTypeId(const std::string& sPipelineEntityName);
		static std::string GetEntityTypeName(int iEntityTypeId);

		PipelinePosition(void) : m_bOptional(true), m_iEntityType(-1),
			m_iRelativePosition(-1), m_lExecutionOrder(-1) {}

		PipelinePosition(const std::string& sEntityName, const std::string& sRelativePosition,
			long lExecutionOrder, bool bOptional) : m_bOptional(bOptional),
			m_iEntityType(GetEntityTypeId(sEntityName)),
			m_iRelativePosition(GetRelativePositionId(sRelativePosition, m_iEntityType)),
			m_lExecutionOrder(lExecutionOrder)
		{
		}

		PipelinePosition(int iEntityTypeId, int iRelativePositionId,
			long lExecutionOrder, bool bOptional) : m_bOptional(bOptional),
			m_iEntityType(iEntityTypeId),
			m_iRelativePosition(iRelativePositionId),
			m_lExecutionOrder(lExecutionOrder) {}

		std::string GetRelativePositionName() const;

		std::string GetEntityTypeName() const;

		int GetEntityTypeId() const {
			return m_iEntityType;
		}

		int GetRelativePositionId() const {
			return m_iRelativePosition;
		}

		long getExecutionOrder() const {
			return m_lExecutionOrder;
		}

		bool isOptional() const {
			return m_bOptional;
		}

		~PipelinePosition(void) {}

	private:
		void SetRelativePosition(const std::string& sRelativePosition);

		bool m_bOptional;
		int m_iEntityType;
		int m_iRelativePosition;
		long m_lExecutionOrder;
	};

}

#endif                             

