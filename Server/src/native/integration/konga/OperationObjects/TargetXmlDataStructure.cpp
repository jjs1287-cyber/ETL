/*
 * Copyright (c) 1998-2010 Jitterbit, Inc.
 *
 */
#include "OperationEngineContext.h"
#include "TargetXmlDataStructure.h"

using namespace std;
using namespace konga;

void TargetXmlDataStructure::doPreTransformation(const OperationEngineContext& context) const {
	if ( !getSchemaUrl().empty() ) {
		return;                                                     
	}

	CopyTargetSchemaFilesToTmpDir(context.getLocalTmpDirForOperation(), context.getTranDbConnection());
}

