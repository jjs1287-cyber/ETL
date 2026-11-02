/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */

#include <konga/Utils/StringUtils.h>
#include <konga/Utils/CrException.h>

#include <konga/DbRelations/EntityIdConstants.h>
#include <konga/DbRelations/DatabaseTableNameMappings.h>

#include "PipelinePosition.h"

using namespace konga;
using std::string;

namespace {
	const string pre("Pre");
	const string post("Post");
	const string onRequest("OnRequest");
	const string onResponse("OnResponse");
	const string inside("Inside");
}

int PipelinePosition::GetRelativePositionId(const string& sRelativePosition, int iEntityTypeId) {
	const string& sEntityTypeName = GetEntityNameByEntityId(iEntityTypeId);
	switch( iEntityTypeId ) {
		case SourceEntityId:
		case TargetEntityId:
			if ( EqualsIgnoreCase(sRelativePosition, pre) )
				return PIPELINE_POSITION_PRE;
			else if ( EqualsIgnoreCase(sRelativePosition, post) )
				return PIPELINE_POSITION_POST;
			throw CrException("The relative position \"" + sRelativePosition
			+ "\" is not defined for entity type \"" + sEntityTypeName + "\".");
		case WebServiceEntityId:
			if ( EqualsIgnoreCase(sRelativePosition, onRequest) )
				return PIPELINE_POSITION_ON_REQUEST;
			else if ( EqualsIgnoreCase(sRelativePosition, onResponse) )
				return PIPELINE_POSITION_ON_RESPONSE;
			throw CrException("The relative position \"" + sRelativePosition
				+ "\" is not defined for a web service call.");
		case ScriptEntityId:
			if ( EqualsIgnoreCase(sRelativePosition, inside) )
				return PIPELINE_POSTITION_INSIDE;
			else {
				throw CrException("Invalid relative position for Scripts: " + sRelativePosition);
			}
		default:
			throw CrException("Invalid relative position type: Entity type \"" + sEntityTypeName
				+ "\" does not define relative positions.");
	};
}

std::string PipelinePosition::GetRelativePosition(int iRelativePositionId, int iEntityTypeId) {
	const string& sEntityTypeName = GetEntityNameByEntityId(iEntityTypeId);
	switch(iEntityTypeId) {
		case SourceEntityId:
		case TargetEntityId:
			switch(iRelativePositionId) {
				case PIPELINE_POSITION_PRE:
					return pre;
				case PIPELINE_POSITION_POST:
					return post;
				default:
					throw CrException("The relative position id "
						+ toString(iRelativePositionId)
						+ " is not defined for entity \"" + sEntityTypeName + "\".");
			};
			break;
		case WebServiceEntityId:
			switch(iRelativePositionId) {
				case PIPELINE_POSITION_ON_REQUEST:
					return onRequest;
				case PIPELINE_POSITION_ON_RESPONSE:
					return onResponse;
				default:
					throw CrException("The relative position id "
						+ toString(iRelativePositionId)
						+ " is not defined for entity \"" + sEntityTypeName + "\".");
			};
			break;
		case ScriptEntityId:
			switch(iRelativePositionId) {
				case PIPELINE_POSTITION_INSIDE:
					return inside;
				default:
					throw CrException("The relative position id "
						+ toString(iRelativePositionId)
						+ " is not defined for entity \"" + sEntityTypeName + "\".");
			};
			break;
		default:
			throw CrException("Internal Error: Entity type \"" + sEntityTypeName
				+ "\" does not define relative positions.");
	};
}

namespace {
	const string source("Source");
	const string target("Target");
	const string webService("WebServiceCall");
	const string script("Script");
}

int PipelinePosition::GetEntityTypeId(const std::string& sPipelineEntityName) {
	if ( EqualsIgnoreCase(sPipelineEntityName, source) )
		return SourceEntityId;
	else if ( EqualsIgnoreCase(sPipelineEntityName, target) )
		return TargetEntityId;
	else if ( EqualsIgnoreCase(sPipelineEntityName, webService) )
		return WebServiceEntityId;
	else if ( EqualsIgnoreCase(sPipelineEntityName, script) )
		return ScriptEntityId;
	
	throw CrException("The entity \"" + sPipelineEntityName + "\" does not define a pipeline position.");
}

std::string PipelinePosition::GetEntityTypeName(int iEntityTypeId) {
	switch(iEntityTypeId) {
		case SourceEntityId:
			return source;
		case TargetEntityId:
			return target;
		case WebServiceEntityId:
			return webService;
		case ScriptEntityId:
			return script;
		default:
			throw CrException("The entity \"" + GetEntityNameByEntityId(iEntityTypeId)
				+ "\" does not define a pipeline position.");
	};
}

void PipelinePosition::SetRelativePosition(const std::string& sRelativePosition) {
	m_iRelativePosition = GetRelativePositionId(sRelativePosition, m_iEntityType);
}

std::string PipelinePosition::GetRelativePositionName() const {
	return GetRelativePosition(m_iRelativePosition, m_iEntityType);
}

std::string PipelinePosition::GetEntityTypeName() const {
	return GetEntityTypeName(m_iEntityType);
}
