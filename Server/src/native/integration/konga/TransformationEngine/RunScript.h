/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */
#ifndef TRANSFORMATION_ENGINE_RUN_SCRIPT_IMPL_H
#define TRANSFORMATION_ENGINE_RUN_SCRIPT_IMPL_H

#include <string>

#include "TransformationEngineApi.h"	                           

class TreeMapper;
class CTransform;

namespace konga {

	class DataElement;
	class ScriptCache;
	class OperationDataElementHolder;

	int incrementScriptNestingLevel();
	int decrementScriptNestingLevel();
	void setScriptNestingLevel(int i);
	int getMaxNestingLevel(const OperationDataElementHolder& deHolder);

	DataElement* RunScript(const std::string& stScriptGuid, ScriptCache& scriptCache, TreeMapper* pTreeMapper, CTransform* pTrans);
	TRANSFORMATIONENGINE_API DataElement* RunScript(long scriptId, ScriptCache& scriptCache, TreeMapper* pTreeMapper, CTransform* pTrans);
}

#endif                                           

